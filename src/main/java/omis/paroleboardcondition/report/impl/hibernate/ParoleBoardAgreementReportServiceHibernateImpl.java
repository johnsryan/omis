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
package omis.paroleboardcondition.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.condition.domain.Agreement;
import omis.condition.domain.ConditionCategory;
import omis.condition.domain.ConditionGroup;
import omis.condition.report.ConditionSummary;
import omis.offender.domain.Offender;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.report.ParoleBoardAgreementReportService;
import omis.paroleboardcondition.report.ParoleBoardAgreementSummary;

/**
 * Parole Board Agreement Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardAgreementReportServiceHibernateImpl
		implements ParoleBoardAgreementReportService {
	
	private static final String SUMMARIZE_PAROLE_BOARD_AGREEMENT_QUERY_NAME =
			"summaryParoleBoardAgreement";
	
	private static final String FIND_SUMMARIES_BY_OFFENDER_QUERY_NAME =
			"findParoleBoardAgreementSummariesByOffender";
	
	private static final String FIND_SUMMARIES_BY_OFFENDER_ON_DATES_QUERY_NAME =
			"findParoleBoardAgreementSummariesByOffenderOnDates";
	
	private static final String FIND_BY_AGREEMENT_AND_CATEGORY_QUERY_NAME =
			"findConditionSummariesByAgreementAndConditionCategory";
	
	private static final String FIND_BY_AGREEMENT_AND_GROUP_QUERY_NAME =
			"findConditionSummariesByAgreementAndConditionGroup";
	
	private static final String AGREEMENT_PARAM_NAME = "agreement";
	
	private static final String PAROLE_BOARD_AGREEMENT_PARAM_NAME =
			"paroleBoardAgreement";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String CONDITION_GROUP_PARAM_NAME = "conditionGroup";
	
	private static final String CONDITION_CATEGORY_PARAM_NAME =
			"conditionCategory";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public ParoleBoardAgreementReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public ParoleBoardAgreementSummary summarize(
			final ParoleBoardAgreement agreement) {
		ParoleBoardAgreementSummary summary = (ParoleBoardAgreementSummary)
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_PAROLE_BOARD_AGREEMENT_QUERY_NAME)
				.setParameter(PAROLE_BOARD_AGREEMENT_PARAM_NAME, agreement)
				.setReadOnly(true)
				.uniqueResult();
		
		return summary;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardAgreementSummary> findByOffender(
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardAgreementSummary> paroleBoardAgreementSummaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_SUMMARIES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return paroleBoardAgreementSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardAgreementSummary> findByOffenderOnDates(
			final Offender offender, final Date startDate, final Date endDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardAgreementSummary> paroleBoardAgreementSummaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_SUMMARIES_BY_OFFENDER_ON_DATES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setReadOnly(true)
				.list();
		return paroleBoardAgreementSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ConditionSummary>
		findConditionSummariesByAgreementAndConditionCategory(
			final Agreement agreement,
			final ConditionCategory conditionCategory) {
		@SuppressWarnings("unchecked")
		List<ConditionSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_BY_AGREEMENT_AND_CATEGORY_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setParameter(CONDITION_CATEGORY_PARAM_NAME, conditionCategory)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ConditionSummary>
		findConditionSummariesByAgreementAndConditionGroup(
			final Agreement agreement, final ConditionGroup conditionGroup) {
		@SuppressWarnings("unchecked")
		List<ConditionSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_BY_AGREEMENT_AND_GROUP_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setParameter(CONDITION_GROUP_PARAM_NAME, conditionGroup)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}
}