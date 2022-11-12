package com.marcelolimot.appi3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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
        val btnAcc= binding.imgUser
        val btnLista = binding.btnLista

        btnEditAccount.setOnClickListener(){
            edit()
        }

        btnLista.setOnClickListener(){
            lista()
        }

        btnAdd.setOnClickListener(){
            add()
        }

        btnSair.setOnClickListener(){
            sair()
        }

        btnAcc.setOnClickListener(){
            edit()
        }
    }

    override fun onStart() {
        super.onStart()

        val img_view = binding.imgUser
        val usuario = FirebaseAuth.getInstance().uid.toString()

        db.collection("Usuarios").document(usuario)
            .addSnapshotListener{ documento, error ->
                if(documento != null){
                    binding.txtUsuario.text = documento.getString("nome")
                    val imgUrl: String? = documento.getString("imgUrl")
                    Glide.with(this).asBitmap().load(imgUrl).into(img_view)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        val img_view = binding.imgUser
        val usuario = FirebaseAuth.getInstance().uid.toString()

        db.collection("Usuarios").document(usuario)
            .addSnapshotListener{ documento, error ->
                if(documento != null){
                    binding.txtUsuario.text = documento.getString("nome")
                    val imgUrl: String? = documento.getString("imgUrl")
                    Glide.with(this).asBitmap().load(imgUrl).into(img_view)
                }
            }
    }


    private fun lista(){
        val intent = Intent(this, lista_produtos::class.java)
        startActivity(intent)
    }

    private fun edit(){
        val intent = Intent(this, edit_user::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun add(){
        val intent = Intent(this, cad_produto::class.java)
        startActivity(intent)
    }

    private fun sair(){
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}