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
package omis.travelpermit.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.travelpermit.dao.TravelPermitNoteDao;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;
import omis.travelpermit.exception.TravelPermitNoteExistsException;

/**
 * Delegate for travel permit note.
 *
 * @author Yidong Li
 * @version 0.0.2 (Aug 18, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitNoteDelegate {
	/* Resources. */
	private final TravelPermitNoteDao travelPermitNoteDao;
	private final InstanceFactory<TravelPermitNote>
		travelPermitNoteInstanceFactory;
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	/**
	 * Instantiates delegate for managing travel permit notes.
	 * 
	 * @param travelPermitNoteInstanceFactory instance factory for travel permit
	 * note
	 * @param travelPermitDao travel permit dao
	 * @param auditComponentRetriever audit component retriever
	 */
	public TravelPermitNoteDelegate(
		final TravelPermitNoteDao travelPermitNoteDao,
		final InstanceFactory<TravelPermitNote> travelPermitNoteInstanceFactory,
		final AuditComponentRetriever auditComponentRetriever) {
		this.travelPermitNoteDao = travelPermitNoteDao;
		this.travelPermitNoteInstanceFactory = travelPermitNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Methods. */
	/**
	 * Create travel permit note.
	 * 
	 * @param travelPErmit travel permit
	 * @param date date
	 * @param value value
	 * @throws TravelPermitNoteExistsException note already exists
	 * @return travel permit
	 */
	public TravelPermitNote createTravelPermitNote(
		final TravelPermit permit, final Date date, final String value)
			throws TravelPermitNoteExistsException {
		if(this.travelPermitNoteDao.findExistingTravelPermitNote(permit, date,
			value)!=null){
			throw new TravelPermitNoteExistsException(
				"Travel permit note already exists");
		}
		
		TravelPermitNote travelPermitnote
		= this.travelPermitNoteInstanceFactory.createInstance();
		travelPermitnote.setDate(date);
		travelPermitnote.setValue(value);
		travelPermitnote.setPermit(permit);
		travelPermitnote.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		travelPermitnote.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		return this.travelPermitNoteDao.makePersistent(travelPermitnote);
	}	
	
	/**
	 * Updates travel permit note
	 * 
	 * @param note travel permit note
	 * @param date date
	 * @param value value
	 * @return updated travel permit note
	 * @throws DuplicateEntityFoundException if work assignment note exists
	 */
	public TravelPermitNote updateTravelPermitNote(final TravelPermitNote note, 
			final Date date, final String value)
			throws TravelPermitNoteExistsException{
		if (this.travelPermitNoteDao.findExcludedExistingTravelPermitNote(note,
			date, value)!=null) {
			throw new TravelPermitNoteExistsException(
				"Travel permit note already exists");
		}
		note.setDate(date);
		note.setValue(value);
		note.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		note.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		return this.travelPermitNoteDao.makePersistent(note);
	}
	
	/**
	 * Removes travel permit note.
	 * 
	 * @param note travel permit note
	 */
	public void removeTravelPermitNote(final TravelPermitNote note) {
		this.travelPermitNoteDao.makeTransient(note);
	}
	
	/**
	 * Find travel permits notes by travel permit
	 * 
	 * @param travelPermit travel permit
	 * @return a list of travel permits
	 *
	 */
	public List<TravelPermitNote> findNotes(
		final TravelPermit permit){
		return this.travelPermitNoteDao.findNotes(permit);
	}
}