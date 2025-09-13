package src.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {
    private static final String URL = "jdbc:sqlite:classificados.db";
    private Connection connection;

    public ConexaoSQLite() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(URL);
            criarTabelas();
        } catch (SQLException e) {
            System.err.println("Erro na conexão ou criação das tabelas: " + e.getMessage());
            throw e;
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void criarTabelas() throws SQLException {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "senha VARCHAR(255) NOT NULL," +
                "tipo VARCHAR(50) NOT NULL," +
                "campo_extra VARCHAR(255)" +
                ");";

        // Adicionada a coluna 'data_criacao' na tabela vagas
        String sqlVagas = "CREATE TABLE IF NOT EXISTS vagas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo VARCHAR(255) NOT NULL," +
                "descricao TEXT," +
                "tipo VARCHAR(50) NOT NULL," +
                "id_publicador INTEGER NOT NULL," +
                "ativa BOOLEAN NOT NULL," +
                "data_criacao DATETIME NOT NULL," +
                "FOREIGN KEY (id_publicador) REFERENCES usuarios(id)" +
                ");";

        String sqlCandidaturas = "CREATE TABLE IF NOT EXISTS candidaturas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_aluno INTEGER NOT NULL," +
                "id_vaga INTEGER NOT NULL," +
                "FOREIGN KEY (id_aluno) REFERENCES usuarios(id)," +
                "FOREIGN KEY (id_vaga) REFERENCES vagas(id)" +
                ");";

        try (Statement statement = this.connection.createStatement()) {
            statement.execute(sqlUsuarios);
            statement.execute(sqlVagas);
            statement.execute(sqlCandidaturas);
        }
    }
}