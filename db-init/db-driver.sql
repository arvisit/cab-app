SELECT 'CREATE DATABASE driver_service_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'driver_service_db')\gexec
