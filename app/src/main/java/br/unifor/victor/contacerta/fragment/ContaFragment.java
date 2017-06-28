package br.unifor.victor.contacerta.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.activity.MainActivity;
import br.unifor.victor.contacerta.adapter.ContatoAdapter;
import br.unifor.victor.contacerta.adapter.ContatoContaAdapter;
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
   // private ArrayList<Produto> produtos1;
   private ArrayList<Contato> contatos;
  //  private ArrayList<Contato> contatos1;
    private TextView txtContaTotal;
    private TextView txtTotalpessoas;
    public double contaTotal;
    private double contaPorPessoa;
    private ListView listView;
    private ArrayAdapter adapter;
    private int numeroPessoasPorPedido;
    private double contaPessoa2;
    private double contaPessoa;


    private  String codContaFirebase;
    private String myDataFromActivity;

    LinearLayout lllistview;
    LinearLayout lltextview;
    ToggleButton toggle;
    Switch switch1;

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
        // instanciar objetos
        contatos = new ArrayList<>();
        produtos = new ArrayList<>();

        final View view = inflater.inflate(R.layout.fragment_conta, container, false);
        // monta listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos_contas);
        adapter = new ContatoContaAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);




        txtContaTotal = (TextView) view.findViewById(R.id.textViewConta);
        txtTotalpessoas = (TextView) view.findViewById(R.id.textPorPessoa);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        lllistview = (LinearLayout) view.findViewById(R.id.linear_listview);
        lltextview = (LinearLayout) view.findViewById(R.id.linear_textview_pessoa);
      //  toggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        switch1 = (Switch) view.findViewById(R.id.switch1);


        lltextview.setVisibility(View.VISIBLE);
        lllistview.setVisibility(View.GONE);


//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    lllistview.setVisibility(View.VISIBLE);
//                    lltextview.setVisibility(View.GONE);
//
//                } else {
//                    // The toggle is disabled
//                    lltextview.setVisibility(View.VISIBLE);
//                    lllistview.setVisibility(View.GONE);
//
//                }
//            }
//        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lllistview.setVisibility(View.VISIBLE);
                    lltextview.setVisibility(View.GONE);

                } else {
                    // The toggle is disabled
                    lltextview.setVisibility(View.VISIBLE);
                    lllistview.setVisibility(View.GONE);

                }
            }
        });




        firebase.child("contas").child(codContaFirebase).child("produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Produto produto1 = dados.getValue(Produto.class);
                    contaTotal = contaTotal + Double.parseDouble(produto1.getValor());
                    txtContaTotal.setText(String.format("%.2f",(contaTotal)));
                    Log.i("PRECO DO PRODUTO",produto1.getValor());
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

        //listview
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

                            produtos.clear();
                            Produto produto = dados.getValue(Produto.class);
                            produtos.add(produto);

                            String x = contatos.get(position).getIdentificadorUsuario();
                            String y = produto.getIdPessoaProduto().toString();

                            List<String> ppp = produto.getPessoasPorProdutos();
                            numeroPessoasPorPedido = ppp.size();

                            //pecorrendo lista de pessoas que compartilham produto
                            for(int i=0;i<ppp.size();i++){
                                Log.i("*****VALOR DO LIST******", ppp.get(i).toString());

                                if (x.equals(ppp.get(i).toString())){
                                    double valorProd = Double.parseDouble(produto.getValor())/numeroPessoasPorPedido;
                                    contaPessoa2 += valorProd;

                                }

                            }



                            if (x.equals(y)){
                                double valorProd = Double.parseDouble(produto.getValor())/numeroPessoasPorPedido;
                                contaPessoa += valorProd;
                            }

                        }

                        // contaPessoa2 = contaPessoa2/numeroPessoasPorPedido;
                        contatos.get(position).setValorConta(contaPessoa2);
                        Log.i("*****CONTA POR PESSOA nome******", String.valueOf(contatos.get(position).getNome()));
                        Log.i("*****CONTA POR PESSOA valor******", String.valueOf(contatos.get(position).getValorConta()));

                        Toast.makeText(getActivity(), "A conta da " + contatos.get(position).getNome() + " é " + String.format("%.2f",(contatos.get(position).getValorConta())) , Toast.LENGTH_LONG).show();
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





        // Inflate the layout for this fragment
        return view;
    }







}
