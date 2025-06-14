import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function Products() {

    const [apiOnline, setApiOnline] = useState(true);

    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultProducts = await axios.get("http://localhost:8080/api/products/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });

                setProducts(resultProducts.data);
            } catch (error) {
                setApiOnline(false);
                console.error("Error: ", error);
            }
        };

        fetchData();
    }, []);

    const filteredProducts = products;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentProducts = filteredProducts.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredProducts?.length || 0) / itemsPerPage);

    function renderProducts() {
        try {
            return currentProducts.map((product, index) => (

                <tr key={index}>
                    <th scope='row'>{product.id}</th>
                    <td>{product.name}</td>
                    <td>{product.category.description}</td>
                    <td>{product.weight}</td>
                    <td>{product.sku}</td>
                    <td className='text-center'>
                        <div>
                            <Link to={`edit/${product.id}`} className='btn btn-warning btn-sm'>Edit product</Link>
                        </div>
                    </td>
                    <td className='text-center'>
                        <div>
                            <Link to={`delete/${product.id}`} className='btn btn-danger btn-sm'>Delete product</Link>
                        </div>
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading products:", error);
            return (
                <tr>
                    <td colSpan="7" style={{ color: 'red' }}>Error loading products</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Product list</h3>
            </div>
            <div style={{ margin: "10px", display: 'flex', justifyContent: "space-between" }}>
                <Link className='btn btn-success btn-m' to={"add"}>Add Product</Link>
                <Link className='btn btn-primary btn-m' to={"menu"}>Go back to menu</Link>
            </div>




            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Name</th>
                        <th scope='col'>Category</th>
                        <th scope='col'>Weight</th>
                        <th scope='col'>SKU</th>
                        <th scope='col'>Edit</th>
                        <th scope='col'>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {currentProducts.length === 0 ? (
                        <tr>
                            <td colSpan="12" style={{ color: 'red' }}>No products found</td>
                        </tr>
                    ) : (
                        renderProducts()
                    )}
                </tbody>
            </table>
            <div>
                <nav className="d-flex justify-content-center">
                    <ul className="pagination">
                        {Array.from({ length: totalPages }, (_, i) => (
                            <li key={i + 1} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                                <button className="page-link" onClick={() => setCurrentPage(i + 1)}>{i + 1}</button>
                            </li>
                        ))}
                    </ul>
                </nav>
            </div>
            {!apiOnline && (
                <div className='alert alert-danger text-center'>
                    Error: Backend system is down
                </div>
            )}
        </div>
    )
}
