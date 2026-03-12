# Job Service

Job Service for **SmartHire** — manages job postings, persists to PostgreSQL, and publishes `job.posted` events to Kafka.

> **Full platform:** See the [main SmartHire README](../README.md) for architecture, event flow, and all services.

## Tech Stack

- **Java 21** · **Spring Boot 3.2**
- **Spring Data JPA** · **PostgreSQL**
- **Spring Kafka** (optional when broker is unavailable)
- **Lombok** · **Maven**

## Prerequisites

- Java 21
- Maven 3.8+
- Docker & Docker Compose (for PostgreSQL and Kafka)

## Quick Start

### 1. Start infrastructure

```bash
cd job-service
docker compose up -d
```

This starts:

- **PostgreSQL** on `localhost:5556` (database: `jobdb`, user: `smarthire`, password: `smarthire123`)
- **Kafka** on `localhost:9092`

### 2. Run the application

```bash
mvn spring-boot:run
```

The API is available at **http://localhost:8082**.

To use a different port (e.g. if 8082 is in use):

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/api/jobs`     | List all jobs |
| `GET`  | `/api/jobs/active` | List active jobs only |
| `POST` | `/api/jobs`     | Create a job |

### Create a job (POST /api/jobs)

**Request body (JSON):**

```json
{
  "title": "Software Engineer",
  "company": "Acme Corp",
  "description": "Build great software with Java and Spring.",
  "location": "Remote",
  "salaryRange": "$100k-$150k",
  "requiredSkills": ["Java", "Spring Boot", "PostgreSQL"]
}
```

**Response:** The created job (with `id`, `status`, `createdAt`, etc.).

### List jobs (GET /api/jobs, GET /api/jobs/active)

**Response:** JSON array of jobs.

## Configuration

Main settings in `src/main/resources/application.yml`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8082 | HTTP port |
| `spring.datasource.url` | jdbc:postgresql://localhost:5556/jobdb | PostgreSQL URL |
| `spring.kafka.bootstrap-servers` | localhost:9092 | Kafka brokers |

Override via environment variables or `--key=value` (e.g. `--server.port=8083`).

## Dev profile (no PostgreSQL)

To run without PostgreSQL using an in-memory H2 database:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- Database: H2 in-memory
- H2 console: enabled (if dependency present)
- Kafka is still used if configured; to avoid Kafka, ensure it is not configured when using this profile.

## Testing the API

**cURL — list jobs:**

```bash
curl -s http://localhost:8082/api/jobs
```

**cURL — create job:**

```bash
curl -s -X POST http://localhost:8082/api/jobs \
  -H "Content-Type: application/json" \
  -d '{"title":"Software Engineer","company":"Acme","description":"Build software","location":"Remote","requiredSkills":["Java"]}'
```

In **Postman**, use base URL `http://localhost:8082` and the endpoints above (GET/POST with JSON body for create).

## Events

When a job is created, the service publishes a `JobPostedEvent` to the Kafka topic **`job.posted`** (if Kafka is available). If the broker is unreachable, the job is still saved and a warning is logged; the API response is unchanged.

## License

Part of the SmartHire project.
