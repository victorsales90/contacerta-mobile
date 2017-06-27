package br.unifor.victor.contacerta.model;



public class DataModel {

    public String name;
    public boolean checked;
    private String identificadorUsuario;

    public DataModel(String name, boolean checked, String identificadorUsuario) {
        this.name = name;
        this.checked = checked;
        this.identificadorUsuario = identificadorUsuario;

    }

}