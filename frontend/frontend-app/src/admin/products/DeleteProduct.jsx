import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function DeleteProduct() {

    const navigate = useNavigate();

    const { idProduct } = useParams();


    const [apiOnline, setApiOnline] = useState(true);

    const [product, setProduct] = useState({
        id: 0,
        name: "",
        category: { id: 0, description: "" },
        weight: 0,
        sku: "",
    });

    const { id, name, weight, sku, category, active } = product;

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
                    const resultProduct = await axios.get(`http://localhost:8080/api/products/${idProduct}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                    setProduct(resultProduct.data);
                } catch (error) {
                    console.error("Error fetching the product to delete:", error);
                    if (error.status === 404) {
                        alert("Product not found");
                        navigate("/admin/items/products");
                    }
                }
            }
        };

        run();
    }, []);

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        const confirmDelete = window.confirm("This action will SOFT DELETE the product. ¿Continue?");
        if (!confirmDelete) return;
        try {
            await axios.put(`http://localhost:8080/api/products/delete/${idProduct}`, product, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Product succesfully deleted');
            navigate("/admin/products");


        } catch (error) {
            console.error(error);
            alert("Error deleting the product: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Delete product</h3>
            </div>
            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-3 mx-auto">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' required value={id} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="nameTxt" className="form-label">Name</label>
                        <input type="text" className="form-control" id="nameTxt" name='name' minLength={1} maxLength={45} required value={name} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="categoryDdl" className="form-label">Género</label>
                        <select id="categoryDdl" name='Category' className="form-control" value={product.category.id} disabled>
                            <option key={1} value={product.category.id}>{product.category.description}</option>
                        </select>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="weightTxt" className="form-label">Weight</label>
                        <input type="text" className="form-control" id="weightTxt" name='weight' minLength={1} required value={weight} pattern='^\d+(\.\d+)?$' disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="skuTxt" className="form-label">SKU</label>
                        <input type="text" className="form-control" id="skuTxt" name='sku' minLength={13} maxLength={13} required value={sku} disabled />
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-danger"  >Delete</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={'/admin/products'} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
