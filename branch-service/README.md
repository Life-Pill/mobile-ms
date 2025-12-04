# Branch Service

LifePill Branch Management Microservice - Handles all branch and employer-related operations.

## Overview

The Branch Service is a microservice responsible for managing pharmacy branches and their employers. It provides REST APIs for CRUD operations on branches and employers.

## Features

- Branch Management (CRUD operations)
- Employer Management (CRUD operations)
- Employer Bank Details Management
- Branch Image Upload
- Search by location/name
- Active/Inactive status management

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Netflix Eureka Client**
- **Micrometer (Tracing)**
- **Zipkin**
- **Swagger/OpenAPI**

## API Endpoints

### Branch Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/branch/save` | Create a new branch |
| GET | `/lifepill/v1/branch/get-by-id` | Get branch by ID |
| GET | `/lifepill/v1/branch/get-all-branches` | Get all branches |
| GET | `/lifepill/v1/branch/get-active-branches` | Get active branches |
| PUT | `/lifepill/v1/branch/update` | Update branch |
| DELETE | `/lifepill/v1/branch/delete-by-id` | Delete branch |
| PUT | `/lifepill/v1/branch/update-status` | Update branch status |
| POST | `/lifepill/v1/branch/update-image` | Update branch image |
| GET | `/lifepill/v1/branch/search-by-name` | Search by name |
| GET | `/lifepill/v1/branch/search-by-location` | Search by location |
| GET | `/lifepill/v1/branch/get-with-employers` | Get branch with employers |

### Employer Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/employer/save` | Create employer |
| GET | `/lifepill/v1/employer/get-by-id` | Get employer by ID |
| GET | `/lifepill/v1/employer/get-all` | Get all employers |
| GET | `/lifepill/v1/employer/get-by-branch` | Get employers by branch |
| PUT | `/lifepill/v1/employer/update` | Update employer |
| DELETE | `/lifepill/v1/employer/delete` | Delete employer |
| PUT | `/lifepill/v1/employer/update-status` | Update employer status |
| POST | `/lifepill/v1/employer/update-profile-image` | Update profile image |
| GET | `/lifepill/v1/employer/get-by-role` | Get employers by role |
| GET | `/lifepill/v1/employer/get-active-by-branch` | Get active employers by branch |
| GET | `/lifepill/v1/employer/get-by-email` | Get employer by email |

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | localhost |
| `DB_PORT` | PostgreSQL port | 5432 |
| `BRANCH_DB_NAME` | Database name | branch_service_db |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `EUREKA_HOST` | Eureka server host | localhost |
| `EUREKA_PORT` | Eureka server port | 8761 |
| `EUREKA_USER` | Eureka username | eureka |
| `EUREKA_PASSWORD` | Eureka password | password |
| `ZIPKIN_HOST` | Zipkin host | localhost |
| `ZIPKIN_PORT` | Zipkin port | 9411 |

## Running Locally

```bash
# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/lifepill-branch-service.jar
```

## Docker

```bash
# Build Docker image
docker build -t lifepill-branch-service .

# Run container
docker run -p 8081:8081 \
  -e DB_HOST=postgres \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  lifepill-branch-service
```

## Swagger Documentation

Access Swagger UI at: `http://localhost:8081/swagger-ui.html`

## Port

- Default port: **8081**

## Service Registration

Registers with Eureka as: **BRANCH-SERVICE**
