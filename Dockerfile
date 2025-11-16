### BUILD STAGE ###
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN --mount=type=cache,target=/root/.m2 mvn -q dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package


### RUNTIME STAGE ###
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
