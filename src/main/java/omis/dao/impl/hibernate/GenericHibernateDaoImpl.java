/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.dao.impl.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.type.Type;

import omis.dao.GenericDao;

/**
 * Hibernate implementation of generic data access object.
 * 
 * <p>The entity to which data access is provided by the object can be
 * stated explicitly in the constructor for the entity name property or
 * can be inferred from the parameter {@code <T>}.
 * 
 * <p>Parts based on example in <i>Java Persistence with Hibernate, First
 * Edition</i>, Bauer <i>et al</i>.
 * 
 * @author Stephen Abson
 * @version 0.1.3 (July 12, 2018)
 * @since OMIS 3.0
 * @see GenericDao
 * @param <T> type of entity object
 */
public abstract class GenericHibernateDaoImpl<T extends Serializable>
		implements GenericDao<T> {
	
	private final SessionFactory sessionFactory;
	
	private final String entityName;
	
	/**
	 * Instantiates an Hibernate implementation of generic data access
	 * object with the specified session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	protected GenericHibernateDaoImpl(final SessionFactory sessionFactory,
			final String entityName) {
		this.sessionFactory = sessionFactory;
		this.entityName = entityName;
	}
	
	/**
	 * Get the session factory.
	 * 
	 * @return session factory
	 */
	protected final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	// Returns the persisted class
	private Class<T> getPersistentClass() {
		@SuppressWarnings("unchecked")
		Class<T> persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		return persistentClass;
	}
	
	/**
	 * Returns the entity name.
	 * 
	 * <p>If the entity name is not set, returns the canonical name of the
	 * persistent entity type.
	 * 
	 * @return entity name if set; otherwise canonical name of persistent
	 * entity type
	 */
	protected final String getEntityName() {
		if (this.entityName != null) {
			return this.entityName;
		} else {
			return this.getPersistentClass().getCanonicalName();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public T findById(final Long id, final boolean lock) {
		Session session = this.getSessionFactory().getCurrentSession();
		if (lock) {
			@SuppressWarnings("unchecked")
			T entity = (T) session.load(this.getPersistentClass(), id,
						LockOptions.UPGRADE);
			return entity;
		} else {
			@SuppressWarnings("unchecked")
			T entity = (T) session.load(this.getPersistentClass(), id);
			return entity;
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<T> findAll() {
		return findByCriteria();
	}
	
	/**
	 * Finds by criteria.
	 * 
	 * @param criterion criterion by which to find
	 * @return entities matching criteria
	 */
	protected List<T> findByCriteria(final Criterion... criterion) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(this.getEntityName());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) crit.list();
		return result;
	}
	
	/** {@inheritDoc} */
	@Override
	public T makePersistent(final T entity) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(this.getEntityName(), entity);
		return entity;
	}
	
	/** {@inheritDoc} */
	@Override
	public void makeTransient(final T entity) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(this.getEntityName(), entity);
	}
	
	/**
	 * Returns type of property.
	 * 
	 * @param propertyName name of property
	 * @return type of property
	 */
	protected Type getEntityPropertyType(final String propertyName) {
		return this.getSessionFactory().getClassMetadata(
				this.getEntityName()).getPropertyType(propertyName);
	}
}