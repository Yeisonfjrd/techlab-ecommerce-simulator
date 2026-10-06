# Paso 7: reporte de stock bajo

**PR:** issue #8 · **Archivos:** `StockProperties.java`, `ProductRepository`, `ProductService.lowStock`, `LowStockApiTest.java`

## Qué había en el simulador

```js
const MIN_STOCK_THRESHOLD = 5;
const lowStockProducts = products.filter(p => p.stock <= MIN_STOCK_THRESHOLD);
```

## Qué hice

1. **El umbral pasó a ser configuración**, no una constante en el código:

   ```yaml
   techlab:
     stock:
       low-threshold: 5
   ```

   Se lee con un `record` anotado con `@ConfigurationProperties(prefix = "techlab.stock")`, con 5 por defecto (`@DefaultValue`). En producción se puede cambiar sin recompilar, por ejemplo con la variable de entorno `TECHLAB_STOCK_LOW_THRESHOLD=10` (Spring convierte el nombre solo).
2. **`@ConfigurationPropertiesScan`** en la clase principal registra el record como bean y se lo inyecto a `ProductService` por constructor.
3. **El filtro lo hace la base, no Java:** `findByStockLessThanEqualOrderByStockAsc` se traduce a `WHERE stock <= ? ORDER BY stock`. Traer todos los productos y filtrar en memoria, como hacía el JS, no escala.
4. **`<=` igual que el original.** El README decía "menos de 5"; lo corregí porque el simulador incluía el 5.
5. **`GET /api/products/low-stock`** está declarado como ruta literal. Spring prioriza una ruta literal sobre `/{id}`, así que `low-stock` nunca se intenta convertir a `Long`.

## El test

`@SpringBootTest(properties = "techlab.stock.low-threshold=3")` pisa la configuración **solo para ese test**. Así pruebo que el valor sale de la configuración y no está fijo en el código.

## Preguntas que te pueden hacer

**¿`@Value` o `@ConfigurationProperties`?**
`@Value("${techlab.stock.low-threshold}")` sirve para un valor suelto. `@ConfigurationProperties` agrupa varios valores relacionados en un objeto tipado, valida tipos al arrancar (si ponés `low-threshold: cinco`, la app no levanta) y se puede inyectar y testear como cualquier bean.

**¿Qué orden de prioridad tiene la configuración en Spring Boot?**
De mayor a menor (simplificado): argumentos de línea de comandos → variables de entorno → `application-{perfil}.yml` → `application.yml` → valores por defecto en el código.
