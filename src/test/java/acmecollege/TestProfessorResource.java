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

import acmecollege.entity.Professor;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestProfessorResource {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);
	private static Professor newprof;

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
	public void test44_getById_Professor_adminrole() {
		int professorIdToFetch = 1;
		WebTarget target = webTarget.path("professor/" + professorIdToFetch).register(adminAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Professor fetchedProfessor = response.readEntity(Professor.class);
		assertNotNull(fetchedProfessor);
	}

	@Test
	public void test45_add_Professor_adminrole() {
		Professor newProfessor = new Professor();
		newProfessor.setFirstName("John");
		newProfessor.setLastName("Doe");
		newProfessor.setDepartment("newDep");

		WebTarget target = webTarget.path("professor").register(adminAuth);

		Response response = target.request().post(Entity.json(newProfessor));
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		Professor addedProfessor = response.readEntity(Professor.class);
		newprof = addedProfessor;
		assertNotNull(addedProfessor);
		assertEquals("John", addedProfessor.getFirstName());
		assertEquals("Doe", addedProfessor.getLastName());
	}

	@Test
	public void test46_delete_Professor_adminrole() {
		
		
		WebTarget deleteTarget = webTarget.path("professor/" + newprof.getId())
				.register(adminAuth);
		
		logger.debug(newprof.getId());
		Response deleteResponse = deleteTarget.request().delete();

		assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());
	}

	@Test
	public void test47_getById_Professor_userrole() {
		int professorIdToFetch = 1;
		WebTarget target = webTarget.path("professor/" + professorIdToFetch).register(userAuth);

		Response response = target.request().get();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Professor fetchedProfessor = response.readEntity(Professor.class);
		assertNotNull(fetchedProfessor);
	}

	@Test
	public void test48_add_Professor_userrole() {
		Professor newProfessor = new Professor();
		newProfessor.setFirstName("John");
		newProfessor.setLastName("Doe");
		newProfessor.setDepartment("newDep");

		WebTarget target = webTarget.path("professor").register(userAuth);

		Response response = target.request().post(Entity.json(newProfessor));

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

	@Test
	public void test49_delete_Professor_userrole() {
		int professorIdToDelete = 1;
		WebTarget target = webTarget.path("professor/" + professorIdToDelete).register(userAuth);

		Response response = target.request().delete();

		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
	}

}
