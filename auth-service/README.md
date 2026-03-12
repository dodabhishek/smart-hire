# SmartHire AI 🤖
### Microservices-Based AI Recruitment Platform

> Built with **Java 21 · Spring Boot 3 · Apache Kafka · PostgreSQL · OpenAI GPT-4o · Docker**

A production-grade recruitment platform where AI automatically matches candidates to jobs using event-driven microservices architecture. When a candidate registers, a Kafka event triggers AI analysis against every active job — scoring compatibility 0–100 with detailed reasoning — all without any service knowing the others exist.

---

## 🏗️ Architecture

```
                    ┌─────────────────────────────┐
                    │        API Gateway           │
                    │    Spring Cloud Gateway      │
                    │    + Circuit Breaker         │
                    └──────┬─────────┬─────────┬───┘
                           │         │         │
              ┌────────────▼──┐ ┌────▼─────┐ ┌─▼──────────────┐
              │   Candidate   │ │   Job    │ │  AI Matching   │
              │   Service     │ │  Service │ │  Service       │
              │   :8081       │ │  :8082   │ │  :8083         │
              │   PostgreSQL  │ │ PostgreSQL│ │  PostgreSQL    │
              └──────┬────────┘ └────┬─────┘ └───────┬────────┘
                     │               │               │
                     └───────────────┼───────────────┘
                                     │
                           ┌─────────▼──────────┐
                           │    Apache Kafka     │
                           │                    │
                           │ • candidate.registered  
                           │ • job.posted        │
                           │ • match.found       │
                           └─────────┬──────────┘
                                     │
                          ┌──────────▼─────────┐
                          │ Notification Service│
                          │ :8084               │
                          │ (Email via SMTP)    │
                          └────────────────────┘
```

## 🔄 Event-Driven Flow

```
1. POST /api/candidates
      → Candidate Service saves to PostgreSQL
      → Publishes  candidate.registered  to Kafka

2. AI Matching Service hears candidate.registered
      → Fetches all active jobs from Job Service
      → Calls OpenAI GPT-4o for each job
      → Saves MatchResult (score 0–100) to PostgreSQL
      → Publishes  match.found  to Kafka

3. Notification Service hears match.found
      → If score ≥ 70 → sends HTML email to candidate

4. POST /api/jobs
      → Job Service saves to PostgreSQL
      → Publishes  job.posted  to Kafka

5. AI Matching Service hears job.posted
      → Fetches all active candidates
      → Runs AI match for each → publishes results
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2, Spring Cloud Gateway |
| AI Integration | Spring AI + OpenAI GPT-4o |
| Messaging | Apache Kafka (event-driven, async) |
| Database | PostgreSQL 16 (one DB per service) |
| Observability | Spring Actuator |
| Containerisation | Docker + Docker Compose |
| Build | Maven 3.9 |

---

## 📂 Project Structure

```
smart-hire/
│
├── docker-compose.yml              ← Spins up entire stack
├── .env.example                    ← Environment variable template
├── README.md
│
├── candidate-service/              ← Candidate CRUD + Kafka producer
│   ├── src/main/java/com/smarthire/candidateservice/
│   │   ├── controller/             ← REST endpoints
│   │   ├── service/                ← Business logic + Kafka publish
│   │   ├── repository/             ← JPA / PostgreSQL
│   │   ├── model/                  ← Candidate entity
│   │   ├── event/                  ← CandidateRegisteredEvent
│   │   └── config/                 ← Kafka producer config
│   ├── Dockerfile
│   └── pom.xml
│
├── job-service/                    ← Job CRUD + Kafka producer
│   ├── src/main/java/com/smarthire/jobservice/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── event/                  ← JobPostedEvent
│   │   └── config/
│   ├── Dockerfile
│   └── pom.xml
│
├── ai-matching-service/            ← OpenAI + Kafka consumer/producer
│   ├── src/main/java/com/smarthire/aimatching/
│   │   ├── service/                ← Spring AI + GPT-4o scoring
│   │   ├── consumer/               ← Kafka listeners
│   │   ├── model/                  ← MatchResult entity
│   │   └── config/
│   ├── Dockerfile
│   └── pom.xml
│
└── notification-service/           ← Kafka consumer → Email
    ├── src/main/java/com/smarthire/notification/
    │   ├── service/                ← Email sender
    │   ├── consumer/               ← match.found listener
    │   └── config/
    ├── Dockerfile
    └── pom.xml
```

---

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Docker Desktop
- OpenAI API key

### 1. Clone the repo
```bash
git clone https://github.com/dodabhishek/smart-hire.git
cd smart-hire
```

### 2. Set environment variables
```bash
cp .env.example .env
# Edit .env and add your OPENAI_API_KEY
```

### 3. Start infrastructure
```bash
# Start PostgreSQL, Kafka, Zookeeper
docker-compose up -d
```

### 4. Run each service
Open each service folder in IntelliJ and run, or:
```bash
cd candidate-service && mvn spring-boot:run &
cd job-service && mvn spring-boot:run &
cd ai-matching-service && mvn spring-boot:run &
cd notification-service && mvn spring-boot:run
```

---

## 📡 API Reference

### Candidate Service — `localhost:8081`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/candidates` | Register a new candidate |
| `GET` | `/api/candidates` | List all candidates |
| `GET` | `/api/candidates/{id}` | Get candidate by ID |

**Register a candidate:**
```bash
curl -X POST http://localhost:8081/api/candidates \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Abhishek",
    "lastName": "Thakur",
    "email": "abhishek@example.com",
    "currentTitle": "Java Developer",
    "yearsOfExperience": 3
  }'
```

### Job Service — `localhost:8082`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/jobs` | Post a new job |
| `GET` | `/api/jobs` | List all jobs |
| `GET` | `/api/jobs/active` | List active jobs only |

**Post a job:**
```bash
curl -X POST http://localhost:8082/api/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Senior Java Developer",
    "company": "TechCorp India",
    "description": "We need a Java expert to lead our backend team",
    "location": "Bangalore",
    "salaryRange": "25-35 LPA",
    "requiredSkills": ["Java", "Spring Boot", "Kafka", "PostgreSQL"]
  }'
```

---

## 🐳 Docker Services

| Container | Image | Port |
|---|---|---|
| `candidate-postgres` | postgres:16-alpine | 5555 |
| `job-postgres` | postgres:16-alpine | 5556 |
| `ai-postgres` | postgres:16-alpine | 5557 |
| `zookeeper` | confluentinc/cp-zookeeper:7.6.0 | 2181 |
| `kafka` | confluentinc/cp-kafka:7.6.0 | 9092 |
| `kafka-ui` | provectuslabs/kafka-ui | 8090 |

**View Kafka events visually:**
👉 http://localhost:8090

---

## 📋 Kafka Topics

| Topic | Published By | Consumed By |
|---|---|---|
| `candidate.registered` | Candidate Service | AI Matching Service, Notification Service |
| `job.posted` | Job Service | AI Matching Service |
| `match.found` | AI Matching Service | Notification Service |

---

## 🧠 Key Design Decisions

**Database per Service** — Each microservice owns its own PostgreSQL database. Services never share a database or query each other's tables directly.

**Async over Sync** — Services don't call each other via HTTP. They publish events to Kafka and move on. If a service is down, events wait and are processed when it recovers.

**AI as a Service** — The AI Matching Service is completely isolated. You can swap OpenAI for any other LLM (Claude, Gemini) by changing one config value.

**Circuit Breaker at Gateway** — The API Gateway uses Spring Cloud CircuitBreaker. If one service is slow, other services keep working normally.

---

## 🗺️ Roadmap

- [x] Candidate Service with Kafka events
- [x] Job Service with Kafka events
- [ ] AI Matching Service with OpenAI GPT-4o
- [ ] Notification Service with email
- [ ] API Gateway with circuit breaker
- [ ] Kubernetes manifests
- [ ] Distributed tracing with OpenTelemetry

---

## 👨‍💻 Author

**Abhishek Thakur**
- GitHub: [@dodabhishek](https://github.com/dodabhishek)
- LinkedIn: [abhishek-thakur](https://www.linkedin.com/in/abhishek-thakur-b50828280/)
- LeetCode: [dodabhishek](https://leetcode.com/dodabhishek) — Knight (2000+)

---

*Built with Java 21 · Spring Boot 3 · Apache Kafka · PostgreSQL · OpenAI · Docker*