package com.developer.article.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDaoImpl<E, PK extends Serializable> implements
		IGenericDao<E, PK> {

	private static final Logger LOG = LoggerFactory
			.getLogger(GenericDaoImpl.class);

	private Class<E> entityClass = null;

	private SessionFactory sf;

	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}

	
	public PK save(E newInstance) {
		return (PK) sf.getCurrentSession().save(newInstance);
	}

	public E findByPrimaryKey(PK primaryKey) {
		return (E) sf.getCurrentSession().get(getEntityClass(), primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {

		Session session = sf.getCurrentSession();
		Criteria executableCriteria = createDetachedCriteria()
				.getExecutableCriteria(session);
		return executableCriteria.list();

	}

	
	public void update(E transientObject) {
		sf.getCurrentSession().update(transientObject);
	}

	
	public void delete(E persistentObject) {
		if (persistentObject != null) {
			sf.getCurrentSession().delete(persistentObject);
		} else {
			LOG.warn(getClass().getSimpleName()
					+ ": attempt to delete NULL object");
		}
	}

	
	public void deleteByPrimaryKey(PK primaryKey) {
		E entity = this.findByPrimaryKey(primaryKey);
		this.delete(entity);
	}

	
	public void deleteAll() {
		sf.getCurrentSession()
				.createQuery("DELETE FROM " + getEntityClass().getName())
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public final Class<E> getEntityClass() {
		// use lazy initialization, obtain via reflection (a one time hit)
		if (this.entityClass == null) {
			ParameterizedType pType = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.entityClass = (Class<E>) pType.getActualTypeArguments()[0];
		}
		return this.entityClass;
	}

	public DetachedCriteria createDetachedCriteria() {
		return DetachedCriteria.forClass(getEntityClass());
	}

}