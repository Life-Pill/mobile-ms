-- V1__Create_Branch_Tables.sql
-- Branch Service Database Schema
-- Note: Employers are now managed by Identity Service (not Branch Service)

-- Create branch table (matching Branch.java entity)
CREATE TABLE IF NOT EXISTS branch (
    branch_id BIGSERIAL PRIMARY KEY,
    branch_name VARCHAR(100) NOT NULL,
    branch_address VARCHAR(255) NOT NULL,
    branch_contact VARCHAR(20),
    branch_fax VARCHAR(20),
    branch_email VARCHAR(100),
    branch_description VARCHAR(500),
    branch_image OID,
    branch_image_url VARCHAR(500),
    branch_status BOOLEAN DEFAULT true,
    branch_location VARCHAR(255),
    branch_created_on VARCHAR(50),
    branch_created_by VARCHAR(100),
    branch_latitude DOUBLE PRECISION,
    branch_longitude DOUBLE PRECISION,
    opening_hours VARCHAR(100),
    closing_hours VARCHAR(100)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_branch_name ON branch(branch_name);
CREATE INDEX IF NOT EXISTS idx_branch_status ON branch(branch_status);
CREATE INDEX IF NOT EXISTS idx_branch_location ON branch(branch_location);
