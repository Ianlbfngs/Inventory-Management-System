import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function EditBatch() {

    const navigate = useNavigate();

    const { idBatch } = useParams();


    const [apiOnline, setApiOnline] = useState(true);

    const [products, setProducts] = useState([]);

    const [batch, setBatch] = useState({
        id: 0,
        expirationDate: "",
        batchCode: "",
        product: { id: 0 },
        active: true
    });

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

    const { id, expirationDate, batchCode, product } = batch;

    function formatDateToDDMMYYYY(dateString) {
        if (!dateString) return "";

        const [year, month, day] = dateString.split("-");
        return `${day}-${month}-${year}`;
    }

    function formatDateToYYYYMMDD(dateString) {
        if (!dateString) return "";

        const [day, month, year] = dateString.split("-");
        return `${year}-${month}-${day}`;
    }

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultProducts = await axios.get("http://localhost:8080/api/products/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                const resultBatch = await axios.get(`http://localhost:8080/api/batches/id/${idBatch}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });

                resultBatch.data.expirationDate = formatDateToYYYYMMDD(resultBatch.data.expirationDate);

                setProducts(resultProducts.data);
                setBatch(resultBatch.data);

            } catch (error) {
                setApiOnline(false);
                if (error.status === 404) {
                    alert("Batch not found");
                    navigate("/admin/items/batches");
                }
                console.error("Error:", error);
            }
        };
        fetchData();
    }, []);


    const onInputChangeBatch = (e) => {
        const { name, value } = e.target;
        if (name === "Product") {
            setBatch(prev => ({
                ...prev,
                product: { ...prev.product, id: parseInt(value) }
            }));
        } else {
            setBatch(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };



    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        if (product.id === 0) {
            alert('Select a product');
            return;
        }
        batch.expirationDate = formatDateToDDMMYYYY(expirationDate);
        try {
            await axios.put(`http://localhost:8080/api/batches/update/${idBatch}`, batch, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Batch succesfully updated');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error updating the batch: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Edit batch</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' value={id} disabled />
                    </div>
                    {
                        (expirationDate) ? (
                            <div className="col-md-6">
                                <label htmlFor="expirationDate" className="form-label">Expiration Date</label>
                                <input type="date" className="form-control" id="expirationDate" name='expirationDate' value={expirationDate} onChange={onInputChangeBatch} />
                                <button type='button' style={{ margin: "10px" }} className="btn btn-danger" onClick={() => setBatch(prev => ({ ...prev, expirationDate: "" }))}>Delete expiration date</button>

                            </div>
                        ) : (
                            <div className="col-md-6">

                                <label htmlFor="expirationTxt" className="form-label">Expiration Date</label>
                                <input type="text" className="form-control" id="expirationTxt" value={"N/A"} disabled />
                                <button type='button' style={{ margin: "10px" }} className="btn btn-primary" onClick={() => setBatch(prev => ({ ...prev, expirationDate: "2030-01-01" }))}>Add expiration date</button>
                            </div>
                        )
                    }

                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="batchCodeTxt" className="form-label">Batch Code</label>
                        <input type="text" className="form-control" id="batchCodeTxt" name='batchCode' minLength={1} maxLength={45} required value={batchCode} onChange={onInputChangeBatch} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="productDdl" className="form-label">Product</label>
                        <select id="productDdl" name='Product' className="form-control" value={product.id} onChange={onInputChangeBatch}>
                            <option value={0}>Select the product of the batch</option>
                            {products.map((product, index) => (
                                <option key={index} value={product.id}>{product.name}</option>
                            ))}
                        </select>
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-warning"  >Edit</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={'/admin/batches'} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
