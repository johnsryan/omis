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
package omis.condition.report.impl.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.condition.domain.Agreement;
import omis.condition.report.AgreementSummary;
import omis.condition.report.ConditionSummary;
import omis.condition.report.ConditionSummaryReportService;
import omis.offender.domain.Offender;

/** 
 * Hibernate implementation of report service.
 * 
 * @author Jonny Santy
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.2 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class ConditionSummaryReportServiceHibernateImpl 
	implements ConditionSummaryReportService {
	
	private static final String FIND_BY_AGREEMENT_QUERY_NAME =
			"findConditionSummariesByAgreement";
	
	private static final String FIND_AGREEMENTS_BY_OFFENDER_QUERY_NAME =
			"findAgreementsByOffenderAndEffectiveDate";
	
	private static final String SUMMARIZE_AGREEMENT_QUERY_NAME =
			"summarizeAgreement";
	
	private static final String AGREEMENT_PARAM_NAME = "agreement";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory - session factory. */
	public ConditionSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ConditionSummary> findByAgreement(final Agreement agreement,
			final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ConditionSummary> retVal = (List<ConditionSummary>) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_BY_AGREEMENT_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setReadOnly(true)
				.list();
		return retVal;
	}
	
	/** {@inheritDoc} */
	@Override
	public AgreementSummary summarize(final Agreement agreement) {
		return (AgreementSummary) this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_AGREEMENT_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.setReadOnly(true)
				.uniqueResult();
	}
	
	/** {@inheritDoc} */
	@Override 
	public List<AgreementSummary> findByOffender(final Offender offender,
			final Date effectiveDate) {
		List<AgreementSummary> agreementSummaries =
				new ArrayList<AgreementSummary>();
		@SuppressWarnings("unchecked")
		List<Agreement> agreements = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_AGREEMENTS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		for(Agreement agreement : agreements){
			List<ConditionSummary> conditionSummaries = 
					this.findByAgreement(agreement, effectiveDate);
			AgreementSummary agreementSummary = this.summarize(agreement);
			agreementSummary.setConditionSummaries(conditionSummaries);
			agreementSummaries.add(agreementSummary);
		}
		return agreementSummaries;
	}
}