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
package omis.hearinganalysis.web.controller;

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
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisTaskAssociation;
import omis.hearinganalysis.report.HearingAnalysisSummaryService;
import omis.hearinganalysis.service.HearingAnalysisTaskService;
import omis.hearinganalysis.web.form.HearingAnalysisTaskForm;
import omis.hearinganalysis.web.validator.HearingAnalysisTaskFormValidator;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.web.form.TaskAssignmentItem;
import omis.task.web.form.TaskFields;
import omis.task.web.form.TaskItemOperation;
import omis.user.domain.UserAccount;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * HearingAnalysisTaskController.java
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 17, 2018)
 * @since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/hearingAnalysis/task")
@PreAuthorize("hasRole('USER')")
public class HearingAnalysisTaskController {
	
	private static final String EDIT_VIEW_NAME = "/hearingAnalysis/task/edit";
	
	private static final String HEARING_ANALYSIS_TASK_ACTION_MENU_VIEW_NAME =
			"/hearingAnalysis/task/includes/hearingAnalysisTaskActionMenu";
	
	private static final String HOME_REDIRECT =
			"redirect:/hearingAnalysis/home.html?hearingAnalysis=%d";
	
	/* Model Keys */
	
	private static final String FORM_MODEL_KEY = "hearingAnalysisTaskForm";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";
	
	private static final String TASK_ASSIGNMENT_ITEM_INDEX_MODEL_KEY =
			"taskAssignmentItemIndex";
	
	private static final String TASK_FIELDS_PROPERTY_NAME_MODEL_KEY =
			"taskFieldsPropertyName";
	
	private static final String HEARING_ANALYSIS_SUMMARY_MODEL_KEY = 
			"hearingAnalysisSummary";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"hearingAnalysisTask.exists";

	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.hearinganalysis.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("hearingAnalysisTaskService")
	private HearingAnalysisTaskService hearingAnalysisTaskService;
	
	@Autowired
	@Qualifier("hearingAnalysisSummaryService")
	private HearingAnalysisSummaryService hearingAnalysisSummaryService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisTaskAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
			hearingAnalysisTaskAssociationPropertyEditorFactory;
	
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
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Validators. */
	
	@Autowired
	private HearingAnalysisTaskFormValidator hearingAnalysisTaskFormValidator;
	
	/**
	 * Default Constructor for HearingAnalysisTaskController
	 */
	public HearingAnalysisTaskController() {
	}
	
	/**
	 * @param taskAssociation
	 * @return
	 */
	@RequestMapping(value="/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_ANALYSIS_TASK_VIEW') or "
			+ "hasRole('HEARING_ANALYSIS_TASK_EDIT') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value="hearingAnalysisTaskAssociation", required=true)
			final HearingAnalysisTaskAssociation taskAssociation){
		return this.prepareEditMav(taskAssociation.getHearingAnalysis(),
				this.prepareForm(taskAssociation));
	}
	
	/**
	 * @param taskAssociation
	 * @param form
	 * @param bindingResult
	 * @return
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value="/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('HEARING_ANALYSIS_TASK_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value="hearingAnalysisTaskAssociation", required=true)
			final HearingAnalysisTaskAssociation taskAssociation,
			final HearingAnalysisTaskForm form,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException{
		this.hearingAnalysisTaskFormValidator.validate(form, bindingResult);
		if(bindingResult.hasErrors()) {
			return this.prepareEditMav(taskAssociation.getHearingAnalysis(), 
					form);
		}
		this.hearingAnalysisTaskService.updateTask(
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
		
		return new ModelAndView(String.format(HOME_REDIRECT, 
				taskAssociation.getHearingAnalysis().getId()));
	}
	
	/**
	 * @param hearingAnalysis
	 * @return
	 */
	@RequestMapping(value="/hearingAnalysisTaskActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayHearingAnalysisTaskActionMenu(
			@RequestParam(value="hearingAnalysis", required = true)
			final HearingAnalysis hearingAnalysis){
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		return new ModelAndView(HEARING_ANALYSIS_TASK_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/* Helper Methods */
	
	// Process items.
	private void processItems(final List<TaskAssignmentItem> taskAssignmentItems,
			final Task task) throws DuplicateEntityFoundException {
		for(TaskAssignmentItem item : taskAssignmentItems) {
			if(TaskItemOperation.CREATE.equals(item.getTaskItemOperation())) {
				this.hearingAnalysisTaskService.createTaskAssignment(task, 
						item.getAssigneeAccount(), item.getAssignedDate());
			}
			else if(TaskItemOperation.UPDATE.equals(
					item.getTaskItemOperation())) {
				this.hearingAnalysisTaskService.updateTaskAssignment(
						item.getTaskAssignment(), item.getAssigneeAccount(),
						item.getAssignedDate(), new Date());
			}
			else if(TaskItemOperation.REMOVE.equals(
					item.getTaskItemOperation())) {
				this.hearingAnalysisTaskService.removeTaskAssignment(
						item.getTaskAssignment());
			}
		}
	}
	
	// Prepare edit model and view.
	private ModelAndView prepareEditMav(
			final HearingAnalysis hearingAnalysis,
			final HearingAnalysisTaskForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		map.addAttribute(TASK_ASSIGNMENT_ITEM_INDEX_MODEL_KEY,
				form.getTaskFields().getTaskAssignmentItems().size());
		map.addAttribute(TASK_FIELDS_PROPERTY_NAME_MODEL_KEY, "taskFields");
		this.offenderSummaryModelDelegate.add(map, 
				hearingAnalysis.getEligibility().getOffender());
		map.addAttribute(HEARING_ANALYSIS_SUMMARY_MODEL_KEY, 
				this.hearingAnalysisSummaryService.summarize(hearingAnalysis));
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared HearingAnalysisTaskForm
	 * @param taskAssociation - HearingAnalysisTaskAssociation
	 * @return Prepared HearingAnalysisTaskForm
	 */
	private HearingAnalysisTaskForm prepareForm(
			final HearingAnalysisTaskAssociation taskAssociation) {
		HearingAnalysisTaskForm form =
				new HearingAnalysisTaskForm();
		TaskFields taskFields = new TaskFields();
		List<TaskAssignmentItem> taskAssignmentItems =
				new ArrayList<TaskAssignmentItem>();
		for(TaskAssignment taskAssignment :
				this.hearingAnalysisTaskService
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
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
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
		binder.registerCustomEditor(HearingAnalysis.class, 
				this.hearingAnalysisPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysisTaskAssociation.class, 
				this.hearingAnalysisTaskAssociationPropertyEditorFactory.
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
