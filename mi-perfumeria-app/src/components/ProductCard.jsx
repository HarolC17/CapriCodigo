import { Link } from 'react-router-dom';
import './ProductCard.css';

function ProductCard({ product }) {
    return (
        <div className="product-card">
            <div className={`product-image ${!product.imagenUrl ? 'no-image' : ''}`}>
                {product.imagenUrl ? (
                    <img
                        src={product.imagenUrl}
                        alt={product.nombre}
                    />
                ) : (
                    <span className="product-icon">🌸</span>
                )}
            </div>

            <div className="product-info">
                <h3 className="product-name">{product.nombre}</h3>
                <p className="product-brand">{product.marca}</p>
                <p className="product-type">{product.tipo}</p>

                <div className="product-footer">
                    <span className="product-price">${product.precio.toFixed(2)}</span>
                    <span className={`product-stock ${product.stock > 0 ? 'in-stock' : 'out-stock'}`}>
            {product.stock > 0 ? `Stock: ${product.stock}` : 'Sin stock'}
          </span>
                </div>

                <Link to={`/product/${product.id}`} className="btn-view-detail">
                    Ver Detalle
                </Link>
            </div>
        </div>
    );
}

export default ProductCard;
