import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Navigation from "./shared/Navigation";
import Login from "./shared/Login";
import Register from "./shared/Register";
import AdminRoutes from "./routes/AdminRoutes";
import ProtectedRoute from "./manager/ProtectedRoute";


function App() {
  return (
    <div className="container-fluid text-center">
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route exact path="/login" element={<Login />} />
          <Route exact path="/register" element={<Register />}/>
          <Route path="/admin/*" element={<ProtectedRoute> <AdminRoutes /></ProtectedRoute>} />
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
