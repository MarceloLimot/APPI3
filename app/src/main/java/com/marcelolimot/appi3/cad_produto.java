package com.marcelolimot.appi3;

import static android.content.ContentValues.TAG;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcelolimot.appi3.model.produtos;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class cad_produto extends AppCompatActivity {

    private EditText edit_Cod, edit_Nome, edit_Qtd, edit_Valor, edit_Desc;
    private Button btn_Cadastrar, btn_Atualizar;
    private ImageButton btn_upload;
    String[] mensagens = {"Preencha todos os campos!","Cadastro realizado com sucesso!", "Produto ja cadastrado!"};
    String usuarioID, countProd , nomeImg, imgUrl;
    long qtdProd;
    private ImageView img_view;
    private Uri img_selecionada;
    List<produtos> listaProdutos;
    private String[] strImg, urlImg, urlImgSplit;
    FirebaseFirestore db;
    private StorageReference armazenamentoRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_produto);

        IniciarComponentes();

        armazenamentoRef = FirebaseStorage.getInstance().getReference();

        Intent cod= getIntent();
        Bundle b = cod.getExtras();

        if(b!=null){
            String codExtra = (String) b.get("cod");
            String nomeExtra = (String) b.get("nome");
            String qtdExtra = (String) b.get("qtd");
            String valorExtra = (String) b.get("valor");
            String descExtra = (String) b.get("desc");
            String imgUrlExtra = (String) b.get("imgUrl");
            edit_Cod.setText(codExtra);
            edit_Nome.setText(nomeExtra);
            edit_Qtd.setText(qtdExtra);
            edit_Valor.setText(valorExtra);
            edit_Desc.setText(descExtra);

            edit_Cod.setEnabled(false);
            btn_Cadastrar.setVisibility(View.INVISIBLE);
            btn_Atualizar.setVisibility(View.VISIBLE);
            btn_Cadastrar.setEnabled(false);
            btn_Atualizar.setEnabled(true);

            if(imgUrlExtra.length() > 0){
                imgUrl = imgUrlExtra;
                strImg = imgUrlExtra.split("/");
                urlImg = strImg[7].split("%2F");
                urlImgSplit = urlImg[1].split("\\?");
                nomeImg = urlImgSplit[0];
                //Log.i("String ImgUrlExtra", nomeImg);
            }


            db = FirebaseFirestore.getInstance();
            db.collection("Produtos").document(codExtra)
                    .addSnapshotListener((value, error) ->{
                        String imgUrl= imgUrlExtra;
                        Glide.with(getApplicationContext()).asBitmap().load(imgUrlExtra).into(img_view);
            });

        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collection = db.collection("Produtos");
            AggregateQuery countQuery = collection.count();
            countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task ->{
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    qtdProd = snapshot.getCount();
                    countProd = String.valueOf(qtdProd + 1);
                    if(countProd.length() < 2) {
                        edit_Cod.setText("0" + countProd);
                    }else{
                        edit_Cod.setText(countProd);
                    }
                    edit_Cod.setEnabled(false);

                    btn_Cadastrar.setVisibility(View.VISIBLE);
                    btn_Atualizar.setVisibility(View.INVISIBLE);
                    btn_Cadastrar.setEnabled(true);
                    btn_Atualizar.setEnabled(false);
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            });
        }


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

        btn_Atualizar.setOnClickListener(new View.OnClickListener(){
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
                    AtualizarProduto();
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
                                //Log.i("Teste", uri.toString());

                                //excluirImagem();

                                String codProd = cod;
                                String imgUrl = uri.toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference =db.collection("Produtos").document(codProd);

                                documentReference.update("imgUrl", imgUrl);

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Erro", e.getMessage(), e);
                    }
                });
    }

    private void excluirImagem(){
        StorageReference imgRef = armazenamentoRef.child("/images/"+nomeImg);
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
                if(img_selecionada != null){
                    salvarImagem(cod);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados " + e.toString());
            }
        });
    }


    private void AtualizarProduto(){
        String cod = edit_Cod.getText().toString();
        String nome= edit_Nome.getText().toString();
        String qtd= edit_Qtd.getText().toString();
        String valor = edit_Valor.getText().toString();
        String desc = edit_Desc.getText().toString();
        String uri_img = "";

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Map<String, Object> produtos = new HashMap<>();
        produtos.put("nome", nome);
        produtos.put("qtd",qtd);
        produtos.put("valor",valor);
        produtos.put("desc",desc);


        DocumentReference documentReference =db.collection("Produtos").document(cod);
        documentReference.update(produtos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao Atualizar os dados");
                if(img_selecionada != null){
                    salvarImagem(cod);
                    excluirImagem();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao Atualizar os dados " + e.toString());
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
        btn_Atualizar = findViewById(R.id.btn_Atualizar);
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
                    //Log.i("Teste","selecionou a imagem");
                    Uri uri = result.getData().getData();

                    img_selecionada = uri;
                    Glide.with(this).asBitmap().load(img_selecionada).into(img_view);
                }
            });
}