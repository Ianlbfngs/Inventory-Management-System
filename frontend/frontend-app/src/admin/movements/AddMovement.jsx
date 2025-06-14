import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function AddMovement() {

    const [apiOnline, setApiOnline] = useState(true);


    const [actionType, setActionType] = useState(-1);
    // actionType determines which fields are shown in the form.
    // values:
    // 1: transfer from an external source
    // 2: transfer from a storage to an external target
    // 3: transfer to a new stock
    // 4: transfer between storages 

    const [everyStock, setEveryStock] = useState([]);

    const [newMovement, setMovement] = useState({
        originStockId: -1,
        targetStockId: -1,
        stockAmount: 0,
    });

    const [newStock, setStock] = useState({
        storageId: -1,
        batchCode: ""
    });

    const { storageId, batchCode } = newStock;
    const { originStockId, targetStockId, stockAmount } = newMovement;

    const verifyBackendStatus = async () => {
        try {
            await axios.get('http://localhost:8080/actuator/health');
            try {
                await axios.get('http://localhost:8080/api/movements/actuator/health');
                try {
                    await axios.get('http://localhost:8080/api/stock/actuator/health');
                    console.log('backend OK');
                    setApiOnline(true);
                    return true;

                } catch (error) {
                    console.warn('Stock service OFF');
                }

            } catch (error) {
                console.warn('Movements service OFF');
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
                    const resultStock = await axios.get("http://localhost:8080/api/stock/all", { headers: { Authorization: 'Bearer ' + localStorage.getItem("jwtToken") } });
                    setEveryStock(resultStock.data);
                } catch (error) {
                    console.error("Error fetching every stock:", error);
                }
            }
        };

        run();
    }, []);

    const onInputChangeMovement = (e) => {
        const { name, value } = e.target;
        setMovement((prev) => ({
            ...prev,
            [name]: parseInt(value, 10)
        }));
    };

    const onInputChangeStock = (e) => {
        const { name, value } = e.target;
        setStock((prev) => ({
            ...prev,
            [name]: name === 'storageId' ? parseInt(value, 10) : value
        }));
    };





    const buildRequestBody = () => {
        const baseStock = {
            storageId: -1,
            batchCode: ""
        };

        const isStockUnchanged =
            newStock.storageId === baseStock.storageId &&
            newStock.batchCode.trim() === baseStock.batchCode;

        return {
            movement: newMovement,
            stockDTO: isStockUnchanged ? null : newStock
        };
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        if (! await verifyBackendStatus()) return;
        console.log(newMovement);
        if (newMovement.targetStockId === -2 || newMovement.originStockId === -2) {
            alert('Origin and target must be setted!');
            return;
        }
        if (newMovement.targetStockId === newMovement.originStockId) {
            alert('Origin and target must be different!');
            return;
        }

        const requestBody = buildRequestBody();

        try {
            await axios.post('http://localhost:8080/api/movements/add', requestBody, { headers: { Authorization: 'Bearer ' + localStorage.getItem('jwtToken') } });
            alert('Movement succesfully added');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Error making the movement: " + (error.response.data.error || error.message));

        }
    }

    function renderStock(stockId) {
        if (stockId === -1) return;
        const stock = everyStock.find(stock => stock.id === stockId);

        return (
            <div style={{ margin: "30px" }}>
                <table className="table table-striped table-hover align-middle">
                    <thead className='table-dark'>
                        <tr>
                            <th scope='col'>Id</th>
                            <th scope='col'>Storage id</th>
                            <th scope='col'>Batch code</th>
                            <th scope='col'>Available stock</th>
                            <th scope='col'>Pending stock</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr key={stock.id}>
                            <th scope='row'>{stock.id}</th>
                            <td>{stock.storageId}</td>
                            <td>{stock.batchCode}</td>
                            <td>{stock.availableStock}</td>
                            <td>{stock.pendingStock}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

        )

    }


    return (
        <div className='container'>
            <div style={{ margin: "30px" }}>
                <h3>Make a movement</h3>
            </div>
            {
                (actionType === -1) ? (
                    <div className="row">
                        <div className="col-12 mb-4">
                            <button className="btn btn-success w-50" onClick={() => setActionType(1)}>EXTERNAL SOURCE</button>
                        </div>
                        <div className="col-12 mb-4">
                            <button className="btn btn-success w-50" onClick={() => setActionType(2)}>EXTERNAL TARGET</button>
                        </div>
                        <div className="col-12 mb-4">
                            <button className="btn btn-success w-50" onClick={() => setActionType(3)}>STOCK TO NEW STOCK</button>
                        </div>
                        <div className="col-12 mb-4">
                            <button className="btn btn-success w-50" onClick={() => setActionType(4)}>STOCK TO STOCK</button>
                        </div>
                        <div className="col-12">
                            <Link className="btn btn-primary w-50" to="/admin/movements" >Go back</Link>
                        </div>
                    </div>
                ) : ("")
            }

            <form onSubmit={(e) => onSubmit(e)}>
                {
                    (() => {
                        switch (actionType) {
                            case 1:
                                return (
                                    <div style={{ margin: "30px" }}>
                                        <div className="row mb-3">
                                            <h4>Movement</h4>
                                            <p hidden>{newMovement.targetStockId = 0}</p>
                                            <div className="col-md-6 mx-auto">
                                                <label htmlFor="amountTxt" className="form-label">Amount</label>
                                                <input type="number" className="form-control" id="amountTxt" name="stockAmount" minLength={1} required value={stockAmount} onChange={onInputChangeMovement} />
                                            </div>
                                        </div>
                                        <div className="row mb-3">
                                            <h4>New Target Stock</h4>
                                            <div className="col-md-6">
                                                <label htmlFor="storageIdTxt" className="form-label">Storage Id</label>
                                                <input type="number" className="form-control" id="storageIdTxt" name="storageId" minLength={1} required value={storageId} onChange={onInputChangeStock} />
                                            </div>
                                            <div className="col-md-6">
                                                <label htmlFor="batchCodeTxt" className="form-label">Batch code</label>
                                                <input type="text" className="form-control" id="batchCodeTxt" name="batchCode" minLength={1} maxLength={45} required value={batchCode} onChange={onInputChangeStock} />
                                            </div>
                                        </div>
                                    </div>
                                );
                            case 2:
                                return (
                                    <div style={{ margin: "30px" }}>
                                        <div className="row mb-3">
                                            <h4>Movement</h4>
                                            <div className="col-md-6">
                                                <label htmlFor="originDDL" className="form-label">Origin stock</label>
                                                <select id="originDDL" name='originStockId' className="form-control" value={originStockId} onChange={onInputChangeMovement}>
                                                    <option value={-2}>Select the origin stock</option>
                                                    {everyStock.map((stock, index) => (
                                                        <option key={index} value={stock.id}>{stock.id}</option>
                                                    ))}
                                                </select>
                                            </div>
                                            <div className="col-md-6">
                                                <label htmlFor="amountTxt" className="form-label">Amount</label>
                                                <input type="number" className="form-control" id="amountTxt" name="stockAmount" minLength={1} required value={stockAmount} pattern='^\d+(\.\d+)?$' onChange={onInputChangeMovement} />
                                            </div>
                                        </div>
                                    </div>
                                );
                            case 3:
                                return (
                                    <div style={{ margin: "30px" }}>
                                        <div className="row mb-3">
                                            <h4>Movement</h4>
                                            <p hidden>{newMovement.targetStockId = 0}</p>
                                            <div className="col-md-6">
                                                <label htmlFor="originDDL" className="form-label">Origin stock</label>
                                                <select id="originDDL" name='originStockId' className="form-control" value={originStockId} onChange={onInputChangeMovement}>
                                                    <option value={-2}>Select the origin stock</option>
                                                    {everyStock.map((stock, index) => (
                                                        <option key={index} value={stock.id}>{stock.id}</option>
                                                    ))}
                                                </select>
                                            </div>
                                            <div className="col-md-6">
                                                <label htmlFor="amountTxt" className="form-label">Amount</label>
                                                <input type="number" className="form-control" id="amountTxt" name="stockAmount" minLength={1} required value={stockAmount} onChange={onInputChangeMovement} />
                                            </div>
                                        </div>
                                        <div className="col-md-6 mx-auto">
                                            <h4>New Target Stock</h4>
                                            <label htmlFor="storageIdTxt" className="form-label">Storage id</label>
                                            <input type="number" className="form-control" id="storageIdTxt" name="storageId" minLength={1} required value={storageId} onChange={onInputChangeStock} />
                                        </div>
                                    </div>
                                );
                            case 4:
                                return (
                                    <div style={{ margin: "30px" }}>
                                        <div className="row mb-3">
                                            <h4>Movement</h4>
                                            <div className="col-md-6">
                                                <label htmlFor="originDDL" className="form-label">Origin stock</label>
                                                <select id="originDDL" name='originStockId' className="form-control" value={originStockId} onChange={onInputChangeMovement}>
                                                    <option value={-2}>Select the origin stock</option>
                                                    {everyStock.map((stock, index) => (
                                                        <option key={index} value={stock.id}>{stock.id}</option>
                                                    ))}
                                                </select>
                                            </div>
                                            <div className="col-md-6">
                                                <label htmlFor="targetDDL" className="form-label">Target stock</label>
                                                <select id="targetDDL" name='targetStockId' className="form-control" value={targetStockId} onChange={onInputChangeMovement}>
                                                    <option value={-2}>Select the target stock</option>
                                                    {everyStock.map((stock, index) => (
                                                        <option key={index} value={stock.id}>{stock.id}</option>
                                                    ))}
                                                </select>
                                            </div>
                                        </div>
                                        <div className="col-md-6 mx-auto">
                                            <label htmlFor="amountTxt" className="form-label">Amount</label>
                                            <input type="number" className="form-control" id="amountTxt" name="stockAmount" minLength={1} required value={stockAmount} pattern='^\d+(\.\d+)?$' onChange={onInputChangeMovement} />
                                        </div>
                                    </div>
                                );
                            default:
                                return null;
                        }
                    })()
                }

                {(originStockId > 0) ? (renderStock(originStockId)) : ("")}

                {(targetStockId > 0) ? (renderStock(targetStockId)) : ("")}

                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: Backend system is down
                    </div>
                )}
                {
                    (actionType === -1) ? ("") : (
                        <div className='text-center'>
                            <button type="submit" className="btn btn-success"  >Add</button>
                            <button style={{ marginLeft: "30px" }} type="button" className="btn btn-warning" onClick={() => window.location.reload()}  >Reload</button>
                            <Link style={{ marginLeft: "30px" }} className="btn btn-primary" to="/admin/movements" >Go back</Link>
                        </div>
                    )
                }

            </form>
        </div>

    )
}
