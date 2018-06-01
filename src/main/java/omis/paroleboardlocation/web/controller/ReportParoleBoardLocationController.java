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
package omis.paroleboardlocation.web.controller;

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
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.report.ParoleBoardLocationSummary;
import omis.paroleboardlocation.report.ParoleBoardLocationSummaryReportService;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller for displaying parole board locations.
 * 
 * @author Josh Divine
 * @author Sierra Rosales
 * @version 0.1.0 (Feb 21, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardLocation")
public class ReportParoleBoardLocationController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardLocation/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardLocation/includes/paroleBoardLocationsActionMenu";

	/* Model keys. */
	
	private static final String PAROLE_BOARD_LOCATION_SUMMARIES_MODEL_KEY = 
			"paroleBoardLocationSummaries";
	
	private static final String PAROLE_BOARD_LOCATION_MODEL_KEY = 
			"paroleBoardLocation";
	
	/* Report Names. */
	
	private static final String BOARD_LOCATION_DETAILS_REPORT_NAME
		= "/BOPP/Parole_Board_Location_Details";
	
	/* Report Parameters. */
	
	private static final String BOARD_LOCATION_DETAILS_ID_REPORT_PARAM_NAME
		= "BOARD_LOCATION_ID";		
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleBoardLocationPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardLocationPropertyEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("paroleBoardLocationSummaryReportService")
	private ParoleBoardLocationSummaryReportService 
			paroleBoardLocationSummaryReportService;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;	
	
	/* Constructors. */
	
	/** Instantiates controller for parole board locations. */
	public ReportParoleBoardLocationController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists parole board locations
	 * 
	 * @return screen that lists parole board locations
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list() {
		List<ParoleBoardLocationSummary> locationSummaries = this
				.paroleBoardLocationSummaryReportService
				.findParoleBoardLocationSummaries();
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_LOCATION_SUMMARIES_MODEL_KEY, 
				locationSummaries);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for parole board locations.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @return action menu for parole board locations
	 */
	@RequestMapping(value = "/paroleBoardLocationsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleBoardLocation", required = false)
				final ParoleBoardLocation paroleBoardLocation) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_BOARD_LOCATION_MODEL_KEY, 
				paroleBoardLocation);
		return mav;
	}
	
	/**
	 * Returns the report for the specified board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/boardLocationDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportBoardLocationDetails(@RequestParam(
			value = "paroleBoardLocation", required = true)
			final ParoleBoardLocation paroleBoardLocation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(BOARD_LOCATION_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(paroleBoardLocation.getId()));
		byte[] doc = this.reportRunner.runReport(
				BOARD_LOCATION_DETAILS_REPORT_NAME,
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
		binder.registerCustomEditor(ParoleBoardLocation.class, 
				this.paroleBoardLocationPropertyEditorFactory
				.createPropertyEditor());
	}
}
