package src.util;
import java.util.List;
import java.util.stream.Collectors;

import src.model.TipoVaga;
import src.model.Vaga;

public class FiltroDeBusca {

    public static List<Vaga> filtrarPorTipo(List<Vaga> vagas, TipoVaga tipo) {
        return vagas.stream()
                .filter(vaga -> vaga.getTipo().equals(tipo))
                .collect(Collectors.toList());
    }

    public static List<Vaga> filtrarPorTitulo(List<Vaga> vagas, String palavraChave) {
        return vagas.stream()
                .filter(vaga -> vaga.getTitulo().toLowerCase().contains(palavraChave.toLowerCase()))
                .collect(Collectors.toList());
    }
}
