package br.unifor.victor.contacerta.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.activity.MainActivity;
import br.unifor.victor.contacerta.adapter.ContatoAdapter;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.model.Contato;
import br.unifor.victor.contacerta.model.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Produto> produtos1;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private String codContaFirebase;
    String identificadorUsuarioLogado;
    private double contaPessoa;
    private double contaPessoa2;
    private int numeroPessoasPorPedido;


    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        codContaFirebase = activity.getMyCodConta();
        Log.i("******TELA CONTATO FRAG******",codContaFirebase);

        firebase = ConfiguracaoFirebase.getFirebase();

        // instanciar objetos
        contatos = new ArrayList<>();
        produtos1 = new ArrayList<>();

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // monta listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);
//        adapter = new ArrayAdapter(getActivity(), R.layout.lista_contato, contatos);

        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);

        // recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        identificadorUsuarioLogado = preferencias.getIdentificador();



        firebase.child("contas").child(codContaFirebase).child("contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // limpar lista
                contatos.clear();

                // listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                firebase.child("contas").child(codContaFirebase).child("produtos").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dados: dataSnapshot.getChildren()){
                            Produto produto1 = dados.getValue(Produto.class);
                            produtos1.clear();

                            produtos1.add(produto1);
                            String x = contatos.get(position).getIdentificadorUsuario();
                            String y = produto1.getIdPessoaProduto().toString();
                            List<String> ppp = produto1.getPessoasPorProdutos();
                            numeroPessoasPorPedido = ppp.size();

                            //pecorrendo lista de pessoas que compartilham produto
                            for(int i=0;i<ppp.size();i++){
                                Log.i("*****VALOR DO LIST******", ppp.get(i).toString());

                                if (x.equals(ppp.get(i).toString())){
                                    contaPessoa2 += Double.parseDouble(produto1.getValor());

                                }

                            }



                            if (x.equals(y)){
                               contaPessoa += Double.parseDouble(produto1.getValor());
                            }

                        }

                        contaPessoa2 = contaPessoa2/numeroPessoasPorPedido;
                        contatos.get(position).setValorConta(contaPessoa2);
                        Log.i("*****CONTA POR PESSOA nome******", String.valueOf(contatos.get(position).getNome()));
                        Log.i("*****CONTA POR PESSOA valor******", String.valueOf(contatos.get(position).getValorConta()));

                        Toast.makeText(getActivity(), "A conta da " + contatos.get(position).getNome() + " é " + String.valueOf(contatos.get(position).getValorConta()) , Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), "A conta da " + contatos.get(position).getNome() + " é " + contaPessoa , Toast.LENGTH_LONG).show();
                        contaPessoa = 0.0;
                        contaPessoa2 = 0.0;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });



            }


        });



        return view;
    }

}
