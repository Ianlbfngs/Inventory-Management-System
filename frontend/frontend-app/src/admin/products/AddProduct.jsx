import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function AddProduct() {

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
                const resultCategories = await axios.get("http://localhost:8080/api/categories/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setCategories(resultCategories.data);
                } catch (error) {
                    console.error("Error fetching categories:", error);
                }
            }
        };

        run();
    }, []);

    const [product, setProduct] = useState({
        id: 0,
        name: "",
        category: { id: 0 },
        weight: 0,
        sku: "",
        active: false
    });

    const { name, weight, sku, category, active } = product;



    const onInputChangeProduct = (e) => {
        const { name, value } = e.target;
        if (name === "Category") {
            setProduct(prev => ({
                ...prev,
                category: { ...prev.category, id: parseInt(value) }
            }));
        } else {
            setProduct(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };



    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        if (category.id === 0) {
            alert('Select a category');
            return;
        }
        try {
            await axios.post('http://localhost:8080/api/products/add', product, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Product succesfully added');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error adding the product: " + (error.response.data.error || error.message));

        }
    }




    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Add product</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="nameTxt" className="form-label">Name</label>
                        <input type="text" className="form-control" id="nameTxt" name='name' minLength={1} maxLength={45} required value={name} onChange={onInputChangeProduct} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="categoryDdl" className="form-label">Category</label>
                        <select id="categoryDdl" name='Category' className="form-control" value={category.id} onChange={onInputChangeProduct}>
                            <option value={0}>Select the category</option>
                            {categories.map((category, index) => (
                                <option key={index} value={category.id}>{category.description}</option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="weightTxt" className="form-label">Weight</label>
                        <input type="text" className="form-control" id="weightTxt" name='weight' minLength={1} required value={weight} pattern='^\d+(\.\d+)?$' onChange={onInputChangeProduct} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="skuTxt" className="form-label">SKU</label>
                        <input type="text" className="form-control" id="skuTxt" name='sku' minLength={13} maxLength={13} required value={sku} onChange={onInputChangeProduct} />
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-success"  >Add</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={'/admin/products'} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
