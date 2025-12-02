package model;

public class Doacao {
    private Doador doador;
    private Item item;

    public Doacao(Doador doador, Item item) {
        this.doador = doador;
        this.item = item;
    }

    public Doador getDoador() {
        return doador;
    }

    public Item getItem() {
        return item;
    }
}