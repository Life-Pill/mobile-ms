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
| [`employee-identity-service`](./employee-identity-service) | Employee Identity Service | Employee authentication with Redis session caching |

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
| **User Auth Service** | 8080 | JWT-based authentication service (Mobile) |
| **Identity Service** | 8085 | Employee authentication with Redis session caching |
| **Patient Customer Service** | 8070 | Patient & customer management, prescriptions, payments |
| **Branch Service** | 8071 | Branch management |
| **Inventory Service** | 8072 | Inventory management |
| **Order Service** | 8073 | Order processing |
| **PostgreSQL** | 5432 | Relational database |
| **MongoDB** | 27017 | Document database |
| **Redis** | 6379 | Session caching and rate limiting |
| **Prometheus** | 9090 | Metrics collection |
| **Grafana** | 3001 | Metrics visualization |
| **Zipkin** | 9411 | Distributed tracing |

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
                          ├──────────┐       │
                          │          │       │
                          ▼          ▼       │
              ┌───────────────┐ ┌────────────┴────────┐
              │   Identity    │ │ Patient Customer    │
              │ Service(8085) │ │ Service (8070)      │
              └───────┬───────┘ └──────────┬──────────┘
                      │                    │
               ┌──────┴──────┐   ┌─────────┼─────────┐
               │   Redis     │   │         │         │
               │   (6379)    │   │         │         │
               └─────────────┘   ▼         ▼         ▼
                          ┌──────────┐ ┌───────┐ ┌─────────┐
                          │PostgreSQL│ │MongoDB│ │ Stripe  │
                          │  (5432)  │ │(27017)│ │  (API)  │
                          └──────────┘ └───────┘ └─────────┘

                    ┌─────────────────────────────────────────────────┐
                    │                   Monitoring                     │
                    │  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
                    │  │Prometheus│  │ Grafana  │  │  Zipkin  │       │
                    │  │  (9090)  │  │  (3001)  │  │  (9411)  │       │
                    │  └──────────┘  └──────────┘  └──────────┘       │
                    └─────────────────────────────────────────────────┘
```

## Pull Architecture (Service Discovery)

The system uses **Pull-based Service Discovery** via Netflix Eureka:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PULL ARCHITECTURE                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ┌─────────────┐          1. Register              ┌─────────────────┐     │
│   │   Service   │ ─────────────────────────────────▶│  Eureka Server  │     │
│   │  (on start) │                                   │    (8761)       │     │
│   └─────────────┘                                   └────────┬────────┘     │
│                                                              │              │
│                                                              │              │
│   ┌─────────────┐         2. Pull Registry          ┌───────┴───────┐      │
│   │ API Gateway │ ◀────────────────────────────────│ Service List  │      │
│   │   (9191)    │         (Every 30 sec)           └───────────────┘      │
│   └──────┬──────┘                                                          │
│          │                                                                  │
│          │ 3. Route Request (with cached registry)                         │
│          │                                                                  │
│          ▼                                                                  │
│   ┌──────────────────────────────────────────────────────────────────┐     │
│   │                      MICROSERVICES                                │     │
│   │                                                                   │     │
│   │  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐     │     │
│   │  │ Identity  │  │   User    │  │  Patient  │  │   Order   │     │     │
│   │  │  Service  │  │   Auth    │  │  Service  │  │  Service  │     │     │
│   │  │  (8085)   │  │  (8080)   │  │  (8070)   │  │  (8073)   │     │     │
│   │  └───────────┘  └───────────┘  └───────────┘  └───────────┘     │     │
│   │                                                                   │     │
│   │  ┌───────────┐  ┌───────────┐                                    │     │
│   │  │  Branch   │  │ Inventory │                                    │     │
│   │  │  Service  │  │  Service  │                                    │     │
│   │  │  (8071)   │  │  (8072)   │                                    │     │
│   │  └───────────┘  └───────────┘                                    │     │
│   └──────────────────────────────────────────────────────────────────┘     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

PULL FLOW:
1. Services REGISTER themselves with Eureka on startup
2. API Gateway PULLS the service registry every 30 seconds
3. Gateway uses CACHED registry to route requests to services
4. If a service goes down, it's removed from registry after heartbeat timeout
```

## Session Management Architecture (Redis Caching)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     SESSION MANAGEMENT FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────┐    1. Login (password)     ┌──────────────┐                   │
│  │  Client  │ ──────────────────────────▶│   Identity   │                   │
│  │  (POS)   │                            │   Service    │                   │
│  └──────────┘                            └──────┬───────┘                   │
│       │                                         │                           │
│       │                                         │ 2. Authenticate           │
│       │                                         │    & Generate JWT         │
│       │                                         ▼                           │
│       │                                  ┌──────────────┐                   │
│       │                                  │  PostgreSQL  │                   │
│       │                                  │   (5432)     │                   │
│       │                                  └──────────────┘                   │
│       │                                         │                           │
│       │                                         │ 3. Cache Session          │
│       │                                         ▼                           │
│       │                                  ┌──────────────┐                   │
│       │                                  │    Redis     │                   │
│       │    ◀──────────────────────────── │   (6379)     │                   │
│       │    4. Return JWT + Session       │              │                   │
│       │                                  │  ┌────────┐  │                   │
│       │                                  │  │Session │  │                   │
│       │                                  │  │ Data:  │  │                   │
│       │                                  │  │ -token │  │                   │
│       │                                  │  │ -user  │  │                   │
│       │                                  │  │ -expiry│  │                   │
│       │                                  │  │-revoked│  │                   │
│       │                                  │  └────────┘  │                   │
│       │                                  └──────────────┘                   │
│       │                                                                     │
│       │    5. Re-login with PIN (from cache)                               │
│       │ ─────────────────────────────────────────▶                         │
│       │                                                                     │
│       │    ◀─────────────────────────────────────                          │
│       │    6. Return cached session                                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

SESSION OPERATIONS:
┌─────────────────────────────────────────────────────────────────┐
│ Temporary Logout  │ Keep cache data, set DB inactive           │
│                   │ User can re-login with PIN                 │
├───────────────────┼────────────────────────────────────────────┤
│ Permanent Logout  │ Delete cache data, set DB inactive         │
│                   │ User must login with password              │
├───────────────────┼────────────────────────────────────────────┤
│ Session Valid     │ revoked == false AND expiresAt > now()     │
└───────────────────┴────────────────────────────────────────────┘
```

## Identity Service Session Endpoints

| Method | Endpoint | Request Body | Description |
|--------|----------|--------------|-------------|
| POST | `/lifepill/v1/auth/authenticate` | `{"employerEmail": "...", "employerPassword": "..."}` | Login with password (caches session) |
| POST | `/lifepill/v1/auth/authenticate-pin` | `{"employerEmail": "...", "pin": 1234}` | Login with PIN (caches session) |
| POST | `/lifepill/v1/session/authenticate/cached` | `{"username": "...", "pin": 1234}` | Re-login from cache with PIN |
| POST | `/lifepill/v1/session/logout/temporary` | `{"username": "..."}` | Temporary logout (keeps cache) |
| POST | `/lifepill/v1/session/logout/permanent` | `{"username": "..."}` | Permanent logout (removes cache) |
| GET | `/lifepill/v1/session/get-cached-employer/email/{email}` | - | Get cached session by email |
| GET | `/lifepill/v1/session/get-all-cached-employers` | - | Get all cached sessions |
| GET | `/lifepill/v1/session/check/{email}` | - | Check if session is valid |

## Startup Order

**Important:** Services must be started in the following order:

```
1. PostgreSQL, MongoDB, Redis - Infrastructure
2. Service Registry (Eureka Server) - Port 8761
3. Config Server - Port 8888
4. Zipkin - Port 9411
5. API Gateway - Port 9191
6. Identity Service - Port 8085
7. User Auth Service - Port 8080
8. Patient Customer Service - Port 8070
9. Other Services...
10. Prometheus, Grafana - Monitoring
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

#### Identity Service
```bash
DB_URL=jdbc:postgresql://localhost:5432/identity_service_db
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=your-jwt-secret-key
EUREKA_URI=http://localhost:8761/eureka/
```

## 🏃‍♂️ Running the Services

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
| Identity Service Swagger | http://localhost:8085/swagger-ui/index.html |
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

## 🔒 Security Features

### API Gateway
- Circuit Breaker pattern (Resilience4j)
- Request rate limiting
- CORS configuration
- Request/Response logging
- Authentication header relay

### Identity Service
- JWT-based authentication
- BCrypt password encoding
- PIN-based quick authentication
- Redis session caching with TTL
- Session revocation support

### Service Registry
- Basic authentication for dashboard
- Secured actuator endpoints

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

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Cloud 2023.0.1**
- **Spring Cloud Gateway** (Reactive)
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Config** (Configuration Management)
- **Resilience4j** (Circuit Breaker)
- **Spring Data Redis** (Session Caching)
- **Micrometer + Prometheus** (Metrics)
- **PostgreSQL** (Database)
- **MongoDB** (Document Database)
- **Redis** (Session Cache)
- **Flyway** (Database Migration)
- **JWT** (Authentication)

## 📞 Support

For support, email support@lifepill.com or raise an issue in the repository.

## 📄 License

This project is licensed under the MIT License.
