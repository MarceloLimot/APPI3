package com.marcelolimot.appi3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.marcelolimot.appi3.databinding.ActivityListaProdutosBinding;
import com.marcelolimot.appi3.model.produtos;

public class ProdutoAdapter extends RecyclerView.Adapter {


    List<com.marcelolimot.appi3.model.produtos> listaProdutos;
    private Context context;

    public ProdutoAdapter(lista_produtos lista_produtos, List<produtos> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_item, parent, false);

        context = view.getContext();
        ProdutoAdapter.ViewHolderClass vhClass = new ProdutoAdapter.ViewHolderClass(view);
        return vhClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProdutoAdapter.ViewHolderClass vhClass = (ProdutoAdapter.ViewHolderClass) holder;
        produtos prod = listaProdutos.get(position);

        String cod;
        String imgUrl = prod.getImgUrl();
        vhClass.txt_nome_Produto.setText(prod.getNome());
        vhClass.txt_qtd_produto.setText(prod.getQtd());
        vhClass.txt_valor_produto.setText(prod.getValor());
        cod = prod.getCod();

        if(imgUrl != ""){
            Glide.with(((ViewHolderClass) holder).imgProd).load(prod.getImgUrl()).into(vhClass.imgProd);
        }


        vhClass.btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,cad_produto.class);

                //intent.putExtra("edit_Nome", prod.getNome());
                intent.putExtra("cod", prod.getCod());
                intent.putExtra("nome", prod.getNome());
                intent.putExtra("qtd", prod.getQtd());
                intent.putExtra("valor", prod.getValor());
                intent.putExtra("desc", prod.getDesc());
                intent.putExtra("imgUrl", prod.getImgUrl());
                intent.putExtra("imgDefault", R.drawable.ic_cam_produto);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);



                Log.d("Clique", "Botao foi clicado! COD:" + cod);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        public View context;
        private TextView txt_nome_Produto, txt_qtd_produto, txt_valor_produto;
        private ImageView imgProd;
        private ImageFilterButton btn_edit;
        private String cod;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txt_nome_Produto = itemView.findViewById(R.id.txt_nome_Produto);
            txt_qtd_produto = itemView.findViewById(R.id.txt_qtd_produto);
            txt_valor_produto = itemView.findViewById(R.id.txt_valor_produto);
            imgProd = itemView.findViewById(R.id.imgProd);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }
    }

}
