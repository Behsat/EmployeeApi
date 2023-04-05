# Employee API - Coding Challenge

Employee Api is the domain service for handling process of employees.E.g. 
fetching, creation, updating and deletion.

## Application Metadata

Application Name: **employee-api**

## Building and Running

### Prerequisites

- Java 11
- Apache Maven

### Dependencies

- Spring Boot

### Build 

This is a normal maven project. But before running it, docker-compose file should be executed.
Docker File can be find in here: [Docker-Compose-File][Docker-File]

- Simply execute: `docker-compose up -d`
- Maven Build: `mvn clean package`

### Run the Project

#### Unix

1. `cd` to the project directory
2. `chmod +x mvnw`
3. `./mvnw clean install spring-boot:run`

#### Windows

1. `cd` to the project directory
2. `mvnw.cmd clean install spring-boot:run`

## Resources

### Swagger-UI
Swagger UI also implemented this project. UI can be accessible through here:
[Swagger-UI][Swagger-UI]

### Postman Collection
Sample postman collection added to project resource path: [Postman-Collection][Postman-Collection]

## Development Information

This project use Mongo DB as database storage, Kafka as Message Broker, Swagger as Visualization.

Spring basic authentication implemented the project and to get response from endpoints authentication required.
- Usernmae: **user**
- Password: **password**

Or

Add Authorization parameter to the endpoints.
- Request Header Parameter Name: **Authorization**
- Request Header Parameter Value: **Basic dXNlcjpwYXNzd29yZA==**

[Docker-File]: docker-compose.yml
[Swagger-UI]: http://localhost:8080/swagger-ui/index.html
[Postman-Collection]: src/main/resources/JETCodingChallange.postman_collection.json