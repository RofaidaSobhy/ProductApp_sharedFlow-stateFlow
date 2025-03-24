package com.example.designpatternskotlin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.designpatternskotlin.data.models.Product

@Database(entities = [Product::class] , version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao

    companion object{
        @Volatile
        private var instanceOfDB : ProductDatabase? = null
        fun getInstance(context: Context): ProductDatabase{
            return instanceOfDB ?: synchronized(this){
                val temp: ProductDatabase = Room.databaseBuilder(context,ProductDatabase::class.java,"productdb").build()
                instanceOfDB = temp
                temp
            }

        }
    }
}