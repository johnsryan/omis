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

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.jail.domain.Jail;
import omis.warrant.dao.WarrantArrestDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;
import omis.warrant.exception.WarrantArrestExistsException;

/**
 * WarrantArrestDelegate.java
 * 
 *@author Annie Jacques 
 *@author Yidong Li
 *@version 0.1.0 (April 25, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantArrestDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A WarrantArrest already exists with specified Warrant";
	
	private final WarrantArrestDao warrantArrestDao;
	
	private final InstanceFactory<WarrantArrest> 
		warrantArrestInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for WarrantArrestDelegate
	 * @param warrantArrestDao
	 * @param warrantArrestInstanceFactory
	 * @param auditComponentRetriever
	 */
	public WarrantArrestDelegate(
			final WarrantArrestDao warrantArrestDao,
			final InstanceFactory<WarrantArrest> 
				warrantArrestInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.warrantArrestDao = warrantArrestDao;
		this.warrantArrestInstanceFactory = warrantArrestInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a WarrantArrest with the specified properties
	 * @param warrant - Warrant
	 * @param date - Date
	 * @param jail - Jail
	 * @param contactByDate - Date
	 * @return Newly created WarrantArrest
	 * @throws WarrantArrestExistsException - When a WarrantArrest already
	 * exists for the specified Warrant
	 */
	public WarrantArrest create(final Warrant warrant, final Date date,
			final Jail jail, final Date contactByDate)
					throws WarrantArrestExistsException{
		if(this.warrantArrestDao.find(warrant) != null){
			throw new WarrantArrestExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		WarrantArrest warrantArrest = 
				this.warrantArrestInstanceFactory.createInstance();
		
		warrantArrest.setDeterminationDeadline(contactByDate);
		warrantArrest.setDate(date);
		warrantArrest.setJail(jail);
		warrantArrest.setWarrant(warrant);
		warrantArrest.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		warrantArrest.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantArrestDao.makePersistent(warrantArrest);
	}
	
	/**
	 * Updates specified WarrantArrest with the given properties
	 * @param warrantArrest - WarrantArrest to update
	 * @param date - Date
	 * @param jail - Jail
	 * @param contactByDate - Date
	 * @return Updated WarrantArrest
	 * @throws WarrantArrestExistsException - When another WarrantArrest
	 * already exists for this WarrantArrest's Warrant
	 */
	public WarrantArrest update(final WarrantArrest warrantArrest,
			final Date date, final Jail jail,
			final Date contactByDate)
					throws WarrantArrestExistsException{
		if(this.warrantArrestDao.findExcluding(warrantArrest.getWarrant(),
				warrantArrest) != null){
			throw new WarrantArrestExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		warrantArrest.setDeterminationDeadline(contactByDate);
		warrantArrest.setDate(date);
		warrantArrest.setJail(jail);
		warrantArrest.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantArrestDao.makePersistent(warrantArrest);
	}
	
	/**
	 * Removes a WarrantArrest
	 * @param warrantArrest - WarrentArrest to remove
	 */
	public void remove(final WarrantArrest warrantArrest){
		this.warrantArrestDao.makeTransient(warrantArrest);
	}
	
	/**
	 * Returns a WarrantArrest with specified Warrant
	 * @param warrant - Warrant
	 * @return WarrantArrest with specified Warrant
	 */
	public WarrantArrest findByWarrant(final Warrant warrant){
		return this.warrantArrestDao.find(warrant);
	}
	
}
