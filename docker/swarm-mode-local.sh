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
# docker-compose pull adminserver api-gateway authserver circuit-breaker command-side-blog command-side-project configserver query-side-blog query-side-project registry
# docker-compose bundle -o micro-company.dsb

#List all nodes
echo "-------------------------"
echo "######### Nodes: ########"
echo "-------------------------"
docker node ls

# Create a stack using docker deploy command
docker deploy --file micro-company.dsb micro-company

# List all services
echo "-------------------------"
echo "####### Services: #######"
echo "-------------------------"
docker service ls

# Explore the API
echo "-------------------------"
echo "#########################"
echo "#### Explore the API: ###" 
echo "#########################"
echo "Please have patience, containers need some time to start. You can run this command to list all your services (with current status): docker service ls"
echo "-------------------------"
echo "-------------------------------------------------------------"
echo "### Visit in browser and listen to events via web socket: ###"
echo "-------------------------------------------------------------"
echo " $(docker-machine ip swmaster):$(docker service inspect --format='{{ (index (index .Endpoint.Ports) 0).PublishedPort}}'  micro-company_api-gateway)/socket/index.html"
echo "-------------------------"
echo "### Create Blog Post: ###"
echo "-------------------------"
echo "curl -H \"Content-Type: application/json\" -X POST -d '{\"title\":\"xyz\",\"rawContent\":\"xyz\",\"publicSlug\": \"publicslug\",\"draft\": true,\"broadcast\": true,\"category\": \"ENGINEERING\", \"publishAt\": \"2016-12-23T14:30:00+00:00\"}' $(docker-machine ip swmaster):$(docker service inspect --format='{{ (index (index .Endpoint.Ports) 0).PublishedPort}}'  micro-company_api-gateway)/command/blog/blogpostcommands"
echo "-----------------------------"
echo "#### Read all blog posts: ####"
echo "-----------------------------"
echo "curl $(docker-machine ip swmaster):$(docker service inspect --format='{{ (index (index .Endpoint.Ports) 0).PublishedPort}}'  micro-company_api-gateway)/query/blog/blogposts"

echo "-------------------------------"
echo "######## Have fun ! ###########"
echo "-------------------------------"
echo "### Scale service  'scale micro-company_api-gateway' ###"
echo "eval $(docker-machine env swmaster)"
echo "docker service scale micro-company_api-gateway=2"
echo "docker service ps micro-company_api-gateway"
