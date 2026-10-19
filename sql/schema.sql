-- ==========================================================
-- Travel App Database Initialization Script
-- Target Table: customer_data
-- Compatible with: MySQL 8.x (AWS EC2 / RDS / Local)
-- ==========================================================

-- 1. Create Database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS travel_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE travel_db;

-- 2. Create customer_data Table
CREATE TABLE IF NOT EXISTS customer_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    bmi DECIMAL(5,2) NOT NULL,
    desired_country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Optional: Create Indexes for fast querying
CREATE INDEX idx_customer_country ON customer_data(desired_country);
CREATE INDEX idx_customer_created_at ON customer_data(created_at);

-- 4. Sample Seed Data (For Testing)
INSERT INTO customer_data (name, age, gender, bmi, desired_country) VALUES
('Sophia Martinez', 28, 'Female', 21.45, 'Japan'),
('Alexander Wright', 34, 'Male', 24.12, 'Switzerland'),
('Priya Sharma', 26, 'Female', 22.80, 'Italy')
ON DUPLICATE KEY UPDATE id=id;
