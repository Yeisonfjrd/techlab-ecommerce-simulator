# Paso 2: manejo de errores

**PR:** issue #4 · **Archivos:** `common/error/*`

## Qué había en el simulador

Cada función tenía su `try/catch` y mostraba un cartel:

```js
} catch (error) {
  showMessage(`Error al agregar producto: ${error.message}`, 'error');
  return null;
}
```

En una API no hay cartel: el cliente necesita un **código HTTP** y un cuerpo que pueda leer.

## Qué hice

1. **Dos excepciones propias**, que son las únicas que lanzan los servicios:
   - `ResourceNotFoundException` → **404**. Pediste el producto 99 y no existe.
   - `BusinessRuleException` → **409 Conflict**. El pedido está bien armado pero choca con el estado actual: no hay stock, el email ya existe.
2. **Un solo `@RestControllerAdvice`** que las convierte en respuestas. Los controllers no tienen ni un `try/catch`.
3. **`ProblemDetail`** como formato. Es el estándar RFC 9457 y viene en Spring:

```json
{ "type": "about:blank", "title": "Not found", "status": 404, "detail": "Product 99 not found", "instance": "/api/products/99" }
```

4. **Errores de validación (400)** con la lista de campos, para que un frontend pueda marcar cada input:

```json
{ "title": "Invalid request", "status": 400, "errors": { "price": "must be greater than 0" } }
```

## El test

`@WebMvcTest` levanta solo la capa web (controllers, advice, Jackson), sin base de datos. Para probar el handler sin depender de los controllers reales, armé un controller **dentro del test** que tira cada excepción.

## Preguntas que te pueden hacer

**¿409 o 422 para "no hay stock"?**
Los dos se usan. Elegí 409 porque el problema no es el formato del pedido (eso sería 400/422) sino que choca con el estado actual del recurso: si mañana entra stock, el mismo pedido funciona.

**¿Por qué no devolver el mensaje de cualquier excepción?**
Porque una `NullPointerException` o un error de SQL pueden filtrar detalles internos (nombres de tablas, rutas). Solo mis excepciones, que tienen mensajes pensados para el cliente, llegan tal cual. Todo lo demás cae en el 500 genérico de Spring.

**¿Diferencia entre `@ControllerAdvice` y `@RestControllerAdvice`?**
`@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`: lo que devuelve el método se serializa como JSON en vez de buscar una vista.
