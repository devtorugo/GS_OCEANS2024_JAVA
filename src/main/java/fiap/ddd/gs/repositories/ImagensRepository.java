package fiap.ddd.gs.repositories;

import fiap.ddd.gs.entities.Especimes;
import fiap.ddd.gs.entities.Imagens;
import fiap.ddd.gs.infrastructure.OracleDbConfiguration;
import fiap.ddd.gs.utils.Log4Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImagensRepository {

    private static final String TB_NAME = "IMAGENS";
    private static final Log4Logger logger = new Log4Logger(ImagensRepository.class);

    public List<Imagens> getAll() {
        List<Imagens> imagensList = new ArrayList<>();
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                imagensList.add(mapResultSetToImagens(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter todas as imagens do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter todas as imagens do banco de dados", e);
        }
        return imagensList;
    }

    public Optional<Imagens> getById(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToImagens(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao obter imagem por ID do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao obter imagem por ID do banco de dados", e);
        }
        return Optional.empty();
    }

    public void create(Imagens imagem) {
        try (Connection conn = OracleDbConfiguration.getConnection()) {
            int nextId = getNextId(conn);
            imagem.setId(nextId);

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (ID, NOME_ARQUIVO, TAMANHO_ARQUIVO, ID_ESPECIMES) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, imagem.getId());
                stmt.setString(2, imagem.getNomeArquivo());
                stmt.setDouble(3, imagem.getTamanhoAequivo());
                stmt.setInt(4, imagem.getEspecimes().getId());

                stmt.executeUpdate();
            }

            logger.info("Imagem adicionada ao banco de dados: " + imagem.toString());
        } catch (SQLException e) {
            logger.error("Erro ao criar a imagem no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao criar a imagem no banco de dados", e);
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

    public void update(Imagens imagem) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE " + TB_NAME + " SET NOME_ARQUIVO = ?, TAMANHO_ARQUIVO = ?, ID_ESPECIMES = ? WHERE ID = ?")) {
            stmt.setString(1, imagem.getNomeArquivo());
            stmt.setDouble(2, imagem.getTamanhoAequivo());
            stmt.setInt(3, imagem.getEspecimes().getId());
            stmt.setInt(4, imagem.getId());

            stmt.executeUpdate();

            logger.info("Imagem atualizada no banco de dados: " + imagem.toString());
        } catch (SQLException e) {
            logger.error("Erro ao atualizar a imagem no banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar a imagem no banco de dados", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = OracleDbConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE ID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Imagem removida do banco de dados. ID: " + id);
        } catch (SQLException e) {
            logger.error("Erro ao excluir a imagem do banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir a imagem do banco de dados", e);
        }
    }

    private Imagens mapResultSetToImagens(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nomeArquivo = rs.getString("NOME_ARQUIVO");
        double tamanhoArquivo = rs.getDouble("TAMANHO_ARQUIVO");
        int idEspecime = rs.getInt("ID_ESPECIMES");

        EspecimesRepository especimesRepository = new EspecimesRepository();
        Especimes especime = especimesRepository.getById(idEspecime).orElse(null);

        return new Imagens(id, nomeArquivo, tamanhoArquivo, especime);
    }
}
