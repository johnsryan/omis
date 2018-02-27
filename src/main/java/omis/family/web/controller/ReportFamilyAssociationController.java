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
package omis.family.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omis.beans.factory.PropertyEditorFactory;
import omis.family.domain.FamilyAssociation;
import omis.family.report.FamilyAssociationReportService;
import omis.family.report.FamilyAssociationSummary;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

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

/**
 * Controller for family association report.
 * 
 * @author Yidong Li
 * @version 0.1.5 (Feb 23, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/family")
@PreAuthorize("hasRole('USER')")
public class ReportFamilyAssociationController {
	/* views */
	private static final String LIST_VIEW_NAME = "family/list";
	private static final String FAMILY_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME 
		= "family/includes/familyAssociationRowActionMenu";
	private static final String FAMILY_ASSOCIATIONS_ACTION_MENU_VIEW_NAME
		= "family/includes/familyAssociationsActionMenu";
	
	/* model keys */
	private static final String FAMILY_ASSOCIATIONS_SUMMARIES_MODEL_KEY 
		= "familyAssociationsSummaries";
	private static final String OFFENDER_MODEL_KEY
		= "offender";
	private static final String FAMILY_MEMBER_OFFENDER_MODEL_KEY 
		= "familyMemberOffender";
	private static final String FAMILY_ASSOCIATION_MODEL_KEY
		= "familyAssociation";
	
/* Report names. */
	
	private static final String FAMILY_ASSOCIATION_LISTING_REPORT_NAME 
		= "/Relationships/Family/Family_Listing";

	private static final String 
		FAMILY_ASSOCIATION_LISTING_LEGACY_REPORT_NAME 
		= "/Relationships/Family/Family_Listing_Legacy";

	private static final String FAMILY_ASSOCIATION_DETAILS_REPORT_NAME 
		= "/Relationships/Family/Family_Details";
	
	private static final String FAMILY_ASSOCIATION_DETAILS_REPORT_REDACTED_NAME 
		= "/Relationships/Family/Family_Details_Redacted";

	/* Report parameter names. */
	
	private static final String FAMILY_ASSOCIATION_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String FAMILY_ASSOCIATION_DETAILS_ID_REPORT_PARAM_NAME 
		= "FAMILY_ASSOC_ID";
		
	/* Message bundles. */
	
	/* Property editor. */
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("familyAssociationPropertyEditorFactory")
	private PropertyEditorFactory familyAssociationPropertyEditorFactory;
		
	/* Services. */
	@Autowired
	@Qualifier("familyAssociationReportService")
	private FamilyAssociationReportService familyAssociationReportService;
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Report runners. */
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Constructor. */
	/** Instantiates a default family association report controller. */
	public ReportFamilyAssociationController() {
		// Default instantiation
	}
	
	/**
	 * Displays a list of family association.
	 * 
	 * @param offender offender
	 * @return view to display the list of family associations
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", required = true) 
		final Offender offender) {
		List<FamilyAssociationSummary> familyAssociationsSummaries
			= new ArrayList<FamilyAssociationSummary>();
		familyAssociationsSummaries = this.familyAssociationReportService
			.findByOffender(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(FAMILY_ASSOCIATIONS_SUMMARIES_MODEL_KEY,
			familyAssociationsSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Returns family association row action menu.
	 * 
	 * @param familyMemberOffender family member offender
	 * @param offender - offender
	 * @param familyAssociation family association
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/familyAssociationRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayFamilyAssociationRowActionMenu(
			@RequestParam(
			value = "familyMemberOffender", required = true)
			final Boolean familyMemberOffender,
			@RequestParam(
			value = "offender", required = true)
			final Offender offender, 
			@RequestParam(value = "familyAssociation", 
			required = true) final FamilyAssociation familyAssociation) {
		ModelMap map = new ModelMap();
		if (familyMemberOffender != null) {
			map.addAttribute(FAMILY_MEMBER_OFFENDER_MODEL_KEY, 
					familyMemberOffender);
		}
		map.addAttribute(FAMILY_ASSOCIATION_MODEL_KEY, familyAssociation);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
			FAMILY_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for family associations action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for family associations action menu
	 */
	@RequestMapping(value = "/familyAssociationsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView familyAssociationsActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(FAMILY_ASSOCIATIONS_ACTION_MENU_VIEW_NAME, map);
	}	
	
	/**
	 * Returns the report for the specified offenders family associations.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/familyAssociationListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportFamilyAssociationListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FAMILY_ASSOCIATION_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				FAMILY_ASSOCIATION_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the legacy report for the specified offenders 
	 * family associations.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/familyAssociationListingLegacyReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportFamilyAssociationListingLegacy(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FAMILY_ASSOCIATION_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				FAMILY_ASSOCIATION_LISTING_LEGACY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified family association.
	 * 
	 * @param familyAssociation family association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/familyAssociationDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportFamilyAssociationDetails(@RequestParam(
			value = "familyAssociation", required = true)
			final FamilyAssociation familyAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FAMILY_ASSOCIATION_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(familyAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				FAMILY_ASSOCIATION_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the redacted report for the specified family association.
	 * 
	 * @param familyAssociation family association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/familyAssociationDetailsReportRedacted.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportFamilyAssociationDetailsRedacted(
			@RequestParam(value = "familyAssociation", required = true)
			final FamilyAssociation familyAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FAMILY_ASSOCIATION_DETAILS_ID_REPORT_PARAM_NAME,
			Long.toString(familyAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				FAMILY_ASSOCIATION_DETAILS_REPORT_REDACTED_NAME,
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
		binder.registerCustomEditor(FamilyAssociation.class,
			this.familyAssociationPropertyEditorFactory.createPropertyEditor());
	}	
}