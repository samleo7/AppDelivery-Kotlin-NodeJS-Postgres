package com.example.delivery.activities.client.addres.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.R
import com.example.delivery.activities.client.addres.create.ClientAddressCreateActivity
import com.example.delivery.activities.client.payments.mercado_pago.form.ClientPaymentFormActivity
import com.example.delivery.activities.client.payments.payment_method.ClientPaymentMethodActivity
import com.example.delivery.adapters.AddressAdapter
import com.example.delivery.models.*
import com.example.delivery.providers.AddressProvider
import com.example.delivery.providers.OrdersProvider
import com.example.delivery.utils.SharedPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    var fabCreateAddress: FloatingActionButton? = null
    var toolbar:Toolbar? = null

    var recyclerViewAddress: RecyclerView? = null
    var buttonNext:Button? = null
    var adapter:AddressAdapter? = null
    var addressProvider: AddressProvider? = null
    var ordersProvider:OrdersProvider? = null
    var sharedPref:SharedPref? = null
    var user: User? = null

    var address = ArrayList<Address>()

    val gson = Gson()

    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)
        getProductsFromSharedPref()

        fabCreateAddress = findViewById(R.id.fab_address_create)
       // fabCreateAddress?.setOnClickListener { goToAddressCreate() }
        toolbar = findViewById(R.id.toolbar)
        buttonNext = findViewById(R.id.btn_next)
        recyclerViewAddress = findViewById(R.id.recyclerview_address)

        recyclerViewAddress?.layoutManager = LinearLayoutManager(this)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        //Inicializando
        addressProvider = AddressProvider(user?.sessionToken!!)
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        fabCreateAddress?.setOnClickListener { goToAddressCreate() }

        getAddress()

        buttonNext?.setOnClickListener { getAddressFromSession() }

    }

    private fun getProductsFromSharedPref() {

        if (!sharedPref?.getData("order").isNullOrBlank()) { // Si existe una orden en shared Pref
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
        }
    }

    private fun createOrder(idAddress:String){
        val order = Order(
            products = selectedProducts,
            idClient = user?.id!!,
            idAddress = idAddress
        )

        ordersProvider?.create(order)?.enqueue(object:Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null){
                    Toast.makeText(this@ClientAddressListActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@ClientAddressListActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
               Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getAddressFromSession(){
        if (!sharedPref?.getData("address").isNullOrBlank()) {
            val a = gson.fromJson(sharedPref?.getData("address"), Address::class.java) // Si exite una
            createOrder(a.id!!)

            goToPaymentsForm()
        }
        else{
            Toast.makeText(this, "Seleccina una direccion para continuar", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToPaymentsForm(){
        val i = Intent(this, ClientPaymentMethodActivity::class.java)
        startActivity(i)
    }


    // Metodo para  Borrar la direccion seleccionada --> Se usara en su adapter
    fun resetValue(position:Int){
        val viewHolder = recyclerViewAddress?.findViewHolderForAdapterPosition(position) // Una direccion
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility = View.GONE
    }

    private fun getAddress(){
        addressProvider?.getAddress(user?.id!!)?.enqueue(object : Callback<ArrayList<Address>>{
            override fun onResponse(call: Call<ArrayList<Address>>, response: Response<ArrayList<Address>>) {

                if (response.body() != null){
                    address = response.body()!!
                    adapter = AddressAdapter(this@ClientAddressListActivity, address)
                    recyclerViewAddress?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)

        }
    }

    private fun goToAddressCreate() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }
}