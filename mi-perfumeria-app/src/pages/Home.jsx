import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getAllProducts, searchByName, searchByBrand, searchByType } from '../services/productService';
import ProductCard from '../components/ProductCard';
import './Home.css';

function Home() {
    const [products, setProducts] = useState([]);
    const [allProducts, setAllProducts] = useState([]); // Para extraer marcas y tipos
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedBrand, setSelectedBrand] = useState('');
    const [selectedType, setSelectedType] = useState('');
    const [page, setPage] = useState(0);

    // Estados para marcas y tipos dinámicos
    const [brands, setBrands] = useState([]);
    const [types, setTypes] = useState([]);

    // Cargar todos los productos al inicio para extraer marcas y tipos
    useEffect(() => {
        loadAllProductsForFilters();
    }, []);

    // Cargar productos según filtros
    useEffect(() => {
        loadProducts();
    }, [page, selectedBrand, selectedType]);

    const loadAllProductsForFilters = async () => {
        try {
            // Cargar todos los productos (máximo 50)
            const data = await getAllProducts(0, 50);
            const productArray = Array.isArray(data) ? data : [];
            setAllProducts(productArray);

            // Extraer marcas únicas
            const uniqueBrands = [...new Set(productArray.map(p => p.marca))].sort();
            setBrands(uniqueBrands);

            // Extraer tipos únicos
            const uniqueTypes = [...new Set(productArray.map(p => p.tipo))].sort();
            setTypes(uniqueTypes);

        } catch (err) {
            console.error('Error al cargar filtros:', err);
        }
    };

    const loadProducts = async () => {
        try {
            setLoading(true);
            let data;

            // Prioridad: Primero marca, luego tipo, luego búsqueda por nombre, finalmente todos
            if (selectedBrand) {
                data = await searchByBrand(selectedBrand, page, 12);
            } else if (selectedType) {
                data = await searchByType(selectedType, page, 12);
            } else if (searchTerm) {
                data = await searchByName(searchTerm, page, 12);
            } else {
                data = await getAllProducts(page, 12);
            }

            setProducts(Array.isArray(data) ? data : []);
            setError('');
        } catch (err) {
            setError('Error al cargar productos');
            console.error(err);
            setProducts([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setPage(0);
        setSelectedBrand('');
        setSelectedType('');
        loadProducts();
    };

    const handleBrandChange = (brand) => {
        setSelectedBrand(brand);
        setSelectedType('');
        setSearchTerm('');
        setPage(0);
    };

    const handleTypeChange = (type) => {
        setSelectedType(type);
        setSelectedBrand('');
        setSearchTerm('');
        setPage(0);
    };

    const clearFilters = () => {
        setSearchTerm('');
        setSelectedBrand('');
        setSelectedType('');
        setPage(0);
    };

    if (loading && products.length === 0) {
        return <div className="loading">Cargando productos...</div>;
    }

    return (
        <div className="home-container">
            <div className="home-header">
                <h1>Catálogo de Perfumes</h1>

                {/* Buscador por nombre */}
                <form onSubmit={handleSearch} className="search-form">
                    <input
                        type="text"
                        placeholder="Buscar perfume por nombre..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="search-input"
                    />
                    <button type="submit" className="btn-search">
                        🔍 Buscar
                    </button>
                </form>

                {/* Filtros por marca y tipo */}
                <div className="filters-section">
                    <div className="filter-group">
                        <label>🏷️ Filtrar por Marca:</label>
                        <select
                            value={selectedBrand}
                            onChange={(e) => handleBrandChange(e.target.value)}
                            className="filter-select"
                        >
                            <option value="">Todas las marcas</option>
                            {brands.map((brand) => (
                                <option key={brand} value={brand}>{brand}</option>
                            ))}
                        </select>
                    </div>

                    <div className="filter-group">
                        <label>🌸 Filtrar por Tipo:</label>
                        <select
                            value={selectedType}
                            onChange={(e) => handleTypeChange(e.target.value)}
                            className="filter-select"
                        >
                            <option value="">Todos los tipos</option>
                            {types.map((type) => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                        </select>
                    </div>

                    {(searchTerm || selectedBrand || selectedType) && (
                        <button onClick={clearFilters} className="btn-clear-filters">
                            ✖️ Limpiar Filtros
                        </button>
                    )}
                </div>

                {/* Indicador de filtros activos */}
                {(searchTerm || selectedBrand || selectedType) && (
                    <div className="active-filters">
                        <span>Filtros activos:</span>
                        {searchTerm && <span className="filter-badge">Búsqueda: "{searchTerm}"</span>}
                        {selectedBrand && <span className="filter-badge">Marca: {selectedBrand}</span>}
                        {selectedType && <span className="filter-badge">Tipo: {selectedType}</span>}
                    </div>
                )}
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="products-grid">
                {products.length === 0 ? (
                    <p className="no-products">No se encontraron productos</p>
                ) : (
                    products.map((product) => (
                        <ProductCard key={product.id} product={product} />
                    ))
                )}
            </div>

            {/* Paginación */}
            <div className="pagination">
                <button
                    onClick={() => setPage(page - 1)}
                    disabled={page === 0}
                    className="btn-page"
                >
                    ← Anterior
                </button>
                <span className="page-number">Página {page + 1}</span>
                <button
                    onClick={() => setPage(page + 1)}
                    disabled={products.length < 12}
                    className="btn-page"
                >
                    Siguiente →
                </button>
            </div>
        </div>
    );
}

export default Home;
