package br.unifor.victor.contacerta.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.unifor.victor.contacerta.config.ConfiguracaoFirebase;

/**
 * Created by victor on 07/06/2017.
 */

public class Usuario {
    private String id;
    private String uid;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
