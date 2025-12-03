# LifePill Microservices Architecture

[![API Gateway](https://img.shields.io/badge/submodule-api--gateway-blue)](https://github.com/Life-Pill/api-gateway)
[![Service Registry](https://img.shields.io/badge/submodule-service--registry-green)](https://github.com/Life-Pill/service-registry)
[![User Auth](https://img.shields.io/badge/submodule-user--auth-orange)](https://github.com/Life-Pill/mobile-auth-user-service)
[![Patient Customer Service](https://img.shields.io/badge/submodule-patient--customer--service-purple)](https://github.com/Life-Pill/patient-customer-service)

## Production-Grade Microservices Setup

This repository contains a production-grade microservices architecture for the LifePill application, following industry best practices and SOLID principles.

## Repository Structure (Git Submodules)

This is a **monorepo** that links to the following microservices as Git submodules:

| Submodule | Repository | Description |
|-----------|------------|-------------|
| [`api-gateway`](./api-gateway) | [Life-Pill/api-gateway](https://github.com/Life-Pill/api-gateway) | Spring Cloud Gateway with Circuit Breaker |
| [`service-registry`](./service-registry) | [Life-Pill/service-registry](https://github.com/Life-Pill/service-registry) | Netflix Eureka Server |
| [`user-auth`](./user-auth) | [Life-Pill/mobile-auth-user-service](https://github.com/Life-Pill/mobile-auth-user-service) | JWT Authentication Service |
| [`patient-customer-service`](./patient-customer-service) | [Life-Pill/patient-customer-service](https://github.com/Life-Pill/patient-customer-service) | Patient & Customer Management Service |

### 🔄 Cloning with Submodules

```bash
# Clone with all submodules
git clone --recurse-submodules https://github.com/Life-Pill/mobile-ms.git

# Or if already cloned, initialize submodules
git submodule init
git submodule update

# Update all submodules to latest
git submodule update --remote --merge
```

## Services Overview

| Service | Port | Description |
|---------|------|-------------|
| **Service Registry** | 8761 | Netflix Eureka Server for service discovery |
| **Config Server** | 8888 | Centralized configuration management |
| **API Gateway** | 9191 | Single entry point with routing, load balancing, and circuit breaker |
| **User Auth Service** | 8080 | JWT-based authentication service |
| **Patient Customer Service** | 8070 | Patient & customer management, prescriptions, payments |
| **PostgreSQL** | 5432 | Relational database for user-auth and customer service |
| **MongoDB** | 27017 | Document database for customer service |
| **Redis** | 6379 | Cache and rate limiting for API Gateway |
| **Prometheus** | 9090 | Metrics collection |
| **Grafana** | 3001 | Metrics visualization |
| **Zipkin** | 9411 | Distributed tracing |

## Startup Order

**Important:** Services must be started in the following order:

```
1. PostgreSQL, MongoDB, Redis - Infrastructure
2. Service Registry (Eureka Server) - Port 8761
3. Config Server - Port 8888
4. Zipkin - Port 9411
5. API Gateway - Port 9191
6. User Auth Service - Port 8080
7. Patient Customer Service - Port 8070
8. Prometheus, Grafana - Monitoring
```

## Architecture Diagram

```
                                    ┌─────────────────┐
                                    │  Config Server  │
                                    │    (8888)       │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
                    ▼                        ▼                        ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────────────┐
│   Client    │───▶│ API Gateway │───▶│  Service    │◀───│    User Auth        │
│  (Mobile/   │    │   (9191)    │    │  Registry   │    │    Service (8080)   │
│   Web)      │    └──────┬──────┘    │   (8761)    │    └─────────────────────┘
└─────────────┘           │           └─────────────┘              ▲
                          │                  ▲                     │
                          │                  │              ┌──────┴──────┐
                          │                  │              │  PostgreSQL │
                          │                  │              │   (5432)    │
                          │                  │              └─────────────┘
                          │                  │
                          │           ┌─────────────────────┐
                          └──────────▶│ Patient Customer    │
                                      │ Service (8070)      │
                                      └──────────┬──────────┘
                                                 │
                                    ┌────────────┼────────────┐
                                    │            │            │
                             ┌──────▼──────┐ ┌───▼────┐ ┌─────▼─────┐
                             │  PostgreSQL │ │MongoDB │ │  Stripe   │
                             │   (5432)    │ │(27017) │ │  (API)    │
                             └─────────────┘ └────────┘ └───────────┘

                    ┌─────────────────────────────────────────────────┐
                    │                   Monitoring                     │
                    │  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
                    │  │Prometheus│  │ Grafana  │  │  Zipkin  │       │
                    │  │  (9090)  │  │  (3001)  │  │  (9411)  │       │
                    │  └──────────┘  └──────────┘  └──────────┘       │
                    └─────────────────────────────────────────────────┘
```

## 🔧 Configuration

### Environment Variables

Each service requires specific environment variables. Copy the `.env.example` files to `.env` and configure:

#### Service Registry
```bash
EUREKA_PORT=8761
EUREKA_USERNAME=admin
EUREKA_PASSWORD=your-secure-password
```

#### Config Server
```bash
CONFIG_SERVER_PORT=8888
CONFIG_SERVER_USERNAME=configuser
CONFIG_SERVER_PASSWORD=your-secure-password
EUREKA_URI=http://localhost:8761/eureka/
```

#### API Gateway
```bash
API_GATEWAY_PORT=9191
EUREKA_URI=http://localhost:8761/eureka/
CORS_ORIGINS=http://localhost:3000,http://localhost:8081
```

#### User Auth Service
```bash
DB_URL=jdbc:postgresql://localhost:5432/mobile_user_auth_db
DB_USERNAME=postgres
DB_PASSWORD=your-db-password
JWT_SECRET=your-jwt-secret-key
EUREKA_URI=http://localhost:8761/eureka/
```

## 🏃‍♂️ Running the Services

### Using Maven

```bash
# 1. Start Service Registry
cd service-registry
./mvnw spring-boot:run

# 2. Start Config Server (optional)
cd config-server
./mvnw spring-boot:run

# 3. Start API Gateway
cd api-gateway
./mvnw spring-boot:run

# 4. Start User Auth Service
cd user-auth
./mvnw spring-boot:run
```

### Using Docker Compose

```bash
# Copy environment file and configure
cp .env.example .env

# Build and start all services
docker compose --env-file .env up -d

# View logs
docker compose logs -f

# Stop all services
docker compose down
```

## Quick Start with Docker

```bash
# 1. Clone repository
git clone --recurse-submodules https://github.com/Life-Pill/mobile-ms.git
cd mobile-ms

# 2. Setup environment
cp .env.example .env
# Edit .env with your configuration

# 3. Start all services
docker compose --env-file .env up -d

# 4. Verify services
docker compose ps
```

## Service URLs

After starting with Docker Compose:

| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:9191 |
| User Auth Swagger | http://localhost:8080/api/swagger-ui.html |
| Patient Customer Swagger | http://localhost:8070/swagger-ui.html |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3001 |
| Zipkin | http://localhost:9411 |

##  Monitoring & Health Checks

### Actuator Endpoints

All services expose actuator endpoints for monitoring:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health status |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Metrics data |
| `/actuator/prometheus` | Prometheus format metrics |

### Service Discovery Dashboard

Access Eureka Dashboard: `http://localhost:8761`
- Username: `admin` (default)
- Password: `admin` (default)

### API Gateway Endpoints

| Endpoint | Description |
|----------|-------------|
| `/gateway/health` | Gateway health |
| `/gateway/services` | List registered services |
| `/gateway/info` | Gateway information |

## 🔒 Security Features

### API Gateway
- Circuit Breaker pattern (Resilience4j)
- Request rate limiting
- CORS configuration
- Request/Response logging
- Authentication header relay

### Service Registry
- Basic authentication for dashboard
- Secured actuator endpoints

### Config Server
- Basic authentication for config endpoints
- Encrypted property support

## 🔄 Circuit Breaker Configuration

The API Gateway implements circuit breaker pattern:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      userAuthCircuitBreaker:
        sliding-window-size: 5
        failure-rate-threshold: 40
        wait-duration-in-open-state: 15s
```

## 📝 API Routes

### User Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | User registration |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/refresh` | Refresh token |
| POST | `/api/auth/logout` | User logout |
| POST | `/api/auth/forgot-password` | Password reset request |

### User Profile
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/me` | Get current user |
| PUT | `/api/users/me` | Update profile |
| PUT | `/api/users/me/password` | Change password |

### Patient Customer Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | List all customers |
| POST | `/api/customers` | Create customer |
| GET | `/api/customers/{id}` | Get customer by ID |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |
| GET | `/api/prescriptions` | List prescriptions |
| POST | `/api/prescriptions` | Create prescription |
| GET | `/api/medical-records` | List medical records |
| POST | `/api/medical-records` | Create medical record |
| POST | `/api/payments/create-intent` | Create Stripe payment intent |
| POST | `/api/payments/confirm` | Confirm payment |
| GET | `/api/prescription-orders` | List prescription orders |
| POST | `/api/prescription-orders` | Create prescription order |
| GET | `/api/sub-customers` | List sub-customers |
| POST | `/api/sub-customers` | Create sub-customer |

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 📚 SOLID Principles Applied

1. **Single Responsibility**: Each service has one responsibility
2. **Open/Closed**: Configuration-based extension without code changes
3. **Liskov Substitution**: Standard Spring interfaces used throughout
4. **Interface Segregation**: Focused filter and handler interfaces
5. **Dependency Inversion**: Abstraction-based dependencies

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Cloud 2023.0.1**
- **Spring Cloud Gateway** (Reactive)
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Config** (Configuration Management)
- **Resilience4j** (Circuit Breaker)
- **Micrometer + Prometheus** (Metrics)
- **PostgreSQL** (Database)
- **Flyway** (Database Migration)
- **JWT** (Authentication)

## 📞 Support

For support, email support@lifepill.com or raise an issue in the repository.

## 📄 License

This project is licensed under the MIT License.
