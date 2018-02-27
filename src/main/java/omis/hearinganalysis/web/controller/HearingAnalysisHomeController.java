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

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.hearinganalysis.domain.HearingAnalysisTaskAssociation;
import omis.hearinganalysis.domain.ParoleHearingAnalysisTaskSource;
import omis.hearinganalysis.domain.ParoleHearingTaskGroup;
import omis.hearinganalysis.report.HearingAnalysisSummaryService;
import omis.hearinganalysis.service.HearingAnalysisTaskService;
import omis.hearinganalysis.web.form.HearingAnalysisHomeForm;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItem;
import omis.hearinganalysis.web.form.HearingAnalysisNoteItemOperation;
import omis.hearinganalysis.web.form.HearingAnalysisTaskItem;
import omis.hearinganalysis.web.form.HearingAnalysisTaskItemOperation;
import omis.hearinganalysis.web.validator.HearingAnalysisHomeFormValidator;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.person.domain.Person;
import omis.task.domain.Task;
import omis.task.domain.TaskParameterValue;
import omis.task.domain.TaskTemplateParameterValue;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for hearing analysis home.
 *
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/hearingAnalysis")
public class HearingAnalysisHomeController {

	/* View names. */
	
	private static final String HOME_VIEW_NAME = "/hearingAnalysis/home/home";
	
	/* Action menus. */
	
	private static final String HOME_ACTION_MENU_VIEW_NAME = 
			"/hearingAnalysis/home/includes/homeActionMenu";
	
	/* Redirects. */
	
	private static final String HOME_REDIRECT =
			"redirect:/hearingAnalysis/home.html?hearingAnalysis=%d";
	
	/* Model keys. */
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";

	private static final String HEARING_ANALYSIS_SUMMARY_MODEL_KEY = 
			"hearingAnalysisSummary";
	
	private static final String FORM_MODEL_KEY = "hearingAnalysisHomeForm";

	private static final String USER_ACCOUNT_MODEL_KEY =
			"AuditComponentRetrieverSpringMvcImpl#auditUserAccount";
	
	private static final String HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY = 
			"hearingAnalysisNoteIndex";
	
	/* Message keys. */
	
	private static final String HEARING_ANALYSIS_TASK_EXISTS_MESSAGE_KEY = 
			"hearingAnalysisTask.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.hearinganalysis.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("hearingAnalysisSummaryService")
	private HearingAnalysisSummaryService hearingAnalysisSummaryService;
	
	@Autowired
	@Qualifier("hearingAnalysisTaskService")
	private HearingAnalysisTaskService hearingAnalysisTaskService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisTaskAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
			hearingAnalysisTaskAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisNotePropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisNotePropertyEditorFactory;

	/* Validators. */

	@Autowired
	@Qualifier("hearingAnalysisHomeFormValidator")
	private HearingAnalysisHomeFormValidator hearingAnalysisHomeFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for hearing analysis home. */
	public HearingAnalysisHomeController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows hearing analysis home screen.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return hearing analysis home screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@RequestMapping(value = "/home.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('HEARING_ANALYSIS_TASK_VIEW') or hasRole('ADMIN')")
	public ModelAndView home(
			@RequestParam(value = "hearingAnalysis", required = true) 
			final HearingAnalysis hearingAnalysis) 
					throws DuplicateEntityFoundException {
		checkTasks(hearingAnalysis);
		List<HearingAnalysisTaskItem> analysisTaskItems = new ArrayList<>();
		List<HearingAnalysisTaskItem> planningTaskItems = new ArrayList<>();
		for (HearingAnalysisTaskAssociation taskAssociation : 
			this.hearingAnalysisTaskService
			.findHearingAnalysisTasksByHearingAnalysis(hearingAnalysis)) {
			HearingAnalysisTaskItem item = new HearingAnalysisTaskItem();
			item.setTaskAssociation(taskAssociation);
			if(taskAssociation.getTask().getCompletionDate() != null) {
				item.setItemOperation(
						HearingAnalysisTaskItemOperation.COMPLETE);
			}
			else {
				item.setItemOperation(
						HearingAnalysisTaskItemOperation.INCOMPLETE);
			}
			switch (taskAssociation.getTaskSource().getCategory()) {
				case ANALYSIS:
					analysisTaskItems.add(item);
					break;
				case PLANNING:
					planningTaskItems.add(item);
					break;
				default:
					throw new UnsupportedOperationException(
							"Parole hearing task category not supported.");
			}
		}
		HearingAnalysisHomeForm form = new HearingAnalysisHomeForm();
		form.setAnalysisTaskItems(analysisTaskItems);
		form.setPlanningTaskItems(planningTaskItems);
		List<HearingAnalysisNote> notes = this.hearingAnalysisTaskService
				.findHearingAnalysisNotesByHearingAnalysis(hearingAnalysis);
		List<HearingAnalysisNoteItem> noteItems = 
				new ArrayList<HearingAnalysisNoteItem>();
		for (HearingAnalysisNote note : notes) {
			HearingAnalysisNoteItem noteItem = new HearingAnalysisNoteItem();
			noteItem.setHearingAnalysisNote(note);
			noteItem.setDate(note.getDate());
			noteItem.setValue(note.getDescription());
			noteItem.setOperation(HearingAnalysisNoteItemOperation.EDIT);
			noteItems.add(noteItem);
		}
		form.setHearingAnalysisNoteItems(noteItems);
		return prepareMav(hearingAnalysis, form);
	}
	
	/**
	 * Updates hearing analysis tasks.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param form hearing analysis home form
	 * @param bindingResult binding result
	 * @return redirect to hearing analysis home screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@RequestMapping(value = "/home.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('HEARING_ANALYSIS_TASK_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "hearingAnalysis", required = true) 
			final HearingAnalysis hearingAnalysis,
			final HearingAnalysisHomeForm form,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.hearingAnalysisHomeFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareRedisplay(form, hearingAnalysis, bindingResult);
		}
		
		List<HearingAnalysisTaskItem> taskItems = new ArrayList<>();
		taskItems.addAll(form.getAnalysisTaskItems());
		taskItems.addAll(form.getPlanningTaskItems());
		
		for(HearingAnalysisTaskItem item : taskItems) {
			if(HearingAnalysisTaskItemOperation.SET_COMPLETE
					.equals(item.getItemOperation())) {
				Task task = item.getTaskAssociation().getTask(); 
				this.hearingAnalysisTaskService.updateTask(task,
						task.getControllerName(), task.getMethodName(),
						task.getDescription(), task.getSourceAccount(),
						task.getOriginationDate(), new Date());
			}
		}
		processHearingAnalysisNoteItems(hearingAnalysis,
				form.getHearingAnalysisNoteItems());
		
		return new ModelAndView(String.format(HOME_REDIRECT, 
				hearingAnalysis.getId()));
	}
	
	/**
	 * Displays action menu for hearing analysis home screen.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return action menu for hearing analysis home screen
	 */
	@RequestMapping(value = "/homeActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showHomeActionMenu(
			@RequestParam(value = "hearingAnalysis", required = true)
			final HearingAnalysis hearingAnalysis) {
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		return new ModelAndView(HOME_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helper methods. */
	
	// Prepares the model and view
	private ModelAndView prepareMav(final HearingAnalysis hearingAnalysis, 
			final HearingAnalysisHomeForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		this.offenderSummaryModelDelegate.add(map, 
				hearingAnalysis.getEligibility().getOffender());
		map.addAttribute(HEARING_ANALYSIS_SUMMARY_MODEL_KEY, 
				this.hearingAnalysisSummaryService.summarize(hearingAnalysis));
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(HEARING_ANALYSIS_NOTE_INDEX_MODEL_KEY, 
				form.getHearingAnalysisNoteItems() != null ? 
						form.getHearingAnalysisNoteItems().size() : 0);
		return new ModelAndView(HOME_VIEW_NAME, map);
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final HearingAnalysisHomeForm form,
			final HearingAnalysis hearingAnalysis,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(hearingAnalysis, form);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY, 
				bindingResult);
		return mav;
	}
	
	// Creates missing task pieces if necessary
	private void checkTasks(final HearingAnalysis hearingAnalysis) 
			throws DuplicateEntityFoundException {
		ParoleHearingTaskGroup taskGroup = this.hearingAnalysisTaskService
				.findParoleHearingTaskGroupByHearingAnalysisCategory(
						hearingAnalysis.getCategory());
		if (taskGroup != null) {
			List<ParoleHearingAnalysisTaskSource> taskSources = 
					this.hearingAnalysisTaskService
					.findParoleHearingTaskSourcesByTaskTemplateGroup(
							taskGroup.getGroup());
			for (ParoleHearingAnalysisTaskSource taskSource : taskSources) {
				Task task = this.hearingAnalysisTaskService
						.findTaskByTaskTemplateAndHearingAnalysis(
								taskSource.getTaskTemplate(), hearingAnalysis);
				if (task == null) {
					task = this.hearingAnalysisTaskService.createTask(
							taskSource.getTaskTemplate().getControllerName(),
							taskSource.getTaskTemplate().getMethodName(),
							taskSource.getTaskTemplate().getName(),
							this.retrieveUserAccount(), new Date(), null);
					this.hearingAnalysisTaskService
						.createHearingAnalysisTaskAssociation(task, 
								hearingAnalysis, taskSource);
				}
				List<TaskTemplateParameterValue> templateParameterValues = this
						.hearingAnalysisTaskService
						.findTaskTemplateParameterValuesByTaskTemplate(
								taskSource.getTaskTemplate());
				for (TaskTemplateParameterValue templateParameterValue 
						: templateParameterValues) {
					TaskParameterValue value = this.hearingAnalysisTaskService
							.findTaskParameterValueByTaskTemplateParameterValueAndTask(
									templateParameterValue, task);
					if (value == null) {
						String instanceValue = null;
						
						if (templateParameterValue.getTypeName().equals(
								HearingAnalysis.class.getTypeName())) {
							instanceValue = hearingAnalysis.getId().toString();
						} else if (templateParameterValue.getTypeName().equals(
								ParoleEligibility.class.getTypeName())) {
							instanceValue = hearingAnalysis.getEligibility()
									.getId().toString();
						} else if (templateParameterValue.getTypeName().equals(
								Person.class.getTypeName()) ||
								templateParameterValue.getTypeName().equals(
										Offender.class.getTypeName())) {
							instanceValue = hearingAnalysis.getEligibility()
									.getOffender().getId().toString();
						} else if (templateParameterValue.getTypeName().equals(
								Date.class.getTypeName())) {
							instanceValue = convertDateToString(hearingAnalysis
									.getEligibility()
									.getHearingEligibilityDate());
						} else {
							throw new UnsupportedOperationException("Task " +
									"Template Parameter Value Not Currently " +
									"Supported");
						}
						this.hearingAnalysisTaskService
							.createTaskParameterValue(task, 
									templateParameterValue.getOrder(),
									templateParameterValue.getTypeName(),
									instanceValue);
					}
				}
			}
		}
	}
	
	/**
	 * Retrieves the current users account.
	 * 
	 * @return current user
	 */
	private UserAccount retrieveUserAccount() {
		UserAccount userAccount = (UserAccount) RequestContextHolder
				.getRequestAttributes()
					.getAttribute(USER_ACCOUNT_MODEL_KEY,
						RequestAttributes.SCOPE_REQUEST);
		if (userAccount == null) {
			String username = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			userAccount = 
					this.hearingAnalysisTaskService.findUserAccountByUsername(
							username);
			RequestContextHolder.getRequestAttributes()
				.setAttribute(USER_ACCOUNT_MODEL_KEY, userAccount,
						RequestAttributes.SCOPE_REQUEST);
		}
		return userAccount;
	}
	
	// Converts date to string
	private String convertDateToString(final Date date) {
		PropertyEditor editor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		editor.setValue(date);
		return editor.getAsText();
	}
	
	// Process hearing analysis notes
	private void processHearingAnalysisNoteItems(
			final HearingAnalysis hearingAnalysis,
			final List<HearingAnalysisNoteItem> hearingAnalysisNoteItems) 
					throws DuplicateEntityFoundException {
		if (hearingAnalysisNoteItems != null) {
			for (HearingAnalysisNoteItem noteItem : hearingAnalysisNoteItems) {
				if (HearingAnalysisNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisTaskService.createHearingAnalysisNote(
							hearingAnalysis, noteItem.getValue(), 
							noteItem.getDate());
				} else if (HearingAnalysisNoteItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisTaskService.updateHearingAnalysisNote(
							noteItem.getHearingAnalysisNote(), 
							noteItem.getValue(), noteItem.getDate());
				} else if (HearingAnalysisNoteItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.hearingAnalysisTaskService.removeHearingAnalysisNote(
							noteItem.getHearingAnalysisNote());
				}
			}
		}
	}
	
	/* Exception handler methods. */
	
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
		(HEARING_ANALYSIS_TASK_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(HearingAnalysis.class, 
				this.hearingAnalysisPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysisTaskAssociation.class, 
				this.hearingAnalysisTaskAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysisNote.class,
				this.hearingAnalysisNotePropertyEditorFactory
				.createPropertyEditor());
	}
}
