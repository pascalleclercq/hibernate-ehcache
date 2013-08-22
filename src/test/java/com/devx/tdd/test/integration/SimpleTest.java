package com.devx.tdd.test.integration;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.developer.article.dao.IGenericDao;
import com.developer.article.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:config.xml")
public class SimpleTest {

	@Autowired
	private IGenericDao<Person, Integer> personDAO;

	private SessionFactory sf;

	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}

	private void simulateNewTx() {
		TransactionSynchronizationManager.bindResource(sf,
				new SessionHolder(sf.openSession()));
	}

	private void simulateEndTx() {
		SessionHolder session = (SessionHolder) TransactionSynchronizationManager
				.getResource(sf);
		session.getSession().close();
		TransactionSynchronizationManager.unbindResource(sf);
	}

	@Test
	public void warmUp() throws Exception {

		Assert.assertNotNull(personDAO);
	}

	@Test
	public void retrieveMenyTimeAPerson() throws Exception {
		Person newInstance = new Person();
		newInstance.setFirstName("first");
		newInstance.setFirstName("last");
		newInstance.setEmail("email");
		simulateNewTx();
		Integer id = personDAO.save(newInstance);
		simulateEndTx();
		System.err.println("new Id " + id);
		Assert.assertNotNull(id);

		for (int i = 0; i < 1000; i++) {
			simulateNewTx();
			Person person = personDAO.findByPrimaryKey(id);
			Assert.assertNotNull(person);
			simulateEndTx();
		}
	}
}
