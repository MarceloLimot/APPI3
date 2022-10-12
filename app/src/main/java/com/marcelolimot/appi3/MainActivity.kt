package com.marcelolimot.appi3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    private fun setListeners(){


    }

    private fun logar(login: String, senha:String){
        val login = login.toString()
        val senha = senha.toString()

        if((login == "admin") && (senha == "admin")){

        }
    }


}