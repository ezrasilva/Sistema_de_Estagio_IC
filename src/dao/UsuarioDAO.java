package src.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.model.Aluno;
import src.model.Empresa;
import src.model.Professor;
import src.model.Usuario;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo, campo_extra) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getTipo());
            
            String campoExtra = null;
            if (usuario instanceof Aluno) {
                campoExtra = ((Aluno) usuario).getCurso();
            } else if (usuario instanceof Empresa) {
                campoExtra = ((Empresa) usuario).getNomeFantasia();
            } else if (usuario instanceof Professor) {
                campoExtra = ((Professor) usuario).getDepartamento();
            }
            statement.setString(5, campoExtra);

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return criarUsuario(resultSet);
                }
            }
        }
        return null;
    }
    
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return criarUsuario(resultSet);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                usuarios.add(criarUsuario(resultSet));
            }
        }
        return usuarios;
    }
    
    private Usuario criarUsuario(ResultSet resultSet) throws SQLException {
        String tipo = resultSet.getString("tipo");
        int id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");
        String email = resultSet.getString("email");
        String senha = resultSet.getString("senha");
        String campoExtra = resultSet.getString("campo_extra");
        
        Usuario usuario = null;
        switch (tipo) {
            case "Aluno":
                usuario = new Aluno(nome, email, senha, campoExtra);
                break;
            case "Empresa":
                usuario = new Empresa(nome, email, senha, campoExtra);
                break;
            case "Professor":
                usuario = new Professor(nome, email, senha, campoExtra);
                break;
        }
        if (usuario != null) {
            usuario.setId(id);
        }
        return usuario;
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, campo_extra = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            
            String campoExtra = null;
            if (usuario instanceof Aluno) {
                campoExtra = ((Aluno) usuario).getCurso();
            } else if (usuario instanceof Empresa) {
                campoExtra = ((Empresa) usuario).getNomeFantasia();
            } else if (usuario instanceof Professor) {
                campoExtra = ((Professor) usuario).getDepartamento();
            }
            statement.setString(4, campoExtra);
            statement.setInt(5, usuario.getId());
            statement.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
