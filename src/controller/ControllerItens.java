package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Item;
import model.Vencimento;

public class ControllerItens {

    private ArrayList<Item> itens = new ArrayList<>();

    public ControllerItens() {
    }

    public ArrayList<Item> getItens() {
        return itens;
    }

    public Item getItem(int codigo){
        for (Item item : itens) {
            if (item.getCodigo() == codigo) {
                return item;
            }
        }
        return null;
    }

    public boolean removeItem(int codigo){
        Item item = getItem(codigo);
        if (item != null) {
            itens.remove(item);
            return true;
        }
        return false;
    }

    public boolean removeVencidos() {
        boolean removeu = false;
        LocalDate hoje = LocalDate.now();

        // criando cópia para evitar ConcurrentModificationException
        ArrayList<Item> copiaItens = new ArrayList<>(itens);
        
        for (Item item : copiaItens) {
            ArrayList<Vencimento> vencidos = new ArrayList<>();
            
            for (Vencimento v : item.getVencimentos()) {
                if (v.getVencimento().isBefore(hoje)) {
                    // Diminuir a quantidade total
                    item.setQtd(item.getQtd() - v.getQtd());
                    vencidos.add(v);
                    removeu = true;
                }
            }
            
            // Remover vencimentos expirados
            item.getVencimentos().removeAll(vencidos);
            
            // Se o item não tem mais vencimentos, o remove da lista principal
            if (item.getVencimentos().isEmpty() || item.getQtd() <= 0) {
                itens.remove(item);
            }
        }

        return removeu;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }
}