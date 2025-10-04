# RAG Chat Storage - Minimal Spring Boot Project

This is a minimal, ready-to-run Spring Boot project for storing chat sessions and messages for a RAG-based chatbot.

## Features included
- Create chat sessions
- Add messages to sessions
- Rename session, mark favorite, delete session (and messages)
- API key authentication via `X-API-KEY` header
- Simple in-memory rate limiting per API key
- Swagger UI (OpenAPI) at `/swagger-ui.html`
- Docker + docker-compose with Postgres and Adminer

## Quick start (with Docker)
1. Copy `.env.example` to `.env` and edit values.
2. Create jar file with command
    ```bash
    ./mvnw clean package -DskipTests
    ```
3. Run:
   ``` )
   docker compose up --build
   ```
4. The app will be available at `http://localhost:8080`.
5. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## APIs (examples)
- `POST /api/v1/sessions` - create session
- `PATCH /api/v1/sessions/{id}/rename` - rename session
- `PATCH /api/v1/sessions/{id}/favorite` - mark favorite
- `DELETE /api/v1/sessions/{id}` - delete session
- `POST /api/v1/sessions/{sessionId}/messages` - add message
- `GET /api/v1/sessions/{sessionId}/messages` - list messages (pagination: page, size)

Include header `X-API-KEY` with your API key from `.env`.

## Build & run locally
Requires JDK 17 and Maven.
```
./mvnw spring-boot:run
```

