FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /cab-app-discovery-server
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=/cab-app-discovery-server/target/cab-app-discovery-server-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /opt/discovery-server/app.jar
ENTRYPOINT ["java", "-jar", "/opt/discovery-server/app.jar"]
