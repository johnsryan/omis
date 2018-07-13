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
package omis.instance.factory.hibernate.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import omis.instance.factory.InstanceFactory;
import omis.instance.factory.delegate.hibernate.HibernateInstanceFactoryDelegate;

/**
 * Hibernate implementation of instance factory.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jul 8, 2013)
 * @since OMIS 3.0
 */
public class InstanceFactoryHibernateImpl<T extends Serializable>
		implements InstanceFactory<T> {

	private final HibernateInstanceFactoryDelegate instanceFactoryDelegate;
	
	private final String entityName;
	
	/**
	 * Instantiates with the specified instance factory delegate and entity
	 * name.
	 * 
	 * @param instanceFactoryDelegate instance factory delegate
	 * @param entityName entity name
	 * @throws IllegalArgumentException if entity with name cannot be
	 * found in configuration or the actual type of the entity is not or
	 * does not extend type {@code T}
	 */
	public InstanceFactoryHibernateImpl(
			final HibernateInstanceFactoryDelegate instanceFactoryDelegate,
			final String entityName) {
		if (!instanceFactoryDelegate.entityExists(entityName)) {
			throw new IllegalArgumentException(String.format(
					"Entity with name \'%s\' cannot be found in configuration",
					entityName));
		}
		Class<T> entityReferenceType = this.getEntityReferenceType();
		if (!instanceFactoryDelegate.entityAssignableFrom(
				entityReferenceType, entityName)) {
			throw new IllegalArgumentException(String.format(
					"Cannot assign \'%s\' to \'%s\'",
					entityReferenceType.getCanonicalName(),
					entityName));
		}
		this.instanceFactoryDelegate = instanceFactoryDelegate;
		this.entityName = entityName;
	}
	
	/** {@inheritDoc} */
	@Override
	public T createInstance() {
		@SuppressWarnings("unchecked")
		T entity = (T) this.instanceFactoryDelegate.instantiate(entityName);
		return entity;
	}
	
	// Returns the persisted class
	private Class<T> getEntityReferenceType() {
		if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
			@SuppressWarnings("unchecked")
			Class<T> entityReferenceType
				= (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
			return entityReferenceType;
		} else {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) Serializable.class;
			return clazz;
		}
	}
}