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
package omis.service.delegate.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import omis.service.delegate.AssociatedEntityOperation;
import omis.service.delegate.AssociatedEntityOperationRegistry;

/**
 * Abstract implementation of registry of operations to perform on associated
 * entities.
 * 
 * @param T type of entity associates of which to operate
 * @author Stephen Abson
 * @version 0.0.1 (Jun 19, 2018)
 * @since OMIS 3.0
 */
public class AbstractAssociatedEntityOperationRegistry
		<T extends Serializable>
		implements AssociatedEntityOperationRegistry<T> {
	
	private final List<AssociatedEntityOperation<T>> operations
		= new ArrayList<AssociatedEntityOperation<T>>();

	/**
	 * Returns unmodifiable operations.
	 * 
	 * @return unmodifiable operations
	 */
	protected List<AssociatedEntityOperation<T>> getOperations() {
		return Collections.unmodifiableList(this.operations);
	}

	/** {@inheritDoc} */
	@Override
	public void register(
			final AssociatedEntityOperation<T> operation) {
		if (operation == null) {
			throw new NullPointerException();
		}
		this.operations.add(operation);
	}

	/** {@inheritDoc} */
	@Override
	public boolean testAny(final T entity) {
		for (AssociatedEntityOperation<T> operation : this.operations) {
			if (operation.test(entity)) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int applyAll(final T entity) {
		int totalCount = 0;
		for (AssociatedEntityOperation<T> operation : this.operations) {
			if (operation.test(entity)) {
				totalCount = totalCount + operation.apply(entity);
			}
		}
		return totalCount;
	}
}