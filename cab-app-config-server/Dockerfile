FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /cab-app-config-server
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/cab-app-config-server/target/cab-app-config-server-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /opt/config-server/app.jar
ENTRYPOINT ["java", "-jar", "/opt/config-server/app.jar"]
