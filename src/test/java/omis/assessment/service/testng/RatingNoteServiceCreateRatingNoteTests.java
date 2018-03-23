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
package omis.assessment.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.assessment.domain.RatingNote;
import omis.assessment.service.RatingNoteService;
import omis.assessment.service.delegate.RatingNoteDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireDelegate;
import omis.questionnaire.service.delegate.QuestionnaireCategoryDelegate;
import omis.questionnaire.service.delegate.QuestionnaireTypeDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create rating notes.
 *
 * @author Josh Divine
 * @version 0.0.1 (Mar 14, 2018)
 * @since OMIS 3.0
 */
@Test
public class RatingNoteServiceCreateRatingNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private QuestionnaireTypeDelegate questionnaireTypeDelegate;
	
	@Autowired
	private QuestionnaireCategoryDelegate questionnaireCategoryDelegate;
	
	@Autowired
	private AdministeredQuestionnaireDelegate administeredQuestionnaireDelegate;

	@Autowired
	private RatingNoteDelegate ratingNoteDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("ratingNoteService")
	private RatingNoteService ratingNoteService;

	/* Test methods. */

	/**
	 * Tests the creation of rating notes.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateRatingNote() throws DuplicateEntityFoundException {
		// Arrangements
		Person answerer = this.personDelegate.create("Answerer", "The", null, 
				null);
		Person assessor = this.personDelegate.create("Assessor", "The", null, 
				null);
		QuestionnaireCategory questionnaireCategory = this
				.questionnaireCategoryDelegate.create("Category", true, null);
		QuestionnaireType questionnaireType = this.questionnaireTypeDelegate
				.create("Title", "Title", 1, questionnaireCategory, true, 
						this.parseDateText("01/01/2000"), null, "Help");
		AdministeredQuestionnaire administeredQuestionnaire = this
				.administeredQuestionnaireDelegate.create(answerer, false, 
						"Comments", assessor, this.parseDateText("01/01/2018"), 
						questionnaireType);
		Date date = this.parseDateText("01/01/2018");
		String description = "Description";

		// Action
		RatingNote ratingNote = this.ratingNoteService.createRatingNote(date, 
				description, administeredQuestionnaire);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("date", date)
			.addExpectedValue("description", description)
			.addExpectedValue("administeredQuestionnaire", 
					administeredQuestionnaire)
			.performAssertions(ratingNote);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person answerer = this.personDelegate.create("Answerer", "The", null, 
				null);
		Person assessor = this.personDelegate.create("Assessor", "The", null, 
				null);
		QuestionnaireCategory questionnaireCategory = this
				.questionnaireCategoryDelegate.create("Category", true, null);
		QuestionnaireType questionnaireType = this.questionnaireTypeDelegate
				.create("Title", "Title", 1, questionnaireCategory, true, 
						this.parseDateText("01/01/2000"), null, "Help");
		AdministeredQuestionnaire administeredQuestionnaire = this
				.administeredQuestionnaireDelegate.create(answerer, false, 
						"Comments", assessor, this.parseDateText("01/01/2018"), 
						questionnaireType);
		Date date = this.parseDateText("01/01/2018");
		String description = "Description";
		this.ratingNoteDelegate.create(date, description, 
				administeredQuestionnaire);
		
		// Action
		this.ratingNoteService.createRatingNote(date, description, 
				administeredQuestionnaire);
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