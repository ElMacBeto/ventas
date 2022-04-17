package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteProductUseCase {
    val db = MainApplication.database.productoDao()

    suspend fun deleteProduct(newProduct: ProductosEntity){
        return withContext(Dispatchers.IO) {
            db.deleteProducto(newProduct)
        }
    }
}