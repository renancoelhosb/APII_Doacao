package controller;

import java.util.ArrayList;
import model.Doador;

public class ControllerDoadores {
    private ArrayList<Doador> doador;

    public ControllerDoadores() {
        this.doador = new ArrayList<Doador>();
    }

    public ArrayList<Doador> getDoadores() {
        return doador;
    }
    
    public void addDoador(String nome, int identificacao, int telefone) {
        this.doador.add(new Doador(nome, identificacao, telefone, 0));
    }

    public boolean removeDoador(int id) {
        Doador d = getDoador(id);
        if (d != null) {
            return this.doador.remove(d);
        }
        return false;
    }

    public boolean removeDoador(Doador d) {
        return this.doador.remove(d);
    }

    public Doador getDoador(int id) {
        for (Doador r : this.doador) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}