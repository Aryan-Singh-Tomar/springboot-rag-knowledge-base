# Secure Knowledge Base Q&A API (Spring Boot + RAG)

A production-style backend service built with **Spring Boot** that allows users to ingest documents and ask questions over them using a **Retrieval-Augmented Generation (RAG)**–ready architecture.  
The project emphasizes clean backend design, validation, pagination, observability, and a scalable foundation for AI-powered retrieval.

---

## ✨ What This Project Does

- Stores and manages documents in a knowledge base
- Exposes clean, validated REST APIs for document ingestion and listing
- Provides a question-answering entry point designed for RAG workflows
- Uses **pgvector-enabled PostgreSQL** to support semantic retrieval
- Returns predictable, client-friendly error responses
- Follows production-grade Spring Boot layering and conventions

This is **not a toy CRUD app** — it reflects how real backend services are structured and evolved.

---

## 🧠 RAG Architecture (How It Works)

High-level flow:

```
Client
  ↓
REST API (Controller)
  ↓
Service Layer (business logic)
  ↓
Repository (JPA / PostgreSQL + pgvector)
  ↓
Context Retrieval → Answer Generation (RAG-ready)
```

- Documents are ingested and stored in the database
- The `/chat/ask` API is designed as the entry point for RAG
- PostgreSQL is configured with **pgvector**, making the system ready for
  embedding storage and similarity-based retrieval
- Embedding generation and vector search can be plugged in without changing
  the API or core architecture

---

## 🧱 Architecture & Design Principles

The application follows a clean layered architecture:

```
Controller → Service → Repository → Database
```

### Key design decisions:
- **DTO-based APIs**: External contracts are isolated from internal models
- **Thin controllers**: Controllers handle HTTP only, no business logic
- **Expressive services**: All orchestration and rules live in services
- **Centralized error handling**: Consistent error responses across APIs
- **Pagination-first design**: Listing endpoints are pageable from day one

This keeps the codebase maintainable, testable, and extensible.

---

## 🛠 Tech Stack

- **Language:** Java
- **Framework:** Spring Boot, Spring MVC
- **Persistence:** Spring Data JPA
- **Database:** PostgreSQL (pgvector-enabled), H2 (demo/testing)
- **Validation:** Jakarta Bean Validation
- **Pagination:** Spring Data `Pageable`
- **API Docs:** Swagger / OpenAPI
- **Build Tool:** Maven
- **Deployment:** Docker-ready

---

## 📘 API Documentation (Swagger)

Swagger UI is available once the application is running:

```
http://localhost:8080/swagger-ui.html
```

Swagger allows:
- Interactive API exploration
- Request/response schema visibility
- Manual API testing without external tools

---

## 🔌 API Endpoints Overview

### Health Check
Verifies that the service is running.

```
GET /health
```

Response:
```
OK
```

---

### Create Document
Stores a document in the knowledge base.

```
POST /documents
```

Request:
```json
{
  "title": "Spring Boot Basics",
  "content": "Spring Boot simplifies Java backend development..."
}
```

Response:
```json
{
  "id": 1,
  "title": "Spring Boot Basics",
  "createdAt": "2024-06-01T10:30:00Z"
}
```

- Input is validated using Bean Validation
- Invalid requests return structured validation errors

---

### List Documents (Paginated)
Fetches documents using pagination and sorting.

```
GET /documents?page=0&size=10&sort=createdAt,desc
```

Response:
```json
{
  "content": [
    {
      "id": 1,
      "title": "Spring Boot Basics",
      "createdAt": "2024-06-01T10:30:00Z"
    }
  ],
  "totalPages": 1,
  "totalElements": 1
}
```

- Built using Spring Data `Pageable`
- Supports pagination and sorting out of the box

---

### Ask a Question (RAG Entry Point)
Accepts a question and returns an answer based on stored knowledge.

```
POST /chat/ask
```

Request:
```json
{
  "question": "What is Spring Boot?"
}
```

Response:
```json
{
  "answer": "Spring Boot is a framework that simplifies building Java applications..."
}
```

This endpoint is designed to:
- Retrieve relevant document context
- Generate answers grounded in stored knowledge
- Evolve into a full RAG pipeline without API changes

---

## 🚨 Error Handling

The API follows a consistent error response format.

Example validation error:
```json
{
  "timestamp": "2024-06-01T10:35:00Z",
  "status": 400,
  "error": "Validation failed",
  "path": "/documents",
  "fieldErrors": [
    {
      "field": "title",
      "message": "must not be blank"
    }
  ]
}
```

This makes client-side error handling predictable and reliable.

---

## ▶️ Running the Project Locally

### Prerequisites
- Java 17+
- Maven
- Docker (optional, for PostgreSQL)

---

### Option 1: Quick Demo (In-Memory DB)

```bash
mvn spring-boot:run
```

Use this mode to quickly explore APIs and Swagger without external dependencies.

---

### Option 2: PostgreSQL with pgvector (Docker)

```bash
docker run --name kb-postgres \
  -e POSTGRES_DB=kbdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d pgvector/pgvector:pg16
```

Then start the application:

```bash
mvn spring-boot:run
```

---

## 🔍 Why This Project Matters

This project demonstrates:
- Strong Spring Boot backend fundamentals
- Real-world API concerns: validation, pagination, error contracts
- Clean separation of concerns
- RAG-ready backend design using pgvector
- A foundation suitable for production and AI-powered extensions

---

## 🚀 Possible Enhancements

- Plug in embedding generation during document ingestion
- Enable similarity-based retrieval using pgvector indexes
- Add authentication and role-based access control
- Add source attribution to RAG answers
- Deploy using managed PostgreSQL and cloud services

---

## 📌 Summary

This repository showcases a clean, scalable backend service designed with
production practices and future AI integration in mind. It reflects how modern
Spring Boot applications are built, structured, and evolved.
