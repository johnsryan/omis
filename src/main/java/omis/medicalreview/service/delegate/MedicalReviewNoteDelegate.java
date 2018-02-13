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
import java.util.List;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.medicalreview.dao.MedicalReviewNoteDao;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewNote;

/**
 * Medical Review Note Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewNoteDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A Medical Review Note already exists with the given Date and "
			+ "Description for the specified Medical Review.";
	
	private final MedicalReviewNoteDao medicalReviewNoteDao;
	
	private final InstanceFactory<MedicalReviewNote> 
		medicalReviewNoteInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for MedicalReviewNoteDelegate.
	 * @param medicalReviewNoteDao - Medical Review Note DAO
	 * @param medicalReviewNoteInstanceFactory - Medical Review Note Instance
	 * Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public MedicalReviewNoteDelegate(
			final MedicalReviewNoteDao medicalReviewNoteDao,
			final InstanceFactory<MedicalReviewNote> 
				medicalReviewNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.medicalReviewNoteDao = medicalReviewNoteDao;
		this.medicalReviewNoteInstanceFactory =
				medicalReviewNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Medical Review Note with the given properties.
	 * 
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @return newly created Medical Review Note
	 * @throws DuplicateEntityFoundException - When a Medical Review Note
	 * already exists with the given Date and Description for the
	 * specified Medical Review.
	 */
	public MedicalReviewNote create(final MedicalReview medicalReview,
			final String description, final Date date)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewNoteDao.find(
				medicalReview, description, date) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		MedicalReviewNote medicalReviewNote = 
				this.medicalReviewNoteInstanceFactory.createInstance();
		
		medicalReviewNote.setDate(date);
		medicalReviewNote.setDescription(description);
		medicalReviewNote.setMedicalReview(medicalReview);
		medicalReviewNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		medicalReviewNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewNoteDao.makePersistent(medicalReviewNote);
	}
	
	/**
	 * Updates the specified Medical Review Note with the given properties.
	 * 
	 * @param medicalReviewNote - Medical Review Note to update
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @return Updated Medical Review Note
	 * @throws DuplicateEntityFoundException - When a Medical Review Note
	 * already exists with the given Date and Description for the
	 * specified Medical Review.
	 */
	public MedicalReviewNote update(final MedicalReviewNote medicalReviewNote,
			final MedicalReview medicalReview, final String description,
			final Date date)
				throws DuplicateEntityFoundException {
		if (this.medicalReviewNoteDao.findExcluding(medicalReview, description,
				date, medicalReviewNote) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		medicalReviewNote.setDate(date);
		medicalReviewNote.setDescription(description);
		medicalReviewNote.setMedicalReview(medicalReview);
		medicalReviewNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.medicalReviewNoteDao.makePersistent(medicalReviewNote);
	}
	
	/**
	 * Removes the specified Medical Review Note.
	 * 
	 * @param medicalReviewNote - Medical Review Note to remove
	 */
	public void remove(final MedicalReviewNote medicalReviewNote) {
		this.medicalReviewNoteDao.makeTransient(medicalReviewNote);
	}
	
	/**
	 * Returns a list of Medical Review Notes for the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Notes for the specified Medical Review.
	 */
	public List<MedicalReviewNote> findByMedicalReview(
			final MedicalReview medicalReview) {
		return this.medicalReviewNoteDao.findByMedicalReview(medicalReview);
	}
	
}
