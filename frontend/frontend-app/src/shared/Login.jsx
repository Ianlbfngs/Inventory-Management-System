import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import React, { useState } from 'react'
export default function Login() {

    const navigate = useNavigate();

    const [apiOnline, setApiOnline] = useState(true);

    const [credential, setCredential] = useState({
        username: "",
        password: ""
    });

    const { username, password } = credential

    const urlCredentials = 'http://localhost:8080/api/credentials';

    const verifyBackendStatus = async () => {
        try {
            await axios.get('http://localhost:8080/actuator/health');
            try {
                await axios.get(urlCredentials + '/actuator/health');
                console.log('backend OK');
                setApiOnline(true);
                return true;
            } catch (error) {
                console.warn('Credentials service OFF');
            }
        } catch (error) {
            console.warn('Api gateway OFF');
        }
        setApiOnline(false);
        return false;
    };

    const onInputChangeCredential = (e) => {
        const { name, value } = e.target;
        setCredential(prev => ({
            ...prev,
            [name]: value
        }));
    };



    const onSubmit = async (e) => {
        e.preventDefault();
        if (await verifyBackendStatus()) {
            try {
                const result = await axios.post(urlCredentials + "/login", credential);
                alert("Login successful");
                console.log(result);
                localStorage.setItem("jwtToken", result.data.token);
                navigate("/admin/menu");
            } catch (error) {
                console.log(error.message);
                console.log(error.response?.data?.error);
                alert(error.response?.data?.error);
            }
        } else {
            alert("Unable to stablish connection with backend services");
        }

    }

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Login</h3>
            </div>
            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="userTxt" className="form-label">Username</label>
                        <input type="text" className="form-control" id="userTxt" name='username' minLength={1} maxLength={45} required value={username} onChange={onInputChangeCredential} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="passwordTxt" className="form-label">Password</label>
                        <input type="password" className="form-control" id="passwordTxt" name='password' minLength={1} maxLength={45} required value={password} onChange={onInputChangeCredential} />
                    </div>
                </div>
                <div className='text-center'>
                    <button type="submit" className="btn btn-primary" >Login</button>
                    <Link className='btn btn-warning btn-m' style={{margin: "30px"}} to={"/register"}>Go to Register Form</Link>

                </div>
            </form>
            {!apiOnline && (
                <div className='alert alert-danger text-center'>
                    Error: Backend system is down
                </div>
            )}
        </div>
    )
}
