package com.practica.ventasmoviles.sys.viewModel.categorias

import com.practica.ventasmoviles.data.entities.CategoriaEntity

class CategoriaRegisterValidate {

    fun validateProduct(categoria: CategoriaEntity): CategoriaErrorMessage {
        var currentErrorMessage = CategoriaErrorMessage()

        if(!notEmptyValidate(categoria.name!!)){
            currentErrorMessage.name = "debe llenar el campo"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.name=null
            currentErrorMessage.status=true
        }
        if(!notEmptyValidate(categoria.description!!)){
            currentErrorMessage.description = "debe llenar el campo"
            currentErrorMessage.status=false
        }else {
            currentErrorMessage.description=null
            currentErrorMessage.status=true
        }
        return currentErrorMessage
    }

    private fun notEmptyValidate(string:String):Boolean{
        return string.isNotEmpty()
    }

}