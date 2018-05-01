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


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.jail.domain.Jail;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.warrant.dao.WarrantDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.exception.WarrantExistsException;

/**
 * Warrant delegate.
 * 
 *@author Annie Jacques
 *@author Joel Norris
 *@author Yidong Li
 *@version 0.1.1 (April 4, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A Warrant already exists on given date for the specified Offender.";
	
	private final WarrantDao warrantDao;
	
	private final InstanceFactory<Warrant> 
		warrantInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for WarrantDelegate
	 * @param warrantDao
	 * @param warrantInstanceFactory
	 * @param auditComponentRetriever
	 */
	public WarrantDelegate(
			final WarrantDao warrantDao,
			final InstanceFactory<Warrant> 
				warrantInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.warrantDao = warrantDao;
		this.warrantInstanceFactory = warrantInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Warrant with the specified properties
	 * @param offender - Offender
	 * @param date - Date
	 * @param addressee - String
	 * @param issuedBy - Person
	 * @param bondable - Boolean
	 * @param bondRecommendation - BigDecimal
	 * @param warrantReason - WarrantReasonCategory
	 * @param holdingJail holding jail
	 * @return Newly created Warrant
	 * @throws DuplicateEntityFoundException - When a Warrant already exists
	 * on given date for the specified offender
	 */
	public Warrant create(final Offender offender, final Date date,
			final String addressee, final Person issuedBy,
			final Boolean bondable, final BigDecimal bondRecommendation,
			final WarrantReasonCategory warrantReason, final Jail holdingJail)
					throws WarrantExistsException{
		if(this.warrantDao.find(offender, date) != null){
			throw new WarrantExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		Warrant warrant = 
				this.warrantInstanceFactory.createInstance();
		
		warrant.setOffender(offender);
		warrant.setAddressee(addressee);
		warrant.setBondable(bondable);
		warrant.setBondRecommendation(bondRecommendation);
		warrant.setDate(date);
		warrant.setIssuedBy(issuedBy);
		warrant.setWarrantReason(warrantReason);
		warrant.setHoldingJail(holdingJail);
		warrant.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		warrant.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantDao.makePersistent(warrant);
	}
	
	/**
	 * Updates a Warrant with the specified properties
	 * @param warrant - Warrant to update
	 * @param date - Date
	 * @param addressee - String
	 * @param issuedBy - Person
	 * @param bondable - Boolean
	 * @param bondRecommendation - BigDecimal
	 * @param warrantReason - WarrantReasonCategory
	 * @param holdingJail holding jail
	 * @return Updated Warrant
	 * @throws WarrantExistsException - When a Warrant already exists
	 * on given date for the specified offender
	 */
	public Warrant update(final Warrant warrant, final Date date,
			final String addressee, final Person issuedBy,
			final Boolean bondable, final BigDecimal bondRecommendation,
			final WarrantReasonCategory warrantReason,
			final Jail holdingJail)
			throws WarrantExistsException{
		if(this.warrantDao.findExcluding(
				warrant.getOffender(), date, warrant) != null){
			throw new WarrantExistsException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		warrant.setAddressee(addressee);
		warrant.setBondable(bondable);
		warrant.setBondRecommendation(bondRecommendation);
		warrant.setDate(date);
		warrant.setIssuedBy(issuedBy);
		warrant.setWarrantReason(warrantReason);
		warrant.setHoldingJail(holdingJail);
		warrant.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantDao.makePersistent(warrant);
	}
	
	/**
	 * Removes a Warrant
	 * @param warrant - Warrant to remove
	 */
	public void remove(final Warrant warrant){
		this.warrantDao.makeTransient(warrant);
	}
	
	/**
	 * Returns a List of Warrants for the specified Offender
	 * @param offender - Offender
	 * @return List of Warrants
	 */
	public List<Warrant> findByOffender(final Offender offender){
		return this.warrantDao.findByOffender(offender);
	}
	
}
