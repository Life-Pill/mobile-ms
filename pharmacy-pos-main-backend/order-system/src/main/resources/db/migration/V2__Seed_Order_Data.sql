-- Flyway Migration V2: Seed Order Service Data
-- Sample orders for testing and demonstration

-- Insert sample orders
-- Note: employer_id and branch_id reference data in Identity and Branch services
INSERT INTO orders (employer_id, branch_id, order_date, total) VALUES
(1, 1, '2025-12-01 09:15:00', 1250.50),
(1, 1, '2025-12-01 10:30:00', 875.00),
(2, 1, '2025-12-01 11:45:00', 2150.75),
(1, 2, '2025-12-01 14:20:00', 560.25),
(3, 2, '2025-12-01 15:00:00', 1890.00),
(2, 3, '2025-12-02 08:30:00', 450.00),
(1, 1, '2025-12-02 09:00:00', 3250.00),
(3, 3, '2025-12-02 10:15:00', 780.50),
(2, 2, '2025-12-02 11:30:00', 1125.75),
(1, 4, '2025-12-02 13:45:00', 2340.00),
(2, 1, '2025-12-03 09:00:00', 1560.25),
(3, 2, '2025-12-03 10:30:00', 890.00),
(1, 3, '2025-12-03 11:15:00', 1780.50),
(2, 4, '2025-12-03 14:00:00', 2150.00),
(1, 5, '2025-12-03 15:30:00', 670.75),
(3, 1, '2025-12-04 08:45:00', 1350.00),
(2, 2, '2025-12-04 09:30:00', 2890.25),
(1, 3, '2025-12-04 10:00:00', 1245.50),
(3, 4, '2025-12-04 11:20:00', 1875.00),
(2, 5, '2025-12-04 12:00:00', 980.00);

-- Insert order details (item_id references items in Inventory Service)
-- Order 1 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(1, 1, 'Paracetamol 500mg', 150.00),
(1, 2, 'Amoxicillin 500mg', 450.50),
(1, 3, 'Vitamin C 1000mg', 350.00),
(1, 4, 'Omeprazole 20mg', 300.00);

-- Order 2 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(2, 1, 'Paracetamol 500mg', 225.00),
(2, 5, 'Metformin 500mg', 650.00);

-- Order 3 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(3, 2, 'Amoxicillin 500mg', 900.00),
(3, 3, 'Vitamin C 1000mg', 500.75),
(3, 6, 'Ibuprofen 400mg', 750.00);

-- Order 4 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(4, 1, 'Paracetamol 500mg', 210.25),
(4, 4, 'Omeprazole 20mg', 350.00);

-- Order 5 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(5, 2, 'Amoxicillin 500mg', 890.00),
(5, 5, 'Metformin 500mg', 1000.00);

-- Order 6 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(6, 3, 'Vitamin C 1000mg', 450.00);

-- Order 7 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(7, 1, 'Paracetamol 500mg', 500.00),
(7, 2, 'Amoxicillin 500mg', 1250.00),
(7, 4, 'Omeprazole 20mg', 800.00),
(7, 6, 'Ibuprofen 400mg', 700.00);

-- Order 8 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(8, 5, 'Metformin 500mg', 780.50);

-- Order 9 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(9, 3, 'Vitamin C 1000mg', 625.75),
(9, 4, 'Omeprazole 20mg', 500.00);

-- Order 10 items
INSERT INTO order_details (order_id, item_id, name, amount) VALUES
(10, 1, 'Paracetamol 500mg', 340.00),
(10, 2, 'Amoxicillin 500mg', 1200.00),
(10, 6, 'Ibuprofen 400mg', 800.00);

-- Insert payment details for each order
INSERT INTO payment_details (order_id, payment_method, payment_amount, payment_date, payment_notes, payment_discount, paid_amount) VALUES
(1, 'CASH', 1250.50, '2025-12-01 09:15:00', 'Regular customer', 0.00, 1250.50),
(2, 'CARD', 875.00, '2025-12-01 10:30:00', NULL, 0.00, 875.00),
(3, 'CASH', 2150.75, '2025-12-01 11:45:00', 'Corporate account', 150.75, 2000.00),
(4, 'CARD', 560.25, '2025-12-01 14:20:00', NULL, 0.00, 560.25),
(5, 'CASH', 1890.00, '2025-12-01 15:00:00', 'Loyalty discount applied', 90.00, 1800.00),
(6, 'CASH', 450.00, '2025-12-02 08:30:00', NULL, 0.00, 450.00),
(7, 'CARD', 3250.00, '2025-12-02 09:00:00', 'Large order', 250.00, 3000.00),
(8, 'CASH', 780.50, '2025-12-02 10:15:00', NULL, 0.00, 780.50),
(9, 'CARD', 1125.75, '2025-12-02 11:30:00', 'Insurance claim', 125.75, 1000.00),
(10, 'CASH', 2340.00, '2025-12-02 13:45:00', NULL, 0.00, 2340.00),
(11, 'CARD', 1560.25, '2025-12-03 09:00:00', NULL, 60.25, 1500.00),
(12, 'CASH', 890.00, '2025-12-03 10:30:00', NULL, 0.00, 890.00),
(13, 'CARD', 1780.50, '2025-12-03 11:15:00', 'Premium customer', 80.50, 1700.00),
(14, 'CASH', 2150.00, '2025-12-03 14:00:00', NULL, 0.00, 2150.00),
(15, 'CARD', 670.75, '2025-12-03 15:30:00', NULL, 0.00, 670.75),
(16, 'CASH', 1350.00, '2025-12-04 08:45:00', NULL, 50.00, 1300.00),
(17, 'CARD', 2890.25, '2025-12-04 09:30:00', 'Hospital account', 190.25, 2700.00),
(18, 'CASH', 1245.50, '2025-12-04 10:00:00', NULL, 0.00, 1245.50),
(19, 'CARD', 1875.00, '2025-12-04 11:20:00', NULL, 75.00, 1800.00),
(20, 'CASH', 980.00, '2025-12-04 12:00:00', 'Cash payment', 0.00, 980.00);
