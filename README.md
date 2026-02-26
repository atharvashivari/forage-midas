# Midas
Project repo for the JPMC Advanced Software Engineering Forage program

Task 1 & 2: Distributed Ingestion (Kafka)

The Goal: Connect the microservice to a real-time data stream. 


What you did: Configured a Kafka Consumer within Spring Boot to listen to a specific transaction topic. 


The Tech: Used the spring-kafka library and a @KafkaListener to handle asynchronous message ingestion. 


The Logic: Built a deserialization flow that converted raw JSON bytes into a structured Transaction POJO (Plain Old Java Object). 


Interview Flex: "I implemented a scalable ingestion layer that decoupled the transaction producer from our processing logic, allowing the system to handle spikes in traffic without blocking." 

Task 3: Relational Persistence (JPA & H2)

The Goal: Create a reliable "source of truth" for user funds. 

What you did: Modeled a relational schema using JPA Entities (UserRecord, TransactionRecord) and mapped them to an H2 in-memory database.


The Tech: Used Spring Data JPA Repositories to perform CRUD operations without writing raw SQL. 


The Logic: Implemented business validation to ensure the sender had a sufficient balance before updating the database. 


Interview Flex: "I designed the persistence layer to maintain strict data integrity, ensuring that user balances are updated atomically and every transaction is archived for audit purposes." 

Task 4: Inter-Service Communication (REST Integration)

The Goal: Integrate with an external "Black Box" service for bonuses. 


What you did: Used Spring's RestTemplate to send POST requests to an external Incentive API. 


The Tech: Managed the request/response lifecycle by mapping the API's JSON response back into an Incentive DTO (Data Transfer Object). 


The Logic: Updated the balance logic to add the incentive only to the recipient, ensuring the sender was never overcharged for the bonus. 


Interview Flex: "I integrated a third-party incentive service into our workflow, handling external API boundaries and ensuring the system remained resilient even if the external service returned a null response." 

Task 5: API Exposure (REST Controller)

The Goal: Build a user-facing interface to query the system state. 


What you did: Created a @RestController that exposed a GET /balance endpoint. 


The Tech: Configured the application to run on a dedicated port (33400) and used @RequestParam to handle user ID lookups. 

The Logic: Handled "Edge Cases" where a user might not exist by using Java's Optional API to safely return a 0 balance instead of a 500 error.


Interview Flex: "I developed a user-facing REST API that surfaced critical account data, ensuring the endpoint was both performant and safe against null pointer exceptions."