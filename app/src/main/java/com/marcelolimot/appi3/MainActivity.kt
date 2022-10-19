package com.marcelolimot.appi3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.marcelolimot.appi3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnLogin =binding.btnLogin
        val btnRegistrar = binding.btnRegistrar
        val txtLogin = binding.txtlogin
        val txtSenha = binding.txtsenha
        val login =binding.txtlogin.text.toString()


        btnLogin.setOnClickListener(){
            val login = binding.txtlogin.text.toString()
            val senha = binding.txtsenha.text.toString()


            if(login == "admin" && senha == "admin") {
                logar()
            }
            else{
                binding.txtErro.visibility = View.VISIBLE
            }

        }

        btnRegistrar.setOnClickListener(){
            teste()
        }

        txtLogin.setOnClickListener(){
            binding.txtErro.visibility = View.INVISIBLE
        }

        
        txtSenha.setOnClickListener(){
            binding.txtErro.visibility = View.INVISIBLE
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