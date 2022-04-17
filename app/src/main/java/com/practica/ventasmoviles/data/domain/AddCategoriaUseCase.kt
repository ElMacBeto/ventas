package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCategoriaUseCase {
    val db = MainApplication.database.categoriaDao()

    suspend fun addCategoria(categoria:CategoriaEntity): Long {
        return withContext(Dispatchers.IO) {
            db.addCategoria(categoria)
        }
    }
}