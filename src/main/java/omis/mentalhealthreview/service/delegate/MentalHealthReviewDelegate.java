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
package omis.mentalhealthreview.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.mentalhealthreview.dao.MentalHealthReviewDao;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.offender.domain.Offender;

/**
 * Mental health review delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewDelegate {

	/* Data access objects. */
	
	private final MentalHealthReviewDao mentalHealthReviewDao;

	/* Instance factories. */
	
	private final InstanceFactory<MentalHealthReview> 
			mentalHealthReviewInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of mental health review delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param mentalHealthReviewDao mental health review data access object
	 * @param mentalHealthReviewInstanceFactory mental health review instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public MentalHealthReviewDelegate(
			final MentalHealthReviewDao mentalHealthReviewDao,
			final InstanceFactory<MentalHealthReview> 
					mentalHealthReviewInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.mentalHealthReviewDao = mentalHealthReviewDao;
		this.mentalHealthReviewInstanceFactory = 
				mentalHealthReviewInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/**
	 * Creates a new mental health review with the specified parameters.
	 * 
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @return mental health review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public MentalHealthReview create(final Date date, final String text, 
			final Offender offender) throws DuplicateEntityFoundException {
		if (this.mentalHealthReviewDao.find(date, text, offender) != null) {
			throw new DuplicateEntityFoundException(
					"Mental health review already exists.");
		}
		MentalHealthReview mentalHealthReview = this
				.mentalHealthReviewInstanceFactory.createInstance();
		populateMentalHealthReview(mentalHealthReview, date, text, offender);
		mentalHealthReview.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.mentalHealthReviewDao.makePersistent(mentalHealthReview);
	}

	/**
	 * Updates an existing mental health review with the specified parameters.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @return mental health review
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public MentalHealthReview update(
			final MentalHealthReview mentalHealthReview, final Date date, 
			final String text, final Offender offender) 
					throws DuplicateEntityFoundException {
		if (this.mentalHealthReviewDao.findExcluding(date, text, offender, 
				mentalHealthReview) != null) {
			throw new DuplicateEntityFoundException(
					"Mental health review already exists.");
		}
		populateMentalHealthReview(mentalHealthReview, date, text, offender);
		return this.mentalHealthReviewDao.makePersistent(mentalHealthReview);
	}
	
	/**
	 * Removes the specified mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 */
	public void remove(final MentalHealthReview mentalHealthReview) {
		this.mentalHealthReviewDao.makeTransient(mentalHealthReview);
	}
	
	// Populates a mental health review
	private void populateMentalHealthReview(
			final MentalHealthReview mentalHealthReview,
			final Date date, final String text, final Offender offender) {
		mentalHealthReview.setDate(date);
		mentalHealthReview.setText(text);
		mentalHealthReview.setOffender(offender);
		mentalHealthReview.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}