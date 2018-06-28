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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.report.MentalHealthReviewSummary;
import omis.mentalhealthreview.report.MentalHealthReviewSummaryReportService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller for reporting mental health reviews.
 * 
 * @author Josh Divine
 * @author Sierra Haynes
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
	
	/* Report names. */

	private static final String MENTAL_HEALTH_REVIEW_LISTING_REPORT_NAME =
			"/CaseManagement/MentalHealthReviews/Mental_Health_Reviews_Listing";
	
	private static final String MENTAL_HEALTH_REVIEW_DETAILS_REPORT_NAME =
			"/CaseManagement/MentalHealthReviews/Mental_Health_Review_Details";	
	
	/* Report parameter names. */

	private static final String MENTAL_HEALTH_REVIEW_ID_REPORT_PARAM_NAME =
			"MENTAL_REVIEW_ID";
	
	private static final String DOC_ID_REPORT_PARAM_NAME =
			"DOC_ID";	
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;	
	
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
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/mentalReviewListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportMentalHealthReview(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				MENTAL_HEALTH_REVIEW_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified mental health review.
	 * 
	 * @param mentalHealthReview - Mental Health Review
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/mentalHealthReviewDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportMentalHealthReviewDetails(@RequestParam(
			value = "mentalHealthReview", required = true)
			final MentalHealthReview mentalHealthReview,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(MENTAL_HEALTH_REVIEW_ID_REPORT_PARAM_NAME,
				Long.toString(mentalHealthReview.getId()));
		byte[] doc = this.reportRunner.runReport(
				MENTAL_HEALTH_REVIEW_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
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
