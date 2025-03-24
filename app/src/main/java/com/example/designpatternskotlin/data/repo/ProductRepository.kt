package com.example.designpatternskotlin.data.repo

import com.example.designpatternskotlin.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(isOnline :Boolean): Flow<List<Product>?>
    suspend fun addProduct(product: Product):Long
    suspend fun removeProduct(product: Product):Int
}