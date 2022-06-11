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
import com.example.delivery.activities.client.home.ClienHomeActivity
import com.example.delivery.activities.delivery.home.DeliveryHomeActivity
import com.example.delivery.activities.restaurant.home.RestaurantHomeActivity
import com.example.delivery.models.Rol
import com.example.delivery.utils.SharedPref

class RolesAdapter(val context:Activity, val roles:ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharePref = SharedPref(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
       val rol = roles[position] // CADA ROL

        holder.textViewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        //Dar clip a los items del roles
        holder.itemView.setOnClickListener { goToRol(rol) }

    }

    private fun goToRol(rol:Rol){
        if(rol.name == "RESTAURANTE"){

            sharePref.save("rol", "RESTAURANTE")

            val i = Intent(context, RestaurantHomeActivity::class.java)
            context.startActivity(i)
        }

        else if(rol.name == "CLIENTE"){
            sharePref.save("rol", "CLIENTE")

            val i = Intent(context, ClienHomeActivity::class.java)
            context.startActivity(i)
        }

        else if(rol.name == "REPARTIDOR"){
            sharePref.save("rol", "REPARTIDOR")

            val i = Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }

    }

    class RolesViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.textview_rol)
            imageViewRol = view.findViewById(R.id.imageview_rol)
        }

    }


}