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
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.mentalhealthreview.dao.MentalHealthNoteDao;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;

/**
 * Mental health note delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthNoteDelegate {

	/* Data access objects. */
	
	private final MentalHealthNoteDao mentalHealthNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<MentalHealthNote> 
			mentalHealthNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of mental health note delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param mentalHealthNoteDao mental health note data access object
	 * @param mentalHealthNoteInstanceFactory mental health note instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public MentalHealthNoteDelegate(
			final MentalHealthNoteDao mentalHealthNoteDao,
			final InstanceFactory<MentalHealthNote> 
					mentalHealthNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.mentalHealthNoteDao = mentalHealthNoteDao;
		this.mentalHealthNoteInstanceFactory = 
				mentalHealthNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new mental health note.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param description description
	 * @return mental health note
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public MentalHealthNote create(final MentalHealthReview mentalHealthReview, 
			final Date date, final String description) 
					throws DuplicateEntityFoundException {
		if (this.mentalHealthNoteDao.find(mentalHealthReview, date, description)
				!= null) {
			throw new DuplicateEntityFoundException(
					"Mental health note already exists.");
		}
		MentalHealthNote mentalHealthNote = this.mentalHealthNoteInstanceFactory
				.createInstance();
		populateMentalHealthNote(mentalHealthNote, mentalHealthReview, date, 
				description);
		mentalHealthNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.mentalHealthNoteDao.makePersistent(mentalHealthNote);
	}
	
	/**
	 * Updates the specified mental health note.
	 * 
	 * @param mentalHealthNote mental health note
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param description description
	 * @return mental health note
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	public MentalHealthNote update(final MentalHealthNote mentalHealthNote, 
			final MentalHealthReview mentalHealthReview, final Date date, 
			final String description) throws DuplicateEntityFoundException {
		if (this.mentalHealthNoteDao.findExcluding(mentalHealthReview, date, 
				description, mentalHealthNote) != null) {
			throw new DuplicateEntityFoundException(
					"Mental health note already exists.");
		}
		populateMentalHealthNote(mentalHealthNote, mentalHealthReview, date, 
				description);
		mentalHealthNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.mentalHealthNoteDao.makePersistent(mentalHealthNote);
	}

	/**
	 * Removes the specified mental health note.
	 * 
	 * @param mentalHealthNote mental health note
	 */
	public void remove(final MentalHealthNote mentalHealthNote) {
		this.mentalHealthNoteDao.makeTransient(mentalHealthNote);
	}
	
	/**
	 * Returns a list of mental health notes for the specified mental health 
	 * review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health notes
	 */
	public List<MentalHealthNote> findByMentalHealthReview(
				MentalHealthReview mentalHealthReview) {
		return this.mentalHealthNoteDao.findByMentalHealthReview(
				mentalHealthReview);
	}
	
	// Populates a mental health note
	private void populateMentalHealthNote(
			final MentalHealthNote mentalHealthNote,
			final MentalHealthReview mentalHealthReview, final Date date,
			final String description) {
		mentalHealthNote.setMentalHealthReview(mentalHealthReview);
		mentalHealthNote.setDate(date);
		mentalHealthNote.setDescription(description);
		mentalHealthNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}
}
