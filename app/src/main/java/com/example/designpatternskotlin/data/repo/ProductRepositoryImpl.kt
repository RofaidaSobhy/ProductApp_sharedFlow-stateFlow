package com.example.designpatternskotlin.data.repo

import androidx.collection.emptyLongSet
import com.example.designpatternskotlin.data.local.ProductLocalDataSource
import com.example.designpatternskotlin.data.local.ProductLocalDataSourceImpl
import com.example.designpatternskotlin.data.models.Product
import com.example.designpatternskotlin.data.remote.ProductsRemoteDataSource
import com.example.designpatternskotlin.data.remote.ProductsRemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl private constructor(
    private val remoteDataSource: ProductsRemoteDataSource
    ,private val localDataSource: ProductLocalDataSource
) :ProductRepository{
    override suspend fun getAllProducts(isOnline :Boolean): Flow<List<Product>?>{
        return if(isOnline) {
            remoteDataSource.getAllProducts()
        }
        else{
               localDataSource.getAllProducts()
        }
    }

   override suspend fun addProduct(product: Product):Long{
        return localDataSource.insertProduct(product)
    }

    override suspend fun removeProduct(product: Product):Int{
        return localDataSource.deleteProduct(product)
    }

    companion object{
        private var INSTANCE : ProductRepositoryImpl? = null
        fun getInstance(remoteDataSource: ProductsRemoteDataSource,
                        localDataSource: ProductLocalDataSource): ProductRepository{
            return INSTANCE ?: synchronized(this){
                val temp = ProductRepositoryImpl(remoteDataSource,localDataSource)
                INSTANCE = temp
                temp
            }
        }
    }

}