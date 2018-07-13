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
package omis.locationterm.service.delegate;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.locationterm.dao.LocationTermChangeActionAssociationDao;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.domain.LocationTermChangeActionAssociation;
import omis.locationterm.exception.LocationTermChangeActionAssociationExistsException;

/**
 * Delegate for association of location term to change action.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2018)
 * @since OMIS 3.0
 */
public class LocationTermChangeActionAssociationDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<LocationTermChangeActionAssociation>
	locationTermChangeActionAssociationInstanceFactory;
	
	/* Data access objects. */
	
	private final LocationTermChangeActionAssociationDao
	locationTermChangeActionAssociationDao;
	
	/* Audit component retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for association of location term to change action.
	 * 
	 * @param locationTermChangeActionAssociationInstanceFactory instance
	 * factory for association of location term to change action
	 * @param locationTermChangeActionAssociationDao data access object for
	 * association of location term to change action
	 * @param auditComponentRetriever audit component retriever
	 */
	public LocationTermChangeActionAssociationDelegate(
			final InstanceFactory<LocationTermChangeActionAssociation>
			locationTermChangeActionAssociationInstanceFactory,
			final LocationTermChangeActionAssociationDao
			locationTermChangeActionAssociationDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.locationTermChangeActionAssociationInstanceFactory
			= locationTermChangeActionAssociationInstanceFactory;
		this.locationTermChangeActionAssociationDao
			= locationTermChangeActionAssociationDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */
	
	/**
	 * Associates location term and change action.
	 * 
	 * @param locationTerm location term
	 * @param changeAction change action
	 * @return association of location term and change action
	 * @throws LocationTermChangeActionAssociationExistsException if location
	 * term and change action are associated
	 */
	public LocationTermChangeActionAssociation create(
				final LocationTerm locationTerm,
				final LocationTermChangeAction changeAction)
			throws LocationTermChangeActionAssociationExistsException {
		if (this.locationTermChangeActionAssociationDao.find(
				locationTerm) != null) {
			throw new LocationTermChangeActionAssociationExistsException(
					"Association of location term and change action exists");
		}
		LocationTermChangeActionAssociation association
			= this.locationTermChangeActionAssociationInstanceFactory
				.createInstance();
		association.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		association.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		association.setLocationTerm(locationTerm);
		association.setChangeAction(changeAction);
		return this.locationTermChangeActionAssociationDao
				.makePersistent(association);
	}
	
	/**
	 * Returns association of location term to change action.
	 * 
	 * @param locationTerm location term
	 * @return association of location term to change action
	 */
	public LocationTermChangeActionAssociation findByLocationTerm(
			final LocationTerm locationTerm) {
		return this.locationTermChangeActionAssociationDao
				.find(locationTerm);
	}
	
	/**
	 * Removes associations with change action by location term.
	 * 
	 * @param locationTerm location term by which to remove change action
	 * associations
	 * @return whether associations were removed
	 */
	public boolean removeByLocationTerm(
			final LocationTerm locationTerm) {
		return this.locationTermChangeActionAssociationDao
				.removeByLocationTerm(locationTerm) > 0;
	}
}