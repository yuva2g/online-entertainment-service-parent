## Online Entertainment Recommendation System
Online entertainment service is a collection of 3 services 
1. entertainment service - fetches the entertainment data from the other services
2. album query service - fetches the album data from the iTunes API
3. book query service - fetches the book data from the Google Books API

### Architecture



1. album-query-service
    - Implemented using Hexogonal Architecture.
    - Uses RestTemplate to make the HTTP calls to the iTunes API
    - Circuit Breaker using Resilience4j
    - Endpoints
        - GET /albums - This endpoint fetches the album data from the iTunes API and returns the response to the client.

2. book-query-service
    - Implemented using Layered Architecture.
    - Uses WebClient to make the HTTP calls to the Google Books API
    - OAuth2 for Google APIs (Not working, needs investigation)
    - Circuit Breaker using Resilience4j
    - Endpoints
        - GET /books - This endpoint fetches the book data from the Google Books API and returns the response to the client.

3. entertainment-service
    - Implemented using Layered Architecture
    - Concurrent calls using CompletableFuture
    - Circuit Breaker using Resilience4j
    - Endpoints
        - GET /online-entertainments - This endpoint fetches the data from the album query service and the book query service and returns the response to the client.

### Tools and Technologies
- Java 21
- Spring Boot 3.4.3
- Maven
- Resilience4j
- WebClient
- RestTemplate
- Lombok
- OAuth2
- Cucumber Tests (BDD) - test module
- Actuator (exposing metrics)

### Installation
1. Clone the repository
```git clone http://github.com/yuva2g/online-entertainment-service.git```
```cd online-entertainment-service```

2. Build the project
```mvn clean install```

3. Run the project
```cd <service-name>``` (album-query-service, entertainment-service)
```mvn spring-boot:run -Dspring-boot.run.profiles=local```

```cd book-query-service```
```mvn spring-boot:run -Dspring-boot.run.profiles=local -Dgoogle.client-id=<clientId> -Dgoogle.client-ecret=<clientSecret> -Dgoogle.api-key=<apiKey>```
Note: Google client-id, client-secret and api-key is provided in email.

4. POSTMAN Collection or Browser
```http://localhost:8095/online-entertainments?searchText=Datascience```

### Future Enhancements
- Config Server, Service Registry, and API Gateway
- Shared library for models like Album, Book, etc.
- GraphQL & Kotlin
- Observability using Prometheus and Grafana
- Distributed Tracing and Logging using Zipkin and ELK
- Improve Cucumber Tests and add more scenarios
- Performance Testing
- Contract Testing using Pact
- OpenAPI Documentation
- Implementing the OAuth2 for Google APIs

### Troubleshooting
- Check your Java and Maven versions, both should have Java 21 
  ```java --version```
  ```mvn -v```