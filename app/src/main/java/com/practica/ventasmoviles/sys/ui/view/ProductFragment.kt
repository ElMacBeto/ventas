package com.practica.ventasmoviles.sys.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.R
import com.practica.ventasmoviles.data.entities.ProductosEntity
import com.practica.ventasmoviles.databinding.FragmentMainMenuBinding
import com.practica.ventasmoviles.sys.ui.view.adapter.ProductListAdapter
import com.practica.ventasmoviles.sys.viewModel.productos.ProductViewModel
import androidx.appcompat.widget.SearchView

class ProductFragment : Fragment() {

    private var _binding:FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    private val mainMenuFragmentViewModel: ProductViewModel by viewModels()
    private var productList = emptyList<ProductosEntity>()
    private lateinit var adapter: ProductListAdapter
    val db = MainApplication.database.productoDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<MaterialToolbar>(R.id.topAppBar)?.title = "Productos"
        productList = db.getAllProductos()
        initRecyclerView()

        mainMenuFragmentViewModel.products.observe(viewLifecycleOwner, Observer { currentProductList ->
            adapter.setListProducts(currentProductList)
            productList = currentProductList
        })

        mainMenuFragmentViewModel.id.observe(viewLifecycleOwner, Observer {
            changeEditFragment(it)
        })

        activity?.findViewById<SearchView>(R.id.search)?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=""){
                    query?.let { mainMenuFragmentViewModel.search(it) }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText==""){
                    adapter.setListProducts(db.getAllProductos())
                }
                return true
            }
        })

    }

    fun initRecyclerView(){
        adapter = ProductListAdapter()
        adapter.setListProducts(productList)
        binding.rvProducts.layoutManager = LinearLayoutManager(parentFragment?.context)
        binding.rvProducts.adapter = adapter
    }

    fun changeEditFragment(idm: Int){
        val bundle =Bundle()
        bundle.putInt("id", idm)
        val fragment = RegistrarProductoFragment()
        fragment.arguments = bundle
        val transition = parentFragmentManager
        transition.popBackStack()
        val fragmentTransition =transition.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }

    /*opciones de la tarjeta del producto, ver detalles, eliminar, editar*/
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = adapter.getPosition()
        val producto = productList[position]
        return when (item.title) {
            "Ver detalles" -> {

                mainMenuFragmentViewModel.verDetallesProduct(producto.id)
                true
            }
            "Eliminar" -> {
                showDialog(producto)
                true
            }
            "Editar" -> {
                //mainMenuFragmentViewModel.editarProducto(producto.id)
                changeEditFragment(producto.id)
                true
            }
            else -> false
        }
    }

    private fun showDialog(producto: ProductosEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("eliminar categoria")
        builder.setCancelable(true)
        builder.setPositiveButton("si"){ dialog, _ ->
            mainMenuFragmentViewModel.eliminarProducto(producto)
            dialog.dismiss()
        }
        builder.setNegativeButton("no"){dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    override fun onResume() {
        activity?.findViewById<CoordinatorLayout>(R.id.appbar)?.visibility = View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.register_button)?.visibility = View.VISIBLE
        super.onResume()
    }


}