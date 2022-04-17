package com.practica.ventasmoviles.sys.viewModel.categorias

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.domain.DeleteCategoriaUseCase
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import kotlinx.coroutines.launch

class CategoriaViewModel: ViewModel() {

    var categorias = MutableLiveData<List<CategoriaEntity>>()
    var db = MainApplication.database.categoriaDao()
    //var fragment = MutableLiveData<Fragment>()
    var id = MutableLiveData<Int>()

    fun eliminarCategoria(currentCategoria: CategoriaEntity){
        val deleteCategoriaUseCase= DeleteCategoriaUseCase()
        viewModelScope.launch {
            deleteCategoriaUseCase.deleteProduct(currentCategoria)
            val currentProducts = db.getAllCategoria()
            categorias.postValue(currentProducts)
            println("eliminar editar " +currentCategoria.id)
        }
    }

    fun verDetallesCategoria(id:Int){
        println("eliminar detalles " +id)
    }

    fun editarCategoria(id:Int){
        changeEditFragment(id)
    }

    fun changeEditFragment(currentId:Int){
        id.postValue(currentId)
    }
}