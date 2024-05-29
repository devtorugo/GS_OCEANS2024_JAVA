package fiap.ddd.gs.services;

import fiap.ddd.gs.entities.Contato;
import fiap.ddd.gs.repositories.ContatoRepository;

import java.util.List;

public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public List<Contato> getContatos() {
        try {
            return contatoRepository.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter contatos: " + e.getMessage());
        }
    }

    public void adicionarContato(Contato contato) {
        contatoRepository.create(contato);
    }

    public void atualizarContato(Contato contato) {
        if (contatoRepository.getById(contato.getId()).isEmpty()) {
            throw new IllegalArgumentException("Contato não encontrado");
        }
        contatoRepository.update(contato);
    }

    public void deletarContato(int id) {
        if (contatoRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Contato não encontrado");
        }
        contatoRepository.delete(id);
    }
}
