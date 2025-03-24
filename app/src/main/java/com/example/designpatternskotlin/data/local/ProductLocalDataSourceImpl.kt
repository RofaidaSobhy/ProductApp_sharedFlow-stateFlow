package com.example.designpatternskotlin.data.local

import com.example.designpatternskotlin.data.models.Product
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSourceImpl(private val dao : ProductDao):ProductLocalDataSource {
    override suspend fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllFavoriteProducts()
    }

    override suspend fun deleteProduct(product: Product?): Int {
    return if(product!=null)
            dao.deleteProduct(product)
        else
            -1
    }

    override suspend fun insertProduct(product: Product): Long {
        return dao.insertProduct(product)
    }

}