package br.unifor.victor.contacerta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.adapter.CustomAdapter;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Base64Custom;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.model.Contato;
import br.unifor.victor.contacerta.model.DataModel;
import br.unifor.victor.contacerta.model.Produto;

public class NovoProdutoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference firebase;
    String identificadorUsuarioLogado;
    private Produto produto;
    private String idProduto;
    private String codContaFirebase;
    private ArrayList<DataModel> consumidores;
    private ArrayList<Contato> contatos;
    private List<String> pessoasPorProdutos2 = new ArrayList<String>();
    private ListView listViewConsumidores;
    private ArrayAdapter adapterConsumidores;
    private String codContaGlobal;
    private Intent it5;
    private boolean selecionarConsumidor;



//    EditText editTextNovaConta;
//    Button btNovaConta;
//    String identificadorConta;
//    private ArrayAdapter adapter;
//    private ArrayList<String> contas;
//    private ListView listView;
//
//    String codContaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);
     //   toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   toolbar.setTitle("Conta Certa");


        it5 = getIntent();
        codContaGlobal = it5.getStringExtra("idconta");
        Log.i("******TELA NOVO PROD******", codContaGlobal);

        firebase = ConfiguracaoFirebase.getFirebase();
        // instanciar objetos
        contatos = new ArrayList<>();

        // recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(NovoProdutoActivity.this);
        identificadorUsuarioLogado = preferencias.getIdentificador();



        listViewConsumidores = (ListView) findViewById(R.id.lv_consumidores2);

        consumidores = new ArrayList<>();



        firebase.child("contas").child(codContaGlobal).child("contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // limpar lista
                contatos.clear();

                // listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                    consumidores.add(new DataModel(contato.getNome(), false, contato.getIdentificadorUsuario()));

                }
                adapterConsumidores.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // monta listview e adapter
//        listViewConsumidores = (ListView) findViewById(R.id.lv_consumidores2);
//        adapterConsumidores = new ArrayAdapter(NovoProdutoActivity.this, android.R.layout.simple_list_item_1, consumidores);
//        listViewConsumidores.setAdapter(adapterConsumidores);

        adapterConsumidores = new CustomAdapter(consumidores, getApplicationContext());
        listViewConsumidores.setAdapter(adapterConsumidores);
        listViewConsumidores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= consumidores.get(position);
                dataModel.checked = !dataModel.checked;
                adapterConsumidores.notifyDataSetChanged();
                selecionarConsumidor = true;



            }
        });

        final EditText etNome = (EditText) findViewById(R.id.nome_produto2);
        final EditText etPreco = (EditText) findViewById(R.id.preco_produto2);
      //  final EditText etPessoasConsumo = (EditText) findViewById(R.id.pessoas_consumo2);
        Button btSalvar = (Button) findViewById(R.id.bt_salvar_produto);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nomeProduto = etNome.getText().toString();
                String precoProduto = etPreco.getText().toString();
               // String pessoasConsumo = etPessoasConsumo.getText().toString();
                String idPessoaProduto = identificadorUsuarioLogado;




                if (nomeProduto.isEmpty() || precoProduto.isEmpty()  ) {
                    Toast.makeText(NovoProdutoActivity.this, "Ops! Você esqueceu de prencher os campos.", Toast.LENGTH_SHORT).show();

                }

                else if (!selecionarConsumidor) {
                    Toast.makeText(NovoProdutoActivity.this, "Selecione pelo menos um consumidor.", Toast.LENGTH_SHORT).show();
                }

                else {


                    for(int i=0;i<contatos.size();i++){
                        if(consumidores.get(i).checked) {
                            Log.i("PESSOAS", contatos.get(i).getNome());
                            pessoasPorProdutos2.add(contatos.get(i).getIdentificadorUsuario());
                        }
                    }



                    produto = new Produto();
                    produto.setNome(nomeProduto);
                    produto.setValor(precoProduto);
                    produto.setIdPessoaProduto(identificadorUsuarioLogado);
                    produto.setPessoasPorProdutos(pessoasPorProdutos2);
                    idProduto = Base64Custom.codificarParaBase64(nomeProduto);

                    firebase.child("usuarios").child(identificadorUsuarioLogado).child("minhascontas").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot todasContas: dataSnapshot.getChildren()){
                               // codContaFirebase = todasContas.getKey();

                                firebase.child("contas").child(codContaGlobal).child("produtos").child(idProduto).setValue(produto);

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    finish();
                }



            }
        });





    }















}
