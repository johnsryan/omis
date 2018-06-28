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
 * Registry of operations to perform on associated entities.
 * 
 * @param T type of entity associates of which to operate
 * @author Stephen Abson
 * @version 0.0.1 (Jun 19, 2018)
 * @since OMIS 3.0
 */
public interface AssociatedEntityOperationRegistry
		<T extends Serializable> {

	/**
	 * Registers operation.
	 * 
	 * @param operation operation
	 * @throws NullPointerException if operation is {@code null}
	 */
	void register(AssociatedEntityOperation<T> operation);
	
	/**
	 * Returns whether any associated entities exists on which any operation can
	 * be applied.
	 * 
	 * @param entity entity any associated entity of which to check
	 * @return whether any associated entities exist on which any operations can
	 * be applied
	 */
	boolean testAny(T entity);
	
	/**
	 * Applies all registered operations to associated entities.
	 * 
	 * @param entity entity all associated entities of which to apply operation
	 * @return count of associated entities on which operation was applied
	 */
	int applyAll(T entity);
}