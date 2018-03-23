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

import omis.assessment.service.AssessmentDocumentAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireType;
import omis.assessment.service.delegate.AssessmentDocumentAssociationDelegate;
import omis.document.service.delegate.DocumentDelegate;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireDelegate;
import omis.questionnaire.service.delegate.QuestionnaireCategoryDelegate;
import omis.questionnaire.service.delegate.QuestionnaireTypeDelegate;

/**
 * Tests method to TODO.
 *
 * @author Annie Wahl
 * @version 0.0.1 (Mar 15, 2018)
 * @since OMIS 3.0
 */
@Test
public class
	AssessmentDocumentAssociationServiceUpdateAssessmentDocumentAssociationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private AssessmentDocumentAssociationDelegate
		assessmentDocumentAssociationDelegate;

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

	/* Services. */

	@Autowired
	@Qualifier("assessmentDocumentAssociationService")
	private AssessmentDocumentAssociationService
		assessmentDocumentAssociationService;

	/* Test methods. */

	@Test
	public void testUpdateAssessmentDocumentAssociation()
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
		AssessmentDocumentAssociation assessmentDocumentAssociation =
				this.assessmentDocumentAssociationDelegate.create(document,
						date, administeredQuestionnaire);
		Document document2 = this.documentDelegate.create(
				this.parseDateText("10/31/2013"), "File2", ".file2",
				"Filey File-o");
		Date date2 = this.parseDateText("10/31/2013");
		
		// Action
		assessmentDocumentAssociation =
				this.assessmentDocumentAssociationService
				.updateAssessmentDocumentAssociation(
						assessmentDocumentAssociation, document2, date2,
						administeredQuestionnaire);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("document", document2)
			.addExpectedValue("date", date2)
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
		AssessmentDocumentAssociation assessmentDocumentAssociation =
				this.assessmentDocumentAssociationDelegate.create(document,
						date, administeredQuestionnaire);
		Document document2 = this.documentDelegate.create(
				this.parseDateText("10/31/2013"), "File2", ".file2",
				"Filey File-o");
		Date date2 = this.parseDateText("10/31/2013");
		AssessmentDocumentAssociation assessmentDocumentAssociation2 =
				this.assessmentDocumentAssociationDelegate.create(document2,
						date2, administeredQuestionnaire);
		
		// Action
		assessmentDocumentAssociation =
				this.assessmentDocumentAssociationService
				.updateAssessmentDocumentAssociation(
						assessmentDocumentAssociation, document2, date2,
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