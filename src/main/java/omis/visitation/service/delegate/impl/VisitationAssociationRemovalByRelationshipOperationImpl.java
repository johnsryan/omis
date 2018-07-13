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
package omis.visitation.service.delegate.impl;

import omis.relationship.domain.Relationship;
import omis.service.delegate.AssociatedEntityOperationRegistry;
import omis.service.delegate.impl.AbstractAssociatedEntityOperation;
import omis.visitation.dao.VisitationAssociationDao;

/**
 * Operation to remove visitation association by relationship.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 2, 2018)
 * @since OMIS 3.0
 */
public class VisitationAssociationRemovalByRelationshipOperationImpl
		extends AbstractAssociatedEntityOperation<Relationship> {
	
	private final VisitationAssociationDao visitationAssociationDao;

	/**
	 * Instantiates operation to remove visitation association by relationship.
	 * 
	 * @param registry registry
	 * @param visitationAssociationDao data access object for visitation
	 * association
	 */
	public VisitationAssociationRemovalByRelationshipOperationImpl(
			final AssociatedEntityOperationRegistry<Relationship> registry,
			final VisitationAssociationDao visitationAssociationDao) {
		super(registry);
		this.visitationAssociationDao = visitationAssociationDao;
	}

	/** {@inheritDoc} */
	@Override
	public boolean test(final Relationship entity) {
		return this.visitationAssociationDao.countByRelationship(entity) > 0;
	}

	/** {@inheritDoc} */
	@Override
	public int apply(final Relationship entity) {
		return this.visitationAssociationDao.removeByRelationship(entity);
	}
}