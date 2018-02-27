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
package omis.paroleeligibility.web.controller;

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
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.paroleeligibility.report.ParoleEligibilitySummary;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller to report parole eligibilities.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/paroleEligibility")
@PreAuthorize("hasRole('USER')")
public class ReportParoleEligibilityController {
	
	/* View names. */
	
	private static final String LIST_VIEW_NAME = "paroleEligibility/list";
	
	/* Action menu view names. */
	
	private static final String ELIGIBILITIES_ACTION_MENU_VIEW_NAME
		= "paroleEligibility/includes/eligibilitiesActionMenu";
	
	/* Model keys. */
	
	private static final String PAROLE_ELIGIBILITY_SUMMARIES_MODEL_KEY
		= "paroleEligibilitySummaries";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String ELIGIBILITY_MODEL_KEY = "eligibility";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleEligibilityReportService")
	private ParoleEligibilityReportService paroleEligibilityReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Report names. */
	
	private static final String PAROLE_ELIGIBILITY_LISTING_REPORT_NAME
		= "/BOPP/ParoleEligibility/"; // TODO: Add actual path name
	
	/* Report parameter names. */
	
	private static final String DOC_ID_REPORT_PARAM_NAME = "DOC_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructor. */
	
	/**
	 * Instantiation controller to report parole eligibilities.
	 */
	public ReportParoleEligibilityController() {
		// Default instantiation
	}
	
	/**
	 * Displays a list of parole eligibility by offender.
	 * 
	 * @param offender offender
	 * @return screen to display list of parole eligibilities by offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		List<ParoleEligibilitySummary> paroleEligibilitySummaries
			= this.paroleEligibilityReportService.findByOffender(offender);
		mav.addObject(PAROLE_ELIGIBILITY_SUMMARIES_MODEL_KEY, 
				paroleEligibilitySummaries);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Returns the report for the specified parole eligibility.
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/paroleEligibilityListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_ELIGIBILITY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportParoleEligibilityListing(
			@RequestParam(value = "offender", required = true) 
			final Offender offender, 
			@RequestParam(value = "reportFormat", required = true) 
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				PAROLE_ELIGIBILITY_LISTING_REPORT_NAME, reportParamMap, 
				reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(doc, 
				reportFormat);
	}
	
	/* Action menus. */
	
	/**
	 * Returns eligibilities action menu.
	 * 
	 * @param offender offender
	 * @return eligibilities action menu
	 */
	@RequestMapping(
			value = "/eligibilitiesActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showEligibilitiesActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "eligibility", required = false)
				final ParoleEligibility eligibility) {
		ModelAndView mav = new ModelAndView(ELIGIBILITIES_ACTION_MENU_VIEW_NAME);
		if (offender != null) {
			mav.addObject(OFFENDER_MODEL_KEY, offender);
		}
		if (eligibility != null) {
			mav.addObject(ELIGIBILITY_MODEL_KEY, eligibility);
			mav.addObject(HEARING_ANALYSIS_MODEL_KEY, 
					this.paroleEligibilityReportService
					.findHearingAnalysisByParoleEligibility(eligibility));
		}			
		return mav;
	}
	
	/* Init binder. */
	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(ParoleEligibility.class, 
				this.paroleEligibilityPropertyEditorFactory
					.createPropertyEditor());
	}

}
