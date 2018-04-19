package omis.stg.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.stg.domain.SecurityThreatGroupActivity;
import omis.stg.domain.SecurityThreatGroupActivityNote;
import omis.stg.service.SecurityThreatGroupActivityService;
import omis.stg.service.delegate.SecurityThreatGroupActivityDelegate;
import omis.stg.service.delegate.SecurityThreatGroupActivityNoteDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update security threat group activity notes.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public class SecurityThreatGroupActivityServiceUpdateNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("securityThreatGroupActivityDelegate")
	private SecurityThreatGroupActivityDelegate 
		securityThreatGroupActivityDelegate;
	
	@Autowired
	@Qualifier("securityThreatGroupActivityNoteDelegate")
	private SecurityThreatGroupActivityNoteDelegate 
		securityThreatGroupActivityNoteDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("securityThreatGroupActivityService")
	private SecurityThreatGroupActivityService 
		securityThreatGroupActivityService;

	/* Test methods. */
	
	/**
	 * Tests update of the date for a security threat group activity note.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateNoteDate() throws DuplicateEntityFoundException {
		// Arrangements
		Person reportedBy = this.personDelegate.create("Doe", "John", null, 
				null);
		SecurityThreatGroupActivity activity = this
				.securityThreatGroupActivityDelegate.create(
						this.parseDateText("01/01/2017"), reportedBy, 
						"Summary");
		Date date = this.parseDateText("02/01/2017");
		String value = "Value";
		SecurityThreatGroupActivityNote note = this
				.securityThreatGroupActivityNoteDelegate.addNote(activity, date, 
						value);
		Date newDate = this.parseDateText("02/02/2017");
		
		// Action
		note = this.securityThreatGroupActivityService.updateNote(note, 
				newDate, value);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("activity", activity)
			.addExpectedValue("date", newDate)
			.addExpectedValue("value", value)
			.performAssertions(note);
	}
	
	/**
	 * Tests update of the value for a security threat group activity note.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateNoteValue() throws DuplicateEntityFoundException {
		// Arrangements
		Person reportedBy = this.personDelegate.create("Doe", "John", null, 
				null);
		SecurityThreatGroupActivity activity = this
				.securityThreatGroupActivityDelegate.create(
						this.parseDateText("01/01/2017"), reportedBy, 
						"Summary");
		Date date = this.parseDateText("02/01/2017");
		String value = "Value";
		SecurityThreatGroupActivityNote note = this
				.securityThreatGroupActivityNoteDelegate.addNote(activity, 
						date, value);
		String newValue = "Value 2";
		
		// Action
		note = this.securityThreatGroupActivityService.updateNote(note, date, 
				newValue);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("activity", activity)
			.addExpectedValue("date", date)
			.addExpectedValue("value", newValue)
			.performAssertions(note);
	}

	 /**
	 * Tests update of the value for a security threat group activity note 
	 * when an existing note exists with the same date and value but a 
	 * different activity to test fix for production issue [TICK:21288].
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateNoteDuplicateDateAndValueWithDifferentActivity() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Date reportDate = this.parseDateText("01/01/2017");
		Person reportedBy = this.personDelegate.create("Doe", "John", null, 
				null);
		SecurityThreatGroupActivity activity = 
				this.securityThreatGroupActivityDelegate.create(reportDate, 
						reportedBy, "Summary");
		Date date = this.parseDateText("02/01/2017");
		String value = "Value";
		SecurityThreatGroupActivityNote note = this
				.securityThreatGroupActivityNoteDelegate.addNote(activity, date, 
						value);
		String newValue = "Value 2";
		SecurityThreatGroupActivity newActivity = 
				this.securityThreatGroupActivityDelegate.create(reportDate, 
						reportedBy, "Summary2");
		this.securityThreatGroupActivityNoteDelegate.addNote(newActivity, date, 
				newValue);
		
		// Action
		note = this.securityThreatGroupActivityService.updateNote(note, 
				date, newValue);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("activity", activity)
			.addExpectedValue("date", date)
			.addExpectedValue("value", newValue)
			.performAssertions(note);
	}
	
	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person reportedBy = this.personDelegate.create("Doe", "John", null, 
				null);
		SecurityThreatGroupActivity activity = this
				.securityThreatGroupActivityDelegate.create(
						this.parseDateText("01/01/2017"), 
						reportedBy, "Summary");
		Date date = this.parseDateText("02/01/2017");
		String value = "Value";
		this.securityThreatGroupActivityNoteDelegate.addNote(activity, date, 
				value);
		Date secondDate = this.parseDateText("03/01/2017");
		SecurityThreatGroupActivityNote note = this
				.securityThreatGroupActivityNoteDelegate.addNote(activity, 
						secondDate, value);
		
		// Action
		note = this.securityThreatGroupActivityService.updateNote(note, date, 
				value);
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