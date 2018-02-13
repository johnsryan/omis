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
package omis.mentalhealthreview.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.exception.DuplicateEntityFoundException;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update mental health reviews.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class MentalHealthReviewServiceUpdateMentalHealthReviewTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("mentalHealthReviewDelegate")
	private MentalHealthReviewDelegate mentalHealthReviewDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("mentalHealthReviewService")
	private MentalHealthReviewService mentalHealthReviewService;

	/* Test methods. */

	/**
	 * Tests the update of the date for a mental health review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateMentalHealthReviewDate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, text, offender);
		Date newDate = this.parseDateText("01/02/2018");
		
		// Action
		mentalHealthReview = this.mentalHealthReviewService
				.updateMentalHealthReview(mentalHealthReview, newDate, text);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("date", newDate)
			.addExpectedValue("text", text)
			.addExpectedValue("offender", offender)
			.performAssertions(mentalHealthReview);
	}


	/**
	 * Tests the update of the text for a mental health review.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateMentalHealthReviewText() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, text, offender);
		String newText = "New text";
		
		// Action
		mentalHealthReview = this.mentalHealthReviewService
				.updateMentalHealthReview(mentalHealthReview, date, newText);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("date", date)
			.addExpectedValue("text", newText)
			.addExpectedValue("offender", offender)
			.performAssertions(mentalHealthReview);
	}
	
	/**
	 * Tests that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		this.mentalHealthReviewDelegate.create(date, text, offender);
		Date secondDate = this.parseDateText("01/02/2018");
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(secondDate, text, offender);
		
		// Action
		mentalHealthReview = this.mentalHealthReviewService
				.updateMentalHealthReview(mentalHealthReview, date, text);
	}

	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}