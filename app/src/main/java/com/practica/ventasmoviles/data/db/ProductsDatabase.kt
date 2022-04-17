package com.practica.ventasmoviles.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practica.ventasmoviles.data.datasource.database.dao.CategoriaDao
import com.practica.ventasmoviles.data.datasource.database.dao.ProductoDao
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity

@Database(entities = [ProductosEntity::class, CategoriaEntity::class], version = 3)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun categoriaDao():CategoriaDao
}