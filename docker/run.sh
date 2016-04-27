#! /bin/bash

set -e


# Export the active docker machine IP
 export DOCKER_HOST_IP=$(docker-machine ip $(docker-machine active))
 # docker-machine doesn't exist in Linux, assign default ip if it's not set
 DOCKER_HOST_IP=${DOCKER_HOST_IP:-0.0.0.0}
 echo set DOCKER_HOST_IP $DOCKER_HOST_IP

DOCKER_COMPOSE="docker-compose -p micro-company"

if [ "$1" = "--use-existing" ] ; then
  shift;
else
  ${DOCKER_COMPOSE?} stop
  ${DOCKER_COMPOSE?} rm -v --force
fi

${DOCKER_COMPOSE?} up -d my-mongo my-rabbit
./wait-for-database.sh $DOCKER_HOST_IP 27017
./wait-for-database.sh $DOCKER_HOST_IP 15672     
   

${DOCKER_COMPOSE?} up -d configserver
./wait-for-services.sh $DOCKER_HOST_IP 8888

${DOCKER_COMPOSE?} up -d registry authserver
./wait-for-services.sh $DOCKER_HOST_IP 8761 9999

${DOCKER_COMPOSE?} up -d




