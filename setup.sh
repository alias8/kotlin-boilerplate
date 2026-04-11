#!/bin/bash
docker-compose up -d db
echo "Waiting for Postgres..."
until docker-compose exec db pg_isready -U jameskirk -d featureflags > /dev/null 2>&1; do
  sleep 1
done
docker-compose up -d app
echo "Done! Test: curl -N -H 'Authorization: Bearer test-token-1' http://localhost:8080/stream"
