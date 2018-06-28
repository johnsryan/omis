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
package omis.travelpermit.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.report.TravelPermitReportService;
import omis.travelpermit.report.TravelPermitSummary;

/**
 * Report travel permit controller.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (May 22, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/travelPermit")
@PreAuthorize("hasRole('USER')")
public class ReportTravelPermitController {
	
	/* View names. */
	
	private static final String LIST_VIEW_NAME = "travelPermit/list";
	
	private static final String TRAVEL_PERMITS_ACTION_MENU_VIEW_NAME
		= "travelPermit/includes/travelPermitsActionMenu";
	
	private static final String TRAVEL_PERMITS_ROW_ACTION_MENU_VIEW_NAME
		= "travelPermit/includes/travelPermitsRowActionMenu";
	
	/* Model keys. */
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String TRAVEL_PERMITS_MODEL_KEY = "travelPermits";
	
	private static final String TRAVEL_PERMIT_MODEL_KEY = "travelPermit";
	
	/* Report parameter names. */
	
	private static final String TRAVEL_PERMIT_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String TRAVEL_PERMIT_DETAILS_ID_REPORT_PARAM_NAME 
		= "TRAVEL_PERMIT_ID";
	
	/* Report names. */
	
	private static final String TRAVEL_PERMIT_LISTING_REPORT_NAME 
		= "/CaseManagement/TravelPermits/Travel_Permits_Listing";
	
	private static final String TRAVEL_PERMIT_REQUEST_REPORT_NAME 
	    = "/CaseManagement/TravelPermits/Travel_Permit_Request";
	
	private static final String TRAVEL_PERMIT_DETAILS_REPORT_NAME 
		= "/CaseManagement/TravelPermits/Travel_Permit_Details";
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("travelPermitPropertyEditorFactory")
	private PropertyEditorFactory travelPermitPropertyEditorFactory;
	
	/* Services. */
	
	@Autowired
	@Qualifier("travelPermitReportService")
	private TravelPermitReportService travelPermitReportService;
	
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
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/** Instantiates an implementation of travel permit controller. */
	public ReportTravelPermitController() {
		// Default constructor.
	}

	/* URL mapped methods. */
	
	/**
	 * 
	 *
	 *
	 * @param offender
	 * @return
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAVEL_PERMIT_LIST')")
	public ModelAndView list(@RequestParam(value = "offender", required = true)
			final Offender offender) {
		List<TravelPermitSummary> travelPermits 
			= new ArrayList<TravelPermitSummary>();
		travelPermits = this.travelPermitReportService.findByOffender(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(TRAVEL_PERMITS_MODEL_KEY, travelPermits);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Travel permits action menu.
	 *
	 *
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "travelPermitsActionMenu", 
			method = RequestMethod.GET)
	public ModelAndView travelPermitsActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(TRAVEL_PERMITS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Travel permits row action menu.
	 *
	 *
	 * @param offender offender
	 * @param travelPermit travel permit
	 * @return model and view
	 */
	@RequestMapping(value = "travelPermitsRowActionMenu", 
			method = RequestMethod.GET)
	public ModelAndView travelPermitsRowActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "travelPermit", required = true)
			final TravelPermit travelPermit	) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(TRAVEL_PERMIT_MODEL_KEY, travelPermit);
		ModelAndView mav = new ModelAndView(
				TRAVEL_PERMITS_ROW_ACTION_MENU_VIEW_NAME, map);
		return mav;
	}
	
	/**
	 * Returns the report for the specified offenders travel permits.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/travelPermitListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTravelPermitListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(TRAVEL_PERMIT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				TRAVEL_PERMIT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	
	/**
	 * Returns the report for the specified offenders travel permit request.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/travelPermitRequestReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTravelPermitRequest(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(TRAVEL_PERMIT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				TRAVEL_PERMIT_REQUEST_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified travel permit.
	 * 
	 * @param travelPermit travel permit
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/travelPermitDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTravelPermitDetails(@RequestParam(
			value = "travelPermit", required = true)
			final TravelPermit travelPermit,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(TRAVEL_PERMIT_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(travelPermit.getId()));
		byte[] doc = this.reportRunner.runReport(
				TRAVEL_PERMIT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(TravelPermit.class, 
				this.travelPermitPropertyEditorFactory.createPropertyEditor());
	}
}