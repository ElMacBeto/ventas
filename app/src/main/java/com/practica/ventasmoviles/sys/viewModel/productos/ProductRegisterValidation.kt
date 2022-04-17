package com.practica.ventasmoviles.sys.viewModel.productos

import com.practica.ventasmoviles.data.entities.ProductosEntity

class ProductRegisterValidation {

    fun validateProduct(producto: ProductosEntity): ErrorMessage {
        var currentErrorMessage = ErrorMessage()

        if(!validateName(producto.nombre!!)){
            currentErrorMessage.name = "el nombre debe de tener minimo 5 caracteres maximo 70"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.name=null
            currentErrorMessage.status=true
        }

        if(validateCosto(producto.costo)){
            currentErrorMessage.costo = null
            currentErrorMessage.status=true
        }else {
            currentErrorMessage.costo = "obligatorio"
            currentErrorMessage.status=false
        }

        if(validateSelectedOption(producto.categoria!!)){
            currentErrorMessage.categoria = "Seleccione un opción"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.categoria = null
            currentErrorMessage.status=true
        }

        if(!validateCosto(producto.precioMenudeo)){
            currentErrorMessage.precioMenudeo = "obligatorio"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.precioMenudeo = null
            currentErrorMessage.status=false
        }

        if(!validateCosto(producto.precioMayoreo)){
            currentErrorMessage.precioMayoreo = "obligatorio"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.precioMayoreo = null
            currentErrorMessage.status=true
        }

        if(!validateMarca(producto.marca!!)){
            currentErrorMessage.marca = "marca debe de tener minimo 3 caracteres maximo 40"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.marca = null
            currentErrorMessage.status=true
        }

        if(!validateMarca(producto.color!!)){
            currentErrorMessage.color = "color debe de tener minimo 3 caracteres maximo 40"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.color = null
            currentErrorMessage.status=true
        }

        if(validateSelectedOption(producto.unidadMedida!!)){
            currentErrorMessage.unidadDeMedida = "Seleccione un opción"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.unidadDeMedida = null
            currentErrorMessage.status=true
        }
        if(producto.cantidadMin==null){
            currentErrorMessage.cantidad = "Seleccione un opción"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.cantidad = null
            currentErrorMessage.status=true
        }

        return currentErrorMessage
    }

    private fun validateName(name:String): Boolean {
        return name.length in 5..68
    }

    private fun validateCosto(costo: Float?): Boolean {
        val newcosto:Float
        newcosto = if(costo != null) costo else 0f
        return newcosto > 0.1f
    }

    private fun validateMarca(marca:String):Boolean{
        return marca.length in 3..40
    }

    private fun validateSelectedOption(option:String):Boolean{
        return option.isEmpty()
    }
}