-- V1__Create_Identity_Tables.sql
-- Initial schema for Employee Identity Service
-- Note: Column types must match Hibernate/JPA entity mappings exactly

-- Create sequence for employer_id (GenerationType.AUTO uses sequences)
CREATE SEQUENCE IF NOT EXISTS employer_seq START WITH 1 INCREMENT BY 50;

-- Create employer_bank_details table first (referenced by employer)
CREATE TABLE IF NOT EXISTS employer_bank_details (
    employer_bank_details_id BIGSERIAL PRIMARY KEY,
    bank_name VARCHAR(255),
    bank_branch_name VARCHAR(255),
    bank_account_number VARCHAR(255),
    employer_description VARCHAR(255),
    monthly_payment DOUBLE PRECISION,
    payment_status BOOLEAN,
    employer_id BIGINT
);

-- Create employer table
CREATE TABLE IF NOT EXISTS employer (
    employer_id BIGINT PRIMARY KEY DEFAULT nextval('employer_seq'),
    employer_nic_name VARCHAR(50),
    employer_first_name VARCHAR(50) NOT NULL,
    employer_last_name VARCHAR(50),
    profile_image OID,
    profile_image_url VARCHAR(255),
    employer_password VARCHAR(255) NOT NULL,
    employer_email VARCHAR(50) UNIQUE,
    employer_phone VARCHAR(12),
    employer_address VARCHAR(100),
    employer_salary DOUBLE PRECISION,
    employer_nic VARCHAR(12) NOT NULL,
    is_active BOOLEAN DEFAULT false,
    pin INTEGER,
    gender VARCHAR(10) NOT NULL,
    employer_date_of_birth DATE,
    role VARCHAR(15) NOT NULL,
    employer_bank_details_id BIGINT,
    branch_id BIGINT NOT NULL,
    CONSTRAINT fk_employer_bank_details FOREIGN KEY (employer_bank_details_id) 
        REFERENCES employer_bank_details(employer_bank_details_id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_employer_branch_id ON employer(branch_id);
CREATE INDEX IF NOT EXISTS idx_employer_email ON employer(employer_email);
CREATE INDEX IF NOT EXISTS idx_employer_nic ON employer(employer_nic);
CREATE INDEX IF NOT EXISTS idx_employer_role ON employer(role);
CREATE INDEX IF NOT EXISTS idx_employer_active ON employer(is_active);
