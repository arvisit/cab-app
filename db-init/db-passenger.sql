SELECT 'CREATE DATABASE passenger_service_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passenger_service_db')\gexec
