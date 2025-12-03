package controller;

import java.util.ArrayList;
import model.Doacao;
import model.Item;
import model.Receptor;

public class ControllerDoacoes {
    private ArrayList<Doacao> doacoes;

    public ControllerDoacoes() {
        this.doacoes = new ArrayList<Doacao>();
    }

    public ArrayList<Doacao> getDoacoes() {
        return doacoes;
    }

    public boolean insertDoacao(Receptor receptor, Item item) {
        
        return this.doacoes.add(new Doacao(receptor, item));
    }

    public ArrayList<Receptor> getReceptores() {
        ArrayList<Receptor> receptores = new ArrayList<Receptor>();
        for (Doacao doacao : doacoes) {
            receptores.add(doacao.getReceptor());
        }
        return receptores;
    }

    public ArrayList<Doacao> getDoacao(Receptor receptor) {
        ArrayList<Doacao> doacoesReceptor = new ArrayList<Doacao>();
        for (Doacao doacao : doacoes) {
            if (doacao.getReceptor().equals(receptor)) {
                doacoesReceptor.add(doacao);
            }
        }
        return doacoesReceptor;
    }

    public void removeDoacao(Receptor receptor, Item item) {
        doacoes.removeIf(doacao -> doacao.getReceptor().equals(receptor) && doacao.getItem().equals(item));
    }

    public void concludeRequest(Receptor receptor, Item item) {
        // Lógica para concluir a solicitação
        removeDoacao(receptor, item);
        // Outras operações podem ser adicionadas aqui
    }
}