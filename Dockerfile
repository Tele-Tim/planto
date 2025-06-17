FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependecy:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTest


FROM openjdk:17-jre-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

LABEL authors="tim"

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
