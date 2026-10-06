# Paso 3: CRUD de productos con DTOs y validación

**PR:** issue #3 · **Archivos:** `product/*Request|Response|Service|Controller.java`, `category/*`, `ProductApiTest.java`

## Las tres capas

```
HTTP  ──►  ProductController   (recibe JSON, valida, devuelve códigos HTTP)
              │
              ▼
           ProductService      (reglas de negocio, @Transactional)
              │
              ▼
           ProductRepository   (SQL, lo genera Spring Data)
```

En el simulador todo esto estaba mezclado en `ProductService` del JS: validaba, guardaba en el array **y** mostraba el mensaje en pantalla.

## Qué hice

1. **DTOs como `record`.** `ProductRequest` es lo que manda el cliente y `ProductResponse` lo que devuelvo. La entidad `Product` nunca sale del servicio. Ver la pregunta de abajo.
2. **Validación con anotaciones** en `ProductRequest`, la misma que hacía el JS pero declarada:

   | Simulador (JS) | Ahora |
   |---|---|
   | `if (!productData.nombre)` | `@NotBlank` |
   | `parseFloat(precio) <= 0` | `@Positive` |
   | `parseInt(stock) < 0` | `@PositiveOrZero` |

   `@Valid` en el controller dispara la validación. Si falla, Spring lanza `MethodArgumentNotValidException` y el handler del paso 2 devuelve 400 con los campos.
3. **El bug del stock 0.** El JS hacía `if (!productData.stock) throw ...`. En JavaScript `!0` es `true`, así que **un producto con stock 0 se rechazaba** aunque el mensaje decía que solo el negativo era inválido. Con `@PositiveOrZero` el 0 es válido, y hay un test (`stockZeroIsValid`) que lo deja escrito.
4. **`POST` devuelve 201 + header `Location`** con la URL del recurso nuevo (`/api/products/7`). Es lo que espera un cliente REST.
5. **`PUT` sin `save()`.** La entidad que trae `findById` está *managed*: Hibernate detecta los cambios y hace el `UPDATE` al cerrar la transacción (*dirty checking*).
6. **`@Transactional(readOnly = true)` en la clase** y `@Transactional` en los métodos que escriben. `readOnly` le avisa a Hibernate que no tiene que buscar cambios, y es más rápido.
7. **Categorías:** `GET` y `POST /api/categories`. Un nombre repetido (sin importar mayúsculas) devuelve 409.

## Endpoints

```
GET    /api/products?name=tec     buscar (sin name: todos)
GET    /api/products/{id}         uno, 404 si no existe
POST   /api/products              crear → 201 + Location
PUT    /api/products/{id}         reemplazar
DELETE /api/products/{id}         borrar → 204
GET    /api/categories
POST   /api/categories            409 si ya existe
```

Todo se puede probar desde Swagger: `http://localhost:8080/swagger-ui.html`.

## Los tests

`ProductApiTest` usa `@SpringBootTest` + `@AutoConfigureMockMvc`: levanta **toda** la app y le pega por HTTP simulado. Con `@Transactional` en la clase, cada test hace rollback y no ensucia al siguiente.

## Preguntas que te pueden hacer

**¿Por qué DTOs y no devolver la entidad?**
1. **Seguridad:** si mañana agrego un campo interno a `Product` (costo, proveedor), no se filtra solo en el JSON.
2. **Contrato estable:** puedo cambiar la base sin romper a los clientes de la API.
3. **LAZY:** serializar una entidad con relaciones LAZY fuera de la transacción tira `LazyInitializationException`. El DTO se arma dentro del servicio, con la transacción abierta.

**¿Por qué `record`?**
Son inmutables, con `equals`/`hashCode`/`toString` gratis y sin setters. Un DTO no tiene que cambiar después de creado.

**¿Diferencia entre `PUT` y `PATCH`?**
`PUT` reemplaza el recurso completo (por eso pide todos los campos obligatorios). `PATCH` modifica solo lo que mandás. Lo vas a ver en el paso de pedidos con `PATCH /status`.

**¿Qué es inyección por constructor y por qué no `@Autowired` en el campo?**
Las dependencias quedan `final` (no se pueden reemplazar), la clase se puede testear con `new ProductService(mockRepo, mockCategories)` sin Spring, y si una clase tiene demasiadas dependencias se nota en el constructor.
