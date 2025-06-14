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

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;

import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;
import org.apache.logging.log4j.LogManager;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.MembershipCard;

@Path("/membershipcards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ACMECollegeService service;

	@Inject
	protected SecurityContext sc;

	@GET
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({ADMIN_ROLE})
    public Response getCardsById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
    	LOG.debug("try to retrieve specific membershipcard " + id);
        Response response = null;
        List <MembershipCard> card = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            card = service.getAllWithId(MembershipCard.class, MembershipCard.STUDENT_ID_CARD_QUERY_NAME, id);
            response = Response.status(card == null ? Status.NOT_FOUND : Status.OK).entity(card).build();
        }
        else {
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

	@GET
	@RolesAllowed({ ADMIN_ROLE })
	public Response getAllMembershipCards() {
		LOG.debug("Retrieving all membership cards...");
		List<MembershipCard> cards = service.getAll(MembershipCard.class, MembershipCard.ALL_CARDS_QUERY_NAME);
		return Response.ok(cards).build();
	}

	@POST
	@RolesAllowed({ ADMIN_ROLE })
	public Response addMembershipCard(MembershipCard newCard) {
		LOG.debug("Adding new membership card: {}", newCard);
		MembershipCard addedCard = service.persistMembershipCard(newCard);
		return Response.status(Response.Status.CREATED).entity(addedCard).build();
	}

	@DELETE
	@Path("/{cardId}")
	@RolesAllowed({ ADMIN_ROLE })
	public Response deleteMembershipCard(@PathParam("cardId") int cardId) {
		service.deleteMembershipCardById(cardId);
		return Response.noContent().build();
	}

	// Update method (PUT) can be added
}
