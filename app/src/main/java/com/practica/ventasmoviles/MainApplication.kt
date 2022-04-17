package com.practica.ventasmoviles

import android.app.Application
import androidx.room.Room
import com.practica.ventasmoviles.data.db.ProductsDatabase

class MainApplication: Application() {
    companion object {
        lateinit var database: ProductsDatabase
    }
    override fun onCreate() {
        super.onCreate()

        MainApplication.database =  Room.databaseBuilder(
            this, ProductsDatabase::class.java,
            "productos-db")
            .allowMainThreadQueries()
            .build()
    }
}