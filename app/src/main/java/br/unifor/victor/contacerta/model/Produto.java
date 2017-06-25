package br.unifor.victor.contacerta.model;

/**
 * Created by victo on 09/06/2017.
 */

public class Produto {

    String nome;
    String valor;
    String idProduto;
    String idPessoaProduto;

    public Produto() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdPessoaProduto() {
        return idPessoaProduto;
    }

    public void setIdPessoaProduto(String idPessoaProduto) {
        this.idPessoaProduto = idPessoaProduto;
    }
}

