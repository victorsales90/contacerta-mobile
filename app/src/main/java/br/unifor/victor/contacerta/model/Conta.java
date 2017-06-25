package br.unifor.victor.contacerta.model;

import java.util.ArrayList;

/**
 * Created by victo on 13/06/2017.
 */

public class Conta {

    String identificadorConta;
    ArrayList <Produto> produtos;
    ArrayList <Contato> contatos;


    public Conta() {
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }
}
