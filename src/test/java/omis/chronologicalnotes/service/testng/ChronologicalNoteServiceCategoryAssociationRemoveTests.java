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
package omis.chronologicalnotes.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryExistsException;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryGroupExistsException;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryAssociationDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryGroupDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Chronological note service category association remove tests.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 6, 2018)
 * @since OMIS 3.0
 */
@Test(groups = "chronologicalNote")
public class ChronologicalNoteServiceCategoryAssociationRemoveTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryAssociationDelegate")
	private ChronologicalNoteCategoryAssociationDelegate chronologicalNoteCategoryAssociationDelegate;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryDelegate")
	private ChronologicalNoteCategoryDelegate chronologicalNoteCategoryDelegate;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryGroupDelegate")
	private ChronologicalNoteCategoryGroupDelegate chronologicalNoteCategoryGroupDelegate;
	
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
	 * Instantiates a default instance of chronological note service category
	 * association remove tests.
	 */
	public ChronologicalNoteServiceCategoryAssociationRemoveTests() {
		//Default constructor.
	}
	
	/**
	 * Tests removal of chronological note association to category. 
	 */
	public void testRemove() {
		Date date = this.parseDateText("01/01/2018");
		Offender offender = this.offenderDelegate.createWithoutIdentity("Schmoe", "Joe", "Not So", null);
		String narrative = new String("This is the narrative of the test chronological note");
		ChronologicalNote note;
		ChronologicalNoteCategoryGroup group;
		ChronologicalNoteCategory category;
		String title = "title";
		try {
			note = this.chronologicalNoteDelegate.create(date, offender, title, narrative);
			group = this.chronologicalNoteCategoryGroupDelegate.create("Group", (short) 1, true);
			category = this.chronologicalNoteCategoryDelegate.create("categoryName", group, true);
			this.chronologicalNoteService.associateCategory(note, category);
		} catch (ChronologicalNoteCategoryExistsException e) {
			throw new RuntimeException("Chronological note category exists: " + e);
		} catch (ChronologicalNoteExistsException e) {
			throw new RuntimeException("Chronological note exists: " + e);
		} catch (ChronologicalNoteCategoryGroupExistsException e) {
			throw new RuntimeException("Chronological note category group exists: " + e);
		}
		//Action
		this.chronologicalNoteService.dissociateCategory(note, category);
		//Assertion
		assert !this.chronologicalNoteService.findAssociatedCategories(note).contains(category)
			: "Category not dissociated from note.";
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