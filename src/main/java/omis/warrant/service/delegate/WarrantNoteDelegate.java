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
package omis.warrant.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.warrant.dao.WarrantNoteDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantNote;
import omis.warrant.exception.WarrantNoteExistsException;

/**
 * WarrantNoteDelegate.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
/**
 * WarrantNoteDelegate.java
 * 
 *@author Annie Jacques 
 *@author Yidong Li
 *@version 0.1.0 (April 25, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantNoteDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A WarrantNote already exists with given Note and Date for the specified Warrant.";
	
	private final WarrantNoteDao warrantNoteDao;
	
	private final InstanceFactory<WarrantNote> 
		warrantNoteInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for WarrantNoteDelegate
	 * @param warrantNoteDao
	 * @param warrantNoteInstanceFactory
	 * @param auditComponentRetriever
	 */
	public WarrantNoteDelegate(
			final WarrantNoteDao warrantNoteDao,
			final InstanceFactory<WarrantNote> 
				warrantNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.warrantNoteDao = warrantNoteDao;
		this.warrantNoteInstanceFactory = warrantNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	
	/**
	 * Creates a WarrantNote with the specified properties
	 * @param warrant - Warrant
	 * @param note - String
	 * @param date - Date
	 * @return Newly created WarrantNote
	 * @throws WarrantNoteExistsException - When a WarrantNote already exists
	 * with the given Note and Date for the specified Warrant
	 */
	public WarrantNote create(final Warrant warrant, final String note,
			final Date date)
				throws WarrantNoteExistsException{
		if(this.warrantNoteDao.find(note, date, warrant) != null){
			throw new WarrantNoteExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		WarrantNote warrantNote = 
				this.warrantNoteInstanceFactory.createInstance();
		
		warrantNote.setWarrant(warrant);
		warrantNote.setDate(date);
		warrantNote.setValue(note);
		warrantNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		warrantNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantNoteDao.makePersistent(warrantNote);
	}
	
	/**
	 * Updates a WarrantNote with the specified properties
	 * @param warrantNote - WarrantNote to update
	 * @param note - String
	 * @param date - Date
	 * @return Updated WarrantNote
	 * @throws WarrantNoteExistsException - When a WarrantNote already exists
	 * with the given Note and Date for the specified Warrant
	 */
	public WarrantNote update(final WarrantNote warrantNote, final String note,
			final Date date)
				throws WarrantNoteExistsException{
		if(this.warrantNoteDao.findExcluding(
				note, date, warrantNote.getWarrant(), warrantNote) != null){
			throw new WarrantNoteExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		warrantNote.setDate(date);
		warrantNote.setValue(note);
		warrantNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantNoteDao.makePersistent(warrantNote);
	}
	
	/**
	 * Removes a WarrantNote
	 * @param warrantNote - WarrantNote to remove
	 */
	public void remove(final WarrantNote warrantNote){
		this.warrantNoteDao.makeTransient(warrantNote);
	}
	
	/**
	 * Returns a list of all WarrantNotes with specified Warrant
	 * @param warrant - Warrant
	 * @return List of all WarrantNotes with specified Warrant
	 */
	public List<WarrantNote> findByWarrant(final Warrant warrant){
		return this.warrantNoteDao.findByWarrant(warrant);
	}
	
}
