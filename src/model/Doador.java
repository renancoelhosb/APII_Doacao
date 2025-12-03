package model;

public class Doador extends Pessoa {
    private static final long serialVersionUID = 1L;
    private int qtdDoacoes;


    public Doador(String nome, long identificacao, long telefone, int qtdDoacoes) {
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