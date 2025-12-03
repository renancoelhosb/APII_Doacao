package model;

import java.io.Serializable;

public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private long identificacao;
    private int telefone;

    public Pessoa(String nome, long identificacao, int telefone) {
        this.nome = nome;
        this.identificacao = identificacao;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public long getId() {
        return identificacao;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
}