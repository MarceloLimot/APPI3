package com.marcelolimot.appi3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marcelolimot.appi3.model.produtos;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;


public class grafico extends AppCompatActivity {

    PieChart pieChart;
    List<produtos> listaProdutos;
    TextView item1;

    RecyclerView recyclerView;
    GraficoAdapter graficoAdapter;
    FirebaseFirestore db;
    private String usuario;
    private String nome;
    private String qtd;
    private int total ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        recyclerView = findViewById(R.id.recycler_grafico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        usuario = FirebaseAuth.getInstance().getUid().toString();

        item1 = findViewById(R.id.txt_item1);
        pieChart = findViewById(R.id.pieChart);


        listaProdutos = new ArrayList<produtos>();
        graficoAdapter = new GraficoAdapter(grafico.this,listaProdutos);
        recyclerView.setAdapter(graficoAdapter);
        EventChangeListener();
        setData();


    }

    private void EventChangeListener() {
        db.collection("Produtos").whereEqualTo("userCad",usuario)
                .orderBy("qtd", Query.Direction.DESCENDING)
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
                            graficoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }



    private void setData(){
        total = 0;
        db.collection("Produtos")
                .whereEqualTo("userCad", usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                nome = document.getString("nome");
                                qtd = String.valueOf(document.getLong("qtd"));
                                total = total + Integer.parseInt(qtd);
                                int hash = nome.hashCode();
                                int hash2 = qtd.hashCode();

                                pieChart.addPieSlice(new PieModel(document.getString("nome"),
                                        Integer.parseInt(String.valueOf(document.getLong("qtd"))),
                                        Color.rgb(hash, hash2, 0)));
                            }
                            item1.setText(String.valueOf(total));
                        }
                        else{
                            Toast.makeText(getApplicationContext()
                                    , "Nenhum dado encontrado!"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        pieChart.startAnimation();

    }



}