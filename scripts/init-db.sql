-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15),
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Create alerts table
CREATE TABLE alerts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    symbol VARCHAR(300) NOT NULL,
    condition VARCHAR(20) NOT NULL, -- 'price_above', 'price_below'
    target_price NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'active', -- 'active', 'triggered', 'notified'
    created_at TIMESTAMP DEFAULT NOW(),
    triggered_at TIMESTAMP,
    notified_at TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_alerts_symbol ON alerts(symbol);
CREATE INDEX idx_alerts_status ON alerts(status);
CREATE INDEX idx_alerts_user_id ON alerts(user_id);

-- Seed test user (hardcoded user for demo)
INSERT INTO users (id, email, name,phone) VALUES
    (1, 'demo@example.com', 'Demo User','+917039774564');

-- Seed some test alerts
INSERT INTO alerts (user_id, symbol, condition, target_price, status) VALUES
    (1, 'AAPL', 'price_below', 145.00, 'active'),
    (1, 'GOOGL', 'price_above', 2850.00, 'active'),
    (1, 'TSLA', 'price_below', 200.00, 'active');

ALTER TABLE alerts
ALTER COLUMN symbol TYPE VARCHAR(30);

