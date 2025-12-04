#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Customer Service Database
    CREATE DATABASE customer_service_db;
    GRANT ALL PRIVILEGES ON DATABASE customer_service_db TO $POSTGRES_USER;
    
    -- Branch Service Database
    CREATE DATABASE branch_service_db;
    GRANT ALL PRIVILEGES ON DATABASE branch_service_db TO $POSTGRES_USER;
    
    -- Inventory Service Database
    CREATE DATABASE inventory_service_db;
    GRANT ALL PRIVILEGES ON DATABASE inventory_service_db TO $POSTGRES_USER;
    
    -- Order Service Database (formerly POS System)
    CREATE DATABASE order_service_db;
    GRANT ALL PRIVILEGES ON DATABASE order_service_db TO $POSTGRES_USER;
    
    -- Identity Service Database (Employee Authentication)
    CREATE DATABASE identity_service_db;
    GRANT ALL PRIVILEGES ON DATABASE identity_service_db TO $POSTGRES_USER;
EOSQL
