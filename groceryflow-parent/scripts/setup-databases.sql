-- =====================================================================
-- GroceryFlow: Database Initialization Script for PostgreSQL 17
-- Creates 6 isolated databases for Database-per-Service Architecture
-- Run this in pgAdmin or via psql:
-- psql -U postgres -f scripts/setup-databases.sql
-- =====================================================================

-- Terminate any active connections before dropping if re-running
SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'auth_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS auth_db;
CREATE DATABASE auth_db;

SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'product_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS product_db;
CREATE DATABASE product_db;

SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'inventory_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS inventory_db;
CREATE DATABASE inventory_db;

SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'order_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS order_db;
CREATE DATABASE order_db;

SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'fulfillment_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS fulfillment_db;
CREATE DATABASE fulfillment_db;

SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'payment_db' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS payment_db;
CREATE DATABASE payment_db;

\echo 'All 6 GroceryFlow databases created successfully: auth_db, product_db, inventory_db, order_db, fulfillment_db, payment_db'
