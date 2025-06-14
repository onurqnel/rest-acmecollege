/*
 * Dec 2023
 *  Created and
 *  * Updated by:  Group 10
 *   Lewis Brown
 *   Josh Duffenais
 *   Asher Fincham
 *   Onur Önel
 *
 *
 */
package acmecollege.rest.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;

@Path("/clubmemberships")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @GET
    public Response getAllClubMemberships() {
        LOG.debug("Retrieving all club memberships...");
        List<ClubMembership> memberships = service.getAll(ClubMembership.class, ClubMembership.FIND_ALL);
        return Response.ok(memberships).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addClubMembership(ClubMembership newClubMembership) {
        LOG.debug("Adding new club membership: {}", newClubMembership);
        ClubMembership addedMembership = service.persistClubMembership(newClubMembership);
        return Response.status(Response.Status.CREATED).entity(addedMembership).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateClubMembership(@PathParam("id") int id, ClubMembership clubMembership) {
        LOG.debug("Updating club membership with id: {}", id);
        ClubMembership updatedMembership = service.updateClubMembership(id, clubMembership);
        if (updatedMembership == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("ClubMembership not found").build();
        }
        return Response.ok(updatedMembership).build();
    }
}
