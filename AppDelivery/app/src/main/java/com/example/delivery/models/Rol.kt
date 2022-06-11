package com.example.delivery.models

import com.google.gson.annotations.SerializedName

//Los atributos tienes que ser lo mismo que la BD postgres
class Rol (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("route") val route: String
) {

    override fun toString(): String {
        return "Rol(id='$id', rol='$name', image='$image', route='$route')"
    }

}