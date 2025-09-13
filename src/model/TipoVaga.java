package src.model;
public enum TipoVaga {
    ESTAGIO("Estágio"),
    IC("Iniciação Científica");

    private String descricao;

    TipoVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
