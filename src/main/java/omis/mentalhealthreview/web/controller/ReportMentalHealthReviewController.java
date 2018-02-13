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
package omis.mentalhealthreview.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.report.MentalHealthReviewSummary;
import omis.mentalhealthreview.report.MentalHealthReviewSummaryReportService;

/**
 * Controller for reporting mental health reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/mentalHealthReview")
public class ReportMentalHealthReviewController {

	/* View names. */
	
	private static final String VIEW_NAME = "mentalHealthReview/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"mentalHealthReview/includes/mentalHealthReviewsActionMenu";

	/* Model keys. */
	
	private static final String MENTAL_HEALTH_REVIEW_SUMMARIES_MODEL_KEY = 
			"mentalHealthReviewSummaries";
	
	private static final String MENTAL_HEALTH_REVIEW_MODEL_KEY = 
			"mentalHealthReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("mentalHealthReviewPropertyEditorFactory")
	private PropertyEditorFactory mentalHealthReviewPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("mentalHealthReviewSummaryReportService")
	private MentalHealthReviewSummaryReportService 
			mentalHealthReviewSummaryReportService;
	
	/* Helpers. */

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for mental health reviews. */
	public ReportMentalHealthReviewController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists mental health reviews
	 * 
	 * @return screen that lists mental health reviews
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		List<MentalHealthReviewSummary> mentalHealthReviews;
			mentalHealthReviews = this.mentalHealthReviewSummaryReportService
					.findMentalHealthReviewSummariesByOffender(offender);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(MENTAL_HEALTH_REVIEW_SUMMARIES_MODEL_KEY, 
				mentalHealthReviews);
		this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for mental health reviews.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return action menu for mental health reviews
	 */
	@RequestMapping(value = "/mentalHealthReviewsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "mentalHealthReview", required = false)
				final MentalHealthReview mentalHealthReview,
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		if (mentalHealthReview == null && offender == null) {
			throw new IllegalArgumentException(
					"Mental health review or offender required.");
		}
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		if (mentalHealthReview != null) {
			mav.addObject(MENTAL_HEALTH_REVIEW_MODEL_KEY, 
					mentalHealthReview);
		}
		if (offender != null) {
			mav.addObject(OFFENDER_MODEL_KEY, offender);
		}
		return mav;
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(MentalHealthReview.class, 
				this.mentalHealthReviewPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
	}
}
