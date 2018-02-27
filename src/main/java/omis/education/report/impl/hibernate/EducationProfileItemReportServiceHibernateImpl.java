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
package omis.education.report.impl.hibernate;

import java.util.Arrays;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.education.domain.AchievementCategoryLevel;
import omis.education.report.EducationProfileItemReportService;
import omis.education.report.EducationProfileSummary;
import omis.offender.domain.Offender;

/** 
 * Hibernate implementation of education profile item report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class EducationProfileItemReportServiceHibernateImpl 
	implements EducationProfileItemReportService {
	private static final String FIND_HIGH_SCHOOL_EDUCATION_BY_OFFENDER_QUERY_NAME =
			"findHighSchoolEducationByOffender";
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final String LEVELS_PARAM_NAME = "levels";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory - session factory. */
	public EducationProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** [@inheritDoc} */
	@Override
	public List<EducationProfileSummary> findEducationSummaryByOffender(
			final Offender offender) {
		return this.cast(this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_HIGH_SCHOOL_EDUCATION_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameterList(LEVELS_PARAM_NAME,
						Arrays.asList(AchievementCategoryLevel.HIGHSCHOOL,
						AchievementCategoryLevel.MASTERS, 
						AchievementCategoryLevel.ASSOCIATE,
						AchievementCategoryLevel.DOCTORAL, 
						AchievementCategoryLevel.BACHELORS))
				.setReadOnly(true)
				.list());
	}
	
	/* Casts to list of education summaries. */
	@SuppressWarnings("unchecked")
	private List<EducationProfileSummary> cast(List<?> objs) {
		return (List<EducationProfileSummary>) objs;
	}
}