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
import com.example.delivery.activities.client.shopping_bag.ClientShoppingBagActivity
import com.example.delivery.models.Product
import com.example.delivery.utils.SharedPref

class ShoppingBagAdapter(val context:Activity, val products:ArrayList<Product>): RecyclerView.Adapter<ShoppingBagAdapter.ShoppingBagViewHolder>() {

    val sharePref = SharedPref(context)

    init {
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag, parent, false)
        return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {

        val product = products[position] // OPTENEMOS CADA UNA DE LAS CATEGORIAS

        holder.textViewName.text = product.name
        holder.textViewCounter.text = "${product.quantity}"
        holder.textViewPrice.text = "${product.price * product.quantity!!}$"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        holder.imageViewAdd.setOnClickListener { addItem(product, holder) }
        holder.imageViewRemove.setOnClickListener { removeItem(product, holder) }
        holder.imageViewDelete.setOnClickListener { deleteItem(position) }

        //Dar clip a los items del producto para ir a detalle
    //    holder.itemView.setOnClickListener { goToDetail(product) }
    }

    private fun getTotal(): Double {
        var total = 0.0
        for (p in products) {
            total = total + (p.quantity!! * p.price)
        }
        return total
    }

    // Es para comparar si un producto ya existe en shared pref y asi poder editar la cantidad de productos seleccionado
    private fun getIndexOf(idProduct: String): Int {
        var pos = 0

        for (p in products) {
            if (p.id == idProduct){
                return pos
            }
            pos++
        }

        return -1
    }

    private fun deleteItem(position: Int){
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)
        sharePref.save("order", products)

        // Para mantener actualizado el total de precios
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }

    private fun addItem(product: Product, holder:ShoppingBagViewHolder){

        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
        products[index].quantity = product.quantity

        holder.textViewCounter.text = "${product?.quantity}"
        holder.textViewPrice.text = "${product.quantity!! * product.price}$"

        sharePref.save("order", products)

        // Para mantener actualizado el total de precios
        (context as ClientShoppingBagActivity).setTotal(getTotal())

    }

    private fun removeItem(product:Product, holder:ShoppingBagViewHolder){

        if (product.quantity!! > 1) {
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity

            holder.textViewCounter.text = "${product?.quantity}"
            holder.textViewPrice.text = "${product.quantity!! * product.price}$"

            sharePref.save("order", products)

            // Para mantener actualizado el total de precios
            (context as ClientShoppingBagActivity).setTotal(getTotal())

        }
    }








    // Pasando a la otra activity y pasando los datos del producto a detalles
    private fun goToDetail(product: Product){
            val i = Intent(context, ClientProductsDetailActivity::class.java)
            i.putExtra("product", product.toJson())
            context.startActivity(i)
    }

    class ShoppingBagViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textViewName: TextView
        val textViewPrice: TextView
        val textViewCounter: TextView
        val imageViewProduct: ImageView
        val imageViewAdd:ImageView
        val imageViewRemove:ImageView
        val imageViewDelete:ImageView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewPrice = view.findViewById(R.id.textview_price)
            textViewCounter = view.findViewById(R.id.textview_counter)
            imageViewProduct = view.findViewById(R.id.imageview_product)
            imageViewAdd = view.findViewById(R.id.imageview_add)
            imageViewRemove = view.findViewById(R.id.imageview_remove)
            imageViewDelete = view.findViewById(R.id.imageview_delete)
        }

    }


}