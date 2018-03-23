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
 * Tests date range occurs within check.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 9, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"dateRange", "util"})
public class DateRangeOccursWithinTests {

	private DateFormat dateFormat;
	
	/** Tests range occurs within another without nulls dates. */
	public void testOccursWithinWithoutNulls() {
		DateRange startRange = this.createDateRange(
				"11/22/1996", "11/23/1996");
		DateRange endRange = this.createDateRange(
				"11/21/1996", "11/24/1996");
		assert startRange.occursWithin(endRange)
			: startRange + " does not occur within " + endRange;
	}
	
	/** Tests range does not occur within another without null dates. */
	public void testDoesNotOccurWithinWithoutNulls() {
		DateRange startRange
			= this.createDateRange("12/13/2006", "12/22/2006");
		DateRange endRange
			= this.createDateRange("11/23/1996", "12/25/2005");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/** Tests reverse of {@code testDoesNotOccurWithinWithoutNulls}. */
	public void testDoesNotOccurWithinWithoutNullsReverse() {
		DateRange startRange
			= this.createDateRange("11/23/1996", "12/25/2005");
		DateRange endRange
			= this.createDateRange("12/13/2006", "12/22/2006");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/** Tests that an overlapping date range does not occur within. */
	public void testOverlapsButDoesNotOccurWithinWithoutNulls() {
		DateRange startRange
			= this.createDateRange("10/01/2006", "12/22/2007");
		DateRange endRange
			= this.createDateRange("11/22/2006", "12/23/2007");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange; 
	}
	
	/**
	 * Tests reverse of {@code testOverlapsButDoesNotOccurWithinWithoutNulls}.
	 */
	public void testOverlapsButDoesNotOccurWithinWithoutNullsReverse() {
		DateRange startRange
			= this.createDateRange("11/22/2006", "12/23/2007");
		DateRange endRange
			= this.createDateRange("10/01/2006", "12/22/2007");
		assert startRange.overlaps(endRange)
			: startRange + " does not overlap " + endRange;
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange; 
	}
	
	/**
	 * Tests that an overlapping date range with {@code null}s does not occur
	 * within.
	 */
	public void testOverlapsButDoesNotOccurWithinWithNulls() {
		DateRange startRange = this.createDateRange("11/22/2006", null);
		DateRange endRange = this.createDateRange(null, "12/22/2007");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/**
	 * Tests reverse of {@code testOverlapsButDoesNotOccurWithinWithNulls}.
	 */
	public void testOverlapsButDoesNotOccurWithinWithNullsReverse() {
		DateRange startRange = this.createDateRange(null, "12/22/2007");
		DateRange endRange = this.createDateRange("11/22/2006", null);
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/**
	 * Tests that a later starting date range occurs within an earlier starting
	 * date range with null end dates.
	 */
	public void testOccursWithinWithNullEndDates() {
		DateRange startRange = this.createDateRange("11/29/2011", null);
		DateRange endRange = this.createDateRange("11/22/2011", null);
		assert startRange.occursWithin(endRange)
			: startRange + " does not occur within " + endRange;
	}
	
	/**
	 * Tests that an earlier starting date range does not occur within a later
	 * starting date range with null end dates.
	 */
	public void testDoesNotOccurWithinWithNullEndDates() {
		DateRange startRange = this.createDateRange("10/23/1996", null);
		DateRange endRange = this.createDateRange("09/11/1997", null);
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/**
	 * Tests that earlier ending date range occurs within a later ending
	 * date range with null start dates.
	 */
	public void testOccursWithinWithNullStartDates() {
		DateRange startRange = this.createDateRange(null, "11/22/2011");
		DateRange endRange = this.createDateRange(null, "12/21/2013");
		assert startRange.occursWithin(endRange)
			: startRange + " does not occur within " + endRange;
	}

	/**
	 * Tests that later ending date range occurs within an earlier ending
	 * date range with null start dates.
	 */
	public void testDoesNotOccurWithinWithNullStartDates() {
		DateRange startRange = this.createDateRange(null, "11/25/2014");
		DateRange endRange = this.createDateRange(null, "12/21/2013");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/**
	 * Tests that a later starting date range occurs within an earlier starting
	 * date range with equal end dates.
	 */
	public void testOccursWithinWithEqualEndDates() {
		DateRange startRange = this.createDateRange(
				"11/29/2011", "12/13/2013");
		DateRange endRange = this.createDateRange(
				"11/22/2011",  "12/13/2013");
		assert startRange.occursWithin(endRange)
			: startRange + " does not occur within " + endRange;
	}
	
	/**
	 * Tests that an earlier starting date range does not occur within a later
	 * starting date range with equal end dates.
	 */
	public void testDoesNotOccurWithinWithEqualEndDates() {
		DateRange startRange = this.createDateRange(
				"10/23/1996", "11/11/1998");
		DateRange endRange = this.createDateRange(
				"09/11/1997", "11/11/1998");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/**
	 * Tests that earlier ending date range occurs within a later ending
	 * date range with equal start dates.
	 */
	public void testOccursWithinWithEqualStartDates() {
		DateRange startRange = this.createDateRange(
				"12/11/1956", "11/22/2011");
		DateRange endRange = this.createDateRange(
				"12/11/1956", "12/21/2013");
		assert startRange.occursWithin(endRange)
			: startRange + " does not occur within " + endRange;
	}

	/**
	 * Tests that later ending date range occurs within an earlier ending
	 * date range with equal start dates.
	 */
	public void testDoesNotOccurWithinWithEqualStartDates() {
		DateRange startRange = this.createDateRange(
				"12/25/0000", "11/25/2014");
		DateRange endRange = this.createDateRange(
				"12/25/0000", "12/21/2013");
		assert !startRange.occursWithin(endRange)
			: startRange + " occurs within " + endRange;
	}
	
	/** Tests that a date range occurs within forever. */
	public void testRangeOccursWithinForever() {
		DateRange range
			= this.createDateRange("01/01/0101", "02/02/0202");
		DateRange forever = new DateRange((Date) null, null);
		assert range.occursWithin(forever)
			: range + " does not occur within " + forever;
	}
	
	/** Tests that forever does not occur within a range. */
	public void testForeverDoesNotOccurWithinRange() {
		DateRange forever = new DateRange((Date) null, null);
		DateRange range
			= this.createDateRange("01/01/0101", "02/02/0202");
		assert !forever.occursWithin(range)
			: forever + " occurs within " + range;
	}
	
	// Creates date range from parsed start and end date values
	private DateRange createDateRange(
			final String startDate, final String endDate) {
		Date parsedStartDate;
		Date parsedEndDate;
		if (this.dateFormat == null) {
			this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		}
		if (startDate != null) {
			try {
				parsedStartDate = this.dateFormat.parse(startDate);
			} catch (ParseException e) {
				throw new RuntimeException("Parse error: " + e.getMessage(), e);
			}
		} else {
			parsedStartDate = null;
		}
		if (endDate != null) {
			try {
				parsedEndDate = this.dateFormat.parse(endDate);
			} catch (ParseException e) {
				throw new RuntimeException("Parse error: " + e.getMessage(), e);
			}
		} else {
			parsedEndDate = null;
		}
		return new DateRange(parsedStartDate, parsedEndDate);
	}
}