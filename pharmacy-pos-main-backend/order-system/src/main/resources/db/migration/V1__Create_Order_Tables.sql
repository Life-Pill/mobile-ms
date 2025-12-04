-- Flyway Migration V1: Create Order Service Tables
-- Order Service - Handles orders, payments, and transactions

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL PRIMARY KEY,
    employer_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DOUBLE PRECISION NOT NULL DEFAULT 0.00
);

-- Create order_details table
CREATE TABLE IF NOT EXISTS order_details (
    order_details_id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_order_details_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Create payment_details table
CREATE TABLE IF NOT EXISTS payment_details (
    payment_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_amount DOUBLE PRECISION NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_notes TEXT,
    payment_discount DOUBLE PRECISION DEFAULT 0.00,
    paid_amount DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_payment_details_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_orders_employer_id ON orders(employer_id);
CREATE INDEX IF NOT EXISTS idx_orders_branch_id ON orders(branch_id);
CREATE INDEX IF NOT EXISTS idx_orders_order_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_order_details_order_id ON order_details(order_id);
CREATE INDEX IF NOT EXISTS idx_order_details_item_id ON order_details(item_id);
CREATE INDEX IF NOT EXISTS idx_payment_details_order_id ON payment_details(order_id);
CREATE INDEX IF NOT EXISTS idx_payment_details_payment_date ON payment_details(payment_date);

-- Add comments
COMMENT ON TABLE orders IS 'Stores customer orders with references to Identity and Branch services';
COMMENT ON TABLE order_details IS 'Stores order line items with references to Inventory service';
COMMENT ON TABLE payment_details IS 'Stores payment information for orders';
