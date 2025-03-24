package com.example.designpatternskotlin.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ProductAPIService {
    @GET("products")
    suspend fun getAllProducts(): Response<ProductResponse>
}