package es.uvigo.esei.daa.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.esei.daa.dao.DAOException;
import es.uvigo.esei.daa.dao.PetsDAO;
import es.uvigo.esei.daa.entities.Pet;

/**
 * REST resource for managing people.
 *
 * @author Miguel Reboiro Jato.
 */
@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
public class PetsResource {
    private final static Logger LOG = Logger.getLogger(PetsResource.class.getName());

    private final PetsDAO dao;


    public PetsResource() {
        this(new PetsDAO());
    }

    // Needed for testing purposes
    PetsResource(PetsDAO dao) {
        this.dao = dao;
    }


    @GET
    @Path("/{personId}")
    public Response personsPets(
            @PathParam("personId") int id
    ) {
        try {
            return Response.ok(this.dao.personsPets(id)).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid pet id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a pet", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }



    @GET
    public Response list() {
        try {
            return Response.ok(this.dao.list()).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing pets", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

/**
    @POST
    public Response add(
            @FormParam("name") String name,
            @FormParam("surname") String surname
    ) {
        try {
            final Person newPerson = this.dao.add(name, surname);

            return Response.ok(newPerson).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response modify(
            @PathParam("id") int id,
            @FormParam("name") String name,
            @FormParam("surname") String surname
    ) {
        try {
            final Person modifiedPerson = new Person(id, name, surname);
            this.dao.modify(modifiedPerson);

            return Response.ok(modifiedPerson).build();
        } catch (NullPointerException npe) {
            final String message = String.format("Invalid data for person (name: %s, surname: %s)", name, surname);

            LOG.log(Level.FINE, message);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in modify method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error modifying a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

*/
    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id") int id
    ) {
        try {
            this.dao.delete(id);

            return Response.ok(id).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid pet id in delete method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error deleting a pet", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }
}
