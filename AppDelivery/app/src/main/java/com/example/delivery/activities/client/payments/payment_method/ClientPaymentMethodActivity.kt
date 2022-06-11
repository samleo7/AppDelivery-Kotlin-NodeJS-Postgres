package com.example.delivery.activities.client.payments.payment_method

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

import androidx.core.content.ContextCompat
import com.example.delivery.R
import com.example.delivery.activities.client.payments.mercado_pago.form.ClientPaymentFormActivity
import com.example.delivery.activities.client.payments.paypal.form.ClientPaymentPaypalFormActivity

class ClientPaymentMethodActivity : AppCompatActivity() {

    var toolbar:Toolbar? = null
    var imageViewMercadoPago: ImageView? = null
    var imageViewPaypal:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_method)

        toolbar = findViewById(R.id.toolbar)
        imageViewMercadoPago = findViewById(R.id.imageview_mercadopago)
        imageViewPaypal = findViewById(R.id.imageview_paypal)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Metodos de pago"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewMercadoPago?.setOnClickListener { goToMercadoPago() }
        imageViewPaypal?.setOnClickListener { goToPaypal() }

    }

    private fun goToMercadoPago(){
        val i = Intent(this, ClientPaymentFormActivity::class.java)
        startActivity(i)
    }

    private fun goToPaypal(){
        val i = Intent(this, ClientPaymentPaypalFormActivity::class.java)
        startActivity(i)
    }
}