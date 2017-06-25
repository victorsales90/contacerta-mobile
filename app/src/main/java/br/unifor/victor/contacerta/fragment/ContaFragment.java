package br.unifor.victor.contacerta.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.activity.MainActivity;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.model.Contato;
import br.unifor.victor.contacerta.model.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContaFragment extends Fragment {

    private DatabaseReference firebase;

    private ArrayList<Produto> produtos;
    private ArrayList<Contato> contatos;
    private TextView txtContaTotal;
    private TextView txtTotalpessoas;
    public double contaTotal;
    private double contaPorPessoa;

    private  String codContaFirebase;
    private String myDataFromActivity;


    public ContaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        codContaFirebase = activity.getMyCodConta();
        Log.i("******TELA CONTA FRAG******",codContaFirebase);

        firebase = ConfiguracaoFirebase.getFirebase();

        final View view = inflater.inflate(R.layout.fragment_conta, container, false);

        txtContaTotal = (TextView) view.findViewById(R.id.textViewConta);
        txtTotalpessoas = (TextView) view.findViewById(R.id.textPorPessoa);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();


        firebase.child("contas").child(codContaFirebase).child("produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    contaTotal = contaTotal + Double.parseDouble(produto.getValor());
                    txtContaTotal.setText(String.format("%.2f",(contaTotal)));
                    Log.i("PRECO DO PRODUTO",produto.getValor());
                    Log.i("SOMA DA CONTA", String.valueOf(contaTotal));

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase.child("contas").child(codContaFirebase).child("contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totalPessoas = 0;
                // limpar lista
                //  contatos.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    //produtos.add(produto);

                    totalPessoas += 1;

                }
                contaPorPessoa = contaTotal/totalPessoas;
                txtTotalpessoas.setText(String.format("%.2f",(contaPorPessoa)));
                Log.i("************TOTAL DE PESSOAS************", String.valueOf(totalPessoas));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










        // Inflate the layout for this fragment
        return view;
    }

}
