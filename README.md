# microservice-company

Digital experiences are becoming essential to every business. Yet few firms have the organizational structure or technology needed to make good on their promise. In fact, research shows that the No. 1 reason for project failure is businesses hardwiring point integrations between individual customer-facing experiences and backend systems. It restricts agility and your ability to innovate. Instead, what's needed is a unified API strategy. 

Companies must embrace a new way of projecting themselves in the form of secure, easy-to-consume unified APIs that are easily integrated into a wide range of digital touchpoints. 

## Architecture

Microservices enable businesses to innovate faster and stay ahead of the competition. But one major challenge with the microservices architecture is the management of distributed data. Each microservice has its own private database. It is difficult to implement business transactions that maintain data consistency across multiple services as well as queries that retrieve data from multiple services.

### Patterns and techniques used in this architecture:

1. [Microservices](https://bitbucket.org/idugalic_microservices_lab/micro-company/downloads/Microservices.docx) 
2. [Command and Query Responsibility Separation (CQRS)](https://bitbucket.org/idugalic_microservices_lab/micro-company/downloads/springone2gx2015eventsourcingandcqrs-150925224302-lva1-app6892.pdf)
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

### Running the Demo yourself...

Assuming you already have...

- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (I'm using v1.8.0_60)
- [Git](https://git-scm.com/) (I'm using v1.9.1)
- [Docker](https://www.docker.com/) (I'm using v1.8.2)

...then do the following...

#### Step 1: Spin up the Database and Messaging servers...

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

#### Step 2: Clone and build the project...

Next we can download, build and unit test the microservices-sampler project. Here I'm using the Gradle wrapper, so there is no need to actually install Gradle if you don't want to.

```bash
$ git clone https://idugalic@bitbucket.org/idugalic_microservices_lab/micro-company.git
$ cd microservice-company
$ ./gradlew clean test
```

#### Step 3: Run the Integration Test...

So far so good. Now we want to test the delivery of event messages to other processes. To do this we need two (**2**) terminal windows. In one window we'll boot the `query-side` (which contains an event-listener and a materialised view), and in the other terminal we'll fire the `command-side` integration tests (which generate commands that generate events).

#### In **Terminal #1**:

Start the docker servers for RabbitMQ and MongoDB (if you haven't already).

```bash
$ docker start my-rabbit
$ docker start my-mongo
```

Now run the command-side Spring Boot application via gradle.

```bash
$ ./gradlew command-side:bootRun
```
After lots of logging output from Spring Boot, you should have a the command-side microservice ready and listening on port 9000.

#### In **Terminal #2**:

```bash
$ ./gradlew query-side:bootRun
```

After lots of output from Spring Boot, you should have a fully booted query-side microservice listening on http port 9090.

#### In **Terminal #3**:

```bash
$ ./gradlew integrationTest
```

The integration test sends an 'AddProduct' command to the command-side REST API listening on port 9000. When the command-side has processed the command a 'ProductAdded' event is generated and sent to the query-side via RabbitMQ. The query-side processes this event and adds a record for the product to it's database. You should visually observe this process as logging output in the terminal 1 and terminal 2 windows while the test is in progress.

#### Issuing Queries with CURL
If you want to inspect the query-side yourself, just use CURL to query the command-side's REST API like this...

```bash
$ curl http://localhost:9090/products
```

As HATEOAS is switched on, you should be offered other links which you can also traverse with curl.