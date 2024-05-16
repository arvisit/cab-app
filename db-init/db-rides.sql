SELECT 'CREATE DATABASE rides_service_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'rides_service_db')\gexec
