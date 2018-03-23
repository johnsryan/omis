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
package omis.assessment.service.delegate;

import java.util.Date;
import java.util.List;

import omis.assessment.dao.RatingNoteDao;
import omis.assessment.domain.RatingNote;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Rating note delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class RatingNoteDelegate {

	/* Data access objects. */
	
	private final RatingNoteDao ratingNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<RatingNote> ratingNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** 
	 * Instantiates an implementation of rating note delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param ratingNoteDao rating note data access object
	 * @param ratingNoteInstanceFactory rating note instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public RatingNoteDelegate(
			final RatingNoteDao ratingNoteDao,
			final InstanceFactory<RatingNote> 
					ratingNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.ratingNoteDao = ratingNoteDao;
		this.ratingNoteInstanceFactory = 
				ratingNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new rating note.
	 * 
	 * @param date date
	 * @param description description
	 * @param administeredQuestionnaire administered questionnaire
	 * @return rating note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingNote create(final Date date, final String description, 
			final AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException {
		if (this.ratingNoteDao.find(date, description, 
				administeredQuestionnaire) != null) {
			throw new DuplicateEntityFoundException(
					"Rating note already exists.");
		}
		RatingNote ratingNote = this.ratingNoteInstanceFactory.createInstance();
		populateRatingNote(ratingNote, date, description, 
				administeredQuestionnaire);
		ratingNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.ratingNoteDao.makePersistent(ratingNote);
	}
	
	/**
	 * Updates an existing rating note.
	 * 
	 * @param ratingNote rating note
	 * @param date date
	 * @param description description
	 * @param administeredQuestionnaire administered questionnaire
	 * @return rating note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingNote update(final RatingNote ratingNote, final Date date, 
			final String description, 
			final AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException {
		if (this.ratingNoteDao.findExcluding(date, description, 
				administeredQuestionnaire, ratingNote) != null) {
			throw new DuplicateEntityFoundException(
					"Rating note already exists.");
		}
		populateRatingNote(ratingNote, date, description, 
				administeredQuestionnaire);
		return this.ratingNoteDao.makePersistent(ratingNote);
	}

	/**
	 * Removes the specified rating note.
	 * 
	 * @param ratingNote rating note
	 */
	public void remove(final RatingNote ratingNote) {
		this.ratingNoteDao.makeTransient(ratingNote);
	}
	
	/**
	 * Return a list of rating notes for the specified administered 
	 * questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of rating notes
	 */
	public List<RatingNote> findRatingNotesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.ratingNoteDao.findByAdministeredQuestionnaire(
				administeredQuestionnaire);
	}
	
	// Populates a rating note
	private void populateRatingNote(final RatingNote ratingNote, 
			final Date date, final String description,
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ratingNote.setAdministeredQuestionnaire(administeredQuestionnaire);
		ratingNote.setDate(date);
		ratingNote.setDescription(description);
		ratingNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}