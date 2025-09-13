package src.model;
import java.io.Serializable;

public class Professor extends Usuario implements Publicavel, Serializable {
    private String departamento;

    public Professor(String nome, String email, String senha, String departamento) {
        super(nome, email, senha);
        this.departamento = departamento;
    }

    // Getters e Setters
    public String getDepartamento() {
        return this.departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    @Override
    public void exibirPerfil() {
        System.out.println("--- Perfil do Professor ---");
        System.out.println("ID: " + this.getId());
        System.out.println("Nome: " + this.getNome());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Departamento: " + this.departamento);
    }
    
    @Override
    public String getTipo() {
        return "Professor";
    }

    @Override
    public void desativar() {
        System.out.println("O professor " + this.getNome() + " foi desativado.");
    }
}
