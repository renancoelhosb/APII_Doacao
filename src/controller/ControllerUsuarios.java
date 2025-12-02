package controller;

import java.util.ArrayList;
import model.Usuario;

public class ControllerUsuarios {
    private ArrayList<Usuario> usuarios;

    public ControllerUsuarios() {
        this.usuarios = new ArrayList<Usuario>();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean createUsuario(String nome, String senha, boolean isMaster) {
        if (getUsuario(nome) != null) {
            System.out.println("Erro: Usuário com o nome '" + nome + "' já existe.");
            return false;
        }

        Usuario novoUsuario = new Usuario(nome, senha, isMaster);
        return this.usuarios.add(novoUsuario);
    }
    
    public Usuario getUsuario(String nome) {
        for (Usuario u : this.usuarios) {
            if (u.getUsuario().equalsIgnoreCase(nome)) {
                return u;
            }
        }
        return null;
    }

    public boolean removeUsuario(String nome) {
        Usuario u = getUsuario(nome);
        if (u != null && !(u.isMaster())) {
            return this.usuarios.remove(u);
        }
        return false;
    }
}