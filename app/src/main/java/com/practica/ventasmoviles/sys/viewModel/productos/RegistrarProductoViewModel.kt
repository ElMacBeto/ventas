package com.practica.ventasmoviles.sys.viewModel.productos

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.ventasmoviles.data.datasource.repository.Repository
import com.practica.ventasmoviles.data.domain.AddProductUseCase
import com.practica.ventasmoviles.data.domain.UpdateCategoriaUseCase
import com.practica.ventasmoviles.data.domain.UpdateProductUseCase
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import com.practica.ventasmoviles.sys.ui.view.ProductFragment
import kotlinx.coroutines.launch

class RegistrarProductoViewModel:ViewModel() {

    val repository =  Repository()
    var products = MutableLiveData<List<ProductosEntity>>()
    var errorMessage = MutableLiveData<ErrorMessage>()
    val registerValidation = ProductRegisterValidation()
    var fragment = MutableLiveData<Fragment>()

    fun validateProduct(producto:ProductosEntity,editFlag:Boolean){
        val currentErrorMessage = registerValidation.validateProduct(producto)
        errorMessage.postValue(currentErrorMessage)
        if(currentErrorMessage.status){
            if (!editFlag){
                addNewProduct(producto)
            }else{
                println(producto.id)
                edit(producto)
            }
            fragment.postValue(ProductFragment())
        }
    }

    private fun addNewProduct(producto:ProductosEntity){
        val addProductUseCase= AddProductUseCase()
        viewModelScope.launch {
            addProductUseCase.addProduct(producto)
            val currentProducts = repository.getAllProducts()
            products.postValue(currentProducts)
        }
    }

    fun edit(categoria: ProductosEntity){
        val addCategoriaUseCase= UpdateProductUseCase()
        viewModelScope.launch {
            addCategoriaUseCase.updateCategoria(categoria)
            val currentCategoria = repository.getAllProducts()
            products.postValue(currentCategoria)
        }
    }


}