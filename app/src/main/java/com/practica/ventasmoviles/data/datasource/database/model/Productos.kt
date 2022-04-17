package com.practica.ventasmoviles.data.datasource.database.model

data class Productos(
                     val imagen:String,
                     val nombre:String,
                     val costo:Float,
                     val precioMenudeo:Float,
                     val precioMayoreo:Float,
                     val categoria:String,
                     val marca:String,
                     val color:String,
                     val unidadMedida:String,
                     val cantidadMin:Int)