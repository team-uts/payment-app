# Payment App

- java 21
- spring boot 4.1.0

## Local Environment Setup

### Database
- generate and validate the final docker compose configuration for 'local' profile.
```shell
docker-compose --env-file .env.local -f docker-compose-local.yaml config
```
- Start the database services:
```shell
docker-compose --env-file .env.local -f docker-compose-local.yaml up -d         
```
you will see the following output if everything is up and running:
```shell
[+] up 3/3
 ✔ Network payment-db_default   Created                                                                                                                              0.0s
 ✔ Container payment-source-db  Started                                                                                                                              0.1s
 ✔ Container payment-replica-db Started
```

## DB Migration

```shell
$ ./gradlew :persistence:flywayMigrate -Dprofile=local
$ ./gradlew :persistence:flywayInfo -Dprofile=local
$ ./gradlew :persistence:flywayValidate -Dprofile=local
```

## Domain Layer

Hexagonal Architecture

### Roles and Specifications for Each Layer (Module)

- app
  - `RestController`: handles HTTP requests and responses (endpoints)
  - `RequestDto`
  - `Mapper`
- domain
  - `UseCase`
    - handles main business logic for a specific request
    - orchestrates the flow between different components
  - logic:
    1. `Facade`
    2. `Processor`
    3. `Executor`
  - interface:
    - `Service`: manages some business logic and Ports
    - `Port`: contract with other modules (infra and persistence) / connection
    - `Info`: response dto to app module (api response)
  - `Model`: represents the core business concepts, rules, and behaviors of a specific domain.
- persistence
  - `Adapter`
    - implementation of the Port
    - responsible for interacting with the data repository
  - data:
    - `Entity`
    - `Repository`
- infra
  - `Adapter`
    - implementation of the Port
    - responsible for interacting with external services (e.g., payment gateways)