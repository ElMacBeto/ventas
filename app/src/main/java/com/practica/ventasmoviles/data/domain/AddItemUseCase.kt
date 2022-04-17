package com.practica.ventasmoviles.data.domain

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.practica.ventasmoviles.sys.ui.view.ProductFragment
import com.practica.ventasmoviles.sys.ui.view.RegistrarCategoriaFragment
import com.practica.ventasmoviles.sys.ui.view.RegistrarProductoFragment

class AddItemUseCase {

    fun selectObjetcToAdd(item: FragmentManager):String{
        var addItem= ""
        if(item.findFragmentByTag("productos")?.isVisible == true){
            addItem="productos"
        }
        if(item.findFragmentByTag("categoria")?.isVisible == true){
            addItem="categoria"
        }

        return addItem
    }

    fun getAddItemFragment(item:String):Fragment{
        return when(item){
            "productos"-> RegistrarProductoFragment()
            "categoria"-> RegistrarCategoriaFragment()
            else -> ProductFragment()
        }
    }




}