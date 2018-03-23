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
 * Chronological note service update tests.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteServiceUpdateTests 
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
	 * Instantiates an instance of chronological note service update tests.
	 */
	public ChronologicalNoteServiceUpdateTests() {
		//Default constructor.
	}
	
	/**
	 * Tests the update of a chronological note.
	 * 
	 * @throws ChronologicalNoteExistsException Thrown if a chronological note 
	 * already exists with the specified update date and update narrative.
	 */
	@Test
	public void testUpdate() throws ChronologicalNoteExistsException {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Schmoe", "Joe", "Not So", null);
		String narrative = new String(
				"This is the narrative of the test chronological note");
		String title = "title";
		ChronologicalNote note = this.chronologicalNoteDelegate.create(
				date, offender, title, narrative);
		
		Date updatedDate = this.parseDateText("02/02/2018");
		String updatedNarrative = new String(
				"This is the updated narrative of the test chronological note");
		String updatedTitle = "newTitle";
		//Action
		note = this.chronologicalNoteService.update(
				note, updatedDate, updatedTitle, updatedNarrative);
		
		//Assertions
		PropertyValueAsserter.create()
			.addExpectedValue(DATE_PROPERTY_VALUE_NAME, updatedDate)
			.addExpectedValue(OFFENDER_PROPERTY_VALUE_NAME, offender)
			.addExpectedValue("title", updatedTitle)
			.addExpectedValue(NARRATIVE_PROPERTY_VALUE_NAME, 
					updatedNarrative)
			.performAssertions(note);
	}
	
	/**
	 * Tests {@code ChronologicalNoteService} create method to ensure a 
	 * {@link ChronologicalNoteExistsException}
	 * is thrown in the proper circumstance.
	 * 
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found
	 * with the specified date, offender, and narrative.
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
		
		Date updatedDate = this.parseDateText("02/02/2018");
		String updatedNarrative = new String(
				"This is the updated narrative of the test chronological note");
		String updatedTitle = "newTitle";
		ChronologicalNote noteToUpdate = this.chronologicalNoteDelegate.create(
				updatedDate, offender, updatedTitle, updatedNarrative);
		//Action
		this.chronologicalNoteService.update(
				noteToUpdate, date, title, narrative);
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