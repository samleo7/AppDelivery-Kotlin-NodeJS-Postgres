package com.example.delivery.routes

import com.example.delivery.models.Category
import com.example.delivery.models.Product
import com.example.delivery.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes {

    @GET("products/findByCategory/{id_category}")
    fun findByCategory(
        @Path("id_category") idCategory: String,
        @Header("Authotization") token: String
    ): Call<ArrayList<Product>>

    @Multipart
    @POST("products/create")
    fun create(
        @Part image: Array<MultipartBody.Part?>,
        @Part("product") product: RequestBody,  // El string "product" tiene que ser lo mismo que el controller
        @Header("Authorization") token:String
    ): Call<ResponseHttp>
}