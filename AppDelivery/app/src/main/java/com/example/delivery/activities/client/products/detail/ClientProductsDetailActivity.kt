package com.example.delivery.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.delivery.R
import com.example.delivery.models.Product
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientProductsDetailActivity : AppCompatActivity() {

    val TAG = "ProductsDe"

    var product:Product? = null
    val gson = Gson()

    var imageSlider: ImageSlider? = null
    var textViewName: TextView? = null
    var textViewDescription:TextView? = null
    var textViewPrice: TextView? = null
    var textViewCounter: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd: Button? = null

    var counter = 1
    var productPrice = 0.0

    var sharedPref:SharedPref? = null
    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        // Obteniendo los datos de los productos del Producto adapter
        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        sharedPref = SharedPref(this)

        imageSlider = findViewById(R.id.imageslider)
        textViewName = findViewById(R.id.textview_name)
        textViewDescription = findViewById(R.id.textview_description)
        textViewPrice = findViewById(R.id.textview_price)
        textViewCounter = findViewById(R.id.textview_counter)
        imageViewAdd = findViewById(R.id.imageview_add)
        imageViewRemove = findViewById(R.id.imageview_remove)
        buttonAdd = findViewById(R.id.btn_add_product)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        imageSlider?.setImageList(imageList)

        textViewName?.text = product?.name
        textViewDescription?.text = product?.description
        textViewPrice?.text = "${product?.price}$"

        imageViewAdd?.setOnClickListener { addItem() }
        imageViewRemove?.setOnClickListener { removeItem() }
        buttonAdd?.setOnClickListener { addToBag() }

        getProductsFromSharedPref()

    }

    private fun addToBag() {
        val index = getIndexOf(product?.id!!) // Indice del producto si es que existe en shared pref

        if (index == -1) { // Este producto no existe aun en shared pref
            if(product?.quantity == null){
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        }
        else { // Si ya existe el producto en shared pref , debemos editar la cantidad
            selectedProducts[index].quantity = counter
        }

        sharedPref?.save("order", selectedProducts)
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()
    }

    private fun getProductsFromSharedPref() {

        if (!sharedPref?.getData("order").isNullOrBlank()) { // Si existe una orden en shared Pref
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            val index = getIndexOf(product?.id!!)

            if(index != -1) {
                product?.quantity = selectedProducts[index].quantity
                textViewCounter?.text = "${product?.quantity}"
                productPrice = product?.price!! * product?.quantity!!
                textViewPrice?.text = "${productPrice}$"
                buttonAdd?.setText("Editar producto")
                buttonAdd?.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }

            for (p in selectedProducts){
                Log.d(TAG, "Shared pref: $p")
            }
        }

    }

    // Es para comparar si un producto ya existe en shared pref y asi poder editar la cantidad de productos seleccionado
    private fun getIndexOf(idProduct: String): Int {
        var pos = 0

        for (p in selectedProducts) {
            if (p.id == idProduct){
                return pos
            }
            pos++
        }

        return -1
    }


    private fun addItem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        textViewCounter?.text = "${product?.quantity}"
        textViewPrice?.text = "${productPrice}$"

    }

    private fun removeItem(){
        if (counter > 1) {
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            textViewCounter?.text = "${product?.quantity}"
            textViewPrice?.text = "${productPrice}$"

        }
    }
}