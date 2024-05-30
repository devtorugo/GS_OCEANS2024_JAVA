package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Especimes;
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

public class EspecimesRepository {

    private static final String TB_NAME = "ESPECIMES";
    private static final Log4Logger logger = new Log4Logger(EspecimesRepository.class);

    public List<Especimes> getAll() {
        List<Especimes> especimes = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                especimes.add(mapResultSetToEspecimes(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todos os espécimes do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todos os espécimes do banco de dados", e);
        }
        return especimes;
    }

    public Optional<Especimes> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEspecimes(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter espécime por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter espécime por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Especimes especime) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            especime.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, NOME_DA_ESPECIE, LOCALIZACAO_GEOGRAFICA, DESCRICAO_DO_ANIMAL, AMEACAS, ID_LOGIN) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setInt(1, especime.getId());
                stmt.setString(2, especime.getNomeEspecie());
                stmt.setString(3, especime.getLocalizacaoGeografica());
                stmt.setString(4, especime.getDescricao());
                stmt.setString(5, especime.getAmeacas());
                stmt.setInt(6, especime.getLogin().getId());

                stmt.executeUpdate();
            }

            logger.info("Espécime adicionado ao banco de dados: " + especime.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar o espécime no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o espécime no banco de dados", e);
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

    public void update(Especimes especime) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET NOME_DA_ESPECIE = ?, LOCALIZACAO_GEOGRAFICA = ?, DESCRICAO_DO_ANIMAL = ?, AMEACAS = ?, ID_LOGIN = ? WHERE ID = ?")) {
            stmt.setString(1, especime.getNomeEspecie());
            stmt.setString(2, especime.getLocalizacaoGeografica());
            stmt.setString(3, especime.getDescricao());
            stmt.setString(4, especime.getAmeacas());
            stmt.setInt(5, especime.getLogin().getId());
            stmt.setInt(6, especime.getId());

            stmt.executeUpdate();

            logger.info("Espécime atualizado no banco de dados: " + especime.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar o espécime no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o espécime no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Espécime removido do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir o espécime do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir o espécime do banco de dados", e);
        }
    }

    private Especimes mapResultSetToEspecimes(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nomeEspecie = rs.getString("NOME_DA_ESPECIE");
        String localizacaoGeografica = rs.getString("LOCALIZACAO_GEOGRAFICA");
        String descricao = rs.getString("DESCRICAO_DO_ANIMAL");
        String ameacas = rs.getString("AMEACAS");
        int idLogin = rs.getInt("ID_LOGIN");

        LoginRepository loginRepository = new LoginRepository();
        Login login = loginRepository.getById(idLogin).orElse(null);

        return new Especimes(id, nomeEspecie, localizacaoGeografica, descricao, ameacas, login);
    }
}
