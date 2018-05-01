/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.victim.web.controller;

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
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.victim.domain.VictimAssociation;
import omis.victim.report.VictimAssociationReportService;
import omis.victim.report.VictimAssociationSummary;
import omis.victim.web.controller.delegate.VictimSummaryModelDelegate;

/**
 * Controller for listing victim associations.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 3, 2015)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/victim/association")
public class ReportVictimAssociationsController {
	
	/* View Names */
	
	private static final String LIST_BY_OFFENDER_VIEW_NAME
		= "victim/association/list";

	private static final String ACTION_MENU_VIEW_NAME
		= "victim/association/includes/victimAssociationsActionMenu";
	
	private static final String 
		VICTIM_ASSOCIATION_SEARCH_ROW_ACTION_MENU_VIEW_NAME
			= "victim/association/includes"
					+ "/victimAssociationSearchRowActionMenu";
	
	/* Model Keys */
	
	private static final String VICTIM_ASSOCIATION_SUMMARIES_MODEL_KEY
		= "victimAssociationSummaries";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String VICTIM_MODEL_KEY = "victim";
	
	private static final String VICTIM_ASSOCIATION_MODEL_KEY
		= "victimAssociation";

	private static final String REDIRECT_TARGET_MODEL_KEY = "redirectTarget";

	private static final String HAS_NOTES_MODEL_KEY
		= "victimAssociationHasNotes";
	
	private static final String OFFENDER_YES_NO_MODEL_KEY = "offenderYesNo";
	
	private static final String VICTIM_ASSOCIATION_EXISTS_MODEL_KEY 
		= "victimAssociationExists";
	
	/* Services */
	
	@Autowired
	@Qualifier("victimAssociationReportService")
	private VictimAssociationReportService victimAssociationReportService;

	/* Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("victimSummaryModelDelegate")
	private VictimSummaryModelDelegate victimSummaryModelDelegate;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("victimAssociationPropertyEditorFactory")
	private PropertyEditorFactory victimAssociationPropertyEditorFactory;

	/* Report names. */
	
	private static final String VICTIM_LISTING_REPORT_NAME 
		= "/Relationships/Victims/Victims_Listing";
	
	private static final String VICTIM_LISTING_LEGACY_REPORT_NAME 
		= "/Relationships/Victims/Victim_Listing_Legacy";

	private static final String VICTIM_PERS_DETAILS_REPORT_NAME 
		= "/Relationships/Victims/Victim_Personal_Details";
	
	private static final String VICTIM_PERS_DETAILS_REDACTED_REPORT_NAME 
		= "/Relationships/Victims/Victim_Personal_Details_Redacted";
	
	private static final String VICTIM_ASSOC_DETAILS_REPORT_NAME 
		= "/Relationships/Victims/Victim_Association_Details";

    private static final String VICTIM_ASSOC_DETAILS_REDACTED_REPORT_NAME 
		= "/Relationships/Victims/Victim_Association_Details_Redacted";	

	private static final String VICTIM_IMPACT_REPORT_NAME 
	    = "/Relationships/Victims/Victim_Impact_Statement";

    private static final String VICTIM_IMPACT_KID_REPORT_NAME 
	    = "/Relationships/Victims/Victim_Impact_for_Kids";
    
	/* Report parameter names. */
	
	private static final String VICTIM_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String VICTIM_LISTING_LEGACY_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String VICTIM_DETAILS_ID_REPORT_PARAM_NAME 
		= "VICTIM_ASSOC_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;

	/* Constructors */
	
	/** Instantiates controller for victim associations. */
	public ReportVictimAssociationsController() {
		// Default instantiation
	}
	
	/* URL Invokable Methods */
	
	/**
	 * Displays a list of victims for offender.
	 * 
	 * @param offender offender
	 * @return screen to display victims for offender
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_LIST')")
	@RequestMapping(value = "/listByOffender.html", method = RequestMethod.GET)
	public ModelAndView listByOffender(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		List<VictimAssociationSummary> victimAssociationSummaries
			= this.victimAssociationReportService.findSummariesByOffender(
					offender);
		ModelAndView mav = new ModelAndView(LIST_BY_OFFENDER_VIEW_NAME);
		mav.addObject(VICTIM_ASSOCIATION_SUMMARIES_MODEL_KEY,
				victimAssociationSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(REDIRECT_TARGET_MODEL_KEY, VictimRedirectTarget.OFFENDER);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Displays a list of associations for victim.
	 * 
	 * @param victim victim
	 * @return screen to display associations for victim
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_LIST')")
	@RequestMapping(value = "/listByVictim.html", method = RequestMethod.GET)
	public ModelAndView listByVictim(
			@RequestParam(value = "victim", required = true)
				final Person victim) {
		List<VictimAssociationSummary> victimAssociationSummaries
			= this.victimAssociationReportService.findSummariesByVictim(victim);
		ModelAndView mav = new ModelAndView(LIST_BY_OFFENDER_VIEW_NAME);
		mav.addObject(VICTIM_ASSOCIATION_SUMMARIES_MODEL_KEY,
				victimAssociationSummaries);
		mav.addObject(VICTIM_MODEL_KEY, victim);
		mav.addObject(REDIRECT_TARGET_MODEL_KEY, VictimRedirectTarget.VICTIM);
		this.victimSummaryModelDelegate.add(mav.getModelMap(), victim);
		return mav;
	}
	
	/* Action menus */
	
	/**
	 * Displays action menu for victim associations.
	 * 
	 * @param offender offender
	 * @param victim victim
	 * @param victimAssociation victim association
	 * @param redirectTarget redirect target
	 * @return action menu for victim associations
	 */
	@RequestMapping(value = "/associationsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "victim", required = false)
				final Person victim,
			@RequestParam(value = "victimAssociation", required = false)
				final VictimAssociation victimAssociation,
			final VictimRedirectTarget redirectTarget) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(VICTIM_MODEL_KEY, victim);
		mav.addObject(VICTIM_ASSOCIATION_MODEL_KEY, victimAssociation);
		mav.addObject(REDIRECT_TARGET_MODEL_KEY, redirectTarget);
		if (victimAssociation != null) {
			if (this.victimAssociationReportService
					.hasNotes(victimAssociation)) {
				mav.addObject(HAS_NOTES_MODEL_KEY, true);
			}
			mav.addObject(OFFENDER_YES_NO_MODEL_KEY, 
					this.victimAssociationReportService.isOffender(
						victimAssociation.getRelationship().getSecondPerson()));
		}
		return mav;
	}
	
	/**
	 * Model and view of victim association search row action menu.
	 *
	 *
	 * @param offender offender
	 * @param victim victim
	 * @return model and view of action menu
	 */
	@RequestMapping(value = "victimAssociationSearchRowActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView victimAssociationSearchRowActionMenu(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "victim", required = true)
		final Person victim) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_YES_NO_MODEL_KEY, 
			this.victimAssociationReportService.isOffender(victim));
		map.addAttribute(OFFENDER_MODEL_KEY,  offender);	
		map.addAttribute(VICTIM_MODEL_KEY, victim);
		map.addAttribute(VICTIM_ASSOCIATION_EXISTS_MODEL_KEY, 
			this.victimAssociationReportService
			.victimAssociationExists(offender, victim));
		map.addAttribute(VICTIM_ASSOCIATION_MODEL_KEY, 
			this.victimAssociationReportService
			.findVictimAssociation(offender, victim));
		return new ModelAndView(
				VICTIM_ASSOCIATION_SEARCH_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders victims.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the legacy report for the specified offenders victims.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimListingLegacyReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimLegacyListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_LISTING_LEGACY_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_LISTING_LEGACY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified victim.
	 * 
	 * @param victimAssociation victim association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('VICTIM_ASSOCIATION_VIEW') and "
			+ "hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimDetails(@RequestParam(
			value = "victimAssociation", required = true)
			final VictimAssociation victimAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(victimAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_PERS_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the redacted report for the specified victim.
	 * 
	 * @param victimAssociation victim association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimDetailsRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimDetailsRedacted(@RequestParam(
			value = "victimAssociation", required = true)
			final VictimAssociation victimAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(victimAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_PERS_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified victim association.
	 * 
	 * @param victimAssociation victim association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimAssocDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('VICTIM_ASSOCIATION_VIEW') and "
			+ "hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimAssocDetails(@RequestParam(
			value = "victimAssociation", required = true)
			final VictimAssociation victimAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(victimAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_ASSOC_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the redacted report for the specified victim association.
	 * 
	 * @param victimAssociation victim association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/victimAssocDetailsRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimAssocDetailsRedacted(
			@RequestParam(value = "victimAssociation", required = true)
			final VictimAssociation victimAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VICTIM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(victimAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VICTIM_ASSOC_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the victim impact statement.
	 * @param reportFormat report format
	 * @return statement
	 */
	@RequestMapping(value = "/victimImpactStatementReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimImpactStatement(
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		byte[] doc = this.reportRunner.runReport(
				VICTIM_IMPACT_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the victim impact statement for kids.
	 * @param reportFormat report format
	 * @return kids statement
	 */
	@RequestMapping(value = "/victimImpactStatementKidReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVictimImpactStatementKids(
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		byte[] doc = this.reportRunner.runReport(
				VICTIM_IMPACT_KID_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Init Binders */
	
	/**
	 * Registers property editors.
	 * 
	 * @param binder web data binder
	 */
	@InitBinder
	protected void registerPropertyEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(VictimAssociation.class,
				this.victimAssociationPropertyEditorFactory
					.createPropertyEditor());
	}
}