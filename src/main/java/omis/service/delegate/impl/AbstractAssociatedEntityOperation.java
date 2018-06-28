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

import omis.service.delegate.AssociatedEntityOperation;
import omis.service.delegate.AssociatedEntityOperationRegistry;

/**
 * Abstract implementation of associated entity operation.
 * 
 * <p>Implementation registers itself on instantiation. Call:
 * <pre>
 *   super(registry);
 * </pre>
 * in subclass constructors.
 * 
 * @param T type of entity associates of which to operate
 * @author Stephen Abson
 * @version 0.0.1 (Jun 27, 2018)
 * @since OMIS 3.0
 */
public abstract class AbstractAssociatedEntityOperation<T extends Serializable>
		implements AssociatedEntityOperation<T> {

	/**
	 * Instantiates associated entity operation. Registers itself in registry.
	 * 
	 * @param registry registry in which to register {@code this}.
	 */
	public AbstractAssociatedEntityOperation(
			final AssociatedEntityOperationRegistry<T> registry) {
		registry.register(this);
	}
}