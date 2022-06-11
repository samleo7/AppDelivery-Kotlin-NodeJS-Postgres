package com.example.delivery.activities.delivery.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.R
import com.example.delivery.activities.delivery.home.DeliveryHomeActivity
import com.example.delivery.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.example.delivery.adapters.OrderProductsAdapter
import com.example.delivery.models.Order
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.OrdersProvider
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersDetailActivity : AppCompatActivity() {
    val TAG = "DelivOrdersDetail"
    var order: Order? = null
    val gson = Gson()

    var toobar: Toolbar? = null
    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewDate: TextView? = null
    var textViewTotal: TextView? = null
    var textViewStatus: TextView? = null
    var recyclerViewProducts: RecyclerView? = null
    var buttonUpdate: Button? = null
    var buttonGoToMap: Button? = null

    var adapter: OrderProductsAdapter? = null

    var usersProvider: UsersProvider? = null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null

    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_detail)

        sharedPref = SharedPref(this)

        // Tiene todos los datos de la orden
        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

        getUserFromSession()

        usersProvider = UsersProvider(user?.sessionToken!!)
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        toobar = findViewById(R.id.toolbar)
        toobar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toobar?.title = "Order #${order?.id}"
        setSupportActionBar(toobar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewClient = findViewById(R.id.textview_client)
        textViewAddress = findViewById(R.id.textview_address)
        textViewDate = findViewById(R.id.textview_date)
        textViewTotal = findViewById(R.id.textview_total)
        textViewStatus = findViewById(R.id.textview_status)
        buttonUpdate = findViewById(R.id.btn_update)
        buttonGoToMap = findViewById(R.id.btn_go_to_map)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)

        adapter = OrderProductsAdapter(this, order?.products!!)
        recyclerViewProducts?.adapter = adapter

        textViewClient?.text = "${order?.client?.name} ${order?.client?.lastname}"
        textViewAddress?.text = order?.address?.address
        textViewDate?.text = "${order?.timestamp}"
        textViewStatus?.text = order?.status

        Log.d(TAG, "Orden: ${order.toString()}")

        getTotal()

        //Para que el boton,  esten en GONE segun lo que esta en el XML
        if (order?.status == "DESPACHADO"){
            buttonUpdate?.visibility = View.VISIBLE
        }

        if (order?.status == "EN CAMINO"){
            buttonGoToMap?.visibility = View.VISIBLE
        }


        buttonUpdate?.setOnClickListener { updateOrder() }
        buttonGoToMap?.setOnClickListener { goToMap() }
    }

    private fun updateOrder(){

        ordersProvider?.updateToOnTheWay(order!!)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null){

                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "ENTREGA INICIADA", Toast.LENGTH_LONG).show()
                        goToMap()
                    }
                    else{
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "No se pudo asignar el repartidor", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this@DeliveryOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersDetailActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun goToMap(){
        val i = Intent(this, DeliveryOrdersMapActivity::class.java)
        i.putExtra("order", order?.toJson())
        startActivity(i)
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getTotal(){
        var total = 0.0

        for (p in order?.products!!){
            total = total + (p.price * p.quantity!!)
        }
        textViewTotal?.text = "${total}$"
    }
}