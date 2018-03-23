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
package omis.hearing.service.delegate;

import java.util.List;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.InfractionPleaDao;
import omis.hearing.domain.InfractionPlea;
import omis.instance.factory.InstanceFactory;

/**
 * Infraction Plea Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
public class InfractionPleaDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Infraction Plea already exists with the given name.";
	
	private final InfractionPleaDao infractionPleaDao;
	
	private final InstanceFactory<InfractionPlea> 
		infractionPleaInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for InfractionPleaDelegate.
	 * @param infractionPleaDao - Infraction Plea DAO
	 * @param infractionPleaInstanceFactory - Infraction Plea Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public InfractionPleaDelegate(
			final InfractionPleaDao infractionPleaDao,
			final InstanceFactory<InfractionPlea> 
				infractionPleaInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.infractionPleaDao = infractionPleaDao;
		this.infractionPleaInstanceFactory = infractionPleaInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	
	/**
	 * Creates an Infraction Plea with the specified properties.
	 * @param name - String name
	 * @param valid - Boolean valid
	 * @return newly created Infraction Plea
	 * @throws DuplicateEntityFoundException - When an infraction plea already
	 * exists with the given name.
	 */
	public InfractionPlea create(final String name, final Boolean valid)
			throws DuplicateEntityFoundException {
		if (this.infractionPleaDao.find(name) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		InfractionPlea infractionPlea = 
				this.infractionPleaInstanceFactory.createInstance();
		
		infractionPlea.setName(name);
		infractionPlea.setValid(valid);
		infractionPlea.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.infractionPleaDao.makePersistent(infractionPlea);
	}
	
	/**
	 * Updates an Infraction Plea with the specified properties.
	 * @param infractionPlea - Infraction Plea to update.
	 * @param name - String name
	 * @param valid - Boolean valid
	 * @return updated Infraction Plea
	 * @throws DuplicateEntityFoundException - When an infraction plea already
	 * exists with the given name.
	 */
	public InfractionPlea update(final InfractionPlea infractionPlea,
			final String name, final Boolean valid)
				throws DuplicateEntityFoundException {
		if (this.infractionPleaDao.findExcluding(name,
				infractionPlea) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}

		infractionPlea.setName(name);
		infractionPlea.setValid(valid);
		infractionPlea.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.infractionPleaDao.makePersistent(infractionPlea);
	}
	
	/**
	 * Removes an Infraction Plea.
	 * @param infractionPlea - Infraction Plea to remove.
	 */
	public void remove(final InfractionPlea infractionPlea) {
		this.infractionPleaDao.makeTransient(infractionPlea);
	}
	
	/**
	 * Returns a list of all valid Infraction Pleas.
	 * @return List of all valid Infraction Pleas.
	 */
	public List<InfractionPlea> findAll() {
		return this.infractionPleaDao.findAll();
	}
}
