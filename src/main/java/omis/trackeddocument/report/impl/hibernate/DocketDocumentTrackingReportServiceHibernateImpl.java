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
package omis.trackeddocument.report.impl.hibernate;

import java.util.List;

import omis.person.domain.Person;
import omis.trackeddocument.report.DocketDocumentReceivalSummary;
import omis.trackeddocument.report.DocketDocumentTrackingReportService;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for docket document tracking.
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class DocketDocumentTrackingReportServiceHibernateImpl
		implements DocketDocumentTrackingReportService {
	/* Query names. */
	private static final String SUMMARIZE_BY_DEFENDANT_QUERY_NAME
		= "summarizeDocketDocumentReceivalByDefendant";
	
	/* Parameter names. */
	private static final String DEFENDANT_PARAM_NAME = "defendant";
	
	/* Resources. */
	private final SessionFactory sessionFactory;

	/* Constructors. */
	/**
	 * Instantiates an implementation of report service.
	 * 
	 * @param sessionFactory session factory
	 */
	public DocketDocumentTrackingReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<DocketDocumentReceivalSummary> summarizedByDefendant(final
			Person defendant) {
		@SuppressWarnings("unchecked")
		List<DocketDocumentReceivalSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(SUMMARIZE_BY_DEFENDANT_QUERY_NAME)
			.setParameter(DEFENDANT_PARAM_NAME, defendant)
			.setReadOnly(true)
			.list();
		return summaries;
	}
}