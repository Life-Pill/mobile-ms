-- Migration: Update image columns length for S3 URLs
-- This migration extends image URL columns to accommodate full S3 URLs

ALTER TABLE item ALTER COLUMN item_image TYPE VARCHAR(1000);
ALTER TABLE supplier ALTER COLUMN supplier_image TYPE VARCHAR(1000);
ALTER TABLE supplier_company ALTER COLUMN company_image TYPE VARCHAR(1000);
ALTER TABLE item_category ALTER COLUMN category_image TYPE VARCHAR(1000);
