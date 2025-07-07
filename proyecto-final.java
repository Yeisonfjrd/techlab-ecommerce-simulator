<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión - TechLab E-commerce</title>
    
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #e2e8f0;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 2rem;
        }
        .container {
            max-width: 1200px;
            width: 100%;
            margin: 0 auto;
            padding: 2.5rem;
            background-color: #ffffff;
            border-radius: 1.25rem;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
            border: 1px solid #cbd5e1;
        }
        .section-title {
            font-size: 2.25rem;
            font-weight: 800;
            color: #1a202c;
            margin-bottom: 2rem;
            text-align: center;
            letter-spacing: -0.025em;
            background: linear-gradient(to right, #6366f1, #8b5cf6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .btn {
            padding: 0.85rem 1.75rem;
            border-radius: 0.75rem;
            font-weight: 700;
            transition: all 0.3s ease-in-out;
            cursor: pointer;
            text-align: center;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            position: relative;
            overflow: hidden;
            z-index: 1;
        }
        .btn::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            background: rgba(255, 255, 255, 0.15);
            border-radius: 50%;
            transform: translate(-50%, -50%);
            transition: width 0.4s ease-in-out, height 0.4s ease-in-out;
            z-index: -1;
        }
        .btn:hover::before {
            width: 200%;
            height: 200%;
        }
        .btn-primary {
            background: linear-gradient(to right, #6366f1, #8b5cf6);
            color: #ffffff;
        }
        .btn-primary:hover {
            background: linear-gradient(to right, #4f46e5, #7c3aed);
            transform: translateY(-2px);
        }
        .btn-secondary {
            background-color: #64748b;
            color: #ffffff;
        }
        .btn-secondary:hover {
            background-color: #475569;
            transform: translateY(-2px);
        }
        .btn-success {
            background: linear-gradient(to right, #10b981, #059669);
            color: #ffffff;
        }
        .btn-success:hover {
            background: linear-gradient(to right, #059669, #047857);
            transform: translateY(-2px);
        }
        .btn-danger {
            background: linear-gradient(to right, #ef4444, #dc2626);
            color: #ffffff;
        }
        .btn-danger:hover {
            background: linear-gradient(to right, #dc2626, #b91c1c);
            transform: translateY(-2px);
        }
        .input-field {
            padding: 0.85rem;
            border: 1px solid #cbd5e1;
            border-radius: 0.625rem;
            width: 100%;
            box-sizing: border-box;
            background-color: #f8fafc;
            transition: border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
        }
        .input-field:focus {
            outline: none;
            border-color: #6366f1;
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
        }
        .form-group {
            margin-bottom: 1.25rem;
        }
        .hidden {
            display: none;
        }
        .section-panel {
            padding: 2rem;
            background-color: #f8fafc;
            border-radius: 1rem;
            box-shadow: inset 0 1px 3px 0 rgba(0, 0, 0, 0.05);
            border: 1px solid #e2e8f0;
        }
        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            border-radius: 0.75rem;
            overflow: hidden;
        }
        th, td {
            padding: 1rem 1.25rem;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
        }
        th {
            background-color: #f1f5f9;
            font-weight: 700;
            color: #334155;
            text-transform: uppercase;
            font-size: 0.875rem;
        }
        tbody tr:nth-child(odd) {
            background-color: #ffffff;
        }
        tbody tr:nth-child(even) {
            background-color: #f8fafc;
        }
        tbody tr:hover {
            background-color: #e0f2fe;
            cursor: pointer;
        }
        .modal-overlay {
            background-color: rgba(0, 0, 0, 0.6);
            backdrop-filter: blur(5px);
        }
        .modal-content {
            background-color: #ffffff;
            border-radius: 1rem;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
        }
    </style>
</head>
<body class="bg-gray-100 min-h-screen flex items-center justify-center">
    <div class="container">
        <h1 class="section-title">SISTEMA DE GESTIÓN - TECHLAB</h1>

        <div id="mainMenu" class="section-panel mb-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Menú Principal</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <button id="showProductManagement" class="btn btn-primary">1) Gestionar Productos</button>
                <button id="showCategoryManagement" class="btn btn-secondary" disabled title="Funcionalidad no implementada">2) Gestionar Categorías</button>
                <button id="showCart" class="btn btn-primary">3) Ver Carrito de Compras</button>
                <button id="showCreateOrder" class="btn btn-primary">4) Realizar Pedido</button>
                <button id="showOrderHistory" class="btn btn-primary">5) Consultar Historial de Pedidos</button>
                <button id="showAdmin" class="btn btn-primary">6) Administración (usuarios y stock)</button>
                <button id="exitApp" class="btn btn-danger col-span-full">7) Salir</button>
            </div>
        </div>

        <div id="productManagementSection" class="hidden section-panel">
            <h2 class="section-title text-blue-700">Gestión de Productos</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
                <button id="addProductBtn" class="btn btn-success">a) Agregar Producto</button>
                <button id="listProductsBtn" class="btn btn-primary">b) Listar Productos</button>
                <button id="searchProductBtn" class="btn btn-primary">c) Buscar Producto</button>
                <button id="updateProductBtn" class="btn btn-primary">d) Actualizar Producto</button>
                <button id="deleteProductBtn" class="btn btn-danger">e) Eliminar Producto</button>
                <button id="checkLowStockBtn" class="btn btn-secondary">f) Verificar Stock Mínimo</button>
                <button id="backToMainMenuFromProducts" class="btn btn-secondary col-span-full">g) Volver al menú principal</button>
            </div>

            <div id="addProductForm" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Agregar Nuevo Producto</h3>
                <div class="form-group">
                    <label for="newProductName" class="block text-gray-700 text-sm font-bold mb-2">Nombre:</label>
                    <input type="text" id="newProductName" class="input-field" placeholder="Nombre del producto">
                </div>
                <div class="form-group">
                    <label for="newProductDescription" class="block text-gray-700 text-sm font-bold mb-2">Descripción:</label>
                    <textarea id="newProductDescription" class="input-field" placeholder="Descripción del producto"></textarea>
                </div>
                <div class="form-group">
                    <label for="newProductPrice" class="block text-gray-700 text-sm font-bold mb-2">Precio:</label>
                    <input type="number" id="newProductPrice" class="input-field" placeholder="Precio" step="0.01">
                </div>
                <div class="form-group">
                    <label for="newProductCategory" class="block text-gray-700 text-sm font-bold mb-2">Categoría:</label>
                    <input type="text" id="newProductCategory" class="input-field" placeholder="Categoría">
                </div>
                <div class="form-group">
                    <label for="newProductImage" class="block text-gray-700 text-sm font-bold mb-2">URL de Imagen:</label>
                    <input type="text" id="newProductImage" class="input-field" placeholder="URL de imagen (ej: https://placehold.co/100x100)">
                </div>
                <div class="form-group">
                    <label for="newProductStock" class="block text-gray-700 text-sm font-bold mb-2">Stock:</label>
                    <input type="number" id="newProductStock" class="input-field" placeholder="Stock" min="0">
                </div>
                <button id="saveNewProduct" class="btn btn-success w-full mt-4">Guardar Producto</button>
            </div>

            <div id="listProductsDisplay" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Lista de Productos</h3>
                <div id="productsList" class="overflow-x-auto">
                </div>
            </div>

            <div id="searchProductForm" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Buscar Producto</h3>
                <div class="form-group">
                    <label for="searchProductId" class="block text-gray-700 text-sm font-bold mb-2">ID del Producto (opcional):</label>
                    <input type="number" id="searchProductId" class="input-field" placeholder="ID del producto">
                </div>
                <div class="form-group">
                    <label for="searchProductName" class="block text-gray-700 text-sm font-bold mb-2">Nombre del Producto (opcional):</label>
                    <input type="text" id="searchProductName" class="input-field" placeholder="Nombre del producto">
                </div>
                <button id="executeSearchProduct" class="btn btn-primary w-full mt-4">Buscar</button>
                <div id="searchProductResult" class="mt-6 p-4 bg-gray-50 rounded-md border border-gray-200">
                </div>
            </div>

            <div id="updateProductForm" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Actualizar Producto</h3>
                <div class="form-group">
                    <label for="updateProductId" class="block text-gray-700 text-sm font-bold mb-2">ID del Producto a Actualizar:</label>
                    <input type="number" id="updateProductId" class="input-field" placeholder="ID del producto">
                </div>
                <div class="form-group">
                    <label for="updateProductPrice" class="block text-gray-700 text-sm font-bold mb-2">Nuevo Precio (opcional):</label>
                    <input type="number" id="updateProductPrice" class="input-field" placeholder="Nuevo precio" step="0.01">
                </div>
                <div class="form-group">
                    <label for="updateProductStock" class="block text-gray-700 text-sm font-bold mb-2">Nuevo Stock (opcional):</label>
                    <input type="number" id="updateProductStock" class="input-field" placeholder="Nuevo stock" min="0">
                </div>
                <button id="executeUpdateProduct" class="btn btn-primary w-full mt-4">Actualizar</button>
            </div>

            <div id="deleteProductForm" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Eliminar Producto</h3>
                <div class="form-group">
                    <label for="deleteProductId" class="block text-gray-700 text-sm font-bold mb-2">ID del Producto a Eliminar:</label>
                    <input type="number" id="deleteProductId" class="input-field" placeholder="ID del producto">
                </div>
                <button id="executeDeleteProduct" class="btn btn-danger w-full mt-4">Eliminar</button>
            </div>

            <div id="lowStockDisplay" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Productos con Stock Bajo</h3>
                <div id="lowStockList" class="overflow-x-auto">
                </div>
            </div>
        </div>

        <div id="cartSection" class="hidden section-panel">
            <h2 class="section-title text-green-700">Ver Carrito de Compras</h2>
            <div id="cartContent" class="p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
            </div>
            <button id="backToMainMenuFromCart" class="btn btn-secondary w-full">Volver al menú principal</button>
        </div>

        <div id="createOrderSection" class="hidden section-panel">
            <h2 class="section-title text-yellow-700">Realizar Pedido</h2>
            <div class="p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Agregar Producto al Carrito</h3>
                <div class="form-group">
                    <label for="productIdToAddToCart" class="block text-gray-700 text-sm font-bold mb-2">ID del Producto:</label>
                    <input type="number" id="productIdToAddToCart" class="input-field" placeholder="ID del producto">
                </div>
                <div class="form-group">
                    <label for="quantityToAddToCart" class="block text-gray-700 text-sm font-bold mb-2">Cantidad:</label>
                    <input type="number" id="quantityToAddToCart" class="input-field" placeholder="Cantidad" min="1">
                </div>
                <button id="addToCartFromOrder" class="btn btn-primary w-full mt-4">Agregar al Carrito</button>
            </div>
            <div class="p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Resumen del Carrito</h3>
                <div id="orderCartSummary" class="mb-4">
                </div>
                <button id="confirmOrderBtn" class="btn btn-success w-full mt-4">Confirmar Pedido</button>
            </div>
            <button id="backToMainMenuFromCreateOrder" class="btn btn-secondary w-full">Volver al menú principal</button>
        </div>

        <div id="orderHistorySection" class="hidden section-panel">
            <h2 class="section-title text-purple-700">Consultar Historial de Pedidos</h2>
            <div id="ordersList" class="p-6 border border-gray-200 rounded-lg bg-white shadow-sm">
            </div>
            <button id="backToMainMenuFromOrderHistory" class="btn btn-secondary mt-6 w-full">Volver al menú principal</button>
        </div>

        <div id="adminSection" class="hidden section-panel">
            <h2 class="section-title text-teal-700">Administración</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <button id="showUserManagement" class="btn btn-primary">a) Gestión de Usuarios</button>
                <button id="showStockAdjustment" class="btn btn-primary">b) Ajuste de Stock Manual</button>
                <button id="backToMainMenuFromAdmin" class="btn btn-secondary col-span-full">c) Volver al menú principal</button>
            </div>

            <div id="userManagementPanel" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Gestión de Usuarios</h3>
                <div class="form-group">
                    <label for="newUserName" class="block text-gray-700 text-sm font-bold mb-2">Nombre:</label>
                    <input type="text" id="newUserName" class="input-field" placeholder="Nombre del usuario">
                </div>
                <div class="form-group">
                    <label for="newUserEmail" class="block text-gray-700 text-sm font-bold mb-2">Email:</label>
                    <input type="email" id="newUserEmail" class="input-field" placeholder="Email del usuario">
                </div>
                <button id="addNewUserBtn" class="btn btn-success w-full mb-4">Agregar Usuario</button>
                <h4 class="text-lg font-semibold mb-3 text-gray-700">Lista de Usuarios</h4>
                <div id="usersList" class="overflow-x-auto">
                </div>
            </div>

            <div id="stockAdjustmentPanel" class="hidden p-6 border border-gray-200 rounded-lg bg-white shadow-sm mb-4">
                <h3 class="text-xl font-semibold mb-4 text-gray-800">Ajuste de Stock Manual</h3>
                <div id="stockAdjustmentList" class="overflow-x-auto">
                </div>
            </div>
        </div>

        <div id="messageBox" class="hidden fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 modal-overlay">
            <div class="modal-content p-8 max-w-sm w-full text-center">
                <p id="messageText" class="text-lg font-semibold mb-6"></p>
                <button id="closeMessageBox" class="btn btn-primary w-full">Aceptar</button>
            </div>
        </div>

        <div id="confirmationModal" class="hidden fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 modal-overlay">
            <div class="modal-content p-8 max-w-sm w-full text-center">
                <p id="confirmationMessage" class="text-lg font-semibold mb-6"></p>
                <div class="flex justify-center space-x-4">
                    <button id="confirmActionBtn" class="btn btn-danger">Confirmar</button>
                    <button id="cancelActionBtn" class="btn btn-secondary">Cancelar</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        let products = [
            { id: 1, nombre: 'Laptop Gamer', descripcion: 'Potente laptop para juegos', precio: 1200.00, categoria: 'Electrónica', imagen: 'https://placehold.co/100x100/ADD8E6/000000?text=Laptop', stock: 10 },
            { id: 2, nombre: 'Teclado Mecánico', descripcion: 'Teclado RGB con switches azules', precio: 75.50, categoria: 'Accesorios', imagen: 'https://placehold.co/100x100/ADD8E6/000000?text=Teclado', stock: 25 },
            { id: 3, nombre: 'Monitor Curvo 27"', descripcion: 'Monitor de alta resolución para gaming', precio: 300.00, categoria: 'Electrónica', imagen: 'https://placehold.co/100x100/ADD8E6/000000?text=Monitor', stock: 15 },
            { id: 4, nombre: 'Mouse Inalámbrico', descripcion: 'Mouse ergonómico con batería de larga duración', precio: 30.00, categoria: 'Accesorios', imagen: 'https://placehold.co/100x100/ADD8E6/000000?text=Mouse', stock: 50 },
            { id: 5, nombre: 'Auriculares Gaming', descripcion: 'Auriculares con sonido envolvente 7.1', precio: 90.00, categoria: 'Audio', imagen: 'https://placehold.co/100x100/ADD8E6/000000?text=Auriculares', stock: 20 }
        ];

        let users = [
            { id: 1, nombre: 'Admin User', email: 'admin@techlab.com', rol: 'admin' },
            { id: 2, nombre: 'Client User', email: 'client@techlab.com', rol: 'cliente' }
        ];

        let nextProductId = products.length > 0 ? Math.max(...products.map(p => p.id)) + 1 : 1;
        let nextUserId = users.length > 0 ? Math.max(...users.map(u => u.id)) + 1 : 1;
        let orders = [];
        let nextOrderId = 1;
        let cart = [];
        const MIN_STOCK_THRESHOLD = 5;

        class Product {
            constructor(id, nombre, descripcion, precio, categoria, imagen, stock) {
                this.id = id;
                this.nombre = nombre;
                this.descripcion = descripcion;
                this.precio = precio;
                this.categoria = categoria;
                this.imagen = imagen;
                this.stock = stock;
            }
        }

        class OrderLine {
            constructor(productId, quantity, priceAtTimeOfOrder, productName) {
                this.productId = productId;
                this.quantity = quantity;
                this.priceAtTimeOfOrder = priceAtTimeOfOrder;
                this.productName = productName;
            }
        }

        class Order {
            constructor(id, userId, orderLines, totalCost, status = 'pendiente') {
                this.id = id;
                this.userId = userId;
                this.orderLines = orderLines;
                this.totalCost = totalCost;
                this.status = status;
                this.orderDate = new Date().toLocaleString();
            }
        }

        class User {
            constructor(id, nombre, email, rol = 'cliente') {
                this.id = id;
                this.nombre = nombre;
                this.email = email;
                this.rol = rol;
            }
        }

        const ProductService = {
            getAllProducts: () => {
                return [...products];
            },

            getProductById: (id) => {
                return products.find(p => p.id === id);
            },

            getProductByName: (name) => {
                const lowerCaseName = name.toLowerCase();
                return products.filter(p => p.nombre.toLowerCase().includes(lowerCaseName));
            },

            addProduct: (productData) => {
                try {
                    if (!productData.nombre || !productData.precio || !productData.stock) {
                        throw new Error('Nombre, precio y stock son campos obligatorios.');
                    }
                    if (isNaN(productData.precio) || parseFloat(productData.precio) <= 0) {
                        throw new Error('El precio debe ser un número positivo.');
                    }
                    if (isNaN(productData.stock) || parseInt(productData.stock) < 0) {
                        throw new Error('El stock no puede ser negativo.');
                    }

                    const newProduct = new Product(
                        nextProductId++,
                        productData.nombre,
                        productData.descripcion || '',
                        parseFloat(productData.precio),
                        productData.categoria || 'General',
                        productData.imagen || 'https://placehold.co/100x100/ADD8E6/000000?text=Producto',
                        parseInt(productData.stock)
                    );
                    products.push(newProduct);
                    showMessage(`Producto "${newProduct.nombre}" agregado con ID: ${newProduct.id}.`);
                    checkLowStockAlert(newProduct);
                    return newProduct;
                } catch (error) {
                    showMessage(`Error al agregar producto: ${error.message}`, 'error');
                    return null;
                }
            },

            updateProduct: (id, updateData) => {
                try {
                    const productIndex = products.findIndex(p => p.id === id);
                    if (productIndex === -1) {
                        throw new Error(`Producto con ID ${id} no encontrado.`);
                    }

                    const productToUpdate = products[productIndex];
                    if (updateData.precio !== undefined) {
                        const newPrice = parseFloat(updateData.precio);
                        if (isNaN(newPrice) || newPrice <= 0) {
                            throw new Error('El precio debe ser un número positivo.');
                        }
                        productToUpdate.precio = newPrice;
                    }
                    if (updateData.stock !== undefined) {
                        const newStock = parseInt(updateData.stock);
                        if (isNaN(newStock) || newStock < 0) {
                            throw new Error('El stock no puede ser negativo.');
                        }
                        productToUpdate.stock = newStock;
                    }

                    products[productIndex] = productToUpdate;
                    showMessage(`Producto con ID ${id} actualizado correctamente.`);
                    checkLowStockAlert(productToUpdate);
                    return productToUpdate;
                } catch (error) {
                    showMessage(`Error al actualizar producto: ${error.message}`, 'error');
                    return null;
                }
            }
        };

        const OrderService = {
            createOrder: (cartItems, userId = 'user123') => {
                try {
                    if (cartItems.length === 0) {
                        throw new Error('El carrito está vacío. Agregue productos para crear un pedido.');
                    }

                    let totalCost = 0;
                    const orderLines = [];
                    const productsToUpdateStock = [];

                    for (const item of cartItems) {
                        const product = ProductService.getProductById(item.productId);
                        if (!product) {
                            throw new Error(`Producto con ID ${item.productId} no encontrado.`);
                        }
                        if (product.stock < item.quantity) {
                            throw new Error(`Stock insuficiente para el producto "${product.nombre}". Stock disponible: ${product.stock}, solicitado: ${item.quantity}.`);
                        }
                        productsToUpdateStock.push({ product, quantity: item.quantity });
                        totalCost += product.precio * item.quantity;
                        orderLines.push(new OrderLine(item.productId, item.quantity, product.precio, product.nombre));
                    }

                    for (const { product, quantity } of productsToUpdateStock) {
                        product.stock -= quantity;
                        checkLowStockAlert(product);
                    }

                    const newOrder = new Order(nextOrderId++, userId, orderLines, totalCost);
                    orders.push(newOrder);
                    cart = [];
                    showMessage(`Pedido #${newOrder.id} creado con éxito. Costo total: $${newOrder.totalCost.toFixed(2)}`);
                    return newOrder;
                } catch (error) {
                    showMessage(`Error al crear pedido: ${error.message}`, 'error');
                    return null;
                }
            },

            getOrdersByUserId: (userId) => {
                return orders.filter(o => o.userId === userId);
            },

            updateOrderStatus: (orderId, newStatus) => {
                const orderIndex = orders.findIndex(o => o.id === orderId);
                if (orderIndex === -1) {
                    showMessage(`Pedido con ID ${orderId} no encontrado.`, 'error');
                    return false;
                }
                orders[orderIndex].status = newStatus;
                showMessage(`Estado del Pedido #${orderId} actualizado a "${newStatus.toUpperCase()}".`);
                return true;
            }
        };

        const UserService = {
            getAllUsers: () => {
                return [...users];
            },
            addUser: (userData) => {
                try {
                    if (!userData.nombre || !userData.email) {
                        throw new Error('Nombre y email son campos obligatorios.');
                    }
                    if (users.some(u => u.email === userData.email)) {
                        throw new Error('Ya existe un usuario con este email.');
                    }
                    const newUser = new User(nextUserId++, userData.nombre, userData.email, userData.rol || 'cliente');
                    users.push(newUser);
                    showMessage(`Usuario "${newUser.nombre}" agregado con ID: ${newUser.id}.`);
                    return newUser;
                } catch (error) {
                    showMessage(`Error al agregar usuario: ${error.message}`, 'error');
                    return null;
                }
            },
            deleteUser: (id) => {
                try {
                    const initialLength = users.length;
                    users = users.filter(u => u.id !== id);
                    if (users.length === initialLength) {
                        throw new Error(`Usuario con ID ${id} no encontrado.`);
                    }
                    showMessage(`Usuario con ID ${id} eliminado correctamente.`);
                    return true;
                } catch (error) {
                    showMessage(`Error al eliminar usuario: ${error.message}`, 'error');
                    return false;
                }
            }
        };

        const mainMenu = document.getElementById('mainMenu');
        const productManagementSection = document.getElementById('productManagementSection');
        const addProductForm = document.getElementById('addProductForm');
        const listProductsDisplay = document.getElementById('listProductsDisplay');
        const searchProductForm = document.getElementById('searchProductForm');
        const updateProductForm = document.getElementById('updateProductForm');
        const deleteProductForm = document.getElementById('deleteProductForm');
        const cartSection = document.getElementById('cartSection');
        const createOrderSection = document.getElementById('createOrderSection');
        const orderHistorySection = document.getElementById('orderHistorySection');
        const lowStockDisplay = document.getElementById('lowStockDisplay');
        const adminSection = document.getElementById('adminSection');
        const userManagementPanel = document.getElementById('userManagementPanel');
        const stockAdjustmentPanel = document.getElementById('stockAdjustmentPanel');

        const productsListDiv = document.getElementById('productsList');
        const cartContentDiv = document.getElementById('cartContent');
        const orderCartSummaryDiv = document.getElementById('orderCartSummary');
        const ordersListDiv = document.getElementById('ordersList');
        const lowStockListDiv = document.getElementById('lowStockList');
        const usersListDiv = document.getElementById('usersList');
        const stockAdjustmentListDiv = document.getElementById('stockAdjustmentList');

        const messageBox = document.getElementById('messageBox');
        const messageText = document.getElementById('messageText');
        const closeMessageBoxBtn = document.getElementById('closeMessageBox');

        const confirmationModal = document.getElementById('confirmationModal');
        const confirmationMessage = document.getElementById('confirmationMessage');
        const confirmActionBtn = document.getElementById('confirmActionBtn');
        const cancelActionBtn = document.getElementById('cancelActionBtn');

        let pendingConfirmationAction = null;

        function showMessage(message, type = 'info') {
            messageText.textContent = message;
            messageBox.classList.remove('hidden');
            if (type === 'error') {
                messageText.classList.add('text-red-600');
            } else {
                messageText.classList.remove('text-red-600');
            }
        }

        closeMessageBoxBtn.addEventListener('click', () => {
            messageBox.classList.add('hidden');
        });

        function showConfirmationModal(message, onConfirm) {
            confirmationMessage.textContent = message;
            confirmationModal.classList.remove('hidden');
            pendingConfirmationAction = onConfirm;
        }

        function hideConfirmationModal() {
            confirmationModal.classList.add('hidden');
            pendingConfirmationAction = null;
        }

        confirmActionBtn.addEventListener('click', () => {
            if (pendingConfirmationAction) {
                pendingConfirmationAction();
            }
            hideConfirmationModal();
        });

        cancelActionBtn.addEventListener('click', () => {
            hideConfirmationModal();
        });

        function showMainMenu() {
            [productManagementSection, cartSection, createOrderSection, orderHistorySection, lowStockDisplay, adminSection,
             addProductForm, listProductsDisplay, searchProductForm, updateProductForm, deleteProductForm,
             userManagementPanel, stockAdjustmentPanel].forEach(el => el.classList.add('hidden'));
            mainMenu.classList.remove('hidden');
        }

        function hideProductSubsections() {
            [addProductForm, listProductsDisplay, searchProductForm, updateProductForm, deleteProductForm, lowStockDisplay].forEach(el => el.classList.add('hidden'));
        }

        function hideAdminSubsections() {
            [userManagementPanel, stockAdjustmentPanel].forEach(el => el.classList.add('hidden'));
        }

        document.getElementById('showProductManagement').addEventListener('click', () => {
            mainMenu.classList.add('hidden');
            productManagementSection.classList.remove('hidden');
            hideProductSubsections();
        });

        document.getElementById('showCart').addEventListener('click', () => {
            mainMenu.classList.add('hidden');
            cartSection.classList.remove('hidden');
            renderCart();
        });

        document.getElementById('showCreateOrder').addEventListener('click', () => {
            mainMenu.classList.add('hidden');
            createOrderSection.classList.remove('hidden');
            renderOrderCartSummary();
        });

        document.getElementById('showOrderHistory').addEventListener('click', () => {
            mainMenu.classList.add('hidden');
            orderHistorySection.classList.remove('hidden');
            renderOrderHistory();
        });

        document.getElementById('showAdmin').addEventListener('click', () => {
            mainMenu.classList.add('hidden');
            adminSection.classList.remove('hidden');
            hideAdminSubsections();
        });

        document.getElementById('exitApp').addEventListener('click', () => {
            showMessage('Gracias por usar el Sistema de Gestión TechLab. ¡Hasta pronto!');
        });

        document.getElementById('backToMainMenuFromProducts').addEventListener('click', showMainMenu);
        document.getElementById('backToMainMenuFromCart').addEventListener('click', showMainMenu);
        document.getElementById('backToMainMenuFromCreateOrder').addEventListener('click', showMainMenu);
        document.getElementById('backToMainMenuFromOrderHistory').addEventListener('click', showMainMenu);
        document.getElementById('backToMainMenuFromAdmin').addEventListener('click', showMainMenu);

        document.getElementById('addProductBtn').addEventListener('click', () => {
            hideProductSubsections();
            addProductForm.classList.remove('hidden');
            document.getElementById('newProductName').value = '';
            document.getElementById('newProductDescription').value = '';
            document.getElementById('newProductPrice').value = '';
            document.getElementById('newProductCategory').value = '';
            document.getElementById('newProductImage').value = '';
            document.getElementById('newProductStock').value = '';
        });

        document.getElementById('saveNewProduct').addEventListener('click', () => {
            const productData = {
                nombre: document.getElementById('newProductName').value,
                descripcion: document.getElementById('newProductDescription').value,
                precio: document.getElementById('newProductPrice').value,
                categoria: document.getElementById('newProductCategory').value,
                imagen: document.getElementById('newProductImage').value,
                stock: document.getElementById('newProductStock').value
            };
            ProductService.addProduct(productData);
        });

        document.getElementById('listProductsBtn').addEventListener('click', () => {
            hideProductSubsections();
            listProductsDisplay.classList.remove('hidden');
            renderProductsList();
        });

        function renderProductsList() {
            const products = ProductService.getAllProducts();
            if (products.length === 0) {
                productsListDiv.innerHTML = '<p class="text-gray-600">No hay productos disponibles.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-3 px-4 text-left">ID</th>
                            <th class="py-3 px-4 text-left">Imagen</th>
                            <th class="py-3 px-4 text-left">Nombre</th>
                            <th class="py-3 px-4 text-left">Descripción</th>
                            <th class="py-3 px-4 text-left">Precio</th>
                            <th class="py-3 px-4 text-left">Categoría</th>
                            <th class="py-3 px-4 text-left">Stock</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            products.forEach(p => {
                html += `
                    <tr class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="py-3 px-4">${p.id}</td>
                        <td class="py-3 px-4"><img src="${p.imagen}" alt="${p.nombre}" class="w-12 h-12 object-cover rounded-md"></td>
                        <td class="py-3 px-4">${p.nombre}</td>
                        <td class="py-3 px-4">${p.descripcion}</td>
                        <td class="py-3 px-4">$${p.precio.toFixed(2)}</td>
                        <td class="py-3 px-4">${p.categoria}</td>
                        <td class="py-3 px-4">${p.stock}</td>
                    </tr>
                `;
            });
            html += `
                    </tbody>
                </table>
            `;
            productsListDiv.innerHTML = html;
        }

        document.getElementById('searchProductBtn').addEventListener('click', () => {
            hideProductSubsections();
            searchProductForm.classList.remove('hidden');
            document.getElementById('searchProductId').value = '';
            document.getElementById('searchProductName').value = '';
            document.getElementById('searchProductResult').innerHTML = '';
        });

        document.getElementById('executeSearchProduct').addEventListener('click', () => {
            const id = parseInt(document.getElementById('searchProductId').value);
            const name = document.getElementById('searchProductName').value.trim();
            let foundProducts = [];

            if (!isNaN(id)) {
                const productById = ProductService.getProductById(id);
                if (productById) {
                    foundProducts.push(productById);
                }
            } else if (name !== '') {
                foundProducts = ProductService.getProductByName(name);
            }

            const resultDiv = document.getElementById('searchProductResult');
            if (foundProducts.length > 0) {
                let html = '<h4 class="font-semibold text-lg mb-2">Producto(s) Encontrado(s):</h4>';
                foundProducts.forEach(product => {
                    html += `
                        <div class="mb-4 p-3 bg-gray-100 rounded-md border border-gray-200">
                            <p><strong>ID:</strong> ${product.id}</p>
                            <p><strong>Nombre:</strong> ${product.nombre}</p>
                            <p><strong>Descripción:</strong> ${product.descripcion}</p>
                            <p><strong>Precio:</strong> $${product.precio.toFixed(2)}</p>
                            <p><strong>Categoría:</strong> ${product.categoria}</p>
                            <p><strong>Stock:</strong> ${product.stock}</p>
                            <img src="${product.imagen}" alt="${product.nombre}" class="w-24 h-24 object-cover rounded-md mt-2">
                        </div>
                    `;
                });
                resultDiv.innerHTML = html;
            } else {
                resultDiv.innerHTML = '<p class="text-red-500">Producto no encontrado.</p>';
            }
        });

        document.getElementById('updateProductBtn').addEventListener('click', () => {
            hideProductSubsections();
            updateProductForm.classList.remove('hidden');
            document.getElementById('updateProductId').value = '';
            document.getElementById('updateProductPrice').value = '';
            document.getElementById('updateProductStock').value = '';
        });

        document.getElementById('executeUpdateProduct').addEventListener('click', () => {
            const id = parseInt(document.getElementById('updateProductId').value);
            const price = document.getElementById('updateProductPrice').value;
            const stock = document.getElementById('updateProductStock').value;

            if (isNaN(id)) {
                showMessage('Por favor, ingrese un ID de producto válido.', 'error');
                return;
            }

            const updateData = {};
            if (price !== '') {
                updateData.precio = price;
            }
            if (stock !== '') {
                updateData.stock = stock;
            }

            if (Object.keys(updateData).length === 0) {
                showMessage('Ingrese al menos un valor (precio o stock) para actualizar.', 'error');
                return;
            }

            ProductService.updateProduct(id, updateData);
        });

        document.getElementById('deleteProductBtn').addEventListener('click', () => {
            hideProductSubsections();
            deleteProductForm.classList.remove('hidden');
            document.getElementById('deleteProductId').value = '';
        });

        document.getElementById('executeDeleteProduct').addEventListener('click', () => {
            const idToDelete = parseInt(document.getElementById('deleteProductId').value);
            if (isNaN(idToDelete)) {
                showMessage('Por favor, ingrese un ID de producto válido.', 'error');
                return;
            }
            showConfirmationModal(`¿Está seguro de que desea eliminar el producto con ID ${idToDelete}?`, () => {
                ProductService.deleteProduct(idToDelete);
            });
        });

        document.getElementById('checkLowStockBtn').addEventListener('click', () => {
            hideProductSubsections();
            lowStockDisplay.classList.remove('hidden');
            renderLowStockProducts();
        });

        function checkLowStockAlert(product) {
            if (product.stock <= MIN_STOCK_THRESHOLD) {
                showMessage(`¡Alerta de Stock Bajo! El producto "${product.nombre}" (ID: ${product.id}) tiene solo ${product.stock} unidades.`, 'error');
            }
        }

        function renderLowStockProducts() {
            const lowStockProducts = products.filter(p => p.stock <= MIN_STOCK_THRESHOLD);
            if (lowStockProducts.length === 0) {
                lowStockListDiv.innerHTML = '<p class="text-gray-600">No hay productos con stock bajo en este momento.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-3 px-4 text-left">ID</th>
                            <th class="py-3 px-4 text-left">Nombre</th>
                            <th class="py-3 px-4 text-left">Stock Actual</th>
                            <th class="py-3 px-4 text-left">Categoría</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            lowStockProducts.forEach(p => {
                html += `
                    <tr class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="py-3 px-4">${p.id}</td>
                        <td class="py-3 px-4">${p.nombre}</td>
                        <td class="py-3 px-4 text-red-600 font-bold">${p.stock}</td>
                        <td class="py-3 px-4">${p.categoria}</td>
                    </tr>
                `;
            });
            html += `
                    </tbody>
                </table>
            `;
            lowStockListDiv.innerHTML = html;
        }

        function renderCart() {
            if (cart.length === 0) {
                cartContentDiv.innerHTML = '<p class="text-gray-600">El carrito está vacío.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-3 px-4 text-left">ID Producto</th>
                            <th class="py-3 px-4 text-left">Nombre</th>
                            <th class="py-3 px-4 text-left">Cantidad</th>
                            <th class="py-3 px-4 text-left">Precio Unitario</th>
                            <th class="py-3 px-4 text-left">Subtotal</th>
                            <th class="py-3 px-4 text-left">Acciones</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            let totalCartCost = 0;
            cart.forEach((item, index) => {
                const product = ProductService.getProductById(item.productId);
                const productName = product ? product.nombre : 'Producto Desconocido';
                const unitPrice = product ? product.precio : 0;
                const subtotal = unitPrice * item.quantity;
                totalCartCost += subtotal;
                html += `
                    <tr class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="py-3 px-4">${item.productId}</td>
                        <td class="py-3 px-4">${productName}</td>
                        <td class="py-3 px-4">${item.quantity}</td>
                        <td class="py-3 px-4">$${unitPrice.toFixed(2)}</td>
                        <td class="py-3 px-4">$${subtotal.toFixed(2)}</td>
                        <td class="py-3 px-4">
                            <button onclick="removeFromCart(${index})" class="btn btn-danger text-xs px-2 py-1">Eliminar</button>
                        </td>
                    </tr>
                `;
            });
            html += `
                    </tbody>
                    <tfoot class="bg-gray-100 text-gray-900 font-bold">
                        <tr>
                            <td colspan="4" class="py-3 px-4 text-right">Total:</td>
                            <td class="py-3 px-4">$${totalCartCost.toFixed(2)}</td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>
            `;
            cartContentDiv.innerHTML = html;
        }

        function addToCart(productId, quantity) {
            try {
                const product = ProductService.getProductById(productId);
                if (!product) {
                    throw new Error('Producto no encontrado.');
                }
                if (quantity <= 0 || isNaN(quantity)) {
                    throw new Error('La cantidad debe ser un número positivo.');
                }
                if (product.stock < quantity) {
                    throw new Error(`Stock insuficiente para "${product.nombre}". Disponible: ${product.stock}, solicitado: ${quantity}.`);
                }

                const existingItemIndex = cart.findIndex(item => item.productId === productId);
                if (existingItemIndex !== -1) {
                    cart[existingItemIndex].quantity += quantity;
                } else {
                    cart.push({ productId, quantity });
                }
                showMessage(`"${product.nombre}" (x${quantity}) agregado al carrito.`);
                renderOrderCartSummary();
            } catch (error) {
                showMessage(`Error al agregar al carrito: ${error.message}`, 'error');
            }
        }

        function removeFromCart(index) {
            if (index >= 0 && index < cart.length) {
                const removedItem = cart.splice(index, 1);
                const product = ProductService.getProductById(removedItem[0].productId);
                showMessage(`"${product ? product.nombre : 'Producto'}" eliminado del carrito.`);
                renderCart();
                renderOrderCartSummary();
            }
        }

        document.getElementById('addToCartFromOrder').addEventListener('click', () => {
            const productId = parseInt(document.getElementById('productIdToAddToCart').value);
            const quantity = parseInt(document.getElementById('quantityToAddToCart').value);
            addToCart(productId, quantity);
            document.getElementById('productIdToAddToCart').value = '';
            document.getElementById('quantityToAddToCart').value = '';
        });

        document.getElementById('confirmOrderBtn').addEventListener('click', () => {
            OrderService.createOrder(cart);
            renderOrderCartSummary();
        });

        function renderOrderCartSummary() {
            if (cart.length === 0) {
                orderCartSummaryDiv.innerHTML = '<p class="text-gray-600">El carrito para el pedido está vacío. Agregue productos.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-2 px-3 text-left">Producto</th>
                            <th class="py-2 px-3 text-left">Cantidad</th>
                            <th class="py-2 px-3 text-left">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            let totalCost = 0;
            cart.forEach(item => {
                const product = ProductService.getProductById(item.productId);
                if (product) {
                    const subtotal = product.precio * item.quantity;
                    totalCost += subtotal;
                    html += `
                        <tr class="border-b border-gray-200">
                            <td class="py-2 px-3">${product.nombre}</td>
                            <td class="py-2 px-3">${item.quantity}</td>
                            <td class="py-2 px-3">$${subtotal.toFixed(2)}</td>
                        </tr>
                    `;
                }
            });
            html += `
                    </tbody>
                    <tfoot class="bg-gray-100 text-gray-900 font-bold">
                        <tr>
                            <td colspan="2" class="py-2 px-3 text-right">Total del Pedido:</td>
                            <td class="py-2 px-3">$${totalCost.toFixed(2)}</td>
                        </tr>
                    </tfoot>
                </table>
            `;
            orderCartSummaryDiv.innerHTML = html;
        }

        function renderOrderHistory() {
            const userOrders = OrderService.getOrdersByUserId('user123');
            if (userOrders.length === 0) {
                ordersListDiv.innerHTML = '<p class="text-gray-600">No hay pedidos en el historial.</p>';
                return;
            }

            let html = '';
            userOrders.forEach(order => {
                html += `
                    <div class="mb-4 p-4 border border-gray-200 rounded-lg bg-white shadow-sm">
                        <h4 class="font-semibold text-lg text-indigo-800 mb-2">Pedido #${order.id}</h4>
                        <p><strong>Fecha:</strong> ${order.orderDate}</p>
                        <p><strong>Estado:</strong> <span class="font-medium text-green-700">${order.status.toUpperCase()}</span></p>
                        <p><strong>Costo Total:</strong> $${order.totalCost.toFixed(2)}</p>
                        <p class="font-semibold mt-2">Productos:</p>
                        <ul class="list-disc list-inside ml-4">
                `;
                order.orderLines.forEach(line => {
                    html += `<li>${line.productName} (x${line.quantity}) - $${line.priceAtTimeOfOrder.toFixed(2)} c/u</li>`;
                });
                html += `
                        </ul>
                        <div class="mt-3 flex items-center space-x-2">
                            <label for="status-${order.id}" class="text-sm font-medium text-gray-700">Cambiar estado:</label>
                            <select id="status-${order.id}" class="input-field w-auto p-2 text-sm" onchange="OrderService.updateOrderStatus(${order.id}, this.value); renderOrderHistory();">
                                <option value="pendiente" ${order.status === 'pendiente' ? 'selected' : ''}>Pendiente</option>
                                <option value="confirmado" ${order.status === 'confirmado' ? 'selected' : ''}>Confirmado</option>
                                <option value="enviado" ${order.status === 'enviado' ? 'selected' : ''}>Enviado</option>
                                <option value="entregado" ${order.status === 'entregado' ? 'selected' : ''}>Entregado</option>
                                <option value="cancelado" ${order.status === 'cancelado' ? 'selected' : ''}>Cancelado</option>
                            </select>
                        </div>
                    </div>
                `;
            });
            ordersListDiv.innerHTML = html;
        }

        document.getElementById('showUserManagement').addEventListener('click', () => {
            hideAdminSubsections();
            userManagementPanel.classList.remove('hidden');
            renderUsersList();
        });

        document.getElementById('addNewUserBtn').addEventListener('click', () => {
            const userName = document.getElementById('newUserName').value.trim();
            const userEmail = document.getElementById('newUserEmail').value.trim();
            UserService.addUser({ nombre: userName, email: userEmail });
            document.getElementById('newUserName').value = '';
            document.getElementById('newUserEmail').value = '';
            renderUsersList();
        });

        function renderUsersList() {
            const users = UserService.getAllUsers();
            if (users.length === 0) {
                usersListDiv.innerHTML = '<p class="text-gray-600">No hay usuarios registrados.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-3 px-4 text-left">ID</th>
                            <th class="py-3 px-4 text-left">Nombre</th>
                            <th class="py-3 px-4 text-left">Email</th>
                            <th class="py-3 px-4 text-left">Rol</th>
                            <th class="py-3 px-4 text-left">Acciones</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            users.forEach(u => {
                html += `
                    <tr class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="py-3 px-4">${u.id}</td>
                        <td class="py-3 px-4">${u.nombre}</td>
                        <td class="py-3 px-4">${u.email}</td>
                        <td class="py-3 px-4">${u.rol}</td>
                        <td class="py-3 px-4">
                            <button onclick="deleteUser(${u.id})" class="btn btn-danger text-xs px-2 py-1">Eliminar</button>
                        </td>
                    </tr>
                `;
            });
            html += `
                    </tbody>
                </table>
            `;
            usersListDiv.innerHTML = html;
        }

        function deleteUser(id) {
            showConfirmationModal(`¿Está seguro de que desea eliminar el usuario con ID ${id}?`, () => {
                UserService.deleteUser(id);
                renderUsersList();
            });
        }

        document.getElementById('showStockAdjustment').addEventListener('click', () => {
            hideAdminSubsections();
            stockAdjustmentPanel.classList.remove('hidden');
            renderStockAdjustmentList();
        });

        function renderStockAdjustmentList() {
            const allProducts = ProductService.getAllProducts();
            if (allProducts.length === 0) {
                stockAdjustmentListDiv.innerHTML = '<p class="text-gray-600">No hay productos para ajustar stock.</p>';
                return;
            }

            let html = `
                <table class="min-w-full bg-white rounded-lg overflow-hidden shadow-md">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-3 px-4 text-left">ID</th>
                            <th class="py-3 px-4 text-left">Nombre</th>
                            <th class="py-3 px-4 text-left">Stock Actual</th>
                            <th class="py-3 px-4 text-left">Ajustar Cantidad</th>
                            <th class="py-3 px-4 text-left">Acciones</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
            `;
            allProducts.forEach(p => {
                html += `
                    <tr class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="py-3 px-4">${p.id}</td>
                        <td class="py-3 px-4">${p.nombre}</td>
                        <td class="py-3 px-4">${p.stock}</td>
                        <td class="py-3 px-4">
                            <input type="number" id="adjustStock-${p.id}" class="input-field w-24 p-2 text-sm" placeholder="+/-">
                        </td>
                        <td class="py-3 px-4">
                            <button onclick="adjustProductStock(${p.id})" class="btn btn-primary text-xs px-2 py-1">Ajustar</button>
                        </td>
                    </tr>
                `;
            });
            html += `
                    </tbody>
                </table>
            `;
            stockAdjustmentListDiv.innerHTML = html;
        }

        function adjustProductStock(productId) {
            const inputElement = document.getElementById(`adjustStock-${productId}`);
            const adjustment = parseInt(inputElement.value);

            if (isNaN(adjustment) || adjustment === 0) {
                showMessage('Ingrese una cantidad válida para ajustar el stock (positiva o negativa).', 'error');
                return;
            }

            const product = ProductService.getProductById(productId);
            if (!product) {
                showMessage('Producto no encontrado para ajuste de stock.', 'error');
                return;
            }

            const newStock = product.stock + adjustment;
            if (newStock < 0) {
                showMessage('El stock no puede ser negativo.', 'error');
                return;
            }

            ProductService.updateProduct(productId, { stock: newStock });
            renderStockAdjustmentList();
            inputElement.value = '';
        }

        showMainMenu();
    </script>
</body>
</html>
