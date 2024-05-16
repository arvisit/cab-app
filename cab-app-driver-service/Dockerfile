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

WORKDIR /cab-app-driver-service
COPY cab-app-driver-service/pom.xml .
RUN mvn dependency:go-offline
COPY cab-app-driver-service/src ./src
RUN mvn package -DskipTests --file /cab-app-driver-service/pom.xml

FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=/cab-app-driver-service/target/cab-app-driver-service-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /opt/driver-service/app.jar
ENTRYPOINT ["java", "-jar", "/opt/driver-service/app.jar"]
