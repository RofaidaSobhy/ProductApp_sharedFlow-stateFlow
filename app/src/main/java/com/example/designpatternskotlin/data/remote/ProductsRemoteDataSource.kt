package com.example.designpatternskotlin.data.remote

import com.example.designpatternskotlin.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource{
    suspend fun getAllProducts():Flow<List<Product>?>
}