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
package omis.instance.factory.delegate.hibernate;

import java.io.Serializable;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

/**
 * Delegate to instantiate entities using a Hibernate configuration. 
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jun 25, 2013)
 * @since OMIS 3.0
 */
public final class HibernateInstanceFactoryDelegate {
	
	private final Configuration configuration;
	
	/**
	 * Instantiates a implementation of delegate to instantiate entities using
	 * a Hibernate configuration.
	 * 
	 * @param configuration Hibernate configuration
	 */
	public HibernateInstanceFactoryDelegate(final Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Returns whether an entity with the given name can be identified in
	 * the configuration.
	 * 
	 * @param entityName entity name
	 * @return whether entity with given name can be identified in configuration
	 */
	public boolean entityExists(final String entityName) {
		return this.configuration.getClassMapping(entityName) != null;
	}
	
	/**
	 * Returns whether the class is assignable from the actual type of the
	 * named entity.
	 * 
	 * @param clazz class
	 * @param entityName name of entity
	 * @return whether class is assignable from actual type of named entity
	 */
	public boolean entityAssignableFrom(
			final Class<? extends Serializable> clazz,
			final String entityName) {
		PersistentClass entityClassMapping
			= this.configuration.getClassMapping(entityName);
		if (entityClassMapping == null) {
			throw new IllegalArgumentException(String.format(
				"Entity with name \'%s\' cannot be found in configuration",
				entityName));
		}
		Class<?> entityClass = entityClassMapping.getMappedClass();
		return clazz.isAssignableFrom(entityClass);
	}
	
	/**
	 * Instantiates and returns an instance of entity type.
	 * 
	 * @param entityName name of entity to instantiate
	 * @param clazz type of entity to instantiate
	 * @return instance of entity type
	 * @param <T> type to instantiate
	 */
	public <T extends Serializable> T instantiate(final String entityName,
			final Class<? extends T> clazz) {
		final String className = this.configuration.getClassMapping(entityName)
				.getClassName();
		try {
			@SuppressWarnings("unchecked")
			Class<? extends T> implClazz = (Class<? extends T>)
				Class.forName(className);
			return implClazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Illegal class name: " + className, e);
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Could not instantiate entity named: " + entityName, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Illegal access while instantiating entity named: "
							+ entityName, e);
		}
	}
	
	/**
	 * Instantiates and returns an instance of the entity with the canonical
	 * name of the specified entity type.
	 * 
	 * @param clazz type of entity to instantiate
	 * @return instance of entity type
	 * @param <T> type to instantiate
	 */
	public <T extends Serializable> T instantiate(final Class<T> clazz) {
		return this.instantiate(clazz.getCanonicalName(), clazz);
	}
	
	/**
	 * Instantiates and returns an instance of the entity with the specified
	 * name.
	 * 
	 * @param entityName name of entity a new instance of which to return
	 * @return new instance of entity with specified name
	 */
	public Serializable instantiate(final String entityName) {
		return this.instantiate(entityName, Serializable.class);
	}
}