package com.example.delivery.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.delivery.R
import com.example.delivery.activities.client.home.ClienHomeActivity
import com.example.delivery.activities.delivery.home.DeliveryHomeActivity
import com.example.delivery.activities.restaurant.home.RestaurantHomeActivity
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        buttonLogin = findViewById(R.id.btn_login)
        imageViewGoToRegister = findViewById(R.id.imageview_go_to_register)

        imageViewGoToRegister?.setOnClickListener { goToRegister() }
        buttonLogin?.setOnClickListener { login() }

        //Metodo para que luego de logearse no pida de nuevo
        getUserFromSession()
    }

    private fun login(){
        val email = editTextEmail?.text.toString()   // NULL POINTER EXCEPTION
        val password = editTextPassword?.text.toString()

        if (isvalidForm(email, password)){

            // Validacion con el token (NODE JS + Postgres + JsonWebSocket)
            usersProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d("MainActivity", "Response:  ${response.body()}")

                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                        //Metodo - SharedPreference
                        saveUserInSession(response.body()?.data.toString())

                    }
                    else{
                        Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error  ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        else{
            Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
        }

     //   Log.d("MainActivity", "El password es $password")

    }

    private fun goToClientHome(){
        val i = Intent(this, ClienHomeActivity::class.java)
        // Para que ya no pase a este activity al retroceder la actvidad
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK  //ELIMINAR EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun goToRestaurantHome(){
        val i = Intent(this, RestaurantHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK  //ELIMINAR EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun goToDeliveryHome(){
        val i = Intent(this, DeliveryHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK  //ELIMINAR EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun goToSelectRol(){
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK  //ELIMINAR EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)

        if (user.roles?.size!! > 1) { // Tiene mas de un rol
            goToSelectRol()

        }
        else { // Solo un rol (Cliente)
            goToClientHome()
        }

        //
    }

    fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    //Es para que ya no inicie sesion despues de logearse --- Mediante sharedPreferenc
    private fun getUserFromSession(){

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            // SI EL USUARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)

            if (!sharedPref.getData("rol").isNullOrBlank()){

                //SI EL USUARIO SELECCIONO EL ROL
                val rol = sharedPref.getData("rol")?.replace("\"", "")
                Log.d("dar", "ROL $rol")

                if (rol == "RESTAURANTE"){
                    goToRestaurantHome()
                }
               else if (rol == "CLIENTE"){
                    goToClientHome()
                }
                else if (rol == "REPARTIDOR"){
                    goToDeliveryHome()
                }
            }
            else{
                goToClientHome()
            }
        }
    }

    private fun isvalidForm(email:String, password:String):Boolean{

        if (email.isBlank()){
            return false
        }

        if (password.isBlank()){
            return false
        }

        if (!email.isEmailValid()){
            return false
        }

        return true
    }

    private fun goToRegister() {
       val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}