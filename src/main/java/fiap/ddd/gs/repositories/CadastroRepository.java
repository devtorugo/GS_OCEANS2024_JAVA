package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Cadastro;
import fiap.ddd.gs.infrastructure.OracleDbConfiguration;
import fiap.ddd.gs.utils.Log4Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CadastroRepository {

    private static final String TB_NAME = "CADASTRO";
    private static final Log4Logger logger = new Log4Logger(CadastroRepository.class);

    public List<Cadastro> getAll() {
        List<Cadastro> cadastros = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cadastros.add(mapResultSetToCadastro(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todos os cadastros do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todos os cadastros do banco de dados", e);
        }
        return cadastros;
    }

    public Optional<Cadastro> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCadastro(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter cadastro por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter cadastro por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Cadastro cadastro) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            cadastro.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, NOME, SOBRENOME, EMAIL, CAUSA_SOCIAL, SENHA) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setInt(1, cadastro.getId());
                stmt.setString(2, cadastro.getNome());
                stmt.setString(3, cadastro.getSobrenome());
                stmt.setString(4, cadastro.getEmail());
                stmt.setString(5, cadastro.getCausaSocial());
                stmt.setString(6, cadastro.getSenha());

                stmt.executeUpdate();
            }

            logger.info("Cadastro adicionado ao banco de dados: " + cadastro.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar o cadastro no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o cadastro no banco de dados", e);
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


    public void update(Cadastro cadastro) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET NOME = ?, SOBRENOME = ?, EMAIL = ?, CAUSA_SOCIAL = ?, SENHA = ? WHERE ID = ?")) {
            stmt.setString(1, cadastro.getNome());
            stmt.setString(2, cadastro.getSobrenome());
            stmt.setString(3, cadastro.getEmail());
            stmt.setString(4, cadastro.getCausaSocial());
            stmt.setString(5, cadastro.getSenha());
            stmt.setInt(6, cadastro.getId());

            stmt.executeUpdate();

            logger.info("Cadastro atualizado no banco de dados: " + cadastro.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar o cadastro no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o cadastro no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Cadastro removido do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir o cadastro do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir o cadastro do banco de dados", e);
        }
    }

    private Cadastro mapResultSetToCadastro(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nome = rs.getString("NOME");
        String sobrenome = rs.getString("SOBRENOME");
        String email = rs.getString("EMAIL");
        String causaSocial = rs.getString("CAUSA_SOCIAL");
        String senha = rs.getString("SENHA");

        return new Cadastro(id, nome, sobrenome, email, causaSocial, senha);
    }
}
