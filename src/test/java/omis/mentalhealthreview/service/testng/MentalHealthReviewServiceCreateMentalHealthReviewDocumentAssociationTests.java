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

import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDelegate;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDocumentAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create mental health review document associations.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class 
	MentalHealthReviewServiceCreateMentalHealthReviewDocumentAssociationTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("documentDelegate")
	private DocumentDelegate documentDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("mentalHealthReviewDelegate")
	private MentalHealthReviewDelegate mentalHealthReviewDelegate;
	
	@Autowired
	@Qualifier("mentalHealthReviewDocumentAssociationDelegate")
	private MentalHealthReviewDocumentAssociationDelegate 
			mentalHealthReviewDocumentAssociationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("mentalHealthReviewService")
	private MentalHealthReviewService mentalHealthReviewService;

	/* Test methods. */

	/**
	 * Tests the creation of mental health review document associations.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testCreateMentalHealthReviewDocumentAssociation() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		Document document = this.documentDelegate.create(date, "filename", 
				"123", "title");
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, text, offender);

		// Action
		MentalHealthReviewDocumentAssociation documentAssociation = this
				.mentalHealthReviewService
				.createMentalHealthReviewDocumentAssociation(document, 
						mentalHealthReview);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("document", document)
			.addExpectedValue("mentalHealthReview", mentalHealthReview)
			.performAssertions(documentAssociation);
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
		Document document = this.documentDelegate.create(date, "filename", 
				"123", "title");;
		String text = "Text";
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"Tom", "Harry", null);
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewDelegate
				.create(date, text, offender);
		this.mentalHealthReviewDocumentAssociationDelegate.create(document, 
				mentalHealthReview);
		
		// Action
		this.mentalHealthReviewService
				.createMentalHealthReviewDocumentAssociation(document, 
						mentalHealthReview);
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