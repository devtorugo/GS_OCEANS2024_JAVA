package fiap.ddd.gs.resources;

import fiap.ddd.gs.entities.Doacoes;
import fiap.ddd.gs.repositories.DoacoesRepository;
import fiap.ddd.gs.utils.Log4Logger;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("doacoes")
public class DoacoesResource {

    private final DoacoesRepository doacoesRepository = new DoacoesRepository();
    private static final Log4Logger logger = new Log4Logger(DoacoesResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Doacoes> getDoacoes() {
        try {
            return doacoesRepository.getAll();
        } catch (Exception e) {
            logger.error("Erro ao obter doações: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoacao(@PathParam("id") int id) {
        try {
            Optional<Doacoes> doacao = doacoesRepository.getById(id);
            if (doacao.isPresent()) {
                return Response.ok(doacao.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Doação não encontrada").build();
            }
        } catch (Exception e) {
            logger.error("Erro ao obter doação: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionarDoacao(Doacoes doacao) {
        try {
            doacoesRepository.create(doacao);
            return Response.status(Response.Status.CREATED).entity(doacao).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar doação: " + e.getMessage());
            throw new RuntimeException("Erro ao adicionar doação", e);
        }
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarDoacao(@PathParam("id") int id, Doacoes doacaoAtualizada) {
        try {
            if (doacoesRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Doação não encontrada").build();
            }
            doacaoAtualizada.setId(id);
            doacoesRepository.update(doacaoAtualizada);
            return Response.status(Response.Status.OK).entity(doacaoAtualizada).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar doação: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarDoacao(@PathParam("id") int id) {
        try {
            if (doacoesRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Doação não encontrada").build();
            }
            doacoesRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Doação removida").build();
        } catch (Exception e) {
            logger.error("Erro ao deletar doação: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
