import { useState, useEffect } from 'react';
import {
    getAllProducts,
    createProduct,
    updateProduct,
    deleteProduct,
    restockProduct
} from '../../services/productService';
import './AdminProducts.css';

function AdminProducts() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [showRestockModal, setShowRestockModal] = useState(false);
    const [restockData, setRestockData] = useState({ productoId: null, cantidad: 0 });

    const [formData, setFormData] = useState({
        id: null,
        nombre: '',
        marca: '',
        tipo: '',
        precio: 0,
        stock: 0,
        imagenUrl: ''
    });

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            setLoading(true);
            const data = await getAllProducts(0, 50); // ✅ Máximo 50

            if (Array.isArray(data)) {
                setProducts(data);
            } else {
                setProducts([]);
            }

            setError('');
        } catch (err) {
            setError('Error al cargar productos');
            setProducts([]);
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('¿Estás seguro de eliminar este producto?')) {
            return;
        }

        try {
            await deleteProduct(id);
            loadProducts();
        } catch (err) {
            setError('Error al eliminar producto');
            console.error(err);
        }
    };

    const handleEdit = (product) => {
        setEditingProduct(product);
        setFormData({
            id: product.id,
            nombre: product.nombre,
            marca: product.marca,
            tipo: product.tipo,
            precio: product.precio,
            stock: product.stock,
            imagenUrl: product.imagenUrl || ''
        });
        setShowForm(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            if (editingProduct) {
                await updateProduct(formData);
            } else {
                await createProduct(formData);
            }

            resetForm();
            loadProducts();
        } catch (err) {
            setError(editingProduct ? 'Error al actualizar producto' : 'Error al crear producto');
            console.error(err);
        }
    };

    const handleRestock = async () => {
        try {
            await restockProduct(restockData.productoId, restockData.cantidad);
            setShowRestockModal(false);
            setRestockData({ productoId: null, cantidad: 0 });
            loadProducts();
        } catch (err) {
            setError('Error al reponer stock');
            console.error(err);
        }
    };

    const openRestockModal = (productId) => {
        setRestockData({ productoId: productId, cantidad: 0 });
        setShowRestockModal(true);
    };

    const resetForm = () => {
        setFormData({
            id: null,
            nombre: '',
            marca: '',
            tipo: '',
            precio: 0,
            stock: 0,
            imagenUrl: ''
        });
        setEditingProduct(null);
        setShowForm(false);
    };

    if (loading) {
        return <div className="loading">Cargando productos...</div>;
    }

    // Asegurar que products sea siempre un array
    const productsList = Array.isArray(products) ? products : [];

    return (
        <div className="admin-products-container">
            <div className="admin-products-header">
                <h1>Gestión de Productos</h1>
                <button onClick={() => setShowForm(!showForm)} className="btn-new">
                    {showForm ? 'Cancelar' : '+ Nuevo Producto'}
                </button>
            </div>

            {error && <div className="error-message">{error}</div>}

            {showForm && (
                <div className="product-form-container">
                    <h2>{editingProduct ? 'Editar Producto' : 'Crear Producto'}</h2>
                    <form onSubmit={handleSubmit} className="product-form">
                        <div className="form-group">
                            <label>Nombre:</label>
                            <input
                                type="text"
                                value={formData.nombre}
                                onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Marca:</label>
                            <input
                                type="text"
                                value={formData.marca}
                                onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Tipo:</label>
                            <input
                                type="text"
                                value={formData.tipo}
                                onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Precio:</label>
                            <input
                                type="number"
                                step="0.01"
                                value={formData.precio}
                                onChange={(e) => setFormData({ ...formData, precio: parseFloat(e.target.value) || 0 })}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Stock:</label>
                            <input
                                type="number"
                                value={formData.stock}
                                onChange={(e) => setFormData({ ...formData, stock: parseInt(e.target.value) || 0 })}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>URL de Imagen:</label>
                            <input
                                type="url"
                                value={formData.imagenUrl}
                                onChange={(e) => setFormData({ ...formData, imagenUrl: e.target.value })}
                                placeholder="https://ejemplo.com/imagen.jpg"
                            />
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="btn-submit">
                                {editingProduct ? 'Actualizar' : 'Crear'}
                            </button>
                            <button type="button" onClick={resetForm} className="btn-cancel">
                                Cancelar
                            </button>
                        </div>
                    </form>
                </div>
            )}

            <div className="products-table-container">
                <table className="products-table">
                    <thead>
                    <tr>
                        <th>Imagen</th>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Marca</th>
                        <th>Tipo</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {productsList.length === 0 ? (
                        <tr>
                            <td colSpan="8" style={{ textAlign: 'center', padding: '2rem' }}>
                                No hay productos disponibles
                            </td>
                        </tr>
                    ) : (
                        productsList.map((product) => (
                            <tr key={product.id}>
                                <td>
                                    {product.imagenUrl ? (
                                        <img
                                            src={product.imagenUrl}
                                            alt={product.nombre}
                                            style={{ width: '50px', height: '50px', objectFit: 'cover', borderRadius: '5px' }}
                                        />
                                    ) : (
                                        <span style={{ fontSize: '2rem' }}>🌸</span>
                                    )}
                                </td>
                                <td>{product.id}</td>
                                <td>{product.nombre}</td>
                                <td>{product.marca}</td>
                                <td>{product.tipo}</td>
                                <td>${product.precio.toFixed(2)}</td>
                                <td>
                    <span className={product.stock > 10 ? 'stock-ok' : 'stock-low'}>
                      {product.stock}
                    </span>
                                </td>
                                <td>
                                    <button onClick={() => handleEdit(product)} className="btn-edit">
                                        ✏️
                                    </button>
                                    <button onClick={() => openRestockModal(product.id)} className="btn-restock">
                                        📦
                                    </button>
                                    <button onClick={() => handleDelete(product.id)} className="btn-delete">
                                        🗑️
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>

            {showRestockModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Reponer Stock</h2>
                        <div className="form-group">
                            <label>Cantidad a agregar:</label>
                            <input
                                type="number"
                                min="1"
                                value={restockData.cantidad}
                                onChange={(e) => setRestockData({ ...restockData, cantidad: parseInt(e.target.value) || 0 })}
                            />
                        </div>
                        <div className="modal-actions">
                            <button onClick={handleRestock} className="btn-submit">
                                Confirmar
                            </button>
                            <button onClick={() => setShowRestockModal(false)} className="btn-cancel">
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AdminProducts;
