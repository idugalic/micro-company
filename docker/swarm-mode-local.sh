#!/bin/bash

set -e

# Docker Machine Setup - master
docker-machine create \
 	-d virtualbox \
 	swmaster
# Docker Machine Setup - node 1
docker-machine create \
 	-d virtualbox \
 	swnode1
# Docker Machine Setup - node 2
docker-machine create \
 	-d virtualbox \
 	swnode2
# Docker Machine Setup - node 3
 docker-machine create \
 	-d virtualbox \
 	swnode3

# Point your local docker client to the swarm master
eval $(docker-machine env swmaster)

# Configure swarm mode cluster - initialization on master
docker $(docker-machine config swmaster) swarm init \
	--advertise-addr $(docker-machine ip swmaster):2377


# Configure swarm mode cluster - join nodes
docker $(docker-machine config swnode1) swarm join --token $(docker swarm join-token --quiet worker) $(docker-machine ip swmaster):2377
docker $(docker-machine config swnode2) swarm join --token $(docker swarm join-token --quiet worker) $(docker-machine ip swmaster):2377
docker $(docker-machine config swnode3) swarm join --token $(docker swarm join-token --quiet worker) $(docker-machine ip swmaster):2377

# Create Bundle from compose file
#docker-compose bundle -o micro-company.dsb

#List all nodes
docker node ls

# Create a stack using docker deploy command
docker deploy --file micro-company.dsb micro-company

# List all services
docker service ls

# Inspect task. Please check the PublishedPort and use it to test the API latter on.
docker service inspect micro-company_api-gateway

# Scale service  'scale micro-company_api-gateway'
# docker idugalic$ docker service scale micro-company_api-gateway=2
# docker service ps micro-company_api-gateway
# List all services
docker service ls

# CURL. Please note to use PublishedPort instead of 30006
# curl $(docker-machine ip swmaster):30006/info
# curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' $(docker-machine ip swmaster):30006/command/blog/blogposts
# curl $(docker-machine ip swmaster):30006/query/blog/blogposts
# Visit $(docker-machine ip swmaster):30006/socket/index.html (http://192.168.99.100:30006/socket/index.html) and listen to all events via Web socket.

# Remove machines
# docker-machine rm swmaster
# docker-machine rm swnode1
# docker-machine rm swnode2
# docker-machine rm swnode3
