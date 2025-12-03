package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Vencimento implements Serializable{
    private int qtd; 
    private LocalDate vencimento;

    public Vencimento(int qtd, LocalDate vencimento) {
        this.qtd = qtd;
        this.vencimento = vencimento;
    }

    public int getQtd() {
        return qtd;
    }
    
    // Adicionando setter que estava faltando
    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
    
    public LocalDate getVencimento() {
        return vencimento;
    }
}