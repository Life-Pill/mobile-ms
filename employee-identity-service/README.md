# LifePill Employee Identity Service

Employee authentication and identity management service for the LifePill POS system.

## Overview

This microservice handles:
- Employee registration and authentication
- JWT token generation and validation
- Role-based access control (OWNER, MANAGER, CASHIER)
- PIN-based authentication
- Employer profile management

## Architecture

The Identity Service is part of the LifePill microservices ecosystem:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   API Gateway   │────▶│Identity Service │────▶│ Branch Service  │
│   (port 9191)   │     │   (port 8085)   │     │   (port 8081)   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │  PostgreSQL DB  │
                        │identity_service_db│
                        └─────────────────┘
```

## API Endpoints

### Authentication Endpoints (`/lifepill/v1/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register new employer | No |
| POST | `/authenticate` | Login with email/password | No |
| POST | `/authenticate-pin` | Login with PIN | No |
| POST | `/logout` | Logout user | No |
| GET | `/test` | Health check | No |

### Employer Management Endpoints (`/lifepill/v1/employer`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/{employerId}` | Get employer by ID | Yes |
| GET | `/email/{email}` | Get employer by email | Yes |
| GET | `/all` | Get all employers | OWNER/MANAGER |
| GET | `/branch/{branchId}` | Get employers by branch | OWNER/MANAGER |
| GET | `/role/{role}` | Get employers by role | OWNER/MANAGER |
| PUT | `/{employerId}` | Update employer | Yes |
| DELETE | `/{employerId}` | Delete employer | OWNER |
| PUT | `/{employerId}/branch/{branchId}` | Update branch assignment | OWNER/MANAGER |

### Identity Validation Endpoints (`/lifepill/v1/identity`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/validate` | Validate JWT token | No (token in header) |
| GET | `/employer/{email}` | Get employer by email | No |
| GET | `/health` | Health check | No |

## Configuration

### Application Properties

```yaml
server:
  port: 8085

spring:
  application:
    name: IDENTITY-SERVICE
  datasource:
    url: jdbc:postgresql://localhost:5432/identity_service_db
    username: postgres
    password: postgres

jwt:
  secret: your-secret-key
  access-token:
    expiration: 86400000  # 24 hours
  refresh-token:
    expiration: 604800000 # 7 days

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server port | 8085 |
| `DB_HOST` | Database host | localhost |
| `DB_PORT` | Database port | 5432 |
| `DB_NAME` | Database name | identity_service_db |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `JWT_SECRET` | JWT signing secret | (required) |
| `EUREKA_URI` | Eureka server URL | http://localhost:8761/eureka/ |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |

## Running Locally

### Prerequisites
- Java 17+
- PostgreSQL 15+
- Redis (optional)
- Eureka Server running

### Steps

1. Create the database:
```sql
CREATE DATABASE identity_service_db;
```

2. Build the application:
```bash
./mvnw clean package -DskipTests
```

3. Run the application:
```bash
java -jar target/employee-identity-service-v1.0.0.jar
```

Or with Maven:
```bash
./mvnw spring-boot:run
```

## Docker

### Build Image
```bash
docker build -t lifepill/identity-service:latest .
```

### Run Container
```bash
docker run -d \
  --name identity-service \
  -p 8085:8085 \
  -e DB_HOST=postgres \
  -e DB_NAME=identity_service_db \
  -e JWT_SECRET=your-secret \
  -e EUREKA_URI=http://eureka:8761/eureka/ \
  lifepill/identity-service:latest
```

## API Usage Examples

### Register Employer
```bash
curl -X POST http://localhost:8085/lifepill/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "employerFirstName": "John",
    "employerLastName": "Doe",
    "employerEmail": "john@lifepill.com",
    "employerPassword": "password123",
    "employerNic": "123456789V",
    "gender": "MALE",
    "role": "MANAGER",
    "branchId": 1
  }'
```

### Authenticate
```bash
curl -X POST http://localhost:8085/lifepill/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "employerEmail": "john@lifepill.com",
    "employerPassword": "password123"
  }'
```

### Get Employer (Authenticated)
```bash
curl -X GET http://localhost:8085/lifepill/v1/employer/1 \
  -H "Authorization: Bearer <token>"
```

### Validate Token
```bash
curl -X GET http://localhost:8085/lifepill/v1/identity/validate \
  -H "Authorization: Bearer <token>"
```

## Security

### Roles
- **OWNER**: Full access to all endpoints
- **MANAGER**: Access to branch and employer management
- **CASHIER**: Limited access to own profile

### JWT Token Structure
```json
{
  "sub": "john@lifepill.com",
  "roles": ["ROLE_MANAGER"],
  "iat": 1699000000,
  "exp": 1699086400
}
```

## Swagger Documentation

Access Swagger UI at: `http://localhost:8085/swagger-ui.html`

API docs at: `http://localhost:8085/v3/api-docs`

## Monitoring

- Health check: `GET /actuator/health`
- Metrics: `GET /actuator/prometheus`
- Info: `GET /actuator/info`

## Dependencies

- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Security
- Spring Data JPA
- PostgreSQL Driver
- JWT (jjwt)
- OpenFeign (for Branch Service communication)
- Resilience4j (Circuit Breaker)
