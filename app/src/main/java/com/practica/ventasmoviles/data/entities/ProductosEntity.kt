package com.practica.ventasmoviles.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos_entity")
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val imagen:String?,
    val nombre:String?,
    val costo:Float?=0f,
    val precioMenudeo:Float?=0f,
    val precioMayoreo:Float?=0f,
    val categoria:String?,
    val marca:String?,
    val color:String?,
    val unidadMedida:String?,
    val cantidadMin:Int?)