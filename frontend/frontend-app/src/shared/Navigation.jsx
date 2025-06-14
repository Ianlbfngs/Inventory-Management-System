import React from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom';

export default function Navigation() {
  const location = useLocation();
  const navigate = useNavigate();
  
  if (localStorage.getItem("jwtToken") == null) {
    return null;
  }

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    navigate("/login");
  };
  return (
    <div className='container-fluid'>
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          <a className="navbar-brand">
            Stock Management Sistem
          </a>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav">
              <>
                <li className="nav-item">
                  <Link className="nav-link active" to="/admin/menu">Menu</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link active" to="/admin/products/menu">Products Menu</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link active" to="/admin/storages">Storages</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link active" to="/admin/stock">Stock</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link active" to="/admin/movements">Movements</Link>
                </li>
              </>
            </ul>
          </div>
          <button className="nav-link active btn btn-link text-white" onClick={handleLogout}>Logout</button>
        </div>
      </nav>
    </div>
  );
}
