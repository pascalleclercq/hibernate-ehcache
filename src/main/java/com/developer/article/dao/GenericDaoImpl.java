package com.developer.article.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericDaoImpl<E, PK extends Serializable> implements
		IGenericDao<E, PK> {

	private static final Logger LOG = LoggerFactory
			.getLogger(GenericDaoImpl.class);

	private Class<E> entityClass = null;

	private SessionFactory sf;

	@Autowired
	public void setDataSource(SessionFactory sf) {
		this.sf = sf;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public PK save(E newInstance) {
		return (PK) sf.getCurrentSession().save(newInstance);
	}

	public E findByPrimaryKey(PK primaryKey) {
		return (E) sf.getCurrentSession().get(getEntityClass(), primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		
		Session session = sf.getCurrentSession();
		Criteria executableCriteria = createDetachedCriteria().getExecutableCriteria(session);
		return executableCriteria.list();

	}

	@SuppressWarnings("unchecked")
	protected List<E> findAllByProperty(String propertyName, Object value) {
		DetachedCriteria criteria = createDetachedCriteria().add(
				Restrictions.eq(propertyName, value));
		Session session = sf.getCurrentSession();
		Criteria executableCriteria = criteria.getExecutableCriteria(session);
		return executableCriteria.list();
	}

	protected E findByUniqueProperty(String propertyName, Object value) {
		List<E> all = findAllByProperty(propertyName, value);
		if (all.size() == 0) {
			return null;
		} else {
			return all.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<E> findByCriteria(DetachedCriteria criteria) {

		Session session = sf.getCurrentSession();
		Criteria executableCriteria = criteria.getExecutableCriteria(session);
		return executableCriteria.list();
	}

	@SuppressWarnings("unchecked")
	protected List<E> find(String queryString, Object... values) {
		Query query = sf.getCurrentSession().createQuery(queryString);
		// return
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}

	public int bulkUpdate(String queryString, Object... values) {
		// return sf.getCurrentSession().bulkUpdate(queryString, values);

		Query queryObject = sf.getCurrentSession().createQuery(queryString);
		// prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.executeUpdate();
	}

	@Transactional
	public void update(E transientObject) {
		sf.getCurrentSession().update(transientObject);
	}

	@Transactional
	public void saveOrUpdate(E transientObject) {
		sf.getCurrentSession().saveOrUpdate(transientObject);
	}

	@Transactional()
	public void delete(E persistentObject) {
		if (persistentObject != null) {
			sf.getCurrentSession().delete(persistentObject);
		} else {
			LOG.warn(getClass().getSimpleName()
					+ ": attempt to delete NULL object");
		}
	}

	@Transactional
	public void deleteByPrimaryKey(PK primaryKey) {
		E entity = this.findByPrimaryKey(primaryKey);
		this.delete(entity);
	}

	@Transactional
	public void deleteAll() {
		sf.getCurrentSession()
				.createQuery("DELETE FROM " + getEntityClass().getName())
				.executeUpdate();
	}

	/**
	 * Locks an object for updating
	 * 
	 * @param entity
	 *            Entity
	 */
	@Transactional
	public void lock(E entity) {
		sf.getCurrentSession().lock(entity, LockMode.PESSIMISTIC_WRITE);
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