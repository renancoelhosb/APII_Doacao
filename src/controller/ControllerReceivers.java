package controller;

import java.util.ArrayList;
import model.Receptor;

public class ControllerReceivers {
    private ArrayList<Receptor> receptores;

    public ControllerReceivers() {
        this.receptores = new ArrayList<Receptor>();
    }

    public ArrayList<Receptor> getReceivers() {
        return receptores;
    }
    
    public void addReceptor(String nome, int identificacao, int telefone, double renda) {
        this.receptores.add(new Receptor(nome, identificacao, telefone, renda));
    }

    public boolean removeReceptor(int id) {
        Receptor r = getReceptor(id);
        if (r != null) {
            return this.receptores.remove(r);
        }
        return false;
    }

    public boolean removeReceptor(Receptor r) {
        return this.receptores.remove(r);
    }

    public Receptor getReceptor(int id) {
        for (Receptor r : this.receptores) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}