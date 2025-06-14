import { Link } from 'react-router-dom';

export default function Menu() {

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Products Menu </h3>
            </div>
            <div className="row mb-3">
                <div className="col-md-6">
                    <Link className='btn btn-primary btn-lg w-100 ' to="/admin/products">Products</Link>
                </div>
                <div className="col-md-6">
                    <Link className='btn btn-primary btn-lg w-100 ' to="/admin/batches">Batches</Link>
                </div>
                <div className="col-md-6">
                </div>
            </div>
            <div className="row m-0">
                    <Link className='btn btn-primary btn-lg w-100 ' to="/admin/categories">Categories</Link>
            </div>
        </div>
    )
}
