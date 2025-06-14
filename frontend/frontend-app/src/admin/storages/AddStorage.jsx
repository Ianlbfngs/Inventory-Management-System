import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function AddStorage() {

    const [apiOnline, setApiOnline] = useState(true);

    const [storage, setStorage] = useState({
        id: 0,
        name: "",
        capacity: 0,
        location: "",
        active: false
    });

    const { name, capacity, location } = storage;

    const onInputChangeStorage = (e) => {
        const { name, value } = e.target;

        setStorage(prev => ({
            ...prev,
            [name]: value
        }));
    };

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

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        try {
            await axios.post('http://localhost:8080/api/storage/add', storage, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Storage succesfully added');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error adding the storage: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Add storage</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="nameTxt" className="form-label">Name</label>
                        <input type="text" className="form-control" id="nameTxt" name='name' minLength={1} maxLength={200} required value={name} onChange={onInputChangeStorage} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="capacityTxt" className="form-label">Capacity</label>
                        <input type="number" className="form-control" id="capacityTxt" name='capacity' required value={capacity} onChange={onInputChangeStorage} />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6 mx-auto">
                        <label htmlFor="locationTxt" className="form-label">Location</label>
                        <input type="text" className="form-control" id="locationTxt" name='location' minLength={1} maxLength={150} required value={location} onChange={onInputChangeStorage} />
                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-success"  >Add</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to="/admin/storages" >Go back</Link>

                </div>
            </form>
        </div>

    )
}
