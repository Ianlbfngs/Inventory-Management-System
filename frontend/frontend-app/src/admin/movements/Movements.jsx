import axios from 'axios';
import React, { useEffect, useState, useMemo } from 'react'
import { Link } from 'react-router-dom';

export default function Movements() {

    const [apiOnline, setApiOnline] = useState(true);

    const [movements, setMovements] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultMovements = await axios.get("http://localhost:8080/api/movements/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setMovements(resultMovements.data);
            } catch (error) {
                setApiOnline(false);
                console.error("Error: ", error);
            }
        };

        fetchData();
    }, []);

    const filteredMovements = movements;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentMovements = filteredMovements.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredMovements?.length || 0) / itemsPerPage);

    const statusMap = useMemo(() => {
        const map = new Map();
        map.set(-1, "Cancelled");
        map.set(0, "Pending");
        map.set(1, "Received");
        return map;
    }, []);

    function renderMovements() {
        try {
            return currentMovements.map((movement, index) => (

                <tr key={index}>
                    <th scope='row'>{movement.id}</th>
                    <td>{movement.movementType.description}</td>
                    <td>{(movement.originStockId !== -1) ? movement.originStockId : "External origin"}</td>
                    <td>{(movement.targetStockId !== -1) ? movement.targetStockId : "External target"}</td>
                    <td>{movement.stockAmount}</td>
                    <td>{movement.issueDate}</td>
                    <td>{movement.receiptDate || "N/A"}</td>
                    <td>{movement.createdByUser}</td>
                    <td>{statusMap.get(movement.status)}</td>
                    <td>
                        {movement.status === 0 ? (<Link to={`recive/${movement.id}`} className='btn btn-warning btn-sm'>Mark as recived</Link>) : ("")}
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading movements:", error);
            return (
                <tr>
                    <td colSpan="8" style={{ color: 'red' }}>Error loading movements</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Movements list</h3>
            </div>
            <div style={{ margin: "10px", display: 'flex',   justifyContent: "space-between"  }}>
                <Link className='btn btn-success btn-m' to={"add"}>Make a movement</Link>              
                <Link className='btn btn-primary btn-m' to={"/admin/menu"}>Go back to menu</Link>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Type</th>
                        <th scope='col'>Origin Stock ID</th>
                        <th scope='col'>Target Stock ID</th>
                        <th scope='col'>Amount</th>
                        <th scope='col'>Issue Date</th>
                        <th scope='col'>Receipt Date</th>
                        <th scope='col'>Created by</th>
                        <th scope='col'>Status</th>
                        <th scope='col'></th>
                    </tr>
                </thead>
                <tbody>
                    {currentMovements.length === 0 ? (
                        <tr>
                            <td colSpan="12" style={{ color: 'red' }}>No movements found</td>
                        </tr>
                    ) : (
                        renderMovements()
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
