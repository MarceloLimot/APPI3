package com.marcelolimot.appi3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class cad_produto extends AppCompatActivity {

    private EditText edit_Cod, edit_Nome, edit_Qtd, edit_Valor, edit_Desc;
    private Button btn_Cadastrar;
    private ImageButton btn_upload;
    String[] mensagens = {"Preencha todos os campos!","Cadastro realizado com sucesso!"};
    String usuarioID, cod;
    private ImageView img_view;
    private Uri img_selecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_produto);

        IniciarComponentes();

        btn_Cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String cod = edit_Cod.getText().toString();
                String nome= edit_Nome.getText().toString();
                String qtd= edit_Qtd.getText().toString();
                String valor = edit_Valor.getText().toString();
                String desc = edit_Desc.getText().toString();
                if(cod.isEmpty() || nome.isEmpty() || qtd.isEmpty() || valor.isEmpty() || desc.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else{
                    CadastrarProduto();
                    Sair();
                    edit_Cod.setText("");
                    edit_Nome.setText("");
                    edit_Qtd.setText("");
                    edit_Valor.setText("");
                    edit_Desc.setText("");
                    img_view.setImageURI(Uri.parse(""));
                }
            }
        });

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(view -> {
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
        });
    }



    private void SalvarDadosProduto(){
        String cod = edit_Cod.getText().toString();
        String nome= edit_Nome.getText().toString();
        String qtd= edit_Qtd.getText().toString();
        String valor = edit_Valor.getText().toString();
        String desc = edit_Desc.getText().toString();

        CadastrarProduto();

    }

    private void salvarImagem(String cod){

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

                                String codProd = cod;
                                String imgUrl = uri.toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference =db.collection("Produtos").document(codProd);
                                documentReference.update("imgUrl", imgUrl);
                                //Sair();
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




    private void CadastrarProduto(){
        String cod = edit_Cod.getText().toString();
        String nome= edit_Nome.getText().toString();
        String qtd= edit_Qtd.getText().toString();
        String valor = edit_Valor.getText().toString();
        String desc = edit_Desc.getText().toString();
        String uri_img = "";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> produtos = new HashMap<>();
        produtos.put("cod",cod);
        produtos.put("nome", nome);
        produtos.put("qtd",qtd);
        produtos.put("valor",valor);
        produtos.put("desc",desc);
        produtos.put("imgUrl", uri_img);
        produtos.put("userCad", usuarioID);


        DocumentReference documentReference =db.collection("Produtos").document(cod);
        documentReference.set(produtos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");
                salvarImagem(cod);
                //Sair();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados " + e.toString());
            }
        });
    }

    private void IniciarComponentes(){
        edit_Cod = findViewById(R.id.edit_Cod);
        edit_Nome = findViewById(R.id.edit_Nome);
        edit_Qtd = findViewById(R.id.edit_Qtd);
        edit_Valor = findViewById(R.id.edit_Valor);
        edit_Desc = findViewById(R.id.edit_Desc);
        btn_Cadastrar = findViewById(R.id.btn_Cadastrar);
        img_view = findViewById(R.id.imgUser);
    }

    private void Sair(){
        Intent intent = new Intent(this, menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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