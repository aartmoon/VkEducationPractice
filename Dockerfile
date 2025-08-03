FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /

COPY --from=build /workspace/target/*.jar /app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default
ENV SERVER_PORT=8080

ENTRYPOINT ["sh", "-c", "exec java -jar /app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --server.port=${SERVER_PORT}"]
