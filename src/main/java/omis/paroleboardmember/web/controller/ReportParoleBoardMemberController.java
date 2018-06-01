package omis.paroleboardmember.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleboardmember.report.ParoleBoardMemberSummary;
import omis.paroleboardmember.report.ParoleBoardMemberSummaryReportService;
import omis.paroleboardmember.web.form.ParoleBoardMemberSearchForm;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller for displaying parole board members.
 *
 * @author Josh Divine
 * @author Sierra Rosales
 * @version 0.1.0 (Nov 9, 2017)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardMember")
public class ReportParoleBoardMemberController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardMember/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardMember/includes/paroleBoardMembersActionMenu";

	/* Model keys. */
	
	private static final String PAROLE_BOARD_MEMBER_SUMMARIES_MODEL_KEY = 
			"paroleBoardMemberSummaries";
	
	private static final String PAROLE_BOARD_MEMBER_MODEL_KEY = 
			"paroleBoardMember";
	
	private static final String PAROLE_BOARD_MEMBER_SEARCH_FORM_MODEL_KEY = 
			"paroleBoardMemberSearchForm";

	/* Report Names. */
	
	private static final String BOARD_MEMBER_DETAILS_REPORT_NAME
		= "/BOPP/Board_of_Pardons_and_Parole_Staff_Details";
	
	/* Report Parameters. */
	
	private static final String BOARD_MEMBER_DETAILS_ID_REPORT_PARAM_NAME
		= "BOARD_MEMBER_ID";		
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleBoardMemberPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardMemberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("paroleBoardMemberSummaryReportService")
	private ParoleBoardMemberSummaryReportService 
			paroleBoardMemberSummaryReportService;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;	
	
	/* Constructors. */
	
	/** Instantiates controller for parole board members. */
	public ReportParoleBoardMemberController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists parole board members
	 * 
	 * @return screen that lists parole board members
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_MEMBER_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "date", required = false)
				final Date date,
			@RequestParam(value = "startDate", required = false)
				final Date startDate,
			@RequestParam(value = "endDate", required = false)
				final Date endDate) {
		List<ParoleBoardMemberSummary> paroleBoardMembers;
		ParoleBoardMemberSearchForm form = new ParoleBoardMemberSearchForm();
		if (date != null) {
			form.setDate(date);
			form.setSingleDate(true);
			paroleBoardMembers = this.paroleBoardMemberSummaryReportService
					.findByDate(date);
		} else if (startDate != null || endDate != null) {
			form.setStartDate(startDate);
			form.setEndDate(endDate);
			form.setSingleDate(false);
			paroleBoardMembers = this.paroleBoardMemberSummaryReportService
					.findByDates(startDate, endDate);
		} else {
			form.setSingleDate(false);
			paroleBoardMembers = this.paroleBoardMemberSummaryReportService
					.findByDates(null, null);
		}
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_MEMBER_SEARCH_FORM_MODEL_KEY, form);
		mav.addObject(PAROLE_BOARD_MEMBER_SUMMARIES_MODEL_KEY, 
				paroleBoardMembers);
		return mav;
	}
	
	/**
	 * Updates the screen that lists parole board members.
	 * 
	 * @param form parole board member search form
	 * @param result binding result
	 * @return model and view for parole board members list screen
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PAROLE_BOARD_MEMBER_LIST') or hasRole('ADMIN')")
	public ModelAndView updateList(final ParoleBoardMemberSearchForm form,
				final BindingResult result) {
		List<ParoleBoardMemberSummary> paroleBoardMembers;
		if (form.getSingleDate()) {
			paroleBoardMembers = this.paroleBoardMemberSummaryReportService
					.findByDate(form.getDate());
			form.setStartDate(null);
			form.setEndDate(null);
		} else {
			paroleBoardMembers = this.paroleBoardMemberSummaryReportService
					.findByDates(form.getStartDate(), form.getEndDate());
			form.setDate(null);
		}
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_MEMBER_SEARCH_FORM_MODEL_KEY, form);
		mav.addObject(PAROLE_BOARD_MEMBER_SUMMARIES_MODEL_KEY, 
				paroleBoardMembers);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for parole board members.
	 * 
	 * @param paroleBoardMember parole board member
	 * @return action menu for parole board members
	 */
	@RequestMapping(value = "/paroleBoardMembersActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleBoardMember", required = false)
				final ParoleBoardMember paroleBoardMember) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_BOARD_MEMBER_MODEL_KEY, 
				paroleBoardMember);
		return mav;
	}
	
	/**
	 * Returns the report for the specified board member.
	 * 
	 * @param paroleBoardMember parole board member
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/boardMemberDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_MEMBER_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportBoardMemberDetails(@RequestParam(
			value = "paroleBoardMember", required = true)
			final ParoleBoardMember paroleBoardMember,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(BOARD_MEMBER_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(paroleBoardMember.getId()));
		byte[] doc = this.reportRunner.runReport(
				BOARD_MEMBER_DETAILS_REPORT_NAME,
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
		binder.registerCustomEditor(ParoleBoardMember.class, 
				this.paroleBoardMemberPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}
