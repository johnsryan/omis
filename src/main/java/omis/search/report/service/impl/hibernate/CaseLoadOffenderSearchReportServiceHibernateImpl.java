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
package omis.search.report.service.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.search.report.OffenderSearchResult;
import omis.search.report.service.CaseLoadOffenderSearchReportService;

/** 
 * Implementation of case search summary service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0 
 */
public class CaseLoadOffenderSearchReportServiceHibernateImpl
	implements CaseLoadOffenderSearchReportService {

	private static final String DATE_PARAM_NAME = "date";

	private static final String PERSON_PARAM_NAME = "person";

	private static final String FIND_CASELOAD_OFFENDER_BY_PERSON =
			"findCaseLoadOffenderByPerson";

	private final SessionFactory sessionFactory;

	/** Constructor.
	 * @param sessionFactory session factory. */
	public CaseLoadOffenderSearchReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	/** {@inheritDoc} */
	@Override
	public List<OffenderSearchResult> findCaseLoadOffenderSearchByPerson(
			final Person person) {
		@SuppressWarnings("unchecked")
		final List<OffenderSearchResult> caseLoad = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASELOAD_OFFENDER_BY_PERSON)
				.setParameter(PERSON_PARAM_NAME, person)
				.setDate(DATE_PARAM_NAME, new Date())
				.setReadOnly(true)
				.list();

		return caseLoad;
	}
}