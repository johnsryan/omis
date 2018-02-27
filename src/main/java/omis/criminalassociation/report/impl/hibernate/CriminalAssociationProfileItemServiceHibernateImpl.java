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
package omis.criminalassociation.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.criminalassociation.report.CriminalAssociationProfileItemService;
import omis.offender.domain.Offender;

/** 
 * Hibernate implementation of criminal association profile item service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class CriminalAssociationProfileItemServiceHibernateImpl 
	implements CriminalAssociationProfileItemService {
	private static final String 
	FIND_COUNTS_BY_OFFENDER_AND_DATE_QUERY_NAME
	= "findCriminalAssociationCountByOffenderAndDate";
	private static final String EFFECTIVE_DATE_PARAM_NAME 
		= "effectiveDate";
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
	
	/** Constructor.
	 * @param sessionFactory - session factory. */
	public CriminalAssociationProfileItemServiceHibernateImpl(
			final SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer findCriminalAssociationCountByOffenderAndDate(
			final Offender offender, final Date effectiveDate) {
		return ((Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_COUNTS_BY_OFFENDER_AND_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.uniqueResult()).intValue();
	}
}
