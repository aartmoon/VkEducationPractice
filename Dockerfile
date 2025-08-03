# --- Build stage: сборка с Maven + Temurin JDK 21 ---
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# --- Runtime stage: минимальный образ с JRE 21 ---
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /

# Копируем собранный jar
COPY --from=build /workspace/target/*.jar /app.jar

# Проброс порта (можно убрать, если не нужен)
EXPOSE 8080

# Значения по умолчанию (можно переопределять в docker-compose или окружении)
ENV SPRING_PROFILES_ACTIVE=default
ENV SERVER_PORT=8080

ENTRYPOINT ["sh", "-c", "exec java -jar /app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --server.port=${SERVER_PORT}"]
