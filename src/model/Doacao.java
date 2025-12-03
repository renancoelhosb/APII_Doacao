package model;

import java.io.Serializable;

public class Doacao implements Serializable{
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