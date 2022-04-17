package com.practica.ventasmoviles.data.domain

import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateCategoriaUseCase {

    val db = MainApplication.database.categoriaDao()

    suspend fun updateCategoria(categoria: CategoriaEntity) {
        return withContext(Dispatchers.IO) {
            db.updateCategoria(categoria)
        }
    }
}