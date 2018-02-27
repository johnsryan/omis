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
package omis.offender.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.offender.report.AlternativeOffenderNameProfileItemReportService;

/**
 * Hibernate implementation of report service for alternative offender name
 * profile items.
 *
 * @author Stephen Abson
 * @author Josh Divine 
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class AlternativeOffenderNameProfileItemReportServiceHibernateImpl
		implements AlternativeOffenderNameProfileItemReportService {
	
	/* Query names. */
	
	private static final String
	COUNT_BY_PERSON_AND_EFFECTIVE_DATE_QUERY_NAME
		= "countAlternativeNameAssociationForPersonOnDate";
	
	/* Parameter names. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for
	 * alternative offender name profile items.
	 * 
	 * @param sessionFactory session factory
	 */
	public AlternativeOffenderNameProfileItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementation. */
	
	/** {@inheritDoc} */
	@Override
	public Integer findAlternativeOffenderNameCountByOffenderAndDate(
			final Offender offender, final Date effectiveDate) {
		Long result = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(COUNT_BY_PERSON_AND_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.uniqueResult();
		return result.intValue();
	}
}