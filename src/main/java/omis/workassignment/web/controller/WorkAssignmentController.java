package omis.workassignment.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.workassignment.domain.FenceRestriction;
import omis.workassignment.domain.WorkAssignment;
import omis.workassignment.domain.WorkAssignmentCategory;
import omis.workassignment.domain.WorkAssignmentChangeReason;
import omis.workassignment.domain.WorkAssignmentGroup;
import omis.workassignment.domain.WorkAssignmentNote;
import omis.workassignment.report.WorkAssignmentReportService;
import omis.workassignment.report.WorkAssignmentSummary;
import omis.workassignment.service.WorkAssignmentService;
import omis.workassignment.web.form.WorkAssignmentForm;
import omis.workassignment.web.form.WorkAssignmentNoteItem;
import omis.workassignment.web.form.WorkAssignmentNoteItemOperation;
import omis.workassignment.web.validator.WorkAssignmentFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/** Controller for work assignment.
 * @author Yidong Li
 * @version 0.1.1 (Aug 23, 2016)
 * @since OMIS 3.0 */
@Controller
@RequestMapping("/workAssignment")
@PreAuthorize("hasRole('USER')")
public class WorkAssignmentController {
	/* views */
	private static final String EDIT_VIEW_NAME = "workAssignment/edit";
	private static final String LIST_VIEW_NAME = "workAssignment/list";
	private static final String WORK_ASSIGNMENT_LIST_ACTION_MENU_VIEW_NAME
		= "workAssignment/includes/workAssignmentListActionMenu";
	private static final String WORK_ASSIGNMENT_EDIT_ACTION_MENU_VIEW_NAME
		= "workAssignment/includes/workAssignmentEditActionMenu";
	private static final String WORK_ASSIGNMENT_NOTE_ACTION_MENU_VIEW_NAME
		= "workAssignment/includes/workAssignmentNoteActionMenu";
	private static final String WORK_ASSIGNMENT_NOTE_ITEM_VIEW_NAME 
		= "workAssignment/includes/workAssignmentNoteItem";
	private static final String WORK_ASSIGNMENT_ROW_ACTION_MENU_VIEW_NAME
	= "workAssignment/includes/workAssignmentRowActionMenu";
	
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/workAssignment/list.html?offender=%d";
	
	/* Property editor. */
	@Autowired
	@Qualifier("workAssignmentPropertyEditorFactory")
	private PropertyEditorFactory workAssignmentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("fenceRestrictionPropertyEditorFactory")
	private PropertyEditorFactory fenceRestrictionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("workAssignmentGroupPropertyEditorFactory")
	private PropertyEditorFactory workAssignmentGroupPropertyEditorFactory;
	
	@Autowired
	@Qualifier("workAssignmentCategoryPropertyEditorFactory")
	private PropertyEditorFactory workAssignmentCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("workAssignmentChangeReasonPropertyEditorFactory")
	private PropertyEditorFactory workAssignmentChangeReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired 
	private CustomDateEditorFactory customDateEditorFactory;
		
	/* model keys */
	private static final String WORK_ASSIGNMENT_FORM_MODEL_KEY 
		= "workAssignmentForm";
	private static final String WORK_ASSIGNMENT_CATEGORIES_MODEL_KEY 
		= "workAssignmentCategories";
	private static final String WORK_ASSIGNMENT_CHANGE_REASONS_MODEL_KEY 
		= "workAssignmentChangeReasons";
	private static final String FENCE_RESTRICTIONS_MODEL_KEY 
	= "fenceRestrictions";
	private static final String WORK_ASSIGNMENT_SUMMARIES_MODEL_KEY 
		= "workAssignmentSummaries";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String OFFENDER_SUMMARY_MODEL_KEY = "offenderSummary";
	private static final String WORK_ASSIGNMENT_MODEL_KEY 
		= "workAssignment";
	private static final String CREATE_WORK_ASSIGNMENT_MODEL_KEY 
		= "createWorkAssignment";
	private static final String WORK_ASSIGNMENT_NOTE_ITEM_MODEL_KEY
		="workAssignmentNoteItem";
	private static final String WORK_ASSIGNMENT_NOTE_ITEMS_MODEL_KEY
		="workAssignmentNoteItems";
	private static final String WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY
		= "workAssignmentNoteIndex";
	private static final String ORIGINAL_WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY
		="originalWorkAssignmentNoteIndex";
	
	/* Services. */
	@Autowired
	@Qualifier("workAssignmentService")
	private WorkAssignmentService workAssignmentService; 
	
	@Autowired
	@Qualifier("workAssignmentReportService")
	private WorkAssignmentReportService workAssignmentReportService; 
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validator */
	@Autowired
	@Qualifier("workAssignmentFormValidator")
	private WorkAssignmentFormValidator workAssignmentFormValidator;
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	
	/* Report names. */
	
	private static final String OFFENDER_WORK_ASSIGNMENT_HISTORY_REPORT_NAME 
		= "/Placement/WorkAssignments/Offender_Work_Assignment_History";
	
	private static final String WORK_ASSIGNMENTS_LISTING_REPORT_NAME 
		= "/Placement/WorkAssignments/Work_Assignments_Listing";	

	private static final String WORK_ASSIGNMENTS_DETAILS_REPORT_NAME 
		= "/Placement/WorkAssignments/Work_Assignments_Details";

	/* Report parameter names. */
	
	private static final String WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String WORK_ASSIGNMENTS_DETAILS_ID_REPORT_PARAM_NAME 
		= "WORK_ASSIGNMENT_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructor. */
	
	/** Instantiates a default work assignment controller. */
	public WorkAssignmentController() {
		// Default instantiation
	}
	
	/**
	 * Displays a list of work assignment.
	 * 
	 * @param offender offender
	 * @return view to display the list of work assignment
	 */
	@RequestMapping(value="/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", required = true) 
		final Offender offender) {
		List<WorkAssignmentSummary> workAssignmentSummaries 
			= new ArrayList<WorkAssignmentSummary>();
		workAssignmentSummaries = this.workAssignmentReportService.findByOffender(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(WORK_ASSIGNMENT_SUMMARIES_MODEL_KEY, workAssignmentSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender(offender);
		mav.addObject(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}

	/**
	 * Displays screen to create new a work assignment.
	 * @param offender offender for whom to create vehicle association
	 * @return model and view to create a new work assignment 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
		@RequestParam(value = "offender", required = true)
			final Offender offender) {
				WorkAssignmentForm workAssignmentForm 
					= new WorkAssignmentForm();
				boolean createWorkAssignment = true; 
				List<WorkAssignmentNoteItem> workAssignmentNoteItems 
					= new ArrayList<WorkAssignmentNoteItem>();
				int workAssignmentNoteIndex = workAssignmentNoteItems.size();
				return this.prepareEditMav(workAssignmentForm,offender, 
					createWorkAssignment, workAssignmentNoteItems, 
					workAssignmentNoteIndex, 0);
	}
	
	/**
	 * Saves a new created work assignment.
	 * 
	 * @param offender offender
	 * @param workAssignmentForm work assignment form
	 * @param result binding result
	 * @return redirect to list vehicle association by offender
	 * @throws DuplicateEntityFoundException 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true)
			final Offender offender,
			final WorkAssignmentForm workAssignmentForm,
			final BindingResult result) throws DuplicateEntityFoundException {
			this.workAssignmentFormValidator.validate(workAssignmentForm, result);
			if (result.hasErrors()) {
				return this.prepareRedisplayEditMav(
					offender, workAssignmentForm, result, true, 
					workAssignmentForm.getWorkAssignmentNoteItems(),0);
			} 
			
			WorkAssignment workAssignment = this.workAssignmentService.create(
				offender, workAssignmentForm.getFenceRestriction(), 
				workAssignmentForm.getWorkAssignmentCategory(), 
				workAssignmentForm.getWorkAssignmentChangeReason(), 
				workAssignmentForm.getAssignmentDate(), 
				workAssignmentForm.getTerminationDate(), 
				workAssignmentForm.getComments(),
				workAssignmentForm.getEndExistingWorkAssignment());
			
			for (WorkAssignmentNoteItem noteItem : 
				workAssignmentForm.getWorkAssignmentNoteItems()){
				if(noteItem.getOperation().equals(
					WorkAssignmentNoteItemOperation.CREATE)){
					this.workAssignmentService.addNote(workAssignment, 
						noteItem.getDate(), noteItem.getNote());
				}
			}
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}	
	
	/** Edit work assignment. 
	 * @param vehicleAssociation vehicle association.
	 * @return edited vehicle association view. */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
		@RequestParam(value = "workAssignment", required = true)
		final WorkAssignment workAssignment) {
			WorkAssignmentForm workAssignmentForm 
				= new WorkAssignmentForm();
			workAssignmentForm.setAssignmentDate(
				workAssignment.getAssignedDate());
			workAssignmentForm.setFenceRestriction(
				workAssignment.getFenceRestriction());
			workAssignmentForm.setComments(workAssignment.getComments());
			workAssignmentForm.setTerminationDate(
				workAssignment.getTerminationDate());
			workAssignmentForm.setWorkAssignmentCategory(
				workAssignment.getCategory());
			workAssignmentForm.setWorkAssignmentChangeReason(
				workAssignment.getChangeReason());
			List<WorkAssignmentNote> workAssignmentNotes 
				= this.workAssignmentService.findNotes(workAssignment);
			List<WorkAssignmentNoteItem> workAssignmentNoteItems 
			= new ArrayList<WorkAssignmentNoteItem>();
			for(WorkAssignmentNote note : workAssignmentNotes)
			{
				WorkAssignmentNoteItem item = new WorkAssignmentNoteItem();
				item.setDate(note.getDate());
				item.setNote(note.getValue());
				item.setOperation(WorkAssignmentNoteItemOperation.UPDATE);
				workAssignmentNoteItems.add(item);
			}
			workAssignmentForm.setWorkAssignmentNoteItems(workAssignmentNoteItems);
			int workAssignmentNoteIndex = workAssignmentNoteItems.size();
			int originalWorkAssignmentNoteIndex = this.workAssignmentService
					.findNotes(workAssignment).size();
					
			ModelAndView mav = prepareEditMav(workAssignmentForm, 
				workAssignment.getOffender(), false, workAssignmentNoteItems,
				workAssignmentNoteIndex, originalWorkAssignmentNoteIndex); 
			mav.addObject(WORK_ASSIGNMENT_MODEL_KEY, workAssignment);
			return mav; 
	}
	
	/**
	 * Updates an existing work assignment.
	 * 
	 * @param workAssignment work assignment
	 * @param workAssignmentForm work assignment form
	 * @param result binding result
	 * @return redirect to list vehicle association
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "workAssignment", required = true)
			final WorkAssignment workAssignment,
			final WorkAssignmentForm workAssignmentForm,
			final BindingResult result) throws DuplicateEntityFoundException {	
		this.workAssignmentFormValidator.validate(
			workAssignmentForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayEditMav(
				workAssignment.getOffender(), workAssignmentForm, result, false,
				workAssignmentForm.getWorkAssignmentNoteItems(),
				this.workAssignmentService.findNotes(workAssignment).size());
				return mav;
		}

		this.workAssignmentService.update(workAssignment, 
			workAssignmentForm.getFenceRestriction(), 
			workAssignmentForm.getWorkAssignmentCategory(), 
			workAssignmentForm.getWorkAssignmentChangeReason(), 
			workAssignmentForm.getAssignmentDate(), 
			workAssignmentForm.getTerminationDate(), 
			workAssignmentForm.getComments(),
			workAssignmentForm.getEndExistingWorkAssignment());
		
		/* Update/add work assignment notes */
		List<WorkAssignmentNote> originalWorkAssignmentNotes 
			= this.workAssignmentService.findNotes(workAssignment);
		int originalWorkAssignmentNoteIndex 
			= originalWorkAssignmentNotes.size();	
		List<WorkAssignmentNoteItem> workAssignmentNoteItems 
			= workAssignmentForm.getWorkAssignmentNoteItems();
		int newWorkAssignmentNoteIndex = workAssignmentNoteItems.size()-1;
		
		for (int index = 0; index < originalWorkAssignmentNoteIndex; index++) {
			if(WorkAssignmentNoteItemOperation.UPDATE.equals(
					workAssignmentNoteItems.get(index).getOperation())){
					this.workAssignmentService.updateNote(
						originalWorkAssignmentNotes.get(index), 
						workAssignmentNoteItems.get(index).getDate(), 
						workAssignmentNoteItems.get(index).getNote());
			}
			if(WorkAssignmentNoteItemOperation.REMOVE.equals(
				workAssignmentNoteItems.get(index).getOperation())){
				this.workAssignmentService.removeNote(
					originalWorkAssignmentNotes.get(index));
			}
		}
		
		for (int index = originalWorkAssignmentNoteIndex; 
			index <= newWorkAssignmentNoteIndex; index++) {
			if(WorkAssignmentNoteItemOperation.CREATE.equals(
				workAssignmentNoteItems.get(index).getOperation())){
				this.workAssignmentService.addNote(workAssignment, 
				workAssignmentNoteItems.get(index).getDate(), 
				workAssignmentNoteItems.get(index).getNote());
			}
		}
		
		return new ModelAndView(String.format(LIST_REDIRECT,
			workAssignment.getOffender().getId()));
	}
	
	/**
	 * Removes an existing work assignment.
	 * 
	 * @param workAssignment work assignment to remove
	 * @return redirect to list religious preferences
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "workAssignment", required = true)
			final WorkAssignment workAssignment) {
		List<WorkAssignmentNote> workAssignmentNotes 
			= this.workAssignmentService.findNotes(workAssignment);
		Offender offender = workAssignment.getOffender();
		for(WorkAssignmentNote workAssignmentNote : workAssignmentNotes){
			this.workAssignmentService.removeNote(workAssignmentNote);
		}
		this.workAssignmentService.remove(workAssignment);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Returns a view for an action menu on list screen
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	@RequestMapping(value = "/workAssignmentListActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentListActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_LIST_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for an action menu on edit/create screen
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	@RequestMapping(value = "/workAssignmentEditActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentEditActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_EDIT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for an action menu
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	@RequestMapping(value = "/workAssignmentNoteActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentNoteActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_NOTE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Adds a work assignment note
	 * 
	 * @param noteItemIndex work assignment note index
	 * @return model and view for a new work assignment note
	 */
	@RequestMapping(value = "/addWorkAssignmentNoteItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addFamilyAssociationNoteItem(@RequestParam(
			value = "noteItemIndex", required = true)
			final int noteItemIndex) {
		ModelMap map = new ModelMap();
		WorkAssignmentNoteItem workAssignmentNoteItem 
			= new WorkAssignmentNoteItem();
		workAssignmentNoteItem.setOperation(
			WorkAssignmentNoteItemOperation.CREATE); 
		map.addAttribute(WORK_ASSIGNMENT_NOTE_ITEM_MODEL_KEY, 
			workAssignmentNoteItem);
		map.addAttribute(WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, noteItemIndex);
		return new ModelAndView(WORK_ASSIGNMENT_NOTE_ITEM_VIEW_NAME, map);
	}
	
	private ModelAndView prepareEditMav(
		final WorkAssignmentForm workAssignmentForm, 
		final Offender offender, final Boolean createWorkAssignment,
		final List<WorkAssignmentNoteItem> workAssignmentNoteItems,
		final int workAssignmentNoteIndex,
		final int originalWorkAssignmentNoteIndex) {
			ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
			mav.addObject(WORK_ASSIGNMENT_FORM_MODEL_KEY, 
				workAssignmentForm);
			List<WorkAssignmentCategory> workAssignmentCategories 
				= this.workAssignmentService.findCategories();
			mav.addObject(WORK_ASSIGNMENT_CATEGORIES_MODEL_KEY, 
				workAssignmentCategories);
			List<WorkAssignmentChangeReason> workAssignmentChangeReasons 
				= this.workAssignmentService.findChangeReasons();
			mav.addObject(WORK_ASSIGNMENT_CHANGE_REASONS_MODEL_KEY, 
				workAssignmentChangeReasons);
			List<FenceRestriction> fenceRestrictions 
				= this.workAssignmentService.findFenceRestrictions();
			mav.addObject(FENCE_RESTRICTIONS_MODEL_KEY, 
				fenceRestrictions);
			mav.addObject(CREATE_WORK_ASSIGNMENT_MODEL_KEY, createWorkAssignment);
			mav.addObject(WORK_ASSIGNMENT_NOTE_ITEMS_MODEL_KEY, 
				workAssignmentNoteItems);
			mav.addObject(WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, workAssignmentNoteIndex); 
			mav.addObject(ORIGINAL_WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, 
				originalWorkAssignmentNoteIndex); 
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			return mav;
	}
	
	// Prepares redisplay edit/create screen
	private ModelAndView prepareRedisplayEditMav(
		final Offender offender,
		final WorkAssignmentForm workAssignmentForm,
		final BindingResult result,
		final boolean createNew,
		final List<WorkAssignmentNoteItem> workAssignmentNoteItems,
		final int originalWorkAssignmentNoteItems) {
		int workAssignmentNoteIndex = workAssignmentNoteItems.size();
		ModelAndView mav = this.prepareEditMav(workAssignmentForm, offender,
			createNew, workAssignmentNoteItems, workAssignmentNoteIndex,
			originalWorkAssignmentNoteItems);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ WORK_ASSIGNMENT_MODEL_KEY, result);
		return mav;
	}	
	
	/**
	 * Returns a view for work assignment action menu pertaining
	 * 
	 * @param offender offender
	 * @return view for employment action menu
	 */
	@RequestMapping(value = "/workAssignmentRowActionMenu.html",method =RequestMethod.GET)
	public ModelAndView employmentActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender,
		@RequestParam(value = "workAssignment",
		required = true) final WorkAssignment workAssignment) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(WORK_ASSIGNMENT_MODEL_KEY, workAssignment);
		return new ModelAndView(WORK_ASSIGNMENT_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	

	/* Reports. */

	/**
	 * Returns the report for the specified offenders work assignments for offender distribution.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/offenderWorkAssignmentHistoryReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportOffenderWorkAssignmentsHistory(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				OFFENDER_WORK_ASSIGNMENT_HISTORY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders work assignments.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/workAssignmentsListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWorkAssignmentsListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				WORK_ASSIGNMENTS_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified work assignment.
	 * 
	 * @param workAssignment work assignment
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/workAssignmentsDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWorkAssignmentsDetails(@RequestParam(
			value = "workAssignment", required = true)
			final WorkAssignment workAssignment,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(workAssignment.getId()));
		byte[] doc = this.reportRunner.runReport(
				WORK_ASSIGNMENTS_DETAILS_REPORT_NAME,
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
		binder.registerCustomEditor(FenceRestriction.class,
			this.fenceRestrictionPropertyEditorFactory.createPropertyEditor());
 		binder.registerCustomEditor(WorkAssignmentCategory.class,
			this.workAssignmentCategoryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(WorkAssignmentChangeReason.class,
			this.workAssignmentChangeReasonPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(WorkAssignment.class,
			this.workAssignmentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(WorkAssignmentGroup.class,
			this.workAssignmentGroupPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class, 
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}
