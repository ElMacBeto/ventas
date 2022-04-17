package com.practica.ventasmoviles.sys.viewModel.productos
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.data.Repository.ListProduct
import com.practica.ventasmoviles.data.datasource.repository.Repository
import com.practica.ventasmoviles.data.domain.DeleteProductUseCase
import com.practica.ventasmoviles.data.domain.SearchProductUseCase
import com.practica.ventasmoviles.data.entities.ProductosEntity
import com.practica.ventasmoviles.sys.ui.view.RegistrarProductoFragment
import kotlinx.coroutines.launch

class ProductViewModel:ViewModel() {


    var products = MutableLiveData<List<ProductosEntity>>()
    var registerProductFragment = RegistrarProductoFragment()
    var db = MainApplication.database.productoDao()
    var id = MutableLiveData<Int>()


    fun verDetallesProduct(id:Int){
        println("eliminar detalles " +id)

    }

    fun eliminarProducto(producto:ProductosEntity){
        val deleteProductUseCase= DeleteProductUseCase()
        viewModelScope.launch {
            deleteProductUseCase.deleteProduct(producto)
            //val currentProducts = repository.getAllProducts()
            val currentProducts = db.getAllProductos()
            products.postValue(currentProducts)
            println("eliminar editar " +producto.id)
        }
    }

    fun editarProducto(id:Int){
        changeEditFragment(id)
        println("eliminar editar " +id)
    }

    fun changeEditFragment(currentId:Int){
        id.postValue(currentId)
    }

    fun search(query:String){
        viewModelScope.launch {
            val searchProductUseCase= SearchProductUseCase()
            searchProductUseCase.searchProduct(query)
            val currentProducts = ListProduct.listProduct
            products.postValue(currentProducts)

        }

    }

}