package com.practica.ventasmoviles.sys.viewModel.categorias

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.ventasmoviles.data.datasource.repository.Repository
import com.practica.ventasmoviles.data.domain.AddCategoriaUseCase
import com.practica.ventasmoviles.data.domain.AddProductUseCase
import com.practica.ventasmoviles.data.domain.UpdateCategoriaUseCase
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import com.practica.ventasmoviles.sys.ui.view.CategoriaFragment
import com.practica.ventasmoviles.sys.ui.view.ProductFragment
import kotlinx.coroutines.launch

class RegisterCategoriaModelView:ViewModel() {

    var errorMessageCategoria = MutableLiveData<CategoriaErrorMessage>()
    var categorias = MutableLiveData<List<CategoriaEntity>>()
    var fragment = MutableLiveData<Fragment>()
    val registerValidation = CategoriaRegisterValidate()
    val repository =  Repository()

    fun validateCategoria(categoria: CategoriaEntity, editFlag:Boolean){
        val currentErrorMessage = registerValidation.validateProduct(categoria)
        errorMessageCategoria.postValue(currentErrorMessage)
        if(currentErrorMessage.status){
            if (!editFlag){
                addNewProduct(categoria)
            }else{
                println(categoria.id)
                edit(categoria)
            }
            fragment.postValue(CategoriaFragment())
        }
    }

    private fun addNewProduct(categoria: CategoriaEntity){
        val addCategoriaUseCase= AddCategoriaUseCase()
        viewModelScope.launch {
            addCategoriaUseCase.addCategoria(categoria)
            val currentCategoria = repository.getAllCategorias()
            categorias.postValue(currentCategoria)
        }
    }

    fun edit(categoria: CategoriaEntity){
        val addCategoriaUseCase= UpdateCategoriaUseCase()
        viewModelScope.launch {
            addCategoriaUseCase.updateCategoria(categoria)
            val currentCategoria = repository.getAllCategorias()
            categorias.postValue(currentCategoria)
        }
    }

}