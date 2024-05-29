FROM docker.elastic.co/beats/metricbeat:7.7.1

COPY docker/metricbeat/metricbeat.yml /usr/share/metricbeat/metricbeat.yml
USER root
RUN chown root:metricbeat /usr/share/metricbeat/metricbeat.yml
USER metricbeat
