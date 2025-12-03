package controller;

import java.io.Serializable;
import java.util.ArrayList;
import model.Doador;

public class ControllerDoadores implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Doador> doador;

    public ControllerDoadores() {
        this.doador = new ArrayList<Doador>();
    }

    public ArrayList<Doador> getDoadores() {
        return doador;
    }
    
    public void addDoador(String nome, long identificacao, int telefone) {
        this.doador.add(new Doador(nome, identificacao, telefone, 0));
    }

    public boolean removeDoador(long id) {
        Doador d = getDoador(id);
        if (d != null) {
            return this.doador.remove(d);
        }
        return false;
    }

    public boolean removeDoador(Doador d) {
        return this.doador.remove(d);
    }

    public Doador getDoador(long id) {
        for (Doador r : this.doador) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}