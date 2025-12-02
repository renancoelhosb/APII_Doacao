package model;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String usuario;
    private String senha;
    private boolean ehMaster;

    public Usuario(String usuario, String senha, boolean ehMaster) {
        this.usuario = usuario;
        this.senha = senha;
        this.ehMaster = ehMaster;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isMaster() {
        return ehMaster;
    }

    public void setSenha(String novaSenha) {
        this.senha = novaSenha;
    }

}