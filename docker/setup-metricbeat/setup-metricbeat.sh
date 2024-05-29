#!/bin/bash

# Wait for Kibana to be up and running
until curl --output /dev/null --silent --head --fail http://kibana:5601; do
  printf '.'
  sleep 5
done

# Check if dashboard already exists
response=$(curl -s -X GET "http://kibana:5601/api/saved_objects/_find?type=dashboard")
dashboardCount=$(echo "$response" | jq '.saved_objects | length')

if [ "$dashboardCount" -gt 0 ]; then
  echo "Dashboards already exist, skipping setup."
else
  echo "Dashboards do not exist, running setup."
  metricbeat setup -E setup.kibana.host=kibana:5601 -E output.elasticsearch.hosts=["elasticsearch:9200"]
fi
