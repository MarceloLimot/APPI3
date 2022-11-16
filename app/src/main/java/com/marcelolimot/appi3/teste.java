package com.marcelolimot.appi3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class teste extends AppCompatActivity {

    private EditText edit_Nome, edit_Email, edit_Senha;
    private Button btn_Cadastrar, btn_Sair;
    private ImageButton btn_upload;
    private ImageView img_view;
    private Uri img_selecionada;
    String[] mensagens = {"Preencha todos os campos!","Cadastro realizado com sucesso!"};
    String usuarioID;
    private String uri_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        IniciarComponentes();

        if (ContextCompat.checkSelfPermission(teste.this
                , Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(teste.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(view -> {

            AlertDialog.Builder selecionaFoto = new AlertDialog.Builder(teste.this);
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

            /*
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
            */
        });

        btn_Cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nome= edit_Nome.getText().toString();
                String email= edit_Email.getText().toString();
                String senha = edit_Senha.getText().toString();
                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else{
                    CadastrarUsuario(v);
                }
            }
        });

        btn_Sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sair();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_view.setImageBitmap(bitmap);
        }
    }


    private void CadastrarUsuario(View v){
        String email= edit_Email.getText().toString();
        String senha = edit_Senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    salvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(v, mensagens[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                    edit_Nome.setText("");
                    edit_Email.setText("");
                    edit_Senha.setText("");

                    sair();
                }else{
                    String erro;
                    try{
                        throw task.getException();

                    }catch(FirebaseAuthWeakPasswordException e){
                        erro= "Digite uma senha com mais de 6 caracteres";
                    }catch(FirebaseAuthUserCollisionException e){
                        erro= "E-mail ja cadastrado!";
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail invalido!";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuario!";
                    }

                    Snackbar snackbar = Snackbar.make(v, erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void salvarImagem(String uid){
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


    private void salvarDadosUsuario(){
        String nome= edit_Nome.getText().toString();
        String email= edit_Email.getText().toString();
        String uri_img = "";
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("email", email);
        usuarios.put("imgUrl", uri_img);
        usuarios.put("uid",usuarioID);


        DocumentReference documentReference =db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");
                salvarImagem(usuarioID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados " + e.toString());
            }
        });
    }

    private void sair(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void IniciarComponentes(){
        edit_Nome = findViewById(R.id.inputNome);
        edit_Email = findViewById(R.id.inputEmail);
        edit_Senha = findViewById(R.id.inputSenha);
        btn_Cadastrar = findViewById(R.id.btn_Cadastrar);
        btn_Sair= findViewById(R.id.btn_sair);
        img_view = findViewById(R.id.imgUser);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Uri uri = result.getData().getData();
                    img_selecionada = uri;
                    img_view.setImageURI(uri);
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
                    imageUri = saveImage(bm, teste.this);

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
