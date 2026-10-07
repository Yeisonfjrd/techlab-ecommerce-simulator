# TechLab e-commerce · Spring Boot

A REST backend for a small store: products, categories, users and orders with stock control.

It started as my final project for TechLab's back-end course: a single HTML page with the whole store simulated in JavaScript ([`legacy/simulator.html`](legacy/simulator.html)). This repo rebuilds that business logic as a real backend, to practice taking rules that lived in the browser and putting them behind an API with a database, validation and tests.

Java 21 · Spring Boot 4 · Spring Data JPA · Bean Validation · PostgreSQL + Flyway (H2 for local runs and tests) · springdoc OpenAPI

```
GET    /api/products?name=        search, or list everything
GET    /api/products/low-stock    stock <= techlab.stock.low-threshold (5)
GET    /api/products/{id}
POST   /api/products              201 + Location
PUT    /api/products/{id}
DELETE /api/products/{id}         409 if it was ever ordered
GET    /api/categories
POST   /api/categories            409 on duplicate name
GET    /api/users
POST   /api/users                 409 on duplicate email
GET    /api/users/{id}
DELETE /api/users/{id}            409 if the user has orders
GET    /api/users/{id}/orders     newest first
POST   /api/orders                all or nothing on stock
GET    /api/orders/{id}
PATCH  /api/orders/{id}/status    PENDING → PAID → SHIPPED → DELIVERED, or CANCELLED (refunds stock)
```

Errors use `ProblemDetail` (RFC 9457): 400 with field errors, 404, 409 for business rules.

## Running it

```bash
mvn spring-boot:run          # H2 in memory
```

- API docs: http://localhost:8080/swagger-ui.html
- H2 console: http://localhost:8080/h2-console (JDBC URL from `application.yml`)

With PostgreSQL:

```bash
docker compose up -d
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
```

The schema comes from Flyway migrations in `src/main/resources/db/migration`; Hibernate only validates it. CI runs the whole test suite twice, on H2 and on a real PostgreSQL.

## Rules carried over from the simulator

- Price must be positive; stock can't be negative (also enforced by `check` constraints).
- An order is all or nothing: every line is checked for stock before any stock is deducted, in one transaction.
- Each order line stores the name and price at the time of the order.
- Products with 5 units or fewer count as low stock (configurable).
- User emails are unique.

## Things I changed on the way

- **Stock 0 is valid.** The simulator rejected it with `if (!productData.stock)`, because `!0` is `true` in JavaScript.
- **Repeated products in a cart are summed** before the stock check; the simulator checked each line on its own and could oversell.
- **Statuses follow a state machine** instead of accepting any string, and cancelling returns the stock.
- **Concurrent orders** can't oversell the same product: `@Version` on `Product` turns the second write into a 409.

## Known limits

- No authentication yet: roles exist on users but nothing checks them. Next step would be Spring Security with JWT and `@PreAuthorize` on admin endpoints.
- No pagination on list endpoints.
- The H2 test run and the PostgreSQL CI job cover the same suite; Testcontainers would let the PostgreSQL run happen locally too.

