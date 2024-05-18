FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /cab-app-api-gateway
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=/cab-app-api-gateway/target/cab-app-api-gateway-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /opt/api-gateway/app.jar
ENTRYPOINT ["java", "-jar", "/opt/api-gateway/app.jar"]
