import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function EditStorage() {

    const navigate = useNavigate();

    const { idStorage } = useParams();


    const [apiOnline, setApiOnline] = useState(true);


    const [storage, setStorage] = useState({
        id: 0,
        name: "",
        capacity: 0,
        location: "",
        active: false
    });

    const { id, name, capacity, location } = storage;

    const onInputChangeStorage = (e) => {
        const { name, value } = e.target;

        setStorage(prev => ({
            ...prev,
            [name]: value
        }));
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultStorage = await axios.get(`http://localhost:8080/api/storage/${idStorage}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setStorage(resultStorage.data);

            } catch (error) {
                setApiOnline(false);
                if (error.status === 404) {
                    alert("Storage not found");
                    navigate("/admin/storages");
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
            await axios.put(`http://localhost:8080/api/storage/update/${idStorage}`, storage, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Storage succesfully updated');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error updating the storage: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Edit storage</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' value={id} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="nameTxt" className="form-label">Name</label>
                        <input type="text" className="form-control" id="nameTxt" name='name' minLength={1} maxLength={200} required value={name} onChange={onInputChangeStorage} />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="capacityTxt" className="form-label">Capacity</label>
                        <input type="number" className="form-control" id="capacityTxt" name='capacity' required value={capacity} onChange={onInputChangeStorage} />
                    </div>
                    <div className="col-md-6">
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
                    <button type="submit" className="btn btn-warning"  >Edit</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to="/admin/storages" >Go back</Link>

                </div>
            </form>
        </div>

    )
}
