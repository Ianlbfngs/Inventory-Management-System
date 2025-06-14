import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function DeleteCategory() {

    const navigate = useNavigate();

    const { idCategory } = useParams();


    const [apiOnline, setApiOnline] = useState(true);


    const [category, setCategory] = useState({
        id: 0,
        description: "",
    });


    const { id, description } = category;

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultCategory = await axios.get(`http://localhost:8080/api/categories/${idCategory}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                setCategory(resultCategory.data);

            } catch (error) {
                setApiOnline(false);
                if (error.status === 404) {
                    alert("Category not found");
                    navigate("/admin/items/categories");
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

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        const confirmDelete = window.confirm("This will delete PERMANENTLY the category from the database. ¿Continue?");
        if (!confirmDelete) return;
        try {
            await axios.delete(`http://localhost:8080/api/categories/delete/${idCategory}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Category succesfully deleted');
            navigate("/admin/categories");

        } catch (error) {
            console.error(error);
            alert("Error deleting the category: " + (error.response.data.error || error.message));

        }
    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Delete category</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <label className="form-label text-danger" >*Make sure that no product is using the category to delete*</label>

                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' value={id} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="descriptionTxt" className="form-label">Description</label>
                        <input type="text" className="form-control" id="descriptionTxt" name='description' value={description} disabled />

                    </div>
                </div>
                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-danger"  >Delete</button>
                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to={"/admin/categories"} >Go back</Link>

                </div>
            </form>
        </div>

    )
}
