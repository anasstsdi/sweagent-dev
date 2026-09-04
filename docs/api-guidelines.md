# API guidelines

- Base path: `/api`.
- Resource collections are plural nouns: `/api/users`.
- Return DTOs or entities directly (this demo returns entities for simplicity).
- Collections are **paginated** — see `pagination.md`.
- HTTP status: `200` for reads, `201` for creates, `204` for deletes.
- Errors return a JSON body `{ "error": "...", "message": "..." }` (not yet enforced in the demo).
- Do not break existing endpoint contracts without an ADR.
