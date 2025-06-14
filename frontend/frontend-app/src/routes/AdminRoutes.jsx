import React from 'react'

import AdminMenu from '../admin/menus/AdminMenu';
import ProductMenu from '../admin/menus/ProductMenu';
import Products from '../admin/products/Products';
import AddProduct from '../admin/products/AddProduct';
import EditProduct from '../admin/products/EditProduct';
import DeleteProduct from '../admin/products/DeleteProduct';
import Batches from '../admin/batches/Batches';
import AddBatch from '../admin/batches/AddBatch';
import EditBatch from '../admin/batches/EditBatch';
import DeleteBatch from '../admin/batches/DeleteBatch';
import Categories from '../admin/categories/Categories';
import AddCategory from '../admin/categories/AddCategory';
import EditCategory from '../admin/categories/EditCategory';
import DeleteCategory from '../admin/categories/DeleteCategory';
import Storages from '../admin/storages/Storages';
import AddStorage from '../admin/storages/AddStorage';
import EditStorage from '../admin/storages/EditStorage';
import DeleteStorage from '../admin/storages/DeleteStorage';
import Stock from '../admin/stock/Stock';
import DeleteStock from '../admin/stock/DeleteStock';
import Movements from '../admin/movements/Movements';
import ReciveMovement from '../admin/movements/ReciveMovement';
import AddMovement from '../admin/movements/AddMovement';

import { Navigate, Route, Routes } from 'react-router-dom'

export default function AdminRoutes() {
    const isAllowed = true;//localStorage.getItem('rol') === 'admin';
    if (!isAllowed) {
        localStorage.setItem('jwtToken', '');
        localStorage.setItem('rol', '');
        return <Navigate to='/login' replace />;
    }
    return (
        <Routes>
            <Route path='menu' element={<AdminMenu />} />
            <Route path='products/menu' element={<ProductMenu />} />

            <Route path='/products' element={<Products />} />
            <Route path='/products/add' element={<AddProduct />} />
            <Route path='/products/edit/:idProduct' element={<EditProduct />} />
            <Route path='/products/delete/:idProduct' element={<DeleteProduct />} />

            <Route path='/batches' element={<Batches />} />
            <Route path='/batches/add' element={<AddBatch />} />
            <Route path='/batches/edit/:idBatch' element={<EditBatch />} />
            <Route path='/batches/delete/:idBatch' element={<DeleteBatch />} />

            <Route path='/categories' element={<Categories />} />
            <Route path='/categories/add' element={<AddCategory />} />
            <Route path='/categories/edit/:idCategory' element={<EditCategory />} />
            <Route path='/categories/delete/:idCategory' element={<DeleteCategory />} />

            <Route path='/storages' element={<Storages />} />
            <Route path='/storages/add' element={<AddStorage />} />
            <Route path='/storages/edit/:idStorage' element={<EditStorage />} />
            <Route path='/storages/delete/:idStorage' element={<DeleteStorage />} />

            <Route path='/stock' element={<Stock />} />
            <Route path='/stock/delete/:idStock' element={<DeleteStock />} />

            <Route path='/movements' element={<Movements />} />
            <Route path='/movements/add' element={<AddMovement />} />
            <Route path='/movements/recive/:idMovement' element={<ReciveMovement />} />











        </Routes>
    )
}
