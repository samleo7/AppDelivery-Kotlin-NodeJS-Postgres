package com.example.delivery.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.R
import com.example.delivery.activities.client.addres.list.ClientAddressListActivity
import com.example.delivery.models.Address
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson

class AddressAdapter(val context:Activity, val address:ArrayList<Address>): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    val sharedPref = SharedPref(context)
    val gson = Gson()
    var prev = 0
    var positionAddressSession = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        val a = address[position] // Cada una  de las direcciones

        if (!sharedPref.getData("address").isNullOrBlank()){ // Si el usuario elijio una direccion de la lista
            val adr = gson.fromJson(sharedPref.getData("address"), Address::class.java)

            if (adr.id == a.id){
                positionAddressSession = position
                holder.imageViewCheck.visibility = View.VISIBLE

            }
        }

        holder.textViewAddress.text = a.address
        holder.textViewNeighborhood.text = a.neighborhood

        // Para Habilitar el check del icono verde segun el pedido a donde enviar
        holder.itemView.setOnClickListener {

            (context as ClientAddressListActivity).resetValue(prev)
            (context as ClientAddressListActivity).resetValue(positionAddressSession)

            prev = position // Es para que la seleccion de la direccion sea una sola

            holder.imageViewCheck.visibility = View.VISIBLE
            saveAddress(a.toJson())
        }
    }

   // Para guardar la direccion seleccionada
    private fun saveAddress(data:String){
        val ad = gson.fromJson(data, Address::class.java)
        sharedPref.save("address", ad)
    }

    class AddressViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textViewAddress: TextView
        val textViewNeighborhood: TextView
        val imageViewCheck: ImageView

        init {
            textViewAddress = view.findViewById(R.id.textview_address)
            textViewNeighborhood = view.findViewById(R.id.textview_neighborhood)
            imageViewCheck = view.findViewById(R.id.imageview_check)
        }

    }


}