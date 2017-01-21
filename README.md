# Application 'Micro Company' ![status](https://travis-ci.org/idugalic/micro-company.svg?branch=master)

This project is intended to demonstrate end-to-end best practices for building a cloud native, event driven microservice architecture using Spring Cloud.

## Table of Contents

   * [Application 'Micro Company'](#application-micro-company-)
      * [What is cloud native](#what-is-cloud-native)
      * [Architecture](#architecture)
         * [Patterns and techniques:](#patterns-and-techniques)
         * [Technologies](#technologies)
         * [Key benefits](#key-benefits)
         * [How it works](#how-it-works)
         * [Services](#services)
            * [Backing services](#backing-services)
               * [(Service) Registry](#service-registry)
               * [Authorization server (Oauth2)](#authorization-server-oauth2)
               * [Configuration server](#configuration-server)
               * [API Gateway](#api-gateway)
            * [Backend Microservices](#backend-microservices)
               * [BlogMicroservice](#blogmicroservice)
               * [ProjectMicroservice](#projectmicroservice)
               * [Admin server (<a href="http://codecentric.github.io/spring-boot-admin/1.3.2/">http://codecentric.github.io/spring-boot-admin/1.3.2/</a>)](#admin-server-httpcodecentricgithubiospring-boot-admin132)
      * [Running instructions](#running-instructions)
         * [Prerequisite](#prerequisite)
         * [Step 1: Clone the project](#step-1-clone-the-project)
         * [Step 2 (Optional): Build the project](#step-2-optional-build-the-project)
            * [Build the project](#build-the-project)
            * [Build docker images via maven](#build-docker-images-via-maven)
         * [Step 3: Run the application as](#step-3-run-the-application-as)
            * [Run monolithic on localhost](#run-monolithic-on-localhost)
               * [Docker](#docker)
               * [Maven](#maven)
               * [Eclipse](#eclipse)
            * [Run microservices on localhost via docker](#run-microservices-on-localhost-via-docker)
            * [Run microservices on Docker Swarm (mode) - Local cluster](#run-microservices-on-docker-swarm-mode---local-cluster)
            * [Run microservices on Docker Swarm (mode) - AWS cluster](#run-microservices-on-docker-swarm-mode---aws-cluster)
            * [Run microservices on Pivotal Cloud Foundry - PCF Dev](#run-microservices-on-pivotal-cloud-foundry---pcf-dev)
               * [CLI](#cli)
               * [Eclipse](#eclipse-1)
         * [Issuing Commands &amp; Queries with CURL](#issuing-commands--queries-with-curl)
            * [Create Blog post](#create-blog-post)
                  * [Microservices](#microservices)
                  * [Monolithic](#monolithic)
            * [Publish Blog post](#publish-blog-post)
                  * [Microservices](#microservices-1)
                  * [Monolithic](#monolithic-1)
            * [Query Blog posts](#query-blog-posts)
                  * [Microservices](#microservices-2)
                  * [Monolithic](#monolithic-2)
            * [Create Project](#create-project)
                  * [Microservices](#microservices-3)
                  * [Monolithic](#monolithic-3)
            * [Query Projects](#query-projects)
                  * [Microservices](#microservices-4)
                  * [Monolithic](#monolithic-4)
            * [WebSocket on the gateway](#websocket-on-the-gateway)
                  * [Microservices](#microservices-5)
      * [About AXON](#about-axon)
         * [Commands](#commands)
         * [Events](#events)
         * [Domain](#domain)
         * [Repositories](#repositories)
         * [Event Stores](#event-stores)
         * [Event Bus](#event-bus)
         * [Sagas](#sagas)
         * [Testing](#testing)
         * [Spring support](#spring-support)
      * [References and further reading](#references-and-further-reading)

## What is cloud native

To understand “cloud native,” we must first understand “cloud.”
In the context of this application, cloud refers to Platform as a Service. PaaS providers expose a platform that hides infrastructure details from the application developer, where that platform resides on top of Infrastructure as a Service (IaaS). 

A cloud-native application is an application that has been designed and implemented to run on a Platform-as-a-Service installation and to embrace horizontal elastic scaling.

## Architecture

The microservice architectural style is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API or via events (event-driven).

Microservices enable businesses to innovate faster and stay ahead of the competition. But one major challenge with the microservices architecture is the management of distributed data. Each microservice has its own private database. It is difficult to implement business transactions that maintain data consistency across multiple services as well as queries that retrieve data from multiple services.

![Microservice Architecture - Ivan Dugalic](https://i.imgsafe.org/cb15eb4553.png)

### Patterns and techniques:

1. Microservices
2. Command and Query Responsibility Separation (CQRS)
3. DDD - Event Sourcing
4. DDD - Agregates

### Technologies

- [Spring Boot](http://projects.spring.io/spring-boot/) (v1.4.1.RELEASE)
- [Spring Cloud](http://projects.spring.io/spring-cloud/)
- [Spring Data](http://projects.spring.io/spring-data/)
- [Spring Data REST](http://projects.spring.io/spring-data-rest/)
- [Axon Framework](http://www.axonframework.org/) (v3.0-RC1)
- [RabbitMQ](https://www.rabbitmq.com/) (v3.5.4) Axon supports any Spring AMQP supported platform.
- [Docker](https://www.docker.com/) (v1.13.0-rc2-beta3)


### Key benefits

1. Easy implementation of eventually consistent business transactions that span multiple microservices
2. Automatic publishing of events whenever data changes
3. Faster and more scalable querying by using materialized views
4. Reliable auditing for all updates


### How it works

The domain is literally split into a *command-side* microservice application and a *query-side* microservice application (this is CQRS in its most literal form).

Communication between the two microservices is `event-driven` and the demo uses RabbitMQ messaging as a means of passing the events between processes (VM's).

The **command-side** processes commands. Commands are actions which change state in some way. The execution of these commands results in `Events` being generated which are persisted by Axon  and propagated out to other VM's (as many VM's as you like) via RabbitMQ messaging. In event-sourcing, events are the sole records in the system. They are used by the system to describe and re-build aggregates on demand, one event at a time.

The **query-side** is an event-listener and processor. It listens for the `Events` and processes them in whatever way makes the most sense. In this application, the query-side just builds and maintains a *materialised view* which tracks the state of the individual agregates (Product, Blog, Customer, ...). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues are durable, so they can be temporarily stored on behalf of the event-listener if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

Read the [Axon documentation](http://www.axonframework.org/download/) for the finer details of how Axon generally operates to bring you CQRS and Event Sourcing to your apps, as well as lots of detail on how it all get's configured (spoiler: it's mostly spring-context XML for the setup and some Java extensions and annotations within the code).

### Services

#### Backing services

The premise is that there are third-party service dependencies that should be treated as attached resources to your cloud native applications. The key trait of backing services are that they are provided as bindings to an application in its deployment environment by a cloud platform.
Each of the backing services must be located using a statically defined route

##### (Service) Registry
Netflix Eureka is a service registry. It provides a REST API for service instance registration management and for querying available instances. Netflix Ribbon is an IPC client that works with Eureka to load balance(client side) requests across the available service instances.

##### Authorization server (Oauth2)
For issuing tokens and authorize requests.

##### Configuration server
The configuration service is a vital component of any microservices architecture. Based on the twelve-factor app methodology, configurations for your microservice applications should be stored in the environment and not in the project.
Configuration is hosted here: https://github.com/idugalic/micro-company-config.git

##### API Gateway
Implementation of an API gateway that is the single entry point for all clients. The API gateway handles requests in one of two ways. Some requests are simply proxied/routed to the appropriate service. It handles other requests by fanning out to multiple services.


#### Backend Microservices

While the backing services in the middle layer are still considered to be microservices, they solve a set of concerns that are purely operational and security-related. The business logic of this application sits almost entirely in our bottom layer.

##### BlogMicroservice
A Blog service is used for manging and quering the posts of your company. It is split into a *command-side* microservice application and a *query-side* microservice application.

##### ProjectMicroservice
A Project service is used for manging and quering the projects of your company. It is split into a *command-side* microservice application and a *query-side* microservice application.

##### Admin server (http://codecentric.github.io/spring-boot-admin/1.3.2/)
Spring Boot Admin is a simple application to manage and monitor your Spring Boot services. The services are discovered using Spring Cloud (e.g. Eureka). The UI is just an Angular.js application on top of the Spring Boot Actuator endpoints. In case you want to use the more advanced features (e.g. jmx-, loglevel-management), Jolokia must be included in the client services.

Please note that this server/service could fit to 'Backing services' as well. In this case all services would use Admin Client to connect to this service (and not Eureka).


## Running instructions

### Prerequisite

- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Git](https://git-scm.com/) 
- [Docker](https://www.docker.com/)
- VirtualBox

### Step 1: Clone the project

```bash
$ git clone https://github.com/idugalic/micro-company.git
```

### Step 2 (Optional): Build the project
Please note that images are available on the docker hub (https://hub.docker.com/u/idugalic), so if you do not want to build the services, simply skip to Step 3

#### Build the project
 
```bash
$ cd micro-company
$ mvn clean install
```

#### Build docker images via maven

```bash
$ DOCKER_HOST=unix:///var/run/docker.sock mvn docker:build
```

or to build and push images via maven (requires username and password of docker repo):

```bash
$ DOCKER_HOST=unix:///var/run/docker.sock mvn docker:build -DpushImage
```
### Step 3: Run the application as

- monolithic on localhost or
- microservices on localhost via docker or
- microservices on docker swarm (mode) - local cluster
- microservices on docker swarm (mode) - AWS cluster
- microservices on Pivotal Cloud Foundry - PCF Dev

#### Run monolithic on localhost

##### Docker
```bash
$ cd micro-company/docker
$ docker-compose -f docker-compose-monolithic.yml up -d 
```
##### Maven
```bash
$ cd micro-company
$ mvn clean install
$ cd micro-company/monolithic
$ mvn spring-boot:run
```
##### Eclipse
Run as Spring Boot Project. 
I can advice for Boot Dashboard to be used as well.

#### Run microservices on localhost via docker

```bash
$ cd micro-company/docker
$ docker-compose up -d 
```

#### Run microservices on Docker Swarm (mode) - Local cluster

Docker Engine 1.12+ includes swarm mode for natively managing a cluster of Docker Engines called a swarm. https://docs.docker.com/engine/swarm

```bash
$ cd micro-company/docker
$ . ./swarm-mode-local.sh
```
By executing this script you will:

- create 4 virtual machines (VirtualBox is required). One 'swarm master', and three 'swarm nodes'
- initialize cluster on the swarm master
- join nodes to the cluster
- deploy services using a `docker-compose.yml` file directly

Please, follow the instructions in the console log, and have fun :)

##### Experimental features

- Aggregated logs of a service
- Docker build has a new experimental --squash

In version 1.13 the experimental features are now part of the standard binaries and can be enabled by running the Deamon with the --experimental flag. Let’s do just this. First we need to change the dockerd profile and add the flag:
```bash
$ docker-machine ssh swmaster -t sudo vi /var/lib/boot2docker/profile
```
add the --experimental flag to the EXTRA_ARGS variable. In my case the file looks like this after the modification
```
EXTRA_ARGS='
--label provider=virtualbox
--experimental

'
```
Save the changes as reboot the leader node:
```bash
docker-machine stop swmaster
docker-machine start swmaster
```

#### Run microservices on Docker Swarm (mode) - AWS cluster

Docker Engine 1.12+ includes swarm mode for natively managing a cluster of Docker Engines called a swarm. https://docs.docker.com/engine/swarm

We will deploy services on AWS infrastucture. You have to prepare it.
Please note that steps 1 and 3 are optional. You don't have to create users or key pars if you already have them.

- Step 1: Login to your AWS account as a root, and create user (not root) that will be used latter. (http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console)
- Step 2: Login with your new user account https://My_AWS_Account_ID.signin.aws.amazon.com/console/ 
- Step 3: Create Your Key Pair Using Amazon EC2. Please note that the key will be downloaded by the browser. In my case it is '/Users/idugalic/.ssh/idugalic.pem'. (http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#having-ec2-create-your-key-pair).
- Step 4: Create stack on AWS by using CloudFormation template - https://blog.docker.com/2016/11/docker-aws-public-beta/ (https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=Docker&templateURL=https://docker-for-aws.s3.amazonaws.com/aws/beta/aws-v1.13.0-rc2-beta12.json)
- Step 5: Run the shell script bellow and follow instructions.

```bash
$ cd micro-company/docker
$ . ./swarm-mode-aws.sh
```

#### Run microservices on Pivotal Cloud Foundry - PCF Dev
Run services on local workstation with PCF Dev

- Download and install PCF: https://pivotal.io/platform/pcf-tutorials/getting-started-with-pivotal-cloud-foundry-dev/introduction
- Start PCF Dev: `$ cf dev start -m 8192 `
- Login to PCF Dev" `$ cf login -a https://api.local.pcfdev.io --skip-ssl-validation -u admin -p admin -o pcfdev-org`
- Create user service - configserver: `$ cf cups configserver -p '{"uri":"http://configserver.local.pcfdev.io"}'`
- Create user service - registry: `$ cf cups registry -p '{"uri":"http://registry.local.pcfdev.io"}'`
- Create user service - authserver: `$ cf cups authserver -p '{"uri":"http://authserver.local.pcfdev.io"}'`
- Create cloud foundry service instance - mysql: `$ cf create-service p-mysql 512mb mysql`
- Create cloud foundry service instance - rabbit: `$ cf create-service p-rabbitmq standard rabbit`
- Open your browser and point to https://local.pcfdev.io. Explore !


##### CLI
Push microservices in command line:

```bash
$ cd micro-company/
$ mvn clean install
$ cf push -f configserver/manifest.yml -p configserver/target/configserver-0.0.1-SNAPSHOT.jar
$ cf push -f registry/manifest.yml -p registry/target/registry-0.0.1-SNAPSHOT.jar
$ cf push -f authserver/manifest.yml -p authserver/target/authserver-0.0.1-SNAPSHOT.jar
$ cf push -f command-side-blog-service/manifest.yml -p command-side-blog-service/target/command-side-blog-service-0.0.1-SNAPSHOT.jar
$ cf push -f command-side-project-service/manifest.yml -p command-side-project-service/target/command-side-project-service-0.0.1-SNAPSHOT.jar
$ cf push -f query-side-blog-service/manifest.yml -p query-side-blog-service/target/query-side-blog-service-0.0.1-SNAPSHOT.jar
$ cf push -f query-side-project-service/manifest.yml -p query-side-project-service/target/query-side-project-service-0.0.1-SNAPSHOT.jar
$ cf push -f api-gateway/manifest.yml -p api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
$ cf push -f adminserver/manifest.yml -p adminserver/target/adminserver-1.3.3.RELEASE.jar

```

##### Eclipse
Push microservices with 'Boot Dashboard':

- Add local (dev) cloud foundry target (https://api.local.pcfdev.io).
- Once you are connected, start dragging the projects to this instance.


NOTE: Please run 'configserver' first, followed by 'registry' and other services.

### Issuing Commands & Queries with CURL

#### Create Blog post

###### Microservices

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' http://127.0.0.1:9000/command/blog/blogpostcommands 

```
or on the PCF:
```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' api-gateway.local.pcfdev.io/command/blog/blogpostcommands 
```
###### Monolithic
```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' http://127.0.0.1:8080/api/blogpostcommands

```

#### Publish Blog post

###### Microservices

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"publishAt": "2016-12-23T14:30:00+00:00"}' http://127.0.0.1:9000/command/blog/blogpostcommands/{id}/publishcommand

```
###### Monolithic

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"publishAt": "2016-12-23T14:30:00+00:00"}' http://127.0.0.1:8080/api/blogpostcommands/{id}/publishcommand

```

#### Query Blog posts

###### Microservices

```bash
$ curl http://127.0.0.1:9000/query/blog/blogposts
```
or on the PCF:
```bash
$ curl api-gateway.local.pcfdev.io/query/blog/blogposts
```
###### Monolithic
```bash
$ curl http://127.0.0.1:8080/api/blogposts
```

#### Create Project

###### Microservices

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Name","repoUrl":"URL","siteUrl": "siteUrl","description": "sdfsdfsdf"}' http://127.0.0.1:9000/command/project/projectcommands

```
###### Monolithic

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Name","repoUrl":"URL","siteUrl": "siteUrl","description": "sdfsdfsdf"}' http://127.0.0.1:8080/api/projectcommands

```
#### Query Projects

###### Microservices
 
 ```bash
$ curl http://127.0.0.1:9000/query/project/projects
```
###### Monolithic
```bash
$ curl http://127.0.0.1:8080/api/projects
```

#### WebSocket on the gateway

###### Microservices
All the events will be sent to browser via WebSocket and displayed on http://127.0.0.1:9000/socket/index.html
or on the PCF: http://api-gateway.local.pcfdev.io/socket/index.html


## About AXON

### Commands 

Commands are messages that are sent to a system with an intent of doing something. They are sent to a Command Gateway, and then a Command Bus dispatches them to concrete Command Handlers. There can be some validation, or security check done in some Interceptors. The Command should be executed in some Unit of Work, which can be implemented over database transaction. Additionally Command processing may be distributed across many nodes with JGroups.

Axon provides interfaces, implementation and annotations for each one of those concepts. You just need to write your Command classes, and simple Handler classes with one annotation and that's it.

There are some default Interceptors already written - like Interceptor for JSR 303 Bean Validation. If you need, you can easily write your Interceptor by implementing one simple interface.

### Events

As stated before, Commands are messages with intent of doing something. On the other hand, after this something is done, another messages can be produced as a result - Events. They represent a fact.

In Axon, you just need to write your own Event classes. All the infrastructure that is responsible for handling them is there for you. In previous version (1.3) you had to extend base Event class - in current version (2.0) you don't have to do it anymore. Because of that, you can use your Events to integrate with other, non-Axon-based systems easily.

### Domain

You should model your domain very carefully. This is the place, where the essential complexity lays. There are no frameworks that can help you with that. You should spend most of your time in that place.

Thanks to Axon, you can actually do it that way. All the infrastructure code is there for you. The one thing that Axon gives you to help you struggling with the domain modeling is base Aggregate Root class, which you can extend and get access to some useful methods.

In the end, if you are using Event Sourcing, you can use Abstract Annotated Aggregate Root and use annotations on your private methods, that are applying events and changing the state of the Aggregate. Axon will additionally dispatch Events to annotated Entities in the Aggregate.

You should not change the state of the Aggregate in methods that are not reacting on Events. This is normal in Event Sourcing. Axon guards you not to violate this principle, so an inexperienced person won't break anything.

### Repositories

Aggregates are stored in Repositories. You should be able to load Aggregate by id, and save it back. It doesn't matter if you are using SQL database, flat files, or other noSQL solution. The persistance technique should be separated from the concept.

Axon gives you this separation. You can choose to use classic JPA-based Repository, or Event Sourced Repository. It additionally adds possibility to use caching for performance tuning.

### Event Stores

If you choose to use Event Sourcing, you need to store effectively your Events in some Event Store.

Axon gives you an interface for Repository, and a couple of implementations. The simplest implementation is based on flat files, but it is not that powerfull as others. You can also use JPA-based Event Store, which will create only two tables - for Event entries and for Aggregate Snapshot entries. For those two solutions, you will need to serialize your Events. Axon by default uses XStream, but you can choose any other. The third Event Store is implemented over MongoDB. If you need, you can easily implement any other Event Store on your own.

When your Aggregates live for a very long time, they can have a quite long history. For performance reasons you can use Aggregate Snapshotting to shorten the time of Aggregate loading.

Your Events definition may change over the time, so those Event Stores by default give you a possibility to easily write Event Upcasters. It also gives you a support for some advanced conflict resolution when it happens. 

### Event Bus 

In CQRS, after Events are generated, they need to be processed to update the query database. They are dispatched to Event Handlers. Those Event Handlers can be located on the same machine, or distributed in a cluster.

In Axon you just need to annatote methods of components that are supposed to listen for Events. You can also choose if you want to process Events synchronously, or asynchronously.

If some error occurs, you can define how to react. Axon can rollback transaction if you are using any, or it can reschedule the Event and process it again after some time for you. If you need any other error handling procedure, you can write it on your own by implementing simple interface. 

If you have distributed environment, Axon gives you support for Spring AMQP.

Sometimes you need to replay historical Events, and Axon also gives you the support for doing that.

### Sagas

Sometimes your transactions need to live longer. You cannot always finish all the work you need in a single ACID transaction. That is where Sagas come into play. On the other hand, you can use Sagas as a Workflow, or State Machine.

In Axon, Saga is a special Event Handler that handles the long business transaction. You can use Abstract Annotated Saga as a base class for your Sagas and then just use annotations on your methods that handle Events.

If you need to take care about time, or deadlines, Axon provides Sagas with Schedulers that can handle time management. You can use Simple Scheduler that uses pure Java implementation, or Quartz Scheduler that is much more powerfull.

### Testing 

You should test the code that you write, right? You can do it in a normal way, with mocks, or something like this, but if you are using Event Sourcing, you have another possibility. You have a history of Events, then you execute some Command, and a bunch of Events is generated as a result. You can test exactly that way with Axon.

Axon gives you a Given When Then Fixture. As Given you specify historical set of Events, as When you specify a Command that is tested, and as a Then you specify full set of Events that are supposed to be generated. Everything is wired for you, and you just need to define test scenario with those Events and Command.

Of course, you should also test your Sagas. There is a special Annotated Saga Test Fixture for that. As Given you specify a set of historical Event, as When you specify a single Event, and as Then you specify a desired behavior, or state change. If you need, you can also mock the time for testing time-related Sagas.

### Spring support 

Those days, each mature framework in Java world should have some sort of Spring support. Each of Axon's components can be configured as a Spring bean. Axon provides also a namespace shortcut for almost everything it has, so the configuration is as short as it has to be.

## References and further reading

  * http://martinfowler.com/articles/microservices.html
  * http://microservices.io/
  * http://www.slideshare.net/chris.e.richardson/developing-eventdriven-microservices-with-event-sourcing-and-cqrs-phillyete
  * http://12factor.net/
  * http://pivotal.io/platform/migrating-to-cloud-native-application-architectures-ebook
  * http://pivotal.io/beyond-the-twelve-factor-app
  * http://www.infoq.com/news/2016/01/cqrs-axon-example
  * http://www.axonframework.org
  * http://blog.arungupta.me/docker-swarm-cluster-using-consul/
  * https://blog.docker.com/2016/02/containers-as-a-service-caas/
  * http://www.kennybastani.com/2016/04/event-sourcing-microservices-spring-cloud.html
  * https://benwilcock.wordpress.com/2016/06/20/microservices-with-spring-boot-axon-cqrses-and-docker/
  
