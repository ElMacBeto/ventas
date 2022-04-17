package com.practica.ventasmoviles.data.datasource.database.dao

import androidx.room.*
import com.practica.ventasmoviles.data.entities.CategoriaEntity

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categoria_entity")
    fun getAllCategoria(): List<CategoriaEntity>

    @Query("SELECT * FROM categoria_entity WHERE id IN (:categoriaIds)")
    fun loadAllByIds(categoriaIds: IntArray): List<CategoriaEntity>

    @Insert
    fun addCategoria(categoria: CategoriaEntity):Long

    @Delete
    fun deleteCategoria(categoria: CategoriaEntity)

    @Update
    suspend fun updateCategoria(categoria: CategoriaEntity): Int
}