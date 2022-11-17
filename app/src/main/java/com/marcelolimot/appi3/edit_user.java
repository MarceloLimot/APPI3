package com.marcelolimot.appi3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private String usuario, imgUrlExtra, nomeImg;
    private ImageView img_view;
    private Uri img_selecionada;
    private String[] strImg, urlImg, urlImgSplit;
    private ViewTarget<ImageView, Bitmap> img_init;
    private StorageReference armazenamentoRef;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        IniciarComponentes();

        armazenamentoRef = FirebaseStorage.getInstance().getReference();

        usuario =FirebaseAuth.getInstance().getUid().toString();
        db.collection("Usuarios").document(usuario).addSnapshotListener((value, error) ->{
            if(value != null) {
                edit_nome.setText(value.getString("nome"));
                edit_email.setText(value.getString("email"));
                String imgUrl= value.getString("imgUrl");

                Glide.with(this).asBitmap().load(imgUrl).into(img_view);

                imgUrlExtra = imgUrl;
                if(imgUrlExtra.length() > 0){
                    strImg = imgUrlExtra.split("/");
                    urlImg = strImg[7].split("%2F");
                    urlImgSplit = urlImg[1].split("\\?");
                    nomeImg = urlImgSplit[0];
                    //Log.i("String ImgUrlExtra", nomeImg);
                }
            }
        });



        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(view -> {

            AlertDialog.Builder selecionaFoto = new AlertDialog.Builder(edit_user.this);
            selecionaFoto.setTitle("Foto do Perfil");
            selecionaFoto.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    someActivityResultLauncher2.launch(intent);
                }
            });

            selecionaFoto.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent  = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    someActivityResultLauncher.launch(intent);
                }
            });
            selecionaFoto.create().show();


            //Intent intent  = new Intent();
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.setType("image/*");
            //someActivityResultLauncher.launch(intent);
        });



        btn_salvar_img = findViewById(R.id.btn_salvar_img);
        btn_salvar_img.setOnClickListener(view ->{
            salvarImagem();
            excluirImagem();
        });

        btn_sair = findViewById(R.id.btn_sair);
        btn_sair.setOnClickListener(view -> {
            sair();
        });


    }

    private void salvarImagem(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/userimages/" + filename);
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


    private void excluirImagem(){
        StorageReference imgRef = armazenamentoRef.child("//userimages/"+nomeImg);
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Log.i("Concluido", "Imagem deletada com sucesso: "+ nomeImg);
                //Log.i("imgRef", nomeImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.i("imgRef", String.valueOf(imgRef));
                Log.e("Erro", e.getMessage(), e);
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

    public void sair(){
        Intent intent = new Intent(this, menu.class);
        startActivity(intent);
    }



    private void IniciarComponentes(){
        edit_nome =  findViewById(R.id.txt_nome_usuario);
        edit_email =  findViewById(R.id.txt_email_usuario);
        btn_editar = findViewById(R.id.btn_salvar_img);
        btn_sair = findViewById(R.id.btn_sair);
        img_view = findViewById(R.id.imgUser);
        btn_salvar_img = findViewById(R.id.btn_salvar_img);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    //Log.i("Teste","selecionou a imagem");
                    Uri uri = result.getData().getData();
                    img_selecionada = uri;
                    Glide.with(this).asBitmap().load(img_selecionada).into(img_view);
                    //img_view.setImageURI(uri);
                }
            });

    ActivityResultLauncher<Intent> someActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Bundle extras = result.getData().getExtras();
                    Uri imageUri;
                    //Intent data = result.getData();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    WeakReference<Bitmap> result1 =
                            new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                                    imageBitmap.getHeight(),
                                    imageBitmap.getWidth(), false).copy(
                                    Bitmap.Config.RGB_565, true)
                            );

                    Bitmap bm=result1.get();
                    imageUri = saveImage(bm, edit_user.this);

                    img_selecionada = imageUri;
                    img_view.setImageURI(imageUri);

                    //Log.d("Tipo de dado", "");
                }
            });


    private Uri saveImage(Bitmap image, Context context) {
        File imagesFolder = new File(context.getExternalFilesDir("images"),"images");
        Uri uri=null;
        try{
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "imagem.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider",file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }


}
