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
package omis.hearing.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.hearing.report.HearingProfileItemService;
import omis.hearing.report.HearingProfileItemSummary;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of hearing profile item report.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class HearingProfileItemServiceHibernateImpl 
		implements HearingProfileItemService {
	
	private final SessionFactory sessionFactory;
	
	private static final String 
		FIND_HEARING_PROFILE_ITEM_SUMMARY_BY_OFFENDER_QUERY_NAME 
		= "findHearingProfileItemSummaryByOffender";

	private static final String OFFENDER_PARAM_NAME
		= "offender";
	
	/** Instantiates an implementation of 
	 * Hearing Profile Item Service 
	 * @param sessionFactory session factory
	 */
	public HearingProfileItemServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public HearingProfileItemSummary findHearingProfileItemSummaryByOffender(
			final Offender offender) {
		return (HearingProfileItemSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_HEARING_PROFILE_ITEM_SUMMARY_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();				
	}
}