-- Migration: Remove profile_image BLOB column and update profile_image_url length
-- This migration removes byte array storage for employer profile images

ALTER TABLE employer DROP COLUMN IF EXISTS profile_image;
ALTER TABLE employer ALTER COLUMN profile_image_url TYPE VARCHAR(1000);
