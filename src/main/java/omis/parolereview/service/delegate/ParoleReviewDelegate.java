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
package omis.parolereview.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.parolereview.dao.ParoleReviewDao;
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Parole review delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDelegate {

	/* Data access objects. */
	
	private final ParoleReviewDao paroleReviewDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParoleReview> 
			paroleReviewInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole review delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param paroleReviewDao parole review data access object
	 * @param paroleReviewInstanceFactory parole review instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParoleReviewDelegate(
			final ParoleReviewDao paroleReviewDao,
			final InstanceFactory<ParoleReview> 
					paroleReviewInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleReviewDao = paroleReviewDao;
		this.paroleReviewInstanceFactory = paroleReviewInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/**
	 * Creates a new parole review with the specified parameters.
	 * 
	 * @param staffAssignment staff assignment
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @param endorsement parole endorsement category
	 * @param staffRole staff role category
	 * @return parole review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleReview create(final StaffAssignment staffAssignment, 
			final Date date, final String text, final Offender offender,
			final ParoleEndorsementCategory endorsement,
			final StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException {
		if (this.paroleReviewDao.find(staffAssignment, offender, staffRole) != 
				null) {
			throw new DuplicateEntityFoundException(
					"Parole review already exists.");
		}
		ParoleReview paroleReview = this
				.paroleReviewInstanceFactory.createInstance();
		populateParoleReview(paroleReview, staffAssignment, date, text, 
				offender, endorsement, staffRole);
		paroleReview.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.paroleReviewDao.makePersistent(paroleReview);
	}

	/**
	 * Updates an existing parole review with the specified parameters.
	 * 
	 * @param paroleReview parole review
	 * @param staffAssignment staff assignment
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @param endorsement parole endorsement category
	 * @param staffRole staff role category
	 * @return parole review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleReview update(final ParoleReview paroleReview, 
			final StaffAssignment staffAssignment, final Date date,
			final String text, final Offender offender,
			final ParoleEndorsementCategory endorsement,
			final StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException {
		if (this.paroleReviewDao.findExcluding(staffAssignment, offender, 
				staffRole, paroleReview) != null) {
			throw new DuplicateEntityFoundException(
					"Parole review already exists.");
		}
		populateParoleReview(paroleReview, staffAssignment, date, text, 
				offender, endorsement, staffRole);
		return this.paroleReviewDao.makePersistent(paroleReview);
	}
	
	/**
	 * Removes the specified parole review.
	 * 
	 * @param paroleReview parole review
	 */
	public void remove(final ParoleReview paroleReview) {
		this.paroleReviewDao.makeTransient(paroleReview);
	}
	
	// Populates a parole review
	private void populateParoleReview(
			final ParoleReview paroleReview,
			final StaffAssignment staffAssignment, final Date date, 
			final String text, final Offender offender,
			final ParoleEndorsementCategory endorsement,
			final StaffRoleCategory staffRole) {
		paroleReview.setStaffAssignment(staffAssignment);
		paroleReview.setDate(date);
		paroleReview.setText(text);
		paroleReview.setOffender(offender);
		paroleReview.setEndorsement(endorsement);
		paroleReview.setStaffRole(staffRole);
		paroleReview.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}