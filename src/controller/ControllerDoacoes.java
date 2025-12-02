package controller;

import java.util.ArrayList;
import model.Doacao;

public class ControllerDoacoes {
    private ArrayList<Doacao> doacoes;

    public ControllerDoacoes() {
        this.doacoes = new ArrayList<Doacao>();
    }

    public ArrayList<Doacao> getDoacoes() {
        return doacoes;
    }

    public boolean insertDoacao(Doacao doacao) {
        if (doacao.getDoador() != null) {
            doacao.getDoador().registrarDoacao();
        }
        return this.doacoes.add(doacao);
    }
}