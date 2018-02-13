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
package omis.paroleboardcondition.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.paroleboardcondition.dao.ParoleBoardAgreementDao;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;

/**
 * Parole Board Agreement DAO Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardAgreementDaoHibernateImpl
		extends GenericHibernateDaoImpl<ParoleBoardAgreement>
		implements ParoleBoardAgreementDao {
	
	private static final String FIND_QUERY_NAME = "findParoleBoardAgreement";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findParoleBoardAgreementExcluding";
	
	private static final String AGREEMENT_PARAM_NAME = "agreement";
	
	private static final String CATEGORY_PARAM_NAME = "category";
	
	private static final String PAROLE_BOARD_AGREEMENT_PARAM_NAME =
			"paroleBoardAgreement";
	
	private static final String BOARD_HEARING_PARAM_NAME = "boardHearing";
	
	private static final String HEARING_ANALYSIS_PARAM_NAME = "hearingAnalysis";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String Entity Name
	 */
	protected ParoleBoardAgreementDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public ParoleBoardAgreement find(final Agreement agreement,
			final BoardHearing boardHearing,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category) {
		ParoleBoardAgreement paroleBoardAgreement = (ParoleBoardAgreement)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing, 
						this.getEntityPropertyType(BOARD_HEARING_PARAM_NAME))
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis, 
						this.getEntityPropertyType(HEARING_ANALYSIS_PARAM_NAME))
				.setParameter(CATEGORY_PARAM_NAME, category)
				.uniqueResult();
		
		return paroleBoardAgreement;
	}

	/**{@inheritDoc} */
	@Override
	public ParoleBoardAgreement findExcluding(final Agreement agreement,
			final BoardHearing boardHearing,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category,
			final ParoleBoardAgreement paroleBoardAgreementExcluding) {
		ParoleBoardAgreement paroleBoardAgreement = (ParoleBoardAgreement)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing, 
						this.getEntityPropertyType(BOARD_HEARING_PARAM_NAME))
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis, 
						this.getEntityPropertyType(HEARING_ANALYSIS_PARAM_NAME))
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(PAROLE_BOARD_AGREEMENT_PARAM_NAME,
						paroleBoardAgreementExcluding)
				.uniqueResult();
		
		return paroleBoardAgreement;
	}
}