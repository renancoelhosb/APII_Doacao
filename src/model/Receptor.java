package model;

public class Receptor extends Pessoa {
    private static final long serialVersionUID = 1L;
    private double renda;

    public Receptor(String nome, long identificacao, long telefone, double renda) {
        super(nome, identificacao, telefone);
        this.renda = renda;
    }

    public double getRenda() {
        return renda;
    }
}