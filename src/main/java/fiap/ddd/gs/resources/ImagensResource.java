package fiap.ddd.gs.resources;

import fiap.ddd.gs.entities.Imagens;
import fiap.ddd.gs.repositories.ImagensRepository;
import fiap.ddd.gs.utils.Log4Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("imagens")
public class ImagensResource {

    private final ImagensRepository imagensRepository = new ImagensRepository();
    private static final Log4Logger logger = new Log4Logger(ImagensResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Imagens> getImagens() {
        try {
            return imagensRepository.getAll();
        } catch (Exception e) {
            logger.error("Erro ao obter imagens: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImagem(@PathParam("id") int id) {
        try {
            Optional<Imagens> imagem = imagensRepository.getById(id);
            if (imagem.isPresent()) {
                return Response.ok(imagem.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Imagem não encontrada").build();
            }
        } catch (Exception e) {
            logger.error("Erro ao obter imagem: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionarImagem(Imagens imagem) {
        try {
            imagensRepository.create(imagem);
            return Response.status(Response.Status.CREATED).entity(imagem).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar imagem: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarImagem(@PathParam("id") int id, Imagens imagemAtualizada) {
        try {
            if (imagensRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Imagem não encontrada").build();
            }
            imagemAtualizada.setId(id);
            imagensRepository.update(imagemAtualizada);
            return Response.status(Response.Status.OK).entity(imagemAtualizada).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar imagem: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarImagem(@PathParam("id") int id) {
        try {
            if (imagensRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Imagem não encontrada").build();
            }
            imagensRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Imagem removida").build();
        } catch (Exception e) {
            logger.error("Erro ao deletar imagem: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
