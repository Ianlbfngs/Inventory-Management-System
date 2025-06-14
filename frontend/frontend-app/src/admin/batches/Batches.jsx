import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function Batches() {

    const [apiOnline, setApiOnline] = useState(true);

    const [batches, setBatches] = useState([]);

    const verifyBackendStatus = async () => {
        try {
            await axios.get('http://localhost:8080/actuator/health');
            try {
                await axios.get('http://localhost:8080/api/products/actuator/health');
                console.log('backend OK');
                setApiOnline(true);
                return true;
            } catch (error) {
                console.warn('Products service OFF');
            }
        } catch (error) {
            console.warn('Api gateway OFF');
        }
        setApiOnline(false);
        return false;
    };




    useEffect(() => {
        const run = async () => {
            const backendOK = await verifyBackendStatus();
            if (backendOK) {
                try {
                    const resultBatches = await axios.get("http://localhost:8080/api/batches/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                    setBatches(resultBatches.data);
                } catch (error) {
                    console.error("Error fetching batches:", error);
                }
            }
        };

        run();
    }, []);

    const filteredBatches = batches;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentBatches = filteredBatches.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredBatches?.length || 0) / itemsPerPage);

    function renderBatches() {
        try {
            return currentBatches.map((batch, index) => (

                <tr key={index}>
                    <th scope='row'>{batch.id}</th>
                    <td>{batch.expirationDate || "N/A"}</td>
                    <td>{batch.batchCode}</td>
                    <td>{batch.product.name}</td>
                    <td className='text-center'>
                        <div>
                            <Link to={`edit/${batch.id}`} className='btn btn-warning btn-sm'>Edit batch</Link>
                        </div>
                    </td>
                    <td className='text-center'>
                        <div>
                            <Link to={`delete/${batch.id}`} className='btn btn-danger btn-sm'>Delete batch</Link>
                        </div>
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading batch:", error);
            return (
                <tr>
                    <td colSpan="7" style={{ color: 'red' }}>Error loading batch</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Batch list</h3>
            </div>
            <div style={{ margin: "10px", display: 'flex', justifyContent: "space-between" }}>
                <Link className='btn btn-success btn-m' to={"add"}>Add batch</Link>
                <Link className='btn btn-primary btn-m' to={"/admin/products/menu"}>Go back to menu</Link>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Expiration Date</th>
                        <th scope='col'>Code</th>
                        <th scope='col'>Product</th>
                        <th scope='col'>Edit</th>
                        <th scope='col'>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {currentBatches.length === 0 ? (
                        <tr>
                            <td colSpan="6" style={{ color: 'red' }}>No batches found</td>
                        </tr>
                    ) : (
                        renderBatches()
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
