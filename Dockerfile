FROM maven:3.9.9-openjdk-17 AS builder
WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/subscription-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8008

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
