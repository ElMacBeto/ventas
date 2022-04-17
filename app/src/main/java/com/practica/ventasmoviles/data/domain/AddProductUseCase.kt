package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddProductUseCase {
    val db = MainApplication.database.productoDao()

    suspend fun addProduct(newProduct:ProductosEntity): Long {
        return withContext(Dispatchers.IO) {
            db.addProduct(newProduct)
        }
    }

}