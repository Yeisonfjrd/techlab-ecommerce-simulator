# Paso 4: usuarios, email único y roles

**PR:** issue #5 · **Archivos:** `user/*`, `UserApiTest.java`

## Qué había en el simulador

```js
if (users.some(u => u.email === userData.email)) {
  throw new Error('Ya existe un usuario con este email.');
}
const newUser = new User(nextUserId++, userData.nombre, userData.email, userData.rol || 'cliente');
```

## Qué hice

1. **`Role` como enum** (`CLIENT`, `ADMIN`) en vez de los strings `'cliente'`/`'admin'`. Si alguien manda `"SUPERUSER"`, Jackson no lo puede convertir y la API devuelve 400 sola.
2. **`@Enumerated(EnumType.STRING)`.** Guarda `"CLIENT"` en la base y no `0`. Ver la pregunta de abajo.
3. **Tabla `users`, no `user`:** `user` es palabra reservada en PostgreSQL.
4. **Email en minúsculas antes de guardar.** El JS comparaba `===`, así que `Ana@Mail.com` y `ana@mail.com` eran dos cuentas distintas.
5. **Email único en dos lugares**, que es lo más interesante del paso:
   - **En el servicio:** `existsByEmail` antes de insertar, para devolver un 409 con un mensaje claro.
   - **En la base:** `unique = true` en la columna, y si el insert falla atrapo `DataIntegrityViolationException` y devuelvo el mismo 409.
6. **`saveAndFlush` en vez de `save`.** `save` puede dejar el `INSERT` para el final de la transacción, **fuera** de mi `try/catch`. `saveAndFlush` lo ejecuta en ese momento, así el error de la base cae adentro del `catch`.

## Preguntas que te pueden hacer

**Si ya chequeás con `existsByEmail`, ¿para qué el `unique` en la base?**
Por la **condición de carrera**. Dos pedidos al mismo tiempo con el mismo email:

```
Pedido A: existsByEmail → false
Pedido B: existsByEmail → false     (A todavía no insertó)
Pedido A: INSERT  ✔
Pedido B: INSERT  ✔   ← duplicado si no hay constraint
```

El chequeo del servicio da un buen mensaje; el constraint de la base es lo único que **garantiza** que no haya duplicados.

**¿Por qué `EnumType.STRING` y no `ORDINAL`?**
`ORDINAL` guarda la posición (`CLIENT = 0`, `ADMIN = 1`). Si mañana alguien agrega `GUEST` al principio del enum, todos los `0` de la base pasan a ser `GUEST` y los `1` (que eran admins) pasan a ser `CLIENT`. Con `STRING` el valor guardado no depende del orden.

**¿Qué pasa con la transacción cuando atrapás la excepción?**
Queda marcada *rollback-only*: aunque la atrape, no se puede hacer commit. Acá está bien, porque igual lanzo `BusinessRuleException` y se revierte todo.

**¿Qué falta para que los roles sirvan de verdad?**
Autenticación (Spring Security + JWT) y `@PreAuthorize("hasRole('ADMIN')")` en los endpoints de administración. Queda para el proyecto P5 del plan.
