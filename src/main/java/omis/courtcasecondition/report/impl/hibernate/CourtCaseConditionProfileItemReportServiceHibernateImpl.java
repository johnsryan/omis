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
package omis.courtcasecondition.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.courtcasecondition.report.CourtCaseConditionProfileItemReportService;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of court case condition profile item report service.
 * 
 * @author Trevor Isles
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class CourtCaseConditionProfileItemReportServiceHibernateImpl 
	implements CourtCaseConditionProfileItemReportService {
	
	private static final String FIND_COUNT_BY_OFFENDER_QUERY_NAME 
		= "findCourtCaseConditionsCountByOffenderAndEffectiveDate";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor
	 * @param sessionFactory - session factory.
	 */
	public CourtCaseConditionProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer findCourtCaseConditionsByOffenderAndEffectiveDate(
			final Offender offender, final Date date) {
		return Integer.valueOf(((Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_COUNT_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(EFFECTIVE_DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult()).intValue());
	}
}