package src.model;
import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private int id;
    private String nome;
    private String email;
    private String senha;

    // Construtor
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }
    
    // Métodos abstratos (requisito 3d)
    public abstract void exibirPerfil();
    public abstract String getTipo();
}
