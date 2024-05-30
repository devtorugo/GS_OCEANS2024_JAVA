package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Contato;
import fiap.ddd.gs.entities.Login;
import fiap.ddd.gs.infrastructure.OracleDbConfiguration;
import fiap.ddd.gs.utils.Log4Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContatoRepository {

    private static final String TB_NAME = "CONTATO";
    private static final Log4Logger logger = new Log4Logger(ContatoRepository.class);

    public List<Contato> getAll() {
        List<Contato> contatos = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contatos.add(mapResultSetToContato(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todos os contatos do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todos os contatos do banco de dados", e);
        }
        return contatos;
    }

    public Optional<Contato> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContato(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter contato por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter contato por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Contato contato) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            contato.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, NOME, TELEFONE, MENSAGEM, ID_LOGIN) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setInt(1, contato.getId());
                stmt.setString(2, contato.getNome());
                stmt.setString(3, contato.getTelefone());
                stmt.setString(4, contato.getMensagem());


                if (contato.getLogin() != null) {
                    stmt.setInt(5, contato.getLogin().getId());
                } else {
                    stmt.setNull(5, java.sql.Types.INTEGER);
                }

                stmt.executeUpdate();
            }

            logger.info("Contato adicionado ao banco de dados: " + contato.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar o contato no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o contato no banco de dados", e);
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

    public void update(Contato contato) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET NOME = ?, TELEFONE = ?, MENSAGEM = ?, ID_LOGIN = ? WHERE ID = ?")) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getMensagem());


            if (contato.getLogin() != null) {
                stmt.setInt(4, contato.getLogin().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setInt(5, contato.getId());

            stmt.executeUpdate();

            logger.info("Contato atualizado no banco de dados: " + contato.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar o contato no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o contato no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Contato removido do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir o contato do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir o contato do banco de dados", e);
        }
    }

    private Contato mapResultSetToContato(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nome = rs.getString("NOME");
        String telefone = rs.getString("TELEFONE");
        String mensagem = rs.getString("MENSAGEM");
        int idLogin = rs.getInt("ID_LOGIN");

        Login login = null;
        if (!rs.wasNull()) {

            LoginRepository loginRepository = new LoginRepository();
            login = loginRepository.getById(idLogin).orElse(null);
        }

        return new Contato(id, nome, telefone, mensagem, login);
    }
}
