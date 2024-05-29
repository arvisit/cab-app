FROM postgres:15-alpine

COPY docker/postgres/01-setup-extensions.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/01-setup-extensions.sh
