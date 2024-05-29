package fiap.ddd.gs.resources;

import fiap.ddd.gs.entities.Cadastro;
import fiap.ddd.gs.repositories.CadastroRepository;
import fiap.ddd.gs.utils.Log4Logger;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("cadastros")
public class CadastroResource {

    private final CadastroRepository cadastroRepository = new CadastroRepository();
    private static final Log4Logger logger = new Log4Logger(CadastroResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cadastro> getCadastros() {
        try {
            return cadastroRepository.getAll();
        } catch (Exception e) {
            logger.error("Erro ao obter cadastros: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionarCadastro(Cadastro cadastro) {
        try {
            cadastroRepository.create(cadastro);
            return Response.status(Response.Status.CREATED).entity(cadastro).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar cadastro: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarCadastro(@PathParam("id") int id, Cadastro cadastroAtualizado) {
        try {
            if (cadastroRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Cadastro não encontrado").build();
            }
            cadastroAtualizado.setId(id);
            cadastroRepository.update(cadastroAtualizado);
            return Response.status(Response.Status.OK).entity(cadastroAtualizado).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar cadastro: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarCadastro(@PathParam("id") int id) {
        try {
            if (cadastroRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Cadastro não encontrado").build();
            }
            cadastroRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Cadastro removido").build();
        } catch (Exception e) {
            logger.error("Erro ao deletar cadastro: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
