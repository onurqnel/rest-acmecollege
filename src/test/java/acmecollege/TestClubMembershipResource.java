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
import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;

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
import acmecollege.entity.Student;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestClubMembershipResource {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);

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

//	@Test
//	public void test09_add_ClubMembership_adminrole() {
//		Student testStudent = new Student();
//		testStudent.setFullName("Josh", "Duffenais");
//		ClubMembership newClubMembership = new ClubMembership();
//		
//		MembershipCard newMemCard = new MembershipCard(newClubMembership, testStudent, (byte) (0));
//		
//		newClubMembership.setCard(newMemCard);
//		AcademicStudentClub mockStudentClub = new AcademicStudentClub();
//		mockStudentClub.setName("TestClub");
//		newClubMembership.setStudentClub(mockStudentClub);
//		DurationAndStatus mockDurationAndStatus = new DurationAndStatus();
//		newClubMembership.setDurationAndStatus(mockDurationAndStatus);
//
//		WebTarget target = webTarget.path(CLUB_MEMBERSHIP_RESOURCE_NAME).register(adminAuth);
//		Response response = target.request().post(Entity.json(newClubMembership));
//		Entity<ClubMembership> ne = Entity.json(newClubMembership);
//		
//		
//		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
//		ClubMembership returnedMembership = response.readEntity(ClubMembership.class);
//		assertNotNull(returnedMembership);
//	}
//
//	@Test
//	public void test10_update_ClubMembership_adminRole() {
//		int membershipIdToUpdate = 1;
//
//		LocalDateTime newStartDate = LocalDateTime.now();
//		LocalDateTime newEndDate = LocalDateTime.now().plusMonths(6);
//
//		DurationAndStatus updatedDurationAndStatus = new DurationAndStatus();
//		updatedDurationAndStatus.setStartDate(newStartDate);
//		updatedDurationAndStatus.setEndDate(newEndDate);
//
//		ClubMembership updatedInfo = new ClubMembership();
//		updatedInfo.setId(membershipIdToUpdate);
//		updatedInfo.setDurationAndStatus(updatedDurationAndStatus);
//
//		WebTarget target = webTarget.path("clubmemberships/" + membershipIdToUpdate).register(adminAuth);
//
//		Response response = target.request().put(Entity.json(updatedInfo));
//
//		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//
//		ClubMembership updatedMembership = response.readEntity(ClubMembership.class);
//		assertNotNull(updatedMembership);
//		assertEquals(newStartDate, updatedMembership.getDurationAndStatus().getStartDate());
//		assertEquals(newEndDate, updatedMembership.getDurationAndStatus().getEndDate());
//	}

	@Test
	public void test11_add_ClubMembership_userrole() {
		ClubMembership newClubMembership = new ClubMembership();

		AcademicStudentClub mockStudentClub = new AcademicStudentClub();
		mockStudentClub.setName("TestClub");
		mockStudentClub.setId(1);
		newClubMembership.setStudentClub(mockStudentClub);

		MembershipCard mockMembershipCard = new MembershipCard();
		newClubMembership.setCard(mockMembershipCard);

		DurationAndStatus mockDurationAndStatus = new DurationAndStatus();
		newClubMembership.setDurationAndStatus(mockDurationAndStatus);

		WebTarget target = webTarget.path("clubmemberships").register(userAuth);
		Response response = target.request().post(Entity.json(newClubMembership));
		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test12_update_ClubMembership_userrole() {
		int membershipIdToUpdate = 1;

		LocalDateTime newStartDate = LocalDateTime.now();
		LocalDateTime newEndDate = LocalDateTime.now().plusMonths(6);

		DurationAndStatus updatedDurationAndStatus = new DurationAndStatus();
		updatedDurationAndStatus.setStartDate(newStartDate);
		updatedDurationAndStatus.setEndDate(newEndDate);

		ClubMembership updatedInfo = new ClubMembership();
		updatedInfo.setId(membershipIdToUpdate);
		updatedInfo.setDurationAndStatus(updatedDurationAndStatus);

		WebTarget target = webTarget.path("clubmemberships/" + membershipIdToUpdate).register(userAuth);

		Response response = target.request().put(Entity.json(updatedInfo));
		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

}
