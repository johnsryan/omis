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
package omis.disciplinaryCode.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.disciplinaryCode.report.DisciplinaryCodeReportService;
import omis.disciplinaryCode.report.SupervisoryOrganizationDisciplinaryCodeSummary;
import omis.disciplinaryCode.report.SupervisoryOrganizationDisciplinarySummary;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * DisciplinaryCodeReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class DisciplinaryCodeReportServiceHibernateImpl 
	implements DisciplinaryCodeReportService {
	
	/* Query names */
	
	private static final String 
		FIND_DISCIPLINARY_CODE_SUMMARIES_BY_SUPERVISORY_ORGANIZATION 
			= "findDisciplinaryCodeSummariesBySupervisoryOrganization";
	
	private static final String FIND_ALL_SUPERVISORY_ORGANIZATION_SUMMARIES
			= "findAllSupervisoryOrganizationSummaries";
	
	private static final String 
	FIND_DISCIPLINARY_CODE_SUMMARIES_WITH_DATE_RANGE_BY_SUPERVISORY_ORGANIZATION 
	= "findDisciplinaryCodeSummariesWithDateRangeBySupervisoryOrganization";
	
	/* Parameter names */
	
	private static final String SUPERVISORY_ORGANIZATION_PARAMETER_NAME 
		= "supervisoryOrganization";

	private static final String EFFECTIVE_DATE_PARAMETER_NAME = "effectiveDate";

	private static final String START_DATE_PARAMETER_NAME = "startDate";

	private static final String END_DATE_PARAMETER_NAME = "endDate";

	
	
	/* Members */
	
	private final SessionFactory sessionFactory;

	/**
	 * Constructor
	 * @param sessionFactory - session factory
	 */
	public DisciplinaryCodeReportServiceHibernateImpl(
			SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganizationDisciplinaryCodeSummary> 
		findDisciplinaryCodeSummariesBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization, 
			final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<SupervisoryOrganizationDisciplinaryCodeSummary> 
			supervisoryOrganizationDisciplinaryCodeSummaries 
				= this.sessionFactory.getCurrentSession()
					.getNamedQuery(
							FIND_DISCIPLINARY_CODE_SUMMARIES_BY_SUPERVISORY_ORGANIZATION)
					.setParameter(SUPERVISORY_ORGANIZATION_PARAMETER_NAME, 
							supervisoryOrganization)
					.setParameter(EFFECTIVE_DATE_PARAMETER_NAME, 
							effectiveDate)
					.setReadOnly(true)
					.list();
		return supervisoryOrganizationDisciplinaryCodeSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganizationDisciplinarySummary> 
		findAllSupervisoryOrganizationSummaries() {
		@SuppressWarnings("unchecked")
		List<SupervisoryOrganizationDisciplinarySummary> 
			supervisoryOrganizationDisciplinarySummaries 
				= this.sessionFactory.getCurrentSession()
					.getNamedQuery(FIND_ALL_SUPERVISORY_ORGANIZATION_SUMMARIES)
					.setReadOnly(true)
					.list();
		return supervisoryOrganizationDisciplinarySummaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<SupervisoryOrganizationDisciplinaryCodeSummary> 
	findDisciplinaryCodeSummariesBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization, 
			final Date effectiveDate, final Date startDate, 
			final Date endDate) {
		@SuppressWarnings("unchecked")
		List<SupervisoryOrganizationDisciplinaryCodeSummary> 
			supervisoryOrganizationDisciplinaryCodeSummaries 
				= this.sessionFactory.getCurrentSession()
					.getNamedQuery(
							FIND_DISCIPLINARY_CODE_SUMMARIES_WITH_DATE_RANGE_BY_SUPERVISORY_ORGANIZATION)
					.setParameter(SUPERVISORY_ORGANIZATION_PARAMETER_NAME, 
							supervisoryOrganization)
					.setParameter(EFFECTIVE_DATE_PARAMETER_NAME, 
							effectiveDate)
					.setTimestamp(START_DATE_PARAMETER_NAME, startDate)
					.setTimestamp(END_DATE_PARAMETER_NAME, endDate)
					.setReadOnly(true)
					.list();
		return supervisoryOrganizationDisciplinaryCodeSummaries;
	}
}