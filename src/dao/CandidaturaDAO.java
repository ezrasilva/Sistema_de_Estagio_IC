package src.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import src.model.Aluno;
import src.model.Candidatura;
import src.model.Vaga;

public class CandidaturaDAO {
    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private VagaDAO vagaDAO;

    public CandidaturaDAO(Connection connection, UsuarioDAO usuarioDAO, VagaDAO vagaDAO) {
        this.connection = connection;
        this.usuarioDAO = usuarioDAO;
        this.vagaDAO = vagaDAO;
    }

    public void inserir(Candidatura candidatura) throws SQLException {
        String sql = "INSERT INTO candidaturas (id_vaga, id_aluno) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidatura.getVaga().getId());
            stmt.setInt(2, candidatura.getAluno().getId());
            stmt.executeUpdate();
        }
    }

    public List<Candidatura> buscarCandidaturasPorVagaId(int vagaId) throws SQLException {
        List<Candidatura> candidaturas = new ArrayList<>();
        String sql = "SELECT * FROM candidaturas WHERE id_vaga = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vagaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int alunoId = rs.getInt("id_aluno");
                    Aluno aluno = (Aluno) usuarioDAO.buscarPorId(alunoId);
                    Vaga vaga = vagaDAO.buscarPorId(vagaId, usuarioDAO);
                    candidaturas.add(new Candidatura(aluno, vaga));
                }
            }
        }
        return candidaturas;
    }
    
    public List<Candidatura> buscarCandidaturasPorAlunoId(int alunoId) throws SQLException {
        List<Candidatura> candidaturas = new ArrayList<>();
        String sql = "SELECT * FROM candidaturas WHERE id_aluno = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, alunoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int vagaId = rs.getInt("id_vaga");
                    Vaga vaga = vagaDAO.buscarPorId(vagaId, usuarioDAO);
                    Aluno aluno = (Aluno) usuarioDAO.buscarPorId(alunoId);
                    candidaturas.add(new Candidatura(aluno, vaga));
                }
            }
        }
        return candidaturas;
    }
}
