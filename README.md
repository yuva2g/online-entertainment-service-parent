## Online Entertainment Suggestion System
Online entertainment service is a collection of 3 services 
1. entertainment service - fetches the entertainment data from the other services
2. album query service - fetches the album data from the iTunes API
3. book query service - fetches the book data from the Google Books API

### Architecture

![Architecture Diagram.png](Architecture%20Diagram.png)

### Design Justification
- entertainment-service is responsible for fetching the data from the album query service and the book query service.   
- album-query-service is responsible for fetching the album data from the iTunes API.
- book-query-service is responsible for fetching the book data from the Google Books API.
- Named Query Service for album-query-service and book-query-service to scale based on the number of requests.

#### Design Requirements
Stability- ReadTimeout, ConnectTimeout are implemented using WebClient and RestTemplate. Entertainment-service is designed to handle the requests even when Albums or Books Query service is down. 
Resilience- Circuit Breaker is implemented using Resilience4j to handle the failures and timeouts.
Performance- CompletableFuture is used to make concurrent calls to the album query service and the book query service.
Security- OAuth2 is implemented for the book query service to access the Google Books API.
Scalability- entertainment-service is designed to scale based on the number of requests.

1. entertainment-service
   - Acts as an aggregator service (BFF)
   - Implemented using Layered Architecture
   - CompletableFuture to make concurrent calls to the album query service and the book query service
   - Circuit Breaker using Resilience4j
   - Endpoints
        - GET /online-entertainments - This endpoint fetches the data from the album query service and the book query service and returns the response to the client.

2. album-query-service
    - Implemented using Hexagonal Architecture (Ports and Adapters)
    - Port and Adapters enables plugging in different adapters for the same port for example, iTunes API, Spotify API, etc.
    - Uses RestTemplate to make the HTTP calls to the iTunes API
    - Circuit Breaker using Resilience4j
    - Endpoints
        - GET /albums - This endpoint fetches the album data from the iTunes API and returns the response to the client.

3. book-query-service
    - Implemented using Layered Architecture.
    - Uses WebClient to make the HTTP calls to the Google Books API (API key is required)
    - OAuth2 for Google APIs (Not working, needs investigation) 
    - Circuit Breaker using Resilience4j
    - Endpoints
        - GET /books - This endpoint fetches the book data from the Google Books API and returns the response to the client.


### Tools and Technologies
- Java 21
- Spring Boot 3.4.3
- Maven
- Resilience4j
- WebClient
- RestTemplate
- OAuth2
- Cucumber Tests (BDD) - entertainment-service (TestRunner.java)
- Actuator (exposing metrics)
- OpenAPI Definition

### Installation
1. Clone the repository
```git clone http://github.com/yuva2g/online-entertainment-service.git```
```cd online-entertainment-service-parent```

2. Build the project
```mvn clean install```

3. Run the project in separate terminals
   - Open new terminal and navigate to the project directory `online-entertainment-service`
      - ```cd album-query-service``` 
      - ```mvn spring-boot:run -Dspring-boot.run.profiles=local```

   - Open new terminal and navigate to the project directory `online-entertainment-service`
      - ```cd books-query-service```
   
      DO NOT RUN BELOW COMMAND, use command provided in email.
      - ```mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local --google_client_id=<client-id> --google_client_secret=<client-secret> --google.books.api-key=<api-key>"```

   - Open new terminal and navigate to the project directory `online-entertainment-service`
      - ```cd entertainment-service```
      - ```mvn spring-boot:run -Dspring-boot.run.profiles=local```

4. POSTMAN Collection or Browser or swagger
    - http://localhost:8095/online-entertainments?searchText=Believe```
    - http://localhost:8095/swagger-ui/index.html

5. Cucumber Test
    - Make sure the entertainment-service, album-query-service, and book-query-service are running
    - Open [TestRunner.java](entertainment-service%2Fsrc%2Ftest%2Fjava%2Fcom%2Fmyhobbies%2Fonline%2Fentertainmentservice%2Frunner%2FTestRunner.java)
    - Right-click and Run TestRunner.java

### Future Enhancements
- Config Server, Service Registry, and API Gateway
- Shared library for models like Album, Book, etc.
- GraphQL & Kotlin
- Distributed Tracing using Zipkin and Logging using ELK
- Monitoring using Prometheus and Grafana
- Improve Cucumber Tests and add more scenarios
- Performance Testing
- Contract Testing using Pact
- Fixing OAuth2 for Google APIs

### Troubleshooting
- Check your Java and Maven versions, both should have Java 21 
  ```java --version```
  ```mvn -v```