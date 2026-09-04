# ADR 0001 — Pagination with Spring Data `Pageable`

- Status: accepted

## Context

Collection endpoints will grow. We need a consistent pagination approach across the API.

## Decision

Use Spring Data's `Pageable` + `Page<T>` end to end:
- controllers accept `Pageable` (with `@PageableDefault(size = 20)`),
- services pass it to `repository.findAll(pageable)`,
- the raw `Page<T>` JSON (`content`, `totalElements`, `totalPages`, `number`, `size`) is the
  response contract — no custom wrapper.

Query params: `page` (0-based), `size` (default 20, cap 100).

## Consequences

- Zero custom pagination code; consistent across every collection endpoint.
- Frontend types responses as `Page<T>`.
- Changing an endpoint from `List<T>` to `Page<T>` is a breaking contract change and must be
  reflected in the frontend in the same change.
