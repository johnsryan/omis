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
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update documents.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class MentalHealthReviewServiceUpdateDocumentTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("documentDelegate")
	private DocumentDelegate documentDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("mentalHealthReviewService")
	private MentalHealthReviewService mentalHealthReviewService;

	/* Test methods. */

	/**
	 * Tests the update of the date for a document.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateDocumentDate() throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		String title = "Title";
		String filename = "Filename";
		String fileExtension = "pdf";
		Document document = this.documentDelegate.create(date, filename, 
				fileExtension, title);
		Date newDate = this.parseDateText("01/01/2018");
		
		// Action
		document = this.mentalHealthReviewService.updateDocument(document, 
				newDate, title);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("date", newDate)
			.addExpectedValue("title", title)
			.addExpectedValue("filename", filename)
			.addExpectedValue("fileExtension", fileExtension)
			.performAssertions(document);
	}
	
	/**
	 * Tests the update of the title for a document.
	 * 
	 * @throws DuplicateEntityFoundException if entity already exists
	 */
	@Test
	public void testUpdateDocumentTitle() throws DuplicateEntityFoundException {
		// Arrangements
		Date date = this.parseDateText("01/01/2018");
		String title = "Title";
		String filename = "Filename";
		String fileExtension = "pdf";
		Document document = this.documentDelegate.create(date, filename, 
				fileExtension, title);
		String newTitle = "Title";
		
		// Action
		document = this.mentalHealthReviewService.updateDocument(document, date, 
				newTitle);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("date", date)
			.addExpectedValue("title", newTitle)
			.addExpectedValue("filename", filename)
			.addExpectedValue("fileExtension", fileExtension)
			.performAssertions(document);
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