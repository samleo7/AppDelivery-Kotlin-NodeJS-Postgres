package com.example.delivery.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.delivery.R
import com.example.delivery.activities.MainActivity
import com.example.delivery.fragments.client.ClientCategoriesFragment
import com.example.delivery.fragments.client.ClientOrdersFragment
import com.example.delivery.fragments.client.ClientProfileFragment
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class ClienHomeActivity : AppCompatActivity() {

    private val TAG = "ClientHomeActivity"

    var sharedPref:SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null
    var usersProvider:UsersProvider? = null
    var user:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clien_home)

        sharedPref = SharedPref(this)

        openFragment(ClientCategoriesFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.item_home -> {
                    openFragment(ClientCategoriesFragment())
                    true
                }
                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false
            }
        }

        getUserFromSession()

        usersProvider = UsersProvider(token = user?.sessionToken!!)
        createToken()
    }

    private fun createToken(){
        usersProvider?.createToken(user!!, this)
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

  /*  private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    } */

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // SI EL USUARIO EXISTE EN SESION
             user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }


}