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
package omis.offenderrelationship.report.impl.hibernate;

import java.util.Date;
import java.util.List;
import omis.offenderrelationship.report.OffenderRelationshipReportService;
import omis.offenderrelationship.report.OffenderRelationshipSummary;
import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of service to report offender relationships
 * summaries.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class OffenderRelationshipReportServiceHibernateImpl
		implements OffenderRelationshipReportService {
	
	/* Query names. */
	
	private static final String 
		SUMMARIZE_BY_FIRSTNAME_LASTNAME_APPROPXIMATE_QUERY_NAME
		= "summarizeOffenderRelationshipsByFirstNameLastNameAppropximate";
	
	private static final String SUMMARIZE_BY_FIRSTNAME_LASTNAME_EXACT_QUERY_NAME
		= "summarizeOffenderRelationshipsByFirstNameLastNameExact";
	
	private static final String SUMMARIZE_BY_OFFENDER_NUMBER_QUERY_NAME
		= "summarizeOffenderRelationshipsByOffenderNumber";
	
	private static final String SUMMARIZE_BY_SOCIAL_SECURITY_NUMBER_QUERY_NAME
		= "summarizeOffenderRelationshipsBySocialSecurityNumber";
	
	private static final String SUMMARIZE_BY_BIRTH_DATE_QUERY_NAME
		= "summarizeOffenderRelationshipsByBirthDate";
	
	private static final String 
		COUNT_APPROPRIMATE_BY_FIRSTNAME_LASTNAME_QUERY_NAME
		= "countApproximateByFirstNameAndLastName";
	
	private static final String COUNT_EXACT_BY_FIRSTNAME_LASTNAME_QUERY_NAME
		= "countExactByFirstNameAndLastName";
	
	private static final String COUNT_APPROPRIMATE_BY_LASTNAME_QUERY_NAME
		= "countApproximateByLastName";
	
	private static final String COUNT_APPROPRIMATE_BY_FIRSTNAME_QUERY_NAME
		= "countApproximateByFirstName";
	
	private static final String COUNT_EXACT_BY_FIRSTNAME_QUERY_NAME
		= "countExactByFirstName";
	
	private static final String COUNT_EXACT_BY_LASTNAME_QUERY_NAME
		= "countExactByLastName";
	
	private static final String SUMMARIZE_BY_FIRSTNAME_APPROPXIMATE_QUERY_NAME
		= "summarizeOffenderRelationshipsByFirstNameAppropximate";
	
	private static final String SUMMARIZE_BY_LASTNAME_APPROPXIMATE_QUERY_NAME
		= "summarizeOffenderRelationshipsByLastNameAppropximate";
	
	private static final String SUMMARIZE_BY_LASTNAME_EXACT_QUERY_NAME
		= "summarizeOffenderRelationshipsByLastNameExact";
	
	private static final String SUMMARIZE_BY_FIRSTNAME_EXACT_QUERY_NAME
		= "summarizeOffenderRelationshipsByFirstNameExact";
		
	
	/* Parameter names. */
	
	private static final String LAST_NAME_PARAM_NAME = "lastName";
	
	private static final String FIRST_NAME_PARAM_NAME = "firstName";

	private static final String OFFENDER_NUMBER_PARAM_NAME = "offenderNumber";
	
	private static final String BIRTH_DATE_PARAM_NAME = "birthDate";
	
	private static final String SOCIAL_SECURITY_NUMBER_PARAM_NAME
		= "socialSecurityNumber";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	private final Integer maximumResults;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of service to report offender
	 * relationship summaries. 
	 * 
	 * @param sessionFactory session factory
	 * @param maximumResults maximum results 
	 */
	public OffenderRelationshipReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final Integer maximumResults) {
		this.sessionFactory = sessionFactory;
		this.maximumResults = maximumResults;
	}

	/* Method implementation. */
	
	/** {@inheritDoc} */
	@Override
	public List<OffenderRelationshipSummary> summarizeByName(
			final String lastName, final String firstName,
			final Date effectiveDate, final Boolean approximateMatch) {
		String queryName;
		if (approximateMatch) {
			if ((lastName != null && !lastName.isEmpty())
				&& (firstName != null && !firstName.isEmpty())) {
				queryName 
					= SUMMARIZE_BY_FIRSTNAME_LASTNAME_APPROPXIMATE_QUERY_NAME;
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries = 
					this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			} else if ((lastName != null && !lastName.isEmpty())
				&& (firstName == null || firstName.isEmpty())) {
				queryName = SUMMARIZE_BY_LASTNAME_APPROPXIMATE_QUERY_NAME;	
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries = 
					this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			} else {
				queryName = SUMMARIZE_BY_FIRSTNAME_APPROPXIMATE_QUERY_NAME;
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries = 
					this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			}
		} else {
			if (((lastName != null && !lastName.isEmpty())
				&& (firstName != null && !firstName.isEmpty()))) {
				queryName = SUMMARIZE_BY_FIRSTNAME_LASTNAME_EXACT_QUERY_NAME;
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries 
					= this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			} else if	((lastName != null && !lastName.isEmpty())
				&& (firstName == null || firstName.isEmpty())) {
				queryName = SUMMARIZE_BY_LASTNAME_EXACT_QUERY_NAME;	
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries 
					= this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			} else {
				queryName = SUMMARIZE_BY_FIRSTNAME_EXACT_QUERY_NAME;
				@SuppressWarnings("unchecked")
				List<OffenderRelationshipSummary> summaries 
					= this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
					.setReadOnly(true)
					.list();
				return summaries;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<OffenderRelationshipSummary> summarizeByOffenderNumber(
			final Integer offenderNumber, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<OffenderRelationshipSummary> summaries = this.sessionFactory
			.getCurrentSession().getNamedQuery(
					SUMMARIZE_BY_OFFENDER_NUMBER_QUERY_NAME)
			.setParameter(OFFENDER_NUMBER_PARAM_NAME, offenderNumber)
			.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.setReadOnly(true)
			.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<OffenderRelationshipSummary> summarizeBySocialSecurityNumber(
			final Integer socialSecurityNumber, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<OffenderRelationshipSummary> summaries = this.sessionFactory
			.getCurrentSession().getNamedQuery(
					SUMMARIZE_BY_SOCIAL_SECURITY_NUMBER_QUERY_NAME)
			.setParameter(SOCIAL_SECURITY_NUMBER_PARAM_NAME,
					socialSecurityNumber)
			.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.setReadOnly(true)
			.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<OffenderRelationshipSummary> summarizeByBirthDate(
			final Date birthDate, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<OffenderRelationshipSummary> summaries = this.sessionFactory
			.getCurrentSession().getNamedQuery(
					SUMMARIZE_BY_BIRTH_DATE_QUERY_NAME)
			.setTimestamp(BIRTH_DATE_PARAM_NAME, birthDate)
			.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.setReadOnly(true)
			.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer countByPerson(final String lastName,
		final String firstName, final Boolean approximateMatch) {
		String queryName;
		if (approximateMatch) {
			if ((lastName != null && !lastName.isEmpty())
				&& (firstName != null && !firstName.isEmpty())) {
				queryName = COUNT_APPROPRIMATE_BY_FIRSTNAME_LASTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			} else if ((lastName != null && !lastName.isEmpty())
				&& (firstName == null || firstName.isEmpty())) {
				queryName = COUNT_APPROPRIMATE_BY_LASTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(
						COUNT_APPROPRIMATE_BY_LASTNAME_QUERY_NAME)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			} else {
				queryName = COUNT_APPROPRIMATE_BY_FIRSTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			}
		} else {
			if ((lastName != null && !lastName.isEmpty())
				&& (firstName != null && !firstName.isEmpty())) {
				queryName = COUNT_EXACT_BY_FIRSTNAME_LASTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			} else if ((lastName != null && !lastName.isEmpty())
				&& (firstName == null || firstName.isEmpty())) {
				queryName = COUNT_EXACT_BY_LASTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(LAST_NAME_PARAM_NAME, lastName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			} else {
				queryName = COUNT_EXACT_BY_FIRSTNAME_QUERY_NAME;
				Long count = (Long) this.sessionFactory
					.getCurrentSession().getNamedQuery(queryName)
					.setParameter(FIRST_NAME_PARAM_NAME, firstName)
					.setReadOnly(true)
					.uniqueResult();
				return count.intValue();
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer getMaximumResults() {
		return this.maximumResults;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean exceedsMaximumResults(final String lastName, 
		final String firstName, final Boolean approximateMatch) {
		return countByPerson(lastName, firstName, approximateMatch) 
			> 
			this.maximumResults;
	}
}