-- Flyway Migration V3: Seed Today's Order Data for Daily Sales Summary
-- Orders dated today (2025-12-06) for testing daily sales reports

-- Insert orders for today across all branches
-- Branch 1 (LifePill Main Branch) - 5 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-12-06 08:15:00', 1850.00),
(51, 1, '2025-12-06 09:30:00', 2340.50),
(1, 1, '2025-12-06 10:45:00', 876.25),
(51, 1, '2025-12-06 11:00:00', 3250.00),
(1, 1, '2025-12-06 14:30:00', 1560.75);

-- Branch 2 (LifePill Kandy Branch) - 4 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(151, 2, '2025-12-06 08:30:00', 1245.00),
(201, 2, '2025-12-06 10:15:00', 2890.50),
(151, 2, '2025-12-06 12:00:00', 1675.25),
(201, 2, '2025-12-06 15:45:00', 945.00);

-- Branch 3 (LifePill Galle Branch) - 4 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(251, 3, '2025-12-06 09:00:00', 1980.00),
(301, 3, '2025-12-06 11:30:00', 2450.75),
(251, 3, '2025-12-06 13:15:00', 865.50),
(301, 3, '2025-12-06 16:00:00', 1340.00);

-- Branch 4 (LifePill Negombo Branch) - 4 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(351, 4, '2025-12-06 08:45:00', 2150.00),
(401, 4, '2025-12-06 10:00:00', 1876.50),
(351, 4, '2025-12-06 12:30:00', 2340.25),
(401, 4, '2025-12-06 14:00:00', 1120.00);

-- Branch 5 (LifePill Jaffna Branch) - 3 orders
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(451, 5, '2025-12-06 09:15:00', 1560.00),
(501, 5, '2025-12-06 11:45:00', 2890.75),
(451, 5, '2025-12-06 15:00:00', 987.50);

-- Insert order details for today's orders (assuming order IDs start at 21)
-- Branch 1 orders
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(21, 1, 'Paracetamol 500mg', 250.00),
(21, 2, 'Amoxicillin 500mg', 800.00),
(21, 3, 'Vitamin C 1000mg', 450.00),
(21, 6, 'Ibuprofen 400mg', 350.00),
(22, 4, 'Omeprazole 20mg', 890.50),
(22, 5, 'Metformin 500mg', 750.00),
(22, 7, 'Atorvastatin 10mg', 700.00),
(23, 1, 'Paracetamol 500mg', 376.25),
(23, 8, 'Cetirizine 10mg', 500.00),
(24, 2, 'Amoxicillin 500mg', 1250.00),
(24, 3, 'Vitamin C 1000mg', 1000.00),
(24, 9, 'Aspirin 100mg', 1000.00),
(25, 5, 'Metformin 500mg', 860.75),
(25, 10, 'Amlodipine 5mg', 700.00);

-- Branch 2 orders
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(26, 1, 'Paracetamol 500mg', 445.00),
(26, 4, 'Omeprazole 20mg', 800.00),
(27, 2, 'Amoxicillin 500mg', 1290.50),
(27, 6, 'Ibuprofen 400mg', 800.00),
(27, 7, 'Atorvastatin 10mg', 800.00),
(28, 3, 'Vitamin C 1000mg', 875.25),
(28, 8, 'Cetirizine 10mg', 800.00),
(29, 5, 'Metformin 500mg', 945.00);

-- Branch 3 orders
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(30, 1, 'Paracetamol 500mg', 480.00),
(30, 2, 'Amoxicillin 500mg', 750.00),
(30, 9, 'Aspirin 100mg', 750.00),
(31, 3, 'Vitamin C 1000mg', 950.75),
(31, 4, 'Omeprazole 20mg', 750.00),
(31, 10, 'Amlodipine 5mg', 750.00),
(32, 6, 'Ibuprofen 400mg', 465.50),
(32, 7, 'Atorvastatin 10mg', 400.00),
(33, 8, 'Cetirizine 10mg', 640.00),
(33, 5, 'Metformin 500mg', 700.00);

-- Branch 4 orders
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(34, 1, 'Paracetamol 500mg', 550.00),
(34, 2, 'Amoxicillin 500mg', 800.00),
(34, 3, 'Vitamin C 1000mg', 800.00),
(35, 4, 'Omeprazole 20mg', 876.50),
(35, 6, 'Ibuprofen 400mg', 500.00),
(35, 9, 'Aspirin 100mg', 500.00),
(36, 5, 'Metformin 500mg', 1140.25),
(36, 7, 'Atorvastatin 10mg', 600.00),
(36, 10, 'Amlodipine 5mg', 600.00),
(37, 8, 'Cetirizine 10mg', 520.00),
(37, 1, 'Paracetamol 500mg', 600.00);

-- Branch 5 orders
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(38, 2, 'Amoxicillin 500mg', 760.00),
(38, 4, 'Omeprazole 20mg', 800.00),
(39, 3, 'Vitamin C 1000mg', 990.75),
(39, 5, 'Metformin 500mg', 900.00),
(39, 6, 'Ibuprofen 400mg', 1000.00),
(40, 1, 'Paracetamol 500mg', 487.50),
(40, 7, 'Atorvastatin 10mg', 500.00);

-- Insert payment details for today's orders
INSERT INTO payment_details (order_id, payment_method, payment_amount, payment_date, payment_notes, payment_discount, paid_amount) VALUES
(21, 'CASH', 1850.00, '2025-12-06 08:15:00', 'Morning sale', 0.00, 1850.00),
(22, 'CARD', 2340.50, '2025-12-06 09:30:00', 'Card payment', 40.50, 2300.00),
(23, 'CASH', 876.25, '2025-12-06 10:45:00', NULL, 0.00, 876.25),
(24, 'CARD', 3250.00, '2025-12-06 11:00:00', 'Large order', 250.00, 3000.00),
(25, 'CASH', 1560.75, '2025-12-06 14:30:00', 'Afternoon sale', 60.75, 1500.00),
(26, 'CARD', 1245.00, '2025-12-06 08:30:00', NULL, 0.00, 1245.00),
(27, 'CASH', 2890.50, '2025-12-06 10:15:00', 'Corporate purchase', 90.50, 2800.00),
(28, 'CARD', 1675.25, '2025-12-06 12:00:00', 'Insurance claim', 75.25, 1600.00),
(29, 'CASH', 945.00, '2025-12-06 15:45:00', NULL, 0.00, 945.00),
(30, 'CARD', 1980.00, '2025-12-06 09:00:00', 'Regular customer', 80.00, 1900.00),
(31, 'CASH', 2450.75, '2025-12-06 11:30:00', NULL, 50.75, 2400.00),
(32, 'CARD', 865.50, '2025-12-06 13:15:00', NULL, 0.00, 865.50),
(33, 'CASH', 1340.00, '2025-12-06 16:00:00', 'End of day', 40.00, 1300.00),
(34, 'CARD', 2150.00, '2025-12-06 08:45:00', 'First customer', 50.00, 2100.00),
(35, 'CASH', 1876.50, '2025-12-06 10:00:00', NULL, 0.00, 1876.50),
(36, 'CARD', 2340.25, '2025-12-06 12:30:00', 'Bulk order', 140.25, 2200.00),
(37, 'CASH', 1120.00, '2025-12-06 14:00:00', NULL, 20.00, 1100.00),
(38, 'CARD', 1560.00, '2025-12-06 09:15:00', NULL, 60.00, 1500.00),
(39, 'CASH', 2890.75, '2025-12-06 11:45:00', 'Premium customer', 90.75, 2800.00),
(40, 'CASH', 987.50, '2025-12-06 15:00:00', 'Afternoon sale', 0.00, 987.50);
