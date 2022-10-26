package com.marcelolimot.appi3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class edit_user extends AppCompatActivity {

    private TextView edit_nome, edit_email;
    private Button btn_editar, btn_sair;
    private String usuario;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);



        IniciarComponentes();;
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuario =FirebaseAuth.getInstance().getUid().toString();
        db.collection("Usuarios").document(usuario).addSnapshotListener((value, error) ->{
            if(value != null) {
                edit_nome.setText(value.getString("nome"));
                edit_email.setText(value.getString("email"));
            }
        });
    }

    public void sair(View v){
        Intent intent = new Intent(this, menu.class);
        startActivity(intent);
        finish();
    }



    private void IniciarComponentes(){
        edit_nome =  findViewById(R.id.txt_nome_usuario);
        edit_email =  findViewById(R.id.txt_email_usuario);
        btn_editar = findViewById(R.id.btn_editar);
        btn_sair = findViewById(R.id.btn_sair);
    }
}
