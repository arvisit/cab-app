FROM maven:3.9.6-eclipse-temurin-21 AS build

RUN wget -O /opt/apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.49.0/elastic-apm-agent-1.49.0.jar

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

WORKDIR /cab-app-rides-service
COPY cab-app-rides-service/pom.xml .
RUN mvn dependency:go-offline
COPY cab-app-rides-service/src ./src
RUN mvn package -DskipTests --file /cab-app-rides-service/pom.xml

FROM eclipse-temurin:21-jre-alpine AS extract
WORKDIR /application
ARG JAR_FILE=/cab-app-rides-service/target/cab-app-rides-service-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/application
COPY --from=extract /application/dependencies/ ./
COPY --from=extract /application/spring-boot-loader/ ./
COPY --from=extract /application/snapshot-dependencies/ ./
COPY --from=extract /application/application/ ./
COPY --from=build /opt/apm-agent.jar /opt/apm-agent.jar
ENTRYPOINT ["java", "-javaagent:/opt/apm-agent.jar", "$JVM_OPTIONS", "org.springframework.boot.loader.launch.JarLauncher"]

