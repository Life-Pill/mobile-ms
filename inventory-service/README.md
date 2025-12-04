# Inventory Service

LifePill Inventory Management Microservice - Handles all inventory, item, supplier, and category operations.

## Overview

The Inventory Service is a microservice responsible for managing pharmacy inventory, including items/medicines, categories, suppliers, and supplier companies. It provides comprehensive REST APIs for inventory management.

## Features

- Item/Medicine Management (CRUD operations)
- Item Category Management
- Supplier Management
- Supplier Company Management
- Stock Management
- Expired Items Tracking
- Low Stock Alerts
- Item Image Upload
- Paginated Search

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
- **AWS S3 SDK** (for image storage)

## API Endpoints

### Item Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/item/save` | Create a new item |
| POST | `/lifepill/v1/item/save-item-with-image` | Create item with image |
| GET | `/lifepill/v1/item/get-all-items` | Get all items |
| GET | `/lifepill/v1/item/get-item-details-by-id/{id}` | Get item by ID |
| GET | `/lifepill/v1/item/get-by-name` | Search items by name |
| GET | `/lifepill/v1/item/get-by-barcode` | Search items by barcode |
| GET | `/lifepill/v1/item/get-all-item-by-status` | Get items by stock status |
| GET | `/lifepill/v1/item/get-all-item-by-status-lazy-initialized` | Paginated items by status |
| GET | `/lifepill/v1/item/branched/get-item/{branchId}` | Get items by branch |
| GET | `/lifepill/v1/item/get-by-category/{categoryId}` | Get items by category |
| GET | `/lifepill/v1/item/get-by-supplier/{supplierId}` | Get items by supplier |
| GET | `/lifepill/v1/item/get-expired-items` | Get expired items |
| GET | `/lifepill/v1/item/get-items-expiring-soon` | Get items expiring soon |
| GET | `/lifepill/v1/item/get-low-stock-items` | Get low stock items |
| PUT | `/lifepill/v1/item/update` | Update item |
| PUT | `/lifepill/v1/item/update-item-image/{itemId}` | Update item image |
| DELETE | `/lifepill/v1/item/delete-item/{id}` | Delete item |
| PUT | `/lifepill/v1/item/update-stock/{itemId}` | Update item stock |

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/category/save` | Create category |
| GET | `/lifepill/v1/category/get-all` | Get all categories |
| GET | `/lifepill/v1/category/get-by-id/{categoryId}` | Get category by ID |
| PUT | `/lifepill/v1/category/update/{categoryId}` | Update category |
| DELETE | `/lifepill/v1/category/delete/{categoryId}` | Delete category |

### Supplier Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/supplier/save` | Create supplier |
| GET | `/lifepill/v1/supplier/get-all` | Get all suppliers |
| GET | `/lifepill/v1/supplier/get-active` | Get active suppliers |
| GET | `/lifepill/v1/supplier/get-by-id/{supplierId}` | Get supplier by ID |
| GET | `/lifepill/v1/supplier/get-by-company/{companyId}` | Get suppliers by company |
| PUT | `/lifepill/v1/supplier/update/{supplierId}` | Update supplier |
| PUT | `/lifepill/v1/supplier/update-status/{supplierId}` | Update supplier status |
| DELETE | `/lifepill/v1/supplier/delete/{supplierId}` | Delete supplier |

### Supplier Company Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/lifepill/v1/supplier-company/save` | Create company |
| GET | `/lifepill/v1/supplier-company/get-all` | Get all companies |
| GET | `/lifepill/v1/supplier-company/get-by-id/{companyId}` | Get company by ID |
| PUT | `/lifepill/v1/supplier-company/update/{companyId}` | Update company |
| DELETE | `/lifepill/v1/supplier-company/delete/{companyId}` | Delete company |

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | localhost |
| `DB_PORT` | PostgreSQL port | 5432 |
| `INVENTORY_DB_NAME` | Database name | inventory_service_db |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `EUREKA_HOST` | Eureka server host | localhost |
| `EUREKA_PORT` | Eureka server port | 8761 |
| `EUREKA_USER` | Eureka username | eureka |
| `EUREKA_PASSWORD` | Eureka password | password |
| `ZIPKIN_HOST` | Zipkin host | localhost |
| `ZIPKIN_PORT` | Zipkin port | 9411 |
| `AWS_S3_BUCKET` | S3 bucket for images | lifepill-inventory-images |
| `AWS_REGION` | AWS region | us-east-1 |
| `AWS_ACCESS_KEY` | AWS access key | - |
| `AWS_SECRET_KEY` | AWS secret key | - |

## Running Locally

```bash
# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/lifepill-inventory-service.jar
```

## Docker

```bash
# Build Docker image
docker build -t lifepill-inventory-service .

# Run container
docker run -p 8082:8082 \
  -e DB_HOST=postgres \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  lifepill-inventory-service
```

## Swagger Documentation

Access Swagger UI at: `http://localhost:8082/swagger-ui.html`

## Port

- Default port: **8082**

## Service Registration

Registers with Eureka as: **INVENTORY-SERVICE**

## Data Model

### Item Entity

Key fields:
- `itemId` - Unique identifier
- `itemName` - Item name
- `sellingPrice` - Selling price
- `itemBarCode` - Barcode
- `itemQuantity` - Stock quantity
- `expireDate` - Expiration date
- `categoryId` - FK to category
- `supplierId` - FK to supplier
- `branchId` - FK to branch

### MeasuringUnitType Enum

- PIECE, BOX, BOTTLE, PACK, STRIP
- TABLET, CAPSULE, TUBE, SACHET
- VIAL, AMPOULE, ML, L, MG, G, KG, OTHER
