# TECHLAB E‑Commerce Simulator

**Repositorio:** `techlab-ecommerce-simulator`

## Descripción breve para GitHub

> **TechLab E‑Commerce Simulator**: Proyecto frontend en JavaScript que simula una API RESTful completa para la gestión de un e‑commerce (productos, categorías, usuarios, pedidos y control de stock), con interfaz interactiva y alertas de stock mínimo.

---

## 📖 Tabla de Contenidos

1. [Visión General](#visión-general)
2. [Características Principales](#características-principales)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Instalación y Ejecución](#instalación-y-ejecución)
6. [Uso del Sistema](#uso-del-sistema)
7. [Flujos de Trabajo](#flujos-de-trabajo)
8. [Pruebas y Validación](#pruebas-y-validación)
9. [Contribuciones](#contribuciones)
10. [Licencia](#licencia)
11. [Contacto](#contacto)

---

## 🔍 Visión General

Este proyecto, desarrollado como entrega final del curso de Back‑End en Java para TechLab, implementa un **simulador frontend** en JavaScript que reproduce la funcionalidad de una API RESTful de e‑commerce. Permite gestionar productos, categorías, usuarios y pedidos, así como controlar y ajustar el stock, sin necesidad de un backend real.

El objetivo principal es demostrar:

* Diseño de interfaces dinámicas e interactivas.
* Modelado de datos mediante clases JS (`Producto`, `Pedido`, `Usuario`, `Categoría`).
* Lógica de negocio completa: validaciones, manejo de estado y control de stock.
* Flujo de uso similar a una aplicación real, con menús, modales y alertas.

---

## ✨ Características Principales

* **Gestión de Productos (CRUD)**: Crear, listar, buscar (por ID y nombre), actualizar y eliminar productos con modal de confirmación.
* **Gestión de Categorías (CRUD)**: Crear, listar, editar y eliminar categorías; asociación de productos mediante `<select>` dinámico.
* **Gestión de Usuarios (CRUD)**: Simulación de usuarios con listado, alta y baja.
* **Carrito y Pedidos**:

  * Agregar productos al carrito con validación de stock.
  * Confirmar pedido: cálculo de total y descuento automático de stock.
  * Historial de pedidos con cambio de estados (`pendiente`, `confirmado`, `enviado`, `entregado`, `cancelado`).
* **Control de Stock**:

  * Alertas automáticas (modal) al caer por debajo de nivel mínimo (configurable).
  * Panel de administración para ajuste manual de stock.
* **Menú Principal**: Navegación clara con opciones para cada sección.

---

## 🛠️ Tecnologías Utilizadas

* **HTML5** y **CSS3**
* **JavaScript (ES6+)**
* **DOM Manipulation** para UI dinámica
* **Bootstrap** (opcional, o CSS propio) para estilos y modales

---

## 📂 Estructura del Proyecto

```
├── index.html            # Página principal con el menú y secciones
├── css/
│   └── styles.css        # Estilos globales
├── js/
│   ├── app.js            # Lógica principal y rutas simuladas
│   ├── models/
│   │   ├── Producto.js
│   │   ├── Categoria.js
│   │   ├── Usuario.js
│   │   └── Pedido.js
│   ├── controllers/
│   │   ├── ProductoController.js
│   │   ├── CategoriaController.js
│   │   ├── UsuarioController.js
│   │   └── PedidoController.js
│   └── utils/
│       └── helpers.js     # Funciones auxiliares (validaciones, DOM)
└── README.md             # Documentación del proyecto
```

---

## 🚀 Instalación y Ejecución

1. **Clonar repositorio**

   ```bash
   git clone https://github.com/<usuario>/techlab-ecommerce-simulator.git
   ```
2. **Abrir en el navegador**

   * Navegue a la carpeta del proyecto y abra `index.html`.
3. (Opcional) **Vista local con servidor**

   ```bash
   npm install -g http-server
   http-server .
   ```

---

## 🎯 Uso del Sistema

1. Seleccione la opción del Menú Principal.
2. Complete formularios o interactúe con las tablas.
3. Observe las alertas y modales que guían la operación.
4. Cambie estados de pedidos desde el Historial.
5. Ajuste stock manualmente desde Administración.

---

## 🧪 Pruebas y Validación

* Recorrer todos los flujos de CRUD en Productos, Categorías y Usuarios.
* Simular creación de pedidos hasta agotar stock y verificar alertas.
* Modificar estados de pedido en Historial.
* Ajustar stock y confirmar cambios en Productos.

---

## 🤝 Contribuciones

¡Bienvenidas! Para sugerencias o mejoras:

1. Haga un *fork* de este repositorio.
2. Cree una *branch* para su feature.
3. Abra un *pull request* describiendo cambios.

---

## 📄 Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

---

## 📬 Contacto

* **Profesor**: Equipo TechLab
* **Autor**: \Yeison Fajardo

---

*¡Gracias por revisar este proyecto!*
