package br.unifor.victor.contacerta.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.model.Contato;

/**
 * Created by victo on 09/06/2017.
 */

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;


    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Contato> objects) {
        super(c, 0 , objects);
        this.contatos = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(contatos != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_contato, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome_contato);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_preco);

            Contato contato = contatos.get(position);



            nomeContato.setText(contato.getNome());
       //  emailContato.setText(contato.getEmail());

        }

        return view;
    }
}
