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
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.mentalhealthreview.service.delegate.MentalHealthNoteDelegate;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create mental health review notes.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class MentalHealthReviewServiceCreateMentalHealthReviewNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("mentalHealthReviewDelegate")
	private MentalHealthReviewDelegate mentalHealthReviewDelegate;
	
	@Autowired
	@Qualifier("mentalHealthNoteDelegate")
	private MentalHealthNoteDelegate mentalHealthNoteDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("mentalHealthReviewService")
	private MentalHealthReviewService mentalHealthReviewService;

	/* Test methods. */

	/**
	 * Tests the creation of mental health review notes.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testCreateMentalHealthReviewNote() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, "text", offender);
		String description = "Description";

		// Action
		MentalHealthNote mentalHealthNote = this.mentalHealthReviewService
				.createMentalHealthReviewNote(mentalHealthReview, description,
						date);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("mentalHealthReview", mentalHealthReview)
			.addExpectedValue("description", description)
			.addExpectedValue("date", date)
			.performAssertions(mentalHealthNote);
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
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, "text", offender);
		String description = "Description";
		this.mentalHealthNoteDelegate.create(mentalHealthReview, date, 
				description);

		// Action
		this.mentalHealthReviewService.createMentalHealthReviewNote(
				mentalHealthReview, description, date);
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