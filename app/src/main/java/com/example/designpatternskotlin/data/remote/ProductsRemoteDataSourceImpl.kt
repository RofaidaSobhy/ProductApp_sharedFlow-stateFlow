package com.example.designpatternskotlin.data.remote

import com.example.designpatternskotlin.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductsRemoteDataSourceImpl(private val service: ProductAPIService) :
    ProductsRemoteDataSource {

    override suspend fun getAllProducts(): Flow<List<Product>?> {
        return flowOf( service.getAllProducts().body()?.products)

    }
}