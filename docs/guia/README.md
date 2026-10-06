# Guía paso a paso

Cómo pasé el simulador de `legacy/simulator.html` a Spring Boot. Un archivo por paso, en el mismo orden que los PRs. Cada paso dice **qué hice, por qué, y qué te pueden preguntar**.

1. [Entidades y repositorios](01-entidades.md)
2. [Manejo de errores](02-errores.md)
3. [CRUD de productos](03-crud-productos.md)
4. [Usuarios, email único y roles](04-usuarios.md)
5. [Crear un pedido, todo o nada](05-pedidos.md)
6. [Historial y estados del pedido](06-estados-e-historial.md)
7. [Reporte de stock bajo](07-stock-bajo.md)
8. [PostgreSQL, Docker y Flyway](08-postgres-y-flyway.md)

## Cómo estudiar esto

1. Leé un paso y abrí el PR correspondiente en GitHub para ver el diff real.
2. Respondé las preguntas del final **en voz alta, sin mirar**. Si te trabás, anotalo en tu `interview-notes/`.
3. Rompé algo a propósito (sacá el `@Transactional`, cambiá `<=` por `<`) y corré `mvn test` para ver qué test falla y por qué.
4. Contá el proyecto entero en 2 minutos: de dónde salió, la regla del pedido todo o nada y un bug del original que encontraste.
