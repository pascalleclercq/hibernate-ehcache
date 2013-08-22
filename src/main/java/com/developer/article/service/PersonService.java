package com.developer.article.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developer.article.dao.IGenericDao;
import com.developer.article.model.Person;

@Service("personService")
@Path(PersonService.PATH)
public class PersonService {
	
	public static final String PATH = "/person";

	private IGenericDao<Person,Integer> dao;
	@Autowired  public void setPersonDao(IGenericDao<Person,Integer> dao) {
		this.dao = dao;
	}
	
	
	
	@GET
	@Path("/hello")
	public String hello() {
		
		return "hello";
	}
	
	
	@GET
	@Path("/list")
	public List<Person> findAll() {
		List<Person> people = dao.findAll();
		return (people.size() > 0) ? people : null;
	}
	
	@GET @Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("id") Integer id) {
		Person p = dao.findByPrimaryKey(id);
		if (p != null) {
			return Response.ok(p).build();
			//return Response.status(Status.OK).entity(p).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Person person) {
		
		try {
//		
//			Person person = new Person();
//			person.setFirstName(firstName);
//			person.setLastName(lastName);
//			person.setEmail(email);
			Integer id = dao.save(person);
			person.setId(id);
			 return Response.status(Status.CREATED).entity(person).type(MediaType.APPLICATION_JSON).build();
			//return Response.created(UriBuilder.fromPath(PATH + "/{0}").build(id)).entity(person).build();
			
		} catch (Exception ex) {
			return Response.serverError().build();
		}
	}
	
	@PUT @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Person newPerson) {
	
		try {
			Person person = dao.findByPrimaryKey(newPerson.getId());
			if (person != null) {
				if (newPerson.getFirstName() != null) {
					person.setFirstName(newPerson.getFirstName());
				}
				if (newPerson.getLastName() != null){
					person.setLastName(newPerson.getLastName());
				}
				if (newPerson.getEmail() != null) {
					person.setEmail(newPerson.getEmail());
				}
				dao.update(person);
				return Response.ok(person).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}
		
	}
	
	@DELETE 
	public Response deleteAll() {
		try {
			dao.deleteAll();
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	
	@DELETE @Path("/{id}")
	public Response delete(@PathParam("id") Integer id) {
		try {
			dao.deleteByPrimaryKey(id);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	
	
}
