#!/bin/bash

start=$SECONDS
startDate=$(date --rfc-3339=seconds)

cd ..

mvn clean install -f cab-app-common/pom.xml
mvn clean install -f cab-app-exception-handling-starter/pom.xml

for service in *service/; do
    mvn clean spring-cloud-contract:convert --file "$service"pom.xml
    mvn spring-cloud-contract:generateStubs --file "$service"pom.xml

    artifactId=${service::-1}
    stubsJarFullPath=$(ls "$service"target/*-stubs.jar)
    stubsJar=${stubsJarFullPath//$service/}

    mvn install:install-file \
        -Dfile="$stubsJar" \
        -DgroupId=by.arvisit \
        -DartifactId="$artifactId" \
        -Dversion=0.0.1-SNAPSHOT \
        -Dpackaging=jar \
        -Dclassifier=stubs \
        --file "$service"pom.xml
done

testedServices=()
testResults=()

for service in *service/; do
    mvn clean verify --file "$service"pom.xml
    testResults+=($?)
    testedServices+=("${service::-1}")
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
