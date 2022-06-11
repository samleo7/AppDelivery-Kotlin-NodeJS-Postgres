package com.example.delivery.routes

import com.example.delivery.models.Category
import com.example.delivery.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutes {

    @GET("categories/getAll")
    fun getAll(
        @Header("Authotization") token: String
    ):Call<ArrayList<Category>>

    @Multipart
    @POST("categories/create")
    fun create(
        @Part image: MultipartBody.Part,
        @Part("category") category: RequestBody,  // category tiene que ser lo mismo que el controller
        @Header("Authorization") token:String
    ): Call<ResponseHttp>

}