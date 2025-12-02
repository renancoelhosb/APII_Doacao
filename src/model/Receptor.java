package model;

public class Receptor extends Pessoa {
    private double renda;

    public Receptor(String nome, int identificacao, int telefone, double renda) {
        super(nome, identificacao, telefone);
        this.renda = renda;
    }

    public double getRenda() {
        return renda;
    }
}