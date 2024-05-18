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

networkName=cab_app_net_e2e
zookeeperContainer=zookeeper-e2e
kafkaContainer=kafkaserver-e2e
postgresContainer=postgres-e2e

cd ..

originalVolumes=$(docker volume ls --quiet)

mvn clean --file pom.xml
mvn install --file cab-app-common/pom.xml
mvn install --file cab-app-exception-handling-starter/pom.xml

initializeEnvironment() {

    docker network create $networkName

    docker run --detach \
        --name $zookeeperContainer \
        -p 22181:2181 \
        -e ZOOKEEPER_CLIENT_PORT=2181 \
        -e ZOOKEEPER_TICK_TIME=2000 \
        -e KAFKA_OPTS="-Dzookeeper.4lw.commands.whitelist=stat" \
        --health-cmd="echo stat | nc localhost 2181 | grep Mode" \
        --health-interval=10s \
        --health-timeout=5s \
        --health-retries=3 \
        --network $networkName \
        confluentinc/cp-zookeeper:7.4.4

    docker run --detach \
        --name $kafkaContainer \
        -p 29092:29092 \
        -e KAFKA_BROKER_ID=1 \
        -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
        -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
        -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://$kafkaContainer:9092,PLAINTEXT_HOST://localhost:29092 \
        -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT \
        -e KAFKA_ZOOKEEPER_CONNECT=$zookeeperContainer:2181 \
        --network $networkName \
        confluentinc/cp-kafka:7.4.4

    dbUsername=postgres
    dbPassword=root

    docker run --detach \
        --name $postgresContainer \
        -p 5432:5432 \
        -e POSTGRES_USER=$dbUsername \
        -e POSTGRES_PASSWORD=$dbPassword \
        --network $networkName \
        postgres:15-alpine

    sleep 10

    docker cp script/e2e_sql $postgresContainer:/home/e2e_sql/
    docker exec -i $postgresContainer psql -U $dbUsername -f /home/e2e_sql/add_dbs.sql

    passengerPort=8081
    driverPort=8082
    ridesPort=8083
    paymentPort=8084
    kafkaPort=29092
    eurekaPort=8070
    gatewayPort=8080
    eurekaHost=localhost
    kafkaHost=localhost
    activeProfile=dev
    dbUrlBase=jdbc:postgresql://localhost:5432

    tmpLogDiscovery=/tmp/e2e-local-discovery.log
    tmpLogGateway=/tmp/e2e-local-gateway.log
    tmpLogPassenger=/tmp/e2e-local-passenger.log
    tmpLogDriver=/tmp/e2e-local-driver.log
    tmpLogRides=/tmp/e2e-local-rides.log
    tmpLogPayment=/tmp/e2e-local-payment.log

    mvn spring-boot:run \
        -Dspring-boot.run.arguments="
            --spring.profiles.active=$activeProfile
            --server.port=$eurekaPort" \
        --file cab-app-discovery-server/pom.xml | tee $tmpLogDiscovery &
    echo -e "\033[0;35mStart discovery server on port:\033[0m $eurekaPort"

    while [[ "$(grep -E "Started CabAppDiscoveryServerApplication" $tmpLogDiscovery)" == "" ]]; do
        sleep 1
    done

    mvn spring-boot:run \
        -Dspring-boot.run.arguments="
            --EUREKA_HOST=$eurekaHost
            --EUREKA_PORT=$eurekaPort
            --spring.profiles.active=$activeProfile
            --server.port=$gatewayPort" \
        --file cab-app-api-gateway/pom.xml | tee $tmpLogGateway &
    echo -e "\033[0;35mStart api gateway on port:\033[0m $gatewayPort"

    while [[ "$(grep -E "Started CabAppApiGatewayApplication" $tmpLogGateway)" == "" ]]; do
        sleep 1
    done

    registeredServices=("passenger" "driver" "rides" "payment")

    mvn spring-boot:run \
        -Dspring-boot.run.arguments="
            --DB_URL=$dbUrlBase/passenger_service_db
            --DB_PASSWORD=$dbPassword
            --DB_USERNAME=$dbUsername
            --KAFKA_HOST=$kafkaHost
            --KAFKA_PORT=$kafkaPort
            --EUREKA_HOST=$eurekaHost
            --EUREKA_PORT=$eurekaPort
            --spring.profiles.active=$activeProfile
            --server.port=$passengerPort" \
        --file cab-app-passenger-service/pom.xml | tee $tmpLogPassenger &
    echo -e "\033[0;35mStart passenger service on port:\033[0m $passengerPort"
    mvn spring-boot:run \
        -DskipContracts=true \
        -Dspring-boot.run.arguments="
            --DB_URL=$dbUrlBase/driver_service_db
            --DB_PASSWORD=$dbPassword
            --DB_USERNAME=$dbUsername
            --spring.profiles.active=$activeProfile
            --KAFKA_HOST=$kafkaHost
            --KAFKA_PORT=$kafkaPort
            --EUREKA_HOST=$eurekaHost
            --EUREKA_PORT=$eurekaPort
            --server.port=$driverPort" \
        --file cab-app-driver-service/pom.xml | tee $tmpLogDriver &
    echo -e "\033[0;35mStart driver service on port:\033[0m $driverPort"
    mvn spring-boot:run \
        -DskipContracts=true \
        -Dspring-boot.run.arguments="
            --DB_URL=$dbUrlBase/rides_service_db
            --DB_PASSWORD=$dbPassword
            --DB_USERNAME=$dbUsername
            --spring.profiles.active=$activeProfile
            --KAFKA_HOST=$kafkaHost
            --KAFKA_PORT=$kafkaPort
            --EUREKA_HOST=$eurekaHost
            --EUREKA_PORT=$eurekaPort
            --server.port=$ridesPort" \
        --file cab-app-rides-service/pom.xml | tee $tmpLogRides &
    echo -e "\033[0;35mStart rides service on port:\033[0m $ridesPort"
    mvn spring-boot:run \
        -DskipContracts=true \
        -Dspring-boot.run.arguments="
            --DB_URL=$dbUrlBase/payment_service_db
            --DB_PASSWORD=$dbPassword
            --DB_USERNAME=$dbUsername
            --spring.profiles.active=$activeProfile
            --KAFKA_HOST=$kafkaHost
            --KAFKA_PORT=$kafkaPort
            --EUREKA_HOST=$eurekaHost
            --EUREKA_PORT=$eurekaPort
            --server.port=$paymentPort" \
        --file cab-app-payment-service/pom.xml | tee $tmpLogPayment &
    echo -e "\033[0;35mStart payment service on port:\033[0m $paymentPort"

    for spiedService in "${registeredServices[@]}"; do
        while [[ "$(grep -E "cab-app-$spiedService-service.+freshExecutor.+eureka/apps/(delta)?$" /tmp/e2e-local-"$spiedService".log)" == "" ]]; do
            sleep 5
        done
    done

    docker exec -i $postgresContainer psql -U $dbUsername -f /home/e2e_sql/fill_dbs.sql
}

killEnvironment() {
    pids=$(pgrep -f spring.profiles.active)

    if [ -n "$pids" ]; then
        for pid in $pids; do
            kill -9 "$pid"
        done
    fi

    docker stop $zookeeperContainer $kafkaContainer $postgresContainer
    docker rm $zookeeperContainer $kafkaContainer $postgresContainer
    docker network rm $networkName

    newVolumes=$(docker volume ls --quiet)

    for volume in $newVolumes; do
        if ! echo "$originalVolumes" | grep -q "$volume"; then
            docker volume rm "$volume"
        fi
    done
}

testedServices=()
testResults=()

for service in *"$selectedService"-service/; do
    initializeEnvironment

    mvn test -Dtest=CucumberRunnerE2E \
        -DskipContracts=true \
        -DserverPort=$gatewayPort \
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
