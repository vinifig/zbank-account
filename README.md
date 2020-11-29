# ZBank Account Application

A digital bank developed in Kotlin + Spring Boot

## Requirements

* Intellij IDEA with kotlin support
* Docker

## Running

* Start the database containers, running on the project path

```shell script
$ docker-compose up -d
``` 
 
* Open the project on your Intellij IDEA
* Select BankAccountApplicationKt on `Edit run/debug configurations` dialog
* Run the application
* Access your API at http://localhost:8080
* You could find a Swagger with all the endpoints on http://localhost:8080/swagger-ui/

### Attention

All the api requests except for `POST /account` must be authenticated with `Basic(accountId:cpf)`  


