import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function DeleteStock() {

    const navigate = useNavigate();

    const { idStock } = useParams();


    const [apiOnline, setApiOnline] = useState(true);


    const [stock, setStock] = useState({
        id: 0,
        storageId: 0,
        batchCode: "",
        availableStock: 0,
        pendingStock: 0,
        active: false
    });


    const { id, storageId, batchCode, availableStock, pendingStock } = stock;

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultStock = await axios.get(`http://localhost:8080/api/stock/${idStock}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });

                setStock(resultStock.data);

            } catch (error) {
                setApiOnline(false);
                if (error.status === 404) {
                    alert("Stock not found");
                    navigate("/admin/stock");
                }
                console.error("Error:", error);
            }
        };
        fetchData();
    }, []);

    const verifyBackendStatus = async () => {
        try {
            await axios.get('http://localhost:8080/actuator/health');
            try {
                await axios.get('http://localhost:8080/api/stock/actuator/health');
                console.log('backend OK');
                setApiOnline(true);
                return true;
            } catch (error) {
                console.warn('Stock service OFF');
            }
        } catch (error) {
            console.warn('Api gateway OFF');
        }
        setApiOnline(false);
        return false;
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        const confirmDelete = window.confirm("This action will SOFT DELETE the stock. ¿Continue?");
        if (!confirmDelete) return;
        try {
            await axios.put(`http://localhost:8080/api/stock/delete/${idStock}`, stock, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Stock succesfully deleted');
            navigate("/admin/stock");

        } catch (error) {
            console.error(error);
            alert("Error updating the stock: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Delete stock</h3>
            </div>
            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' required value={id} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="storageIdTxt" className="form-label">Storage Id</label>
                        <input type="text" className="form-control" id="storageIdTxt" name='storageId' required value={storageId} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="batchCodeTxt" className="form-label">Batch Code</label>
                        <input type="text" className="form-control" id="batchCodeTxt" name='batchCode'required value={batchCode} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="availableStockTxt" className="form-label">Available Stock</label>
                        <input type="text" className="form-control" id="availableStockTxt" name='availableStock'required value={availableStock} disabled />
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-3 mx-auto">
                        <label htmlFor="pendingStockTxt" className="form-label">Pending Stock</label>
                        <input type="text" className="form-control" id="pendingStockTxt" name='pendingStock'required value={pendingStock} disabled />
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-danger"  >Delete</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to="/admin/stock" >Go back</Link>

                </div>
            </form>
        </div>

    )
}
