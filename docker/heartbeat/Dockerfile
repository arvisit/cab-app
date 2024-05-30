FROM docker.elastic.co/beats/heartbeat:7.17.21

COPY docker/heartbeat/heartbeat.yml /usr/share/heartbeat/heartbeat.yml
USER root
RUN chown root:heartbeat /usr/share/heartbeat/heartbeat.yml
RUN chmod go-w /usr/share/heartbeat/heartbeat.yml
USER heartbeat
