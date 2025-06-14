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

import javax.persistence.Basic;
import javax.persistence.Column;
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

import acmecollege.entity.Course;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCourseResource {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);
	private static int courseID = 0;

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
	public void test13_getById_Course_adminrole() {
		int courseIdToFetch = 1;
		WebTarget target = webTarget.path("course/" + courseIdToFetch).register(adminAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Course fetchedCourse = response.readEntity(Course.class);
		assertNotNull(fetchedCourse);
	}

	@Test
	public void test14_create_Course_adminrole() {
		Course newCourse = new Course();
		newCourse.setCourseCode("cc1");
		newCourse.setCourseTitle("CourseTitle1");
		newCourse.setYear(2023);
		newCourse.setSemester("23F");
		newCourse.setCreditUnits(1);
		newCourse.setOnline((byte) 0);
		
		WebTarget target = webTarget.path("course").register(adminAuth);

		Response response = target.request().post(Entity.json(newCourse));

		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		Course addedCourse = response.readEntity(Course.class);
		courseID = addedCourse.getId();
		assertNotNull(addedCourse);
	}

	@Test
	public void test15_delete_Course_adminrole() {
		int courseIdToDelete = courseID;
		logger.debug(courseIdToDelete);
		
		WebTarget target = webTarget.path("course"+'/' + courseIdToDelete).register(adminAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	public void test16_getById_Course_userrole() {
		int courseIdToFetch = 1;
		WebTarget target = webTarget.path("course/" + courseIdToFetch).register(userAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Course fetchedCourse = response.readEntity(Course.class);
		assertNotNull(fetchedCourse);
	}

	@Test
	public void test17_create_Course_userrole() {
		Course newCourse = new Course();
		WebTarget target = webTarget.path("course").register(userAuth);

		Response response = target.request().post(Entity.json(newCourse));

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test18_delete_Course_userrole() {
		int courseIdToDelete = 1;
		WebTarget target = webTarget.path("course/" + courseIdToDelete).register(userAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

}
