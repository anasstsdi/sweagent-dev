# demo-repository

A tiny Java Spring Boot + Angular project. This is the **repository the AI Software Engineer
Agent works on** — its source of truth. The agent reads and edits these files directly
(filesystem + ripgrep); it never receives the whole repo in a prompt.

## Layout

```
backend/   Spring Boot (Java 21, Maven, H2 in-memory, JUnit)
  com.example.demo
    controller/UserController   REST: GET/POST /api/users, GET/PUT/DELETE /api/users/{id}
    service/UserService         business layer, @Transactional, 404 on missing id
    repository/UserRepository   extends JpaRepository<User, Long>
    model/User                  @Entity mapped to table "users"
    dto/UserInput               validated request body (name, email) — keeps the entity off the API
    config/WebConfig            CORS for the Angular dev server
frontend/  Angular 18 (standalone components) — UserListComponent lists / adds / edits / deletes
docs/          project knowledge the RAG indexes
architecture/  same
ADR/           architecture decision records
```

## Run it end to end

**1. Backend** (port 8090, H2 in-memory, seeds 5 users on startup):

```bash
cd backend && mvn spring-boot:run
```

- API: `http://localhost:8090/api/users`
- Browse the DB: `http://localhost:8090/h2-console` (JDBC URL `jdbc:h2:mem:demo`, user `sa`, no password)

**2. Frontend** (port 4200, proxies `/api` → `:8090`):

```bash
cd frontend && npm install && npm start
```

Open `http://localhost:4200` — the table lists the users from the database; you can add,
edit inline, and delete. Every change calls the API and re-fetches the list.

## Test

```bash
cd backend  && mvn test                    # 7 MockMvc tests over the CRUD endpoints
cd frontend && npm test                    # type-checks the sources
```

## Data flow

```
UserListComponent ──HTTP /api/users──► [proxy :4200→:8090] ──► UserController
                                                                   │
                                                            UserService (@Transactional)
                                                                   │
                                                            UserRepository (Spring Data JPA)
                                                                   │
                                                              H2 (jdbc:h2:mem:demo)
```
