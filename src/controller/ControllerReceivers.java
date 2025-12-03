package controller;

import java.io.Serializable;
import java.util.ArrayList;
import model.Receptor;

public class ControllerReceivers implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Receptor> receptores;

    public ControllerReceivers() {
        this.receptores = new ArrayList<Receptor>();
    }

    public ArrayList<Receptor> getReceivers() {
        return receptores;
    }

    public void addReceptor(String nome, long identificacao, long telefone, double renda) {
        this.receptores.add(new Receptor(nome, identificacao, telefone, renda));
    }
    public boolean removeReceptor(long id) {
        Receptor r = getReceptor(id);
        if (r != null) {
            return this.receptores.remove(r);
        }
        return false;
    }

    public boolean removeReceptor(Receptor r) {
        return this.receptores.remove(r);
    }
    public Receptor getReceptor(long id) {
        for (Receptor r : this.receptores) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}