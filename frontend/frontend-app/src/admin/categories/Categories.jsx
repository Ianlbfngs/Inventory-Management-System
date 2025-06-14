import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function Categories() {

    const [apiOnline, setApiOnline] = useState(true);

    const [categories, setCategories] = useState([]);

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
                    const resultCategories = await axios.get('http://localhost:8080/api/categories/all', { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
                    setCategories(resultCategories.data);
                } catch (error) {
                    console.error("Error fetching categories:", error);
                }
            }
        };

        run();
    }, []);

    const filteredCategories = categories;

    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentCategories = filteredCategories.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil((filteredCategories?.length || 0) / itemsPerPage);

    function renderCategories() {
        try {
            return currentCategories.map((category, index) => (

                <tr key={index}>
                    <th scope='row'>{category.id}</th>
                    <td>{category.description}</td>
                    <td className='text-center'>
                        <div>
                            <Link to={`edit/${category.id}`} className='btn btn-warning btn-sm'>Edit category</Link>
                        </div>
                    </td>
                    <td className='text-center'>
                        <div>
                            <Link to={`delete/${category.id}`} className='btn btn-danger btn-sm'>Delete category</Link>
                        </div>
                    </td>
                </tr>
            ))
        } catch (error) {
            console.error("Error loading categories:", error);
            return (
                <tr>
                    <td colSpan="4" style={{ color: 'red' }}>Error loading categories</td>
                </tr>
            )
        }
    }

    return (
        <div className='container-fluid'>
            <div style={{ margin: "30px" }}>
                <h3>Categories list</h3>
            </div>
            <div style={{ margin: "10px", display: 'flex', justifyContent: "space-between" }}>
                <Link className='btn btn-success btn-m' to={"add"}>Add category</Link>
                <Link className='btn btn-primary btn-m' to={"/admin/products/menu"}>Go back to menu</Link>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope='col'>Id</th>
                        <th scope='col'>Description</th>
                        <th scope='col'>Edit</th>
                        <th scope='col'>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {currentCategories.length === 0 ? (
                        <tr>
                            <td colSpan="4" style={{ color: 'red' }}>No categories found</td>
                        </tr>
                    ) : (
                        renderCategories()
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
