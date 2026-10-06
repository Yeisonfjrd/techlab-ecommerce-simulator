# Paso 8: PostgreSQL, Docker y migraciones con Flyway

**PR:** issue #9 · **Archivos:** `db/migration/V1__initial_schema.sql`, `docker-compose.yml`, `application*.yml`, `pom.xml`, `.github/workflows/ci.yml`

## El problema

Hasta acá Hibernate creaba las tablas solo (`ddl-auto: create-drop`): las borraba y recreaba en cada arranque. Sirve con H2 en memoria, pero en una base real perdés todos los datos en cada deploy.

## Qué hice

1. **Flyway** (`spring-boot-starter-flyway` + `flyway-database-postgresql`). Al arrancar, Flyway mira la tabla `flyway_schema_history`, ve qué migraciones faltan y las corre **en orden y una sola vez**.
2. **`V1__initial_schema.sql`** con todo el esquema escrito a mano. Además de lo que ya garantizaba Java, la base ahora también se defiende sola:
   - `check (price > 0)`, `check (stock >= 0)`, `check (quantity > 0)`: aunque alguien escriba directo en la base, el stock no puede quedar negativo.
   - **Índices en las claves foráneas.** PostgreSQL no los crea solo, y sin ellos `findByUser_Id...` recorre la tabla entera.
3. **`ddl-auto: validate`.** Hibernate ya no toca el esquema: solo verifica al arrancar que coincide con las entidades. Si me olvido de una columna, la app no levanta (mejor eso que un error en producción).
4. **H2 en modo PostgreSQL** (`MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE`) para que **la misma migración** corra en los tests y en la base real.
5. **`docker-compose.yml`** con PostgreSQL 17:

   ```bash
   docker compose up -d
   SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
   ```

6. **Un segundo job de CI** que levanta PostgreSQL como *service container* y corre **todos** los tests contra la base real. Así sé que las migraciones y las consultas funcionan en la base donde se va a desplegar, no solo en H2.

## Cómo se agrega un cambio de esquema a partir de ahora

1. Cambiás la entidad (por ejemplo, agregar `phone` a `User`).
2. Creás `V2__add_user_phone.sql` con `alter table users add column phone varchar(30);`.
3. **Nunca** editás `V1`. Flyway guarda un checksum de cada migración ya aplicada y, si cambió, se niega a arrancar.

## Preguntas que te pueden hacer

**¿Por qué no `ddl-auto: update` en producción?**
- Solo agrega cosas: nunca borra columnas ni renombra. Si renombrás un campo, te quedan dos columnas.
- No hay historial: no sabés qué cambió ni cuándo, y no se puede revisar en un PR.
- Puede hacer cambios que bloquean tablas grandes sin avisar.

Con Flyway cada cambio es un archivo SQL versionado, revisado en un PR y aplicado igual en todos los entornos.

**¿Flyway o Liquibase?**
Los dos hacen lo mismo. Flyway usa SQL plano y es más simple; Liquibase permite escribir los cambios en XML/YAML independientes de la base y tiene rollbacks declarativos. Para un proyecto así, Flyway alcanza.

**¿Para qué testear contra PostgreSQL si ya pasa en H2?**
Porque H2 no es PostgreSQL, aunque lo imite. Tipos, funciones, mayúsculas y orden de los `NULL` pueden comportarse distinto. El job de CI con PostgreSQL real atrapa esas diferencias antes de producción. La alternativa más común en empresas es **Testcontainers**, que levanta el contenedor desde el mismo test.
