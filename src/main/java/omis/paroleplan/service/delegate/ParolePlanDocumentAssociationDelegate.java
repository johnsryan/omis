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
package omis.paroleplan.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.document.domain.Document;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleplan.dao.ParolePlanDocumentAssociationDao;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;

/**
 * Parole plan document association delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanDocumentAssociationDelegate {

	/* Data access objects. */
	
	private final ParolePlanDocumentAssociationDao 
			parolePlanDocumentAssociationDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParolePlanDocumentAssociation> 
			parolePlanDocumentAssociationInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole plan document association 
	 * delegate with the specified date access object and instance factory.
	 * 
	 * @param parolePlanDocumentAssociationDao parole plan document association 
	 * data access object
	 * @param parolePlanDocumentAssociationInstanceFactory parole plan document 
	 * association instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParolePlanDocumentAssociationDelegate(
			final ParolePlanDocumentAssociationDao 
					parolePlanDocumentAssociationDao,
			final InstanceFactory<ParolePlanDocumentAssociation> 
					parolePlanDocumentAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.parolePlanDocumentAssociationDao = 
				parolePlanDocumentAssociationDao;
		this.parolePlanDocumentAssociationInstanceFactory = 
				parolePlanDocumentAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new parole plan document association.
	 * 
	 * @param parolePlan parole plan
	 * @param document document
	 * @return parole plan document association
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParolePlanDocumentAssociation create(final ParolePlan parolePlan, 
			final Document document) throws DuplicateEntityFoundException {
		if (this.parolePlanDocumentAssociationDao.find(parolePlan, document) != 
				null) {
			throw new DuplicateEntityFoundException(
					"Parole plan document association already exists.");
		}
		ParolePlanDocumentAssociation parolePlanDocumentAssociation = this
				.parolePlanDocumentAssociationInstanceFactory.createInstance();
		parolePlanDocumentAssociation.setParolePlan(parolePlan);
		parolePlanDocumentAssociation.setDocument(document);
		parolePlanDocumentAssociation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		parolePlanDocumentAssociation.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.parolePlanDocumentAssociationDao.makePersistent(
				parolePlanDocumentAssociation);
	}

	/**
	 * Removes the specified parole plan document association.
	 * 
	 * @param parolePlanDocumentAssociation parole plan document association
	 */
	public void remove(
			final ParolePlanDocumentAssociation parolePlanDocumentAssociation) {
		this.parolePlanDocumentAssociationDao.makeTransient(
				parolePlanDocumentAssociation);
	}

	/**
	 * Returns a list of parole plan document associations for the specified 
	 * parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return list of parole plan document associations
	 */
	public List<ParolePlanDocumentAssociation> findByParolePlan(
			final ParolePlan parolePlan) {
		return this.parolePlanDocumentAssociationDao.findByParolePlan(
				parolePlan);
	}

}
