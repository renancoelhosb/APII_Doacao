package model;

public abstract class Pessoa {
    private String nome;
    private int identificacao;
    private int telefone;

    public Pessoa(String nome, int identificacao, int telefone) {
        this.nome = nome;
        this.identificacao = identificacao;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
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