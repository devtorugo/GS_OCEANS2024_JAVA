package fiap.ddd.gs.resources;

import fiap.ddd.gs.entities.Especimes;
import fiap.ddd.gs.repositories.EspecimesRepository;
import fiap.ddd.gs.utils.Log4Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("especimes")
public class EspecimesResource {

    private final EspecimesRepository especimesRepository = new EspecimesRepository();
    private static final Log4Logger logger = new Log4Logger(EspecimesResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Especimes> getEspecimes() {
        try {
            return especimesRepository.getAll();
        } catch (Exception e) {
            logger.error("Erro ao obter espécimes: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEspecime(@PathParam("id") int id) {
        try {
            Optional<Especimes> especime = especimesRepository.getById(id);
            if (especime.isPresent()) {
                return Response.ok(especime.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Espécime não encontrado").build();
            }
        } catch (Exception e) {
            logger.error("Erro ao obter espécime: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionarEspecime(Especimes especime) {
        try {
            especimesRepository.create(especime);
            return Response.status(Response.Status.CREATED).entity(especime).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar espécime: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarEspecime(@PathParam("id") int id, Especimes especimeAtualizado) {
        try {
            if (especimesRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Espécime não encontrado").build();
            }
            especimeAtualizado.setId(id);
            especimesRepository.update(especimeAtualizado);
            return Response.status(Response.Status.OK).entity(especimeAtualizado).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar espécime: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarEspecime(@PathParam("id") int id) {
        try {
            if (especimesRepository.getById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Espécime não encontrado").build();
            }
            especimesRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Espécime removido").build();
        } catch (Exception e) {
            logger.error("Erro ao deletar espécime: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
