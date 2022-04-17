package com.practica.ventasmoviles.data.Repository

import androidx.annotation.WorkerThread
import com.practica.ventasmoviles.data.datasource.database.dao.ProductoDao
import com.practica.ventasmoviles.data.entities.ProductosEntity


class ProductsRepository(private val productoDao: ProductoDao) {

    val allWords = productoDao.getAllProductos()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(producto: ProductosEntity) {
        productoDao.addProduct(producto)
    }

}