package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import model.Doacao;
import model.Item;
import model.Receptor;

public class ControllerDoacoes implements Serializable {
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

    // CORRIGIDO: retorna receptores únicos (sem duplicatas)
    public ArrayList<Receptor> getReceptores() {
        Set<Receptor> receptoresUnicos = new HashSet<>();
        for (Doacao doacao : doacoes) {
            receptoresUnicos.add(doacao.getReceptor());
        }
        return new ArrayList<>(receptoresUnicos);
    }

    // retorna todas as doações de um receptor
    public ArrayList<Doacao> getDoacao(Receptor receptor) {
        ArrayList<Doacao> doacoesReceptor = new ArrayList<Doacao>();
        for (Doacao doacao : doacoes) {
            if (doacao.getReceptor().equals(receptor)) {
                doacoesReceptor.add(doacao);
            }
        }
        return doacoesReceptor;
    }

    // retorna itens pedidos por um receptor (sem duplicatas)
    public ArrayList<Item> getItensPorReceptor(Receptor receptor) {
        Set<Item> itensUnicos = new HashSet<>();
        for (Doacao doacao : doacoes) {
            if (doacao.getReceptor().equals(receptor)) {
                itensUnicos.add(doacao.getItem());
            }
        }
        return new ArrayList<>(itensUnicos);
    }

    public void removeDoacao(Receptor receptor, Item item) {
        doacoes.removeIf(doacao -> 
            doacao.getReceptor().equals(receptor) && 
            doacao.getItem().equals(item)
        );
    }

    // MELHORADO: conclui pedido e retorna se foi bem-sucedido
    public boolean concludeRequest(Receptor receptor, Item item) {
        int tamanhoAntes = doacoes.size();
        removeDoacao(receptor, item);
        return doacoes.size() < tamanhoAntes; // retorna true se removeu algo
    }

    // método auxiliar: verifica se receptor tem pedidos pendentes
    public boolean receptorTemPedidos(Receptor receptor) {
        for (Doacao doacao : doacoes) {
            if (doacao.getReceptor().equals(receptor)) {
                return true;
            }
        }
        return false;
    }
}