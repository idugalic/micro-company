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
# Configure swarm mode cluster - initialization on master
docker $(docker-machine config swmaster) swarm init \
	--listen-addr $(docker-machine ip swmaster):2377
# Configure swarm mode cluster - join nodes
docker $(docker-machine config swnode1) swarm join $(docker-machine ip swmaster):2377
docker $(docker-machine config swnode2) swarm join $(docker-machine ip swmaster):2377
docker $(docker-machine config swnode3) swarm join $(docker-machine ip swmaster):2377
# Create Bundle from compose file
docker-compose bundle -o micro-company.dsb
# Point your local docker client to the swarm master 
eval $(docker-machine env swmaster)
# Create a stack using docker deploy command
docker deploy -f micro-company.dsb micro-company
# List all services
docker service ls
# Inspect task
docker service inspect micro-company_api-gateway
# CURL
# curl $(docker-machine ip swmaster):30000/info
# curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' $(docker-machine ip swmaster):30000/command/blog/blogposts
# curl $(docker-machine ip swmaster):30000/query/blog/blogposts
# Visit $(docker-machine ip swmaster):30000/socket/index.html and listen to all events via Web socket.
