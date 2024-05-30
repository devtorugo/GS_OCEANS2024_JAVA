package fiap.ddd.gs.services;

import fiap.ddd.gs.entities.Doacoes;
import fiap.ddd.gs.repositories.DoacoesRepository;

import java.util.List;
import java.util.Optional;

public class DoacoesService {

    private final DoacoesRepository doacoesRepository;

    public DoacoesService(DoacoesRepository doacoesRepository) {
        this.doacoesRepository = doacoesRepository;
    }

    public List<Doacoes> getDoacoes() {
        try {
            return doacoesRepository.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter doações: " + e.getMessage());
        }
    }

    public Optional<Doacoes> getDoacaoById(int id) {
        try {
            return doacoesRepository.getById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter doação: " + e.getMessage());
        }
    }

    public void adicionarDoacao(Doacoes doacao) {
        if (doacao.getId() != 0) {
            throw new IllegalArgumentException("ID da doação deve ser gerado automaticamente.");
        }
        doacoesRepository.create(doacao);
    }

    public void atualizarDoacao(Doacoes doacao) {
        if (doacoesRepository.getById(doacao.getId()).isEmpty()) {
            throw new IllegalArgumentException("Doação não encontrada");
        }
        doacoesRepository.update(doacao);
    }

    public void deletarDoacao(int id) {
        if (doacoesRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Doação não encontrada");
        }
        doacoesRepository.delete(id);
    }
}
