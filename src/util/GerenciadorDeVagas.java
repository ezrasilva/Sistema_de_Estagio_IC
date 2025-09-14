package src.util;
import java.util.ArrayList;
import java.util.List;

import src.model.Vaga;

class GerenciadorDeVagas {
    private static GerenciadorDeVagas instance;
    private List<Vaga> vagas;

    private GerenciadorDeVagas() {
        this.vagas = new ArrayList<>(); 
    }

    public static GerenciadorDeVagas getInstance() {
        if (instance == null) {
            instance = new GerenciadorDeVagas();
        }
        return instance;
    }

    public void adicionarVaga(Vaga vaga) {
        this.vagas.add(vaga);
        System.out.println("Vaga '" + vaga.getTitulo() + "' adicionada com sucesso.");
    }

    public List<Vaga> listarTodasVagas() {
        return this.vagas;
    }
}
