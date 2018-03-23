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
package omis.util.testng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import omis.datatype.DateRange;

/**
 * Tests date range overlap check.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 9, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"dateRange", "util"})
public class DateRangeOverlapsTests {
	
	// Date formatter
	private DateFormat dateTimeFormat;

	/**
	 * Tests that two date ranges overlap where none of the dates are null.
	 * 
	 * <p>Neither date range is within the other.
	 */
	public void testOverlapsWithoutNulls() {
		DateRange startRange = this.createDateRange(
				"01/02/2013 01:00 PM", "02/03/3014 02:00 PM");
		DateRange endRange = this.createDateRange(
				"12/13/2012 01:00 AM", "01/13/2013 04:30 AM");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
	}

	/** Reverse of {@code testOverlapWithoutNulls}. */
	public void testOverlapsWithoutNullsReverse() {
		DateRange startRange = this.createDateRange(
				"12/13/2012 01:00 AM", "01/13/2013 04:30 AM");
		DateRange endRange = this.createDateRange(
				"01/02/2013 01:00 PM", "02/03/3014 02:00 PM");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
	}
	
	/**
	 * Tests overlap where date range is within another.
	 */
	public void testOverlapsWithin() {
		DateRange startRange = this.createDateRange(
				"12/04/2016 12:00 AM", "02/05/2017 9:00 PM");
		DateRange endRange = this.createDateRange(
				"01/03/2015 12:30 PM", "02/05/2018 9:00 PM");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
	}
	
	/**
	 * Reverse of {@code testOverlapWithin}.
	 */
	public void testOverlapsWithinReverse() {
		DateRange startRange = this.createDateRange(
				"01/03/2015 12:30 PM", "02/05/2018 9:00 PM");
		DateRange endRange = this.createDateRange(
				"12/04/2016 12:00 AM", "02/05/2017 9:00 PM");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
	}
	
	// Creates date range from date/time strings
	private DateRange createDateRange(
			final String startDateTime,
			final String endDateTime) {
		Date parsedStartDateTime;
		Date parsedEndDateTime;
		if (this.dateTimeFormat == null) {
			this.dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		}
		if (startDateTime != null) {
			try {
				parsedStartDateTime = this.dateTimeFormat.parse(startDateTime);
			} catch (ParseException e) {
				throw new RuntimeException("Parse error: " + e.getMessage(), e);
			}
		} else {
			parsedStartDateTime = null;
		}
		if (endDateTime != null) {
			try {
				parsedEndDateTime = this.dateTimeFormat.parse(endDateTime);
			} catch (ParseException e) {
				throw new RuntimeException("Parse error: " + e.getMessage(), e);
			}
		} else {
			parsedEndDateTime = null;
		}
		return new DateRange(parsedStartDateTime, parsedEndDateTime);
	}
}