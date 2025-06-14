/*
 * @author Onur Onel
 * @author Asher Fincham
 * @author Lewis Brown
 * @author Josh Duffenais
 *
 */
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import acmecollege.entity.MembershipCard;
import acmecollege.entity.Student;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestMembershipCardResource {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);
	private static int cardId=0;
	private static Student testStudent ;

	static final String HTTP_SCHEMA = "http";
	static final String HOST = "localhost";
	static final int PORT = 8080;

	// Test fixture(s)
	static URI uri;
	static HttpAuthenticationFeature adminAuth;
	static HttpAuthenticationFeature userAuth;

	@BeforeAll
	public static void oneTimeSetUp() throws Exception {
		logger.debug("oneTimeSetUp");
		uri = UriBuilder.fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION).scheme(HTTP_SCHEMA).host(HOST)
				.port(PORT).build();
		adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
		userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
	}

	protected WebTarget webTarget;

	@BeforeEach
	public void setUp() {
		Client client = ClientBuilder
				.newClient(new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
		webTarget = client.target(uri);
	}

	@Test
	public void test22_getById_MembershipCard_adminrole() {
		int cardIdToFetch = 1;
		WebTarget target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME +'/' + cardIdToFetch).register(adminAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		List<MembershipCard> fetchedCards = response.readEntity(new GenericType<List<MembershipCard>>() {
		});
		assertNotNull(fetchedCards);
	}

	@Test
	public void test23_create_MembershipCard_adminrole() {
		int studentIdToFetch = 1;
		WebTarget target = webTarget.path("student/" + studentIdToFetch).register(adminAuth);
		Response response = target.request().get();
		Student fetchedStudent = response.readEntity(Student.class);
		testStudent = fetchedStudent;
		
		MembershipCard newCard = new MembershipCard();
		newCard.setOwner(testStudent);
		newCard.setSigned(false);
		
		target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).register(adminAuth);
		response = target.request().post(Entity.json(newCard));
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		
	}


	@Test
	public void test24_delete_MembershipCard_adminrole() {
		int cardIdToDelete = cardId;
		WebTarget target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME +'/' + cardIdToDelete).register(adminAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

//	@Test
//	public void test25_getById_MembershipCard_userrole() {
//		int cardIdToFetch = 1;
//		WebTarget target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME +'/' + cardIdToFetch).register(userAuth);
//
//		Response response = target.request().get();
//
//		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//		MembershipCard fetchedCard = response.readEntity(MembershipCard.class);
//		assertNotNull(fetchedCard);
//	}

	@Test
	public void test26_create_MembershipCard_userrole() {
		MembershipCard newCard = new MembershipCard();
		WebTarget target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).register(userAuth);

		Response response = target.request().post(Entity.json(newCard));

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test27_delete_MembershipCard_userrole() {
		int cardIdToDelete = 1;
		WebTarget target = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME +'/' + cardIdToDelete).register(userAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

}
