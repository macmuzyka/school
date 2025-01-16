#!/bin/bash
wait_for_service() {
  local host=$1
  local port=$2

  while ! nc -z $host $port; do
    echo "Waiting for 10 seconds for $host:$port..."
    sleep 10
  done
  echo "$host:$port is up!"
}

wait_for_service informer 9001

echo "Starting the application..."
exec java -jar *.jar
