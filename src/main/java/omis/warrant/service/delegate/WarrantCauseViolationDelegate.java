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

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.condition.domain.ConditionClause;
import omis.instance.factory.InstanceFactory;
import omis.warrant.dao.WarrantCauseViolationDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantCauseViolation;
import omis.warrant.exception.WarrantCauseViolationExistsException;

/**
 * WarrantCauseDelegate.java
 * 
 *@author Annie Jacques 
 *@author Yidong Li
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantCauseViolationDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"A WarrantCauseViolation already exists with given Cause and "
			+ "Condition for the specified Warrant.";
	
	private final WarrantCauseViolationDao warrantCauseViolationDao;
	
	private final InstanceFactory<WarrantCauseViolation> 
		warrantCauseViolationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for WarrantCauseDelegate
	 * @param warrantCauseViolationDao
	 * @param warrantCauseViolationInstanceFactory
	 * @param auditComponentRetriever
	 */
	public WarrantCauseViolationDelegate(
			final WarrantCauseViolationDao warrantCauseViolationDao,
			final InstanceFactory<WarrantCauseViolation> 
				warrantCauseViolationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.warrantCauseViolationDao = warrantCauseViolationDao;
		this.warrantCauseViolationInstanceFactory =
				warrantCauseViolationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	
	/**
	 * Creates a WarrantCauseViolation with the specified properties
	 * @param warrant - Warrant
	 * @param cause - CourtCase
	 * @param condition - Condition
	 * @param description - String
	 * @return Newly created WarrantCauseViolation
	 * @throws WarrantCauseViolationExistsException - When a
	 * WarrantCauseViolation already
	 * exists with the given Cause and Condition for the specified Warrant
	 */
	public WarrantCauseViolation create(final Warrant warrant,
			final ConditionClause conditionClause, final String description)
					throws WarrantCauseViolationExistsException{
		if(this.warrantCauseViolationDao.find(warrant, conditionClause) != null){
			throw new WarrantCauseViolationExistsException(
				DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		WarrantCauseViolation warrantCauseViolation = 
				this.warrantCauseViolationInstanceFactory.createInstance();
		
		warrantCauseViolation.setWarrant(warrant);
		warrantCauseViolation.setConditionClause(conditionClause);
		warrantCauseViolation.setDescription(description);
		warrantCauseViolation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		warrantCauseViolation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantCauseViolationDao.makePersistent(warrantCauseViolation);
	}
	
	/**
	 * Updates the specified warrant clause violation.
	 * 
	 * @param warrantCauseViolation warrant clause violation
	 * @param conditionClause condition clause
	 * @param description description
	 * @return updated warrant clause violation
	 * @throws WarrantCauseViolationExistsException thrown when a duplicate
	 * warrant clause violation
	 * is found, excluding the specified warrant clause violation
	 */
	public WarrantCauseViolation update(
			final WarrantCauseViolation warrantCauseViolation,
			final ConditionClause conditionClause,
			final String description)
					throws WarrantCauseViolationExistsException{
		if(this.warrantCauseViolationDao.findExcluding(
				warrantCauseViolation.getWarrant(),
				conditionClause, warrantCauseViolation) != null){
			throw new WarrantCauseViolationExistsException(
				DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		warrantCauseViolation.setConditionClause(conditionClause);
		warrantCauseViolation.setDescription(description);
		warrantCauseViolation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.warrantCauseViolationDao.makePersistent(warrantCauseViolation);
	}
	
	/**
	 * Removes a WarrantCauseViolation
	 * @param warrantCauseViolation - WarrantCauseViolation to remove
	 */
	public void remove(final WarrantCauseViolation warrantCause){
		this.warrantCauseViolationDao.makeTransient(warrantCause);
	}
	
	/**
	 * Returns a list of all WarrantCauseViolations with specified Warrant
	 * @param warrant - Warrant
	 * @return List of all WarrantCauseViolations with specified Warrant
	 */
	public List<WarrantCauseViolation> findByWarrant(final Warrant warrant){
		return this.warrantCauseViolationDao.findByWarrant(warrant);
	}
	
}
