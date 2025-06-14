import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function DeleteBatch() {

    const navigate = useNavigate();

    const { idBatch } = useParams();


    const [apiOnline, setApiOnline] = useState(true);

    const [batch, setBatch] = useState({
        id: 0,
        expirationDate: "",
        batchCode: "",
        product: { id: 0 },
    });


    const { id, expirationDate, batchCode, product } = batch;

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

    function formatDateToYYYYMMDD(dateString) {
        if (!dateString) return "";

        const [day, month, year] = dateString.split("-");
        return `${year}-${month}-${day}`;
    }

    useEffect(() => {
        const run = async () => {
            const backendOK = await verifyBackendStatus();
            if (backendOK) {
                try {
                    const resultBatch = await axios.get(`http://localhost:8080/api/batches/id/${idBatch}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                    resultBatch.data.expirationDate = formatDateToYYYYMMDD(resultBatch.data.expirationDate);
                    setBatch(resultBatch.data);

                } catch (error) {
                    console.error("Error fetching the batch to delete:", error);

                    if (error.status === 404) {
                        alert("Batch not found");
                        navigate("/admin/items/batches");
                    }
                }
            }
        };

        run();
    }, []);

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        const confirmDelete = window.confirm("This action will SOFT DELETE the batch. ¿Continue?");
        if (!confirmDelete) return;
        try {
            await axios.put(`http://localhost:8080/api/batches/delete/${idBatch}`, batch, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Batch succesfully deleted');
            navigate("/admin/batches");


        } catch (error) {
            console.error(error);
            alert("Error deleting the batch: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Delete batch</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' value={id} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="expirationDate" className="form-label">Expiration Date</label>
                        {(expirationDate != "") ? (
                            <input type="date" className="form-control" id="expirationDate" name='expirationDate' value={expirationDate} disabled />
                        ) : (
                            <input type="text" className="form-control" id="expirationDate" name='expiration' value="N/A" disabled />
                        )
                        }
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="batchCodeTxt" className="form-label">Batch Code</label>
                        <input type="text" className="form-control" id="batchCodeTxt" name='batchCode' minLength={1} maxLength={45} required value={batchCode} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="productDdl" className="form-label">Product</label>
                        <select id="productDdl" name='Product' className="form-control" value={product.id} disabled>
                            <option key={-1} value={batch.product.id}>{batch.product.name}</option>
                        </select>
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-danger"  >Delete</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={'/admin/batches'} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
