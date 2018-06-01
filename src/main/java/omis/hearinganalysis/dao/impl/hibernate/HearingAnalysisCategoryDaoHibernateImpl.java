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

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearinganalysis.dao.HearingAnalysisCategoryDao;
import omis.hearinganalysis.domain.HearingAnalysisCategory;

/**
 * Hibernate implementation of the hearing analysis category data access object.
 *
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (May 29, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisCategoryDaoHibernateImpl 
	extends GenericHibernateDaoImpl<HearingAnalysisCategory> 
	implements HearingAnalysisCategoryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findHearingAnalysisCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findHearingAnalysisCategoryExcluding";
	
	private static final String FIND_ALL_QUERY_NAME =
			"findAllHearingAnalysisCategories";
	
	/* Parameters. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String EXCLUDED_HEARING_ANALYSIS_CATEGORY_PARAM_NAME = 
			"excludedHearingAnalysisCategory";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  hearing analysis category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public HearingAnalysisCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisCategory find(final String name) {
		HearingAnalysisCategory hearingAnalysisCategory = 
				(HearingAnalysisCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return hearingAnalysisCategory;
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisCategory findExcluding(final String name, 
			final HearingAnalysisCategory excludedHearingAnalysisCategory) {
		HearingAnalysisCategory hearingAnalysisCategory = 
				(HearingAnalysisCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(EXCLUDED_HEARING_ANALYSIS_CATEGORY_PARAM_NAME, 
						excludedHearingAnalysisCategory)
				.uniqueResult();
		return hearingAnalysisCategory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<HearingAnalysisCategory> findAllHearingAnalysisCategories() {
		@SuppressWarnings("unchecked")
		List<HearingAnalysisCategory> hearingAnalysisCategories = 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ALL_QUERY_NAME)
				.list();
		return hearingAnalysisCategories;
	}
	
}
