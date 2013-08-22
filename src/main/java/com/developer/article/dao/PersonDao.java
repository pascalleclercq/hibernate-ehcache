package com.developer.article.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.developer.article.model.Person;

/**
 * Person DAO
 * @author Jacek Furmankiewicz
 */
@Repository
@Transactional
public class PersonDao extends GenericDaoImpl<Person,Integer>{

	private static final Logger LOG = LoggerFactory.getLogger(PersonDao.class);
	
}
