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
package omis.identificationnumber.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.identificationnumber.report.IdentificationNumberCategorySummary;
import omis.identificationnumber.report.IdentificationNumberCategorySummaryReportService;

/**
 * IdentificationNumberCategorySummaryReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class IdentificationNumberCategorySummaryReportServiceHibernateImpl
		implements IdentificationNumberCategorySummaryReportService {
	
	private static final String FIND_CATEGORY_SUMMARIES_QUERY_NAME =
			"summarizeIdentificationNumberCategories";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory
	 */
	public IdentificationNumberCategorySummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<IdentificationNumberCategorySummary>
				findAllIdentificationNumberCategorySummaries() {
		@SuppressWarnings("unchecked")
		List<IdentificationNumberCategorySummary> summaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_CATEGORY_SUMMARIES_QUERY_NAME)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}
}