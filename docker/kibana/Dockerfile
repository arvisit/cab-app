FROM docker.elastic.co/kibana/kibana:7.17.21

COPY docker/kibana/init-index-patterns.sh /usr/share/kibana/init-index-patterns.sh
USER root
RUN chmod +x /usr/share/kibana/init-index-patterns.sh
USER kibana

CMD ["/bin/bash", "-c", "/usr/share/kibana/bin/kibana & /usr/share/kibana/init-index-patterns.sh"]
