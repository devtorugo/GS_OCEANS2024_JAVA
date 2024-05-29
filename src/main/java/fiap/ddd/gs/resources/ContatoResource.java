package fiap.ddd.gs.resources;

import fiap.ddd.gs.entities.Contato;
import fiap.ddd.gs.repositories.ContatoRepository;
import fiap.ddd.gs.utils.Log4Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("contatos")
public class ContatoResource {

    private final ContatoRepository contatoRepository = new ContatoRepository();
    private static final Log4Logger logger = new Log4Logger(ContatoResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contato> getContatos() {
        try {
            return contatoRepository.getAll();
        } catch (Exception e) {
            logger.error("Erro ao obter contatos: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionarContato(Contato contato) {
        try {
            contatoRepository.create(contato);
            return Response.status(Response.Status.CREATED).entity(contato).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar contato: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarContato(@PathParam("id") int id, Contato contatoAtualizado) {
        try {
            if (contatoRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Contato não encontrado").build();
            }
            contatoAtualizado.setId(id);
            contatoRepository.update(contatoAtualizado);
            return Response.status(Response.Status.OK).entity(contatoAtualizado).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar contato: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarContato(@PathParam("id") int id) {
        try {
            if (contatoRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Contato não encontrado").build();
            }
            contatoRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Contato removido").build();
        } catch (Exception e) {
            logger.error("Erro ao deletar contato: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
