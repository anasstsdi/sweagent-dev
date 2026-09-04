# Architecture

```
Angular (UserListComponent) ──HTTP──► Spring Boot
                                        UserController  (/api/users)
                                          └► UserService
                                               └► UserRepository (Spring Data JPA)
                                                    └► H2 (in-memory)
```

- **Layered**: controller → service → repository. No layer skipping.
- Persistence via Spring Data JPA. `JpaRepository` gives CRUD + `findAll(Pageable)` for free.
- No external DB for the demo: H2 in-memory, seeded at startup.
- The frontend calls the backend under `/api` (proxied in dev).

## Extension points relevant to the demo ticket

Adding pagination touches exactly three files: `UserController`, `UserService`, and the Angular
`UserService` (+ `UserListComponent` for the controls). The repository needs no change —
`findAll(Pageable)` already exists.
