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
package omis.offender.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.ChangeOffenderNameService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offender.web.form.ChangeOffenderNameForm;
import omis.offender.web.validator.ChangeOffenderNameFormValidator;
import omis.person.domain.AlternativeNameCategory;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * ChangeOffenderNameController.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Mar 8, 2017)
 *@since OMIS 3.0
 *
 */

@Controller
@RequestMapping("/offender/name")
@PreAuthorize("hasRole('USER')")
public class ChangeOffenderNameController {
	
	private static final String NAME_CHANGE_VIEW_NAME = "offender/name/change/edit";
	
	private static final String NAME_CHANGE_ACTION_MENU_VIEW_NAME =
			"offender/name/change/includes/changeNameActionMenu";
	
	private static final String CHANGE_OFFENDER_NAME_FORM_MODEL_KEY =
			"changeOffenderNameForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String ALTERNATIVE_NAME_CATEGORIES_MODEL_KEY =
			"alternativeNameCategories";
	
	private static final String SUFFIXES_MODEL_KEY = "suffixes";
	
	/* REDIRECT */
	
	private static final String LEGAL_NAME_IDENTITY_REDIRECT =
			"redirect:/offender/personalDetails/edit.html?offender=%d";
	
	/* Services */
	
	@Autowired
	@Qualifier("changeOffenderNameService")
	private ChangeOffenderNameService changeOffenderNameService;
	
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("alternativeNameCategoryPropertyEditorFactory")
	private PropertyEditorFactory alternativeNameCategoryPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validator */
	
	@Autowired
	@Qualifier("changeOffenderNameFormValidator")
	private ChangeOffenderNameFormValidator changeOffenderNameFormValidator;
	
	/**
	 * Default Contructor for ChangeOffenderNameController
	 */
	public ChangeOffenderNameController() {
	}
	
	
	/**
	 * Returns the screen to change an Offender's legal name
	 * @param offender - Offender whose name is being changed
	 * @return ModelAndView - screen to change an Offender's legal name
	 */
	@RequestMapping(value="/change.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView changeName(
			@RequestParam(value="offender", required=true)
			final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(ALTERNATIVE_NAME_CATEGORIES_MODEL_KEY,
				this.changeOffenderNameService.findAlternativeNameCategories());
		map.addAttribute(CHANGE_OFFENDER_NAME_FORM_MODEL_KEY, 
				new ChangeOffenderNameForm());
		map.addAttribute(SUFFIXES_MODEL_KEY,
				this.changeOffenderNameService.findSuffixes());
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(NAME_CHANGE_VIEW_NAME, map);
	}
	
	/**
	 * Saves a changed legal name of an offender and returns to the legal
	 * name and identity screen, or back to the change legal name screen on
	 * form error
	 * @param offender - Offender whose name is being changed
	 * @param form - ChangeOffenderNameForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - returns to the legal name and identity screen,
	 * or back to the change legal name screen on form error
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value="/change.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('OFFENDER_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView saveName(
			@RequestParam(value="offender", required=true)
			final Offender offender, final ChangeOffenderNameForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException{
		
		this.changeOffenderNameFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(OFFENDER_MODEL_KEY, offender);
			map.addAttribute(ALTERNATIVE_NAME_CATEGORIES_MODEL_KEY,
					this.changeOffenderNameService.findAlternativeNameCategories());
			map.addAttribute(CHANGE_OFFENDER_NAME_FORM_MODEL_KEY, form);
			map.addAttribute(SUFFIXES_MODEL_KEY,
					this.changeOffenderNameService.findSuffixes());
			this.offenderSummaryModelDelegate.add(map, offender);
			
			return new ModelAndView(NAME_CHANGE_VIEW_NAME, map);
		}
		else{
			this.changeOffenderNameService.change(offender, form.getLastName(),
					form.getFirstName(), form.getMiddleName(), form.getSuffix(),
					form.getEffectiveDate(), form.getAlternativeNameCategory());
			
			return new ModelAndView(String.format(LEGAL_NAME_IDENTITY_REDIRECT,
					offender.getId()));
			
		}
	}
	
	
	/**
	 * Displays the change name action menu
	 * @param offender - Offender
	 * @return ModelAndView - change name action menu
	 */
	@RequestMapping(value="/changeNameActionMenu.html", method = RequestMethod.GET)
	public ModelAndView displayNameChangeActionMenu(
			@RequestParam(value="offender", required=true)
			final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(NAME_CHANGE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(AlternativeNameCategory.class,
				this.alternativeNameCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
	
	
}
