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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import omis.facility.domain.Facility;
import omis.health.domain.HealthAppointmentStatus;
import omis.health.domain.HealthRequest;
import omis.health.exception.HealthRequestFollowsUpMultipleReferralsException;
import omis.health.report.ReferralSummary;
import omis.health.report.ReferralSummaryReportService;
import omis.health.report.ReferralType;
import omis.health.report.delegate.UnitLookUpDelegate;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for referral summaries.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ReferralSummaryReportServiceHibernateImpl
		implements ReferralSummaryReportService {

	/* Queries. */
	
	private final static String FIND_INTERNAL_FOR_ACTION_QUERY_NAME
		= "findInternalReferralSummaryForActionRequest";

	private final static String FIND_EXTERNAL_FOR_ACTION_QUERY_NAME
		= "findExternalReferralSummaryForActionRequest";
	
	private static final String FIND_INTERNAL_BY_FACILITY_QUERY_NAME
		= "findInternalReferralSummariesByFacility";
	
	private static final String FIND_INTERNAL_BY_OFFENDER_QUERY_NAME
		= "findInternalReferralSummariesByOffender";
	
	private static final String FIND_EXTERNAL_BY_FACILITY_QUERY_NAME
		= "findExternalReferralSummariesByFacility";

	private static final String FIND_EXTERNAL_BY_OFFENDER_QUERY_NAME
		= "findExternalReferralSummariesByOffender";
	
	private static final String FIND_LAB_WORKS_BY_OFFENDER_QUERY_NAME
		= "findLabWorksByOffender";
	
	private static final String FIND_LAB_WORKS_BY_FACILITY_QUERY_NAME
		="findLabWorksByFacility";
	
	/* Parameters. */
	
	private final static String ACTION_REQUEST_PARAM_NAME = "actionRequest";
	
	private static final String FACILITY_PARAM_NAME = "facility";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String START_DATE_PARAM_NAME = "startDate";

	private static final String END_DATE_PARAM_NAME = "endDate";

	private static final String APPOINTMENT_STATUSES_PARAM_NAME
		= "appointmentStatuses";

	private static final String APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME
		= "appointmentStatusesContainsNull";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Helpers. */
	
	private final UnitLookUpDelegate unitLookUpDelegate;
	
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of report service for referral
	 * summaries.
	 * 
	 * @param sessionFactory session factory
	 * @param unitLookUpDelegate delegate to look up units
	 */
	public ReferralSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final UnitLookUpDelegate unitLookUpDelegate) {
		this.sessionFactory = sessionFactory;
		this.unitLookUpDelegate = unitLookUpDelegate;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public ReferralSummary findInternalForActionRequest(
			final HealthRequest actionRequest,
			final Date effectiveDate) {
		ReferralSummary referralSummary = (ReferralSummary)
			this.sessionFactory.getCurrentSession().getNamedQuery(
				FIND_INTERNAL_FOR_ACTION_QUERY_NAME)
			.setParameter(ACTION_REQUEST_PARAM_NAME, actionRequest)
			.setReadOnly(true)
			.uniqueResult();
		if (referralSummary != null) {
			Field field = this.getUnitNameField();
			String unitName = this.unitLookUpDelegate
					.findUnitAbbreviationByOffenderNumber(
							referralSummary.getOffenderNumber(), effectiveDate);
			this.setField(field, referralSummary, unitName);
		}
		return referralSummary;
	}
	
	/** {@inheritDoc} */
	@Override
	public ReferralSummary findExternalForActionRequest(
			HealthRequest actionRequest, Date effectiveDate) {
		ReferralSummary referralSummary = (ReferralSummary)
				this.sessionFactory.getCurrentSession().getNamedQuery(
					FIND_EXTERNAL_FOR_ACTION_QUERY_NAME)
				.setParameter(ACTION_REQUEST_PARAM_NAME, actionRequest)
				.setReadOnly(true)
				.uniqueResult();
			if (referralSummary != null) {
				Field field = this.getUnitNameField();
				String unitName = this.unitLookUpDelegate
						.findUnitAbbreviationByOffenderNumber(
								referralSummary.getOffenderNumber(),
								effectiveDate);
				this.setField(field, referralSummary, unitName);
			}
			return referralSummary;
	}

	/** {@inheritDoc} */
	@Override
	public ReferralSummary findForActionRequest(
			final HealthRequest actionRequest, final Date effectiveDate)
					throws HealthRequestFollowsUpMultipleReferralsException {
		ReferralSummary referralSummary = this.findInternalForActionRequest(
				actionRequest, effectiveDate);
		ReferralSummary externalReferralSummary
			= this.findExternalForActionRequest(actionRequest, effectiveDate);
		if (referralSummary != null && externalReferralSummary != null) {
			throw new HealthRequestFollowsUpMultipleReferralsException(
					"Request is follow up to more than one referral");
		} else if (referralSummary == null && externalReferralSummary != null) {
			referralSummary = externalReferralSummary;
		}
		/*
		 * TODO: Add labs work referral summary - SA
		 * 
		 * Note: when labs are added, execute something like the following:
		 * 
		 * labWorkReferralSummary = findForLabWorkReferral(request, date)
		 * if referralSummary is not null and labWorkReferralSummary is not
		 * <<null then
		 *   begin
		 *   throw DuplicateEntityFoundException
		 *   end
		 * else if referralSummary is null and labWorkReferralSummary is not
		 * << null then
		 *   begin
		 *   referralSummary = labWorkReferralSummary
		 *   end
		 * 
		 * - SA
		 */
		return referralSummary;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ReferralSummary> findByFacility(
			final Facility facility, final Date startDate, final Date endDate,
			final ReferralType[] types,
			final HealthAppointmentStatus[] statuses,
			final Date effectiveDate) {
		List<ReferralSummary> summaries = new ArrayList<ReferralSummary>();
		for (ReferralType referralType : types) {
			if (ReferralType.INTERNAL_MEDICAL.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> internalSummaries = this.sessionFactory
					.getCurrentSession().getNamedQuery(
							FIND_INTERNAL_BY_FACILITY_QUERY_NAME)
					.setParameter(FACILITY_PARAM_NAME, facility)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.setTimestamp(END_DATE_PARAM_NAME, endDate)
					.setBoolean(APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
							Arrays.asList(statuses).contains(null))
					.setParameterList(APPOINTMENT_STATUSES_PARAM_NAME, statuses)
					.setReadOnly(true)
					.list();
				summaries.addAll(internalSummaries);
			}
			if (ReferralType.EXTERNAL_MEDICAL.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> externalSummaries = this.sessionFactory
					.getCurrentSession().getNamedQuery(
							FIND_EXTERNAL_BY_FACILITY_QUERY_NAME)
					.setParameter(FACILITY_PARAM_NAME, facility)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.setTimestamp(END_DATE_PARAM_NAME, endDate)
					.setBoolean(APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
							Arrays.asList(statuses).contains(null))
					.setParameterList(APPOINTMENT_STATUSES_PARAM_NAME, statuses)
					.setReadOnly(true)
					.list();
				summaries.addAll(externalSummaries);
			}
			if (ReferralType.LAB.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> labWorkReferralSummaries = this
					.sessionFactory.getCurrentSession()
					.getNamedQuery(FIND_LAB_WORKS_BY_FACILITY_QUERY_NAME)
					.setParameter(FACILITY_PARAM_NAME, facility)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.setTimestamp(END_DATE_PARAM_NAME, endDate)
					.setBoolean(APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
						Arrays.asList(statuses).contains(null))
					.setParameterList(APPOINTMENT_STATUSES_PARAM_NAME, statuses)
					.setReadOnly(true)
					.list();
				summaries.addAll(labWorkReferralSummaries);
			}
		}
		addUnits(summaries, effectiveDate);
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ReferralSummary> findByOffender(
			final Offender offender, final Date startDate, final Date endDate,
			final ReferralType[] types,
			final HealthAppointmentStatus[] statuses,
			final Date effectiveDate) {
		List<ReferralSummary> summaries = new ArrayList<ReferralSummary>();
		for (ReferralType referralType : types) {
			if (ReferralType.INTERNAL_MEDICAL.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> internalSummaries = this.sessionFactory
					.getCurrentSession().getNamedQuery(
							FIND_INTERNAL_BY_OFFENDER_QUERY_NAME)
					.setParameter(OFFENDER_PARAM_NAME, offender)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.setTimestamp(END_DATE_PARAM_NAME, endDate)
					.setBoolean(APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
							Arrays.asList(statuses).contains(null))
					.setParameterList(APPOINTMENT_STATUSES_PARAM_NAME, statuses)
					.setReadOnly(true)
					.list();
				summaries.addAll(internalSummaries);
			}
			if (ReferralType.EXTERNAL_MEDICAL.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> externalSummaries = this.sessionFactory
					.getCurrentSession().getNamedQuery(
							FIND_EXTERNAL_BY_OFFENDER_QUERY_NAME)
					.setParameter(OFFENDER_PARAM_NAME, offender)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.setTimestamp(END_DATE_PARAM_NAME, endDate)
					.setBoolean(APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
							Arrays.asList(statuses).contains(null))
					.setParameterList(APPOINTMENT_STATUSES_PARAM_NAME, statuses)
					.setReadOnly(true)
					.list();
				summaries.addAll(externalSummaries);
			}
			if (ReferralType.LAB.equals(referralType)) {
				@SuppressWarnings("unchecked")
				List<ReferralSummary> labWorkReferralSummaries = 
					this.sessionFactory.getCurrentSession()
						.getNamedQuery(FIND_LAB_WORKS_BY_OFFENDER_QUERY_NAME)
						.setParameter(OFFENDER_PARAM_NAME, offender)
						.setTimestamp(START_DATE_PARAM_NAME, startDate)
						.setTimestamp(END_DATE_PARAM_NAME, endDate)
						.setBoolean(
								APPOINTMENT_STATUSES_CONTAINS_NULL_PARAM_NAME,
								Arrays.asList(statuses).contains(null))
						.setParameterList(
								APPOINTMENT_STATUSES_PARAM_NAME, statuses)
						.setReadOnly(true)
						.list();
				summaries.addAll(labWorkReferralSummaries);
			}
		}
		this.addUnits(summaries, effectiveDate);
		return summaries;
	}
	
	// Adds units
	private void addUnits(final Collection<ReferralSummary> summaries,
			final Date date) {
		final Field field = this.getUnitNameField();
		for (ReferralSummary summary : summaries) {
			final String unitName = this.unitLookUpDelegate
					.findUnitAbbreviationByOffenderNumber(
							summary.getOffenderNumber(), date);
			this.setField(field, summary, unitName);
		}
	}
		
	// Returns unit name field for referral summary
	private Field getUnitNameField() {
		final Field field;
		try {
			field = ReferralSummary.class.getDeclaredField("unitName");
		} catch (SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		field.setAccessible(true);
		return field;
	}

	// Sets field of object with value
	private void setField(final Field field, final Object object,
			final Object value) {
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}