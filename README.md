# Secure Knowledge Base Q&A API (RAG)

Spring Boot backend service for storing documents and answering questions
using a Retrieval-Augmented Generation (RAG) approach.

## Features
- Document ingestion APIs
- Paginated REST APIs using Spring Data JPA
- Clean validation and centralized error handling
- Production-style layered architecture

## Tech Stack
- Java, Spring Boot
- Spring Data JPA, PostgreSQL
- Docker (pgvector ready)

## Run Locally

### Start Postgres
```bash
docker run --name kb-postgres \
  -e POSTGRES_DB=kbdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d pgvector/pgvector:pg16
