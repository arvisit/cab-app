#!/bin/bash

start=$SECONDS
startDate=$(date --rfc-3339=seconds)

availableServices=("passenger" "driver" "rides" "payment")

if [ "$#" -gt 1 ]; then
    echo "Zero or one parameter is allowed"
    exit 1
elif [ "$#" -eq 0 ]; then
    selectedService=""
else
    found=false
    for value in "${availableServices[@]}"; do
        if [ "$1" == "$value" ]; then
            found=true
            break
        fi
    done
    if $found; then
        selectedService=$1
    else
        echo "Parameters list should be blank or contain only one of the following values: " "${availableServices[@]}"
        exit 1
    fi
fi

dbPassengerContainer=db-passenger
dbDriverContainer=db-driver
dbRidesContainer=db-rides
dbPaymentContainer=db-payment

cd ..

mvn clean --file pom.xml
mvn install --file cab-app-common/pom.xml
mvn install --file cab-app-exception-handling-starter/pom.xml

initializeEnvironment() {
    tmpLog=/tmp/e2e-docker-compose-up.log

    docker compose up --build 2>&1 | tee $tmpLog &

    for spiedService in *-service/; do
        while [[ "$(grep -E "${spiedService::-1}.+freshExecutor.+eureka/apps/(delta)?$" $tmpLog)" == "" ]]; do
            sleep 5
        done
    done

    dbUsername=postgres

    docker cp script/e2e_sql/add_passengers.sql $dbPassengerContainer:/home/add_passengers.sql
    docker exec -i $dbPassengerContainer psql -U $dbUsername -f /home/add_passengers.sql

    docker cp script/e2e_sql/add_cars_drivers.sql $dbDriverContainer:/home/add_cars_drivers.sql
    docker exec -i $dbDriverContainer psql -U $dbUsername -f /home/add_cars_drivers.sql

    docker cp script/e2e_sql/add_promo_codes_rides.sql $dbRidesContainer:/home/add_promo_codes_rides.sql
    docker exec -i $dbRidesContainer psql -U $dbUsername -f /home/add_promo_codes_rides.sql

    docker cp script/e2e_sql/add_passenger_payments.sql $dbPaymentContainer:/home/add_passenger_payments.sql
    docker exec -i $dbPaymentContainer psql -U $dbUsername -f /home/add_passenger_payments.sql

    docker cp script/e2e_sql/add_driver_payments.sql $dbPaymentContainer:/home/add_driver_payments.sql
    docker exec -i $dbPaymentContainer psql -U $dbUsername -f /home/add_driver_payments.sql
}

killEnvironment() {
    docker compose down --volumes
}

testedServices=()
testResults=()

apiGatewayPort=8080

for service in *"$selectedService"-service/; do
    initializeEnvironment

    mvn test -Dtest=CucumberRunnerE2E \
        -DskipContracts=true \
        -DserverPort=$apiGatewayPort \
        --file "$service"pom.xml
    testResults+=($?)
    testedServices+=("${service::-1}")

    killEnvironment
done

duration=$(( SECONDS - start))
finishDate=$(date --rfc-3339=seconds)

secondsToHMS() {
    local sec="$1"
    local hours=$(( sec / 3600 ))
    local minutes=$(( (sec % 3600) / 60 ))
    local seconds=$(( sec % 60 ))
    
    if [ "$hours" -eq 0 ]; then
        if [ "$minutes" -eq 0 ]; then
            printf "%d s" $seconds
        else
            printf "%02d:%02d min" $minutes $seconds
        fi
    else
        printf "%02d:%02d:%02d h" $hours $minutes $seconds
    fi
}

echo "-----------------------------------------------------"
echo "REPORT:"
for ((i = 0; i < ${#testedServices[@]}; i++)); do
    if [ "${testResults[i]}" -eq 0 ]; then
        #green
        result="\033[0;32mSUCCESS\033[0m"
    else
        #red
        result="\033[0;31mFAILURE\033[0m"
    fi
    echo -e "${testedServices[i]}: $result"
done
echo "-----------------------------------------------------"
echo -e "Script was started at \033[0;36m$startDate\033[0m"
echo -e "Script was finished at \033[0;36m$finishDate\033[0m"
echo -e "Total time: \033[0;32m$(secondsToHMS duration)\033[0m"
echo "-----------------------------------------------------"

for result in "${testResults[@]}"; do
    if [ "$result" -ne 0 ]; then
        exit 1
    fi
done