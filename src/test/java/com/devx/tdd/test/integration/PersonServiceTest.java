package com.devx.tdd.test.integration;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.developer.article.model.Person;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PersonServiceTest {

	public static final String URI = "http://localhost:8080/hibernate-echcache/services/person";

	@Before
	public void setup() throws HttpException, IOException {
		// delete all Person data
		Client client = new Client();

		WebResource wr = client.resource(URI);
		wr.delete();
	}

	@Test
	public void warmUp() throws HttpException, IOException {

	}

	@Test
	public void testCRUD() throws HttpException, IOException {

		Client client = new Client();
		WebResource wr = client.resource(URI);

		for (int i = 0; i < 10; i++) {

			Person newPerson = new Person();
			newPerson.setFirstName("John");
			newPerson.setLastName("Doe");
			newPerson.setEmail("john.doe@gmail.com");

			ClientResponse response = wr.type("application/json").post(
					ClientResponse.class, newPerson);

			assertEquals(201, response.getStatus()); // 201 = Created
			
			Person created = response.getEntity(Person.class);
			Integer id = created.getId();
			// String uri = response.getHeaders().getFirst("Location");
			// System.out.println(uri);
			// verify it exists
			WebResource personResource = client.resource(URI + "/" + id);

			for (int j = 0; j < 10; j++) {
				response = personResource.get(ClientResponse.class);

			}
			assertEquals(200, response.getStatus()); // 200 = OK
			String json = response.getEntity(String.class);

			ObjectMapper mapper = new ObjectMapper();
			Person person = mapper.readValue(json, Person.class);
			assertEquals("John", person.getFirstName());
			assertEquals("Doe", person.getLastName());
			assertEquals("john.doe@gmail.com", person.getEmail());
			// perform an update, one field at a time and test it
			person.setEmail("john.doe@free.fr");
			response = personResource.type("application/json").put(
					ClientResponse.class, person);
			assertEquals(200, response.getStatus());

			// verify it got updated
			response = personResource.get(ClientResponse.class);
			assertEquals(200, response.getStatus());
			json = response.getEntity(String.class);
			person = mapper.readValue(json, Person.class);
			assertEquals("John", person.getFirstName());
			assertEquals("Doe", person.getLastName());
			assertEquals("john.doe@free.fr", person.getEmail());

			// delete it
			response = personResource.delete(ClientResponse.class);
			assertEquals(200, response.getStatus());

			// verify it does not exist
			response = personResource.get(ClientResponse.class);
			assertEquals(404, response.getStatus()); // 204 = no content
		}
	}

}
