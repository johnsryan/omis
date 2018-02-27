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
package omis.family.report.impl.hibernate;

import java.util.List;
import java.util.ArrayList;

import omis.family.report.FamilyAssociationReportService;
import omis.family.report.FamilyAssociationSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of offender family association report service.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationReportServiceHibernateImpl
		implements FamilyAssociationReportService {

	/* Queries */
	private static final String 
		FIND_FAMILY_ASSOCIATION_SUMMARY_BY_OFFENDER_QUERY_NAME 
		= "findFamilyAssociationSummaryByOffender";
	
	/* Parameters */
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private SessionFactory sessionFactory;
		
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of offender family association 
	 * report service.
	 * @param sessionFactory session factory
	 */

	public FamilyAssociationReportServiceHibernateImpl(final SessionFactory
		sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override 
	public List<FamilyAssociationSummary> findByOffender(
		final Offender offender) {
		List<FamilyAssociationSummary> summaries
			= new ArrayList<FamilyAssociationSummary>();
		@SuppressWarnings("unchecked")
		List<FamilyAssociationSummary> internalSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_FAMILY_ASSOCIATION_SUMMARY_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(internalSummaries); 
		return summaries;
	}
}