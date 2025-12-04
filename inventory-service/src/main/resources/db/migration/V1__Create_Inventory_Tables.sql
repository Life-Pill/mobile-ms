-- V1__Create_Inventory_Tables.sql
-- Inventory Service Database Schema

-- Create supplier_company table
CREATE TABLE IF NOT EXISTS supplier_company (
    company_id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    company_address VARCHAR(255),
    company_contact VARCHAR(20),
    company_email VARCHAR(100),
    company_description VARCHAR(500),
    company_logo VARCHAR(500),
    company_website VARCHAR(255),
    company_registration_number VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create supplier table
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id BIGSERIAL PRIMARY KEY,
    company_id BIGINT,
    supplier_name VARCHAR(100) NOT NULL,
    supplier_address VARCHAR(255),
    supplier_phone VARCHAR(20),
    supplier_email VARCHAR(100),
    supplier_description VARCHAR(500),
    supplier_image VARCHAR(500),
    supplier_rating VARCHAR(20),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_supplier_company FOREIGN KEY (company_id) REFERENCES supplier_company(company_id) ON DELETE SET NULL
);

-- Create item_category table
CREATE TABLE IF NOT EXISTS item_category (
    category_id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    category_description VARCHAR(500),
    category_image VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create item table
CREATE TABLE IF NOT EXISTS item (
    item_id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    branch_id BIGINT,
    item_name VARCHAR(100) NOT NULL,
    selling_price DOUBLE PRECISION NOT NULL,
    item_barcode VARCHAR(50) NOT NULL,
    supply_date DATE,
    supplier_price DOUBLE PRECISION NOT NULL,
    is_free_issued BOOLEAN DEFAULT false,
    is_discounted BOOLEAN DEFAULT false,
    item_manufacture VARCHAR(100),
    item_quantity DOUBLE PRECISION NOT NULL,
    is_stock BOOLEAN DEFAULT true,
    measuring_unit_type VARCHAR(30),
    manufacture_date DATE,
    expire_date DATE,
    purchase_date DATE,
    warranty_period VARCHAR(50),
    rack_number VARCHAR(50),
    discounted_price DOUBLE PRECISION,
    discounted_percentage DOUBLE PRECISION,
    warehouse_name VARCHAR(100),
    is_special_condition BOOLEAN DEFAULT false,
    item_image VARCHAR(500),
    item_description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES item_category(category_id) ON DELETE RESTRICT,
    CONSTRAINT fk_item_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE RESTRICT
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_supplier_company ON supplier(company_id);
CREATE INDEX IF NOT EXISTS idx_supplier_active ON supplier(is_active);
CREATE INDEX IF NOT EXISTS idx_category_name ON item_category(category_name);
CREATE INDEX IF NOT EXISTS idx_category_active ON item_category(is_active);
CREATE INDEX IF NOT EXISTS idx_item_category ON item(category_id);
CREATE INDEX IF NOT EXISTS idx_item_supplier ON item(supplier_id);
CREATE INDEX IF NOT EXISTS idx_item_branch ON item(branch_id);
CREATE INDEX IF NOT EXISTS idx_item_name ON item(item_name);
CREATE INDEX IF NOT EXISTS idx_item_barcode ON item(item_barcode);
CREATE INDEX IF NOT EXISTS idx_item_stock ON item(is_stock);
CREATE INDEX IF NOT EXISTS idx_item_expire_date ON item(expire_date);
