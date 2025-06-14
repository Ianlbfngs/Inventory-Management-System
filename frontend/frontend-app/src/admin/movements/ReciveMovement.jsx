import axios from 'axios';
import React, { useEffect, useState, useMemo } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';

export default function ReciveMovement() {

    const navigate = useNavigate();

    const { idMovement } = useParams();


    const [apiOnline, setApiOnline] = useState(true);

    const [newStatus, setNewStatus] = useState(null);

    const [movement, setMovement] = useState({
        id: 0,
        originStockId: 0,
        targetStockId: 0,
        stockAmount: 0,
        issueDate: "",
        receiptDate: "",
        movementType: { description: "" },
        createdByUser: "",
        status: 0

    });


    const { id, originStockId, targetStockId, stockAmount, issueDate, receiptDate, movementType, createdByUser, status } = movement;

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultMovement = await axios.get(`http://localhost:8080/api/movements/${idMovement}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                if (resultMovement.data.status !== 0) {
                    alert("Movement already recived/cancelled");
                    navigate("/admin/movements");
                }
                setMovement(resultMovement.data);


            } catch (error) {
                setApiOnline(false);
                if (error.status === 404) {
                    alert("Movement not found");
                    navigate("/admin/movements");
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
                await axios.get('http://localhost:8080/api/movements/actuator/health');
                console.log('backend OK');
                setApiOnline(true);
                return true;
            } catch (error) {
                console.warn('Movements service OFF');
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
        const confirmDelete = window.confirm("This action cannot be undone. ¿Continue?");
        if (!confirmDelete) return;
        try {
            await axios.put(`http://localhost:8080/api/movements/reception/${idMovement}`, newStatus, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken'), 'Content-Type': 'application/json' } });
            alert('Movement status succesfully changed');
            navigate("/admin/movements");

        } catch (error) {
            console.error(error);
            alert("Error modifying movement status " + (error.response.data.error || error.message));

        }
    }

    const statusMap = useMemo(() => {
        const map = new Map();
        map.set(-1, "Cancelled");
        map.set(0, "Pending");
        map.set(1, "Received");
        return map;
    }, []);

    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Modify movement status</h3>
            </div>
            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="idTxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="idTxt" name='id' required value={id} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="originStockTxt" className="form-label">Origin Stock Id</label>
                        <input type="text" className="form-control" id="originStockTxt" name='originStockId' required value={(originStockId !== -1) ? originStockId : "External entity"} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="targetStockTxt" className="form-label">Target Stock Id</label>
                        <input type="text" className="form-control" id="targetStockTxt" name='targetStockId' required value={(targetStockId !== -1) ? targetStockId : "External entity"} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="stockAmountTxt" className="form-label">Amount</label>
                        <input type="text" className="form-control" id="stockAmountTxt" name='stockAmount' required value={stockAmount} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="issueDate" className="form-label">Issue Date</label>
                        <input type="text" className="form-control" id="issueDate" name='issueDate' required value={issueDate} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="receptionDate" className="form-label">Reception Date</label>
                        <input type="text" className="form-control" id="receptionDate" name='receptionDate' required value={receiptDate || "N/A"} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="movementTypeTxt" className="form-label">Movement Type</label>
                        <input type="text" className="form-control" id="movementTypeTxt" name='movementType' required value={movementType.description} disabled />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="creatorUserTxt" className="form-label">Creator</label>
                        <input type="text" className="form-control" id="creatorUserTxt" name='creatorUser' required value={createdByUser} disabled />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="statusTxt" className="form-label">Status</label>
                        <input type="text" className="form-control" id="statusTxt" name='status' required value={statusMap.get(status)} disabled />
                    </div>

                </div>

                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-success" onClick={() => setNewStatus(true)} >Mark as recived</button>
                    <button type="submit" style={{ marginLeft: "30px" }} className="btn btn-danger" onClick={() => setNewStatus(false)} >Mark as cancelled</button>

                    <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to="/admin/movements" >Go back</Link>

                </div>
            </form>
        </div>

    )
}
