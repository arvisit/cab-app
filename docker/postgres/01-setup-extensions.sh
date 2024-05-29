#!/bin/bash
set -e

# create pg_stat_statements extension
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS pg_stat_statements;
EOSQL

# update PostgreSQL configuration to enable pg_stat_statements
{
    echo "shared_preload_libraries = 'pg_stat_statements'";
    echo "pg_stat_statements.track = all";
} >> "${PGDATA}/postgresql.conf"
