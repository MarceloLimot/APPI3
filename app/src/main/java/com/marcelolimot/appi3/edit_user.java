package com.marcelolimot.appi3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class edit_user extends AppCompatActivity {

    private TextView edit_nome, edit_email;
    private Button btn_editar, btn_sair, btn_salvar_img;
    private ImageButton btn_upload;
    private String usuario;
    private ImageView img_view;
    private Uri img_selecionada;
    private ViewTarget<ImageView, Bitmap> img_init;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        usuario =FirebaseAuth.getInstance().getUid().toString();
        db.collection("Usuarios").document(usuario).addSnapshotListener((value, error) ->{
            if(value != null) {
                edit_nome.setText(value.getString("nome"));
                edit_email.setText(value.getString("email"));
                String imgUrl= value.getString("imgUrl");

                Glide.with(this).asBitmap().load(imgUrl).into(img_view);
            }
        });

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(view -> {
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
        });

        btn_salvar_img = findViewById(R.id.btn_salvar_img);
        btn_salvar_img.setOnClickListener(view ->{
            salvarImagem();
        });

        IniciarComponentes();
    }

    private void salvarImagem(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(img_selecionada)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("Teste", uri.toString());

                                String usuarioID = uid;
                                String imgUrl = uri.toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference =db.collection("Usuarios").document(usuarioID);
                                documentReference.update("imgUrl", imgUrl);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage(), e);
                    }
                });
    }



    /*@Override
    protected void onStart() {
        super.onStart();

        usuario =FirebaseAuth.getInstance().getUid().toString();
        db.collection("Usuarios").document(usuario).addSnapshotListener((value, error) ->{
            if(value != null) {
                edit_nome.setText(value.getString("nome"));
                edit_email.setText(value.getString("email"));
                String imgUrl= value.getString("imgUrl");

                Glide.with(this).asBitmap().load(imgUrl).into(img_view);
            }
        });
    }*/

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
        btn_salvar_img = findViewById(R.id.btn_salvar_img);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Log.i("Teste","selecionou a imagem");
                    Uri uri = result.getData().getData();
                    img_selecionada = uri;
                    Glide.with(this).asBitmap().load(img_selecionada).into(img_view);
                    //img_view.setImageURI(uri);
                }
            });


}
