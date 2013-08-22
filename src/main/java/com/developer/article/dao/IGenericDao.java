package com.developer.article.dao;

import java.io.Serializable;
import java.util.List;

/**
* Generic DAO for Hibernate 
*/
public interface IGenericDao<E,PK  extends Serializable> {
    PK save(E newInstance);
    void update(E transientObject);
    void saveOrUpdate(E transientObject);
    void delete(E persistentObject);
    void deleteByPrimaryKey(PK primaryKey);
    void deleteAll();
    E findByPrimaryKey(PK primaryKey);
    List<E> findAll();
}