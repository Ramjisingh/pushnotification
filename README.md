#  DEMO  Integration Service

## Overview
The IBIS Message Gateway is a service that listens to Azure Event Hub, processes incoming messages, and forwards them to the IBIS system via IBM MQ. It also handles responses from IBM MQ, returning them as required.

## Key Responsibilities
✅ Azure Event Hub Listener – Consumes messages from Event Hub \
✅ IBM MQ Producer – Sends processed messages to IBIS \
✅ IBM MQ Consumer – Receives response messages from IBIS \
✅ Logging & Monitoring – Tracks message flow and processing 

## Architecture Flow

## Technology Stack
- ***Spring Boot 3*** – Java-based service framework
- ***Azure Event Hub*** – Event streaming service for message ingestion
- ***IBM MQ*** – Middleware for message queuing and transport
- ***Firebase*** - Notifying the devices in few scenarios
- ***Log4j2*** – Logging & tracing

## General Setup

### Prerequisites
- Java 21+
- Maven 3+
- Spring Boot 3
- Azure Event Hub & IBM MQ Credentials
- Firebase
- Okta OAuth config

### Steps to Run Locally

**Clone the repository:**

**Run the application**
```shell
mvn clean install
mvn spring-boot:run
```