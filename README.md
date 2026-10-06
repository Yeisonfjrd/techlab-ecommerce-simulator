# TechLab e-commerce · Spring Boot

**Work in progress.** A REST backend for a small store: products, categories, users and orders with stock control.

It started as my final project for TechLab's back-end course: a single HTML page with the whole store simulated in JavaScript ([`legacy/simulator.html`](legacy/simulator.html)). This repo rebuilds that business logic as a real backend, to practice taking rules that lived in the browser and putting them behind an API with a database, validation and tests.

Java 21 · Spring Boot 4 · Spring Data JPA · Bean Validation · H2 (PostgreSQL via profile) · springdoc OpenAPI

## Running it

```bash
mvn spring-boot:run
```

- API docs: http://localhost:8080/swagger-ui.html
- H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:techlab`)
- With PostgreSQL: `SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run` (reads `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`)

## Rules carried over from the simulator

- Price must be positive; stock can't be negative.
- An order is all or nothing: every line is checked for stock before any stock is deducted.
- Each order line stores the price at the time of the order, so later price changes don't rewrite history.
- Products under 5 units count as low stock.
- User emails are unique.

## Roadmap

Tracked as [issues](../../issues), one PR each.
