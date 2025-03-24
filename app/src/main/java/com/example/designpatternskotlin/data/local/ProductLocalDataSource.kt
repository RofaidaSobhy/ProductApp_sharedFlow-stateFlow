package com.example.designpatternskotlin.data.local

import com.example.designpatternskotlin.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun deleteProduct(product: Product?): Int


}