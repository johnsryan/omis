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
package omis.medicalreview.web.controller;

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
import omis.beans.factory.PropertyEditorFactory;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.report.MedicalReviewSummaryReportService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;

/**
 * Medical Review Report Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 1, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/medicalReview")
@PreAuthorize("hasRole('USER')")
public class MedicalReviewReportController {
	
	/* View Names */
	
	private static final String LIST_VIEW_NAME = "/medicalReview/list";
	
	private static final String MEDICAL_REVIEWS_ACTION_MENU_VIEW_NAME =
			"/medicalReview/includes/medicalReviewsActionMenu";
	
	private static final String MEDICAL_REVIEWS_ROW_ACTION_MENU_VIEW_NAME =
			"/medicalReview/includes/medicalReviewsRowActionMenu";
	
	/* Model Keys */
	
	private static final String MEDICAL_REVIEW_SUMMARIES_MODEL_KEY =
			"medicalReviewSummaries";
	
	private static final String MEDICAL_REVIEW_MODEL_KEY = "medicalReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	/* Controller Model Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("medicalReviewSummaryReportService")
	private MedicalReviewSummaryReportService medicalReviewSummaryReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("medicalReviewPropertyEditorFactory")
	private PropertyEditorFactory medicalReviewPropertyEditorFactory;
	
	/**
	 * Default constructor for Medical Review Report Controller.
	 */
	public MedicalReviewReportController() {
	}
	
	/**
	 * Displays the model and view for the Medical Review list screen.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - model and view for the Medical Review list screen.
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(MEDICAL_REVIEW_SUMMARIES_MODEL_KEY,
				this.medicalReviewSummaryReportService
				.findMedicalReviewSummariesByOffender(offender));
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/* Action Menus */
	
	/**
	 * Displays the model and view for the Medical Reviews action menu.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - model and view for the Medical Reviews action menu
	 */
	@RequestMapping(value = "/medicalReviewsActionMenu.html",
					method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')")
	public ModelAndView medicalReviewsActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(MEDICAL_REVIEWS_ACTION_MENU_VIEW_NAME, map);
	}

	/**
	 * Displays the model and view for the Medical Reviews row action menu.
	 * 
	 * @param medicalReview - Medical Review
	 * @return ModelAndView - model and view for the Medical Reviews
	 * row action menu
	 */
	@RequestMapping(value = "/medicalReviewsRowActionMenu.html",
					method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')")
	public ModelAndView medicalReviewsRowActionMenu(
			@RequestParam(value = "medicalReview", required = true)
				final MedicalReview medicalReview) {
		ModelMap map = new ModelMap();
		map.addAttribute(MEDICAL_REVIEW_MODEL_KEY, medicalReview);
		
		return new ModelAndView(MEDICAL_REVIEWS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder init binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(MedicalReview.class,
				this.medicalReviewPropertyEditorFactory.createPropertyEditor());
	}
}
