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
import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.instance.factory.InstanceFactory;
import omis.paroleboardcondition.dao.ParoleBoardAgreementDao;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;

/**
 * Parole Board Agreement Delegate.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardAgreementDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Parole Board Agreement already exists with the given Agreement "
			+ "and Category.";
	
	private final ParoleBoardAgreementDao paroleBoardAgreementDao;
	
	private final InstanceFactory<ParoleBoardAgreement> 
		paroleBoardAgreementInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for ParoleBoardAgreementDelegate.
	 * @param paroleBoardAgreementDao - Parole Board Agreement DAO
	 * @param paroleBoardAgreementInstanceFactory - Parole Board Agreement
	 * Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public ParoleBoardAgreementDelegate(
			final ParoleBoardAgreementDao paroleBoardAgreementDao,
			final InstanceFactory<ParoleBoardAgreement> 
				paroleBoardAgreementInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleBoardAgreementDao = paroleBoardAgreementDao;
		this.paroleBoardAgreementInstanceFactory =
				paroleBoardAgreementInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Parole Board Agreement with the specified properties.
	 * 
	 * @param agreement agreement
	 * @param boardHearing board hearing
	 * @param hearingAnalysis hearing analysis
	 * @param category parole board agreement category
	 * @return parole board agreement
	 * @throws DuplicateEntityFoundException When a parole board agreement
	 * already exists with the given agreement and category.
	 */
	public ParoleBoardAgreement create(final Agreement agreement,
			final BoardHearing boardHearing,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category)
					throws DuplicateEntityFoundException {
		if (this.paroleBoardAgreementDao.find(agreement, boardHearing,
				hearingAnalysis, category) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		ParoleBoardAgreement paroleBoardAgreement = 
				this.paroleBoardAgreementInstanceFactory.createInstance();
		populateParoleBoardAgreement(paroleBoardAgreement, agreement, 
				boardHearing, hearingAnalysis, category);
		paroleBoardAgreement.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		return this.paroleBoardAgreementDao
				.makePersistent(paroleBoardAgreement);
	}
	
	/**
	 * Updates a parole board agreement with the specified properties.
	 * 
	 * @param paroleBoardAgreement parole board agreement to update
	 * @param agreement agreement
	 * @param boardHearing board hearing
	 * @param hearingAnalysis hearing analysis
	 * @param category parole board agreement category
	 * @return parole board agreement
	 * @throws DuplicateEntityFoundException When a parole board agreement
	 * already exists with the given agreement and category.
	 */
	public ParoleBoardAgreement update(
			final ParoleBoardAgreement paroleBoardAgreement,
			final Agreement agreement,
			final BoardHearing boardHearing,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category)
					throws DuplicateEntityFoundException {
		if (this.paroleBoardAgreementDao.findExcluding(agreement, boardHearing, 
				hearingAnalysis, category, paroleBoardAgreement) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		populateParoleBoardAgreement(paroleBoardAgreement, agreement, 
				boardHearing, hearingAnalysis, category);
		return this.paroleBoardAgreementDao
				.makePersistent(paroleBoardAgreement);
	}

	/**
	 * Removes the specified Parole Board Agreement.
	 * @param paroleBoardAgreement - Parole Board Agreement to remove;
	 */
	public void remove(final ParoleBoardAgreement paroleBoardAgreement) {
		this.paroleBoardAgreementDao.makeTransient(paroleBoardAgreement);
	}
	
	/**
	 * Returns a list of all Parole Board Agreements. For use in Unit Testing.
	 * @return List of all Parole Board Agreements
	 */
	public List<ParoleBoardAgreement> findAll() {
		return this.paroleBoardAgreementDao.findAll();
	}
	
	// Populates a parole board agreement
	private void populateParoleBoardAgreement(
			final ParoleBoardAgreement paroleBoardAgreement,
			final Agreement agreement, final BoardHearing boardHearing,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category) {
		paroleBoardAgreement.setAgreement(agreement);
		paroleBoardAgreement.setBoardHearing(boardHearing);
		paroleBoardAgreement.setHearingAnalysis(hearingAnalysis);
		paroleBoardAgreement.setCategory(category);
		paroleBoardAgreement.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}
}
