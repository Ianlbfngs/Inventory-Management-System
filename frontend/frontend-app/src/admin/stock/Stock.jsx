import axios from 'axios';
import React, { use, useEffect, useState, useMemo } from 'react'
import { Link } from 'react-router-dom';

export default function Stock() {

    const [apiOnline, setApiOnline] = useState(true);

    const [stock, setStock] = useState([]);

    const [storages, setStorages] = useState([]);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultStock = await axios.get("http://localhost:8080/api/stock/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setStock(resultStock.data);
                const resultStorage = await axios.get("http://localhost:8080/api/storage/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setStorages(resultStorage.data);
            } catch (error) {
                setApiOnline(false);
                console.error("Error: ", error);
            }
        };

        fetchData();
    }, []);

    const filteredStock = stock;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentStock = filteredStock.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredStock?.length || 0) / itemsPerPage);


    const storageMap = useMemo(() => {
        const map = new Map();
        storages.forEach(storage => map.set(storage.id, storage.name));
        return map;
    }, [storages]);

    function renderStock() {
        try {
            return currentStock.map((stockAux, index) => (

                <tr key={index}>
                    <th scope='row'>{stockAux.id}</th>
                    <td>{storageMap.get(stockAux.storageId) +" | "+stockAux.storageId}</td>
                    <td>{stockAux.batchCode}</td>
                    <td>{stockAux.availableStock}</td>
                    <td>{stockAux.pendingStock}</td>
                    <td className='text-center'>
                        <div>
                            <Link to={`delete/${stockAux.id}`} className='btn btn-danger btn-sm'>Delete stock</Link>
                        </div>
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading stock:", error);
            return (
                <tr>
                    <td colSpan="6" style={{ color: 'red' }}>Error loading stock</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Stock list</h3>
            </div>
            <div style={{ margin: "10px", textAlign: "right"  }}>
                <Link className='btn btn-primary btn-m' to={"/admin/menu"}>Go back to menu</Link>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Storage | ID</th>
                        <th scope='col'>Batch Code</th>
                        <th scope='col'>Available Stock</th>
                        <th scope='col'>Pending Stock</th>
                        <th scope='col'>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {currentStock.length === 0 ? (
                        <tr>
                            <td colSpan="6" style={{ color: 'red' }}>No stock found</td>
                        </tr>
                    ) : (
                        renderStock()
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
