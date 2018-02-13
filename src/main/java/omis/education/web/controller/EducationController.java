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
package omis.education.web.controller;

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

import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.VerificationMethodService;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.education.domain.EducationNote;
import omis.education.domain.EducationTerm;
import omis.education.domain.EducationalAchievement;
import omis.education.domain.EducationalAchievementCategory;
import omis.education.domain.InstituteCategory;
import omis.education.domain.component.Institute;
import omis.education.service.EducationService;
import omis.education.web.form.EducationForm;
import omis.education.web.form.EducationNoteItem;
import omis.education.web.form.EducationalAchievementItem;
import omis.education.web.form.ItemOperation;
import omis.education.web.validator.EducationFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * EducationController.java
 * 
 * @author Annie Jacques 
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/education/")
@PreAuthorize("hasRole('USER')")
public class EducationController {
	
	/* VIEW NAMES */
	
	private static final String EDIT_VIEW_NAME = "education/edit";
	
	private static final String EDUCATION_ACTION_MENU_VIEW_NAME 
		= "/education/includes/educationActionMenu";
	
	private static final String NOTE_ITEMS_ACTION_MENU_VIEW_NAME 
		= "/education/includes/noteItemsActionMenu";

	private static final String ACHIEVEMENT_ITEMS_ACTION_MENU_VIEW_NAME 
		= "/education/includes/achievementItemsActionMenu";
	
	private static final String NOTE_ITEM_ROW_VIEW_NAME 
		= "education/includes/noteItemTableRow";
	
	private static final String ACHIEVEMENT_ITEM_ROW_VIEW_NAME 
		= "education/includes/achievementItemTableRow";
	
	/* MODEL KEYS */
	
	private static final String FORM_MODEL_KEY = "educationForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String EDUCATION_TERM_MODEL_KEY = "educationTerm";
	
	private static final String NOTE_ITEM_MODEL_KEY = "noteItem";

	private static final String NOTE_ITEM_INDEX_MODEL_KEY = "noteItemIndex";
	
	private static final String ACHIEVEMENT_ITEM_MODEL_KEY = "achievementItem";

	private static final String ACHIEVEMENT_ITEM_INDEX_MODEL_KEY 
		= "achievementItemIndex";
	
	private static final String INSTITUTE_CATEGORIES_MODEL_KEY 
		= "instituteCategories";

	private static final String ACHIEVEMENT_CATEGORIES_MODEL_KEY
		= "achievementCategories";
	
	private static final String VERIFICATION_METHODS_MODEL_KEY 
		= "verificationMethods";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT 
		= "redirect:/education/list.html?offender=%d";
	
	/* Message Keys */
		
	private static final String ENTITY_EXISTS_MESSAGE_KEY
	= "entity.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
	= "omis.education.msgs.form";
	
	/* Services */
	
	@Autowired
	private EducationService educationService;
	
	@Autowired
	private VerificationMethodService verificationMethodService;
	
	/* Property Editors */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("educationTermPropertyEditorFactory")
	private PropertyEditorFactory educationTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("educationalAchievementPropertyEditorFactory")
	private PropertyEditorFactory educationalAchievementPropertyEditorFactory;
	
	@Autowired
	@Qualifier("educationNotePropertyEditorFactory")
	private PropertyEditorFactory educationNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("educationalAchievementCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
		educationalAchievementCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("instituteCategoryPropertyEditorFactory")
	private PropertyEditorFactory instituteCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("verificationMethodPropertyEditorFactory")
	private PropertyEditorFactory verificationMethodPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	private EducationFormValidator educationFormValidator;
	
	/* Screens */
	
	/**
	 * Returns a model and view to create an education term
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EDUCATION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "offender", required = true)
		final Offender offender){
		EducationForm form = new EducationForm();
		ModelMap modelMap = new ModelMap();
		final Integer noteIndex;
		final Integer achievementIndex;
		
		if (form.getNoteItems() != null) {
			noteIndex = form.getNoteItems().size(); 
		} else {
			noteIndex = 0;
		}
		if (form.getAchievementItems() != null) {
			achievementIndex = form.getAchievementItems().size(); 
		} else {
			achievementIndex = 0;
		}
		
		modelMap.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteIndex);
		modelMap.addAttribute(ACHIEVEMENT_ITEM_INDEX_MODEL_KEY, 
				achievementIndex);
		modelMap.put(ACHIEVEMENT_CATEGORIES_MODEL_KEY, 
				this.educationService.findAllEducationalAchievementCategories());
		modelMap.put(INSTITUTE_CATEGORIES_MODEL_KEY, 
				this.educationService.findAllInstituteCategories());
		modelMap.put(VERIFICATION_METHODS_MODEL_KEY, 
				this.verificationMethodService.findAll());
		modelMap.put(FORM_MODEL_KEY, form);
		modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(modelMap, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Creates a new education term and returns ModelAndView to the list screen,
	 * or returns to the ModelAndView for create if form errors were found.
	 * @param offender - offender
	 * @param educationForm - education form
	 * @param bindingResult - binding result
	 * @return ModelAndView
	 * @throws DuplicateEntityFoundException - when a duplicate entity 
	 * (education term, education note, educational achievement) was found
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EDUCATION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
		final Offender offender, final EducationForm educationForm,
		final BindingResult bindingResult)
				throws DuplicateEntityFoundException{
		ModelAndView modelAndView = null;
		
		
		
		this.educationFormValidator.validate(educationForm, bindingResult);
		
		if(bindingResult.hasErrors()){
			//return to edit view
			
			ModelMap modelMap = new ModelMap();
			final Integer noteIndex;
			final Integer achievementIndex;
			
			if (educationForm.getNoteItems() != null) {
				noteIndex = educationForm.getNoteItems().size(); 
			} else {
				noteIndex = 0;
			}
			if (educationForm.getAchievementItems() != null) {
				achievementIndex = educationForm.getAchievementItems().size(); 
			} else {
				achievementIndex = 0;
			}
			
			modelMap.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteIndex);
			modelMap.addAttribute(ACHIEVEMENT_ITEM_INDEX_MODEL_KEY, 
					achievementIndex);
			modelMap.put(ACHIEVEMENT_CATEGORIES_MODEL_KEY, 
					this.educationService
					.findAllEducationalAchievementCategories());
			modelMap.put(INSTITUTE_CATEGORIES_MODEL_KEY, 
					this.educationService.findAllInstituteCategories());
			modelMap.put(VERIFICATION_METHODS_MODEL_KEY, 
					this.verificationMethodService.findAll());
			modelMap.put(FORM_MODEL_KEY, educationForm);
			modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
			this.offenderSummaryModelDelegate.add(modelMap, offender);
			
			return new ModelAndView(EDIT_VIEW_NAME, modelMap);
			
		}
		else{
			//No errors, persist entities and redirect to list view
			
			Institute institute = null;
			DateRange attendedDateRange = null;
			VerificationSignature verificationSignature  = null;
			EducationTerm educationTerm = null;
			
			
			attendedDateRange = new DateRange(
					educationForm.getAttendedStartDate(), 
					educationForm.getAttendedEndDate());
			institute = educationForm.getInstitute();
			verificationSignature = new VerificationSignature(
				educationForm.getVerifiedUser(), 
				educationForm.getVerifiedDate(), educationForm.getVerified(), 
				educationForm.getVerificationMethod());
			
			
			if(educationForm.getGraduated()){
						//create educationTerm with degree achievement
				educationTerm = 
						this.educationService.
							createEducationTermWithAchievement(offender, 
								educationForm.getDescription(), 
								educationForm.getSpecialEducation(), 
								attendedDateRange, institute, 
								educationForm.getAchievementDate(),
								educationForm.getAchievementDescription(),
								educationForm.getAchievementCategory(),
								verificationSignature);
			}
			else{//create educationTerm without degree achievement
				educationTerm = 
						this.educationService.
							createEducationTermWithoutAchievement(offender, 
								educationForm.getDescription(), 
								educationForm.getSpecialEducation(), 
								attendedDateRange, institute, 
								verificationSignature);
			}
					
			//create notes
			this.processEducationNoteItems(educationForm.getNoteItems(), 
					educationTerm);
			
			//create achievements
			this.processEducationalAchievementItems(
					educationForm.getAchievementItems(), educationTerm);
					
			modelAndView = new ModelAndView(String.format(
					LIST_REDIRECT, educationTerm.getOffender().getId()));
		}
		
		return modelAndView;
	}
	
	/**
	 * Returns ModelAndView to edit a specified education term
	 * @param offender - offender
	 * @param educationTerm - education term
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EDUCATION_VIEW') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "offender", required = true)
			final Offender offender, @RequestParam(value = "educationTerm", 
			required = true) final EducationTerm educationTerm){
		EducationForm form = new EducationForm();
		
		List<EducationNote> notes 
			= this.educationService.findAllNotesByEducationTerm(educationTerm);
		List<EducationNoteItem> noteItems =
				new ArrayList<EducationNoteItem>();
		for(EducationNote note : notes){
			EducationNoteItem noteItem = new EducationNoteItem();
			noteItem.setDate(note.getDate());
			noteItem.setDescription(note.getDescription());
			noteItem.setNote(note);
			noteItem.setOperation(ItemOperation.UPDATE);
			noteItems.add(noteItem);
		}
		
		List<EducationalAchievement> achievements 
			= this.educationService
			.findAllEducationalAchievementsExcludingDegreeByEducationTerm(
					educationTerm);
		List<EducationalAchievementItem> achievementItems =
				new ArrayList<EducationalAchievementItem>();
		for(EducationalAchievement achievement : achievements){
			EducationalAchievementItem achievementItem 
				= new EducationalAchievementItem();
			achievementItem.setDate(achievement.getDate());
			achievementItem.setDescription(achievement.getDescription());
			achievementItem.setCategory(achievement.getCategory());
			achievementItem.setAchievement(achievement);
			achievementItem.setOperation(ItemOperation.UPDATE);
			achievementItems.add(achievementItem);
		}
		
		if(educationTerm.getAttendedDateRange() != null){
			form.setAttendedStartDate(educationTerm.getAttendedDateRange()
					.getStartDate());
			if(educationTerm.getAttendedDateRange().getEndDate() != null){
				form.setAttendedEndDate(educationTerm.getAttendedDateRange()
						.getEndDate());
			}
		}
		form.setDescription(educationTerm.getDescription());
		if(educationTerm.getAchievement() != null){
			form.setGraduated(true);
			form.setAchievementDate(educationTerm.getAchievement().getDate());
			form.setAchievementDescription(educationTerm.getAchievement()
					.getDescription());
			form.setAchievementCategory(educationTerm.getAchievement()
					.getCategory());
		}
		form.setInstitute(educationTerm.getInstitute());
		form.setSpecialEducation(educationTerm.getSpecialEducation());
		if(educationTerm.getVerificationSignature() != null){
			form.setVerificationMethod(educationTerm.getVerificationSignature()
					.getMethod());
			form.setVerified(educationTerm.getVerificationSignature()
					.getResult());
			form.setVerifiedDate(educationTerm.getVerificationSignature()
					.getDate());
			form.setVerifiedUser(educationTerm.getVerificationSignature()
					.getUserAccount());
		}
		form.setAchievementItems(achievementItems);
		form.setNoteItems(noteItems);
		
		ModelMap modelMap = new ModelMap();
		final Integer noteIndex;
		final Integer achievementIndex;
		
		if (form.getNoteItems() != null) {
			noteIndex = form.getNoteItems().size(); 
		} else {
			noteIndex = 0;
		}
		if (form.getAchievementItems() != null) {
			achievementIndex = form.getAchievementItems().size(); 
		} else {
			achievementIndex = 0;
		}
		
		modelMap.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteIndex);
		modelMap.addAttribute(ACHIEVEMENT_ITEM_INDEX_MODEL_KEY, 
				achievementIndex);
		modelMap.put(EDUCATION_TERM_MODEL_KEY, educationTerm);
		modelMap.put(ACHIEVEMENT_CATEGORIES_MODEL_KEY, 
				this.educationService.findAllEducationalAchievementCategories());
		modelMap.put(INSTITUTE_CATEGORIES_MODEL_KEY, 
				this.educationService.findAllInstituteCategories());
		modelMap.put(VERIFICATION_METHODS_MODEL_KEY, 
				this.verificationMethodService.findAll());
		modelMap.put(FORM_MODEL_KEY, form);
		modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(modelMap, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Updates an education term and returns model and view to the list screen,
	 * or returns model and view to edit if form errors were found
	 * @param educationForm - education form
	 * @param offender - offender
	 * @param educationTerm - education term
	 * @param bindingResult - binding result
	 * @return ModelAndBiew
	 * @throws DuplicateEntityFoundException - when a duplicate entity 
	 * (education term, education note, educational achievement) was found
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EDUCATION_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView update(final EducationForm educationForm, @RequestParam(
			value = "offender", required = true)
			final Offender offender, @RequestParam(value = "educationTerm", 
			required = true) final EducationTerm educationTerm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException{
		ModelAndView modelAndView = null;
		
		this.educationFormValidator.validate(educationForm, bindingResult);
		
		if(bindingResult.hasErrors()){
			//return to edit view
			ModelMap modelMap = new ModelMap();
			final Integer noteIndex;
			final Integer achievementIndex;
			
			if (educationForm.getNoteItems() != null) {
				noteIndex = educationForm.getNoteItems().size(); 
			} else {
				noteIndex = 0;
			}
			if (educationForm.getAchievementItems() != null) {
				achievementIndex = educationForm.getAchievementItems().size(); 
			} else {
				achievementIndex = 0;
			}
			
			modelMap.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteIndex);
			modelMap.addAttribute(ACHIEVEMENT_ITEM_INDEX_MODEL_KEY, 
					achievementIndex);
			modelMap.put(ACHIEVEMENT_CATEGORIES_MODEL_KEY, 
					this.educationService
					.findAllEducationalAchievementCategories());
			modelMap.put(INSTITUTE_CATEGORIES_MODEL_KEY, 
					this.educationService.findAllInstituteCategories());
			modelMap.put(VERIFICATION_METHODS_MODEL_KEY, 
					this.verificationMethodService.findAll());
			modelMap.put(FORM_MODEL_KEY, educationForm);
			modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
			this.offenderSummaryModelDelegate.add(modelMap, offender);
			
			return new ModelAndView(EDIT_VIEW_NAME, modelMap);
		}
		else{
			//No errors, persist entities and redirect to list view
			
			Institute institute = null;
			DateRange attendedDateRange = null;
			VerificationSignature verificationSignature  = null;
			EducationalAchievement achievement = null; 
			
			attendedDateRange = new DateRange(
					educationForm.getAttendedStartDate(), 
					educationForm.getAttendedEndDate());
			institute = educationForm.getInstitute();
			verificationSignature = new VerificationSignature(
				educationForm.getVerifiedUser(), 
				educationForm.getVerifiedDate(), educationForm.getVerified(), 
				educationForm.getVerificationMethod());
			
			if(!(educationForm.getGraduated())){
				//graduated unchecked, 
				//if graduation achievement existed, remove it
				if(educationTerm.getAchievement() != null){
					this.educationService.removeEducationalAchievement(
							educationTerm.getAchievement());
				}
			}
			else{//graduated checked
						//create educationTerm with degree achievement
				if(educationTerm.getAchievement() == null){
					//create new graduation achievement
					achievement = 
							this.educationService.createEducationalAchievement(
							educationTerm, educationForm.getAchievementDate(), 
							educationForm.getAchievementDescription(), 
							educationForm.getAchievementCategory());
				}
				else{
					//update existing graduation achievement
					achievement = 
							this.educationService.updateEducationalAchievement(
							educationTerm.getAchievement(), 
							educationForm.getAchievementDate(), 
							educationForm.getAchievementDescription(), 
							educationForm.getAchievementCategory());
				}
			}
			//update notes
			this.processEducationNoteItems(educationForm.getNoteItems(), 
					educationTerm);
			
			//update achievements
			this.processEducationalAchievementItems(
					educationForm.getAchievementItems(), educationTerm);
			
			
			
			this.educationService.updateEducationTerm(
					educationTerm,educationForm.getDescription(), 
					educationForm.getSpecialEducation(), attendedDateRange, 
					institute, achievement, verificationSignature);
					
			
					
			modelAndView = new ModelAndView(String.format(
					LIST_REDIRECT, educationTerm.getOffender().getId()));
		}
		
		return modelAndView;
	}
	
	/**
	 * Removes a specified education term and returns ModelAndView to list screen
	 * @param offender - offender
	 * @param educationTerm - education term
	 * @return ModelAndView
	 */
	@RequestMapping(value= "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('EDUCATION_REMOVE')")
	public ModelAndView remove(@RequestParam(
			value = "offender", required = true)
			final Offender offender, @RequestParam(value = "educationTerm", 
			required = true) final EducationTerm educationTerm){
		
		List<EducationNote> notes 
			= this.educationService.findAllNotesByEducationTerm(educationTerm);
		List<EducationalAchievement> achievements 
			= this.educationService
				.findAllEducationalAchievementsByEducationTerm(educationTerm);
			
		for(EducationNote note : notes){
			this.educationService.removeNote(note);
		}
		for(EducationalAchievement achievement : achievements){
			this.educationService.removeEducationalAchievement(achievement);
		}
		this.educationService.removeEducationTerm(educationTerm);
		
		return new ModelAndView(String.format(
				LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Displays a  note item row.
	 * 
	 * @return model and view for note item row
	 */
	@RequestMapping(value = "createNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayNoteItemRow(@RequestParam(
			value = "noteItemIndex", required = true)
			final Integer noteItemIndex) {
		ModelMap map = new ModelMap();
		EducationNoteItem noteItem = new EducationNoteItem();
		noteItem.setOperation(ItemOperation.CREATE);
		map.addAttribute(NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteItemIndex);
		return new ModelAndView(NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Displays an achievement item row.
	 * 
	 * @return model and view for achievement item row
	 */
	@RequestMapping(value = "createAchievementItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayAchievementItemRow(@RequestParam(
			value = "achievementItemIndex", required = true)
			final Integer achievementItemIndex) {
		ModelMap map = new ModelMap();
		EducationalAchievementItem achievementItem 
			= new EducationalAchievementItem();
		achievementItem.setOperation(ItemOperation.CREATE);
		map.put(ACHIEVEMENT_CATEGORIES_MODEL_KEY, 
			this.educationService.findAllEducationalAchievementCategories());
		map.addAttribute(ACHIEVEMENT_ITEM_MODEL_KEY, achievementItem);
		map.addAttribute(ACHIEVEMENT_ITEM_INDEX_MODEL_KEY, achievementItemIndex);
		return new ModelAndView(ACHIEVEMENT_ITEM_ROW_VIEW_NAME, map);
	}
	
	/* Action Menus */
	
	/**
	 * Returns model and view for education action menu
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value="/educationActionMenu.html", 
			method=RequestMethod.GET)
	public ModelAndView displayEducationActionMenu(@RequestParam(
			value = "offender", required = true) final Offender offender){
		
		ModelMap map = new ModelMap();
		
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(EDUCATION_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for note items.
	 * 
	 * @return model and view for note items action menu
	 */
	@RequestMapping(value = "noteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for achievement items.
	 * 
	 * @return model and view for achievement items action menu
	 */
	@RequestMapping(value = "achievementItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAchievementItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				ACHIEVEMENT_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Processes the specified list of note items
	 * according to their specified operation values.
	 * 
	 * @param items note items
	 * @param education education term
	 * @throws DuplicateEntityFoundException thrown when a duplicate education
	 * note is found
	 */
	private void processEducationNoteItems(
			final List<EducationNoteItem> items,
			final EducationTerm educationTerm)
		throws DuplicateEntityFoundException {
		if (items != null) {
			for (EducationNoteItem item 
					: items) {
				if (ItemOperation.CREATE
						.equals(item.getOperation())) {
					this.educationService.createNote(educationTerm, 
							item.getDate(), item.getDescription());
				} else if (ItemOperation.UPDATE
						.equals(item.getOperation())) {
					if (this.isNoteChanged(item.getNote(), item.getDate(), 
							item.getDescription())) {
						this.educationService.updateNote(
								item.getNote(), item.getDate(),
								item.getDescription());
					}
				} else if (ItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.educationService.removeNote(
							item.getNote());
				}
			}
		}
	}
	
	/*
	 * Returns whether the education note has different
	 * values than the supplied value and date.
	 * 
	 * @param note education note
	 * @param date date
	 * @param value value
	 * @return whether note information is changed
	 */
	private boolean isNoteChanged(final EducationNote note,
			final Date date, final String value) {
		if(!note.getDescription().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Processes the specified list of achievement items
	 * according to their specified operation values.
	 * 
	 * @param items achievement items
	 * @param education education term
	 * @throws DuplicateEntityFoundException thrown when a duplicate education
	 * achievement is found
	 */
	private void processEducationalAchievementItems(
			final List<EducationalAchievementItem> items,
			final EducationTerm educationTerm)
		throws DuplicateEntityFoundException {
		if (items != null) {
			for (EducationalAchievementItem item 
					: items) {
				if (ItemOperation.CREATE
						.equals(item.getOperation())) {
					this.educationService.createEducationalAchievement(
							educationTerm, item.getDate(), 
							item.getDescription(), item.getCategory());
				} else if (ItemOperation.UPDATE
						.equals(item.getOperation())) {
					this.educationService.updateEducationalAchievement(
							item.getAchievement(), item.getDate(),
							item.getDescription(), item.getCategory());
				} else if (ItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.educationService.removeEducationalAchievement(
							item.getAchievement());
				}
			}
		}
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
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class, this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(EducationTerm.class, 
				this.educationTermPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(EducationalAchievement.class, 
				this.educationalAchievementPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(EducationNote.class, 
				this.educationNotePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(EducationalAchievementCategory.class, 
				this.educationalAchievementCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(InstituteCategory.class, 
				this.instituteCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class, 
				this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(VerificationMethod.class, 
				this.verificationMethodPropertyEditorFactory.createPropertyEditor());
	}
}
