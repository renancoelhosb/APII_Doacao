package model;

public class Doador extends Pessoa {
    private int qtdDoacoes;

    public Doador(String nome, int identificacao, int telefone, int qtdDoacoes) {
        super(nome, identificacao, telefone);
        this.qtdDoacoes = qtdDoacoes;
    }

    public int getQtdDoacoes() {
        return qtdDoacoes;
    }
    
    public void registrarDoacao() {
        this.qtdDoacoes++;
    }
}