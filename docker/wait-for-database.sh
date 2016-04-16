#! /bin/bash

host=$1
port=$2

while ! curl http://${host}:${port}/
do
  echo "$(date) - still trying"
  sleep 1
done

echo "$(date) - connected successfully"



