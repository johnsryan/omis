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

import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.assessment.service.AssessmentDocumentAssociationService;
import omis.assessment.service.delegate.AssessmentDocumentAssociationDelegate;
import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
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
 * Tests method to create assessment document associations.
 *
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Apr 11, 2018)
 * @since OMIS 3.0
 */
@Test
public class
	AssessmentDocumentAssociationServiceCreateAssessmentDocumentAssociationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private DocumentDelegate documentDelegate;

	@Autowired
	private AdministeredQuestionnaireDelegate administeredQuestionnaireDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private QuestionnaireTypeDelegate questionnaireTypeDelegate;
	
	@Autowired
	private QuestionnaireCategoryDelegate questionnaireCategoryDelegate;
	
	@Autowired
	@Qualifier("assessmentDocumentAssociationDelegate")
	private AssessmentDocumentAssociationDelegate
		assessmentDocumentAssociationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("assessmentDocumentAssociationService")
	private AssessmentDocumentAssociationService
		assessmentDocumentAssociationService;

	/* Test methods. */

	@Test
	public void testCreateAssessmentDocumentAssociation()
			throws DuplicateEntityFoundException {
		// Arrangements
		Document document = this.documentDelegate.create(
				this.parseDateText("12/21/2012"), "File", ".file",
				"Filey File-o");
		Date date = this.parseDateText("12/21/2012");
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

		// Action
		AssessmentDocumentAssociation assessmentDocumentAssociation =
				this.assessmentDocumentAssociationService
				.createAssessmentDocumentAssociation(document, date,
						administeredQuestionnaire);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("document", document)
			.addExpectedValue("date", date)
			.addExpectedValue("administeredQuestionnaire",
					administeredQuestionnaire)
			.performAssertions(assessmentDocumentAssociation);
	}

	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException()
			throws DuplicateEntityFoundException {
		// Arrangements
		Document document = this.documentDelegate.create(
				this.parseDateText("12/21/2012"), "File", ".file",
				"Filey File-o");
		Date date = this.parseDateText("12/21/2012");
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
		this.assessmentDocumentAssociationDelegate.create(document, date,
				administeredQuestionnaire);
		
		// Action		
		this.assessmentDocumentAssociationService
			.createAssessmentDocumentAssociation(document, date,
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