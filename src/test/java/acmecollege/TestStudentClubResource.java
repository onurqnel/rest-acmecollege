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
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDateTime;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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

import acmecollege.entity.AcademicStudentClub;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.DurationAndStatus;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.StudentClub;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestStudentClubResource {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);
	private static int clubID=0;

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
	public void test28_getById_StudentClub_adminrole() {
		int clubIdToFetch = 2;
		WebTarget target = webTarget.path("studentclub/" + clubIdToFetch).register(adminAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		StudentClub fetchedClub = response.readEntity(StudentClub.class);
		assertNotNull(fetchedClub);
	}

	@Test
	public void test29_add_StudentClub_adminrole() {
		AcademicStudentClub newClub = new AcademicStudentClub();
		newClub.setName("Test Club Name");
		

		WebTarget target = webTarget.path("studentclub").register(adminAuth);

		Response response = target.request().post(Entity.json(newClub));
		Entity<StudentClub>entity = Entity.json(newClub);

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		StudentClub addedClub = response.readEntity(StudentClub.class);
		clubID = addedClub.getId();
		assertNotNull(addedClub);
		assertEquals("Test Club Name", addedClub.getName());
	}

	@Test
	public void test30_delete_StudentClub_adminrole() {
		int clubIdToDelete = clubID;
		WebTarget target = webTarget.path("studentclub/" + clubIdToDelete).register(adminAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

//	@Test
//	public void test31_update_StudentClub_adminrole() {
//		int clubIdToUpdate = 1;
//
//		AcademicStudentClub updatedClub = new AcademicStudentClub();
//		updatedClub.setName("Updated Club Name");
//		updatedClub.setId(98);
//
//		WebTarget target = webTarget.path("studentclub/" + clubIdToUpdate).register(adminAuth);
//
//		Response response = target.request().put(Entity.json(updatedClub));
//
//		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//
//		StudentClub updatedStudentClub = response.readEntity(StudentClub.class);
//		assertNotNull(updatedStudentClub);
//		assertEquals("Updated Club Name", updatedStudentClub.getName());
//	}

	@Test
	public void test32_addClubMembershipTo_StudentClub_adminrole() {
		int clubId = 1;

		ClubMembership newMembership = new ClubMembership();
		MembershipCard newCard = new MembershipCard();
		newMembership.setCard(newCard);
		DurationAndStatus durationAndStatus = new DurationAndStatus();
		durationAndStatus.setStartDate(LocalDateTime.now());
		durationAndStatus.setEndDate(LocalDateTime.now().plusMonths(6));
		newMembership.setDurationAndStatus(durationAndStatus);

		WebTarget target = webTarget.path("studentclub/" + clubId + "/clubmembership").register(adminAuth);

		Response response = target.request().post(Entity.json(newMembership));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		StudentClub updatedClub = response.readEntity(StudentClub.class);
		assertNotNull(updatedClub);
	}

	@Test
	public void test33_getById_StudentClub_userrole() {
		int clubIdToFetch = 2;
		WebTarget target = webTarget.path("studentclub/" + clubIdToFetch).register(userAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		StudentClub fetchedClub = response.readEntity(StudentClub.class);
		assertNotNull(fetchedClub);
	}

	@Test
	public void test34_add_StudentClub_userrole() {
		AcademicStudentClub newClub = new AcademicStudentClub();
		newClub.setName("Test Club Name");
		newClub.setId(31);

		WebTarget target = webTarget.path("studentclub").register(userAuth);
		Response response = target.request().post(Entity.json(newClub));

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test35_delete_StudentClub_userrole() {
		int clubIdToDelete = 7;
		WebTarget target = webTarget.path("studentclub/" + clubIdToDelete).register(userAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

	}

//	@Test
//	public void test36_update_StudentClub_userrole() {
//		int clubIdToUpdate = 1;
//		AcademicStudentClub updatedClub = new AcademicStudentClub();
//		updatedClub.setName("Updated Club Name");
//
//		WebTarget target = webTarget.path("studentclub/" + clubIdToUpdate).register(userAuth);
//
//		Response response = target.request().put(Entity.json(updatedClub));
//
//		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
//	}

	@Test
	public void test37_addClubMembershipTo_StudentClub_userrole() {
		int clubId = 1;

		ClubMembership newMembership = new ClubMembership();
		MembershipCard newCard = new MembershipCard();
		newMembership.setCard(newCard);
		DurationAndStatus durationAndStatus = new DurationAndStatus();
		durationAndStatus.setStartDate(LocalDateTime.now());
		durationAndStatus.setEndDate(LocalDateTime.now().plusMonths(6));
		newMembership.setDurationAndStatus(durationAndStatus);

		WebTarget target = webTarget.path("studentclub/" + clubId + "/clubmembership").register(userAuth);
		Response response = target.request().post(Entity.json(newMembership));
		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}
}
