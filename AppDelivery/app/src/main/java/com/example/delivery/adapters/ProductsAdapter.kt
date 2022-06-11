package com.example.delivery.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.delivery.R
import com.example.delivery.activities.client.products.detail.ClientProductsDetailActivity
import com.example.delivery.models.Product
import com.example.delivery.utils.SharedPref

class ProductsAdapter(val context:Activity, val products:ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharePref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = products[position] // OPTENEMOS CADA UNA DE LAS CATEGORIAS

        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}$"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        //Dar clip a los items del producto para ir a detalle
        holder.itemView.setOnClickListener { goToDetail(product) }

    }

    // Pasando a la otra activity y pasando los datos del producto a detalles
    private fun goToDetail(product: Product){
            val i = Intent(context, ClientProductsDetailActivity::class.java)
            i.putExtra("product", product.toJson())
            context.startActivity(i)
    }

    class ProductsViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewPrice = view.findViewById(R.id.textview_price)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }

    }


}