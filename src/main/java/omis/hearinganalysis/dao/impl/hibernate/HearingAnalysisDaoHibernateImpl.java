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
package omis.hearinganalysis.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearinganalysis.dao.HearingAnalysisDao;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Hibernate implementation of the hearing analysis data access object.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisDaoHibernateImpl 
	extends GenericHibernateDaoImpl<HearingAnalysis> 
	implements HearingAnalysisDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findHearingAnalysis";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findHearingAnalysisExcluding";
	
	private static final String FIND_BY_PAROLE_ELIGIBILITY_QUERY_NAME = 
			"findHearingAnalysisByParoleEligibility";
	
	/* Parameters. */
	
	private static final String ELIGIBILITY_PARAM_NAME = "eligibility";
	
	private static final String PAROLE_BOARD_ITINERARY_PARAM_NAME = 
			"paroleBoardItinerary";
	
	private static final String CATEGORY_PARAM_NAME = "category";
	
	private static final String ANALYST_PARAM_NAME = "analyst";
	
	private static final String EXCLUDED_HEARING_ANALYSIS_PARAM_NAME = 
			"excludedHearingAnalysis";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  hearing analysis.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public HearingAnalysisDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis find(final ParoleEligibility eligibility, 
			final ParoleBoardItinerary paroleBoardItinerary,
			final HearingAnalysisCategory category,
			final BoardAttendee analyst) {
		HearingAnalysis hearingAnalysis = (HearingAnalysis) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ELIGIBILITY_PARAM_NAME, eligibility)
				.setParameter(PAROLE_BOARD_ITINERARY_PARAM_NAME, 
						paroleBoardItinerary)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(ANALYST_PARAM_NAME, analyst)
				.uniqueResult();
		return hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis findExcluding(final ParoleEligibility eligibility, 
			final ParoleBoardItinerary paroleBoardItinerary,
			final HearingAnalysisCategory category, final BoardAttendee analyst, 
			final HearingAnalysis excludedHearingAnalysis) {
		HearingAnalysis hearingAnalysis = (HearingAnalysis) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ELIGIBILITY_PARAM_NAME, eligibility)
				.setParameter(PAROLE_BOARD_ITINERARY_PARAM_NAME, 
						paroleBoardItinerary)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(ANALYST_PARAM_NAME, analyst)
				.setParameter(EXCLUDED_HEARING_ANALYSIS_PARAM_NAME, 
						excludedHearingAnalysis)
				.uniqueResult();
		return hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis findByParoleEligibility(
			final ParoleEligibility eligibility) {
		HearingAnalysis hearingAnalysis = (HearingAnalysis) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_ELIGIBILITY_QUERY_NAME)
				.setParameter(ELIGIBILITY_PARAM_NAME, eligibility)
				.uniqueResult();
		return hearingAnalysis;
	}
}