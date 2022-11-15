package com.marcelolimot.appi3;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.marcelolimot.appi3.config.ConfigFirebase;
import com.marcelolimot.appi3.model.produtos;

public class lista_produtos extends AppCompatActivity {

    List<produtos> listaProdutos;
    RecyclerView recyclerView;
    ProdutoAdapter produtoAdapter;
    FirebaseFirestore db;


    private ImageView imgUser;
    private TextView txtUsuario;
    private String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        IniciarComponentes();

        recyclerView = findViewById(R.id.recycler_produtos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        usuario = FirebaseAuth.getInstance().getUid().toString();
        db.collection("Usuarios").document(usuario).addSnapshotListener((value, error) ->{
            if(value != null) {
                txtUsuario.setText(value.getString("nome"));
                String imgUrl= value.getString("imgUrl");

                Glide.with(this).asBitmap().load(imgUrl).into(imgUser);
            }
        });



        listaProdutos = new ArrayList<produtos>();
        produtoAdapter = new ProdutoAdapter(lista_produtos.this,listaProdutos);

        recyclerView.setAdapter(produtoAdapter);

        EventChangeListener();

    }


    private void EventChangeListener() {
        db.collection("Produtos").whereEqualTo("userCad",usuario)
                .orderBy("cod", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("db Erro", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType()==DocumentChange.Type.REMOVED){
                                listaProdutos.remove(dc.getDocument().toObject(produtos.class));
                            }
                            if(dc.getType()==DocumentChange.Type.ADDED){
                                listaProdutos.add(dc.getDocument().toObject(produtos.class));
                            }

                            produtoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void IniciarComponentes(){
        txtUsuario = findViewById(R.id.txtUsuario);
        imgUser = findViewById(R.id.imgUser);
    }

}