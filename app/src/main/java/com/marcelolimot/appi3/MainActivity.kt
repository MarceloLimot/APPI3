package com.marcelolimot.appi3

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marcelolimot.appi3.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    var mensagens = arrayOf("Preencha todos os campos!", "Login efetuado com sucesso!", "Erro no Login!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnLogin =binding.btnLogin
        val lblCriar= binding.lblCriar




        btnLogin.setOnClickListener{
            val login = binding.txtlogin.text.toString()
            val senha = binding.txtsenha.text.toString()


            if(login.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(binding.root,mensagens[0], Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.WHITE)
                snackbar.setTextColor(Color.BLACK)
                snackbar.show()
            }else{
                AutenticarUsuario(binding.root)
            }

        }

        lblCriar.setOnClickListener(){
            teste()
        }





    }

    private fun AutenticarUsuario(view: View){
        val login = binding.txtlogin.text.toString()
        val senha = binding.txtsenha.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(login, senha)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val intent = Intent(this, menu::class.java)
                    startActivity(intent)
                }else{
                    var erro: String
                    erro = try {
                        throw task.exception!!
                    } catch (e: Exception){
                        "Erro ao logar usuario!"
                    }
                    val snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.WHITE)
                    snackbar.setTextColor(Color.BLACK)
                    snackbar.show()
                }

            }

    }

    override fun onStart() {
        super.onStart()
        val usuario = Firebase.auth.currentUser
        if(usuario != null){
            logar()
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

    private fun iniciarComponentes(){
        val txtLogin = binding.txtlogin.text.toString()
        val txtSenha = binding.txtsenha.text.toString()
        val btnEntrar = binding.btnLogin
    }



}