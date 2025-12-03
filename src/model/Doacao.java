package model;

public class Doacao {
    private Receptor receptor;
    private Item item;

    public Doacao(Receptor receptor, Item item) {
        this.receptor = receptor;
        this.item = item;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public Item getItem() {
        return item;
    }
}