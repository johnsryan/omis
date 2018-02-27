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
package omis.courtdocument.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.courtdocument.report.CourtCaseDocumentAssociationSummary;
import omis.courtdocument.report
	.CourtCaseDocumentAssociationSummaryReportService;
import omis.offender.domain.Offender;

/** 
 * Hibernate implementation of court case document association summary report
 * service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class CourtCaseDocumentAssociationSummaryReportServiceHibernateImpl
		implements CourtCaseDocumentAssociationSummaryReportService {
	/* Queries */
	private static final String 
		FIND_CRT_CSE_DCMNT_ASSOC_SUM_BY_OFFENDER_QRY_NAME = 
		"findCourtCaseDocumentAssociationSummariesByOffender";
	
	/* Parameters */
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory - session factory. */
	public CourtCaseDocumentAssociationSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<CourtCaseDocumentAssociationSummary> findByOffender(
			final Offender offender) {
		return this.cast(this.sessionFactory.getCurrentSession().getNamedQuery(
				FIND_CRT_CSE_DCMNT_ASSOC_SUM_BY_OFFENDER_QRY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list());
	}
	
	/* Private cast to list of court case document associations summary. */
	@SuppressWarnings("unchecked")
	private List<CourtCaseDocumentAssociationSummary> cast(final List<?> objs) {
		return (List<CourtCaseDocumentAssociationSummary>) objs;
	}
}