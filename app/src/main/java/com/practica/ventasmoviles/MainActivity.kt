package com.practica.ventasmoviles

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.practica.ventasmoviles.databinding.ActivityMainBinding
import com.practica.ventasmoviles.sys.ui.view.CategoriaFragment
import com.practica.ventasmoviles.sys.ui.view.ProductFragment
import com.practica.ventasmoviles.sys.viewModel.MainViewModel
import com.practica.ventasmoviles.sys.viewModel.VisibleFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    val transition = supportFragmentManager
    val db = MainApplication.database.productoDao()
    private lateinit var fragmento:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(VisibleFragment(CategoriaFragment(),"categoria"))

        /**cambia las vistas de acuerdo a las opciones del menu principal**/
        mainViewModel.visibleFragment.observe(this, Observer { visibleFragment->
            changeFragment(visibleFragment)
            println(visibleFragment.tag)
        })

        mainViewModel.addItemFragment.observe(this, Observer { addItemFragment->
            changeAddItemFragment(addItemFragment)
        })

        /**atiende el boton de "agregar" dependiendo de la vista que este visible**/
        binding.registerButton.setOnClickListener{ mainViewModel.changeAddItemFragment(supportFragmentManager) }
        /**atiende las opciones de filtar y busca de la appbar**/
        binding.topAppBar.setOnMenuItemClickListener {  menuItem -> setOnClickItems(menuItem) }
        /**atiende las opciones del menu principal en la appbar**/
        binding.topAppBar.setNavigationOnClickListener { setOnClickMenu() }
    }

    private fun changeFragment(visibleFragment: VisibleFragment){
        val fragmentTransition =transition.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,visibleFragment.fragment, visibleFragment.tag)
        fragmentTransition.commit()
    }

    /**cambia el fragment de agregar item en cualquier vista del menu principal**/
    private fun changeAddItemFragment(fragment: Fragment){
        val fragmentTransition =transition.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }

    /** accion del menu de principal muestra las opciones: -categoria, productos,
     compras, ventas, mis clientes e inventario **/
    private fun setOnClickMenu(){
        val popupMenu = PopupMenu(this,binding.topAppBar)
        popupMenu.menuInflater.inflate(R.menu.main_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.categoria ->{
                    val visibleFragment = VisibleFragment(CategoriaFragment(),"categoria")
                    mainViewModel.changeCategoriaFragment(visibleFragment)}
                R.id.productos ->{
                    val visibleFragment = VisibleFragment(ProductFragment(),"productos")
                    mainViewModel.changeProductFragment(visibleFragment)}
                R.id.compras ->
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                R.id.ventas ->
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                R.id.mis_clientes ->
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                R.id.inventario ->
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
            }
            true
        }
        popupMenu.show()
    }

    /**Selecciona una opcion del menu de appBar correspondiente a las opciones de filtrar y buscar**/
    private fun setOnClickItems(menuItem: MenuItem):Boolean{
        return when (menuItem.itemId) {
            R.id.filter -> {
                println("filtrar ")
                true
            }
            else -> false
        }

    }

    override fun onBackPressed() {
        val categoria = supportFragmentManager.findFragmentByTag("categoria")
        val productos = supportFragmentManager.findFragmentByTag("productos")
        if(categoria?.isVisible == false && productos?.isVisible != false){

        }

        super.onBackPressed()
    }

}