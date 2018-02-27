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

import java.util.List;

import org.hibernate.SessionFactory;

import omis.education.report.EducationDocumentSummary;
import omis.education.report.EducationSummary;
import omis.education.report.EducationSummaryReportService;
import omis.offender.domain.Offender;

/**
 * EducationSummaryReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class EducationSummaryReportServiceHibernateImpl 
	implements EducationSummaryReportService {
	
	/* Queries. */
	private static final String FIND_EDUCATION_SUMMARIES_BY_OFFENDER
		= "findEducationSummariesByOffender";
	
	private static final String FIND_EDUCATION_DOCUMENT_SUMMARIES_BY_OFFENDER
		= "findEducationDocumentSummariesByOffender";
	
	/* Parameters. */
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	/* Members. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor
	 * @param sessionFactory - session factory
	 */
	public EducationSummaryReportServiceHibernateImpl(
			SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<EducationSummary> findByOffender(Offender offender) {
		@SuppressWarnings("unchecked")
		List<EducationSummary> educationSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_EDUCATION_SUMMARIES_BY_OFFENDER)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return educationSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<EducationDocumentSummary>
			findEducationDocumentSummariesByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<EducationDocumentSummary> educationDocumentSummaries = this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_EDUCATION_DOCUMENT_SUMMARIES_BY_OFFENDER)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return educationDocumentSummaries;
	}
}