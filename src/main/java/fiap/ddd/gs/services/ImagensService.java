package fiap.ddd.gs.services;

import fiap.ddd.gs.entities.Imagens;
import fiap.ddd.gs.repositories.ImagensRepository;

import java.util.List;

public class ImagensService {

    private final ImagensRepository imagensRepository;

    public ImagensService(ImagensRepository imagensRepository) {
        this.imagensRepository = imagensRepository;
    }

    public List<Imagens> getImagens() {
        try {
            return imagensRepository.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter imagens: " + e.getMessage());
        }
    }

    public Imagens getImagemById(int id) {
        try {
            return imagensRepository.getById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Imagem não encontrada"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao obter imagem: " + e.getMessage());
        }
    }

    public void adicionarImagem(Imagens imagem) {
        if (imagem.getId() != 0) {
            throw new IllegalArgumentException("ID da imagem deve ser gerado automaticamente.");
        }
        imagensRepository.create(imagem);
    }

    public void atualizarImagem(Imagens imagem) {
        if (imagensRepository.getById(imagem.getId()).isEmpty()) {
            throw new IllegalArgumentException("Imagem não encontrada");
        }
        imagensRepository.update(imagem);
    }

    public void deletarImagem(int id) {
        if (imagensRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Imagem não encontrada");
        }
        imagensRepository.delete(id);
    }
}
