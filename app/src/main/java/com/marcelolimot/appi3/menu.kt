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

        val btnEditAccount = binding.btnEditAccount
        val btnAdd = binding.btnAdd
        val btnSair = binding.btnSair

        btnEditAccount.setOnClickListener(){
            edit()
        }

        btnAdd.setOnClickListener(){
            add()
        }

        btnSair.setOnClickListener(){
            sair()
        }
    }

    override fun onStart() {
        super.onStart()

        val usuario = FirebaseAuth.getInstance().uid.toString()

        db.collection("Usuarios").document(usuario)
            .addSnapshotListener{ documento, error ->
                if(documento != null){
                    binding.txtUsuario.text = documento.getString("nome")
                }
            }
    }

    private fun edit(){
        val intent = Intent(this, edit_user::class.java)
        startActivity(intent)
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