FROM maven:3.9.6-eclipse-temurin-21 AS build

RUN wget -O /opt/apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.49.0/elastic-apm-agent-1.49.0.jar

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
COPY --from=build /opt/apm-agent.jar /opt/apm-agent.jar
WORKDIR /opt/application
COPY --from=extract /application/dependencies/ ./
COPY --from=extract /application/spring-boot-loader/ ./
COPY --from=extract /application/snapshot-dependencies/ ./
COPY --from=extract /application/application/ ./
ENTRYPOINT java -javaagent:/opt/apm-agent.jar $JVM_OPTIONS org.springframework.boot.loader.launch.JarLauncher

