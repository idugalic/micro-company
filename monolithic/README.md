# Micro Company - Monolithic

This version of the application is deployed as a single monolithic application.

Domain Driven Design is applied through Event Sourcing and CQRS. How Event Sourcing enables deployment flexibility.



## Run back-end server
The back-end server (localhost:8080) is used to serve the rest API:

- http://localhost:8080/swagger-ui.html#/blog-controller
- http://localhost:8080/swagger-ui.html#/project-controller


Please note that the back-end server will include front-end (angular) application that will be available on http://localhost:8080.

```bash
$ cd micro-company
$ ./mvnw clean install
$ cd micro-company/monolithic
$ ./mvnw spring-boot:run
```

## Development server for front-end (angular)

```bash
$ cd micro-company/monolithic/src/main/frontend
$ npm start
```

Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files. Front-end application requires back-end application to run and serve the resources.


### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive/pipe/service/class`.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

### Deploying to Github Pages

Run `ng github-pages:deploy` to deploy to Github Pages.

### Further help

To get more help on the `angular-cli` use `ng --help` or go check out the [Angular-CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
