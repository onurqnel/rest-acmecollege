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

import acmecollege.entity.Student;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestStudentResource {
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
	public void test38_getById_Student_adminrole() {
		int studentIdToFetch = 1;
		WebTarget target = webTarget.path("student/" + studentIdToFetch).register(adminAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Student fetchedStudent = response.readEntity(Student.class);
		assertNotNull(fetchedStudent);
	}

	@Test
	public void test39_add_Student_adminrole() {
		Student newStudent = new Student();
		newStudent.setFirstName("John");
		newStudent.setLastName("Doe");

		WebTarget target = webTarget.path("student").register(adminAuth);

		Response response = target.request().post(Entity.json(newStudent));
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		Student addedStudent = response.readEntity(Student.class);
		assertNotNull(addedStudent);
		assertEquals("John", addedStudent.getFirstName());
		assertEquals("Doe", addedStudent.getLastName());
	}

	@Test
	public void test40_delete_Student_adminrole() {
		Student newStudent = new Student();
		newStudent.setFirstName("John");
		newStudent.setLastName("Doe");

		WebTarget target = webTarget.path("student").register(adminAuth);

		Response response = target.request().post(Entity.json(newStudent));
		

		Student addedStudent = response.readEntity(Student.class);
		
		WebTarget deleteTarget = webTarget.path("student/" + addedStudent.getId())
				.register(adminAuth);
		
		logger.debug(addedStudent.getId());
		Response deleteResponse = deleteTarget.request().delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());
	}

	@Test
	public void test41_getById_Student_userrole() {
		int studentIdToFetch = 1;
		WebTarget target = webTarget.path("student/" + studentIdToFetch).register(userAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Student fetchedStudent = response.readEntity(Student.class);
		assertNotNull(fetchedStudent);
	}

	@Test
	public void test42_add_Student_userrole() {
		Student newStudent = new Student();
		newStudent.setFirstName("John");
		newStudent.setLastName("Doe");

		WebTarget target = webTarget.path("student").register(userAuth);

		Response response = target.request().post(Entity.json(newStudent));

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test43_delete_Student_userrole() {
		int studentIdToDelete = 1;
		WebTarget target = webTarget.path("student/" + studentIdToDelete).register(userAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

}
