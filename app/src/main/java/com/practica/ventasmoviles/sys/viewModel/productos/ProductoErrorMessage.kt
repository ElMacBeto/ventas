package com.practica.ventasmoviles.sys.viewModel.productos

data class ErrorMessage(
    var name: String? =null,
    var costo:String? =null,
    var categoria:String? =null,
    var precioMenudeo:String? =null,
    var precioMayoreo:String? =null,
    var marca:String? =null,
    var color:String? =null,
    var unidadDeMedida:String? =null,
    var cantidad:String? =null,
    var status:Boolean = false
)
