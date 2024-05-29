package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Login;
import fiap.ddd.gs.infrastructure.OracleDbConfiguration;
import fiap.ddd.gs.utils.Log4Logger;
import fiap.ddd.gs.entities.Cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginRepository {

    private static final String TB_NAME = "LOGIN";
    private static final Log4Logger logger = new Log4Logger(LoginRepository.class);

    public List<Login> getAll() {
        List<Login> logins = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logins.add(mapResultSetToLogin(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todos os logins do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todos os logins do banco de dados", e);
        }
        return logins;
    }

    public Optional<Login> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLogin(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter login por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter login por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Login login) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            login.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, EMAIL, SENHA, ID_CADASTRO) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, login.getId());
                stmt.setString(2, login.getEmail());
                stmt.setString(3, login.getSenha());
                stmt.setInt(4, login.getCadastro().getId());

                stmt.executeUpdate();
            }

            logger.info("Login adicionado ao banco de dados: " + login.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar o login no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o login no banco de dados", e);
        }
    }

    private int getNextId(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT MAX(ID) FROM " + TB_NAME)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }

                return 1;
            }
        }
    }

    public void update(Login login) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET EMAIL = ?, SENHA = ?, ID_CADASTRO = ? WHERE ID = ?")) {
            stmt.setString(1, login.getEmail());
            stmt.setString(2, login.getSenha());
            stmt.setInt(3, login.getCadastro().getId());
            stmt.setInt(4, login.getId());

            stmt.executeUpdate();

            logger.info("Login atualizado no banco de dados: " + login.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar o login no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o login no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Login removido do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir o login do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir o login do banco de dados", e);
        }
    }

    private Login mapResultSetToLogin(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String email = rs.getString("EMAIL");
        String senha = rs.getString("SENHA");
        int idCadastro = rs.getInt("ID_CADASTRO");

        CadastroRepository cadastroRepository = new CadastroRepository();

        Cadastro cadastro = cadastroRepository.getById(idCadastro).orElse(null);

        return new Login(id, email, senha, cadastro);
    }
}
