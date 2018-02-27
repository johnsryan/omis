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
package omis.caseload.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.caseload.domain.CaseWorkerAssignment;
import omis.caseload.domain.Caseload;
import omis.caseload.domain.CaseworkCategory;
import omis.caseload.domain.OffenderCaseAssignment;
import omis.caseload.report.CaseloadReportService;
import omis.caseload.report.CaseworkSummary;
import omis.caseload.report.OffenderCaseAssignmentSummary;
import omis.caseload.service.delegate.CaseWorkerAssignmentDelegate;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.search.util.PersonRegexUtility;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Hibernate implementation of the caseload report service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class CaseloadReportServiceHibernateImpl 
				implements CaseloadReportService {
	
	/* Queries. */
	private static final String FIND_BY_CASELOAD_CATEGORY_OFFENDER_AND_DATE
					= "findCaseWorkerAssignmentByCaseloadCategoryOffenderDate";
	
	private static final String 
					FIND_CSE_WRK_BY_CSE_WRKRS_EFFCTVE_DTE_QRY_NAME 
						= "findCaseWorkByCaseWorkerAndEffectiveDate";
	private static final String SUMMARIZE_CASE_WORK_QUERY_NAME 
					= "summarizeCaseWorkByCaseWorkerAssignment";	
	private static final String FIND_OFFENDER_CASE_ASSIGNMENT_QUERY_NAME
					= "findOffenderCaseAssignmentByCaseload";
	private static final String 
					FIND_CASE_WORK_SUMMARY_BY_OFFENDER_DATE_QUERY_NAME
					= "findCaseWorkSummariesByOffenderAndDate";
	private static final String FIND_CASE_WORK_SUMMARY_BY_USER_NAME_QUERY_NAME
					= "findCaseWorkSummaryByUserName";
	private static final String 
					FIND_CASE_WORKER_SUMMARIES_BY_CASELOAD_NAME_QUERY_NAME
					= "findCaseWorkerSummariesByCase";
	private static final String
					FIND_CASE_WRKR_SUMMARIES_BY_FRST_LAST_NAME_QUERY_NAME
					= "findCaseWorkerSummariesByFirstLastName";
	private static final String
					FIND_CASE_WRKR_SUMMRS_BY_FIRST_MIDDLE_LAST_QUERY_NAME
					= "findCaseWorkerSummariesByFirstMiddleLastName";
	private static final String COUNT_CASELOAD_CONTACTS_QUERY_NAME = 
			"countCaseloadContacts";
	
	/* Parameters. */
	private static final String PERSON_PARAMETER_NAME = "worker";

	private static final String EFFECTIVE_DATE_PARAMETER_NAME = "effectiveDate";
	private static final String CASELOAD_PARAMETER_NAME = "caseload";
	private static final String CASE_WORKER_ASSIGNMENT_PARAMETER_NAME
					= "caseWorkerAssignment";
	private static final String LAST_NAME_PARAM_NAME = "lastName";
	private static final String FIRST_NAME_PARAM_NAME = "firstName";
	private static final String MIDDLE_NAME_PARAM_NAME = "middleName";
	private static final String USERNAME_PARAM_NAME = "username";
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final String CASEWORK_CATEGORY_PARAM_NAME = "category";
	private static final String OFFENDER_CASE_ASSIGNMENT_PARAM_NAME = 
			"offenderCaseAssignment";
	
	/* REGEX */
	private static final String WHITE_SPACE = "[\\s,]+";
	
	/* Members. */
	private final SessionFactory sessionFactory;
	private final UserAccountDelegate userAccountDelegate;
	private final CaseWorkerAssignmentDelegate caseWorkerAssignmentDelegate;
	
	/**
	 * Constructor.
	 * @param caseWorkerAssignmentDelegate - case worker assignment delegate.
	 * @param sessionFactory session factory
	 * @param userAccountDelegate user account delegate.
	 */
	public CaseloadReportServiceHibernateImpl(
					final SessionFactory sessionFactory, 
					final CaseWorkerAssignmentDelegate 
						caseWorkerAssignmentDelegate,
					final UserAccountDelegate userAccountDelegate) {
		this.sessionFactory = sessionFactory;
		this.caseWorkerAssignmentDelegate = caseWorkerAssignmentDelegate;
		
		this.userAccountDelegate = userAccountDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseworkSummary> findByCaseWorkerAndDate(final Person person, 
					final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
								FIND_CSE_WRK_BY_CSE_WRKRS_EFFCTVE_DTE_QRY_NAME)
						.setParameter(PERSON_PARAMETER_NAME, person)
						.setDate(EFFECTIVE_DATE_PARAMETER_NAME, 
								effectiveDate)
						.setReadOnly(true)
						.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public CaseworkSummary summarize(
					final CaseWorkerAssignment caseWorkerAssignment) {
		CaseworkSummary caseWorkSummary = (CaseworkSummary)
						this.sessionFactory.getCurrentSession()
						.getNamedQuery(SUMMARIZE_CASE_WORK_QUERY_NAME)
						.setParameter(CASE_WORKER_ASSIGNMENT_PARAMETER_NAME, 
								caseWorkerAssignment)
						.setReadOnly(true)
						.uniqueResult();
		return caseWorkSummary;
	}

	/** {@inheritDoc} */
	@Override
	public List<OffenderCaseAssignmentSummary> 
					findOffenderCaseAssignmentSummariesByCaseloadAndDate(
							final Caseload caseload, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<OffenderCaseAssignmentSummary> offenderCaseAssignmentSummary = 
						this.sessionFactory.getCurrentSession()
						.getNamedQuery(FIND_OFFENDER_CASE_ASSIGNMENT_QUERY_NAME)
						.setParameter(CASELOAD_PARAMETER_NAME, caseload)
						.setParameter(EFFECTIVE_DATE_PARAMETER_NAME, 
								effectiveDate)
						.setReadOnly(true)
						.list();
		return offenderCaseAssignmentSummary;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean hasCaseloadContact(
					final OffenderCaseAssignment offenderCaseAssignment) {
		final Boolean query = (Boolean) this.sessionFactory.getCurrentSession()
						.getNamedQuery(COUNT_CASELOAD_CONTACTS_QUERY_NAME)
						.setParameter(OFFENDER_CASE_ASSIGNMENT_PARAM_NAME, 
								offenderCaseAssignment)
						.setReadOnly(true)
						.uniqueResult();
		return query;
	}

	/** {@inheritDoc} */
	@Override
	public List<CaseworkSummary> findCaseWorkerSummariesByCase(
					final Caseload caseload, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		final List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
								FIND_CASE_WORKER_SUMMARIES_BY_CASELOAD_NAME_QUERY_NAME)
						.setParameter(CASELOAD_PARAMETER_NAME, caseload)
						.setParameter(EFFECTIVE_DATE_PARAMETER_NAME, 
								effectiveDate)
						.setReadOnly(true)
						.list();
				
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount findUserAccountByUsername(final String username) {
		return this.userAccountDelegate.findByUsername(username);
	}
	
	@Override
	public CaseWorkerAssignment findSupervisoryCaseWorkerAssignmentBy(
					final Person caseWorker, final Date effectiveDate) {
		return this.caseWorkerAssignmentDelegate
					.findSupervisoryCaseWorkerAssignment(caseWorker, 
							effectiveDate);
	}

	/** {@inheritDoc} */
	@Override
	public CaseworkSummary 
					findSupervisoryCaseWorkerAssignmentByOffenderAndDate(
						final Offender offender, final Date effectiveDate) {
		return (CaseworkSummary) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_BY_CASELOAD_CATEGORY_OFFENDER_AND_DATE)
				.setParameter(CASEWORK_CATEGORY_PARAM_NAME, 
						CaseworkCategory.SUPERVISORY)
				.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<CaseworkSummary> findCaseworkSummariesBySearchCriteria(
					final String searchQuery, final Date effectiveDate) {
		final List<CaseworkSummary> caseworkSummaries;
		if (PersonRegexUtility.isUserName(searchQuery)) {
			caseworkSummaries = this.findCaseWorkerSummariesByUsername(
					searchQuery, effectiveDate);
		} else if (PersonRegexUtility.isFirstLast(searchQuery)) {
			final String[] names = searchQuery.split(WHITE_SPACE);
			caseworkSummaries = this.findCaseworkerSummariesByFirstLast(
								names[0], names[1], effectiveDate);
		} else if (PersonRegexUtility.isFirstMiddleLast(searchQuery)) {
			final String[] names = searchQuery.split(WHITE_SPACE);
			caseworkSummaries = this.findCaseworkerSummariesByFirstMiddleLast(
								names[0], names[1], names[2], effectiveDate);
		} else {
			caseworkSummaries = null;
		}
		return caseworkSummaries;
	}
	
	private List<CaseworkSummary> findCaseWorkerSummariesByUsername(
					final String username, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		final List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
								FIND_CASE_WORK_SUMMARY_BY_USER_NAME_QUERY_NAME)
						.setParameter(USERNAME_PARAM_NAME, username)
						.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
						.setReadOnly(true)
						.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<CaseworkSummary> findCasewokerAssignmentsByOffenderAndDate(
			final Offender offender, final Date effectiveDate) {
		return this.cast(this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_CASE_WORK_SUMMARY_BY_OFFENDER_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list());
	}
	
	
	private List<CaseworkSummary> findCaseworkerSummariesByFirstLast(
					final String firstName, final String lastName, 
					final Date effectiveDate) {
		List<CaseworkSummary> caseworkSummaries = this.cast(this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_CASE_WRKR_SUMMARIES_BY_FRST_LAST_NAME_QUERY_NAME)
				.setString(FIRST_NAME_PARAM_NAME, firstName)
				.setString(LAST_NAME_PARAM_NAME, lastName)
				.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list());
		return caseworkSummaries;
	} 
	
	private List<CaseworkSummary> findCaseworkerSummariesByFirstMiddleLast(
					final String firstName, final String middleName, 
					final String lastName, final Date effectiveDate) {
		final List<CaseworkSummary> caseworkSummaries = this.cast(this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_CASE_WRKR_SUMMRS_BY_FIRST_MIDDLE_LAST_QUERY_NAME)
				.setString(FIRST_NAME_PARAM_NAME, firstName)
				.setString(MIDDLE_NAME_PARAM_NAME, middleName)
				.setString(LAST_NAME_PARAM_NAME, lastName)
				.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list());
		return caseworkSummaries;
	}
	
	// Casts list.
	@SuppressWarnings("unchecked")
	private <T> List<T> cast(final List<?> objs) {
		return  (List<T>) objs;
	}
	
	/*private static final String FIND_SUPERVISOR_CASE_WORK_QUERY_NAME
					= "findSupervisoryCaseWorkByCaseWorkerAndDate";*/
	/*private static final String 
					FIND_CASE_WORK_BY_CASE_WORKERS_AND_DATES_QUERY_NAME 
					= "findCaseWorkByCaseWorkerAndDates";*/
	/*private static final String FIND_CASE_WORK_SUMMARY_BY_LAST_NAME_QUERY_NAME
					= "findCaseWorkSummaryByLastName";*/
/*	private static final String 
					FIND_CASE_WORK_SUMMARY_BY_LAST_FIRST_NAME_QUERY_NAME
					= "findCaseWorkSummaryByLastFirstName";*/
/*	private static final String 
					FIND_CASE_WORK_SUMMARY_BY_CASELOAD_NAME_QUERY_NAME
					= "findCaseWorkSummaryByCaseloadName";*/
/*	private static final String 
					FIND_OFFENDER_CASE_ASSIGNMENT_BY_DATE_RANGE_QUERY_NAME
					= "findOffenderCaseAssignmentByCaseloadAndDateRange";*/
/*	private static final String FIND_CONTACT_SUMMARY_BY_OFFENDFER_QUERY_NAME
					= "findContactSummaryByOffender";*/
	
	
/*	private static final String START_DATE_PARAMETER_NAME = "startDate";
	private static final String END_DATE_PARAMETER_NAME = "endDate";*/
/*	private static final String CASE_WORK_CATEGORY_PARAMETER_NAME 
					= "caseWorkCategory";*/	
/*	private static final String CASELOAD_NAME_PARAMETER_NAME = "caseloadName";*/
/*	private static final String CASELOAD_NAME_PARAM_NAME = "caseloadName";*/
	
	/**  
	@Override
	public List<CaseworkSummary> findByCaseWorkerAndDates(
					final Person person, final Date startDate, 
					final Date endDate,
					final String caseloadName, 
					final CaseworkCategory caseWorkCategory) {
		@SuppressWarnings("unchecked")
		List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
						FIND_CASE_WORK_BY_CASE_WORKERS_AND_DATES_QUERY_NAME)
						.setParameter(PERSON_PARAMETER_NAME, person)
						.setTimestamp(START_DATE_PARAMETER_NAME, startDate)
						.setTimestamp(END_DATE_PARAMETER_NAME, endDate)
						.setParameter(CASELOAD_NAME_PARAMETER_NAME, 
								caseloadName)
						.setParameter(
						CASE_WORK_CATEGORY_PARAMETER_NAME, caseWorkCategory)
						.list();
		return summaries;
	} */
	
	/**  
	@Override
	public CaseworkSummary findSupervisoryCaseWorkByCaseWorkerAndDate(
					final Person person, final Date effectiveDate) {
		CaseworkSummary summary = (CaseworkSummary) this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(FIND_SUPERVISOR_CASE_WORK_QUERY_NAME)
						.setParameter(PERSON_PARAMETER_NAME, person)
						.setDate(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
						.uniqueResult();
		return summary;
	} */
	
	/**  
	@Override
	public List<CaseworkSummary> findCaseWorkSummaryByLastName(
					final String lastName, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		final List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
								FIND_CASE_WORK_SUMMARY_BY_LAST_NAME_QUERY_NAME)
						.setParameter(LAST_NAME_PARAM_NAME, lastName)
						.setDate(EFFECTIVE_DATE_PARAMETER_NAME, 
								effectiveDate).list();
		
		return summaries;
	} */

	/**  
	@Override
	public List<CaseworkSummary> findCaseWorkSummaryByLastFirstName(
					final String lastName, final String firstName, 
					final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		final List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
						FIND_CASE_WORK_SUMMARY_BY_LAST_FIRST_NAME_QUERY_NAME)
							.setParameter(LAST_NAME_PARAM_NAME, lastName)
							.setParameter(FIRST_NAME_PARAM_NAME, firstName)
							.setDate(EFFECTIVE_DATE_PARAMETER_NAME, 
									effectiveDate).list();
		
		return summaries;
	} */

	/**  
	@Override
	public List<CaseworkSummary> findCaseWorkerSummaryByCaseloadName(
					final String caseloadName, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		final List<CaseworkSummary> summaries = this.sessionFactory
						.getCurrentSession()
						.getNamedQuery(
						FIND_CASE_WORK_SUMMARY_BY_CASELOAD_NAME_QUERY_NAME)
							.setParameter(CASELOAD_NAME_PARAM_NAME, 
									caseloadName)
							.setDate(EFFECTIVE_DATE_PARAMETER_NAME, 
									effectiveDate).list();
		return summaries;
	} */

	/**  
	@Override
	public List<OffenderCaseAssignmentSummary> 	
					findOffenderCaseAssignmentSummariesByCaseloadAndDateRange(
								final Caseload caseload, final Date startDate, 
								final Date endDate) {
		@SuppressWarnings("unchecked")
		final List<OffenderCaseAssignmentSummary> summaries 
						= this.sessionFactory.getCurrentSession().getNamedQuery(
						FIND_OFFENDER_CASE_ASSIGNMENT_BY_DATE_RANGE_QUERY_NAME)
							.setParameter(CASELOAD_PARAMETER_NAME, caseload)
							.setTimestamp(START_DATE_PARAMETER_NAME, startDate)
							.setTimestamp(END_DATE_PARAMETER_NAME, endDate)
							.list();
		
		return summaries;
	} */

	/**  
	@Override
	public List<ContactSummary> findContactSummaryByOffender(
					final Offender offender, final Caseload caseload) {
		@SuppressWarnings("unchecked")
		final List<ContactSummary> summaries 
						= this.sessionFactory.getCurrentSession()
						.getNamedQuery(
							FIND_CONTACT_SUMMARY_BY_OFFENDFER_QUERY_NAME)
							.setParameter(OFFENDER_PARAM_NAME, offender)
							.setParameter(CASELOAD_PARAMETER_NAME, caseload)
							.list();
			
		return summaries;
	} */
	
	/** 
	@Override
	public List<CaseWorkerAssignment> 
					findCaseWorkerAssignmentsByCaseload(
							final Caseload caseload, final Date effectiveDate) {
		return this.caseWorkerAssignmentDelegate
				.findCaseWorkerAssignmentsByCaseload(
				caseload, effectiveDate);
	} */
}