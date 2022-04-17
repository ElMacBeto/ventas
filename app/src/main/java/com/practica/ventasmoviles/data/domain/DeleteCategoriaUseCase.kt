package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteCategoriaUseCase {
    val db = MainApplication.database.categoriaDao()

    suspend fun deleteProduct(categoria: CategoriaEntity){
        return withContext(Dispatchers.IO) {
            db.deleteCategoria(categoria)
        }
    }
}