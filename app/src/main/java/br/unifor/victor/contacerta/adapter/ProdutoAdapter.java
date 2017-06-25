package br.unifor.victor.contacerta.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.model.Produto;

/**
 * Created by victo on 09/06/2017.
 */

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    private ArrayList<Produto> produtos;
    private Context context;

    private double precoProdutos;

    public ProdutoAdapter(@NonNull Context c, @NonNull ArrayList<Produto> objects) {
        super(c, 0 , objects);
        this.produtos = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(produtos != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_produto, parent, false);

            TextView nomeProduto = (TextView) view.findViewById(R.id.tv_nome);
            TextView precoProduto = (TextView) view.findViewById(R.id.tv_preco);
            Produto produto = produtos.get(position);

            precoProdutos = Double.parseDouble(produto.getValor());


            nomeProduto.setText(produto.getNome());
            precoProduto.setText(String.format("%.2f",( precoProdutos)));



        }

        return view;
    }
}
