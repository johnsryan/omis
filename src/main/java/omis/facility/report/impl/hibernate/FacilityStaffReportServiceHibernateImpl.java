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
package omis.facility.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.facility.domain.Facility;
import omis.facility.report.FacilityStaffReportService;
import omis.person.domain.Person;


/** 
 * Implementation of staff assignment report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 * @deprecated use module specific report service for facility/staff lookup;
 * consider removal - SA
 */
@Deprecated
public class FacilityStaffReportServiceHibernateImpl
	implements FacilityStaffReportService {
	private final SessionFactory sessionFactory;
	private final static String FIND_FACILITIES_BY_STAFF_QUERY =
			"findFacilitiesByStaffAndDate";
	private final static String FIND_ALL_FACILITIES =
			"FindAllFacilities";

	/** Constructor. */
	public FacilityStaffReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<Facility> findFacilitiesByStaff(final Person staff,
			final Date date) {
		@SuppressWarnings("unchecked")
		final List<Facility> result = this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_FACILITIES_BY_STAFF_QUERY)
				.setParameter("staff", staff)
				.setDate("date", date)
				.setReadOnly(true)
				.list();

		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<Facility> findAllFacilities() {
		@SuppressWarnings("unchecked")
		final List<Facility> result = this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_ALL_FACILITIES).list();

		return result;
	}
}