#!/bin/bash

kibanaUrl=http://localhost:5601
# Wait for Kibana to be up and running
until curl --output /dev/null --silent --head --fail $kibanaUrl; do
  printf '.'
  sleep 5
done

for indexFile in index-patterns/*.json; do
    indexTitle="${indexFile##*/}"
    indexTitle="${indexTitle%%*.}-*"
    response=$(curl -s -o /dev/null -w "%{http_code}" -X GET "$kibanaUrl/api/saved_objects/index-pattern/$indexTitle")

    # Create index pattern in Kibana
    if [ "$response" -eq 200 ]; then
        echo "Index pattern $indexTitle already exists."
    else
        curl -X POST "$kibanaUrl/api/saved_objects/index-pattern/$indexTitle" \
          -H 'kbn-xsrf: true' \
          -H 'Content-Type: application/json' \
          -d @/usr/share/kibana/"$indexFile"
        echo "Index pattern $indexTitle created"
    fi
done

tail -f /dev/null
