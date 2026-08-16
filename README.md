# Payment App

- java 21
- spring boot 4.1.0

## Local Environment Setup

### Database
- generate and validate the final docker compose configuration for 'local' profile.
```bash
docker-compose --env-file .env.local -f docker-compose-local.yaml config
```
- Start the database services:
```bash
docker-compose --env-file .env.local -f docker-compose-local.yaml up -d         
```
you will see the following output if everything is up and running:
```bash
[+] up 3/3
 ✔ Network payment-db_default   Created                                                                                                                              0.0s
 ✔ Container payment-source-db  Started                                                                                                                              0.1s
 ✔ Container payment-replica-db Started
```