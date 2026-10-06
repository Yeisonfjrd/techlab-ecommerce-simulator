# Paso 6: historial y estados del pedido

**PR:** issue #7 · **Archivos:** `OrderStatus.java`, `OrderService.java` (`history`, `updateStatus`), `UserOrdersController.java`, `OrderStatusTest.java`

## Qué había en el simulador

```js
updateOrderStatus: (orderId, newStatus) => {
  orders[orderIndex].status = newStatus;   // cualquier string, desde cualquier estado
}
```

Un pedido entregado podía volver a "pendiente", y uno cancelado no devolvía el stock.

## Qué hice

1. **Una máquina de estados dentro del enum.** Cada estado sabe a cuáles puede pasar:

   ```
   PENDING ──► PAID ──► SHIPPED ──► DELIVERED
      │          │
      └──────────┴──► CANCELLED
   ```

   `canMoveTo` usa un `switch` de Java 21. Si mañana agrego un estado y me olvido de ponerlo en el `switch`, **no compila**. Ese es el chequeo de exhaustividad que dan los switch expressions sobre enums.
2. **`PATCH /api/orders/{id}/status`.** Es `PATCH` porque cambia un solo campo, no reemplaza el pedido.
3. **Cancelar devuelve el stock.** Recorro las líneas y le sumo la cantidad a cada producto, dentro de la misma transacción que el cambio de estado.
4. **`GET /api/users/{userId}/orders`**, el más nuevo primero. Si el usuario no existe da 404, no una lista vacía, para que el cliente distinga "no existe" de "no compró nada".
5. **El controller del historial está en el paquete `order`**, no en `UserController`. Así `user` no depende de `order`: las dependencias van en una sola dirección (`order → product`, `order → user`).
6. **`@EntityGraph` en la consulta del historial** para evitar el problema N+1. Ver abajo.

## Los tests

- `OrderStatusTest` es un **test unitario puro**: sin Spring y sin base, tarda milisegundos. Usa `@ParameterizedTest` para probar cada transición sin escribir 10 métodos.
- `OrderApiTest` suma cuatro casos: el flujo completo, una transición prohibida (409), la cancelación devolviendo stock, y el historial.

## Preguntas que te pueden hacer

**¿Qué es el problema N+1?**
Pedís 20 pedidos (1 query) y para mostrar las líneas de cada uno Hibernate hace 1 query más por pedido (20 queries): 21 en total. Con `@EntityGraph(attributePaths = "lines")` Hibernate trae pedidos y líneas en **una** query con `JOIN`. También se puede resolver con `JOIN FETCH` en JPQL.

**¿Por qué la lógica de transiciones va en el enum y no en el servicio?**
Porque es una regla del **dominio**, no del flujo de la aplicación. Así se puede testear sola (`OrderStatusTest`) y cualquier parte del código que cambie el estado usa la misma regla.

**¿Diferencia entre test unitario y test de integración?**
El unitario prueba una pieza aislada y es rápido (`OrderStatusTest`). El de integración prueba varias capas juntas con Spring y la base (`OrderApiTest`): es más lento pero detecta problemas de configuración, mapeo y transacciones que un unitario no ve.
