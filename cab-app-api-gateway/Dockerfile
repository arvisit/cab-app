FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /cab-app-api-gateway
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS extract
WORKDIR /application
ARG JAR_FILE=/cab-app-api-gateway/target/cab-app-api-gateway-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/application
COPY --from=extract /application/dependencies/ ./
COPY --from=extract /application/spring-boot-loader/ ./
COPY --from=extract /application/snapshot-dependencies/ ./
COPY --from=extract /application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
