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
package omis.presentenceinvestigation.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.docket.domain.Docket;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.presentenceinvestigation.dao.PresentenceInvestigationRequestDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.user.domain.UserAccount;

/** 
 * Service delegate for presentence investigation request related operations.
 * 
 * @author Ryan Johns
 * @author Annie Jacques
 * @author Josh Divine
 * @version 0.1.3 (May 15, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationRequestDelegate {
	private static final String DUPLICATE_ENTITY_FOUND_MSG 
		= "Presentence investigation request exists for this court case.";
	
	private final PresentenceInvestigationRequestDao 
		presentenceInvestigationRequestDao;
	
	private final InstanceFactory<PresentenceInvestigationRequest> 
		presentenceInvestigationRequestInstanceFactory;
	
	private final AuditComponentRetriever auditComponenetRetriever;
	
	
	/** Constructor. 
	 * @param presentenceInvestigationRequestDao - presentence investigation
	 * request dao. 
	 * @param presentenceInvestigationRequestInstanceFactory - presentence
	 * investigation request instance factory. 
	 * @param auditComponentRetriever - audit component retriever. */
	public PresentenceInvestigationRequestDelegate(
			final PresentenceInvestigationRequestDao 
				presentenceInvestigationRequestDao,
			final InstanceFactory<PresentenceInvestigationRequest>
				presentenceInvestigationRequestInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.presentenceInvestigationRequestDao 
			= presentenceInvestigationRequestDao;
		this.presentenceInvestigationRequestInstanceFactory 
			= presentenceInvestigationRequestInstanceFactory;
		this.auditComponenetRetriever = auditComponentRetriever;
	}
	
	/** Creates a presentence investigation request.
	 * @param assignedUser - assigned user.
	 * @param requestDate - request date.
	 * @param expectedCompletionDate - expected completion date.
	 * @param docket - docket.
	 * @param sentenceDate - sentence date.
	 * @param category - PresentenceInvestigationCategory
	 * @param submissionDate - submission date.
	 * @return Presentence investigation request.
	 * @throws DuplicateEntityFoundException - entity with given court case 
	 * exists */
	public PresentenceInvestigationRequest create(
			final UserAccount assignedUser,
			final Date requestDate, final  Date expectedCompletionDate, 
			final Docket docket, final Date sentenceDate, 
			final PresentenceInvestigationCategory category, 
			final Date submissionDate) 
					throws DuplicateEntityFoundException {
		if (this.presentenceInvestigationRequestDao.find(docket) != null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		PresentenceInvestigationRequest presentenceInvestigationRequest = this
				.presentenceInvestigationRequestInstanceFactory
				.createInstance();
		presentenceInvestigationRequest.setCreationSignature(
				new CreationSignature(
						this.auditComponenetRetriever.retrieveUserAccount(), 
						this.auditComponenetRetriever.retrieveDate()));
		populatePresentenceInvestigationRequest(presentenceInvestigationRequest, 
				assignedUser, requestDate, expectedCompletionDate, docket, 
				sentenceDate, category, submissionDate);
		return this.presentenceInvestigationRequestDao.makePersistent(
				presentenceInvestigationRequest);
	}
	
	/** Updates an existing presentence investigation request.\
	 * @param presentenceInvestigationRequest - presentence investigation 
	 * request.
	 * @param assignedUser - assigned user.
	 * @param requestDate - request date.
	 * @param expectedCompletionDate - expected completion date.
	 * @param completionDate - completion date.
	 * @param docket - docket. 
	 * @param sentenceDate - sentence date.
	 * @param category - PresentenceInvestigationCategory
	 * @param submissionDate - submission date.
	 * @return presentence investigation request.
	 * @throws DuplicateEntityFoundException - when presentence investigation
	 * request exists for given court case. */
	public PresentenceInvestigationRequest update(
			final PresentenceInvestigationRequest 
				presentenceInvestigationRequest,
			final UserAccount assignedUser, final Date requestDate,
			final Date expectedCompletionDate, final Docket docket,
			final Date sentenceDate, 
			final PresentenceInvestigationCategory category, 
			final Date submissionDate)
	throws DuplicateEntityFoundException {
		
		if (this.presentenceInvestigationRequestDao.findExcluding(
				presentenceInvestigationRequest, docket) != null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		populatePresentenceInvestigationRequest(presentenceInvestigationRequest, 
				assignedUser, requestDate, expectedCompletionDate, docket, 
				sentenceDate, category, submissionDate);
		return this.presentenceInvestigationRequestDao.makePersistent(
				presentenceInvestigationRequest);
	}

	/** Removes a presentence investigation request.
	 * @param presentenceInvestigationRequest - presentence investigation 
	 * request. */
	public void remove(final PresentenceInvestigationRequest 
			presentenceInvestigationRequest) {
		this.presentenceInvestigationRequestDao.makeTransient(
				presentenceInvestigationRequest);
	}
	
	// Populates a presentence investigation request
	private void populatePresentenceInvestigationRequest(
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest, 
			final UserAccount assignedUser, final Date requestDate, 
			final Date expectedCompletionDate, final Docket docket, 
			final Date sentenceDate, 
			final PresentenceInvestigationCategory category,
			final Date submissionDate) {
		presentenceInvestigationRequest.setDocket(docket);
		presentenceInvestigationRequest.setExpectedCompletionDate(
				expectedCompletionDate);
		presentenceInvestigationRequest.setRequestDate(requestDate);
		presentenceInvestigationRequest.setAssignedUser(assignedUser);
		presentenceInvestigationRequest.setSentenceDate(sentenceDate);
		presentenceInvestigationRequest.setCategory(category);
		presentenceInvestigationRequest.setSubmissionDate(submissionDate);
		presentenceInvestigationRequest.setUpdateSignature(
				new UpdateSignature(
						this.auditComponenetRetriever.retrieveUserAccount(),
						this.auditComponenetRetriever.retrieveDate()));
	}
}