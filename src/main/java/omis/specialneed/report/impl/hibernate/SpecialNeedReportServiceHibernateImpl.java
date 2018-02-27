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
package omis.specialneed.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.specialneed.domain.SpecialNeed;
import omis.specialneed.report.SpecialNeedReportService;
import omis.specialneed.report.SpecialNeedSummary;

/**
 * Hibernate implementation of the special need report service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class SpecialNeedReportServiceHibernateImpl 
	implements SpecialNeedReportService {

	/* Queries. */
	private static final String
			SUMMARIZE_SPECIAL_NEED_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME = 
					"summarizeSpecialNeedByOffenderAndEffectiveDate";
	
	private static final String FIND_SPECIAL_NEEDS_QUERY_NAME = 
			"findSpecialNeeds";
	
	/* Parameters.*/ 
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	private static final String EFFECTIVE_PARAMETER_NAME = "effectiveDate";
	
	/* Members. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public SpecialNeedReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<SpecialNeedSummary> summarizeByOffender(
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<SpecialNeedSummary> specialNeedSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						SUMMARIZE_SPECIAL_NEED_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setDate(EFFECTIVE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list();

		return specialNeedSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<SpecialNeed> find() {
		@SuppressWarnings("unchecked")
		List<SpecialNeed> specialNeeds = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_SPECIAL_NEEDS_QUERY_NAME)
				.setReadOnly(true)
				.list();
		return specialNeeds;
	}
}