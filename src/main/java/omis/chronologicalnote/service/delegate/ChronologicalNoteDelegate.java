/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.chronologicalnote.dao.ChronologicalNoteDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;

/**
 * Chronological note delegate.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (January 30, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteDelegate {

	/* Data access objects. */
	private ChronologicalNoteDao chronologicalNoteDao;
	
	/*Property editor factories. */
	private InstanceFactory<ChronologicalNote> chronologicalNoteInstanceFactory;
	
	/* Component retrievers. */
	private AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates a chronological note delegate with the specified data 
	 * access object and instance factory.
	 * 
	 * @param chronologicalNoteDao chronological note data access object
	 * @param chronologicalNoteInstanceFactory chronological note instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ChronologicalNoteDelegate(final ChronologicalNoteDao 
			chronologicalNoteDao,
			final InstanceFactory<ChronologicalNote> 
				chronologicalNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.chronologicalNoteDao = chronologicalNoteDao;
		this.chronologicalNoteInstanceFactory 
			= chronologicalNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a chronological note with the specified date, offender, 
	 * and narrative.
	 * 
	 * @param date date
	 * @param offender offender
	 * @param title title
	 * @param narrative narrative
	 * @return newly created chronological note
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found.
	 */
	public ChronologicalNote create(final Date date, final Offender offender, 
			final String title, final String narrative)
		throws ChronologicalNoteExistsException {
		if (this.chronologicalNoteDao.find(date, offender, title) != null) {
			throw new ChronologicalNoteExistsException("Chronological note "
					+ "already exists.");
		}
		ChronologicalNote note = this.populateNote(
				this.chronologicalNoteInstanceFactory.createInstance(), 
				date, title, narrative);
		note.setOffender(offender);
		note.setCreationSignature(new CreationSignature(
					this.auditComponentRetriever.retrieveUserAccount(),
					this.auditComponentRetriever.retrieveDate()));
		return this.chronologicalNoteDao.makePersistent(note);
	}
	
	/**
	 * Updates the specified chronological note with the specified date and 
	 * narrative.
	 * 
	 * @param note chronological note
	 * @param date date
	 * @param title title
	 * @param narrative narrative
	 * @return updated chronological note
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found.
	 */
	public ChronologicalNote update(final ChronologicalNote note, 
			final Date date, final String title, final String narrative)
			throws ChronologicalNoteExistsException {
		if (this.chronologicalNoteDao.findExcluding(note, date, 
				note.getOffender(), title) != null) {
			throw new ChronologicalNoteExistsException("Chronological note "
					+ "already exists.");
		}
		return this.chronologicalNoteDao.makePersistent(this.populateNote(
				note, date, title, narrative));
	}
	
	/**
	 * Removes the specified chronological note.
	 * 
	 * @param note chronological note
	 */
	public void remove(final ChronologicalNote note) {
		this.chronologicalNoteDao.makeTransient(note);
	}
	
	/* Helper methodos. */
	
	/*
	 * Populates the specified chronological note with the specified date 
	 * and narrative.
	 * 
	 * @param note chronological note
	 * @param date date
	 * @param narrative narrative
	 * @return populated chronological note
	 */
	private ChronologicalNote populateNote(final ChronologicalNote note, 
			final Date date, final String title, final String narrative) {
		note.setDate(date);
		note.setTitle(title);
		note.setNarrative(narrative);
		note.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return note;
	}
}