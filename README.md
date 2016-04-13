# microservice-company

Digital experiences are becoming essential to every business. Yet few firms have the organizational structure or technology needed to make good on their promise. In fact, research shows that the No. 1 reason for project failure is businesses hardwiring point integrations between individual customer-facing experiences and backend systems. It restricts agility and your ability to innovate. Instead, what's needed is a unified API strategy. 

Companies must embrace a new way of projecting themselves in the form of secure, easy-to-consume unified APIs that are easily integrated into a wide range of digital touchpoints. 

## Architecture

Microservices enable businesses to innovate faster and stay ahead of the competition. But one major challenge with the microservices architecture is the management of distributed data. Each microservice has its own private database. It is difficult to implement business transactions that maintain data consistency across multiple services as well as queries that retrieve data from multiple services.

### Patterns and techniques used in this architecture:

1. Microservices
2. Command and Query Responsibility Separation (CQRS)
3. DDD - Event Sourcing
4. DDD - Agregates

### Technologies

It leverages the following technologies:

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

### Modules

1. BlogMicroservice (command-side and query-side)
2. ProductMicroservice (command-side and query-side)
3. CustomerMicroservice (command-side and query-side)
4. Authorization server
5. Configuration server
6. Gateway (Proxy)

## How it works

The domain is literally split into a *command-side* microservice application and a *query-side* microservice application (this is CQRS in its most literal form).

Both microservices use spring-boot.

Communication between the two microservices is `event-driven` and the demo uses RabbitMQ messaging as a means of passing the events between processes (VM's).

The **command-side** processes commands. Commands are actions which change state in some way. The execution of these commands results in `Events` being generated which are persisted by Axon (using MongoDB) and propagated out to other VM's (as many VM's as you like) via RabbitMQ messaging. In event-sourcing, events are the sole records in the system. They are used by the system to describe and re-build aggregates on demand, one event at a time.

The **query-side** is an event-listener and processor. It listens for the `Events` and processes them in whatever way makes the most sense. In this application, the query-side just builds and maintains a *materialised view* which tracks the state of the individual agregates (Product, Blog, Customer, ...). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues are durable, so they can be temporarily stored on behalf of the event-listener if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.


Read the [Axon documentation](http://www.axonframework.org/download/) for the finer details of how Axon generally operates to bring you CQRS and Event Sourcing to your apps, as well as lots of detail on how it all get's configured (spoiler: it's mostly spring-context XML for the setup and some Java extensions and annotations within the code).

### Prerequisite

- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (I'm using v1.8.0_60)
- [Git](https://git-scm.com/) (I'm using v1.9.1)
- [Docker](https://www.docker.com/) (I'm using v1.8.2)


#### Step 1: Spin up the Database and Messaging servers

First lets get the RabbitMQ and MongoDB servers up and running. I've used Docker for this because it's really simple.

```bash
$ docker run -d --name my-mongo -p 27017:27017 mongo
$ docker run -d --name my-rabbit -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=test -e RABBITMQ_DEFAULT_PASS=password -e RABBITMQ_NODENAME=my-rabbit rabbitmq:3-management
$ docker ps
```

Assuming you've installed Docker already, executing these commands should install the necessary docker containers for MongoDB and RabbitMQ and run them locally. They're given the names `my-rabbit` and `my-mongo` and they'll run in the background until you ask docker to stop them (using `docker stop my-mongo` for example).

If you already have MongoDB and RabbitMQ on your system (using their default ports) you can use those instead once you have the required users and settings configured (see the blockquotes below for details).

> The demo expects RabbitMQ to have a user with the username `test` and the password `password` and for this user to have admin rights so that it can create exchanges and queues. If you don't want to add such a user, stop your local RabbitMQ server and start the docker one instead using the commands outlined above.

> The demo also expects the MongoDB server to have a default `guest` user with no password and for this guest user to have admin rights. If you don't want to add such a user, stop your local MongoDB server and start the docker container instead using the commands above.

#### Step 2: Clone and build the project

Next we can download, build and unit test the microservices-sampler project. Here I'm using the Gradle wrapper, so there is no need to actually install Gradle if you don't want to.

```bash
$ git clone https://github.com/idugalic/micro-company-config.git
$ cd microservice-company
$ mvn clean install
```

#### Step 3: Run

So far so good. Now we want to test the delivery of event messages to other processes. To do this we need two (**2**) terminal windows. In one window we'll boot the `query-side` (which contains an event-listener and a materialised view), and in the other terminal we'll fire the `command-side` integration tests (which generate commands that generate events).

#### In ** (Docker) Terminal #1**:

Start the docker servers for RabbitMQ and MongoDB (if you haven't already).

```bash
$ docker start my-rabbit
$ docker start my-mongo
```

#### In **Terminal #2**:

Now run the command-side Spring Boot application via maven.

```bash
$ mvn spring-boot:run
```
After lots of logging output from Spring Boot, you should have a the command-side microservice ready and listening on port 9000.

#### In **Terminal #3**:

```bash
$ mvn spring-boot:run
```

After lots of output from Spring Boot, you should have a fully booted query-side microservice listening on http port 8080.


#### Issuing Commands & Queries with CURL
To issue a command:

```bash
$ curl (POST) http://localhost:8080/blogposts

{
  "title": "Ivan",
  "rawContent": "Dugalic",
  "publicSlug": "publicslug",
  "draft": true,
  "broadcast": true,
  "category": "ENGINEERING",
  "publishAt": "2016-12-23T14:30:00+00:00"
}
```

If you want to inspect the query-side yourself

```bash
$ curl http://localhost:8081/blogposts
```

As HATEOAS is switched on, you should be offered other links which you can also traverse with curl.

### About AXON

#### Commands 

Commands are messages that are sent to a system with an intent of doing something. They are sent to a Command Gateway, and then a Command Bus dispatches them to concrete Command Handlers. There can be some validation, or security check done in some Interceptors. The Command should be executed in some Unit of Work, which can be implemented over database transaction. Additionally Command processing may be distributed across many nodes with JGroups.

Axon provides interfaces, implementation and annotations for each one of those concepts. You just need to write your Command classes, and simple Handler classes with one annotation and that's it.

There are some default Interceptors already written - like Interceptor for JSR 303 Bean Validation. If you need, you can easily write your Interceptor by implementing one simple interface.

#### Events

As stated before, Commands are messages with intent of doing something. On the other hand, after this something is done, another messages can be produced as a result - Events. They represent a fact.

In Axon, you just need to write your own Event classes. All the infrastructure that is responsible for handling them is there for you. In previous version (1.3) you had to extend base Event class - in current version (2.0) you don't have to do it anymore. Because of that, you can use your Events to integrate with other, non-Axon-based systems easily.

#### Domain

You should model your domain very carefully. This is the place, where the essential complexity lays. There are no frameworks that can help you with that. You should spend most of your time in that place.

Thanks to Axon, you can actually do it that way. All the infrastructure code is there for you. The one thing that Axon gives you to help you struggling with the domain modeling is base Aggregate Root class, which you can extend and get access to some useful methods.

In the end, if you are using Event Sourcing, you can use Abstract Annotated Aggregate Root and use annotations on your private methods, that are applying events and changing the state of the Aggregate. Axon will additionally dispatch Events to annotated Entities in the Aggregate.

You should not change the state of the Aggregate in methods that are not reacting on Events. This is normal in Event Sourcing. Axon guards you not to violate this principle, so an inexperienced person won't break anything.

#### Repositories

Aggregates are stored in Repositories. You should be able to load Aggregate by id, and save it back. It doesn't matter if you are using SQL database, flat files, or other noSQL solution. The persistance technique should be separated from the concept.

Axon gives you this separation. You can choose to use classic JPA-based Repository, or Event Sourced Repository. It additionally adds possibility to use caching for performance tuning.

#### Event Stores

If you choose to use Event Sourcing, you need to store effectively your Events in some Event Store.

Axon gives you an interface for Repository, and a couple of implementations. The simplest implementation is based on flat files, but it is not that powerfull as others. You can also use JPA-based Event Store, which will create only two tables - for Event entries and for Aggregate Snapshot entries. For those two solutions, you will need to serialize your Events. Axon by default uses XStream, but you can choose any other. The third Event Store is implemented over MongoDB. If you need, you can easily implement any other Event Store on your own.

When your Aggregates live for a very long time, they can have a quite long history. For performance reasons you can use Aggregate Snapshotting to shorten the time of Aggregate loading.

Your Events definition may change over the time, so those Event Stores by default give you a possibility to easily write Event Upcasters. It also gives you a support for some advanced conflict resolution when it happens. 

#### Event Bus 

In CQRS, after Events are generated, they need to be processed to update the query database. They are dispatched to Event Handlers. Those Event Handlers can be located on the same machine, or distributed in a cluster.

In Axon you just need to annatote methods of components that are supposed to listen for Events. You can also choose if you want to process Events synchronously, or asynchronously.

If some error occurs, you can define how to react. Axon can rollback transaction if you are using any, or it can reschedule the Event and process it again after some time for you. If you need any other error handling procedure, you can write it on your own by implementing simple interface. 

If you have distributed environment, Axon gives you support for Spring AMQP.

Sometimes you need to replay historical Events, and Axon also gives you the support for doing that.

#### Sagas

Sometimes your transactions need to live longer. You cannot always finish all the work you need in a single ACID transaction. That is where Sagas come into play. On the other hand, you can use Sagas as a Workflow, or State Machine.

In Axon, Saga is a special Event Handler that handles the long business transaction. You can use Abstract Annotated Saga as a base class for your Sagas and then just use annotations on your methods that handle Events.

If you need to take care about time, or deadlines, Axon provides Sagas with Schedulers that can handle time management. You can use Simple Scheduler that uses pure Java implementation, or Quartz Scheduler that is much more powerfull.

#### Testing 

You should test the code that you write, right? You can do it in a normal way, with mocks, or something like this, but if you are using Event Sourcing, you have another possibility. You have a history of Events, then you execute some Command, and a bunch of Events is generated as a result. You can test exactly that way with Axon.

Axon gives you a Given When Then Fixture. As Given you specify historical set of Events, as When you specify a Command that is tested, and as a Then you specify full set of Events that are supposed to be generated. Everything is wired for you, and you just need to define test scenario with those Events and Command.

Of course, you should also test your Sagas. There is a special Annotated Saga Test Fixture for that. As Given you specify a set of historical Event, as When you specify a single Event, and as Then you specify a desired behavior, or state change. If you need, you can also mock the time for testing time-related Sagas.

#### Spring support 

Those days, each mature framework in Java world should have some sort of Spring support. Each of Axon's components can be configured as a Spring bean. Axon provides also a namespace shortcut for almost everything it has, so the configuration is as short as it has to be.

### References and further reading

  * http://www.infoq.com/news/2016/01/cqrs-axon-example
  * http://www.axonframework.org
  * http://eventuate.io