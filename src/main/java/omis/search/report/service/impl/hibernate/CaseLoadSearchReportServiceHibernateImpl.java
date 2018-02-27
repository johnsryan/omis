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

import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.search.report.CaseLoadSearchResult;
import omis.search.report.service.CaseLoadSearchReportService;

/** 
 * Hibernate implementation of case load search report service.
 * 
 * @author Ryan Johns 
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0 
 */
public class CaseLoadSearchReportServiceHibernateImpl
		implements CaseLoadSearchReportService {

	private static final String DATE_PARAM_NAME = "date";

	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String DESCRIPTION_PARAM_NAME = "description";

	private static final String TITLE_PARAM_NAME = "title";

	private static final String FIND_CASELOAD_REPORT_BY_TITLE =
			"findCaseLoadReportByTitle";

	private static final String FIND_CASELOAD_REPORT_BY_DESCRIPTION =
			"findCaseLoadReportByDescription";

	private static final String FIND_CASELOAD_REPORT_BY_OFFENDER =
			"findCaseLoadReportByOffender";

	private static final String FIND_CASELOAD_REPORT_BY_CASEWORKER =
			"findCaseLoadReportByCaseWorker";

	private final SessionFactory sessionFactory;

	/** constructor.
	 * @param sessionFactory session factory. */
	public CaseLoadSearchReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseLoadSearchResult> findByTitle(final String title) {
		@SuppressWarnings("unchecked")
		final List<CaseLoadSearchResult> results =
				(List<CaseLoadSearchResult>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASELOAD_REPORT_BY_TITLE)
				.setParameter(TITLE_PARAM_NAME, title)
				.setReadOnly(true)
				.list();

		return results;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseLoadSearchResult> findByDescription(
			final String description) {
		@SuppressWarnings("unchecked")
		final List<CaseLoadSearchResult> results =
				(List<CaseLoadSearchResult>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASELOAD_REPORT_BY_DESCRIPTION)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setReadOnly(true)
				.list();

		return results;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseLoadSearchResult> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		final List<CaseLoadSearchResult> results =
				(List<CaseLoadSearchResult>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_CASELOAD_REPORT_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(DATE_PARAM_NAME, new Date())
				.setReadOnly(true)
				.list();

		return results;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseLoadSearchResult> findByCaseWorker(final Person person) {
		@SuppressWarnings("unchecked")
		final List<CaseLoadSearchResult> results =
			(List<CaseLoadSearchResult>) this.sessionFactory.getCurrentSession()
			.getNamedQuery(FIND_CASELOAD_REPORT_BY_CASEWORKER)
			.setParameter("person", person)
			.setReadOnly(true)
			.list();

		return results;
	}
}