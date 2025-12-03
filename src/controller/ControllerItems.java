package controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Item;
import model.Vencimento;

public class ControllerItems implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<Item> itens = new ArrayList<>();

    
    public ControllerItems() {
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

    public boolean removeItem(Item item){
        return itens.remove(item);
    }

    public boolean removeVencidos() {
        boolean removeu = false;
        LocalDate hoje = LocalDate.now();


        ArrayList<Item> copiaItens = new ArrayList<Item>(itens);
        
        for (Item item : copiaItens) {
            ArrayList<Vencimento> vencidos = new ArrayList<Vencimento>();
            
            for (Vencimento v : item.getVencimentos()) {
                if (v.getVencimento() != null && v.getVencimento().isBefore(hoje)) {

                    item.setQtd(item.getQtd() - v.getQtd());
                    vencidos.add(v);
                    removeu = true;
                }
            }

            item.getVencimentos().removeAll(vencidos);
 
        }

        return removeu;
    }

    public void adicionarItem(String nome) {
       
        int proxId = 1;
        for (Item i : itens) {
            if (i.getCodigo() >= proxId) {
                proxId = i.getCodigo() + 1;
            }
        }
        itens.add(new Item(proxId, nome));
    }
}