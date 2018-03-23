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
package omis.assessment.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.assessment.report.AssessmentDocumentSummaryReportService;
import omis.beans.factory.PropertyEditorFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Document Report Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 9, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment/document")
@PreAuthorize("hasRole('USER')")
public class AssessmentDocumentReportController {
	
	private static final String LIST_VIEW_NAME = "/assessment/document/list";
	
	private static final String ASSESSMENT_DOCUMENTS_ACTION_MENU_VIEW_NAME =
			"/assessment/document/includes/assessmentDocumentsActionMenu";
	
	private static final String ASSESSMENT_DOCUMENT_ROW_ACTION_MENU_VIEW_NAME =
			"/assessment/document/includes/assessmentDocumentRowActionMenu";
	
	/* Model Keys */
	
	private static final String ASSESSMENT_DOCUMENT_SUMMARIES_MODEL_KEY =
			"assessmentDocumentSummaries";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_DOCUMENT_ASSOCIATION_MODEL_KEY =
			"assessmentDocumentAssociation";
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
				administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("assessmentDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory
				assessmentDocumentAssociationPropertyEditorFactory;
	
	/* Service */
	
	@Autowired
	@Qualifier("assessmentDocumentSummaryReportService")
	private AssessmentDocumentSummaryReportService
				assessmentDocumentSummaryReportService;
	
	/**
	 * Default constructor for Assessment Document Report Controller.
	 */
	public AssessmentDocumentReportController() {
	}
	
	/**
	 * Displays the Model And View of the Assessment Documents list screen.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model And View of the Assessment Documents list screen.
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_LIST') or hasRole('ADMIN')")
	public ModelAndView showContent(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSESSMENT_DOCUMENT_SUMMARIES_MODEL_KEY,
				this.assessmentDocumentSummaryReportService
				.findAssessmentDocumentSummariesByAdministeredQuestionnaire(
						administeredQuestionnaire));
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/**
	 * Displays the Model And View for the Assessment Documents action menu.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model And View for the Assessment Documents action menu.
	 */
	@RequestMapping(value = "/assessmentDocumentsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentDocumentsActionMenu(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(
				ASSESSMENT_DOCUMENTS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the Model and View for the Assessment Documents row action menu.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @return Model and View for the Assessment Documents row action menu.
	 */
	@RequestMapping(value = "/assessmentDocumentsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentDocumentRowActionMenu(
			@RequestParam(value = "assessmentDocumentAssociation",
			required = true)
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSESSMENT_DOCUMENT_ASSOCIATION_MODEL_KEY,
				assessmentDocumentAssociation);
		return new ModelAndView(
				ASSESSMENT_DOCUMENT_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Init binder.
	 * @param binder - web data binder.
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(AssessmentDocumentAssociation.class,
				this.assessmentDocumentAssociationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class,
				this.administeredQuestionnairePropertyEditorFactory
					.createPropertyEditor());
	}
}
