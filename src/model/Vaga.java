package src.model;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vaga implements Serializable {

    private int id;
    private String titulo;
    private String descricao;
    private TipoVaga tipo;
    private Publicavel publicador;
    private boolean ativa;
    private Date dataCriacao;

    // Variável constante (requisito 3c)
    public static final String NOME_SISTEMA = "Conecta UFPA";

    public Vaga() {
        // Construtor vazio para o VagaDAO
    }

    // Construtor (requisito 3i - composição)
    public Vaga(String titulo, String descricao, TipoVaga tipo, Publicavel publicador) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.publicador = publicador;
        this.ativa = true;
        this.dataCriacao = new Date();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoVaga getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoVaga tipo) {
        this.tipo = tipo;
    }

    public Publicavel getPublicador() {
        return publicador;
    }
    
    public void setPublicador(Publicavel publicador) {
        this.publicador = publicador;
    }

    public boolean isAtiva() {
        return ativa;
    }
    
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    // Adicionado o setter para a data de criação
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // Métodos
    public void exibirDetalhes() {
        System.out.println("--- Detalhes da Vaga ---");
        System.out.println("Título: " + this.titulo);
        System.out.println("Descrição: " + this.descricao);
        System.out.println("Tipo: " + this.tipo.getDescricao());
        System.out.println("Publicado por: " + this.publicador.getNome());
        System.out.println("Ativa: " + (this.ativa ? "Sim" : "Não"));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        System.out.println("Data de Publicação: " + sdf.format(this.dataCriacao));
    }
}