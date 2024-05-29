package fiap.ddd.gs.services;

import fiap.ddd.gs.entities.Cadastro;
import fiap.ddd.gs.repositories.CadastroRepository;

import java.util.List;

public class CadastroService {

    private final CadastroRepository cadastroRepository;

    public CadastroService(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
    }

    public List<Cadastro> getCadastros() {
        try {
            return cadastroRepository.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter cadastros: " + e.getMessage());
        }
    }

    public void adicionarCadastro(Cadastro cadastro) {
        if (cadastro.getId() == 0) {
            throw new IllegalArgumentException("ID do cadastro não especificado.");
        }
        cadastroRepository.create(cadastro);
    }

    public void atualizarCadastro(Cadastro cadastro) {
        if (cadastroRepository.getById(cadastro.getId()).isEmpty()) {
            throw new IllegalArgumentException("Cadastro não encontrado");
        }
        cadastroRepository.update(cadastro);
    }

    public void deletarCadastro(int id) {
        if (cadastroRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Cadastro não encontrado");
        }
        cadastroRepository.delete(id);
    }
}
