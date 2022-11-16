package com.marcelolimot.appi3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marcelolimot.appi3.model.produtos;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

public class GraficoAdapter extends RecyclerView.Adapter {

    List<com.marcelolimot.appi3.model.produtos> listaProdutos;
    private Context context;
    int cor;


    public GraficoAdapter(grafico lista_produtos, List<produtos> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grafico_item, parent, false);

        context = view.getContext();
        GraficoAdapter.ViewHolderClass vhClass = new GraficoAdapter.ViewHolderClass(view);
        return vhClass;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GraficoAdapter.ViewHolderClass vhClass = (GraficoAdapter.ViewHolderClass) holder;
        produtos prod = listaProdutos.get(position);


        int hash = prod.getNome().hashCode();
        int hash2 = String.valueOf(prod.getQtd()).hashCode();
        int qtd = prod.getQtd();
        int total = somarEstoque();
        double porcentagem = (qtd*100)/total;
        cor = Color.rgb(hash, hash2, 0);
        vhClass.txt_nome_Produto.setText(prod.getNome());
        vhClass.txt_porcentagem.setText(String.valueOf(porcentagem) + " %");
        vhClass.corProd.setBackground(oval(cor,vhClass.corProd));

        //Log.i("graficoAdapter.java", String.valueOf(hash));
        //Log.i("graficoAdapter.java", String.valueOf(hash2));
    }



    @Override
    public int getItemCount() {
        //Log.i("QTD Produtos:", String.valueOf(listaProdutos.size()));
        return listaProdutos.size();
    }

    public int somarEstoque(){
        int total, i, cont,result;
        int position;
        cont = 0;

        for(i=0; i <listaProdutos.size(); i++){
            produtos prod = listaProdutos.get(i);
            cont = cont + prod.getQtd();
        }
        return cont;
    }



    public class ViewHolderClass extends RecyclerView.ViewHolder {
        public View context;
        private TextView txt_nome_Produto, txt_porcentagem, corProd;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txt_nome_Produto = itemView.findViewById(R.id.txt_nome_Produto);
            txt_porcentagem = itemView.findViewById(R.id.txt_porcentagem);
            corProd = itemView.findViewById(R.id.corProd);
        }


    }
    private static ShapeDrawable oval(@ColorInt int color, View view){
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setIntrinsicHeight(view.getHeight());
        shapeDrawable.setIntrinsicWidth(view.getWidth());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }
}
