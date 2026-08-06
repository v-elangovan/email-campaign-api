# Email Campaign Management API

A Spring Boot REST API for creating, scheduling, processing, and tracking email campaigns.

> **Note:** This project simulates email delivery. No actual emails are sent.

---

# Features

- Create email campaigns
- Add multiple recipients
- Prevent duplicate recipients within a campaign
- Schedule campaigns
- Process scheduled campaigns
- Randomly simulate email delivery (Delivered / Failed)
- Campaign statistics
- Pagination
- Search by campaign name
- Filter by campaign status
- Sorting
- Request validation
- Global exception handling
- Environment-based configuration
- Database migration using Flyway
- Swagger/OpenAPI documentation
- Logging
- Unit testing

---

# Technology Stack

- Java 21 (or Java 25 if configured)
- Spring Boot 3.5.4
- Spring Data JPA
- MySQL
- Flyway
- Maven
- Lombok
- Swagger (SpringDoc OpenAPI)
- JUnit 5
- Mockito

---

# Project Structure

```
src
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── repository
├── service
│   └── impl
├── util
└── resources
    └── db
        └── migration
```

---

# Database Design

## Campaign

| Column | Type |
|----------|------|
| id | BIGINT |
| name | VARCHAR |
| subject | VARCHAR |
| sender_email | VARCHAR |
| content | TEXT |
| scheduled_at | DATETIME |
| status | VARCHAR |
| created_at | TIMESTAMP |
| updated_at | TIMESTAMP |

---

## Recipient

| Column | Type |
|----------|------|
| id | BIGINT |
| campaign_id | BIGINT |
| name | VARCHAR |
| email | VARCHAR |
| status | VARCHAR |
| created_at | TIMESTAMP |

Relationship

```
Campaign (1)
     |
     | One-To-Many
     |
Recipient (N)
```

A unique constraint is applied on:

```
(campaign_id, email)
```

to prevent duplicate recipients within the same campaign.

---

# Setup Instructions

## 1 Clone Repository

```bash
git clone https://github.com/v-elangovan/email-campaign-api.git

cd email-campaign-api
```

---

## 2 Create Database

```sql
CREATE DATABASE email_campaign_db;
```

---

## 3 Configure Environment Variables

Create a `.env` file in the project root.

Example:

```properties
DB_URL=jdbc:mysql://localhost:3306/email_campaign_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

DB_USERNAME=root

DB_PASSWORD=root
```

A sample configuration is provided in:

```
.env.example
```

---

## 4 Run Flyway Migration

Flyway migrations execute automatically during application startup.

Migration files:

```
V1__create_tables.sql

V2__add_indexes.sql
```

---

## 5 Run Application

Using Maven

```bash
mvn spring-boot:run
```

or

```bash
mvn clean install
```

---

# Swagger Documentation

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON

```
http://localhost:8080/api-docs
```

---

# API Endpoints

## Campaign

### Create Campaign

```
POST /api/campaigns
```

---

### Schedule Campaign

```
POST /api/campaigns/{campaignId}/schedule
```

---

### Process Scheduled Campaigns

```
POST /api/campaigns/process
```

---

### Get Campaign List

```
GET /api/campaigns
```

Supports

- Pagination
- Search
- Filter
- Sorting

Example

```
GET /api/campaigns?page=0&size=10&status=SCHEDULED&search=sale
```

---

### Get Campaign Details

```
GET /api/campaigns/{campaignId}
```

---

### Campaign Statistics

```
GET /api/campaigns/{campaignId}/statistics
```

---

## Recipient

### Add Recipients

```
POST /api/campaigns/{campaignId}/recipients
```

Supports adding multiple recipients in one request.

---

# Business Rules

### Campaign

- New campaign starts with **DRAFT** status.
- Only **DRAFT** campaigns can be scheduled.
- Campaign must contain at least one recipient before scheduling.
- Scheduled time must be in the future.

---

### Recipient

- Email validation
- Duplicate emails are not allowed within the same campaign.
- Multiple recipients can be added in one request.

---

### Processing

- Only campaigns with status **SCHEDULED** are processed.
- Only campaigns whose scheduled time has arrived are processed.
- Recipient status is randomly assigned as:
    - DELIVERED
    - FAILED
- Campaign status changes to **COMPLETED** after processing.
- Pessimistic locking is used to prevent concurrent processing.

---

# Error Handling

The application provides consistent JSON error responses.

Examples

- 400 Bad Request
- 404 Not Found
- 409 Conflict
- 500 Internal Server Error

Handled using:

- GlobalExceptionHandler
- Custom Exceptions

---

# Validation

The application validates:

- Required fields
- Email format
- Future scheduled date
- Duplicate recipients
- Campaign state

---

# Logging

Logging is implemented using SLF4J.

Logs include:

- Campaign creation
- Recipient addition
- Campaign scheduling
- Campaign processing
- Errors

---

# Unit Tests

Implemented using:

- JUnit 5
- Mockito

Covered scenarios include:

- Create campaign
- Schedule campaign
- Duplicate recipient validation
- Campaign processing
- Campaign statistics
- Validation failures

Run tests

```bash
mvn test
```

---

# Important Technical Decisions

- Layered architecture (Controller → Service → Repository)
- Flyway for database version control
- Environment variables for database credentials
- Pessimistic locking for concurrency safety
- Batch save (`saveAll`) for better performance
- Global exception handling for consistent API responses
- DTOs used instead of exposing entities directly

---

# Assumptions

- Email sending is simulated.
- Authentication is not implemented.
- One campaign may contain multiple recipients.
- Recipient status is randomly generated.

---

# Production Improvements

If this project were deployed to production, the following improvements would be added:

- Docker & Docker Compose
- Spring Security / JWT Authentication
- Queue-based processing (RabbitMQ/Kafka)
- Retry mechanism for failed emails
- Email provider integration
- Integration testing
- CI/CD pipeline
- Monitoring (Prometheus/Grafana)
- Centralized logging (ELK Stack)
- API rate limiting
- Idempotency support
- Graceful shutdown

---

# Author

**M V Elangovan**

Backend Developer Assignment

Email: elangovanmv45@gmail.com
