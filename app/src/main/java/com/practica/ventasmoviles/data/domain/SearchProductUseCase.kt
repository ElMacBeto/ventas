package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.Repository.ListProduct
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchProductUseCase {

    val db = MainApplication.database.productoDao()

    suspend fun searchProduct(query: String){
         withContext(Dispatchers.IO) {
            val list = db.getProductByField(query)
             if (list != null) {
                 ListProduct.listProduct = list
             }
        }
    }
}