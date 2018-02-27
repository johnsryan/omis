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
package omis.employment.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.employment.report.EmployerSearchSummary;
import omis.employment.report.EmployerSearchSummaryReportService;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Implementation of employer summary report service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class EmployerSearchSummaryReportServiceHibernateImpl 
	implements EmployerSearchSummaryReportService {

	private final SessionFactory sessionFactory;

	/** Constructor. */
	public EmployerSearchSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		@SuppressWarnings("unchecked")
		List<City> cities = this.sessionFactory.getCurrentSession()
				.getNamedQuery("findEmployerCitiesByState")
				.setParameter("state", state)
				.setReadOnly(true)
				.list();
		return cities;
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findAllStates() {
		@SuppressWarnings("unchecked")
		List<State> states = this.sessionFactory.getCurrentSession()
				.getNamedQuery("findAllStates")
				.setReadOnly(true)
				.list();
		return states;
	}

	/** {@inheritDoc} */
	@Override
	public List<EmployerSearchSummary> search(
			final String employerLocationOrganizationName, final City city, 
			final State state, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<EmployerSearchSummary> employerSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery("searchForEmployer")
				.setParameter("employerLocationOrganizationName", 
						employerLocationOrganizationName)
				.setParameter("city", city)
				.setParameter("state", state)
				.setDate("effectiveDate", effectiveDate)
				.setReadOnly(true)
				.list();
		
		return employerSummaries;
	}
}