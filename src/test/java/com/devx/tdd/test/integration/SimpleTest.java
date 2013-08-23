package com.devx.tdd.test.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.developer.article.dao.IGenericDao;
import com.developer.article.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:config.xml")
public class SimpleTest {

	@Autowired
	private IGenericDao<Person, Integer> personDAO;

	@Test
	public void warmUp() throws Exception {
		Assert.assertNotNull(personDAO);
	}

	@Test
	public void retrieveMenyTimeAPerson() throws Exception {
		int j = 0;
		while (j < 100) {
			Person newInstance = new Person();
			newInstance.setFirstName("first");
			newInstance.setFirstName("last");
			newInstance.setEmail("email");
			Integer id = personDAO.save(newInstance);
			//System.err.println("new Id " + id);
			Assert.assertNotNull(id);
			for (int i = 0; i < 100; i++) {
				Person person = personDAO.findByPrimaryKey(id);
				Assert.assertNotNull(person);
			}
			j++;
		}

	}
}
