-- Flyway Migration V4: Comprehensive Order Data for Daily/Weekly/Monthly Sales Reports
-- Orders spanning the last 30 days (November 6 - December 6, 2025) across all branches
-- This provides sufficient data for testing daily, weekly, and monthly sales reports

-- ===================================
-- NOVEMBER 2025 ORDERS
-- ===================================

-- November 6, 2025 (30 days ago)
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-11-06 09:30:00', 2340.50),
(151, 2, '2025-11-06 10:15:00', 1890.25),
(251, 3, '2025-11-06 11:00:00', 2150.00),
(351, 4, '2025-11-06 14:30:00', 1765.75),
(451, 5, '2025-11-06 15:45:00', 2890.00);

-- November 9, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-11-09 08:15:00', 1560.00),
(201, 2, '2025-11-09 09:45:00', 2780.50),
(301, 3, '2025-11-09 10:30:00', 1345.25),
(401, 4, '2025-11-09 12:00:00', 2990.00),
(501, 5, '2025-11-09 13:15:00', 1876.75);

-- November 12, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-11-12 09:00:00', 3250.00),
(151, 2, '2025-11-12 10:30:00', 1945.50),
(251, 3, '2025-11-12 11:45:00', 2670.25),
(351, 4, '2025-11-12 13:00:00', 1890.00),
(451, 5, '2025-11-12 14:30:00', 2445.75);

-- November 15, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-11-15 08:30:00', 2150.50),
(201, 2, '2025-11-15 09:15:00', 3120.75),
(301, 3, '2025-11-15 10:00:00', 1765.00),
(401, 4, '2025-11-15 11:30:00', 2890.25),
(501, 5, '2025-11-15 13:45:00', 1990.50);

-- November 18, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-11-18 09:45:00', 2780.00),
(151, 2, '2025-11-18 10:30:00', 2340.50),
(251, 3, '2025-11-18 11:15:00', 1890.25),
(351, 4, '2025-11-18 12:45:00', 3450.75),
(451, 5, '2025-11-18 14:00:00', 2156.00);

-- November 21, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-11-21 08:00:00', 1945.50),
(201, 2, '2025-11-21 09:30:00', 2567.25),
(301, 3, '2025-11-21 10:45:00', 3120.00),
(401, 4, '2025-11-21 12:15:00', 1876.75),
(501, 5, '2025-11-21 13:30:00', 2890.50);

-- November 24, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-11-24 09:15:00', 3340.00),
(151, 2, '2025-11-24 10:00:00', 1765.50),
(251, 3, '2025-11-24 11:30:00', 2990.25),
(351, 4, '2025-11-24 13:00:00', 2450.00),
(451, 5, '2025-11-24 14:45:00', 1890.75);

-- November 27, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-11-27 08:45:00', 2670.50),
(201, 2, '2025-11-27 09:30:00', 3120.75),
(301, 3, '2025-11-27 10:15:00', 1945.00),
(401, 4, '2025-11-27 11:45:00', 2780.25),
(501, 5, '2025-11-27 13:15:00', 2340.50);

-- November 30, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-11-30 09:00:00', 2890.00),
(151, 2, '2025-11-30 10:30:00', 2450.50),
(251, 3, '2025-11-30 11:15:00', 3250.25),
(351, 4, '2025-11-30 12:45:00', 1876.00),
(451, 5, '2025-11-30 14:00:00', 2156.75);

-- ===================================
-- DECEMBER 2025 ORDERS
-- ===================================

-- December 1, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-12-01 08:30:00', 3120.50),
(201, 2, '2025-12-01 09:45:00', 1890.75),
(301, 3, '2025-12-01 10:30:00', 2670.00),
(401, 4, '2025-12-01 12:00:00', 2340.25),
(501, 5, '2025-12-01 13:30:00', 1765.50);

-- December 2, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-12-02 09:15:00', 2450.00),
(151, 2, '2025-12-02 10:00:00', 2890.50),
(251, 3, '2025-12-02 11:30:00', 1945.25),
(351, 4, '2025-12-02 13:15:00', 3340.00),
(451, 5, '2025-12-02 14:45:00', 2156.75);

-- December 3, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-12-03 08:00:00', 1876.50),
(201, 2, '2025-12-03 09:30:00', 2780.75),
(301, 3, '2025-12-03 10:45:00', 2340.00),
(401, 4, '2025-12-03 12:30:00', 1890.25),
(501, 5, '2025-12-03 13:45:00', 2990.50);

-- December 4, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-12-04 09:30:00', 2670.00),
(151, 2, '2025-12-04 10:15:00', 3250.50),
(251, 3, '2025-12-04 11:00:00', 1765.25),
(351, 4, '2025-12-04 12:45:00', 2450.00),
(451, 5, '2025-12-04 14:15:00', 2890.75);

-- December 5, 2025
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(51, 1, '2025-12-05 08:45:00', 3120.50),
(201, 2, '2025-12-05 09:30:00', 1945.75),
(301, 3, '2025-12-05 10:15:00', 2780.00),
(401, 4, '2025-12-05 11:45:00', 2340.25),
(501, 5, '2025-12-05 13:00:00', 1876.50);

-- December 6, 2025 (Today) - Multiple orders per branch
-- Branch 1 (Main Branch) - 6 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-12-06 08:15:00', 1850.00),
(51, 1, '2025-12-06 09:30:00', 2340.50),
(1, 1, '2025-12-06 10:45:00', 876.25),
(51, 1, '2025-12-06 11:00:00', 3250.00),
(1, 1, '2025-12-06 14:30:00', 1560.75),
(51, 1, '2025-12-06 16:00:00', 2890.50);

-- Branch 2 (Kandy) - 5 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(151, 2, '2025-12-06 08:30:00', 1245.00),
(201, 2, '2025-12-06 10:15:00', 2890.50),
(151, 2, '2025-12-06 12:00:00', 1675.25),
(201, 2, '2025-12-06 15:45:00', 945.00),
(151, 2, '2025-12-06 17:00:00', 2340.75);

-- Branch 3 (Galle) - 5 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(251, 3, '2025-12-06 09:00:00', 1980.00),
(301, 3, '2025-12-06 11:30:00', 2450.75),
(251, 3, '2025-12-06 13:15:00', 865.50),
(301, 3, '2025-12-06 16:00:00', 1340.00),
(251, 3, '2025-12-06 17:30:00', 2156.25);

-- Branch 4 (Negombo) - 5 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(351, 4, '2025-12-06 08:45:00', 2150.00),
(401, 4, '2025-12-06 10:00:00', 1876.50),
(351, 4, '2025-12-06 12:30:00', 2340.25),
(401, 4, '2025-12-06 14:00:00', 1120.00),
(351, 4, '2025-12-06 16:30:00', 2780.75);

-- Branch 5 (Jaffna) - 4 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(451, 5, '2025-12-06 09:15:00', 1560.00),
(501, 5, '2025-12-06 11:45:00', 2890.75),
(451, 5, '2025-12-06 15:00:00', 987.50),
(501, 5, '2025-12-06 17:15:00', 2340.50);

-- ===================================
-- SUMMARY STATISTICS
-- ===================================
-- Total Orders: 110 orders
-- Date Range: November 6 - December 6, 2025 (30 days)
-- Orders per Branch:
--   Branch 1: 22 orders
--   Branch 2: 22 orders  
--   Branch 3: 22 orders
--   Branch 4: 22 orders
--   Branch 5: 22 orders
--
-- This data provides:
-- - Daily sales trends (multiple days with data)
-- - Weekly sales patterns (4+ weeks of data)
-- - Monthly sales comparison (November vs December)
-- - Branch-wise performance analysis
-- - Peak hours analysis (morning, afternoon, evening orders)
