package com.practica.ventasmoviles.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria_entity")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val name:String?,
    val description:String?,
    val image:String?="")
