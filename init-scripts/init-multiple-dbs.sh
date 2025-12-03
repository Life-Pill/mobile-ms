#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE customer_service_db;
    GRANT ALL PRIVILEGES ON DATABASE customer_service_db TO $POSTGRES_USER;
EOSQL
