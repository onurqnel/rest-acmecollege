/**
 * File:  TestACMECollegeSystem.java
 * Course materials (23S) CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 *   Updated by:  Group 10
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
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.STUDENT_CLUB_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.Student;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.StudentClub;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {
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

	@Test
	public void test01_all_Students_adminrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(STUDENT_RESOURCE_NAME).request().get();
		assertThat(response.getStatus(), is(200));
		List<Student> students = response.readEntity(new GenericType<List<Student>>() {
		});
		assertThat(students, is(not(empty())));
		//assertThat(students, hasSize(1));
	}

	@Test
	public void test02_all_ClubMemberships_adminrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(CLUB_MEMBERSHIP_RESOURCE_NAME).request().get();

		assertThat(response.getStatus(), is(200));
		List<ClubMembership> memberships = response.readEntity(new GenericType<List<ClubMembership>>() {
		});

		assertThat(memberships, is(not(empty())));
		assertThat(memberships, hasSize(2));
	}

	@Test
	public void test03_all_Courses_adminrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(COURSE_RESOURCE_NAME).request().get();

		assertThat(response.getStatus(), is(200));
		List<Course> courses = response.readEntity(new GenericType<List<Course>>() {
		});

		assertThat(courses, is(not(empty())));
		assertThat(courses, hasSize(2));
	}

	@Test
	public void test04_all_MembershipCards_adminrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(MEMBERSHIP_CARD_RESOURCE_NAME).request().get();

		assertThat(response.getStatus(), is(200));
		List<MembershipCard> membershipCards = response.readEntity(new GenericType<List<MembershipCard>>() {
		});

		assertThat(membershipCards, is(not(empty())));
		assertThat(membershipCards, hasSize(2));
	}

	@Test
	public void test05_all_StudnentClubs_adminrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(STUDENT_CLUB_RESOURCE_NAME).request().get();

		assertThat(response.getStatus(), is(200));
		List<StudentClub> studentClubs = response.readEntity(new GenericType<List<StudentClub>>() {});


		assertThat(studentClubs, is(not(empty())));
		assertThat(studentClubs, hasSize(2));
	}

	@Test
	public void test06_all_Students_userrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(userAuth).path(STUDENT_RESOURCE_NAME).request().get();
		int status = response.getStatus();
		assertTrue(status == 403 || status == 401,
				"Expected 403 Forbidden or 401 Unauthorized for non-admin role" + status);
	}

	@Test
	public void test07_all_Courses_userrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(userAuth).path(COURSE_RESOURCE_NAME).request().get();
		int status = response.getStatus();
		assertTrue(status == 403 || status == 401,
				"Expected 403 Forbidden or 401 Unauthorized for non-admin role" + status);
	}

	@Test
	public void test08_all_MembershipCards_userrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(userAuth).path(MEMBERSHIP_CARD_RESOURCE_NAME).request().get();
		int status = response.getStatus();
		assertTrue(status == 403 || status == 401,
				"Expected 403 Forbidden or 401 Unauthorized for non-admin role" + status);
	}

}