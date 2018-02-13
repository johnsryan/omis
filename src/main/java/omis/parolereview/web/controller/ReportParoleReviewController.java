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
package omis.parolereview.web.controller;

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
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.report.ParoleReviewSummary;
import omis.parolereview.report.ParoleReviewSummaryReportService;

/**
 * Controller for reporting parole reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleReview")
public class ReportParoleReviewController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleReview/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleReview/includes/paroleReviewsActionMenu";

	/* Model keys. */
	
	private static final String PAROLE_REVIEW_SUMMARIES_MODEL_KEY = 
			"paroleReviewSummaries";
	
	private static final String PAROLE_REVIEW_MODEL_KEY = "paroleReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleReviewPropertyEditorFactory")
	private PropertyEditorFactory paroleReviewPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("paroleReviewSummaryReportService")
	private ParoleReviewSummaryReportService paroleReviewSummaryReportService;
	
	/* Helpers. */

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for parole reviews. */
	public ReportParoleReviewController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists parole reviews
	 * 
	 * @return screen that lists parole reviews
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		List<ParoleReviewSummary> paroleReviews = 
				this.paroleReviewSummaryReportService
				.findParoleReviewSummariesByOffender(offender);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_REVIEW_SUMMARIES_MODEL_KEY, paroleReviews);
		this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for parole reviews.
	 * 
	 * @param paroleReview parole review
	 * @return action menu for parole reviews
	 */
	@RequestMapping(value = "/paroleReviewsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleReview", required = false)
				final ParoleReview paroleReview,
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		if (paroleReview == null && offender == null) {
			throw new IllegalArgumentException(
					"Parole review or offender required.");
		}
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		if (paroleReview != null) {
			mav.addObject(PAROLE_REVIEW_MODEL_KEY, paroleReview);
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
		binder.registerCustomEditor(ParoleReview.class, 
				this.paroleReviewPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
	}
}
