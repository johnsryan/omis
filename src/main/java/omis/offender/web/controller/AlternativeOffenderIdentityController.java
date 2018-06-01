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

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.AlternativeOffenderIdentityService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offender.web.form.AlternativeOffenderIdentityForm;
import omis.offender.web.validator.AlternativeOffenderIdentityFormValidator;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.exception.AlternativeIdentityAssociationExistsException;
import omis.person.exception.PersonIdentityExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

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

/**
 * Controller for alternative offender identities.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 10, 2013)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offender/identity/alternative")
public class AlternativeOffenderIdentityController {

	/* View names. */
	
	private static final String EDIT_VIEW_NAME
		= "offender/identity/alternative/edit";
	
	private static final String LIST_VIEW_NAME
		= "offender/identity/alternative/list";
	
	private static final String STATE_OPTIONS_VIEW_NAME
		= "region/includes/stateOptions";
	
	private static final String CITY_OPTIONS_VIEW_NAME
		= "region/includes/cityOptions";
	
	/* Action menu view names. */
	
	private static final String ALTERNATIVE_IDENTITY_ACTION_MENU_VIEW_NAME
		= "offender/identity/alternative/includes/alternativeIdentityActionMenu";

	private static final String ALTERNATIVE_IDENTITIES_ACTION_MENU_VIEW_NAME
		= "offender/identity/alternative/includes"
				+ "/alternativeIdentitiesActionMenu";
	
	/* Redirects. */
	
	private static final String LIST_REDIRECT
		= "redirect:/offender/identity/alternative/list.html?offender=%d";
	
	/* Model keys. */

	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String SEXES_MODEL_KEY = "sexes";

	private static final String ALTERNATIVE_IDENTITY_CATEGORIES_MODEL_KEY
		= "categories";
	
	private static final String ALTERNATIVE_OFFENDER_IDENTITY_FORM_MODEL_KEY
		= "alternativeOffenderIdentityForm";
	
	private static final String ALTERNATIVE_IDENTITY_ASSOCIATION_MODEL_KEY
		= "alternativeIdentityAssociation";

	private static final String ALTERNATIVE_IDENTITY_ASSOCIATIONS_MODEL_KEY
		= "alternativeIdentityAssociations";
	
	private static final String COUNTRIES_MODEL_KEY = "countries";
	
	private static final String CURRENT_DATE_MODEL_KEY = "currentDate";

	private static final String STATES_MODEL_KEY = "states";

	private static final String CITIES_MODEL_KEY = "cities";
	
	/* Errors messages. */
	
	private static final String PERSON_IDENTITY_EXISTS_MESSAGE_KEY
		= "personIdentity.exists";
	
	private static final String ALTERNATIVE_IDENTITY_ASSOCIATION_EXISTS_MESSAGE_KEY
		= "alternativeIdentityAssociation.exists";
	
	private static final String CITY_EXISTS_MESSAGE_KEY = "birthCity.exists";

	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.offender.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("alternativeOffenderIdentityService")
	private AlternativeOffenderIdentityService
			alternativeOffenderIdentityService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("alternativeIdentityAssociationPropertyEditorFactory")
	private PropertyEditorFactory
			alternativeIdentityAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("alternativeIdentityCategoryPropertyEditorFactory")
	private PropertyEditorFactory
			alternativeIdentityCategoryPropertyEditorFactory;	
	
	@Autowired
	@Qualifier("alternativeNameAssociationPropertyEditorFactory")
	private PropertyEditorFactory
			alternativeNameAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("sexPropertyEditorFactory")
	private PropertyEditorFactory sexPropertyEditorFactory;
	
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validator. */
	
	@Autowired
	@Qualifier("alternativeOffenderIdentityFormValidator")
	private AlternativeOffenderIdentityFormValidator
			alternativeOffenderIdentityFormValidator;

	
	/* Report names. */
	
	private static final String ALT_IDENTITY_LISTING_FULL_REPORT_NAME 
		= "/BasicInformation/AlternativeIdentities/Alternative_Identities_Listing";

	private static final String ALT_IDENTITY_LISTING_REDACTED_REPORT_NAME 
		= "/BasicInformation/AlternativeIdentities/Alternative_Identities_Listing_Redacted";

	private static final String ALT_IDENTITY_DETAILS_FULL_REPORT_NAME 
		= "/BasicInformation/AlternativeIdentities/Alternative_Identity_Details";
	
	private static final String ALT_IDENTITY_DETAILS_REDACTED_REPORT_NAME 
		= "/BasicInformation/AlternativeIdentities/Alternative_Identity_Details_Redacted";

	/* Report parameter names. */
	
	private static final String ALT_IDENTITY_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String ALT_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME 
		= "ALT_IDENTITY_ASSOC_ID";
	
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
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Constructor. */
	
	/** Instantiates a controller for alternative offender identities. */
	public AlternativeOffenderIdentityController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays alternative identities for offender.
	 * 
	 * @param offender offender whose alternative identities to display
	 * @return screen showing alternative identities for offender
	 */
	@PreAuthorize(
			"hasRole('OFFENDER_ALT_IDENTITY_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		List<AlternativeIdentityAssociation> associations
				= this.alternativeOffenderIdentityService
					.findAssociations(offender);
		mav.addObject(ALTERNATIVE_IDENTITY_ASSOCIATIONS_MODEL_KEY,
				associations);
		Date currentDate = new Date();
		mav.addObject(CURRENT_DATE_MODEL_KEY, currentDate);
		this.addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Displays form to create alternative identity.
	 * 
	 * @param offender offender for whom to create alternative identity
	 * @return form to create alternative identity
	 */
	@PreAuthorize(
			"hasRole('OFFENDER_ALT_IDENTITY_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		AlternativeOffenderIdentityForm alternativeOffenderIdentityForm
			= new AlternativeOffenderIdentityForm();
		alternativeOffenderIdentityForm.setCreateNewBirthPlace(false);
		State homeState = this.alternativeOffenderIdentityService
				.findHomeState();
		if (homeState != null) {
			alternativeOffenderIdentityForm.setBirthCountry(
					homeState.getCountry());
		}
		return prepareInitialEditMav(offender, alternativeOffenderIdentityForm);
	}
	
	/**
	 * Displays form to edit alternative identity.
	 * 
	 * @param alternativeIdentityAssociation association of alternative identity
	 * to edit
	 * @return form to edit alternative identity
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "alternativeIdentityAssociation",
				required = true)
			final AlternativeIdentityAssociation
				alternativeIdentityAssociation) {
		AlternativeOffenderIdentityForm alternativeOffenderIdentityForm
				= new AlternativeOffenderIdentityForm();
		if (alternativeIdentityAssociation.getIdentity()
				.getSocialSecurityNumber() != null) {
			String ssnString = String.format("%09d",
					alternativeIdentityAssociation.getIdentity()
						.getSocialSecurityNumber());
			if (ssnString.length() == 9) {
				ssnString = String.format("%s-%s-%s",
						ssnString.substring(0, 3),
						ssnString.substring(3, 5),
						ssnString.substring(5, 9));
			}
			alternativeOffenderIdentityForm.setSocialSecurityNumber(ssnString);
		}
		alternativeOffenderIdentityForm.setStateIdNumber(
				alternativeIdentityAssociation.getIdentity()
					.getStateIdNumber());
		alternativeOffenderIdentityForm.setBirthDate(
				alternativeIdentityAssociation.getIdentity().getBirthDate());
		alternativeOffenderIdentityForm.setBirthCountry(
				alternativeIdentityAssociation.getIdentity().getBirthCountry());
		alternativeOffenderIdentityForm.setBirthState(
				alternativeIdentityAssociation.getIdentity().getBirthState());
		alternativeOffenderIdentityForm.setBirthPlace(
				alternativeIdentityAssociation.getIdentity().getBirthPlace());
		alternativeOffenderIdentityForm.setCreateNewBirthPlace(false);
		alternativeOffenderIdentityForm.setSex(
				alternativeIdentityAssociation.getIdentity().getSex());
		alternativeOffenderIdentityForm.setCategory(
				alternativeIdentityAssociation.getCategory());
		if (alternativeIdentityAssociation.getAlternativeNameAssociation() 
				!= null) {
			alternativeOffenderIdentityForm.setAlternativeNameAssociation(
					alternativeIdentityAssociation
					.getAlternativeNameAssociation());
		}
		if (alternativeIdentityAssociation.getDateRange() != null) {
			alternativeOffenderIdentityForm.setStartDate(
				alternativeIdentityAssociation.getDateRange().getStartDate());
			alternativeOffenderIdentityForm.setEndDate(
				alternativeIdentityAssociation.getDateRange().getEndDate());
		}
		ModelAndView mav = prepareEditMav(
				(Offender)
					alternativeIdentityAssociation.getIdentity().getPerson(),
					alternativeOffenderIdentityForm);
		mav.addObject(ALTERNATIVE_IDENTITY_ASSOCIATION_MODEL_KEY,
				alternativeIdentityAssociation);
		return mav;
	}
	
	// Prepares model and view to edit initial alternative person identity
	private ModelAndView prepareInitialEditMav(
			final Offender offender,
			final AlternativeOffenderIdentityForm
				alternativeOffenderIdentityForm) {
		List<AlternativeIdentityCategory> categories
			= this.alternativeOffenderIdentityService.findCategories();
		if (categories.size() == 1) {
			alternativeOffenderIdentityForm.setCategory(categories.get(0));
		}
		return this.prepareEditMavImpl(
				offender, alternativeOffenderIdentityForm, categories);
	}
	
	// Prepares model and view to edit alternative person identity
	private ModelAndView prepareEditMav(
			final Offender offender,
			final AlternativeOffenderIdentityForm
				alternativeOffenderIdentityForm) {
		List<AlternativeIdentityCategory> categories
			= this.alternativeOffenderIdentityService.findCategories();
		return this.prepareEditMavImpl(
				offender, alternativeOffenderIdentityForm, categories);
	}
	
	// Implements preparation of model and view to edit alternative person
	// identity
	private ModelAndView prepareEditMavImpl(
			final Offender offender,
			final AlternativeOffenderIdentityForm
				alternativeOffenderIdentityForm,
			final List<AlternativeIdentityCategory> categories) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(ALTERNATIVE_OFFENDER_IDENTITY_FORM_MODEL_KEY,
				alternativeOffenderIdentityForm);
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		List<Country> countries = this.alternativeOffenderIdentityService
				.findCountries();
		mav.addObject(COUNTRIES_MODEL_KEY, countries);
		if (alternativeOffenderIdentityForm.getBirthCountry() != null) {
			List<State> states = this.alternativeOffenderIdentityService
					.findStatesByCountry(alternativeOffenderIdentityForm
							.getBirthCountry());
			mav.addObject(STATES_MODEL_KEY, states);
		}
		if (alternativeOffenderIdentityForm.getBirthState() != null) {
			List<City> cities = this.alternativeOffenderIdentityService
					.findCitiesByState(alternativeOffenderIdentityForm
							.getBirthState());
			mav.addObject(CITIES_MODEL_KEY, cities);
		} else if (
				alternativeOffenderIdentityForm.getBirthCountry() != null) {
			List<City> cities;
			if (this.alternativeOffenderIdentityService.hasStates(
					alternativeOffenderIdentityForm.getBirthCountry())) {
				cities = this.alternativeOffenderIdentityService
						.findCitiesByCountryWithoutState(
							alternativeOffenderIdentityForm.getBirthCountry());
			} else {
				cities = this.alternativeOffenderIdentityService
						.findCitiesByCountry(
							alternativeOffenderIdentityForm.getBirthCountry());	
			}
			mav.addObject(CITIES_MODEL_KEY, cities);
		}
		mav.addObject(ALTERNATIVE_IDENTITY_CATEGORIES_MODEL_KEY, categories);
		List<AlternativeNameAssociation> alternativeNames 
			= this.alternativeOffenderIdentityService
				.findAlternativeNames(offender);
		mav.addObject("alternativeNames", alternativeNames);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Saves a new alternative identity.
	 * 
	 * @param offender offender
	 * @param alternativeOffenderIdentityForm alternative offender identity form
	 * @param result binding result
	 * @return redirect to list alternative names
	 * @throws DuplicateEntityFoundException 
	 */
	@PreAuthorize(
			"hasRole('OFFENDER_ALT_IDENTITY_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			final AlternativeOffenderIdentityForm form,
			final BindingResult result)
					throws CityExistsException,
						AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException {
		this.alternativeOffenderIdentityFormValidator.validate(
				form, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(offender,
					form, result);
			return mav;
		}
		Integer socialSecurityNumber;
		if (form.getSocialSecurityNumber() != null
				&& !form.getSocialSecurityNumber().isEmpty()) {
			socialSecurityNumber = Integer.valueOf(form
					.getSocialSecurityNumber().replace("-", ""));
		} else {
			socialSecurityNumber = null;
		}
		City birthPlace;
		if (form.getCreateNewBirthPlace() != null
				&& form.getCreateNewBirthPlace()) {
			birthPlace = this.alternativeOffenderIdentityService.createCity(
					form.getBirthPlaceName(),
					form.getBirthState(),
					form.getBirthCountry());
		} else {
			birthPlace = form.getBirthPlace();
		}
		this.alternativeOffenderIdentityService.associate(offender, 
				form.getAlternativeNameAssociation(), form.getBirthDate(), 
				form.getBirthCountry(), birthPlace, socialSecurityNumber, 
				form.getStateIdNumber(), form.getSex(), 
						new DateRange(form.getStartDate(), form.getEndDate()), 
				form.getCategory());
		return prepareAlternativeIdentityListRedirectMav(offender);
	}
	
	/**
	 * Updates an existing alternative identity.
	 * 
	 * @param alternativeIdentityAssociation association of alternative identity
	 * to update
	 * @param alternativeOffenderIdentityForm alternative offender identity form 
	 * @param result binding result
	 * @return redirect to list alternative identities
	 * @throws DuplicateEntityFoundException 
	 * @throws NumberFormatException 
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_IDENTITY_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "alternativeIdentityAssociation")
				final AlternativeIdentityAssociation
					alternativeIdentityAssociation,
			final AlternativeOffenderIdentityForm form,
			final BindingResult result)
					throws CityExistsException,
						AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException {
		this.alternativeOffenderIdentityFormValidator.validate(
				form, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(
					(Offender)
					alternativeIdentityAssociation.getIdentity().getPerson(),
					form, result);
			mav.addObject(ALTERNATIVE_IDENTITY_ASSOCIATION_MODEL_KEY,
					alternativeIdentityAssociation);
			return mav;
		}
		City birthPlace;
		if (form.getCreateNewBirthPlace() != null
				&& form.getCreateNewBirthPlace()) {
			birthPlace = this.alternativeOffenderIdentityService.createCity(
					form.getBirthPlaceName(),
					form.getBirthState(),
					form.getBirthCountry());
		} else {
			birthPlace = form.getBirthPlace();
		}
		Integer socialSecurityNumber;
		if (form.getSocialSecurityNumber() != null
				&& !form.getSocialSecurityNumber().isEmpty()) {
			socialSecurityNumber = Integer.valueOf(form
					.getSocialSecurityNumber().replace("-", ""));
			this.alternativeOffenderIdentityService.updateAssociation(
					alternativeIdentityAssociation, 
					form.getAlternativeNameAssociation(), form.getBirthDate(), 
					form.getBirthCountry(), birthPlace, socialSecurityNumber, 
					form.getStateIdNumber(), form.getSex(), 
					new DateRange(form.getStartDate(), form.getEndDate()), 
					form.getCategory());
		} else {
			socialSecurityNumber = null;
			this.alternativeOffenderIdentityService.updateAssociationWithoutSsn(
					alternativeIdentityAssociation, 
					form.getAlternativeNameAssociation(), form.getBirthDate(), 
					form.getBirthCountry(), birthPlace, form.getStateIdNumber(),
					form.getSex(), new DateRange(form.getStartDate(), 
							form.getEndDate()), 
					form.getCategory());
		}		
		
		return prepareAlternativeIdentityListRedirectMav(
				(Offender)
					alternativeIdentityAssociation.getIdentity().getPerson());
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplayMav(
			final Offender offender,
			final AlternativeOffenderIdentityForm alternativePersonIdentityForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(offender,
				alternativePersonIdentityForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ ALTERNATIVE_OFFENDER_IDENTITY_FORM_MODEL_KEY, result);
		return mav;
	}
	
	// Returns a redirect to alternative identity list screen
	private ModelAndView prepareAlternativeIdentityListRedirectMav(
			final Offender offender) {
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	// Adds offender summary to a model and view
	private void addOffenderSummary(
			final ModelAndView mav, final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	/**
	 * Removes an alternative offender identity.
	 * 
	 * @param alternativeIdentityAssociation association of alternative
	 * identity to remove
	 * @return redirect to list alternative identities
	 */
	@PreAuthorize("hasRole('OFFENDER_ALT_IDENTITY_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "alternativeIdentityAssociation",
				required = true)
			final AlternativeIdentityAssociation
				alternativeIdentityAssociation) {
		Offender offender = (Offender) alternativeIdentityAssociation
				.getIdentity().getPerson();
		this.alternativeOffenderIdentityService.remove(
				alternativeIdentityAssociation);
		return prepareAlternativeIdentityListRedirectMav(offender);
	}
	
	/* AJAX invokable methods. */
	
	/**
	 * Returns options for States by country.
	 * 
	 * @param country country
	 * @return options for States by country
	 */
	@RequestMapping(value ="/findStatesByCountry.html",
			method = RequestMethod.GET)
	public ModelAndView findStatesByCountry(
			@RequestParam(value = "country", required = true)
				final Country country) {
		List<State> states = this.alternativeOffenderIdentityService
				.findStatesByCountry(country);
		ModelAndView mav = new ModelAndView(STATE_OPTIONS_VIEW_NAME);
		mav.addObject(STATES_MODEL_KEY, states);
		return mav;
	}
	
	/**
	 * Returns options for cities by country.
	 *  
	 * @param country country for which to return cities
	 * @return options for cities by country
	 */
	@RequestMapping(value = "/findCitiesByCountry.html",
			method = RequestMethod.GET)
	public ModelAndView findCitiesByCountry(
			@RequestParam(value = "country", required = true)
				final Country country) {
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		List<City> cities;
		if (this.alternativeOffenderIdentityService.hasStates(country)) {
			cities = this.alternativeOffenderIdentityService
					.findCitiesByCountryWithoutState(country);
		} else {
			cities = this.alternativeOffenderIdentityService
					.findCitiesByCountry(country);
					
		}
		mav.addObject(CITIES_MODEL_KEY, cities);
		return mav;
	}
	
	/**
	 * Returns options for cities by State. 
	 * 
	 * @param state State
	 * @return options for cities by State
	 */
	@RequestMapping(value = "/findCitiesByState.html",
			method = RequestMethod.GET)
	public ModelAndView findCitiesByState(
			@RequestParam(value = "state", required = true)
				final State state) {
		List<City> cities = this.alternativeOffenderIdentityService
				.findCitiesByState(state);
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		mav.addObject(CITIES_MODEL_KEY, cities);
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Returns action menu for alternative identity.
	 * 
	 * @param offender offender
	 * @return action menu for alternative identity
	 */
	@RequestMapping(
			value = "/alternativeIdentityActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showAlternativeIdentityActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				ALTERNATIVE_IDENTITY_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns action menu for alternative identities.
	 * 
	 * @param offender offender
	 * @return action menu for alternative identities
	 */
	@RequestMapping(
			value = "/alternativeIdentitiesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showAlternativeIdentitiesActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				ALTERNATIVE_IDENTITIES_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns action menu for alternative identity row.
	 * 
	 * @param alternativeIdentityAssociation alternative identity association
	 * of row
	 * @return action menu for alternative identity row
	 */
	@RequestMapping(value = "/alternativeIdentityRowActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView alternativeIdentityRowActionMenu(
			@RequestParam(value = "alternativeIdentityAssociation", 
			required = true) 
			final AlternativeIdentityAssociation 
				alternativeIdentityAssociation) {
		ModelMap map = new ModelMap();
		map.addAttribute("alternativeIdentityAssociation", 
				alternativeIdentityAssociation);
		return new ModelAndView(
				"offender/identity/alternative/includes"
				+ "/alternativeIdentityRowActionMenu", map);
	}
	
	/**
	 * Returns the report for the specified offenders alternative identities.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altIdentityListingFullReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('OFFENDER_ALT_IDENTITY_VIEW') and "
			+ "hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')")
	public ResponseEntity<byte []> reporAltIdentityListingFull(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_IDENTITY_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				ALT_IDENTITY_LISTING_FULL_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders alternative identities 
	 * redacted.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altIdentityListingRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAltIdentityListingRedacted(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_IDENTITY_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				ALT_IDENTITY_LISTING_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified alternative identity association.
	 * 
	 * @param alternativeIdentityAssociation alternative identity association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altIdentityDetailsFullReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('OFFENDER_ALT_IDENTITY_VIEW') or "
			+ "hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAltIdentityDetailsFull(@RequestParam(
			value = "alternativeIdentityAssociation", required = true)
			final AlternativeIdentityAssociation alternativeIdentityAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(alternativeIdentityAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				ALT_IDENTITY_DETAILS_FULL_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/**
	 * Returns the report for the specified alternative identity association.
	 * 
	 * @param alternativeIdentityAssociation alternative identity association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/altIdentityDetailsRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_ALT_IDENTITY_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAltIdentityDetailsRedacted(
			@RequestParam(value = "alternativeIdentityAssociation", 
				required = true)
			final AlternativeIdentityAssociation alternativeIdentityAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ALT_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(alternativeIdentityAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				ALT_IDENTITY_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/* Exception handlers. */
	
	/**
	 * Handles {@code PersonIdentityExistsException}.
	 * 
	 * @param personIdentityExistsException exception
	 * @return screen to handle {@code PersonIdentityExistsException}
	 */
	@ExceptionHandler(PersonIdentityExistsException.class)
	public ModelAndView handlePersonIdentityExistsException(
			final PersonIdentityExistsException personIdentityExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PERSON_IDENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				personIdentityExistsException);
	}
	
	/**
	 * Handles {@code AlternativeIdentityAssociationExistsException}.
	 * 
	 * @param alternativeIdentityAssociationExistsException exception
	 * @return screen to handle
	 * {@code AlternativeIdentityAssociationExistsException}
	 */
	@ExceptionHandler(AlternativeIdentityAssociationExistsException.class)
	public ModelAndView handleAlternativeIdentityAssociationExistsException(
			final AlternativeIdentityAssociationExistsException
				alternativeIdentityAssociationExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ALTERNATIVE_IDENTITY_ASSOCIATION_EXISTS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME,
				alternativeIdentityAssociationExistsException);
	}
	
	/**
	 * Handles {@code CityExistsException}.
	 * 
	 * @param cityExistsException exception
	 * @return screen to handle {@code CityExistsException}
	 */
	@ExceptionHandler(CityExistsException.class)
	public ModelAndView handleCityExistsException(
			final CityExistsException cityExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				cityExistsException);
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
		binder.registerCustomEditor(AlternativeIdentityAssociation.class,
				this.alternativeIdentityAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AlternativeIdentityCategory.class,
				this.alternativeIdentityCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AlternativeNameAssociation.class, 
				this.alternativeNameAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Sex.class,
				this.sexPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}