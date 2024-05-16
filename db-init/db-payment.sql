SELECT 'CREATE DATABASE payment_service_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'payment_service_db')\gexec
