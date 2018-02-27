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

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleplan.dao.ParolePlanNoteDao;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanNote;

/**
 * Parole plan note delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanNoteDelegate {

	/* Data access objects. */
	
	private final ParolePlanNoteDao parolePlanNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParolePlanNote> parolePlanNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole plan note delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param parolePlanNoteDao parole plan note data access object
	 * @param parolePlanNoteInstanceFactory parole plan note instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParolePlanNoteDelegate(final ParolePlanNoteDao parolePlanNoteDao,
			final InstanceFactory<ParolePlanNote> parolePlanNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.parolePlanNoteDao = parolePlanNoteDao;
		this.parolePlanNoteInstanceFactory = parolePlanNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new parole plan note.
	 * 
	 * @param parolePlan parole plan
	 * @param description description
	 * @param date date
	 * @return parole plan note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParolePlanNote create(final ParolePlan parolePlan, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		if (this.parolePlanNoteDao.find(parolePlan, description, date) != null) {
			throw new DuplicateEntityFoundException(
					"Parole plan note already exists.");
		}
		ParolePlanNote parolePlanNote = this.parolePlanNoteInstanceFactory
				.createInstance();
		populateParolePlanNote(parolePlanNote, parolePlan, description, date);
		parolePlanNote.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.parolePlanNoteDao.makePersistent(parolePlanNote);
	}

	/**
	 * Updates the specified parole plan note.
	 * 
	 * @param parolePlanNote parole plan note
	 * @param description description
	 * @param date date
	 * @return parole plan note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParolePlanNote update(final ParolePlanNote parolePlanNote, 
			final ParolePlan parolePlan, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		if (this.parolePlanNoteDao.findExcluding(parolePlan, description, date, 
				parolePlanNote) != null) {
			throw new DuplicateEntityFoundException(
					"Parole plan note already exists.");
		}
		populateParolePlanNote(parolePlanNote, parolePlan, description, date);
		return this.parolePlanNoteDao.makePersistent(parolePlanNote);
	}

	/**
	 * Removes the specified parole plan note.
	 * 
	 * @param parolePlanNote parole plan note
	 */
	public void remove(final ParolePlanNote parolePlanNote) {
		this.parolePlanNoteDao.makeTransient(parolePlanNote);
	}

	/**
	 * Returns a list of parole plan notes for the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return list of parole plan notes
	 */
	public List<ParolePlanNote> findByParolePlan(final ParolePlan parolePlan) {
		return this.parolePlanNoteDao.findByParolePlan(parolePlan);
	}

	// Populates a parole plan note
	private void populateParolePlanNote(final ParolePlanNote parolePlanNote, 
			final ParolePlan parolePlan, final String description, 
			final Date date) {
		parolePlanNote.setParolePlan(parolePlan);
		parolePlanNote.setDate(date);
		parolePlanNote.setDescription(description);
		parolePlanNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}