# syntax=docker/dockerfile:1

# Build step: build the chosen Maven module (and its dependencies)
FROM maven:3.9.11-eclipse-temurin-17 AS build

ARG MODULE

WORKDIR /workspace
COPY . .

RUN mvn -pl "${MODULE}" -am -DskipTests package

# Pick the Spring Boot re-packaged jar (skip *-plain.jar / *-original.jar if present)
RUN set -eux; \
  JAR_FILE="$(ls -1 "${MODULE}"/target/*.jar | grep -Ev '(plain|original)' | head -n 1)"; \
  cp "${JAR_FILE}" /tmp/app.jar

# Runtime step
FROM eclipse-temurin:17-jre

ARG PORT=8080

WORKDIR /app
COPY --from=build /tmp/app.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java","-jar","/app/app.jar"]

