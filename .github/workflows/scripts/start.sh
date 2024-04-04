#!/bin/bash

pathToPom=$1

rm -rf logs.txt
mvn spring-boot:run -f $pathToPom > logs.txt &
while ! grep -q "started on port 8080 (http)" logs.txt; do
  ((i++)) && ((i==300)) && break
  sleep 1
done

if ! grep -q "started on port 8080 (http)" logs.txt; then
  echo "::error::Application failed to start in $i seconds."
  exit 1
fi

echo "Application started!"