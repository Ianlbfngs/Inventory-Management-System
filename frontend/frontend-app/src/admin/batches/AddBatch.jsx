import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function AddBatch() {

    const [apiOnline, setApiOnline] = useState(true);

    const [products, setProducts] = useState([]);


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
                    const resultProducts = await axios.get("http://localhost:8080/api/products/all", {headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") }});
                    setProducts(resultProducts.data);
                } catch (error) {
                    console.error("Error fetching products:", error);
                }
            }
        };

        run();
    }, []);



    const [batch, setBatch] = useState({
        id: 0,
        expirationDate: "",
        batchCode: "",
        product: { id: 0 },
        active: false
    });

    const { expirationDate, batchCode, product } = batch;

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

    function formatDateToDDMMYYYY(dateString) {
        if (!dateString) return "";

        const [year, month, day] = dateString.split("-");
        return `${day}-${month}-${year}`;
    }

    const onSubmit = async (e) => {
        e.preventDefault();
        console.log(batch);
        if (! await verifyBackendStatus()) return;
        if (product.id === 0) {
            alert('Select a product');
            return;
        }

        batch.expirationDate = formatDateToDDMMYYYY(expirationDate);

        try {

            await axios.post('http://localhost:8080/api/batches/add', batch, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Batch succesfully added');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error adding the batch: " + (error.response.data.error || error.message));

        }
    }




    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Add batch</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="expirationDate" className="form-label">Expiration Date</label>
                        <input type="date" className="form-control" id="expirationDate" name='expirationDate' value={expirationDate} onChange={onInputChangeBatch} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="batchCodeTxt" className="form-label">Batch Code</label>
                        <input type="text" className="form-control" id="batchCodeTxt" name='batchCode' minLength={1} maxLength={45} required value={batchCode} onChange={onInputChangeBatch} />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6 mx-auto">
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
                    <button type="submit" className="btn btn-success"  >Add</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={'/admin/batches'} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
