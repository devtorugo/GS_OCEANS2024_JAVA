package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Doacoes;
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

public class DoacoesRepository {

    private static final String TB_NAME = "DOACOES";
    private static final Log4Logger logger = new Log4Logger(DoacoesRepository.class);

    public List<Doacoes> getAll() {
        List<Doacoes> doacoes = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doacoes.add(mapResultSetToDoacoes(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todas as doações do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todas as doações do banco de dados", e);
        }
        return doacoes;
    }

    public Optional<Doacoes> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDoacoes(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter doação por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter doação por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Doacoes doacao) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            doacao.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, NOME_DO_DOADOR, CPF, CEP, VALOR_DA_DOACAO, DESCRICAO_DOACAO, ID_LOGIN) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setInt(1, doacao.getId());
                stmt.setString(2, doacao.getNomeDoador());
                stmt.setString(3, doacao.getCpf());
                stmt.setString(4, doacao.getCep());
                stmt.setDouble(5, doacao.getValorDoacao());
                stmt.setString(6, doacao.getDescricao());

                if (doacao.getLogin() != null) {
                    stmt.setInt(7, doacao.getLogin().getId());
                } else {
                    stmt.setNull(7, java.sql.Types.INTEGER);
                }

                stmt.executeUpdate();
            }

            logger.info("Doação adicionada ao banco de dados: " + doacao.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar a doação no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar a doação no banco de dados", e);
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

    public void update(Doacoes doacao) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET NOME_DO_DOADOR = ?, CPF = ?, CEP = ?, VALOR_DA_DOACAO = ?, DESCRICAO_DOACAO = ?, ID_LOGIN = ? WHERE ID = ?")) {
            stmt.setString(1, doacao.getNomeDoador());
            stmt.setString(2, doacao.getCpf());
            stmt.setString(3, doacao.getCep());
            stmt.setDouble(4, doacao.getValorDoacao());
            stmt.setString(5, doacao.getDescricao());

            if (doacao.getLogin() != null) {
                stmt.setInt(6, doacao.getLogin().getId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            stmt.setInt(7, doacao.getId());

            stmt.executeUpdate();

            logger.info("Doação atualizada no banco de dados: " + doacao.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar a doação no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar a doação no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Doação removida do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir a doação do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir a doação do banco de dados", e);
        }
    }

    private Doacoes mapResultSetToDoacoes(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nomeDoador = rs.getString("NOME_DO_DOADOR");
        String cpf = rs.getString("CPF");
        String cep = rs.getString("CEP");
        double valorDoacao = rs.getDouble("VALOR_DA_DOACAO");
        String descricao = rs.getString("DESCRICAO_DOACAO");
        int idLogin = rs.getInt("ID_LOGIN");

        Login login = null;
        if (!rs.wasNull()) {
            LoginRepository loginRepository = new LoginRepository();
            login = loginRepository.getById(idLogin).orElse(null);
        }

        return new Doacoes(id, nomeDoador, cpf, cep, valorDoacao, descricao, login);
    }
}
