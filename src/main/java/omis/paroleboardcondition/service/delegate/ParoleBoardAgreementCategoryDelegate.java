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
package omis.paroleboardcondition.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleboardcondition.dao.ParoleBoardAgreementCategoryDao;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;

/**
 * Parole Board Agreement Category Delegate.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 7, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardAgreementCategoryDelegate {
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Parole Board Agreement Category already exists with the "
			+ "given name.";
	
	private final ParoleBoardAgreementCategoryDao
		paroleBoardAgreementCategoryDao;
	
	private final InstanceFactory<ParoleBoardAgreementCategory> 
		paroleBoardAgreementCategoryInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for ParoleBoardAgreementCategoryDelegate.
	 * @param paroleBoardAgreementCategoryDao - Parole Board Agreement Category
	 * DAO
	 * @param paroleBoardAgreementCategoryInstanceFactory - Parole Board
	 * Agreement Category Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public ParoleBoardAgreementCategoryDelegate(
			final ParoleBoardAgreementCategoryDao
				paroleBoardAgreementCategoryDao,
			final InstanceFactory<ParoleBoardAgreementCategory> 
				paroleBoardAgreementCategoryInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleBoardAgreementCategoryDao = paroleBoardAgreementCategoryDao;
		this.paroleBoardAgreementCategoryInstanceFactory =
				paroleBoardAgreementCategoryInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Parole Board Agreement Category with the specified name.
	 * @param name - String
	 * @param boardHearingAgreement board hearing agreement
	 * @param hearingAnalysisAgreement hearing analysis agreement
	 * @return Newly created Parole Board Agreement Category
	 * @throws DuplicateEntityFoundException - When a Parole Board Agreement
	 * Category already exits with the specified name
	 */
	public ParoleBoardAgreementCategory create(final String name, 
			final Boolean boardHearingAgreement, 
			final Boolean hearingAnalysisAgreement)
				throws DuplicateEntityFoundException {
		if (this.paroleBoardAgreementCategoryDao.find(name) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		ParoleBoardAgreementCategory paroleBoardAgreementCategory = 
				this.paroleBoardAgreementCategoryInstanceFactory
				.createInstance();
		
		populateParoleBoardAgreementCategory(paroleBoardAgreementCategory, name, 
				boardHearingAgreement, hearingAnalysisAgreement);
		paroleBoardAgreementCategory.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
				
		return this.paroleBoardAgreementCategoryDao
				.makePersistent(paroleBoardAgreementCategory);
	}
	
	/**
	 * Updates specified Parole Board Agreement Category with the
	 * specified name.
	 * @param paroleBoardAgreementCategory - Parole Board Agreement Category
	 * to update
	 * @param name - String
	 * @param boardHearingAgreement board hearing agreement
	 * @param hearingAnalysisAgreement hearing analysis agreement
	 * @return Updated Parole Board Agreement Category
	 * @throws DuplicateEntityFoundException - When a Parole Board Agreement
	 * Category already exits with the specified name
	 */
	public ParoleBoardAgreementCategory update(
			final ParoleBoardAgreementCategory paroleBoardAgreementCategory,
			final String name, final Boolean boardHearingAgreement, 
			final Boolean hearingAnalysisAgreement)
				throws DuplicateEntityFoundException {
		if (this.paroleBoardAgreementCategoryDao.findExcluding(
				name, paroleBoardAgreementCategory) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		populateParoleBoardAgreementCategory(paroleBoardAgreementCategory, name, 
				boardHearingAgreement, hearingAnalysisAgreement);
		
		
		return this.paroleBoardAgreementCategoryDao
				.makePersistent(paroleBoardAgreementCategory);
	}

	/**
	 * Removes the specified Parole Board Agreement Category.
	 * @param paroleBoardAgreementCategory - Parole Board Agreement Category
	 * to remove
	 */
	public void remove(
			final ParoleBoardAgreementCategory paroleBoardAgreementCategory) {
		this.paroleBoardAgreementCategoryDao
				.makeTransient(paroleBoardAgreementCategory);
	}
	
	/**
	 * Returns a list of all Parole Board Agreement Categories.
	 * @return List of all Parole Board Agreement Categories.
	 */
	public List<ParoleBoardAgreementCategory> findAll() {
		return this.paroleBoardAgreementCategoryDao.findAll();
	}

	// Populates a parole board agreement category
	private void populateParoleBoardAgreementCategory(
			final ParoleBoardAgreementCategory paroleBoardAgreementCategory,
			final String name, final Boolean boardHearingAgreement, 
			final Boolean hearingAnalysisAgreement) {
		paroleBoardAgreementCategory.setName(name);
		paroleBoardAgreementCategory.setBoardHearingAgreement(
				boardHearingAgreement);
		paroleBoardAgreementCategory.setHearingAnalysisAgreement(
				hearingAnalysisAgreement);
		paroleBoardAgreementCategory.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
	}
}