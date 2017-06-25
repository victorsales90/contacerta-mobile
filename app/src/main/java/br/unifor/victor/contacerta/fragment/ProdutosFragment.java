package br.unifor.victor.contacerta.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.activity.MainActivity;
import br.unifor.victor.contacerta.adapter.ProdutoAdapter;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.model.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Produto> produtos;
    private DatabaseReference firebase;
    public double contaTotal;
    private String codContaFirebase;

    public ProdutosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        codContaFirebase = activity.getMyCodConta();
        Log.i("******TELA PRODUTO FRAG******",codContaFirebase);

        firebase = ConfiguracaoFirebase.getFirebase();
        // instanciar objetos
        produtos = new ArrayList<>();


        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        // monta listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_produtos);
//        adapter = new ArrayAdapter(
//                getActivity(),
//                R.layout.lista_produto,
//                produtos
//        );

        adapter = new ProdutoAdapter(getActivity(), produtos);
        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase.child("contas").child(codContaFirebase).child("produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // limpar lista
                produtos.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    produtos.add(produto);

                    contaTotal = contaTotal + Double.parseDouble(produto.getValor());

                    Log.i("PRECO DO PRODUTO",produto.getValor());
                    Log.i("SOMA DA CONTA", String.valueOf(contaTotal));
                    //    Conta conta = new Conta();
                    //   conta.setContalTotal(contaTotal);

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        return view;
    }


}
