-- =====================================================
-- Script to update BCrypt password hash in all databases
-- Old hash: $2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki
-- New hash: $2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma
-- Password: password123 (10 rounds)
-- =====================================================

-- Connect to mobile_user_auth_db and update users table
\c mobile_user_auth_db;
UPDATE users 
SET password_hash = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma' 
WHERE password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki';
SELECT 'Updated ' || COUNT(*) || ' rows in users table (mobile_user_auth_db)' FROM users WHERE password_hash = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma';

-- Connect to customer_service_db and update customer table
\c customer_service_db;
UPDATE customer 
SET cus_password = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma' 
WHERE cus_password = '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki';
SELECT 'Updated ' || COUNT(*) || ' rows in customer table (customer_service_db)' FROM customer WHERE cus_password = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma';

-- Connect to identity_service_db and update employer table
\c identity_service_db;
UPDATE employer 
SET employer_password = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma' 
WHERE employer_password = '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki';
SELECT 'Updated ' || COUNT(*) || ' rows in employer table (identity_service_db)' FROM employer WHERE employer_password = '$2a$10$1P.byW2hXv3kt/gkG5W4puF6AfXh6dB6I6YGJI4xA8DlKDjtRv1Ma';

-- Done
SELECT 'Password hash update completed!' as status;
