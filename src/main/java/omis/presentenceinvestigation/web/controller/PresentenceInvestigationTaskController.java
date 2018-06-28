package omis.presentenceinvestigation.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociation;
import omis.presentenceinvestigation.service.PresentenceInvestigationTaskService;
import omis.presentenceinvestigation.web.controller.delegate.PresentenceInvestigationRequestSummaryModelDelegate;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationTaskForm;
import omis.presentenceinvestigation.web.validator.PresentenceInvestigationTaskFormValidator;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.web.form.TaskAssignmentItem;
import omis.task.web.form.TaskFields;
import omis.task.web.form.TaskItemOperation;
import omis.user.domain.UserAccount;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * PresentenceInvestigationTaskController.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Jun 12, 2017)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/presentenceInvestigation/task")
@PreAuthorize("hasRole('USER')")
public class PresentenceInvestigationTaskController {
	
	private static final String EDIT_VIEW_NAME =
			"/presentenceInvestigation/task/edit";
	
	private static final String
		PRESENTENCE_INVESTIGATION_TASK_ACTION_MENU_VIEW_NAME =
			"/presentenceInvestigation/task/includes/"
					+ "presentenceInvestigationTaskActionMenu";
	
	private static final String HOME_REDIRECT =
			"redirect:/presentenceInvestigation/"
				+ "home.html?presentenceInvestigationRequest=%d";
	
	/* Model Keys */
	
	private static final String FORM_MODEL_KEY =
			"presentenceInvestigationTaskForm";
	
	private static final String PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY =
			"presentenceInvestigationRequest";
	
	private static final String TASK_ASSIGNMENT_ITEM_INDEX_MODEL_KEY =
			"taskAssignmentItemIndex";
	
	private static final String TASK_FIELDS_PROPERTY_NAME_MODEL_KEY =
			"taskFieldsPropertyName";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"presentenceInvestigationTask.exists";

	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
					= "omis.presentenceinvestigation.msgs.form";
	
	/* Services. */
	
	@Autowired
	private PresentenceInvestigationTaskService
					presentenceInvestigationTaskService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("presentenceInvestigationRequestPropertyEditorFactory")
	private PropertyEditorFactory 
					presentenceInvestigationRequestPropertyEditorFactory;
	
	@Autowired
	@Qualifier("presentenceInvestigationTaskAssociationPropertyEditorFactory")
	private PropertyEditorFactory
				presentenceInvestigationTaskAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("taskPropertyEditorFactory")
	private PropertyEditorFactory taskPropertyEditorFactory;
	
	@Autowired
	@Qualifier("taskAssignmentPropertyEditorFactory")
	private PropertyEditorFactory taskAssignmentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("presentenceInvestigationRequestSummaryModelDelegate")
	private PresentenceInvestigationRequestSummaryModelDelegate
			presentenceInvestigationRequestSummaryModelDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Validators. */
	
	@Autowired
	private PresentenceInvestigationTaskFormValidator
					presentenceInvestigationTaskFormValidator;
	
	/**
	 * Default Constructor for PresentenceInvestigationTaskController
	 */
	public PresentenceInvestigationTaskController() {
	}
	
	/**
	 * Displays the ModelAndView for editting a Presentence Investigation Task
	 * @param taskAssociation - PresentenceInvestigationTaskAssociation
	 * @return ModelAndView for editting a Presentence Investigation Task
	 */
	@RequestMapping(value="/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_TASK_VIEW') or "
			+ "hasRole('PRESENTENCE_INVESTIGATION_TASK_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value="presentenceInvestigationTaskAssociation",
			required=true)
			final PresentenceInvestigationTaskAssociation
				taskAssociation){
		return this.prepareEditMav(taskAssociation
				.getPresentenceInvestigationRequest(),
				this.prepareForm(taskAssociation));
	}
	
	/**
	 * Updates a PresentenceInvestigationTaskAssociation and returns to the
	 * presentenceInvestigationRequest home screen
	 * @param taskAssociation - PresentenceInvestigationTaskAssociation
	 * @param form - PresentenceInvestigationTaskForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView for for presentenceInvestigationRequest home screen,
	 * or back to PresentenceInvestigation task edit on form error
	 * @throws DuplicateEntityFoundException - When a Task already exists with
	 * given ControllerName, MethodName, SourceAccount, Description, and
	 * OriginationDate
	 */
	@RequestMapping(value="/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_TASK_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value="presentenceInvestigationTaskAssociation",
			required=true)
			final PresentenceInvestigationTaskAssociation
				taskAssociation,
			final PresentenceInvestigationTaskForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException{
		
		this.presentenceInvestigationTaskFormValidator
			.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return this.prepareEditMav(taskAssociation
					.getPresentenceInvestigationRequest(), form);
		}
		else {
			this.presentenceInvestigationTaskService.updateTask(
					taskAssociation.getTask(),
					taskAssociation.getTask().getControllerName(),
					taskAssociation.getTask().getMethodName(),
					form.getTaskFields().getDescription(),
					taskAssociation.getTask().getSourceAccount(),
					DateManipulator.getDateAtTimeOfDay(
							form.getTaskFields().getOriginationDate(),
							form.getTaskFields().getOriginationTime()),
					form.getTaskFields().getCompletionDate());
			
			this.processItems(form.getTaskFields().getTaskAssignmentItems(),
					taskAssociation.getTask());
			
			return new ModelAndView(String.format(
					HOME_REDIRECT, taskAssociation
					.getPresentenceInvestigationRequest().getId()));
		}
	}
	
	/**
	 * Displays the ModelAndView for the PresentenceInvestigationTask action menu
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @return ModelAndView for the PresentenceInvestigationTask action menu
	 */
	@RequestMapping(value="/presentenceInvestigationTaskActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayPresentenceInvestigationTaskActionMenu(
			@RequestParam(value="presentenceInvestigationRequest",
			required = true)
			final PresentenceInvestigationRequest
					presentenceInvestigationRequest){
		ModelMap map = new ModelMap();
		map.addAttribute(PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY,
				presentenceInvestigationRequest);
		return new ModelAndView(
				PRESENTENCE_INVESTIGATION_TASK_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	/**
	 * Processes TaskAssignmentItems for creation, updating, and removal.
	 * @param taskAssignmentItems - list of TaskAssignmentItems
	 * @param task - Task
	 * @throws DuplicateEntityFoundException - When a TaskAssignment already exists
	 * with given TaskAsignee and Date for the specified Task
	 */
	private void processItems(final List<TaskAssignmentItem> taskAssignmentItems,
			final Task task) throws DuplicateEntityFoundException {
		for(TaskAssignmentItem item : taskAssignmentItems) {
			if(TaskItemOperation.CREATE.equals(item.getTaskItemOperation())) {
				this.presentenceInvestigationTaskService.createTaskAssignment(
						task, item.getAssignedDate(), item.getAssigneeAccount());
			}
			else if(TaskItemOperation.UPDATE.equals(item.getTaskItemOperation())) {
				this.presentenceInvestigationTaskService.updateTaskAssignment(
						item.getTaskAssignment(), item.getAssignedDate(),
						item.getAssigneeAccount(), new Date());
			}
			else if(TaskItemOperation.REMOVE.equals(item.getTaskItemOperation())) {
				this.presentenceInvestigationTaskService.removeTaskAssignment(
						item.getTaskAssignment());
			}
		}
	}
	
	/**
	 * Returns a prepared ModelAndView for Presentence Investigation Task editing
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @param form - PresentenceInvestigationTaskForm
	 * @return ModelAndView for Presentence Investigation Task editing
	 */
	private ModelAndView prepareEditMav(
			final PresentenceInvestigationRequest presentenceInvestigationRequest,
			final PresentenceInvestigationTaskForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY,
				presentenceInvestigationRequest);
		map.addAttribute(TASK_ASSIGNMENT_ITEM_INDEX_MODEL_KEY,
				form.getTaskFields().getTaskAssignmentItems().size());
		map.addAttribute(TASK_FIELDS_PROPERTY_NAME_MODEL_KEY, "taskFields");
		this.offenderSummaryModelDelegate.add(map,
				(Offender) presentenceInvestigationRequest.getDocket()
				.getPerson());
		this.presentenceInvestigationRequestSummaryModelDelegate.add(map,
				presentenceInvestigationRequest);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared PresentenceInvestigationTaskForm
	 * @param taskAssociation - PresentenceInvestigationTaskAssociation
	 * @return Prepared PresentenceInvestigationTaskForm
	 */
	private PresentenceInvestigationTaskForm prepareForm(
			final PresentenceInvestigationTaskAssociation taskAssociation) {
		PresentenceInvestigationTaskForm form =
				new PresentenceInvestigationTaskForm();
		TaskFields taskFields = new TaskFields();
		List<TaskAssignmentItem> taskAssignmentItems =
				new ArrayList<TaskAssignmentItem>();
		for(TaskAssignment taskAssignment :
				this.presentenceInvestigationTaskService
				.findTaskAssignmentsByTask(taskAssociation.getTask())) {
			TaskAssignmentItem item = new TaskAssignmentItem();
			item.setTaskAssignment(taskAssignment);
			item.setAssignedDate(taskAssignment.getAssignedDate());
			item.setAssigneeAccount(taskAssignment.getAssigneeAccount());
			item.setTaskItemOperation(TaskItemOperation.UPDATE);
			taskAssignmentItems.add(item);
		}
		taskFields.setTaskAssignmentItems(taskAssignmentItems);
		taskFields.setAllowGroup(false);
		taskFields.setAllowTask(false);
		taskFields.setCompletionDate(taskAssociation.getTask()
				.getCompletionDate());
		taskFields.setOriginationDate(taskAssociation.getTask(
				).getOriginationDate());
		taskFields.setOriginationTime(taskAssociation.getTask(
				).getOriginationDate());
		taskFields.setDescription(taskAssociation.getTask().getDescription());
		form.setTaskFields(taskFields);
		return form;
	}
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView
		(ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder){
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(PresentenceInvestigationRequest.class, 
				this.presentenceInvestigationRequestPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(PresentenceInvestigationTaskAssociation.class, 
				this.presentenceInvestigationTaskAssociationPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class, 
				this.userAccountPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(TaskAssignment.class, 
				this.taskAssignmentPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(
				Date.class, "taskFields.originationTime",
				this.customDateEditorFactory
				.createCustomTimeOnlyEditor(true));
	}
}
