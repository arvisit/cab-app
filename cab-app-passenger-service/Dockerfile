FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /cab-app-common
COPY cab-app-common/pom.xml .
RUN mvn dependency:go-offline
COPY cab-app-common/src ./src
RUN mvn install -DskipTests --file /cab-app-common/pom.xml

WORKDIR /cab-app-exception-handling-starter
COPY cab-app-exception-handling-starter/pom.xml .
RUN mvn dependency:go-offline
COPY cab-app-exception-handling-starter/src ./src
RUN mvn install -DskipTests --file /cab-app-exception-handling-starter/pom.xml

WORKDIR /cab-app-passenger-service
COPY cab-app-passenger-service/pom.xml .
RUN mvn dependency:go-offline
COPY cab-app-passenger-service/src ./src
RUN mvn package -DskipTests --file /cab-app-passenger-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/cab-app-passenger-service/target/cab-app-passenger-service-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /opt/passenger-service/app.jar
ENTRYPOINT ["java", "-jar", "/opt/passenger-service/app.jar"]
