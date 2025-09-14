package src.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.model.Publicavel;
import src.model.TipoVaga;
import src.model.Vaga;

import java.sql.Timestamp; 

public class VagaDAO {
    private Connection connection;

    public VagaDAO(Connection connection) {
        this.connection = connection;
        criarTabelaVagas();
    }
    
    private void criarTabelaVagas() {
        String sql = "CREATE TABLE IF NOT EXISTS vagas (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "titulo VARCHAR(255) NOT NULL," +
                     "descricao TEXT NOT NULL," +
                     "tipo VARCHAR(50) NOT NULL," +
                     "id_publicador INTEGER NOT NULL," +
                     "ativa BOOLEAN NOT NULL," +
                     "data_criacao DATETIME NOT NULL," +
                     "FOREIGN KEY (id_publicador) REFERENCES usuarios(id));";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela de vagas: " + e.getMessage());
        }
    }

    public void inserir(Vaga vaga) throws SQLException {
        String sql = "INSERT INTO vagas (titulo, descricao, tipo, id_publicador, ativa, data_criacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vaga.getTitulo());
            stmt.setString(2, vaga.getDescricao());
            stmt.setString(3, vaga.getTipo().toString());
            if (vaga.getPublicador() != null) {
                stmt.setInt(4, vaga.getPublicador().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setBoolean(5, vaga.isAtiva());
            stmt.setTimestamp(6, new Timestamp(vaga.getDataCriacao().getTime())); 
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vaga.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Vaga> listarTodas(UsuarioDAO usuarioDAO) throws SQLException {
        List<Vaga> vagas = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, tipo, id_publicador, ativa, data_criacao FROM vagas";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Vaga vaga = new Vaga();
                vaga.setId(rs.getInt("id"));
                vaga.setTitulo(rs.getString("titulo"));
                vaga.setDescricao(rs.getString("descricao"));
                vaga.setTipo(TipoVaga.valueOf(rs.getString("tipo")));
                vaga.setAtiva(rs.getBoolean("ativa"));
                vaga.setDataCriacao(rs.getTimestamp("data_criacao")); 
                int publicadorId = rs.getInt("id_publicador");
                if (!rs.wasNull()) {
                    vaga.setPublicador((Publicavel) usuarioDAO.buscarPorId(publicadorId));
                }
                vagas.add(vaga);
            }
        }
        return vagas;
    }

    public List<Vaga> listarPorPublicadorId(int publicadorId, UsuarioDAO usuarioDAO) throws SQLException {
        List<Vaga> vagas = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, tipo, id_publicador, ativa, data_criacao FROM vagas WHERE id_publicador = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, publicadorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vaga vaga = new Vaga();
                    vaga.setId(rs.getInt("id"));
                    vaga.setTitulo(rs.getString("titulo"));
                    vaga.setDescricao(rs.getString("descricao"));
                    vaga.setTipo(TipoVaga.valueOf(rs.getString("tipo")));
                    vaga.setAtiva(rs.getBoolean("ativa"));
                    vaga.setDataCriacao(rs.getTimestamp("data_criacao")); 
                    vaga.setPublicador((Publicavel) usuarioDAO.buscarPorId(publicadorId));
                    vagas.add(vaga);
                }
            }
        }
        return vagas;
    }
    
    public Vaga buscarPorId(int id, UsuarioDAO usuarioDAO) throws SQLException {
        String sql = "SELECT id, titulo, descricao, tipo, id_publicador, ativa, data_criacao FROM vagas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vaga vaga = new Vaga();
                    vaga.setId(rs.getInt("id"));
                    vaga.setTitulo(rs.getString("titulo"));
                    vaga.setDescricao(rs.getString("descricao"));
                    vaga.setTipo(TipoVaga.valueOf(rs.getString("tipo")));
                    vaga.setAtiva(rs.getBoolean("ativa"));
                    vaga.setDataCriacao(rs.getTimestamp("data_criacao"));

                    int publicadorId = rs.getInt("id_publicador");
                    if (!rs.wasNull()) {
                        vaga.setPublicador((Publicavel) usuarioDAO.buscarPorId(publicadorId));
                    }
                    return vaga;
                }
            }
        }
        return null;
    }
    
    public void atualizar(Vaga vaga) throws SQLException {
        String sql = "UPDATE vagas SET titulo = ?, descricao = ?, tipo = ?, id_publicador = ?, ativa = ?, data_criacao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vaga.getTitulo());
            stmt.setString(2, vaga.getDescricao());
            stmt.setString(3, vaga.getTipo().toString());
            stmt.setInt(4, vaga.getPublicador().getId());
            stmt.setBoolean(5, vaga.isAtiva());
            stmt.setTimestamp(6, new Timestamp(vaga.getDataCriacao().getTime())); 
            stmt.setInt(7, vaga.getId());
            stmt.executeUpdate();
        }
    }
    
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM vagas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
