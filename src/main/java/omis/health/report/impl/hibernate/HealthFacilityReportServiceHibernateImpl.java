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
package omis.health.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.facility.domain.Facility;
import omis.health.report.HealthFacilityReportService;
import omis.person.domain.Person;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for health facilities.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class HealthFacilityReportServiceHibernateImpl
		implements HealthFacilityReportService {
	
	/* Query names. */
	
	private static final String FIND_FOR_STAFF_ON_DATE_QUERY_NAME
		= "findHealthFacilitiesForStaffMemberOnDate";
	
	private static final String FIND_ALL_QUERY_NAME
		= "findHealthFacilities";
	
	/* Parameter names. */
	private static final String STAFF_MEMBER_PARAM_NAME = "staffMember";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates Hibernate implementation of report service for health
	 * facilities.
	 * 
	 * @param sessionFactory session factory
	 */
	public HealthFacilityReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<Facility> findHealthFacilitiesForStaff(
			final Person staffMember, final Date date) {
		@SuppressWarnings("unchecked")
		List<Facility> facilities = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_FOR_STAFF_ON_DATE_QUERY_NAME)
				.setParameter(STAFF_MEMBER_PARAM_NAME, staffMember)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return facilities;
	}

	/** {@inheritDoc} */
	@Override
	public List<Facility> findHealthFaciliites() {
		@SuppressWarnings("unchecked")
		List<Facility> facilities = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_ALL_QUERY_NAME)
				.setReadOnly(true)
				.list();
		return facilities;
	}
}