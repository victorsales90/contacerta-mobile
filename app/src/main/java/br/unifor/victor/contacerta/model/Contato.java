package br.unifor.victor.contacerta.model;

import com.google.firebase.database.Exclude;

/**
 * Created by victo on 08/06/2017.
 */

public class Contato {

    private String identificadorUsuario;
    private String nome;
    private String email;

    public Contato() {
    }

    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String identificadorUsuario) {
        this.identificadorUsuario = identificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}