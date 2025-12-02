package controller;

import java.util.ArrayList;
import model.Doador;
import model.Pessoa;

public class ControllerPessoa {
    private ArrayList<Pessoa> pessoas;

    public ControllerPessoa() {
        this.pessoas = new ArrayList<>();
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }
    
    public ArrayList<Doador> getDoadores() {
        ArrayList<Doador> doadores = new ArrayList<>();
        for (Pessoa p : pessoas) {
            if (p instanceof Doador) {
                doadores.add((Doador) p);
            }
        }
        return doadores;
    }
    
    public boolean addPessoa(Pessoa novaPessoa) {
        if (getPessoa(novaPessoa.getId()) == null) {
            return this.pessoas.add(novaPessoa);
        }
        return false;
    }

    public boolean removePessoa(int id) {
        Pessoa p = getPessoa(id);
        if (p != null) {
            return this.pessoas.remove(p);
        }
        return false;
    }

    public Pessoa getPessoa(int id) {
        for (Pessoa p : this.pessoas) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}