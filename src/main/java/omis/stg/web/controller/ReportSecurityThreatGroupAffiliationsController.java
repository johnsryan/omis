package omis.stg.web.controller;

import java.util.Date;
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
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.stg.domain.SecurityThreatGroup;
import omis.stg.domain.SecurityThreatGroupActivity;
import omis.stg.domain.SecurityThreatGroupAffiliation;
import omis.stg.domain.SecurityThreatGroupChapter;
import omis.stg.domain.SecurityThreatGroupRank;
import omis.stg.report.SecurityThreatGroupActivityInvolvementSummary;
import omis.stg.report.SecurityThreatGroupActivityReportService;
import omis.stg.report.SecurityThreatGroupActivitySummary;
import omis.stg.report.SecurityThreatGroupAffiliationReportService;
import omis.stg.report.SecurityThreatGroupAffiliationSummary;
import omis.stg.service.SecurityThreatGroupAffiliationService;

/**
 * Controller to report security threat group affiliations.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Sep 17, 2015)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/stg")
@PreAuthorize("hasRole('USER')")
public class ReportSecurityThreatGroupAffiliationsController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME = "stg/list";
	
	private static final String CHAPTER_OPTIONS_VIEW_NAME
			= "stg/affiliation/includes/chapterOptions";
	
	private static final String RANK_OPTIONS_VIEW_NAME
	= "stg/affiliation/includes/rankOptions";
	
	private static final String INVOLVED_OFFENDERS_VIEW_NAME 
	= "stg/activity/includes/involvedOffenders";
	
	private static final String ACTIVITY_INVOLVED_OFFENDERS_VIEW_NAME
	= "stg/activity/includes/associatedOffendersSummaryList";
	
	/* Action menu view names. */
	
	private static final String AFFILIATIONS_ACTION_MENU_VIEW_NAME
		= "stg/affiliation/includes/affiliationsActionMenu";
	
	private static final String AFFILIATION_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
		= "stg/affiliation/includes/affiliationNoteItemsActionMenu";
	
	private static final String ACTIVITY_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
		= "stg/activity/includes/activityNoteItemsActionMenu";
	
	private static final String ACTIVITY_INVOLVEMENT_ITEMS_ACTION_MENU_VIEW_NAME
		= "stg/activity/includes/activityInvolvementItemsActionMenu";
	
	private static final String ACTIVITIES_ACTION_MENU_VIEW_NAME 
	= "stg/activity/includes/activitiesActionMenu";
	
	/* Model keys. */

	private static final String AFFILIATION_SUMMARIES_MODEL_KEY
		= "affiliationSummaries";
	
	private static final String ACTIVITY_SUMMARIES_MODEL_KEY 
	= "activitySummaries";
	
	private static final String INVOLVEMENT_SUMMARIES_MODEL_KEY 
	= "involvementSummaries";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String AFFILIATION_MODEL_KEY = "affiliation";
	
	private static final String ACTIVITY_MODEL_KEY = "activity";
	
	private static final String DEFAULT_CHAPTER_MODEL_KEY = "defaultChapter";
	
	private static final String CHAPTERS_MODEL_KEY = "chapters";
	
	private static final String DEFAULT_RANK_MODEL_KEY = "defaultRank";
	
	private static final String RANKS_MODEL_KEY = "ranks";
	
	/* Services. */
	
	@Autowired
	@Qualifier("securityThreatGroupAffiliationReportService")
	private SecurityThreatGroupAffiliationReportService
	securityThreatGroupAffiliationReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("securityThreatGroupAffiliationPropertyEditorFactory")
	private PropertyEditorFactory
	securityThreatGroupAffiliationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("securityThreatGroupActivityPropertyEditorFactory")
	private PropertyEditorFactory
	securityThreatGroupActivityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	@Autowired
	@Qualifier("securityThreatGroupChapterPropertyEditorFactory")
	private PropertyEditorFactory
	securityThreatGroupChapterPropertyEditorFactory;

	@Autowired
	@Qualifier("securityThreatGroupPropertyEditorFactory")
	private PropertyEditorFactory securityThreatGroupPropertyEditorFactory;
	
	@Autowired
	@Qualifier("securityThreatGroupRankPropertyEditorFactory")
	private PropertyEditorFactory securityThreatGroupRankPropertyEditorFactory;
	
	/* Report names. */
	
	private static final String STG_LISTING_REPORT_NAME 
		= "/Safety/SecurityThreatGroup/STG_Listing";
	
	private static final String STG_ACTIVITY_LISTING_REPORT_NAME 
	= "/Safety/SecurityThreatGroup/STG_Affiliation_Activity_Listing";	

	private static final String STG_DETAILS_REPORT_NAME 
		= "/Safety/SecurityThreatGroup/STG_Details";
	
	private static final String STG_ACTIVITY_DETAILS_REPORT_NAME 
	= "/Safety/SecurityThreatGroup/STG_Activity_Details";

	/* Report parameter names. */
	
	private static final String DOC_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String STG_DETAILS_ID_REPORT_PARAM_NAME 
		= "ID";
	
	private static final String STG_ACTIVITY_DETAILS_ID_REPORT_PARAM_NAME
		= "STG_ACTIVITY_ID";
	
	/* Services. */
	
	@Autowired
	@Qualifier("securityThreatGroupAffiliationService")
	private SecurityThreatGroupAffiliationService
	securityThreatGroupAffiliationService;
	
	@Autowired
	@Qualifier("securityThreatGroupActivityReportService")
	private SecurityThreatGroupActivityReportService
		securityThreatGroupActivityReportService;
	
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
	 * Instantiation controller to report security threat group affiliations.
	 */
	public ReportSecurityThreatGroupAffiliationsController() {
		// Default instantiation
	}
	
	/**
	 * Displays a list of security threat group affiliations by offender.
	 * 
	 * @param offender offender
	 * @param effectiveDate effectiveDate
	 * @return screen to display list of security threat group affiliations by
	 * offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_AFFILIATION_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate) {
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		List<SecurityThreatGroupAffiliationSummary> affiliationSummaries;
		if (effectiveDate != null) {
			affiliationSummaries
				= this.securityThreatGroupAffiliationReportService
					.summarizeByOffender(offender, effectiveDate);
		} else {
			affiliationSummaries
				= this.securityThreatGroupAffiliationReportService
					.summarizeByOffender(offender, new Date());
		}
		mav.addObject(AFFILIATION_SUMMARIES_MODEL_KEY, affiliationSummaries);
		addOffenderSummary(mav, offender);
		
		List<SecurityThreatGroupActivitySummary> activitySummaries;
		activitySummaries = this.securityThreatGroupActivityReportService
				.summarizeByOffender(offender);
		mav.addObject(ACTIVITY_SUMMARIES_MODEL_KEY, activitySummaries);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	@RequestMapping(value = "/findInvolvedOffenders.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_ACTIVITY_LIST') or hasRole('ADMIN')")
	public ModelAndView findInvolvedOffenders(
			@RequestParam(value = "activity", required = true) 
				final SecurityThreatGroupActivity activity) {
		ModelAndView mav = new ModelAndView(INVOLVED_OFFENDERS_VIEW_NAME);
		List<SecurityThreatGroupActivityInvolvementSummary> involvementSummaries;
		involvementSummaries = this.securityThreatGroupActivityReportService
				.summarizeInvolvement(activity);
		mav.addObject(INVOLVEMENT_SUMMARIES_MODEL_KEY, involvementSummaries);
		return mav;
	}
	
	/**
	 * Returns the report for the specified offenders security threat groups.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/stgListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_AFFILIATION_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSTGListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				STG_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders security threat groups and activity.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/stgAffiliationActivityListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_ACTIVITY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSTGActivityListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				STG_ACTIVITY_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified security threat group affiliation.
	 * 
	 * @param affiliation security threat group affiliation
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/stgDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_AFFILIATION_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSTGDetails(@RequestParam(
			value = "affiliation", required = true)
			final SecurityThreatGroupAffiliation affiliation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(STG_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(affiliation.getId()));
		byte[] doc = this.reportRunner.runReport(
				STG_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified security threat group activities.
	 * 
	 * @param activity security threat group activity
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/stgActivityDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('STG_ACTIVITY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportSTGActivityDetails(@RequestParam(
			value = "activity", required = true)
			final SecurityThreatGroupActivity activity,
			@RequestParam(value = "offender", required = true)
					final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(STG_ACTIVITY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(activity.getId()));
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				STG_ACTIVITY_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* AJAX requests. */
	
	/**
	 * Displays a partial page of chapter options by security threat group.
	 * 
	 * @param group Security threat group for the chapters of which to display
	 * @param chapter optional selected chapter
	 * @return partial page of chapter options by group
	 */
	@RequestMapping(value = "/findChaptersByGroup.html", 
			method = RequestMethod.GET)
	public ModelAndView findChaptersByGroup(
			@RequestParam(value = "group", required = true)
				final SecurityThreatGroup group,
			@RequestParam(value = "chapter", required = false)
				final SecurityThreatGroupChapter chapter) {
		ModelAndView mav = new ModelAndView(CHAPTER_OPTIONS_VIEW_NAME);
		mav.addObject(DEFAULT_CHAPTER_MODEL_KEY, chapter);
		List<SecurityThreatGroupChapter> chapters = this
				.securityThreatGroupAffiliationService
				.findChaptersByGroup(group);
		mav.addObject(CHAPTERS_MODEL_KEY, chapters);
		return mav;
	}
	
	/**
	 * Displays a partial page of chapter options by security threat group.
	 * 
	 * @param group Security threat group for the chapters of which to display
	 * @param rank optional selected chapter
	 * @return partial page of chapter options by group
	 */
	@RequestMapping(value = "/findRanksByGroup.html", 
			method = RequestMethod.GET)
	public ModelAndView findRanksByGroup(
			@RequestParam(value = "group", required = true)
				final SecurityThreatGroup group,
			@RequestParam(value = "rank", required = false)
				final SecurityThreatGroupRank rank) {
		ModelAndView mav = new ModelAndView(RANK_OPTIONS_VIEW_NAME);
		mav.addObject(DEFAULT_RANK_MODEL_KEY, rank);
		List<SecurityThreatGroupRank> ranks = this
				.securityThreatGroupAffiliationService
				.findRanksByGroup(group);
		mav.addObject(RANKS_MODEL_KEY, ranks);
		return mav;
	}
	
	/* Helper methods. */
	
	// Adds offender summary to model and view
	private void addOffenderSummary(final ModelAndView mav,
			final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	/* Action menus. */
	
	/**
	 * Returns affiliations action menu.
	 * 
	 * @param offender offender
	 * @param affiliation affiliation
	 * @return affiliations action menu
	 */
	@RequestMapping(
			value = "/affiliationsActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showAffiliationsActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "affiliation", required = false)
				final SecurityThreatGroupAffiliation affiliation) {
		ModelAndView mav = new ModelAndView(AFFILIATIONS_ACTION_MENU_VIEW_NAME);
		if (offender != null) {
			mav.addObject(OFFENDER_MODEL_KEY, offender);
		}
		if (affiliation != null) {
			mav.addObject(AFFILIATION_MODEL_KEY, affiliation);
		}
		return mav;
	}
	
	/**
	 * Returns activities action menu.
	 * 
	 * @param offender offender
	 * @return activities action menu
	 */
	@RequestMapping(
			value = "/activitiesActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showActivitiesActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "activity", required = false)
				final SecurityThreatGroupActivity activity) {
		ModelAndView mav = new ModelAndView(ACTIVITIES_ACTION_MENU_VIEW_NAME);
		if (offender != null) {
			mav.addObject(OFFENDER_MODEL_KEY, offender);
		}
		if (activity != null) {
			mav.addObject(ACTIVITY_MODEL_KEY, activity);
		}			
		return mav;
	}
	
	/**
	 * Displays the action menu for security threat group affiliation note items.
	 * 
	 * @return model and view for security threat group affiliation note items 
	 * action menu
	 */
	@RequestMapping(value = "/affiliationNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAffiliationNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				AFFILIATION_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for activity note items.
	 * 
	 * @return model and view for activity note items action menu
	 */
	@RequestMapping(value = "/activityNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayActivityNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				ACTIVITY_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for involvement items.
	 * 
	 * @return model and view for involvement items action menu
	 */
	@RequestMapping(value = "/activityInvolvementItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayActivityInvolvementItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				ACTIVITY_INVOLVEMENT_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns activity involved offenders. 
	 * 
	 * @param offender offender
	 * @return activity involved offenders
	 */
	@RequestMapping(
			value = "/showInvolvedOffenders.html", method = RequestMethod.GET)
	public ModelAndView showInvolvedOffenders(
			@RequestParam(value = "activity", required = true)
				final SecurityThreatGroupActivity activity) {
		ModelAndView mav = new ModelAndView(ACTIVITY_INVOLVED_OFFENDERS_VIEW_NAME);
		List<SecurityThreatGroupActivityInvolvementSummary> involvementItems 
		= this.securityThreatGroupActivityReportService
		.summarizeInvolvement(activity);
		mav.addObject(INVOLVEMENT_SUMMARIES_MODEL_KEY, involvementItems);
		return mav;
	}
	
	/* Init binder. */
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder init binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(SecurityThreatGroupAffiliation.class,
				this.securityThreatGroupAffiliationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(SecurityThreatGroupActivity.class,
				this.securityThreatGroupActivityPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(SecurityThreatGroup.class,
				this.securityThreatGroupPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(SecurityThreatGroupChapter.class,
				this.securityThreatGroupChapterPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(SecurityThreatGroupRank.class,
				this.securityThreatGroupRankPropertyEditorFactory
					.createPropertyEditor());
	}
}
