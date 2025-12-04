package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Item implements Serializable{
    private int codigo;
    private String nome;
    private ArrayList<Vencimento> vencimentos = new ArrayList<>();
    private int qtd;

    public Item(int codigo, String nome, LocalDate vencimento, int qtd) {
        this.codigo = codigo;
        this.nome = nome;
        this.vencimentos.add(new Vencimento(qtd, vencimento));
        this.qtd = qtd;
    }
    
    public Item(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.vencimentos.add(new Vencimento(0, null));
        this.qtd = 0;
    }

    public int getCodigoCodigo(){
        return codigo; 
    }

    public int getCodigo(){
        return codigo; 
    }
    
    public String getNome(){ 
        return nome; 
    }
    
    public ArrayList<Vencimento> getVencimentos(){
        return vencimentos;
    }
    
    public int getQtd(){ 
        return qtd; 
    }
    
    
    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public boolean removerDoEstoque(int qtdRemover) {
        if (qtdRemover <= this.qtd) {
            this.qtd -= qtdRemover;
            
            
            int restante = qtdRemover;
            for (Vencimento v : vencimentos) {
                if (restante <= 0) break;
                int disponivel = v.getQtd();
                if (disponivel >= restante) {
                    v.setQtd(disponivel - restante);
                    restante = 0;
                } else {
                    restante -= disponivel;
                    v.setQtd(0);
                }
            }
            
            
            vencimentos.removeIf(v -> v.getQtd() == 0);
                return true;
            }
            return false;
        }
    
        public void removerQuantidade(int qtdRemover) {
        if (qtdRemover <= 0) return;

        vencimentos.removeIf(v -> v.getVencimento() == null);
        vencimentos.sort((v1, v2) -> {
            if (v1.getVencimento() == null && v2.getVencimento() == null) return 0;
            if (v1.getVencimento() == null) return 1;
            if (v2.getVencimento() == null) return -1;
            return v1.getVencimento().compareTo(v2.getVencimento());
        });
        
        int qtdRestante = qtdRemover;
        
        for (int i = 0; i < vencimentos.size() && qtdRestante > 0; i++) {
            Vencimento v = vencimentos.get(i);
            int qtdVencimento = v.getQtd();
            
            if (qtdVencimento <= qtdRestante) {
                qtdRestante -= qtdVencimento;
                vencimentos.remove(i);
                i--; 
            } else {
                v.setQtd(qtdVencimento - qtdRestante);
                qtdRestante = 0;
            }
        }
        
        
        this.qtd -= qtdRemover;
    }
    
    
    public void limparVencimentosZerados() {
        vencimentos.removeIf(v -> v.getQtd() <= 0 || v.getVencimento() == null);
    }
}