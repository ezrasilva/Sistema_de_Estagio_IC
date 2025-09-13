package src.model;
import java.io.Serializable;

public class Empresa extends Usuario implements Publicavel, Serializable {
    private String nomeFantasia;

    public Empresa(String nome, String email, String senha, String nomeFantasia) {
        super(nome, email, senha);
        this.nomeFantasia = nomeFantasia;
    }

    // Getters e Setters
    public String getNomeFantasia() {
        return this.nomeFantasia;
    }
    
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    
    @Override
    public void exibirPerfil() {
        System.out.println("--- Perfil da Empresa ---");
        System.out.println("ID: " + this.getId());
        System.out.println("Nome: " + this.getNome());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Nome Fantasia: " + this.nomeFantasia);
    }
    
    @Override
    public String getTipo() {
        return "Empresa";
    }

    @Override
    public void desativar() {
        System.out.println("A empresa " + this.getNomeFantasia() + " foi desativada.");
    }
}
