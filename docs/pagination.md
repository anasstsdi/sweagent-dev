# Pagination — project convention

Les APIs REST qui renvoient une **collection** doivent être paginées.

## Backend (Spring)

- Le contrôleur accepte un paramètre `Pageable` (Spring Data) et renvoie `Page<T>`.
- Paramètres de requête : `page` (0-based, défaut 0) et `size` (défaut 20, max 100).
- Le service passe le `Pageable` au repository ; `JpaRepository` fournit déjà
  `findAll(Pageable)`.
- Exemple de signature attendue :

```java
// UserController
@GetMapping
public Page<User> list(@PageableDefault(size = 20) Pageable pageable) {
    return userService.list(pageable);
}

// UserService
public Page<User> list(Pageable pageable) {
    return users.findAll(pageable);
}
```

## Réponse

`Page<T>` sérialise en JSON avec `content`, `totalElements`, `totalPages`, `number`, `size`.
Ne pas créer d'enveloppe maison : on garde le format standard de Spring Data.

## Frontend (Angular)

- Le service envoie `?page=<n>&size=<n>` et type la réponse `Page<User>`
  (`content: User[]`, `totalElements: number`, ...).
- Le composant garde `page` et `size` en état et rappelle le service au changement de page.

## Tests

- Un test vérifie que `GET /api/users?page=0&size=2` renvoie 2 éléments et `totalElements = 5`.
