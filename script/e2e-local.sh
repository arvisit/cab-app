#!/bin/bash

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

docker cp e2e_sql $postgresContainer:/home/e2e_sql/
docker exec -i $postgresContainer psql -U $dbUsername -f /home/e2e_sql/add_dbs.sql

cd ..

passengerPort=8081
driverPort=8082
ridesPort=8083
paymentPort=8084
activeProfile=e2e
dbUrlBase=jdbc:postgresql://localhost:5432

pids=()

mvn clean spring-boot:run \
    -Dspring-boot.run.arguments="
        --DB_URL=$dbUrlBase/passenger_service_db
        --DB_PASSWORD=$dbPassword
        --DB_USERNAME=$dbUsername
        --spring.profiles.active=$activeProfile
        --server.port=$passengerPort" \
    --file cab-app-passenger-service/pom.xml &
pids+=($!)
echo -e "\033[0;35mStart passenger service on port:\033[0m $passengerPort"
mvn clean spring-boot:run \
    -Dspring-boot.run.arguments="
        --DB_URL=$dbUrlBase/driver_service_db
        --DB_PASSWORD=$dbPassword
        --DB_USERNAME=$dbUsername
        --spring.profiles.active=$activeProfile
        --RIDES_SERVICE_PORT=$ridesPort
        --server.port=$driverPort" \
    --file cab-app-driver-service/pom.xml &
pids+=($!)
echo -e "\033[0;35mStart driver service on port:\033[0m $driverPort"
mvn clean spring-boot:run \
    -Dspring-boot.run.arguments="
        --DB_URL=$dbUrlBase/rides_service_db
        --DB_PASSWORD=$dbPassword
        --DB_USERNAME=$dbUsername
        --spring.profiles.active=$activeProfile
        --PASSENGER_SERVICE_PORT=$passengerPort
        --DRIVER_SERVICE_PORT=$driverPort
        --PAYMENT_SERVICE_PORT=$paymentPort
        --server.port=$ridesPort" \
    --file cab-app-rides-service/pom.xml &
pids+=($!)
echo -e "\033[0;35mStart rides service on port:\033[0m $ridesPort"
mvn clean spring-boot:run \
    -Dspring-boot.run.arguments="
        --DB_URL=$dbUrlBase/payment_service_db
        --DB_PASSWORD=$dbPassword
        --DB_USERNAME=$dbUsername
        --spring.profiles.active=$activeProfile
        --PASSENGER_SERVICE_PORT=$passengerPort
        --DRIVER_SERVICE_PORT=$driverPort
        --RIDES_SERVICE_PORT=$ridesPort
        --server.port=$paymentPort" \
    --file cab-app-payment-service/pom.xml &
pids+=($!)
echo -e "\033[0;35mStart payment service on port:\033[0m $paymentPort"

sleep 30

docker exec -i $postgresContainer psql -U $dbUsername -f /home/e2e_sql/fill_dbs.sql

testedServices=()
testResults=()

for service in *"$selectedService"-service/; do
    mvn test -Dtest=CucumberRunnerE2E \
        -DpassengerServerPort=$passengerPort \
        -DdriverServerPort=$driverPort \
        -DridesServerPort=$ridesPort \
        -DpaymentServerPort=$paymentPort \
        --file "$service"pom.xml
    testResults+=($?)
    testedServices+=("${service::-1}")
done

for pid in "${pids[@]}"; do
    kill "$pid"
done

docker stop $zookeeperContainer $kafkaContainer $postgresContainer
docker rm $zookeeperContainer $kafkaContainer $postgresContainer
docker network rm $networkName

echo "-----------------------------------"
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
echo "-----------------------------------"

for result in "${testResults[@]}"; do
    if [ "$result" -ne 0 ]; then
        exit 1
    fi
done