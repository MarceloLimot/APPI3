package com.marcelolimot.appi3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marcelolimot.appi3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnLogin =binding.btnLogin

        btnLogin.setOnClickListener(){
            val login = binding.txtlogin.text.toString()
            val senha = binding.senha.text.toString()

            if(login == "admin" && senha == "admin") {
               logar()
            }
            else{
                teste()
            }

        }

    }


    private fun logar(){
        val intent = Intent(this, menu::class.java)
        startActivity(intent)
    }

    private fun teste(){
        val intent = Intent(this, teste::class.java)
        startActivity(intent)
    }



}