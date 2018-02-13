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
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.parolereview.dao.ParoleReviewNoteDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewNote;

/**
 * 
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewNoteDelegate {

	/* Data access objects. */
	
	private final ParoleReviewNoteDao paroleReviewNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParoleReviewNote> 
			paroleReviewNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole review note delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param paroleReviewNoteDao parole review note data access object
	 * @param paroleReviewNoteInstanceFactory parole review note instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParoleReviewNoteDelegate(
			final ParoleReviewNoteDao paroleReviewNoteDao,
			final InstanceFactory<ParoleReviewNote> 
					paroleReviewNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleReviewNoteDao = paroleReviewNoteDao;
		this.paroleReviewNoteInstanceFactory = paroleReviewNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/**
	 * Creates a new parole review note.
	 * 
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @return parole review note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleReviewNote create(final ParoleReview paroleReview, 
			final Date date, final String description) 
					throws DuplicateEntityFoundException {
		if (this.paroleReviewNoteDao.find(paroleReview, date, description) != 
				null) {
			throw new DuplicateEntityFoundException(
					"Parole review note already exists.");
		}
		ParoleReviewNote paroleReviewNote = this.paroleReviewNoteInstanceFactory
				.createInstance();
		populateParoleReviewNote(paroleReviewNote, date, description, 
				paroleReview);
		paroleReviewNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.paroleReviewNoteDao.makePersistent(paroleReviewNote);
	}
	
	/**
	 * Updates an existing parole review note.
	 * 
	 * @param paroleReviewNote parole review note
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @return parole review note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleReviewNote update(final ParoleReviewNote paroleReviewNote, 
			final ParoleReview paroleReview, final Date date, 
			final String description) 
					throws DuplicateEntityFoundException {
		if (this.paroleReviewNoteDao.findExcluding(paroleReview, date, 
				description, paroleReviewNote) != null) {
			throw new DuplicateEntityFoundException(
					"Parole review note already exists.");
		}
		populateParoleReviewNote(paroleReviewNote, date, description, 
				paroleReview);
		return this.paroleReviewNoteDao.makePersistent(paroleReviewNote);
	}

	/**
	 * Removes the specified parole review note.
	 * 
	 * @param paroleReviewNote parole review note
	 */
	public void remove(final ParoleReviewNote paroleReviewNote) {
		this.paroleReviewNoteDao.makeTransient(paroleReviewNote);
	}
	
	/**
	 * Returns a list of parole review notes for the specified parole review.
	 * 
	 * @param paroleReview parole review
	 * @return parole review note
	 */
	public List<ParoleReviewNote> findByParoleReview(
			final ParoleReview paroleReview) {
		return this.paroleReviewNoteDao.findByParoleReview(paroleReview);
	}
	
	// Populates a parole review note
	public void populateParoleReviewNote(
			final ParoleReviewNote paroleReviewNote, final Date date,
			final String description, final ParoleReview paroleReview) {
		paroleReviewNote.setDate(date);
		paroleReviewNote.setDescription(description);
		paroleReviewNote.setParoleReview(paroleReview);
		paroleReviewNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}
