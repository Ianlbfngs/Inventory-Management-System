import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function Storages() {

    const [apiOnline, setApiOnline] = useState(true);

    const [storages, setStorages] = useState([]);

    const verifyBackendStatus = async () => {
        try {
            await axios.get('http://localhost:8080/actuator/health');
            try {
                await axios.get('http://localhost:8080/api/storage/actuator/health');
                console.log('backend OK');
                setApiOnline(true);
                return true;
            } catch (error) {
                console.warn('Storage service OFF');
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
                    const resultStorages = await axios.get("http://localhost:8080/api/storage/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                    setStorages(resultStorages.data);
                } catch (error) {
                    console.error("Error fetching storages:", error);
                }
            }
        };

        run();
    }, []);

    const filteredStorages = storages;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentStorages = filteredStorages.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredStorages?.length || 0) / itemsPerPage);

    function renderStorages() {
        try {
            return currentStorages.map((storage, index) => (

                <tr key={index}>
                    <th scope='row'>{storage.id}</th>
                    <td>{storage.name || "N/A"}</td>
                    <td>{storage.capacity}</td>
                    <td>{storage.location}</td>
                    <td className='text-center'>
                        <div>
                            <Link to={`edit/${storage.id}`} className='btn btn-warning btn-sm'>Edit storage</Link>
                        </div>
                    </td>
                    <td className='text-center'>
                        <div>
                            <Link to={`delete/${storage.id}`} className='btn btn-danger btn-sm'>Delete storage</Link>
                        </div>
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading storages:", error);
            return (
                <tr>
                    <td colSpan="6" style={{ color: 'red' }}>Error loading storages</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Storage list</h3>
            </div>
            <div style={{ margin: "10px", display: 'flex', justifyContent: "space-between" }}>
                <Link className='btn btn-success btn-m' to={"add"}>Add storage</Link>
                <Link className='btn btn-primary btn-m' to={"/admin/menu"}>Go back to menu</Link>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Name</th>
                        <th scope='col'>Capacity</th>
                        <th scope='col'>Location</th>
                        <th scope='col'>Edit</th>
                        <th scope='col'>Delete</th>

                    </tr>
                </thead>
                <tbody>
                    {currentStorages.length === 0 ? (
                        <tr>
                            <td colSpan="6" style={{ color: 'red' }}>No storages found</td>
                        </tr>
                    ) : (
                        renderStorages()
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
