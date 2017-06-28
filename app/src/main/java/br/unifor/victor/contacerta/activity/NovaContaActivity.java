package br.unifor.victor.contacerta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import br.unifor.victor.contacerta.R;
import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;
import br.unifor.victor.contacerta.helper.Base64Custom;
import br.unifor.victor.contacerta.helper.Preferencias;
import br.unifor.victor.contacerta.model.Contato;
import br.unifor.victor.contacerta.model.Usuario;

public class NovaContaActivity extends AppCompatActivity {
    EditText editTextNovaConta;
    Button btNovaConta;
    String identificadorConta;
    private ArrayList<String> contas;
    private ListView listView;
    private ArrayAdapter adapter;
    private DatabaseReference firebase;
    private DatabaseReference firebase2;
    String identificadorUsuarioLogado;
    String codContaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_conta);
        firebase = ConfiguracaoFirebase.getFirebase();
        firebase2 = ConfiguracaoFirebase.getFirebase();

        contas = new ArrayList<>();
//        contas.add("TEst");
//        contas.add("TEst");
//        contas.add("TEst");
//        contas.add("TEst");
//        contas.add("TEst");

        // monta listview e adapter
        listView = (ListView) findViewById(R.id.lv_contas);
        adapter = new ArrayAdapter(NovaContaActivity.this, android.R.layout.simple_list_item_1, contas);
        listView.setAdapter(adapter);

        editTextNovaConta = (EditText) findViewById(R.id.edt_nova_conta);
        btNovaConta = (Button) findViewById(R.id.bt_nova_conta);

        Preferencias preferencias = new Preferencias(this);
        identificadorUsuarioLogado = preferencias.getIdentificador();



        firebase.child("usuarios").child(identificadorUsuarioLogado).child("minhascontas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot todasContas: dataSnapshot.getChildren()){
                    codContaFirebase = todasContas.getKey();
                    contas.add(codContaFirebase);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                identificadorConta = editTextNovaConta.getText().toString();
                if (identificadorConta.isEmpty()) {
                    Toast.makeText(NovaContaActivity.this, "Preencha o identificador", Toast.LENGTH_SHORT).show();

                } else {
                    firebase.child("usuarios").child(identificadorUsuarioLogado).child("minhascontas").child(identificadorConta).setValue(true);


                    firebase.child("usuarios").child(identificadorUsuarioLogado).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                // recuperar dados do contato a ser adicionado
                                Usuario usuarioLogad = dataSnapshot.getValue(Usuario.class);
                                // recuperar identificador usuario logado (base 64)

                                final Contato contato = new Contato();
                                    contato.setIdentificadorUsuario(identificadorUsuarioLogado);
                                    contato.setEmail(usuarioLogad.getEmail());
                                    contato.setNome(usuarioLogad.getNome());
                                    firebase.child("contas").child(codContaFirebase).child("contatos").child(identificadorUsuarioLogado).setValue(contato);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







                    Intent it = new Intent(NovaContaActivity.this, MainActivity.class);
                    it.putExtra("idconta",identificadorConta);

                    startActivity(it);
                    finish();

                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Conta escolhida: " + contas.get(position) , Toast.LENGTH_LONG).show();

                Intent it2 = new Intent(NovaContaActivity.this, MainActivity.class);
                it2.putExtra("idconta",contas.get(position));

                startActivity(it2);
                finish();

            }
        });




    }

}
