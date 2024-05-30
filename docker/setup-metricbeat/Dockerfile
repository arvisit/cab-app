FROM docker.elastic.co/beats/metricbeat:7.17.21

USER root
RUN apt install jq -y

COPY docker/setup-metricbeat/setup-metricbeat.sh /usr/share/metricbeat/setup-metricbeat.sh
RUN chmod +x /usr/share/metricbeat/setup-metricbeat.sh
USER metricbeat
CMD ["/usr/share/metricbeat/setup-metricbeat.sh"]
