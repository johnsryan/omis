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
package omis.residence.report.impl.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import omis.offender.domain.Offender;
import omis.residence.domain.ResidenceTerm;
import omis.residence.report.ResidenceReportService;
import omis.residence.report.ResidenceSummary;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of the residence report service.
 * 
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ResidenceReportServiceHibernateImpl 
				implements ResidenceReportService {	

	/*	Queries. */
	private static final String FIND_RESIDENT_TERMS_BY_OFFENDER_QUERY_NAME
					= "findResidentTermsByOffenderAndEffectiveDate";
	
	private static final String FIND_NON_RESIDENT_TERMS_BY_OFFENDER_QUERY_NAME
					= "findNonResidentTermsByOffenderAndEffectiveDate";
	
	private static final String SUMMARIZE_BY_RESIDENCE_TERM 
		= "summarizeByResienceTerm";
	
	/* Parameters.*/ 
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	private static final String EFFECTIVE_PARAMETER_NAME = "effectiveDate";
	
	private static final String RESIDENCE_TERM_PARAMETER_NAME = "residenceTerm";
	
	/* Members. */
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 * @param sessionFactory session factory
	 */
	public ResidenceReportServiceHibernateImpl(
					final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<ResidenceSummary> findByOffender(final Offender offender, 
			final Date effectiveDate) {	
		@SuppressWarnings("unchecked")
		List<ResidenceSummary> residentTermSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_RESIDENT_TERMS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setDate(EFFECTIVE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		@SuppressWarnings("unchecked")
		List<ResidenceSummary> nonResidentTermSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_NON_RESIDENT_TERMS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)				
				.setDate(EFFECTIVE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		List<ResidenceSummary> residenceSummaries 
				= new ArrayList<ResidenceSummary>();
		residenceSummaries.addAll(residentTermSummaries);
		residenceSummaries.addAll(nonResidentTermSummaries);		
		Collections.sort(residenceSummaries, new Comparator<ResidenceSummary>() {
			/** {@inheritDoc} */
			@Override
			public int compare(ResidenceSummary residentTermSummary, 
					ResidenceSummary nonResidentTermSummary) {
				final int result; 
				if (residentTermSummary.getStartDate() != null && 
						nonResidentTermSummary.getStartDate() != null) {
					result = residentTermSummary.getStartDate().compareTo(
							nonResidentTermSummary.getStartDate());
				} else {
					if (residentTermSummary.getStartDate() != null) {
						result = 1;
					} else if (nonResidentTermSummary.getStartDate() != null) {
						result = -1;
					} else {
						result = 0;
					}
				}
				if (result != 0) {
					return result;
				}
				if (residentTermSummary.getEndDate() == null 
						&& nonResidentTermSummary.getEndDate() == null) {
					return 0;					
				} else if (residentTermSummary.getEndDate() != null 
						&& nonResidentTermSummary.getEndDate() == null) {
					return -1;					
				} else if (residentTermSummary.getEndDate() == null 
						&& nonResidentTermSummary.getEndDate() != null) {
					return 1;					
				} else {
					return residentTermSummary.getEndDate().compareTo(
							nonResidentTermSummary.getEndDate());
				}
			} 
		});		
		Collections.reverse(residenceSummaries);
		return residenceSummaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public ResidenceSummary summarizeByResidenceTerm(
			ResidenceTerm residenceTerm) {
		
		ResidenceSummary residenceSummary = (ResidenceSummary) 
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_RESIDENCE_TERM)
				.setParameter(RESIDENCE_TERM_PARAMETER_NAME, residenceTerm)
				.setReadOnly(true)
				.uniqueResult();
		return residenceSummary;
	}
}