# Subscription Service

![CI Status](https://github.com/tennyros/subscription-service/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Java 17](https://img.shields.io/badge/Java-17-blue)
![Spring Boot 3.4.5](https://img.shields.io/badge/Spring_Boot-3.4.5-brightgreen)

**Subscription Service** — is a REST API microservice for managing users and their subscriptions to digital services (such as Netflix, YouTube Premium, etc.).

[Русский](README_RUS.md) | [English](README.md)

## Tech Stack

| Компонент                       | Версия   | Назначение                                  |
|---------------------------------|----------|---------------------------------------------|
| Spring Boot                     | 3.4.5    | Backend framework                           |
| Hibernate ORM (Spring Data JPA) | 6.6.13   | ORM framework for Java data handling        |
| Maven (wrapper)                 | 3.9.9    | Project build tool                          |
| PostgreSQL                      | 17+      | Database management system                  |
| Liquibase                       | 4.31.1   | Database migrations                         |
| MapStruct                       | 1.6.3    | DTO/Entity object mapping                   |
| Springdoc OpenAPI               | 2.8.6    | API documentation (Swagger UI for Spring)   |
| Spring Cloud Netflix            | 2024.0.1 | Integration with Eureka (Service Discovery) |

## Project Structure

```text
Source code structure (branch: dev):
├── main/
│   ├── java/com/github/tennyros/subscription_service/
│   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── request/                  # Request DTOs
│   │   │   └── response/                 # Response DTOs
│   │   ├── exception/                    # Custom exceptions
│   │   ├── http/
│   │   │   ├── advice/                   # Exception handlers
│   │   │   └── rest/                     # REST controllers
│   │   ├── mapper/                       # MapStruct mappers
│   │   ├── model/                        # JPA entities
│   │   ├── repository/                   # Spring Data JPA repositories
│   │   │   └── projection/               # Projection interfaces
│   │   └── service/                      # Business logic
│   │       └── impl/                     # Service implementations
│   └── resources/
│       ├── db/changelog/                 # Liquibase SQL migrations
│       ├── application.yml               # Main configuration
│       └── application-dev.yml           # Development configuration
├── test/                                 # Tests
pom.xml

Built artifacts:
target/
├── generated-sources/
│   ├── annotations/     # MapStruct auto-generated classes
│   │   └── com.github.tennyros.subscription_service.mapper/
```

## Quick Start

### Requirements

1. **Java 17+**
2. **Docker и Docker-compose**

### Setup

**1. Clone the repository:**

```bash
git clone https://github.com/tennyros/subscription-service.git
cd subscription-service
```

**2. Copy the .env file and edit credentials if needed:**

```bash
cp .env.example .env
```

**3. Start the application and PostgreSQL using Docker:**

```bash
# Copy the example configuration (if not already set)
cp docker-compose.example.yml docker-compose.yml  

# Start containers
docker-compose up -d
```

**4. After that, the API will be available at:**

```url
http://localhost:8088/swagger-ui.html
```

## CI Pipeline

```text
The project is configured with CI to automatically build and test 
on pull requests using GitHub Actions.
```
