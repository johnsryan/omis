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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
import omis.datatype.DateRange;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.AlternativeOffenderNameService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offender.web.form.AlternativeOffenderNameForm;
import omis.offender.web.validator.AlternativeOffenderNameFormValidator;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.Suffix;
import omis.person.exception.AlternativeNameAssociationExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for alternative offender names.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.1 (Sept 24, 2014)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offender/name/alternative")
public class AlternativeOffenderNameController {

	/* View names. */
	
	private static final String EDIT_VIEW_NAME
		= "offender/name/alternative/edit";
	
	private static final String LIST_VIEW_NAME
		= "offender/name/alternative/list";
	
	/* Action menu view names. */
	
	private static final String ALTERNATIVE_NAMES_ACTION_MENU_VIEW_NAME
		= "offender/name/alternative/includes/alternativeNamesActionMenu";

	private static final String ALTERNATIVE_NAME_ACTION_MENU_VIEW_NAME
		= "offender/name/alternative/includes/alternativeNameActionMenu";
	
	private static final String ALTERNATIVE_NAMES_ROW_ACTION_MENU_VIEW_NAME
		= "offender/name/alternative/includes/alternativeNamesRowActionMenu";
	
	/* Redirects. */
	
	private static final String LIST_REDIRECT
		= "redirect:/offender/name/alternative/list.html?offender=%d";
	
	/* Model keys. */

	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String ALTERNATIVE_OFFENDER_NAME_FORM_MODEL_KEY
			= "alternativeOffenderNameForm";

	private static final String SUFFIXES_MODEL_KEY = "suffixes";

	private static final String ALTERNATIVE_NAME_CATEGORIES_MODEL_KEY
			= "categories";

	private static final String ALTERNATIVE_NAME_ASSOCIATION_MODEL_KEY
			= "alternativeNameAssociation";

	private static final String ALTERNATIVE_NAME_ASSOCIATIONS_MODEL_KEY
			= "alternativeNameAssociations";
	
	private static final String CURRENT_DATE_MODEL_KEY = "currentDate";
	
	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME = "omis.offender.msgs.form";
	
	/* Message keys. */
	
	private static final String PERSON_NAME_EXISTS_MESSAGE_KEY
		= "personName.exists";
	
	private static final String ALTERNATIVE_NAME_ASSOCIATION_EXISTS_MESSAGE_KEY
		= "alternativeNameAssociation.exists";
	
	/* Report names. */
	
	private static final String ALT_NAME_LISTING_REPORT_NAME 
		= "/BasicInformation/AlternativeNames/Alternative_Names_Listing";

	private static final String ALT_NAME_DETAILS_REPORT_NAME 
		= "/BasicInformation/AlternativeNames/Alternative_Name_Details";

	/* Report parameter names. */
	
	private static final String ALT_NAME_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String ALT_NAME_DETAILS_ID_REPORT_PARAM_NAME 
		= "ALT_NAME_ID";
	
	/* Services. */
	
	@Autowired
	@Qualifier("alternativeOffenderNameService")
	private AlternativeOffenderNameService alternativeOffenderNameService;
	
	/* Report service. */
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;

	@Autowired
	@Qualifier("alternativeNameAssociationPropertyEditorFactory")
	private PropertyEditorFactory
			alternativeNameAssociationPropertyEditorFactory;

	@Autowired
	@Qualifier("alternativeNameCategoryPropertyEditorFactory")
	private PropertyEditorFactory alternativeNameCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validator. */
	
	@Autowired
	@Qualifier("alternativeOffenderNameFormValidator")
	private AlternativeOffenderNameFormValidator
			alternativeOffenderNameFormValidator;

	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;

	/* Constructor. */
	
	/** Instantiates a default controller for alternative offender names. */
	public AlternativeOffenderNameController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays alternative names for offender.
	 * 
	 * @param offender offender whose alternative names to display
	 * @return screen showing alternative names for offender
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
				@RequestParam(value = "offender", required = true)
					final Offender offender) {
		List<AlternativeNameAssociation> associations
			= this.alternativeOffenderNameService.findAssociations(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(ALTERNATIVE_NAME_ASSOCIATIONS_MODEL_KEY, associations);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		Date currentDate = new Date();
		mav.addObject(CURRENT_DATE_MODEL_KEY, currentDate);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Displays form to create alternative name.
	 * 
	 * @param offender offender for whom to create alternative name
	 * @return form to create alternative name
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		return prepareEditMav(offender, new AlternativeOffenderNameForm());
	}
	
	/**
	 * Displays form to edit alternative name.
	 * 
	 * @param alternativeNameAssociation association of alternative name to edit
	 * @return form to edit alternative name
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "alternativeNameAssociation",
				required = true)
			final AlternativeNameAssociation alternativeNameAssociation) {
		AlternativeOffenderNameForm alternativeOffenderNameForm
				= new AlternativeOffenderNameForm();
		alternativeOffenderNameForm.setLastName(
				alternativeNameAssociation.getName().getLastName());
		alternativeOffenderNameForm.setFirstName(
				alternativeNameAssociation.getName().getFirstName());
		alternativeOffenderNameForm.setMiddleName(
				alternativeNameAssociation.getName().getMiddleName());
		alternativeOffenderNameForm.setSuffix(
				alternativeNameAssociation.getName().getSuffix());
		alternativeOffenderNameForm.setCategory(
				alternativeNameAssociation.getCategory());
		if (alternativeNameAssociation.getDateRange() != null) {
			alternativeOffenderNameForm.setStartDate(
					alternativeNameAssociation.getDateRange().getStartDate());
			alternativeOffenderNameForm.setEndDate(
					alternativeNameAssociation.getDateRange().getEndDate());
		}
		ModelAndView mav =  prepareEditMav(
				(Offender) alternativeNameAssociation.getName().getPerson(),
				alternativeOffenderNameForm);
		mav.addObject(ALTERNATIVE_NAME_ASSOCIATION_MODEL_KEY,
				alternativeNameAssociation);
		return mav;
	}

	/**
	 * Saves a new alternative name.
	 * 
	 * @param offender offender
	 * @param alternativeOffenderNameForm alternative offender name form
	 * @param result binding result
	 * @return redirect to list alternative names
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists 
	 * @throws PersonNameExistsException if person name exists
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			final AlternativeOffenderNameForm alternativeOffenderNameForm,
			final BindingResult result)
					throws PersonNameExistsException,
						AlternativeNameAssociationExistsException {
		this.alternativeOffenderNameFormValidator
			.validate(alternativeOffenderNameForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(
					offender, alternativeOffenderNameForm, result);
		}
		AlternativeNameAssociation association = 
				this.alternativeOffenderNameService.associate(offender, 
				alternativeOffenderNameForm.getLastName(), 
				alternativeOffenderNameForm.getFirstName(), 
				alternativeOffenderNameForm.getMiddleName(), 
				alternativeOffenderNameForm.getSuffix(), 
				new DateRange(alternativeOffenderNameForm.getStartDate(), 
						alternativeOffenderNameForm.getEndDate()), 
				alternativeOffenderNameForm.getCategory());
		return prepareAlternativeNameListRedirectMav(
				(Offender) association.getName().getPerson());
	}
	
	/**
	 * Updates an existing alternative name.
	 * 
	 * @param alternativeNameAssociation association of alternative name
	 * to update
	 * @param alternativePersonNameForm alternative person name form
	 * @param result binding result
	 * @return redirect to list alternative names
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists 
	 * @throws PersonNameExistsException if person name exists
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "alternativeNameAssociation",
					required = true)
				final AlternativeNameAssociation alternativeNameAssociation,
			final AlternativeOffenderNameForm alternativePersonNameForm,
			final BindingResult result)
					throws PersonNameExistsException,
						AlternativeNameAssociationExistsException {
		this.alternativeOffenderNameFormValidator
				.validate(alternativePersonNameForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(
					(Offender) alternativeNameAssociation.getName().getPerson(),
					alternativePersonNameForm, result); 
			mav.addObject(ALTERNATIVE_NAME_ASSOCIATION_MODEL_KEY,
					alternativeNameAssociation);
			return mav;
		}
		this.alternativeOffenderNameService.updateAssociation(
				alternativeNameAssociation, 
				alternativePersonNameForm.getLastName(), 
				alternativePersonNameForm.getFirstName(), 
				alternativePersonNameForm.getMiddleName(), 
				alternativePersonNameForm.getSuffix(), 
				new DateRange(alternativePersonNameForm.getStartDate(),
						alternativePersonNameForm.getEndDate()), 
				alternativePersonNameForm.getCategory());
		return prepareAlternativeNameListRedirectMav(
				(Offender) alternativeNameAssociation.getName().getPerson());
	}
	
	/**
	 * Removes an alternative offender name.
	 * 
	 * @param alternativeNameAssociation association of alternative offender
	 * name to remove
	 * @return redirect to list alternative names
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "alternativeNameAssociation", required = true)
				final AlternativeNameAssociation alternativeNameAssociation) {
		Offender offender = (Offender)
					alternativeNameAssociation.getName().getPerson();
		this.alternativeOffenderNameService.remove(alternativeNameAssociation);
		return prepareAlternativeNameListRedirectMav(offender);
	}
	
	/* Action menus. */
	
	/**
	 * Returns action menu for alternative names.
	 * 
	 * @param offender offender
	 * @return action menu for alternative names
	 */
	@RequestMapping(value = "/alternativeNamesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showAlternativeNamesActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				ALTERNATIVE_NAMES_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns action menu for alternative name.
	 * 
	 * @param offender offender
	 * @return action menu for alternative name
	 */
	@RequestMapping(value = "/alternativeNameActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showAlternativeNameActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				ALTERNATIVE_NAME_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns action menu for alternative names row.
	 * 
	 * @param association alternative name association
	 * @return action menu for alternative names row
	 */
	@RequestMapping(value = "/alternativeNameAssociationsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showAlternativeNamesRowActionMenu(
			@RequestParam(value = "alternativeNameAssociation", required = true)
				final AlternativeNameAssociation association) {
		ModelMap map = new ModelMap();
		map.addAttribute(ALTERNATIVE_NAME_ASSOCIATION_MODEL_KEY, association);
		return new ModelAndView(ALTERNATIVE_NAMES_ROW_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/* Exception handlers. */
	
	/**
	 * Returns screen to handle {@code PersonNameExistsException}.
	 * 
	 * @param personNameExistsException exception thrown
	 * @return screen to handle {@code PersonNameExistsException}
	 */
	@ExceptionHandler(PersonNameExistsException.class)
	public ModelAndView handlePersonNameExistsException(
			final PersonNameExistsException personNameExistsException) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(PERSON_NAME_EXISTS_MESSAGE_KEY,
						ERROR_BUNDLE_NAME, personNameExistsException);
	}
	
	/**
	 * Returns screen to handle
	 * {@code AlternativeNameAssociationExistsException}.
	 * 
	 * @param alternativeNameAssociationExistsException exception thrown
	 * @return screen to handle
	 * {@code AlternativeNameAssociationExistsException}
	 */
	@ExceptionHandler(AlternativeNameAssociationExistsException.class)
	public ModelAndView handleAlternativeNameAssociationExistsException(
			final AlternativeNameAssociationExistsException
				alternativeNameAssociationExistsException) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(
						ALTERNATIVE_NAME_ASSOCIATION_EXISTS_MESSAGE_KEY,
						ERROR_BUNDLE_NAME,
						alternativeNameAssociationExistsException);
	}
	
	/* Helper methods. */
	
	// Returns a redirect to alternative name list screen
	private ModelAndView prepareAlternativeNameListRedirectMav(
			final Offender offender) {
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	// Adds offender summary to a model and view
	private void addOffenderSummary(
			final ModelAndView mav, final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplayMav(
			final Offender offender,
			final AlternativeOffenderNameForm alternativePersonNameForm,
			final BindingResult result) {
		ModelAndView mav
			= this.prepareEditMav(offender, alternativePersonNameForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ ALTERNATIVE_OFFENDER_NAME_FORM_MODEL_KEY, result);
		return mav;
	}
	
	// Prepares model and view to edit alternative offender name
	private ModelAndView prepareEditMav(final Offender offender,
			final AlternativeOffenderNameForm alternativePersonNameForm) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(ALTERNATIVE_OFFENDER_NAME_FORM_MODEL_KEY,
				alternativePersonNameForm);
		List<Suffix> suffixes = this.alternativeOffenderNameService
				.findSuffixes();
		mav.addObject(SUFFIXES_MODEL_KEY, suffixes);
		List<AlternativeNameCategory> categories
				= this.alternativeOffenderNameService.findCategories();
		mav.addObject(ALTERNATIVE_NAME_CATEGORIES_MODEL_KEY, categories);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	/* Reports. */

	/**
	 * Returns the report for the specified offenders alternative names.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altNameListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAltNameListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_NAME_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				ALT_NAME_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified alternative name association.
	 * 
	 * @param alternativeNameAssociation alternative name association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altNameDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAltNameDetails(@RequestParam(
			value = "alternativeNameAssociation", required = true)
			final AlternativeNameAssociation alternativeNameAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_NAME_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(alternativeNameAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				ALT_NAME_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
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
		binder.registerCustomEditor(AlternativeNameAssociation.class,
				this.alternativeNameAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AlternativeNameCategory.class,
				this.alternativeNameCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}