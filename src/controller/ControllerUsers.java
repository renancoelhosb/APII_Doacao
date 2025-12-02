package controller;

import java.io.Serializable;
import java.util.ArrayList;
import model.Usuario;

public class ControllerUsers implements Serializable {
    private ArrayList<Usuario> usuarios;

    public ControllerUsers() {
        this.usuarios = new ArrayList<Usuario>();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean createUsuario(String user, String senha, boolean isMaster) {
        if (getUsuario(user) != null) {
            System.out.println("Erro: Usuário " + user + "' já existe.");
            return false;
        }

        Usuario novoUsuario = new Usuario(user, senha, isMaster);
        return this.usuarios.add(novoUsuario);
    }
    
    public Usuario getUsuario(String user) {
        for (Usuario u : this.usuarios) {
            if (u.getUsuario().equalsIgnoreCase(user)) {
                return u;
            }
        }
        return null;
    }

    public boolean removeUsuario(String user) {
        Usuario u = getUsuario(user);
        if (u != null && !(u.isMaster())) {
            return this.usuarios.remove(u);
        }
        return false;
    }
}