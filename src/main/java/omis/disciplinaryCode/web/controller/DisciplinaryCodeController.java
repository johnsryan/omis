package omis.disciplinaryCode.web.controller;

import java.io.IOException;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.domain.SupervisoryOrganizationCode;
import omis.disciplinaryCode.service.DisciplinaryCodeService;
import omis.disciplinaryCode.web.form.DisciplinaryCodeForm;
import omis.disciplinaryCode.web.validator.DisciplinaryCodeFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.supervision.domain.SupervisoryOrganization;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * DisciplinaryCodeController.java
 * 
 *@author Annie Jacques 
 *@version 0.1.1 (Sep 1, 2017)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/disciplinaryCode/")
@PreAuthorize("hasRole('USER')")
public class DisciplinaryCodeController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "disciplinaryCode/edit";
	
	private static final String DISCIPLINARY_CODE_ACTION_MENU_VIEW_NAME
		= "/disciplinaryCode/includes/disciplinaryCodeActionMenu";
	
	private static final String CODE_VIEW_NAME
		= "/disciplinaryCode/json/code";
	
	/* Model Keys */

	private static final String SUPERVISORY_ORGANIZATION_MODEL_KEY 
		= "supervisoryOrganization";
	
	private static final String SUPERVISORY_ORGANIZATION_CODE_MODEL_KEY 
		= "supervisoryOrganizationCode";
	
	private static final String FORM_MODEL_KEY = "disciplinaryCodeForm";
	
	private static final String CODES_MODEL_KEY = "codes";
	
	/* Redirect View Names */
	
	private static final String LIST_REDIRECT 
		= "redirect:/disciplinaryCode/list.html?supervisoryOrganization=%d";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY
		= "entity.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.disciplinaryCode.msgs.form";
	
	/* Services */
	
	@Autowired
	@Qualifier("disciplinaryCodeService")
	private DisciplinaryCodeService disciplinaryCodeService;
	
	/* Property editors */
	
	@Autowired
	@Qualifier("supervisoryOrganizationPropertyEditorFactory")
	private PropertyEditorFactory supervisoryOrganizationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("supervisoryOrganizationCodePropertyEditorFactory")
	private PropertyEditorFactory supervisoryOrganizationCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("disciplinaryCodePropertyEditorFactory")
	private PropertyEditorFactory disciplinaryCodePropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	DisciplinaryCodeFormValidator formValidator;
	
	/* Constructor */
	
	/**
	 * Default Constructor
	 */
	public DisciplinaryCodeController() {
		//Nada.
	}
	
	
	/* Model and Views */
	
	/**
	 * Returns a view to create a supervisory organization code/disciplinary code
	 * @param supervisoryOrganization - supervisory organization associated with
	 * the code to be created
	 * @return ModelAndView - view to create a supervisory organization 
	 * code/disciplinary code
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('DISCIPLINARY_CODE_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "supervisoryOrganization", required = true)
		final SupervisoryOrganization supervisoryOrganization){
		
		DisciplinaryCodeForm form = new DisciplinaryCodeForm();
		ModelMap map = new ModelMap();
		
		return prepareEditMav(supervisoryOrganization, form, map);
	}

	
	/**
	 * Attempts to save a supervisory organization code/disciplinary code
	 * @param supervisoryOrganization - supervisory organization associated with
	 * the supervisory organization code
	 * @param form - disciplinary code form
	 * @param bindingResult - binding result
	 * @return ModelAndView - back to the create view on validation errors,
	 * or back to the disciplinary code list view on success
	 * @throws DuplicateEntityFoundException - when an entity already exists 
	 * (supervisory organization already associated with a disciplinary code,
	 * or disciplinary code already has a specified value)
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('DISCIPLINARY_CODE_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(
			value = "supervisoryOrganization", required = true)
		final SupervisoryOrganization supervisoryOrganization, 
			final DisciplinaryCodeForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException{
		
		this.formValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			
			return prepareEditMav(supervisoryOrganization, form, map);
		}
		else{
			DisciplinaryCode code = null;
			
			if(form.getUseExistingCode()){
				code = form.getDisciplinaryCode();
			}
			else if(!(form.getUseExistingCode())){
				code = this.disciplinaryCodeService
					.createDisciplinaryCode(
						form.getCode(), form.getDescription(),
						form.getExtendedDescription());
			}
			this.disciplinaryCodeService.createSupervisoryOrganizationCode(
					supervisoryOrganization, new DateRange(
							form.getStartDate(), form.getEndDate()), code);

			return new ModelAndView(String.format(
					LIST_REDIRECT, supervisoryOrganization.getId()));
		}
	}
	
	
	/**
	 * Returns a view to edit a supervisory organization code
	 * @param supervisoryOrganizationCode - supervisory organization code to edit
	 * @return ModelAndView - to edit a supervisory organization code
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('DISCIPLINARY_CODE_VIEW') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value
				= "supervisoryOrganizationCode", required = true)
				final SupervisoryOrganizationCode supervisoryOrganizationCode){
		
		DisciplinaryCodeForm form = new DisciplinaryCodeForm();
		ModelMap map = new ModelMap();
		
		form.setDisciplinaryCode(supervisoryOrganizationCode.getCode());
		form.setStartDate(supervisoryOrganizationCode
				.getDateRange().getStartDate());
		form.setEndDate(supervisoryOrganizationCode
				.getDateRange().getEndDate());
		
		map.put(SUPERVISORY_ORGANIZATION_CODE_MODEL_KEY,
				supervisoryOrganizationCode);
		return prepareEditMav(supervisoryOrganizationCode
				.getSupervisoryOrganization(), form, map);
	}
	
	/**
	 * Updates a specified supervisory organization code on successful form
	 * validation and returns to disciplinary code list view
	 * @param supervisoryOrganizationCode - supervisory organization code to update
	 * @param form - disciplinary code form
	 * @param bindingResult - binding result
	 * @return ModelAndView - Back to the edit view on validation errors or
	 * to the disciplinary code list view on success
	 * @throws DuplicateEntityFoundException - when an entity already exists 
	 * (supervisory organization already associated with a disciplinary code,
	 * or disciplinary code already has a specified value)
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('DISCIPLINARY_CODE_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(
				value = "supervisoryOrganizationCode", required = true)
			final SupervisoryOrganizationCode supervisoryOrganizationCode,
			final DisciplinaryCodeForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException{
		
		this.formValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			
			map.put(SUPERVISORY_ORGANIZATION_CODE_MODEL_KEY,
					supervisoryOrganizationCode);
			return prepareEditMav(supervisoryOrganizationCode
					.getSupervisoryOrganization(), form, map);
		}
		else{
			DisciplinaryCode code = null;
			
			if(form.getUseExistingCode()){
				code = form.getDisciplinaryCode();
			}
			else if(!(form.getUseExistingCode())){
				code = this.disciplinaryCodeService
						.createDisciplinaryCode(
							form.getCode(), form.getDescription(),
							form.getExtendedDescription());
			}
			
			this.disciplinaryCodeService.updateSupervisoryOrganizationCode(
					supervisoryOrganizationCode, new DateRange(
							form.getStartDate(), form.getEndDate()), code);
			return new ModelAndView(String.format(
					LIST_REDIRECT, supervisoryOrganizationCode
					.getSupervisoryOrganization().getId()));
		}
	}
	
	/**
	 * Removes specified supervisory organization code and returns to 
	 * disciplinary code list view
	 * @param supervisoryOrganizationCode - supervisory organization code to remove
	 * @return ModelAndView - Disciplinary Code List view
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('DISCIPLINARY_CODE_REMOVE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView remove(@RequestParam(
			value = "supervisoryOrganizationCode", required = true)
		final SupervisoryOrganizationCode supervisoryOrganizationCode){
		
		SupervisoryOrganization organization = supervisoryOrganizationCode
				.getSupervisoryOrganization();
		
		this.disciplinaryCodeService.removeSupervisoryOrganizationCode(
				supervisoryOrganizationCode);
		
		return new ModelAndView(String.format(
				LIST_REDIRECT, organization.getId()));
	}
	
	/* Action Menus */
	
	/**
	 * Displays action menu for disciplinary codes
	 * @param supervisoryOrganization - supervisory organization
	 * @return model and view
	 */
	@RequestMapping(value="/disciplinaryCodeActionMenu.html", 
			method=RequestMethod.GET)
	public ModelAndView displayDisciplinaryCodeActionMenu(@RequestParam(
			value = "supervisoryOrganization", required = true) 
			final SupervisoryOrganization supervisoryOrganization){
		
		ModelMap map = new ModelMap();
		map.addAttribute(SUPERVISORY_ORGANIZATION_MODEL_KEY, 
				supervisoryOrganization);
		
		return new ModelAndView(DISCIPLINARY_CODE_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/* Helper Methods */
	
	/**
	 * Returns a prepared model and view for editing disciplinary code
	 * @param supervisoryOrganization - supervisory organization
	 * @param form - disciplinary code form
	 * @param map - model map
	 * @return ModelAndView
	 */
	private ModelAndView prepareEditMav(
			final SupervisoryOrganization supervisoryOrganization,
			final DisciplinaryCodeForm form, final ModelMap map) {
		map.addAttribute(SUPERVISORY_ORGANIZATION_MODEL_KEY, 
				supervisoryOrganization);
		map.addAttribute(FORM_MODEL_KEY, form);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	
	/** returns disciplinary codes given search criteria.
	 * @param searchCriteria.
	 * @return view of 
	 * @throws IOException  */
	@RequestMapping(value ="/findCode.json", 
		method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchCodeByValue(
		  @RequestParam(value = "term", required = false) 
		 	final String value) throws IOException {
		List<DisciplinaryCode> codes;
		if (StringUtility.hasContent(value)) {
			codes = this.disciplinaryCodeService.findDisciplinaryCodeByValue(value);
		} else {
			codes = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(CODE_VIEW_NAME);
		mav.addObject(CODES_MODEL_KEY, codes);
		return mav;
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
		binder.registerCustomEditor(SupervisoryOrganization.class, 
				this.supervisoryOrganizationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(DisciplinaryCode.class, 
				this.disciplinaryCodePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(SupervisoryOrganizationCode.class, 
				this.supervisoryOrganizationCodePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
	}
	
}
