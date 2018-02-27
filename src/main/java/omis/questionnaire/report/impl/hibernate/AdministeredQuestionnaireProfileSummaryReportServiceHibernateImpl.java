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
package omis.questionnaire.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.questionnaire.report.AdministeredQuestionnaireProfileSummaryReportService;

/**
 * AdministeredQuestionnaireProfileSummaryReportServiceHibernateImpl.java
 * 
 * @author Annie Jacques
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class AdministeredQuestionnaireProfileSummaryReportServiceHibernateImpl
		implements AdministeredQuestionnaireProfileSummaryReportService {
	
	private static final String FIND_COUNT_BY_OFFENDER_QUERY_NAME =
			"findAdministeredQuestionnaireCountByAnswerer";
	
	private static final String ANSWERER_PARAM_NAME = "answerer";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory
	 */
	public AdministeredQuestionnaireProfileSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public Integer findCountByAnswerer(final Person answerer) {
		Integer count = Integer.valueOf(((Long) (this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_COUNT_BY_OFFENDER_QUERY_NAME)
				.setEntity(ANSWERER_PARAM_NAME, answerer)
				.setReadOnly(true)
				.uniqueResult())).intValue());
		
		return count;
	}

}
