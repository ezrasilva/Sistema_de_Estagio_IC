package src.model;
import java.io.Serializable;
import java.util.Date;

public class Candidatura implements Serializable {
    private int id;
    private Aluno aluno;
    private Vaga vaga;
    private Date dataCandidatura;

    public Candidatura(Aluno aluno, Vaga vaga) {
        this.aluno = aluno;
        this.vaga = vaga;
        this.dataCandidatura = new Date();
    }

    public Candidatura(int id, Aluno aluno, Vaga vaga, Date dataCandidatura) {
        this.id = id;
        this.aluno = aluno;
        this.vaga = vaga;
        this.dataCandidatura = dataCandidatura;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public Date getDataCandidatura() {
        return dataCandidatura;
    }
}
