package com.example.delivery.models

import com.google.gson.Gson

class Category (
    val id: String? = null,
    val name: String,
    val image:String? = null
    ){

    override fun toString(): String {
        return name
    }

    //Transformado este usuario a un objeto de tipo JSON
    fun toJson(): String{
        return Gson().toJson(this)
    }
}