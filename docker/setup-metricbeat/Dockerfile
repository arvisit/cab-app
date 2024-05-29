FROM docker.elastic.co/beats/metricbeat:7.7.1

USER root
RUN yum install epel-release -y
RUN yum install jq -y

COPY docker/setup-metricbeat/setup-metricbeat.sh /usr/share/metricbeat/setup-metricbeat.sh
RUN chmod +x /usr/share/metricbeat/setup-metricbeat.sh
USER metricbeat
CMD ["/usr/share/metricbeat/setup-metricbeat.sh"]
