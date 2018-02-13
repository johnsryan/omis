package omis.chronologicalnotes.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryAssociationExistsException;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryExistsException;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryAssociationDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Chronological note category association create tests.
 * 
 * @author Joel Norris
 * @version 0.1.0 (February 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteServiceCategoryAssociationCreateTests 
extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Data access objects. */
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryDao")
	ChronologicalNoteCategoryDao chronologicalNoteCategoryDao;

	/* Delegates. */
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryAssociationDelegate")
	ChronologicalNoteCategoryAssociationDelegate chronologicalNoteCategoryAssociationDelegate;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryDelegate")
	ChronologicalNoteCategoryDelegate chronologicalNoteCategoryDelegate;
	
	@Autowired
	@Qualifier("chronologicalNoteDelegate")
	ChronologicalNoteDelegate chronologicalNoteDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	OffenderDelegate offenderDelegate;
	
	/* Service. */
	@Autowired
	@Qualifier("chronologicalNoteService")
	ChronologicalNoteService chronologicalNoteService;
	
	/* Property value names. */
	
	
	/* Constructor. */
	
	/**
	 * Instantiates a default instance of chronological note category association create tests.
	 */
	public ChronologicalNoteServiceCategoryAssociationCreateTests() {
		//Default constructor.
	}
	
	/**
	 * Tests the creation of an association between a chronological note and a chronological
	 * note category.
	 * 
	 * @throws ChronologicalNoteCategoryAssociationExistsException Thrown when an association already
	 * exists between a chronological note and chronological note category. 
	 */
	@Test
	public void testCreate()
			throws ChronologicalNoteCategoryAssociationExistsException {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity("Schmoe", "Joe", "Not So", null);
		String narrative = new String("This is the narrative of the test chronological note");
		ChronologicalNote note;
		try {
			note = this.chronologicalNoteDelegate.create(date, offender, narrative);
		} catch (ChronologicalNoteExistsException e) {
			throw new RuntimeException("Chronlogical note exists: " + e);
		}
		ChronologicalNoteCategory category;
		try {category = this.chronologicalNoteCategoryDelegate.create("categoryName", true);
		} catch (ChronologicalNoteCategoryExistsException e) {
			throw new RuntimeException("Chronological note category exists: " + e);
		}
		//Action
		this.chronologicalNoteService.associateCategory(note, category);;
		//Assertion
		assert this.chronologicalNoteService.findAssociatedCategories(note)
			.contains(category) : "Category note association not saved";
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