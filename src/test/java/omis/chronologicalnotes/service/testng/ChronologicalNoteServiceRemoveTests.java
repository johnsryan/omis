package omis.chronologicalnotes.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.chronologicalnote.dao.ChronologicalNoteDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.service.delegate.ChronologicalNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Chronological note service remove tests.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteServiceRemoveTests
extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Data access objects. */
	
	@Autowired
	@Qualifier("chronologicalNoteDao")
	private ChronologicalNoteDao chronologicalNoteDao;

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
	 * Instantiates an instance chronological note service remove tests.
	 */
	public ChronologicalNoteServiceRemoveTests() {
		//Default constructor.
	}
	
	/**
	 * Tests the removal of a chronological note.
	 */
	@Test
	public void testRemove() {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity("Schmoe", "Joe", "Not So", null);
		String narrative = new String("This is the narrative of the test chronological note");
		String title = "title";
		//Create chronological note to remove
		ChronologicalNote note;
		try {
			note = this.chronologicalNoteDelegate.create(date, offender, title, narrative);
		} catch (ChronologicalNoteExistsException e) {
			throw new RuntimeException("Chronological note exists", e);
		}
		
		//Action
		this.chronologicalNoteService.remove(note);
		
		//Assertion
		assert this.chronologicalNoteDao.find(date, offender, title) == null
				: "Chronological Note was not eremoved.";
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