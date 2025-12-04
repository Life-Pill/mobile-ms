# LifePill Microservices - API Documentation & Seed Data Guide

## 📚 API Documentation (Swagger/OpenAPI)

### Direct Service Access

| Service | Swagger UI | API Docs |
|---------|------------|----------|
| **User Auth** | http://localhost:8080/api/swagger-ui.html | http://localhost:8080/api/v3/api-docs |
| **Branch Service** | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| **Inventory Service** | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |
| **Identity Service** | http://localhost:8085/swagger-ui.html | http://localhost:8085/v3/api-docs |
| **Order Service** | http://localhost:8086/swagger-ui.html | http://localhost:8086/v3/api-docs |
| **Customer Service** | http://localhost:8070/swagger-ui.html | http://localhost:8070/v3/api-docs |

### Access Through API Gateway (Port 8222)

| Service | Swagger UI | API Docs |
|---------|------------|----------|
| **User Auth** | http://localhost:8222/user-auth/swagger-ui.html | http://localhost:8222/user-auth/v3/api-docs |
| **Branch Service** | http://localhost:8222/branch/swagger-ui.html | http://localhost:8222/branch/v3/api-docs |
| **Inventory Service** | http://localhost:8222/inventory/swagger-ui.html | http://localhost:8222/inventory/v3/api-docs |
| **Identity Service** | http://localhost:8222/identity/swagger-ui.html | http://localhost:8222/identity/v3/api-docs |
| **Order Service** | http://localhost:8222/order/swagger-ui.html | http://localhost:8222/order/v3/api-docs |
| **Customer Service** | http://localhost:8222/customer/swagger-ui.html | http://localhost:8222/customer/v3/api-docs |

### OpenAPI JSON Endpoints

| Service | Direct URL | Gateway URL |
|---------|-----------|-------------|
| **Identity Service** | http://localhost:8085/v3/api-docs | http://localhost:8222/identity/v3/api-docs |
| **Branch Service** | http://localhost:8081/v3/api-docs | http://localhost:8222/branch/v3/api-docs |
| **Inventory Service** | http://localhost:8082/v3/api-docs | http://localhost:8222/inventory/v3/api-docs |
| **Order Service** | http://localhost:8086/v3/api-docs | http://localhost:8222/order/v3/api-docs |
| **User Auth Service** | http://localhost:8080/api/v3/api-docs | http://localhost:8222/user-auth/v3/api-docs |
| **Customer Service** | http://localhost:8070/v3/api-docs | http://localhost:8222/customer/v3/api-docs |

---

## 🔐 Default Credentials

All seed data uses the following default password:
- **Password**: `password123`
- **BCrypt Hash**: `$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki`

---

## 🏢 Branch Service Seed Data

**Database**: `branch_service_db`  
**Migration**: `V2__Seed_Branch_Data.sql`

### Branches (5 records)

| ID | Branch Name | Location | Contact | Hours |
|----|-------------|----------|---------|-------|
| 1 | LifePill Main Branch | Colombo 01 | 0112345678 | 08:00-22:00 |
| 2 | LifePill Kandy Branch | Kandy | 0812345678 | 08:30-21:30 |
| 3 | LifePill Galle Branch | Galle | 0912345678 | 09:00-21:00 |
| 4 | LifePill Negombo Branch | Negombo | 0312345678 | 08:00-22:00 |
| 5 | LifePill Jaffna Branch | Jaffna | 0212345678 | 08:00-20:00 |

---

## 👤 Identity Service Seed Data

**Database**: `identity_service_db`  
**Migration**: `V2__Seed_Identity_Data.sql`

### Employers (16 records)

| ID | Name | Email | Role | Branch ID | PIN |
|----|------|-------|------|-----------|-----|
| 1 | Saman Perera | saman.perera@lifepill.lk | MANAGER | 1 | 1234 |
| 2 | Kumari Silva | kumari.silva@lifepill.lk | CASHIER | 1 | 1235 |
| 3 | Nimal Fernando | nimal.fernando@lifepill.lk | OTHER | 1 | 1236 |
| 4 | Nimali Fernando | nimali.fernando@lifepill.lk | MANAGER | 2 | 2234 |
| 5 | Chaminda Ratnayake | chaminda.r@lifepill.lk | CASHIER | 2 | 2235 |
| 6 | Priyanka Wijesekara | priyanka.w@lifepill.lk | OTHER | 2 | 2236 |
| 7 | Kasun Silva | kasun.silva@lifepill.lk | MANAGER | 3 | 3234 |
| 8 | Dilani Wijesinghe | dilani.w@lifepill.lk | CASHIER | 3 | 3235 |
| 9 | Ruwan Jayasinghe | ruwan.j@lifepill.lk | OTHER | 3 | 3236 |
| 10 | Thilini Jayawardena | thilini.j@lifepill.lk | MANAGER | 4 | 4234 |
| 11 | Asanka Perera | asanka.p@lifepill.lk | CASHIER | 4 | 4235 |
| 12 | Malika Bandara | malika.b@lifepill.lk | OTHER | 4 | 4236 |
| 13 | Raj Kumar | raj.kumar@lifepill.lk | MANAGER | 5 | 5234 |
| 14 | Priya Shankar | priya.s@lifepill.lk | CASHIER | 5 | 5235 |
| 15 | Kumar Veerasingam | kumar.v@lifepill.lk | OTHER | 5 | 5236 |
| 16 | System Administrator | admin@lifepill.lk | OWNER | 1 | 9999 |

### Employer Bank Details (15 records)
- Each employer has associated bank details with monthly salary information
- Banks: Bank of Ceylon, Commercial Bank, Peoples Bank, Sampath Bank, HNB, NSB, Seylan Bank

---

## 📦 Inventory Service Seed Data

**Database**: `inventory_service_db`  
**Migration**: `V2__Seed_Inventory_Data.sql`

### Supplier Companies (5 records)
| ID | Company Name | Location |
|----|--------------|----------|
| 1 | PharmaCorp International | Colombo 01 |
| 2 | MediSupply Lanka | Kandy |
| 3 | HealthPlus Distributors | Galle |
| 4 | Global Pharma Solutions | Negombo |
| 5 | BioMed Suppliers | Colombo 02 |

### Suppliers (7 records)
- ABC Pharmaceuticals
- XYZ Medical Supplies  
- Wellness Products Ltd
- CareFirst Pharma
- DiabeCare Supplies
- SkinHealth Labs
- RespiMed Solutions

### Item Categories (10 records)
1. Analgesics (Pain relief)
2. Antibiotics
3. Vitamins & Supplements
4. Cardiovascular
5. Diabetes Care
6. Dermatology
7. Respiratory
8. Gastrointestinal
9. First Aid
10. Baby Care

### Items (20+ records)
- Comprehensive pharmaceutical products across all categories
- Includes: Paracetamol, Ibuprofen, Aspirin, Amoxicillin, Vitamin C/D, Metformin, etc.

---

## 🛒 Order Service Seed Data

**Database**: `order_service_db`  
**Seed File**: `sql/seed.sql`

### Orders (15 sample orders)
- Date range: November 20-27, 2025
- Various amounts from Rs. 275 to Rs. 2,150

### Order Details (30 records)
- Line items for each order
- Product quantities and prices

### Payment Details (15 records)
- Payment methods: CASH, CARD
- Complete payment records

---

## 👥 Customer Service Seed Data

**Database**: `customer_service_db`  
**Migration**: `V2__Seed_Customer_Data.sql`

### Customers (20 records)

| District | Count | Example Names |
|----------|-------|---------------|
| Colombo | 4 | Saman Perera, Nimali Silva |
| Kandy | 3 | Chandana Pushpakumara, Ruwani Bandara |
| Galle | 3 | Priyantha Wickramasinghe |
| Gampaha | 3 | Dilshan Jayawardena |
| Jaffna | 2 | Raj Kumar Sivanathan |
| Kurunegala | 2 | Malith Dissanayake |
| Matara | 1 | Asanka Rathnayake |
| Anuradhapura | 1 | Kumara Herath |
| Ratnapura | 1 | Chaminda Gamage |

### Sub-Customers/Dependents (18 records)
- Family members linked to main customers
- Includes children, parents, and spouses

---

## 📱 User Auth Service Seed Data

**Database**: `mobile_user_auth_db`  
**Migration**: `V3__Seed_User_Auth_Data.sql`

### Mobile App Users (15 records)
- 13 verified users
- 2 users with pending email verification
- Various locations across Sri Lanka

### User Addresses (10 records)
- Primary and secondary addresses
- Coverage: Colombo, Kandy, Galle, Jaffna, Negombo, Kurunegala

---

## 🔧 Monitoring & Observability

### Service URLs

| Tool | URL | Default Credentials |
|------|-----|---------------------|
| **Eureka Dashboard** | http://localhost:8761 | admin/admin |
| **Prometheus** | http://localhost:9090 | - |
| **Grafana** | http://localhost:3001 | admin/admin |
| **Zipkin** | http://localhost:9411 | - |

### Prometheus Metrics Endpoints

All services expose metrics at `/actuator/prometheus`:

```bash
# Check metrics for each service
curl http://localhost:8080/api/actuator/prometheus  # User Auth
curl http://localhost:8081/actuator/prometheus       # Branch Service
curl http://localhost:8082/actuator/prometheus       # Inventory Service
curl http://localhost:8085/actuator/prometheus       # Identity Service
curl http://localhost:8086/actuator/prometheus       # Order Service
curl http://localhost:8070/actuator/prometheus       # Customer Service
```

### Health Check Endpoints

```bash
curl http://localhost:8080/api/actuator/health  # User Auth
curl http://localhost:8081/actuator/health       # Branch Service
curl http://localhost:8082/actuator/health       # Inventory Service
curl http://localhost:8085/actuator/health       # Identity Service
curl http://localhost:8086/actuator/health       # Order Service
curl http://localhost:8070/actuator/health       # Customer Service
```

---

## 🗄️ Database Configuration

### Database Names by Service

| Service | Database Name | Port |
|---------|---------------|------|
| User Auth | mobile_user_auth_db | 5432 |
| Branch Service | branch_service_db | 5432 |
| Inventory Service | inventory_service_db | 5432 |
| Identity Service | identity_service_db | 5432 |
| Order Service | order_service_db | 5432 |
| Customer Service | customer_service_db | 5432 |

### Enable Flyway Migrations

To seed the database with sample data, set `FLYWAY_ENABLED=true`:

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
```

Or via environment variable:
```bash
export FLYWAY_ENABLED=true
```

---

## 🚀 Quick Start

### 1. Start Infrastructure
```bash
docker-compose up -d postgres redis zipkin prometheus grafana
```

### 2. Start Services (in order)
```bash
# 1. Service Registry (Eureka) - Port 8761
# 2. Config Server - Port 8888
# 3. API Gateway - Port 8222
# 4. Microservices (any order)
```

### 3. Access Services
- **API Gateway**: http://localhost:8222
- **Eureka Dashboard**: http://localhost:8761
- **Grafana**: http://localhost:3001

---

## 🔑 Authentication

Most endpoints require JWT Bearer token authentication.

### For Employer Authentication (Identity Service):
```bash
# Login
POST http://localhost:8085/lifepill/v1/auth/authenticate
{
  "employerEmail": "admin@lifepill.lk",
  "employerPassword": "password123"
}

# PIN Authentication (for POS)
POST http://localhost:8085/lifepill/v1/auth/pin-auth
{
  "branchId": 1,
  "pin": 9999
}
```

### For Mobile User Authentication (User Auth):
```bash
# Register
POST http://localhost:8080/api/v1/auth/register
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}

# Login
POST http://localhost:8080/api/v1/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

### Using Token in Swagger UI:
1. Click the "Authorize" button (🔓)
2. Enter: `Bearer <your-token>`
3. Click "Authorize"

---

## 📝 Notes

1. **Flyway Migrations**: Enable with `FLYWAY_ENABLED=true` environment variable
2. **JPA DDL**: 
   - Development: `ddl-auto: update`
   - Production with migrations: `ddl-auto: validate`
3. **Passwords**: All seed data uses BCrypt-encoded password `password123`
4. **Service Discovery**: All services register with Eureka; use service names for inter-service communication
5. **Employer Roles**: OWNER > MANAGER > CASHIER > OTHER
