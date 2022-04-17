package com.practica.ventasmoviles.sys.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practica.ventasmoviles.MainApplication
import com.practica.ventasmoviles.R
import com.practica.ventasmoviles.data.entities.CategoriaEntity
import com.practica.ventasmoviles.data.entities.ProductosEntity
import com.practica.ventasmoviles.databinding.FragmentRegistrarProductoBinding
import com.practica.ventasmoviles.sys.viewModel.productos.ErrorMessage
import com.practica.ventasmoviles.sys.viewModel.productos.RegistrarProductoViewModel
import java.io.File
import java.io.IOException
import java.util.*

private const val ARG_PARAM1 = "id"

class RegistrarProductoFragment : Fragment() {

    private var _binding:FragmentRegistrarProductoBinding? = null
    private val binding get() = _binding!!
    private val registrarProductoViewModel: RegistrarProductoViewModel by viewModels()
    val db = MainApplication.database.productoDao()
    val dbCategoria = MainApplication.database.categoriaDao()
    private var id: Int? = 0
    var editFlag = false
    var initProduct = ProductosEntity(0,"","",0f,0f,0f,"", "","", "" ,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_PARAM1,1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrarProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<CoordinatorLayout>(R.id.appbar)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.register_button)?.visibility = View.GONE

        if(id!=0) {
            editFlag = true
            setEditValue()
        }

        initOptionsRegisterField()

        registrarProductoViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer { errormessage->
            setErrorMessage(errormessage)
        })

        registrarProductoViewModel.fragment.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            changeFragment(it)
        })

        binding.saveBtn.setOnClickListener{
            val producto = getDataProducto()
            producto.id = initProduct.id
            registrarProductoViewModel.validateProduct(producto,editFlag)
        }

        binding.imageField.setOnClickListener{
            showDialog()
        }

    }

    private fun changeFragment(fragment:Fragment){
        val transition = parentFragmentManager
        val fragmentTransition =transition.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,fragment, "productos")
        fragmentTransition.commit()
    }

    private fun initOptionsRegisterField(){
        val categorias = dbCategoria.getAllCategoria()
        val listCategorias = mutableListOf<String>()
        categorias.map {
            listCategorias.add(it.name!!)
        }
        val itemsUnidades = listOf("Material", "Design", "Components", "Android")
        val adapterCategory = ArrayAdapter(requireContext(), R.layout.list_item_options, listCategorias)
        (binding.categoriaField.editText as? AutoCompleteTextView)?.setAdapter(adapterCategory)
        val adapterUnidadMedida = ArrayAdapter(requireContext(), R.layout.list_item_options, itemsUnidades)
        (binding.unidadMedidaField.editText as? AutoCompleteTextView)?.setAdapter(adapterUnidadMedida)
    }

    fun getDataProducto():ProductosEntity{
        return ProductosEntity(0,
            PhotoPath,
            binding.nameField.text.toString(),
            binding.costoField.text.toString().toFloatOrNull(),
            binding.precioMenuField.text.toString().toFloatOrNull(),
            binding.precioMayoField.text.toString().toFloatOrNull(),
            binding.categoriaValue.text.toString(),
            binding.marcaField.text.toString(),
            binding.colorField.text.toString(),
            binding.unidadMedidaValue.text.toString(),
            binding.cantidadMinField.text.toString().toIntOrNull(),
        )
    }

    fun setErrorMessage(errorMessage: ErrorMessage){
        binding.nameField.error = errorMessage.name
        binding.costoField.error = errorMessage.costo
        binding.categoriaField.error = errorMessage.categoria
        binding.precioMayoField.error = errorMessage.precioMayoreo
        binding.precioMenuField.error = errorMessage.precioMenudeo
        binding.marcaField.error = errorMessage.marca
        binding.colorField.error = errorMessage.color
        binding.unidadMedidaField.error = errorMessage.unidadDeMedida
        binding.cantidadMinField.error = errorMessage.cantidad
    }

    @SuppressLint("SetTextI18n")
    fun setEditValue(){
        val initProducto =  db.getAllProductos()
        initProducto.map {
            if (it.id==id) initProduct=it
        }
        binding.saveBtn.text = "editar"
        binding.nameField.setText(initProduct.nombre)
        binding.costoField.setText(initProduct.costo.toString())
        binding.precioMenuField.setText(initProduct.precioMenudeo.toString())
        binding.precioMayoField.setText(initProduct.precioMayoreo.toString())
        binding.marcaField.setText(initProduct.marca)
        binding.colorField.setText(initProduct.color)
        binding.cantidadMinField.setText(initProduct.cantidadMin.toString())

        val file = File(initProduct.imagen!!)
        if (file?.exists()!!){
            val bitmap: Bitmap = BitmapFactory.decodeFile(initProduct.imagen)
            binding.imageField.setImageBitmap(bitmap)
        }else{
            val uri = initProduct.imagen!!.toUri()
            val sourceFile = DocumentFile.fromSingleUri(requireContext(), uri)
            if (sourceFile!!.exists()) {
                binding.imageField.setImageURI(uri)
            }
        }

    }

    val PICK_IMAGE = 2
    fun selectPictureGalery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    //takepicture
    val REQUEST_IMAGE_CAPTURE = 1
    var PhotoPath=""
    @RequiresApi(Build.VERSION_CODES.N)
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            activity?.let {
                takePictureIntent.resolveActivity(it.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri? = context?.let { it1 ->
                            FileProvider.getUriForFile(
                                it1,
                                "com.practica.android.fileprovider",
                                it
                            )
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val picture = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            if (absolutePath != null){
                 PhotoPath = absolutePath
            }
        }
        return picture
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap: Bitmap = BitmapFactory.decodeFile(PhotoPath)
            //val imageScaled = Bitmap.createScaledBitmap(bitmap, 550, 400, false)
            val matrix = Matrix()
            matrix.postRotate(90F)
            var widthImg = 0.0
            var hightImg = 0.0
            if (bitmap.width > bitmap.height){
                widthImg= bitmap.width*0.6
                hightImg = bitmap.height*1.0
            }else{
                widthImg = bitmap.width*1.0
                hightImg = bitmap.height*0.6
                matrix.postRotate(-90F)
            }
            val rotateBitmap = Bitmap.createBitmap(bitmap,0,0,widthImg.toInt(),hightImg.toInt(), matrix,true)
            val imageScaled = Bitmap.createScaledBitmap(rotateBitmap, 550, 400, false)
            //Toast.makeText(context, PhotoPath, Toast.LENGTH_SHORT).show()
            binding.imageField.setImageBitmap(imageScaled)
        }

        if (requestCode == PICK_IMAGE&& resultCode == Activity.RESULT_OK){
            val imageUri : Uri? = data?.data
            println("uri $imageUri")
            //val sourceFile = DocumentFile.fromSingleUri(requireContext(), imageUri!!)
            PhotoPath = imageUri?.toString()!!
            binding.imageField.setImageURI(imageUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("seleccione fotografia")
        builder.setCancelable(true)
        builder.setPositiveButton("tomar foto"){ dialog, _ ->
            dispatchTakePictureIntent()
            dialog.dismiss()
        }
        builder.setNegativeButton("buscar en galeria"){dialog, _ ->
            selectPictureGalery()
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()

    }

}