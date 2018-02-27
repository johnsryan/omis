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
package omis.substanceuse.report.hibernate.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.substanceuse.domain.SubstanceUse;
import omis.substanceuse.report.SubstanceUseSummary;
import omis.substanceuse.report.SubstanceUseSummaryService;
import omis.substanceuse.report.UseAffirmationSummary;
import omis.substanceuse.report.UseTermSummary;

/**
 * Substance use summary service implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class SubstanceUseSummaryServiceImpl 
	implements SubstanceUseSummaryService {

	/* Query names. */
	
	private static final String FIND_USE_SUMMARIES_QUERY_NAME 
		= "findSubstanceUseSummariesByOffender";
	
	private static final String FIND_USE_TERM_SUMMARIES_QUERY_NAME
		= "findUseTermSummariesByUse";
	
	private static final String FIND_USE_AFFIRMATIONS_SUMMARIES_QUERY_NAME
		= "findUseAffirmationsByUse";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String SUBSTANCE_USE_PARAM_NAME = "substanceUse";
	
	private SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of substance use summary with the specified
	 * session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public SubstanceUseSummaryServiceImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<SubstanceUseSummary> findUseSummaries(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<SubstanceUseSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_USE_SUMMARIES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<UseTermSummary> findUseTermSummaries(
			final SubstanceUse substanceUse) {
		@SuppressWarnings("unchecked")
		List<UseTermSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_USE_TERM_SUMMARIES_QUERY_NAME)
				.setParameter(SUBSTANCE_USE_PARAM_NAME, substanceUse)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<UseAffirmationSummary> findUseAffirmationSummaries(
			final SubstanceUse substanceUse) {
		@SuppressWarnings("unchecked")
		List<UseAffirmationSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_USE_AFFIRMATIONS_SUMMARIES_QUERY_NAME)
				.setParameter(SUBSTANCE_USE_PARAM_NAME, substanceUse)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}