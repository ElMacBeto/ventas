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
import com.practica.ventasmoviles.databinding.FragmentRegistrarCategoriaBinding
import com.practica.ventasmoviles.sys.viewModel.categorias.CategoriaErrorMessage
import com.practica.ventasmoviles.sys.viewModel.categorias.RegisterCategoriaModelView
import java.io.File
import java.io.IOException
import java.util.*


private const val ARG_PARAM1 = "id"

class RegistrarCategoriaFragment : Fragment() {

    private var _binding: FragmentRegistrarCategoriaBinding? = null
    private val binding get() = _binding!!
    private val registrarCategoriaViewModel: RegisterCategoriaModelView by viewModels()
    private var id: Int? = 0
    val db = MainApplication.database.categoriaDao()
    var editFlag = false
    private lateinit var initcategoria:CategoriaEntity

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
        _binding = FragmentRegistrarCategoriaBinding.inflate(inflater, container, false)
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
        }else initcategoria = CategoriaEntity(0,"","")

        registrarCategoriaViewModel.errorMessageCategoria.observe(viewLifecycleOwner, androidx.lifecycle.Observer { errormessage->
            setErrorMessage(errormessage)
        })

        registrarCategoriaViewModel.fragment.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            changeFragment(it)
        })

        binding.saveCategoriaBtn.setOnClickListener{
            val categoria:CategoriaEntity = getCategoria()

            categoria.id = initcategoria.id
            registrarCategoriaViewModel.validateCategoria(categoria, editFlag)
        }

        binding.imageField.setOnClickListener{
            showDialog()
        }

    }

    fun getCategoria():CategoriaEntity{
        return CategoriaEntity(0,
            binding.categoriaRegisterField.text.toString(),
            binding.descripcionCategoriaField.text.toString(),
            PhotoPath
        )
    }

    fun setErrorMessage(errorMessage: CategoriaErrorMessage){
        binding.categoriaRegisterField.error = errorMessage.name
        binding.descripcionCategoriaField.error = errorMessage.description
    }

    private fun changeFragment(fragment:Fragment){
        val transition = parentFragmentManager
        val fragmentTransition =transition.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,fragment,"categoria")
        fragmentTransition.commit()
    }

    @SuppressLint("SetTextI18n")
    fun setEditValue(){
        val initCategoria =  db.getAllCategoria()
        initcategoria = CategoriaEntity(0,"","")
        initCategoria.map {
            if (it.id==id) initcategoria=it
        }
        binding.title.setText("Editar Categoria")
        binding.saveCategoriaBtn.text = "editar"
        binding.categoriaRegisterField.setText(initcategoria.name)
        binding.descripcionCategoriaField.setText(initcategoria.description)

        val file = File(initcategoria.image!!)
        if (file?.exists()!!){
            val bitmap: Bitmap = BitmapFactory.decodeFile(initcategoria.image)
            binding.imageField.setImageBitmap(bitmap)
        }else{
            val uri = initcategoria.image!!.toUri()
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

    //take picture
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