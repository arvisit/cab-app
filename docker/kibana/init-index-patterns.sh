#!/bin/bash

kibanaUrl=http://localhost:5601
# Wait for Kibana to be up and running
until curl --output /dev/null --silent --head --fail $kibanaUrl; do
  printf '.'
  sleep 5
done

for indx in *-index-pattern.json; do
    indexTitle="$(echo "$indx" | cut -d'-' -f1)-*"
    response=$(curl -s -o /dev/null -w "%{http_code}" -X GET "$kibanaUrl/api/saved_objects/index-pattern/$indexTitle")

    # Create index pattern in Kibana
    if [ "$response" -eq 200 ]; then
        echo "Index pattern $indx already exists."
    else
        curl -X POST "$kibanaUrl/api/saved_objects/index-pattern/$indx" \
          -H 'kbn-xsrf: true' \
          -H 'Content-Type: application/json' \
          -d @/usr/share/kibana/"$indx"
        echo "Index pattern $indx created"
    fi
done

tail -f /dev/null
