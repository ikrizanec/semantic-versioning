#!/bin/bash

response=$1
expected=$2

echo "Response: $response"
echo "Expected: $expected"
if [ "$response" != "$expected" ]; then
  echo "::error::Test case failed ($response != $expected)."
  i=$(cat failure) && echo $((++i)) > failure
  exit 1
fi

echo "Test case passed"
i=$(cat success) && echo $((++i)) > success
