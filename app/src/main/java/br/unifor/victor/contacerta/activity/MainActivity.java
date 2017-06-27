package br.unifor.victor.contacerta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.adapter.TabAdapter;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Base64Custom;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.helper.SlidingTabLayout;
import br.unifor.victor.contacerta.model.Contato;
import br.unifor.victor.contacerta.model.Produto;
import br.unifor.victor.contacerta.model.Usuario;

public class MainActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    //layout
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private String identificadorContato;
    private String idProduto;
    private String identificadorConta;
    private String identificadorUsuarioLogado;
    private String codContaFirebase;
    private Intent it;
    private String codContaGlobal;
    private double contaTotal;
    private Button botaoSair;
    private Contato contato2;
    private Produto produto;
    private List<String> pessoasPorProdutos2 = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        it = getIntent();
        codContaGlobal = it.getStringExtra("idconta");
        Log.i("******TELA MAIN ACT******", codContaGlobal);

        firebase = ConfiguracaoFirebase.getFirebase();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Conta Certa");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sltl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        // configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);

        // configurar adpater
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        Preferencias preferencias = new Preferencias(this);
        identificadorUsuarioLogado = preferencias.getIdentificador();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.item_configuracoes:
                novaConta();
                return true;

            case R.id.item_add:
                abrirCadastroContato();
                return true;

            case R.id.item_pesquisa:
                abrirCadastroProduto();
                return true;

            case R.id.item_pesquisa2:
                abrirCadastroProduto2();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void novaConta() {
        Intent it3 = new Intent(MainActivity.this, NovaContaActivity.class);
        startActivity(it3);
        finish();

    }


    private void abrirCadastroProduto() {

        Intent it5 = new Intent(MainActivity.this, NovoProdutoActivity.class);
        it5.putExtra("idconta",codContaGlobal);
        startActivity(it5);


    }

    private void abrirCadastroContato() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // configurações dialog
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        // configura editText
        final EditText editText2 = new EditText(MainActivity.this);
        alertDialog.setView(editText2);


        // configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText2.getText().toString();
                if (emailContato.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Olá, você esqueceu de preencher o e-mail", Toast.LENGTH_SHORT).show();

                } else {
                    // verifica se usuário está cadastrado
                    identificadorContato = Base64Custom.codificarParaBase64(emailContato);


                    firebase.child("usuarios").child(identificadorUsuarioLogado).child("minhascontas").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot todasContas: dataSnapshot.getChildren()){
                                codContaFirebase  = todasContas.getKey();

                                // recupera instancia firebase
                                firebase.child("usuarios").child(identificadorContato).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {

                                            // recuperar dados do contato a ser adicionado
                                            Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);


                                            // recuperar identificador usuario logado (base 64)
                                            Preferencias preferencias = new Preferencias(MainActivity.this);
                                            String identificadorUsuarioLogado = preferencias.getIdentificador();


                                            final Contato contato1 = new Contato();
                                            contato1.setIdentificadorUsuario(identificadorContato);
                                            contato1.setEmail(usuarioContato.getEmail());
                                            contato1.setNome(usuarioContato.getNome());


                                            firebase.child("usuarios").child(identificadorUsuarioLogado).child("minhascontas").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot todasContas: dataSnapshot.getChildren()){
                                                        codContaFirebase = todasContas.getKey();

                                                        firebase.child("contas").child(codContaFirebase).child("contatos").child(identificadorContato).setValue(contato1);
                                                        firebase.child("usuarios").child(identificadorContato).child("minhascontas").child(codContaFirebase).setValue(true);



                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });



                                        } else {
                                            Toast.makeText(MainActivity.this, "Usuário não possui cadastro", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }


            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    public void deslogarUsuario() {
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public String getMyCodConta() {
        return codContaGlobal;
    }


    private void abrirCadastroProduto2() {


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);

        final EditText etNome = (EditText) alertLayout.findViewById(R.id.nome_produto);
        final EditText etPreco = (EditText) alertLayout.findViewById(R.id.preco_produto);
        final EditText etPessoasConsumo = (EditText) alertLayout.findViewById(R.id.pessoas_consumo);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);


        // configura botões
        alert.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nomeProduto = etNome.getText().toString();
                String precoProduto = etPreco.getText().toString();
                String pessoasConsumo = etPessoasConsumo.getText().toString();
                String idPessoaProduto = identificadorUsuarioLogado;



                if (nomeProduto.isEmpty() || precoProduto.isEmpty() ) {
                    Toast.makeText(MainActivity.this, "Ops, você esqueceu de prencher os campos.", Toast.LENGTH_SHORT).show();

                } else {

                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    final String identificadorUsuarioLogado = preferencias.getIdentificador();

                    pessoasPorProdutos2.add("dmljdG9yQGdtYWlsLmNvbQ==");
                    pessoasPorProdutos2.add(pessoasConsumo);

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

                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create();
        alert.show();

    }

}
