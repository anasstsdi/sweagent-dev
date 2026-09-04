# Coding conventions

## Java
- Constructor injection only (no field `@Autowired`).
- One public class per file. Package by layer: `controller`, `service`, `repository`, `model`.
- Keep controllers thin: no business logic, delegate to the service.
- Prefer `Page<T>` / `Pageable` over manual `limit`/`offset`.

## Angular / TypeScript
- Standalone components. `providedIn: 'root'` services.
- Type every HTTP response. No `any`.
- Keep component templates inline for small components.

## General
- Change only what the ticket requires.
- Every behaviour change is covered by a test.
