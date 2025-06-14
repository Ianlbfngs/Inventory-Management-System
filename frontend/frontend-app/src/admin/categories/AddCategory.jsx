import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function AddCategory() {

    const [apiOnline, setApiOnline] = useState(true);

    const [category, setCategory] = useState({
        id: 0,
        description: "",
    });

    const { description } = category;

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

    const onInputChangeCategory = (e) => {
        const { name, value } = e.target;

        setCategory(prev => ({
            ...prev,
            [name]: value
        }));
    };



    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        try {
            await axios.post('http://localhost:8080/api/categories/add', category, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Category succesfully added');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error adding the category: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Add category</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-m">
                        <label htmlFor="descriptionTxt" className="form-label">Description</label>
                        <input type="text" className="form-control" id="descriptionTxt" name='description' minLength={1} maxLength={45} required value={description} onChange={onInputChangeCategory} />
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-success"  >Add</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={"/admin/categories"} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
