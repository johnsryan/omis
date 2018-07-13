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
package omis.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic data access object.
 * 
 * <p>Parts based on example in <i>Java Persistence with Hibernate, First
 * Edition</i>, Bauer <i>et al</i>.
 * 
 * @author Stephen Abson
 * @version 0.1.2 (July 12, 2018)
 * @since OMIS 3.0
 *
 * @param <T> type of entity object
 */
public interface GenericDao<T extends Serializable> {

	/**
	 * Returns an entities by ID.
	 * 
	 * @param id ID of entity to be found
	 * @param lock whether or the selected record should be locked using
	 * pessimistic locking
	 * @return entity with specified ID
	 */
	T findById(Long id, boolean lock);
	
	/**
	 * Returns all entities.
	 * 
	 * <p>This method maybe overridden to exclude invalid entities and
	 * specify sort order.
	 * 
	 * <p>This should also throw {@code UnsupportedOperationException} if an
	 * unmanagable number of entities would be returned.
	 * 
	 * @return all persisted entities
	 */
	List<T> findAll();
	
	/**
	 * Makes an entity persistent.
	 * 
	 * @param entity entity to make persistent
	 * @return persisted entity
	 */
	T makePersistent(T entity);

	/**
	 * Makes an entity transient.
	 * 
	 * @param entity entity to make transient
	 */
	void makeTransient(T entity);
}