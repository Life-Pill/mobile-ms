-- Migration: Remove branch_image BLOB column and update branch_image_url length
-- This migration removes byte array storage and prepares for S3 URL storage

ALTER TABLE branch DROP COLUMN IF EXISTS branch_image;
ALTER TABLE branch ALTER COLUMN branch_image_url TYPE VARCHAR(1000);
