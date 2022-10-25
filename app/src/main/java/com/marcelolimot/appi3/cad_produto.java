package com.marcelolimot.appi3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class cad_produto extends AppCompatActivity {

    private EditText edit_Cod, edit_Nome, edit_Qtd, edit_Valor, edit_Desc;
    private Button btn_Cadastrar;
    String[] mensagens = {"Preencha todos os campos!","Cadastro realizado com sucesso!"};
    String usuarioID, cod;

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

                    edit_Cod.setText("");
                    edit_Nome.setText("");
                    edit_Qtd.setText("");
                    edit_Valor.setText("");
                    edit_Desc.setText("");
                }
            }
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




    private void CadastrarProduto(){
        String cod = edit_Cod.getText().toString();
        String nome= edit_Nome.getText().toString();
        String qtd= edit_Qtd.getText().toString();
        String valor = edit_Valor.getText().toString();
        String desc = edit_Desc.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> produtos = new HashMap<>();
        produtos.put("cod",cod);
        produtos.put("nome", nome);
        produtos.put("qtd",qtd);
        produtos.put("valor",valor);
        produtos.put("desc",desc);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference =db.collection("Produtos").document(cod);
        documentReference.set(produtos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");
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
    }
}