# Paso 5: crear un pedido, todo o nada

**PR:** issue #6 · **Archivos:** `order/*`, cambios chicos en `Product`, `ProductService`, `UserService` y `GlobalExceptionHandler`

Este es el paso más importante del proyecto. Si te preguntan por una sola cosa, que sea esta.

## Qué había en el simulador

```js
for (const item of cartItems) {              // 1. valida todas las líneas
  if (product.stock < item.quantity) throw new Error('Stock insuficiente...');
  productsToUpdateStock.push({ product, quantity: item.quantity });
  orderLines.push(new OrderLine(item.productId, item.quantity, product.precio, product.nombre));
}
for (const { product, quantity } of productsToUpdateStock) {   // 2. recién ahí descuenta
  product.stock -= quantity;
}
```

La idea ya estaba bien: **primero validar todo, después tocar**. Lo que no había era una base de datos ni concurrencia.

## Qué hice

1. **`Order` y `OrderLine`** (`@OneToMany` con `cascade = ALL` y `orphanRemoval`). El pedido es dueño de sus líneas: guardás el pedido y se guardan las líneas. `addLine` mantiene sincronizados los dos lados de la relación y suma el total.
2. **La línea guarda una copia del nombre y del precio** (`productName`, `unitPrice`), igual que `priceAtTimeOfOrder` en el JS. Si mañana sube el precio del mouse, el pedido de ayer sigue diciendo lo que pagaste. Lo prueba `theLineKeepsThePriceItWasBoughtAt`.
3. **Dos fases en `OrderService.create`:** validar todas las líneas y después descontar stock. Si la segunda línea no tiene stock, la primera nunca se tocó.
4. **`@Transactional` en el método.** Aunque algo fallara a mitad de la fase 2, la base vuelve a como estaba.
5. **El mismo producto dos veces en el carrito se suma.** El JS no lo contemplaba: dos líneas de 3 mouses con stock 5 pasaban cada una su chequeo y el stock quedaba en -1. Lo prueba `theSameProductTwiceIsCheckedAsOneQuantity`.
6. **`@Version` en `Product`** (*optimistic locking*). Ver la pregunta de abajo.
7. **No se puede borrar un producto que ya se vendió** (ni un usuario con pedidos). Primero lo chequeo con una consulta JPQL (`isInAnyOrder`) para dar un 409 con un mensaje claro. La clave foránea de la base queda como red de seguridad, y el `flush()` hace que, si falla, falle adentro del método.

   > Lo aprendí con un test: mi primera versión confiaba solo en la clave foránea. En el test, como todo corre en la misma sesión de Hibernate, Hibernate detectó el problema **antes** de llegar a la base y tiró otra excepción (`TransientPropertyValueException`). Chequear explícitamente es más claro y no depende de ese detalle.

## Endpoints

```
POST /api/orders        {"userId": 1, "items": [{"productId": 3, "quantity": 2}]}  → 201
GET  /api/orders/{id}
```

## Preguntas que te pueden hacer

**¿Qué hace `@Transactional`?**
Spring envuelve el bean en un **proxy**. Cuando llamás `orderService.create(...)`, en realidad llamás al proxy, que abre la transacción, llama a tu método y hace commit. Si sale una `RuntimeException`, hace rollback.

**¿Y si llamo a un método `@Transactional` desde la misma clase?**
`this.otroMetodo()` **no pasa por el proxy**, así que esa anotación se ignora. Es la trampa clásica. Por eso la anotación está en el método público que llama el controller.

**¿Por qué rollback con `RuntimeException` y no con cualquier excepción?**
Por defecto Spring solo hace rollback con *unchecked exceptions*. Mis excepciones extienden `RuntimeException` justamente por eso. Para *checked* hay que poner `@Transactional(rollbackFor = Exception.class)`.

**Dos clientes compran el último mouse al mismo tiempo. ¿Qué pasa?**
Sin protección, los dos leen `stock = 1`, los dos validan y los dos escriben `stock = 0`: vendiste dos mouses que no tenías (*lost update*). Con `@Version`, Hibernate hace `UPDATE products SET stock = 0, version = 2 WHERE id = ? AND version = 1`. El segundo `UPDATE` no encuentra la fila con `version = 1`, Spring lanza `OptimisticLockingFailureException` y la API devuelve 409 "intentá de nuevo". La alternativa es *pessimistic locking* (`SELECT ... FOR UPDATE`), que bloquea la fila mientras dura la transacción.

**¿Por qué `cascade` en `Order → OrderLine` pero no en `OrderLine → Product`?**
Una línea no existe sin su pedido, así que el pedido maneja su ciclo de vida. Un producto existe solo y lo comparten muchos pedidos: borrar o guardar una línea nunca tiene que tocar el producto.
