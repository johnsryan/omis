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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.datatype.DateRange;
import omis.facility.domain.Facility;
import omis.health.domain.IrregularScheduleDay;
import omis.health.domain.ProviderAssignment;
import omis.health.domain.ProviderAssignmentCategory;
import omis.health.report.DailyProviderScheduleSummary;
import omis.health.report.ProviderScheduleReportService;

/** 
 * Implementation of daily provider schedule summary.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class ProviderScheduleReportServiceHibernateImpl
		implements ProviderScheduleReportService {
	
	/* Query names. */
	
	private static final String FIND_PROVIDER_DAILY_SCHEDULE_ON_DATE_QUERY =
			"findProviderDailyScheduleOnDate";
	private static final String
			FIND_PROVIDER_DAILY_SCHEDULES_BY_FACILITY_ON_DATE_QUERY =
					"findProviderDailySchedulesByFacilityOnDate";
	private static final String
			FIND_PROVIDER_DAILY_SCHEDULES_BY_PROVIDER_DURING_DATE_RANGE_QUERY =
					"findProviderDailySchedulesByProviderDuringDateRange";
	private static final String
			FIND_PROVIDER_DAILY_SCHEDULES_BY_FACILITY_DURING_DATE_RANGE_QUERY =
					"findProviderDailySchedulesByFacilityDuringDateRange";
	private static final String FIND_PROVIDER_APPOINTMENT_COUNT_BY_DATE =
			"findProviderAppointmentCountByDate";
	private static final String FIND_IRREGULAR_SCHEDULE_DAY_BY_DATE =
			"findIrregularScheduleDayByDate";

	private static final int SUNDAY_INDEX = 0;
	private static final int MONDAY_INDEX = 1;
	private static final int TUESDAY_INDEX = 2;
	private static final int WEDNESDAY_INDEX = 3;
	private static final int THURSDAY_INDEX = 4;
	private static final int FRIDAY_INDEX = 5;
	private static final int SATURDAY_INDEX  = 6;
	private static final int PROVIDER_FIRST_NAME_INDEX = 7;
	private static final int PROVIDER_LAST_NAME_INDEX = 8;
	private static final int PROVIDER_ASSIGNMENT_CATEGORY_INDEX = 9;
	private static final int FACILITY_NAME_INDEX = 10;
	private static final int PROVIDER_ASSIGNMENT_INDEX = 11;
	private static final int HOURS_PER_DAY = 24;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int SECONDS_PER_MINUTE = 60;
	private static final int MILLISECONDS_PER_SECOND = 1000;

	/* Parameter names. */
	
	private final static String PROVIDER_ASSIGNMENT_PARAM_NAME = 
			"providerAssignment";
	private final static String FACILITY_PARAM_NAME = "facility";
	private final static String DAY_PARAM_NAME = "day";
	private final static String START_DATE_PARAM_NAME = "startDate";
	private final static String END_DATE_PARAM_NAME = "endDate";
	private final static String DATE_PARAM_NAME = "date";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;

	/** Constructor.
	 * @param sessionFactory session factory. */
	public ProviderScheduleReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public DailyProviderScheduleSummary findOnDate(
			final ProviderAssignment providerAssignment, final Date day) {
		return this.summarize(day, providerAssignment,
				(Object[]) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_PROVIDER_DAILY_SCHEDULE_ON_DATE_QUERY)
				.setParameter(PROVIDER_ASSIGNMENT_PARAM_NAME, 
						providerAssignment)
				.setReadOnly(true)
				.uniqueResult());
	}

	/** {@inheritDoc} */
	@Override
	public List<DailyProviderScheduleSummary> findByFacilityOnDate(
			final Facility facility, final Date day) {
		@SuppressWarnings("unchecked")
		final List<Object[]> objectList = (List<Object[]>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_PROVIDER_DAILY_SCHEDULES_BY_FACILITY_ON_DATE_QUERY)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setTimestamp(DAY_PARAM_NAME, day)
				.setReadOnly(true)
				.list();

		return this.summarize(day, objectList);
	}

	/** {@inheritDoc} */
	@Override
	public List<DailyProviderScheduleSummary> findDuringDateRange(
			final ProviderAssignment providerAssignment,
			final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		final List<Object[]> objectList = (List<Object[]>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_PROVIDER_DAILY_SCHEDULES_BY_PROVIDER_DURING_DATE_RANGE_QUERY)
				.setParameter(PROVIDER_ASSIGNMENT_PARAM_NAME, 
						providerAssignment)
				.setReadOnly(true)
				.list();

		return this.summarize(dateRange, objectList);
	}

	/** {@inheritDoc} */
	@Override
	public List<DailyProviderScheduleSummary> findByFacilityDuringDateRange(
			final Facility facility, final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		final List<Object[]> objectList = (List<Object[]>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_PROVIDER_DAILY_SCHEDULES_BY_FACILITY_DURING_DATE_RANGE_QUERY)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setTimestamp(START_DATE_PARAM_NAME, dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAM_NAME, dateRange.getEndDate())
				.setReadOnly(true)
				.list();

		return this.summarize(dateRange, objectList);
	}

	/** Determines day of week time schedule.
	 * @param date day of schedule.
	 * @param objs provider schedule.
	 * @return time range of day of week schedule. */
	private DateRange dayOfWeekSchedule(final Date date, final Object[] objs) {
		final Calendar calendar = Calendar.getInstance();
		final IrregularScheduleDay irregularScheduleDay =
				this.findIrregularScheduleDayByDate(date,
						(ProviderAssignment) objs[PROVIDER_ASSIGNMENT_INDEX]);

		if (irregularScheduleDay != null) {
			return irregularScheduleDay.getTimeRange();
		}
		calendar.setTime(date);
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY) {
			return (DateRange) objs[SUNDAY_INDEX];
		} else if (dayOfWeek == Calendar.MONDAY) {
			return (DateRange) objs[MONDAY_INDEX];
		} else if (dayOfWeek == Calendar.TUESDAY) {
			return (DateRange) objs[TUESDAY_INDEX];
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
			return (DateRange) objs[WEDNESDAY_INDEX];
		} else if (dayOfWeek == Calendar.THURSDAY) {
			return (DateRange) objs[THURSDAY_INDEX];
		} else if (dayOfWeek == Calendar.FRIDAY) {
			return (DateRange) objs[FRIDAY_INDEX];
		} else if (dayOfWeek == Calendar.SATURDAY) {
			return (DateRange) objs[SATURDAY_INDEX];
		} else { return null; }
	}

	/** Find time given date.
	 * @param date date. */
	private Date timeOfDate(final Date date) {
		Date result = null;
		if (date != null) {
			result = new Date(
					date.getTime() % (MILLISECONDS_PER_SECOND
							* SECONDS_PER_MINUTE * MINUTES_PER_HOUR
							* HOURS_PER_DAY));
		}
		return result;
	}

	/** Summarize provider schedules for date range.
	 * @param dateRange days to summarize.
	 * @param objsList list of provider schedules.
	 * @return list of daily provider schedule summaries. */
	private List<DailyProviderScheduleSummary> summarize(
			final DateRange dateRange,
			final List<Object[]> objsList) {
		final List<DailyProviderScheduleSummary> list =
				new ArrayList<DailyProviderScheduleSummary>();
		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		final GregorianCalendar after = new GregorianCalendar();
		gregorianCalendar.setTime(dateRange.getStartDate());
		after.setTime(dateRange.getEndDate());
		if (dateRange.getEndDate() != null) {
			while (gregorianCalendar.before(after)) {
			 	list.addAll(this.summarize(gregorianCalendar.getTime(),
					 objsList));
			 	gregorianCalendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			return list;
		} else {
			throw new IllegalStateException(
					 "End date cannot be null for schedule summary.");
		}
	}

	/** Summarize provider schedule by date.
	 * @param date date.
	 * @param objsList list of provider schedules.
	 * @return list of daily provider schedule summaries. */
	private List<DailyProviderScheduleSummary> summarize(final Date day,
		final List<Object[]> objsList) {
		final List<DailyProviderScheduleSummary> list =
				new ArrayList<DailyProviderScheduleSummary>();
		for (final Object[] objects : objsList) {
			list.add(this.summarize(day,
					(ProviderAssignment) objects[PROVIDER_ASSIGNMENT_INDEX],
					objects));
		}
		return list;
	}

	/** Summarize provider schedule by provider schedule objs.
	 * @param date day to summerize.
	 * @param objs provider schedule components. */
	private DailyProviderScheduleSummary summarize(final Date day,
			final ProviderAssignment providerAssignment, final Object[] objs) {
		final DateRange dateRange = this.dayOfWeekSchedule(day, objs);
		Date startDate = null;
		Date endDate = null;

		if (dateRange != null) {
			startDate = dateRange.getStartDate();
			endDate = dateRange.getEndDate();
		}
		return new DailyProviderScheduleSummary(day,
				this.timeOfDate(startDate),
				this.timeOfDate(endDate),
				(String) objs[PROVIDER_FIRST_NAME_INDEX],
				(String) objs[PROVIDER_LAST_NAME_INDEX],
				(ProviderAssignmentCategory)
				objs[PROVIDER_ASSIGNMENT_CATEGORY_INDEX],
				(String) objs[FACILITY_NAME_INDEX],
				this.countAppointmentsByDate(providerAssignment, day));

	}

	/** Count appointments.
	 * @param date date.
	 * @param providerAssignment provider assignment.
	 * @return count of appointments. */
	private int countAppointmentsByDate(final ProviderAssignment providerAssignment,
			final Date date) {
		return this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_PROVIDER_APPOINTMENT_COUNT_BY_DATE)
				.setParameter(PROVIDER_ASSIGNMENT_PARAM_NAME, 
						providerAssignment)
				.setDate(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list().size();
	}

	/** find irregular day schedule.
	 * @param date date.
	 * @return irregularScheduleDay irregular schedule day. */
	private IrregularScheduleDay findIrregularScheduleDayByDate(final Date date,
			final ProviderAssignment providerAssignment) {
		return (IrregularScheduleDay) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_IRREGULAR_SCHEDULE_DAY_BY_DATE)
				.setParameter(PROVIDER_ASSIGNMENT_PARAM_NAME, 
						providerAssignment)
				.setDate(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult();
	}
}