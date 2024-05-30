package fiap.ddd.gs.services;

import fiap.ddd.gs.entities.Especimes;
import fiap.ddd.gs.repositories.EspecimesRepository;

import java.util.List;
import java.util.Optional;

public class EspecimesService {

    private final EspecimesRepository especimesRepository;

    public EspecimesService(EspecimesRepository especimesRepository) {
        this.especimesRepository = especimesRepository;
    }

    public List<Especimes> getEspecimes() {
        try {
            return especimesRepository.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter espécimes: " + e.getMessage());
        }
    }

    public Optional<Especimes> getEspecimeById(int id) {
        try {
            return especimesRepository.getById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter espécime: " + e.getMessage());
        }
    }

    public void adicionarEspecime(Especimes especime) {
        if (especime.getId() != 0) {
            throw new IllegalArgumentException("ID do espécime deve ser gerado automaticamente.");
        }
        especimesRepository.create(especime);
    }

    public void atualizarEspecime(Especimes especime) {
        if (especimesRepository.getById(especime.getId()).isEmpty()) {
            throw new IllegalArgumentException("Espécime não encontrado");
        }
        especimesRepository.update(especime);
    }

    public void deletarEspecime(int id) {
        if (especimesRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Espécime não encontrado");
        }
        especimesRepository.delete(id);
    }
}
