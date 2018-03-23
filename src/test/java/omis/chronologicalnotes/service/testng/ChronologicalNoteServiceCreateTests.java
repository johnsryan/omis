/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnotes.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.service.delegate.ChronologicalNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Chronological note service create tests.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0
 * @since OMIS 3.0
 */
public class ChronologicalNoteServiceCreateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Property value names. */
	
	private static final String OFFENDER_PROPERTY_VALUE_NAME = "offender";
	private static final String DATE_PROPERTY_VALUE_NAME = "date";
	private static final String NARRATIVE_PROPERTY_VALUE_NAME = "narrative";
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("chronologicalNoteDelegate")
	private ChronologicalNoteDelegate chronologicalNoteDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	/* Service. */
	@Autowired
	@Qualifier("chronologicalNoteService")
	private ChronologicalNoteService chronologicalNoteService;	
	
	/* Constructor. */
	
	/**
	 * Instantiates a default instance of chronological note service create 
	 * tests.
	 */
	public ChronologicalNoteServiceCreateTests() {
		//Default constructor.
	}
	
	/* Test methods. */
	
	/**
	 * Test the creation of a chronological note.
	 * 
	 * @throws ChronologicalNoteExistsException Thrown if a chronological 
	 * note already exists with the specified date, offender, and narrative.
	 */
	@Test
	public void testCreate() throws ChronologicalNoteExistsException {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Schmoe", "Joe", "Not So", null);
		String narrative = new String(
				"This is the narrative of the test chronological note");
		String title = "title";
		//Action
		ChronologicalNote note = this.chronologicalNoteService.create(
				date, offender, title, narrative);
		
		//Assertions
		PropertyValueAsserter.create()
			.addExpectedValue(DATE_PROPERTY_VALUE_NAME, date)
			.addExpectedValue(OFFENDER_PROPERTY_VALUE_NAME, offender)
			.addExpectedValue("title", title)
			.addExpectedValue(NARRATIVE_PROPERTY_VALUE_NAME, narrative)
			.performAssertions(note);
	}
	
	/**
	 * Tests {@code ChronologicalNoteService} create method to ensure a 
	 * {@link ChronologicalNoteExistsException}
	 * is thrown in the proper circumstance.
	 * 
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found with the specified date, offender, and 
	 * narrative.
	 */
	@Test(expectedExceptions = {ChronologicalNoteExistsException.class})
	public void testChronologicalNoteExistsException()
			throws ChronologicalNoteExistsException {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Schmoe", "Joe", "Not So", null);
		String narrative = new String(
				"This is the narrative of the test chronological note");
		String title = "title";
		this.chronologicalNoteDelegate.create(date, offender, title, narrative);
		
		//Action
		this.chronologicalNoteService.create(date, offender, title, narrative);
	}
	
	/* Helper methods */
	
	/*
	 * Parses the specified string and returns a {@code Date} object
	 * representing the parsed {@code String}.
	 *  
	 * @param text text to parse
	 * @return date 
	 */
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}