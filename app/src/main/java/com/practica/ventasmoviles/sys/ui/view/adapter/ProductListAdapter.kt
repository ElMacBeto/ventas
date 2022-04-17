package com.practica.ventasmoviles.sys.ui.view.adapter
import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practica.ventasmoviles.R
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity


class ProductListAdapter() :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>(), View.OnCreateContextMenuListener {

    private var productsList= emptyList<ProductosEntity>()
    @SuppressLint("NotifyDataSetChanged")

    fun setListProducts(products: List<ProductosEntity>){
        this.productsList = products
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    private var position:Int = 0

    fun getPosition():Int{
        return position
    }

    fun setPosition(position: Int){
        this.position=position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView
        val categoria: TextView
        val marca: TextView

        init {
            // Define click listener for the ViewHolder's View.
            nombre = view.findViewById(R.id.nombre_label)
            categoria = view.findViewById(R.id.categoria_label)
            marca = view.findViewById(R.id.marca_label)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_producto, viewGroup, false)

        view.setOnCreateContextMenuListener(this)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nombre.text = productsList[position].nombre
        viewHolder.categoria.text = productsList[position].categoria
        viewHolder.marca.text = productsList[position].marca
        viewHolder.itemView.setOnLongClickListener(OnLongClickListener {
            setPosition(position)
            false
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = productsList.size

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle(null);
        menu.add(0, view!!.getId(), 0, "Ver detalles");//groupId, itemId, order, title
        menu.add(0, view.getId(), 0, "Eliminar")
        menu.add(0, view.getId(), 0, "Editar");
    }

}