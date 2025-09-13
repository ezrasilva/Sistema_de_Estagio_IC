package src.model;
import java.io.Serializable;

public class Aluno extends Usuario implements Serializable {
    private String curso;

    public Aluno(String nome, String email, String senha, String curso) {
        super(nome, email, senha);
        this.curso = curso;
    }

    // Getters e Setters
    public String getCurso() {
        return this.curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("--- Perfil do Aluno ---");
        System.out.println("ID: " + this.getId());
        System.out.println("Nome: " + this.getNome());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Curso: " + this.curso);
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }
}
