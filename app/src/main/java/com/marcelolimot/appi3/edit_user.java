package com.marcelolimot.appi3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;


import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class edit_user extends AppCompatActivity {

    private TextView edit_nome, edit_email;
    private Button btn_editar, btn_sair;
    private ImageButton btn_upload;
    private String usuario;
    private ImageView img_view;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(view -> {
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
        });

        IniciarComponentes();
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
        img_view = findViewById(R.id.imgUser);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Uri uri = result.getData().getData();
                    img_view.setImageURI(uri);
                }
            });


}
