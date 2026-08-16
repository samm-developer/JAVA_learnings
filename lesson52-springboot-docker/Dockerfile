# Multi-stage build: compile with Maven, run with a slim JRE image

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8097
# Persist H2 file DB under /data (see docker-compose volume)
ENTRYPOINT ["java", "-jar", "app.jar"]
