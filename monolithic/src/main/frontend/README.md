# Micro Company UI - Frontend

This project was generated with [angular-cli](https://github.com/angular/angular-cli) version 1.0.0-beta.21.

## Run back-end server
The back-end server (localhost:8080) is used to serve the rest API:

- http://localhost:8080/swagger-ui.html#/blog-controller
- http://localhost:8080/swagger-ui.html#/project-controller


Please note that the back-end server will include front-end (angular) application that is available on http://localhost:8080.

### Run it with Maven

```bash
$ cd micro-company
$ ./mvnw clean install
$ cd micro-company/monolithic
$ ./mvnw spring-boot:run
```

## Development server for front-end (angular)
Run `npm start` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.


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
