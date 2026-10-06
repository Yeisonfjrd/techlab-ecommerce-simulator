# Paso 1: entidades y repositorios

**PR:** issue #2 · **Archivos:** `category/Category.java`, `product/Product.java`, los dos `*Repository.java`, `ProductRepositoryTest.java`

## Qué había en el simulador

```js
class Product {
  constructor(id, nombre, descripcion, precio, categoria, imagen, stock) { ... }
}
```

Todo vivía en un array en memoria y el `id` se calculaba a mano (`nextProductId++`). La categoría era un string libre.

## Qué hice

1. **`Category` como tabla propia.** En el simulador, si escribías "Perifericos" y "Periféricos" tenías dos categorías distintas sin darte cuenta. Con una tabla y `unique = true` en el nombre eso no puede pasar.
2. **`Product` con `@ManyToOne` a `Category`.** Muchos productos, una categoría. Puse `fetch = LAZY` para que traer un producto no traiga la categoría si no la uso.
3. **`price` como `BigDecimal` con `precision = 12, scale = 2`.** Ver la pregunta de abajo.
4. **El `id` lo genera la base** (`GenerationType.IDENTITY`), ya no `nextProductId++`.
5. **Constructor `protected` vacío.** JPA lo necesita para crear el objeto al leer de la base. Es `protected` para que desde mi código no se pueda crear un `Product` vacío por error.
6. **Repositorios que extienden `JpaRepository`.** Me dan `save`, `findById`, `findAll`, `delete` sin escribir SQL. `findByNameContainingIgnoreCase` es un *derived query*: Spring arma el `WHERE lower(name) LIKE lower(?)` a partir del nombre del método. Es lo mismo que hacía `getProductByName` en el JS.

## El test

`@DataJpaTest` levanta **solo** la capa de JPA con H2 en memoria, no toda la app. Cada test corre en una transacción que se revierte al final, así que no se pisan entre sí.

El detalle importante es `em.flush(); em.clear();`. Sin eso, `findById` me devuelve el mismo objeto que acabo de guardar desde la caché de Hibernate y el test no prueba nada de la base. Con `clear()` lo obliga a ir a buscarlo de verdad.

## Preguntas que te pueden hacer

**¿Por qué `BigDecimal` y no `double` para la plata?**
`double` es binario y no puede representar exacto 0.1. `0.1 + 0.2` da `0.30000000000000004`. En un carrito con muchas líneas el error se acumula. `BigDecimal` guarda el número decimal exacto. En la base es `NUMERIC(12,2)`.

**¿Por qué `isEqualByComparingTo` y no `isEqualTo` en el test?**
Porque para `BigDecimal`, `equals` compara también la escala: `19.99` y `19.990` no son `equals`, pero sí valen lo mismo. `compareTo` compara solo el valor.

**¿Qué es LAZY y qué problema trae?**
La relación se carga recién cuando la usás. Si la usás fuera de una transacción tira `LazyInitializationException`. Por eso en `application.yml` está `open-in-view: false` y los controllers van a devolver DTOs armados dentro del servicio (paso 2).
