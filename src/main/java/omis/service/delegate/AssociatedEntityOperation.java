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
package omis.service.delegate;

import java.io.Serializable;

/**
 * Operate on which entities associated with another entity can be applied.
 * 
 * <p>Operation may only go one entity association deep - in other words,
 * operation must not be applied to entities associated with the entities
 * associated with the entity.
 * 
 * <p>Typically, entities associated with multiple entities will have multiple
 * operations - one for each associated entity. 
 * 
 * @param T type of entity associates of which to operate
 * @author Stephen Abson
 * @version 0.0.1 (Jun 18, 2018)
 * @since OMIS 3.0
 */
public interface AssociatedEntityOperation<T extends Serializable> {
	
	/**
	 * Returns whether association exists on which operation can be applied.
	 * 
	 * @param entity entity associated entities of which to check
	 * @return whether associated entities exist on which operation can be
	 * applied
	 */
	boolean test(T entity);
	
	/**
	 * Applies operation.
	 * 
	 * @param entity entity associated entities of which to remove
	 * @return count of removed associated entities
	 */
	int apply(T entity);
}