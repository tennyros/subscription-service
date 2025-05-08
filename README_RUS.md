# Subscription Service

![CI Status](https://github.com/tennyros/subscription-service/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Coverage](https://github.com/tennyros/subscription-service/raw/coverage-badge/.github/badges/jacoco.svg)
![Java 17](https://img.shields.io/badge/Java-17-blue)
![Spring Boot 3.4.5](https://img.shields.io/badge/Spring_Boot-3.4.5-brightgreen)

**Subscription Service** — это REST API микросервис для управления пользователями и их подписками на цифровые сервисы (Netflix, YouTube Premium и др.).

[Русский](README_RUS.md) | [English](README.md)

## Стек технологий

| Компонент                       | Версия   | Назначение                               |
|---------------------------------|----------|------------------------------------------|
| Spring Boot                     | 3.4.5    | Бэкэнд фреймворк                         |
| Hibernate ORM (Spring Data JPA) | 6.6.13   | ORM фреймворк для работы с данными Java  |
| Maven (обертка)                 | 3.9.9    | Инструмент для сборки проекта            |
| PostgreSQL                      | 17+      | Система управления базами данных         |
| Liquibase                       | 4.31.1   | Миграции базы данных                     |
| MapStruct                       | 1.6.3    | Маппинг объектов (DTO/Entity)            |
| JUnit 5                         | 5.11.4   | Фреймворк для юнит-тестов                |
| Mockito                         | 5.14.2   | Фреймворк для мокирования в юнит-тестах  |
| JaCoCo                          | 0.8.13   | Отчеты по покрытию тестами               |
| Springdoc OpenAPI               | 2.8.6    | Документация API (Swagger UI для Spring) |
| Spring Cloud Netflix            | 2024.0.1 | Интеграция с Eureka (Service Discovery)  |

## Структура проекта

```text
Структура исходного кода (ветка dev):
├── main/
│   ├── java/com/github/tennyros/subscription_service/
│   │   ├── dto/                          # Объекты передачи данных
│   │   │   ├── request/                  # DTO запросов
│   │   │   └── response/                 # DTO ответов
│   │   ├── exception/                    # Кастомные исключения
│   │   ├── http/
│   │   │   ├── advice/                   # Обработчики исключений
│   │   │   └── rest/                     # REST-контроллеры
│   │   ├── mapper/                       # MapStruct-мапперы
│   │   ├── model/                        # JPA-сущности
│   │   ├── repository/                   # Spring Data JPA репозитории
│   │   │   └── projection/               # Интерфейсы проекций
│   │   └── service/                      # Бизнес-логика
│   │       └── impl/                     # Реализации сервисов
│   └── resources/
│       ├── db/changelog/                 # SQL-миграции Liquibase
│       ├── application.yml               # Основная конфигурация
│       └── application-dev.yml           # Конфиг для разработки
├── test/                                 # Тесты
│   ├── java/
│   │   └── com/github/tennyros/subscription_service/
│   │       ├── controller/       # Юнит/интеграционные тесты контроллер слоя
│   │       ├── mapper/           # Юнит тесты мапперов
│   │       └── service/          # Юнит тесты бизнес слоя
│   └── resources/
│       └── application-test.yml  # Конфигурация для тестирования
pom.xml

Собранные артефакты:
target/
├── generated-sources/
│   ├── annotations/     # Автогенерируемые классы MapStruct
│   │   └── com.github.tennyros.subscription_service.mapper/
├── reports-report/      # Отчеты JaCoCo о покрытии
```

## Быстрый старт

### Требования

1. **Java 17+**
2. **Docker и Docker-compose**

### Настройка

**1. Клонируйте репозиторий:**

```bash
git clone https://github.com/tennyros/subscription-service.git
cd subscription-service
```

**2. Скопируйте файл .env и при необходимости измените учетные данные:**

```bash
cp .env.example .env
```

**3. Запустите приложение и PostgreSQL через Docker:**

```bash
# Скопируйте пример конфигурации (если еще не настроено)
cp docker-compose.example.yml docker-compose.yml  

# Запуск
docker-compose up -d
```

**4. После этого API будет доступно по адресу:**

```url
http://localhost:8088/swagger-ui.html
```

## CI Pipeline

```text
Проект настроен с CI для автоматической сборки и тестирования 
при пулл-реквестах с использованием GitHub Actions.
```
