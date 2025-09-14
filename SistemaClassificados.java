import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import src.dao.CandidaturaDAO;
import src.dao.UsuarioDAO;
import src.dao.VagaDAO;
import src.db.ConexaoSQLite;
import src.model.Aluno;
import src.model.Candidatura;
import src.model.Empresa;
import src.model.Professor;
import src.model.Publicavel;
import src.model.TipoVaga;
import src.model.Usuario;
import src.model.Vaga;
import src.util.FiltroDeBusca;

public class SistemaClassificados {

    public static final String[] TIPOS_DE_USUARIO_ACEITOS = {"Aluno", "Empresa", "Professor"}; 

    private static UsuarioDAO usuarioDAO;
    private static VagaDAO vagaDAO;
    private static CandidaturaDAO candidaturaDAO;
    private static Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        
        try {
            ConexaoSQLite conexao = new ConexaoSQLite();
            connection = conexao.getConnection();

            if (connection != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
                usuarioDAO = new UsuarioDAO(connection);
                vagaDAO = new VagaDAO(connection);
                candidaturaDAO = new CandidaturaDAO(connection, usuarioDAO, vagaDAO);
            } else {
                System.out.println("Falha ao estabelecer conexão com o banco de dados.");
                return;
            }
            
            System.out.println("Bem-vindo ao " + Vaga.NOME_SISTEMA);
            executarSistema();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                }
            }
        }
    }

    private static void executarSistema() {
        while (true) {
            if (usuarioLogado == null) {
                mostrarMenuInicial();
            } else {
                mostrarMenuPrincipal();
            }
        }
    }

    private static void mostrarMenuInicial() {
        System.out.println("\n--- Menu Inicial ---");
        System.out.println("1. Login");
        System.out.println("2. Cadastro");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                fazerLogin();
                break;
            case "2":
                fazerCadastro();
                break;
            case "3":
                System.out.println("Saindo do sistema...");
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                    }
                }
                System.exit(0);
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menu Principal ---");
        if (usuarioLogado instanceof Aluno) {
            mostrarMenuAluno();
        } else if (usuarioLogado instanceof Empresa || usuarioLogado instanceof Professor) {
            mostrarMenuPublicador();
        }
        System.out.println("Sair para deslogar");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();
        if (opcao.equalsIgnoreCase("sair")) {
            usuarioLogado = null;
            System.out.println("Deslogado com sucesso.");
        }
    }

    private static void mostrarMenuAluno() {
        System.out.println("1. Buscar Vagas");
        System.out.println("2. Exibir meu Perfil");
        System.out.println("3. Listar minhas Candidaturas");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                buscarVagas();
                break;
            case "2":
                usuarioLogado.exibirPerfil();
                break;
            case "3":
                listarCandidaturas();
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void mostrarMenuPublicador() {
        System.out.println("1. Publicar Vaga");
        System.out.println("2. Listar Vagas Publicadas");
        System.out.println("3. Exibir meu Perfil");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                publicarVaga();
                break;
            case "2":
                listarVagasPublicadas();
                break;
            case "3":
                usuarioLogado.exibirPerfil();
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void fazerLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            if (usuario != null && usuario.getSenha().equals(senha)) {
                usuarioLogado = usuario;
                System.out.println("Login bem-sucedido!");
            } else {
                System.out.println("Email ou senha inválidos.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    private static void fazerCadastro() {
        System.out.println("--- Cadastro ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        System.out.println("Selecione o tipo de usuário: (Aluno, Empresa, Professor)");
        System.out.print("Tipo: ");
        String tipoUsuario = scanner.nextLine();

        Usuario novoUsuario = null;
        switch (tipoUsuario.toLowerCase()) {
            case "aluno":
                System.out.print("Curso: ");
                String curso = scanner.nextLine();
                novoUsuario = new Aluno(nome, email, senha, curso);
                break;
            case "empresa":
                System.out.print("Nome Fantasia: ");
                String nomeFantasia = scanner.nextLine();
                novoUsuario = new Empresa(nome, email, senha, nomeFantasia);
                break;
            case "professor":
                System.out.print("Departamento: ");
                String departamento = scanner.nextLine();
                novoUsuario = new Professor(nome, email, senha, departamento);
                break;
            default:
                System.out.println("Tipo de usuário inválido.");
                return;
        }

        try {
            usuarioDAO.inserir(novoUsuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static void buscarVagas() {
        System.out.println("\n--- Busca de Vagas ---");
        System.out.println("1. Listar todas as vagas");
        System.out.println("2. Filtrar por tipo (Estágio/IC)");
        System.out.println("3. Filtrar por palavra-chave no título");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        List<Vaga> vagasFiltradas = new ArrayList<>();
        try {
            List<Vaga> todasAsVagas = vagaDAO.listarTodas(usuarioDAO); 
            switch (opcao) {
                case "1":
                    vagasFiltradas = todasAsVagas;
                    break;
                case "2":
                    System.out.print("Digite o tipo (ESTAGIO/IC): ");
                    String tipo = scanner.nextLine().toUpperCase();
                    try {
                        vagasFiltradas = FiltroDeBusca.filtrarPorTipo(todasAsVagas, TipoVaga.valueOf(tipo));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Tipo de vaga inválido.");
                        return;
                    }
                    break;
                case "3":
                    System.out.print("Digite a palavra-chave: ");
                    String palavraChave = scanner.nextLine();
                    vagasFiltradas = FiltroDeBusca.filtrarPorTitulo(todasAsVagas, palavraChave);
                    break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vagas: " + e.getMessage());
            return;
        }

        if (vagasFiltradas.isEmpty()) {
            System.out.println("Nenhuma vaga encontrada.");
        } else {
            System.out.println("--- Vagas Encontradas ---");
            for (int i = 0; i < vagasFiltradas.size(); i++) {
                System.out.println((i + 1) + ". " + vagasFiltradas.get(i).getTitulo() + " (" + vagasFiltradas.get(i).getTipo() + ")");
            }

            System.out.print("Digite o número da vaga para ver detalhes e candidatar-se, ou 0 para voltar: ");
            String escolhaVaga = scanner.nextLine();
            try {
                int indice = Integer.parseInt(escolhaVaga) - 1;
                if (indice >= 0 && indice < vagasFiltradas.size()) {
                    Vaga vagaSelecionada = vagasFiltradas.get(indice);
                    vagaSelecionada.exibirDetalhes();
                    candidatarAVaga(vagaSelecionada);
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private static void candidatarAVaga(Vaga vaga) {
        if (usuarioLogado instanceof Aluno) {
            System.out.println("\nDeseja se candidatar a esta vaga? (sim/nao)");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("sim")) {
                try {
                    Candidatura novaCandidatura = new Candidatura((Aluno) usuarioLogado, vaga);
                    candidaturaDAO.inserir(novaCandidatura);
                    System.out.println("Candidatura realizada com sucesso!");
                } catch (SQLException e) {
                    System.err.println("Erro ao candidatar-se à vaga: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Apenas alunos podem se candidatar a vagas.");
        }
    }

    private static void listarCandidaturas() {
        System.out.println("\n--- Minhas Candidaturas ---");
        if (usuarioLogado instanceof Aluno) {
            try {
                List<Candidatura> candidaturas = candidaturaDAO.buscarCandidaturasPorAlunoId(usuarioLogado.getId());
                if (candidaturas.isEmpty()) {
                    System.out.println("Você ainda não se candidatou a nenhuma vaga.");
                } else {
                    for (Candidatura c : candidaturas) {
                        System.out.println("- Vaga: " + c.getVaga().getTitulo() + " (Publicado por: " + c.getVaga().getPublicador().getNome() + ")");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar candidaturas: " + e.getMessage());
            }
        } else {
            System.out.println("Essa opção é apenas para alunos.");
        }
    }

    private static void publicarVaga() {
        System.out.println("\n--- Publicar Vaga ---");
        System.out.print("Título da Vaga: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição da Vaga: ");
        String descricao = scanner.nextLine();
        System.out.print("Tipo da Vaga (ESTAGIO/IC): ");
        String tipoString = scanner.nextLine().toUpperCase();

        try {
            TipoVaga tipo = TipoVaga.valueOf(tipoString);
            if (usuarioLogado instanceof Publicavel) {
                Publicavel publicador = (Publicavel) usuarioLogado;
                Vaga novaVaga = new Vaga(titulo, descricao, tipo, publicador);
                vagaDAO.inserir(novaVaga);
                System.out.println("Vaga publicada com sucesso!");
            } else {
                System.out.println("Apenas empresas e professores podem publicar vagas.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de vaga inválido.");
        } catch (SQLException e) {
            System.err.println("Erro ao publicar vaga: " + e.getMessage());
        }
    }

    private static void listarVagasPublicadas() {
        System.out.println("\n--- Vagas Publicadas ---");
        if (usuarioLogado instanceof Publicavel) {
            Publicavel publicador = (Publicavel) usuarioLogado;
            try {
                // Alterado para usar o novo método listarPorPublicadorId para maior eficiência
                List<Vaga> vagasPublicadas = vagaDAO.listarPorPublicadorId(publicador.getId(), usuarioDAO);
                if (vagasPublicadas.isEmpty()) {
                    System.out.println("Você ainda não publicou nenhuma vaga.");
                } else {
                    for (Vaga vaga : vagasPublicadas) {
                        System.out.println("Título: " + vaga.getTitulo() + " | Tipo: " + vaga.getTipo() + " | Ativa: " + vaga.isAtiva());
                        
                        List<Candidatura> candidatos = candidaturaDAO.buscarCandidaturasPorVagaId(vaga.getId());
                        System.out.println("Candidatos:");
                        if (candidatos.isEmpty()) {
                            System.out.println("Nenhum candidato ainda.");
                        } else {
                            for (Candidatura candidatura : candidatos) {
                                System.out.println("- " + candidatura.getAluno().getNome());
                            }
                        }
                        System.out.println("-------------------------");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar vagas: " + e.getMessage());
            }
        }
    }
}
