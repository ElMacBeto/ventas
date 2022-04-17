package com.practica.ventasmoviles.sys.ui.view.adapter

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practica.ventasmoviles.R
import com.practica.ventasmoviles.data.entities.CategoriaEntity


class CategoriaListAdapter() :
    RecyclerView.Adapter<CategoriaListAdapter.ViewHolder>(), View.OnCreateContextMenuListener {

    private var productsList= emptyList<CategoriaEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListProducts(products: List<CategoriaEntity>){
        this.productsList = products
        notifyDataSetChanged()
    }

    private var position:Int = 0

    fun getPosition():Int{
        return position
    }

    fun setPosition(position: Int){
        this.position=position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val description: TextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.categoria_item)
            description = view.findViewById(R.id.descripcion_item)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_categoria, viewGroup, false)
        view.setOnCreateContextMenuListener(this)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = "Categoria: "+ productsList[position].name
        viewHolder.description.text ="Descripcion: "+ productsList[position].description

        viewHolder.itemView.setOnLongClickListener(View.OnLongClickListener {
            setPosition(position)
            false
        })
    }

    override fun getItemCount() = productsList.size

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle(null);
        menu.add(0, view!!.getId(), 0, "Ver imagen");//groupId, itemId, order, title
        menu.add(0, view.getId(), 0, "Eliminar")
        menu.add(0, view.getId(), 0, "Editar");
    }

}