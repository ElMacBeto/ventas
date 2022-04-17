package com.practica.ventasmoviles.data.datasource.repository
import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    val db = MainApplication.database.productoDao()
    val dbCategoria = MainApplication.database.categoriaDao()

    suspend fun getAllProducts(): List<ProductosEntity> {
        return withContext(Dispatchers.IO) {
            db.getAllProductos()
        }
    }
    suspend fun getAllCategorias(): List<CategoriaEntity> {
        return withContext(Dispatchers.IO) {
            dbCategoria.getAllCategoria()
        }
    }

}