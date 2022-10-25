package com.marcelolimot.appi3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelolimot.appi3.databinding.ActivityMenuBinding

class menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnAdd = binding.btnAdd
        val btnSair = binding.btnSair

        btnAdd.setOnClickListener(){
            add()
        }

        btnSair.setOnClickListener(){
            sair()
        }
    }




    private fun add(){
        val intent = Intent(this, cad_produto::class.java)
        startActivity(intent)
    }

    private fun sair(){
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}