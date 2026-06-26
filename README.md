# Sleep Logger API

Spring Boot REST API for logging and analyzing user sleep data.  
Built with Java 21, Spring Boot, PostgreSQL, Flyway, and Docker.

## Features

- Create sleep logs
- Fetch last night's sleep
- 30-day analytics:
    - Avg time in bed
    - Avg bed/wake times
    - Feeling distribution (BAD / OK / GOOD)
- Flyway migrations
- Swagger UI

# Local Setup

## Requirements
- Java 21
- Maven
- PostgreSQL running locally

## Run 

```
mvn clean install
mvn spring-boot:run
```

# Docker Setup

## Run 

```
docker compose up --build
```

## Stopping 
```
docker compose down
```

# Services 

API: http://localhost:8080

DB: localhost:5433

## Endpoints 

```
POST /api/sleep
GET /api/sleep/latest?userId={uuid}
GET /api/sleep/analytics?userId={uuid}
```

# Testing

## Unit Test 

```
mvn test
```

## Coverage Report 

```
target/site/jacoco/index.html

```