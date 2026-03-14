CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO employees (first_name, last_name, email, password, phone, position, role, hire_date, is_active)
VALUES ('Admin', 'User', 'admin@company.com', crypt('pass123', gen_salt('bf', 10)), '555-0001', 'System Administrator', 'ADMIN', '2024-01-01', true);
