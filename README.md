# Micro Company application

Digital experiences are becoming essential to every business. 

Strategic application architects need to establish consistent, holistic approaches that enable the flexibility, scalability and versatility that digital business requires. Organizations that delay or fail to implement these new approaches risk falling seriously behind. 

Companies must embrace a new way of projecting themselves in the form of secure, easy-to-consume unified APIs that are easily integrated into a wide range of digital touchpoints.

**This project is intended to demonstrate end-to-end best practices for building a Netflix-like microservice architecture using Spring Cloud.**

## Architecture

Microservices enable businesses to innovate faster and stay ahead of the competition. But one major challenge with the microservices architecture is the management of distributed data. Each microservice has its own private database. It is difficult to implement business transactions that maintain data consistency across multiple services as well as queries that retrieve data from multiple services.

<img class="img-responsive" src="micro-company.png">


### Patterns and techniques:

1. Microservices
2. Command and Query Responsibility Separation (CQRS)
3. DDD - Event Sourcing
4. DDD - Agregates

### Technologies

- [Spring Boot](http://projects.spring.io/spring-boot/) (v1.2.6)
- [Spring Cloud](http://projects.spring.io/spring-cloud/)
- [Spring Data](http://projects.spring.io/spring-data/)
- [Spring Data REST](http://projects.spring.io/spring-data-rest/)
- [Axon Framework](http://www.axonframework.org/) (v2.4)
- [RabbitMQ](https://www.rabbitmq.com/) (v3.5.4) Axon supports any Spring AMQP supported platform.
- [MongoDB](https://www.mongodb.com/) (v.2.14) Axon also supports JDBC & JPA based event-stores.


### Key benefits

1. Easy implementation of eventually consistent business transactions that span multiple microservices
2. Automatic publishing of events whenever data changes
3. Faster and more scalable querying by using materialized views
4. Reliable auditing for all updates


### How it works

The domain is literally split into a *command-side* microservice application and a *query-side* microservice application (this is CQRS in its most literal form).

Communication between the two microservices is `event-driven` and the demo uses RabbitMQ messaging as a means of passing the events between processes (VM's).

The **command-side** processes commands. Commands are actions which change state in some way. The execution of these commands results in `Events` being generated which are persisted by Axon (using MongoDB) and propagated out to other VM's (as many VM's as you like) via RabbitMQ messaging. In event-sourcing, events are the sole records in the system. They are used by the system to describe and re-build aggregates on demand, one event at a time.

The **query-side** is an event-listener and processor. It listens for the `Events` and processes them in whatever way makes the most sense. In this application, the query-side just builds and maintains a *materialised view* which tracks the state of the individual agregates (Product, Blog, Customer, ...). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues are durable, so they can be temporarily stored on behalf of the event-listener if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

Read the [Axon documentation](http://www.axonframework.org/download/) for the finer details of how Axon generally operates to bring you CQRS and Event Sourcing to your apps, as well as lots of detail on how it all get's configured (spoiler: it's mostly spring-context XML for the setup and some Java extensions and annotations within the code).

### Modules

#### BlogMicroservice
A Blog service is used for manging and quering the posts of your company. It is split into a *command-side* microservice application and a *query-side* microservice application.

#### ProjectMicroservice
A Project service is used for manging and quering the projects of your company. It is split into a *command-side* microservice application and a *query-side* microservice application.

#### CustomerMicroservice
A Customer service is used for manging and quering customers of yours. It is split into a *command-side* microservice application and a *query-side* microservice application.

#### (Service) Registry
Netflix Eureka is a service registry. It provides a REST API for service instance registration management and for querying available instances. Netflix Ribbon is an IPC client that works with Eureka to load balance(client side) requests across the available service instances.

#### Authorization server (Oauth2)
For issuing tokens and authorize requests.

#### Configuration server
The configuration service is a vital component of any microservices architecture. Based on the twelve-factor app methodology, configurations for your microservice applications should be stored in the environment and not in the project.
Configuration is hosted here: https://github.com/idugalic/micro-company-config.git

#### Admin server (http://codecentric.github.io/spring-boot-admin/1.3.2/)
Spring Boot Admin is a simple application to manage and monitor your Spring Boot Applications. The applications register with our Spring Boot Admin Client (via http) or are discovered using Spring Cloud (e.g. Eureka). The UI is just an Angular.js application on top of the Spring Boot Actuator endpoints. In case you want to use the more advanced features (e.g. jmx-, loglevel-management), Jolokia must be included in the client application.

#### API Gateway
Implementation of an API gateway that is the single entry point for all clients. The API gateway handles requests in one of two ways. Some requests are simply proxied/routed to the appropriate service. It handles other requests by fanning out to multiple services.

#### Circuit Breaker - Histrix (Dashboard)
Netflix implementation of circuit breaker pattern.

## Running instructions

### Prerequisite

- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Git](https://git-scm.com/) 
- [Docker](https://www.docker.com/)


### Step 1: Clone and build the project (from Docker terminal)

```bash
$ git clone https://github.com/idugalic/micro-company.git
$ cd microservice-company
$ mvn clean install (will create Docker images also)
```

### Step 2: Spin up everything (from Docker terminal)

```bash
$ cd microservice-company/docker
$ ./run.sh 

```

Assuming you've installed Docker already, executing these commands should install the necessary docker containers for MongoDB, RabbitMQ and all other microservices, and run them locally. 

### Issuing Commands & Queries with CURL
Please note that my current docker host IP is 192.168.99.103

#### Create Blog post

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' http://192.168.99.103:8080/blogposts 

```
or on gateway:

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' http://192.168.99.103:8080/command/blog/blogposts 

```
#### Publish Blog post

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"publishAt": "2016-12-23T14:30:00+00:00"}' http://192.168.99.103:8080/blogposts/{id}/publishcommand

```
or on gateway:

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"publishAt": "2016-12-23T14:30:00+00:00"}' http://192.168.99.103:9000/command/blog/blogposts/{id}/publishcommand

```
#### Query Blog posts
```bash
$ curl http://192.168.99.103:8081/blogposts
```
or on gateway:

```bash
$ curl http://192.168.99.103:9000/query/blog/blogposts
```

#### Create Project
```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Name","repoUrl":"URL","siteUrl": "siteUrl","description": "sdfsdfsdf"}' http://192.168.99.103:8082/projects

```

or on gateway:

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"name":"Name","repoUrl":"URL","siteUrl": "siteUrl","description": "sdfsdfsdf"}' http://192.168.99.103:9000/command/project/projects

```
#### Query Projects
```bash
$ curl http://192.168.99.103:8083/projects
```
 or on gateway:
 
 ```bash
$ curl http://192.168.99.103:9000/query/project/projects
```

#### WebSocket on the gateway

All the events will be sent to browser via WebSocket and displayed on http://192.168.99.103:9000/socket/index.html

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
  * http://www.slideshare.net/chris.e.richardson/developing-eventdriven-microservices-with-event-sourcing-and-cqrs-phillyete
  * http://12factor.net/
  * http://pivotal.io/platform/migrating-to-cloud-native-application-architectures-ebook
  * http://pivotal.io/beyond-the-twelve-factor-app
  * http://www.infoq.com/news/2016/01/cqrs-axon-example
  * http://www.axonframework.org
  * http://eventuate.io
