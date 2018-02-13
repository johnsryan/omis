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
package omis.medicalreview.service.delegate;

import java.util.Date;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.medicalreview.dao.MedicalReviewDao;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.offender.domain.Offender;

/**
 * Medical Review Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Medical Review already exists for the given Offender with the "
			+ "specified Date and Health Classification.";
	
	private final MedicalReviewDao medicalReviewDao;
	
	private final InstanceFactory<MedicalReview> 
		medicalReviewInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for MedicalReviewDelegate.
	 * @param medicalReviewDao - Medical Review DAO
	 * @param medicalReviewInstanceFactory - Medical Review Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public MedicalReviewDelegate(
			final MedicalReviewDao medicalReviewDao,
			final InstanceFactory<MedicalReview> 
				medicalReviewInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.medicalReviewDao = medicalReviewDao;
		this.medicalReviewInstanceFactory = medicalReviewInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Medical Review with the given properties.
	 * 
	 * @param offender - Offender
	 * @param date - Date
	 * @param text - String text
	 * @param healthClassification - Medical Health Classification
	 * @return Newly created Medical Review
	 * @throws DuplicateEntityFoundException - When a Medical Review already
	 * exists with the given Date and Health Classification for the specified
	 * Offender.
	 */
	public MedicalReview create(final Offender offender,
			final Date date, final String text,
			final MedicalHealthClassification healthClassification)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewDao.find(
				offender, date, healthClassification) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		MedicalReview medicalReview = 
				this.medicalReviewInstanceFactory.createInstance();
		
		medicalReview.setDate(date);
		medicalReview.setHealthClassification(healthClassification);
		medicalReview.setOffender(offender);
		medicalReview.setText(text);
		medicalReview.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		medicalReview.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewDao.makePersistent(medicalReview);
	}
	
	/**
	 * Update the specified Medical Review with the given properties.
	 * 
	 * @param medicalReview - Medical Review to update
	 * @param offender - Offender
	 * @param date - Date
	 * @param text - String text
	 * @param healthClassification - Medical Health Classification
	 * @return Updated Medical Review
	 * @throws DuplicateEntityFoundException - When a Medical Review already
	 * exists with the given Date and Health Classification for the specified
	 * Offender.
	 */
	public MedicalReview update(final MedicalReview medicalReview,
			final Offender offender, final Date date, final String text,
			final MedicalHealthClassification healthClassification)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewDao.findExcluding(offender, date,
				healthClassification, medicalReview) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		medicalReview.setDate(date);
		medicalReview.setHealthClassification(healthClassification);
		medicalReview.setOffender(offender);
		medicalReview.setText(text);
		medicalReview.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewDao.makePersistent(medicalReview);
	}
	
	/**
	 * Removes the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review to remove
	 */
	public void remove(final MedicalReview medicalReview) {
		this.medicalReviewDao.makeTransient(medicalReview);
	}
}
