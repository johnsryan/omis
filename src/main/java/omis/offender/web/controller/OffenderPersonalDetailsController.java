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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderPersonalDetailsService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offender.web.form.OffenderPersonalDetailsForm;
import omis.offender.web.validator.OffenderPersonalDetailsFormValidator;
import omis.person.domain.Suffix;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for offender personal details.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.1 (Sept 30, 2014)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offender/personalDetails")
public class OffenderPersonalDetailsController {

	/* View names. */

	private static final String EDIT_VIEW_NAME
		= "offender/personalDetails/edit";
	
	private static final String ACTION_MENU_VIEW_NAME
		= "offender/personalDetails/includes/personalDetailsActionMenu";
	
	private static final String STATE_OPTIONS_VIEW_NAME
		= "region/includes/stateOptions";
	
	private static final String CITY_OPTIONS_VIEW_NAME
		= "region/includes/cityOptions";
	
	/* Redirects. */
	
	private static final String PROFILE_REDIRECT
		= "redirect:/offender/profile.html?offender=%d";
	
	/* Model keys. */
	
	private static final String OFFENDER_PERSONAL_DETAILS_FORM_MODEL_KEY
		= "offenderPersonalDetailsForm";

	private static final String SEXES_MODEL_KEY = "sexes";

	private static final String COUNTRIES_MODEL_KEY = "countries";

	private static final String STATES_MODEL_KEY = "states";

	private static final String DEFAULT_BIRTH_STATE_MODEL_KEY
		= "defaultBirthState";

	private static final String CITIES_MODEL_KEY = "cities";

	private static final String SUFFIXES_MODEL_KEY = "suffixes";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	/* Errors messages. */
	
	private static final String BIRTH_CITY_EXISTS_MESSAGE_KEY
		= "birthCity.exists";
	
	private static final String PERSON_NAME_EXISTS_MESSAGE_KEY
		= "personName.exists";

	private static final String PERSON_IDENTITY_EXISTS_MESSAGE_KEY
		= "personIdentity.exists";

	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.offender.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("offenderPersonalDetailsService")
	private OffenderPersonalDetailsService offenderPersonalDetailsService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
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
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("offenderPersonalDetailsFormValidator")
	private OffenderPersonalDetailsFormValidator
		offenderPersonalDetailsFormValidator;

	/* Report names. */

	private static final String LEGAL_NAME_AND_IDENTITY_DETAILS_FULL_REPORT_NAME 
		= "/BasicInformation/LegalNameandIdentity/Legal_Name_and_Identity";

	private static final String 
		LEGAL_NAME_AND_IDENTITY_DETAILS_REDACTED_REPORT_NAME 
		= "/BasicInformation/LegalNameandIdentity/Legal_Name_and_Identity_Redacted";

	/* Report parameter names. */

	private static final String 
		LEGAL_NAME_AND_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME = "DOC_ID";

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
	
	/** Instantiates a controller for offender personal details. */
	public OffenderPersonalDetailsController() {
		// Default instantiation
	}
	
	/* Web screen requests. */
	
	/**
	 * Displays a form allowing the editing of offender person details.
	 * 
	 * @param offender offender whose personal details to edit
	 * @return form allowing editing of person
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		OffenderPersonalDetailsForm identityForm
				= new OffenderPersonalDetailsForm();
		identityForm.setFirstName(offender.getName().getFirstName());
		identityForm.setLastName(offender.getName().getLastName());
		identityForm.setMiddleName(offender.getName().getMiddleName());
		identityForm.setSuffix(offender.getName().getSuffix());
		if (offender.getIdentity() != null) {
			identityForm.setBirthCountry(
					offender.getIdentity().getBirthCountry());
			identityForm.setBirthDate(offender.getIdentity().getBirthDate());
			identityForm.setBirthPlace(offender.getIdentity().getBirthPlace());
			identityForm.setStateIdNumber(
					offender.getIdentity().getStateIdNumber());
			identityForm.setBirthState(offender.getIdentity().getBirthState());
			identityForm.setSex(offender.getIdentity().getSex());
			if (offender.getIdentity().getSocialSecurityNumber() != null) {	
				if (hasRole("ADMIN") || hasRole("OFFENDER_SSN_EDIT")
					|| hasRole("OFFENDER_SSN_VIEW")) {
					String stringSSN = String.format("%09d",
							offender.getIdentity().getSocialSecurityNumber());
					if (stringSSN.length() != 9) {
						throw new IllegalStateException(
								"Invalid Social Security Number");
					} else {
						String formattedSSN = String
								.format("%s-%s-%s",
										stringSSN.substring(0,3),
										stringSSN.substring(3,5),
										stringSSN.substring(5,9));
						identityForm.setSocialSecurityNumber(
								formattedSSN);
					}
					identityForm.setValidateSocialSecurityNumber(true);
				} else {
					String socialSecurityNumber
						= offender.getIdentity().getSocialSecurityNumber()
							.toString(); 
					if (socialSecurityNumber.length() >= 4) {
						socialSecurityNumber = "XXX-XX-"
								+ socialSecurityNumber.substring(
										socialSecurityNumber.length() - 4,
										socialSecurityNumber.length());
					} else {
						socialSecurityNumber = "XXX-XX-"
								+ socialSecurityNumber;
					}
					identityForm.setSocialSecurityNumber(
							socialSecurityNumber);
					identityForm.setValidateSocialSecurityNumber(false);
				}
			}
			identityForm.setCreateNewBirthPlace(false);
			identityForm.setDeceased(offender.getIdentity().getDeceased());
			identityForm.setDeathDate(offender.getIdentity().getDeathDate());
		}
		ModelAndView mav = this.prepareMav(identityForm, offender);
		return mav;
	}
	
	
	
	/**
	 * Updates offender personal details.
	 * 
	 * @param offender offender whose personal details to update
	 * @param offenderPersonalDetailsForm form for offender personal details
	 * @param result validation results
	 * @return redirect to profile or redisplay of submitted form if
	 * validation fails
	 * @throws CityExistsException if city exists
	 * @throws PersonIdentityExistsException if identity exists for person
	 * @throws PersonNameExistsException if name exists for person
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('OFFENDER_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			final OffenderPersonalDetailsForm offenderPersonalDetailsForm,
			final BindingResult result)
					throws CityExistsException,
						PersonIdentityExistsException,
						PersonNameExistsException {
		this.offenderPersonalDetailsFormValidator.validate(
				offenderPersonalDetailsForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareMav(offenderPersonalDetailsForm,
					offender);
			mav.addObject(
					BindingResult.MODEL_KEY_PREFIX
						+ OFFENDER_PERSONAL_DETAILS_FORM_MODEL_KEY,
					result);
			return mav;
		}
		City birthPlace;
		if (offenderPersonalDetailsForm.getCreateNewBirthPlace() != null
				&& offenderPersonalDetailsForm.getCreateNewBirthPlace()) {
			birthPlace = this.offenderPersonalDetailsService.createCity(
					offenderPersonalDetailsForm.getBirthPlaceName(),
					offenderPersonalDetailsForm.getBirthState(),
					offenderPersonalDetailsForm.getBirthCountry());
		} else {
			birthPlace = offenderPersonalDetailsForm.getBirthPlace();
		}
		if (hasRole("ADMIN") || hasRole("OFFENDER_SSN_EDIT")
				|| hasRole("OFFENDER_SSN_VIEW")) {
			Integer socialSecurityNumber;
			if (offenderPersonalDetailsForm.getSocialSecurityNumber() != null
					&& !"".equals(offenderPersonalDetailsForm
							.getSocialSecurityNumber())) {
				socialSecurityNumber = Integer.valueOf(
						offenderPersonalDetailsForm.getSocialSecurityNumber()
							.replaceAll("-", ""));
			} else {
				socialSecurityNumber = null;
			}
			this.offenderPersonalDetailsService.changeIdentity(
					offender, offenderPersonalDetailsForm.getBirthDate(), 
					offenderPersonalDetailsForm.getBirthCountry(),
					offenderPersonalDetailsForm.getBirthState(),
					birthPlace, socialSecurityNumber,
					offenderPersonalDetailsForm.getStateIdNumber(), 
					offenderPersonalDetailsForm.getSex(),
					offenderPersonalDetailsForm.getDeceased(),
					offenderPersonalDetailsForm.getDeathDate());
		} else {
			this.offenderPersonalDetailsService.changeIdentityWithoutSsn(
					offender, offenderPersonalDetailsForm.getBirthDate(), 
					offenderPersonalDetailsForm.getBirthCountry(),
					offenderPersonalDetailsForm.getBirthState(),
					birthPlace,
					offenderPersonalDetailsForm.getStateIdNumber(), 
					offenderPersonalDetailsForm.getSex(),
					offenderPersonalDetailsForm.getDeceased(),
					offenderPersonalDetailsForm.getDeathDate());
		}
		this.offenderPersonalDetailsService.updateName(offender, 
				offenderPersonalDetailsForm.getLastName(), 
				offenderPersonalDetailsForm.getFirstName(), 
				offenderPersonalDetailsForm.getMiddleName(), 
				offenderPersonalDetailsForm.getSuffix());
		return new ModelAndView(String.format(PROFILE_REDIRECT, 
				offender.getId()));
	}
	
	/* AJAX invokable methods. */
	
	/**
	 * Returns options for States by country.
	 * 
	 * @param country country
	 * @return options for States by country
	 */
	@RequestMapping(value = "/findStatesByCountry.html",
			method = RequestMethod.GET)
	public ModelAndView findStatesByCountry(
			@RequestParam(value = "country", required = true)
				final Country country) {
		List<State> states = this.offenderPersonalDetailsService
				.findStatesByCountry(country);
		ModelAndView mav = new ModelAndView(STATE_OPTIONS_VIEW_NAME);
		mav.addObject(STATES_MODEL_KEY, states);
		return mav;
	}
	
	/**
	 * Returns options for cities by country.
	 * 
	 * <p>If country has States, return only cities for the country that does
	 * not have the State property set (like DC in the USA). Otherwise, return
	 * all cities for the country.
	 * 
	 * @param country country for which to return cities
	 * @return options for cities by country
	 */
	@RequestMapping(value = "/findCitiesByCountry.html",
			method = RequestMethod.GET)
	public ModelAndView findCititesByCountry(
			@RequestParam(value = "country", required = true)
				final Country country) {
		List<City> cities;
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		if (this.offenderPersonalDetailsService.hasStates(country)) {
			cities = this.offenderPersonalDetailsService
						.findCitiesByCountryWithoutState(country);
		} else {
			cities = this.offenderPersonalDetailsService
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
		List<City> cities = this.offenderPersonalDetailsService
				.findCitiesByState(state);
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		mav.addObject(CITIES_MODEL_KEY, cities);
		return mav;
	}
	
	/**
	 * Returns the report for the specified offenders legal name and identity.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/legalNameAndIdentityDetailsFullReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('OFFENDER_VIEW') and hasRole('OFFENDER_SSN_VIEW')) "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportLegalNameAndIdentityDetailsFull(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(LEGAL_NAME_AND_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				LEGAL_NAME_AND_IDENTITY_DETAILS_FULL_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/**
	 * Returns the report for the specified offenders legal name and identity 
	 * redacted.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/legalNameAndIdentityDetailsRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportLegalNameAndIdentityDetailsRedacted(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(LEGAL_NAME_AND_IDENTITY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				LEGAL_NAME_AND_IDENTITY_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/* Action menus. */
	
	/**
	 * Returns action menu.
	 * 
	 * @param offender offender
	 * @return action menu
	 */
	@RequestMapping(value = "/actionMenu.html", method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/* Exception handlers. */
	
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
				BIRTH_CITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				cityExistsException);
	}
	
	/**
	 * Handles {@code PersonNameExistsException}.
	 * 
	 * @param personNameExistsException exception
	 * @return screen to handle {@code PersonNameExistsException}
	 */
	@ExceptionHandler(PersonNameExistsException.class)
	public ModelAndView handlePersonNameExistsException(
			final PersonNameExistsException personNameExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PERSON_NAME_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				personNameExistsException);
	}
	
	/**
	 * Handles {@code PersonIdentityExistsException}.
	 * 
	 * @param personIdentityExists exception
	 * @return screen to handle {@code PersonIdentityExistsException}
	 */
	@ExceptionHandler(PersonIdentityExistsException.class)
	public ModelAndView handlePersonIdentityExistsException(
			final PersonIdentityExistsException personIdentityExists) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PERSON_IDENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				personIdentityExists);
	}
	
	/* Helper methods. */
	
	// Prepares edit model and view
	private ModelAndView prepareMav(
			final OffenderPersonalDetailsForm offenderPersonalDetailsForm,
			final Offender offender) {
		if (hasRole("SSN_EDIT") || hasRole("ADMIN")) {
			offenderPersonalDetailsForm.setValidateSocialSecurityNumber(true);
		}
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(OFFENDER_PERSONAL_DETAILS_FORM_MODEL_KEY,
				offenderPersonalDetailsForm);
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		List<Country> countries = 
				this.offenderPersonalDetailsService.findCountries();
		mav.addObject(COUNTRIES_MODEL_KEY, countries);
		if (offenderPersonalDetailsForm.getBirthCountry() != null) {
			List<State> states = 
					this.offenderPersonalDetailsService.findStatesByCountry(
							offenderPersonalDetailsForm.getBirthCountry());
			mav.addObject(STATES_MODEL_KEY, states);
		}
		List<City> cities;
		if (offenderPersonalDetailsForm.getBirthState() != null) {
			cities = this.offenderPersonalDetailsService
					.findCitiesByState(offenderPersonalDetailsForm
						.getBirthState());
			mav.addObject(DEFAULT_BIRTH_STATE_MODEL_KEY,
					offenderPersonalDetailsForm.getBirthState());
		} else if (offenderPersonalDetailsForm.getBirthCountry() != null) {
			if (!this.offenderPersonalDetailsService.hasStates(
					offenderPersonalDetailsForm.getBirthCountry())) {
				cities = this.offenderPersonalDetailsService
						.findCitiesByCountry(
								offenderPersonalDetailsForm.getBirthCountry());
			} else {
				cities = this.offenderPersonalDetailsService
						.findCitiesByCountryWithoutState(
								offenderPersonalDetailsForm.getBirthCountry());
			}
		} else {
			cities = Collections.emptyList();
		}
		mav.addObject(CITIES_MODEL_KEY, cities);
		List<Suffix> suffixes = this.offenderPersonalDetailsService
				.findSuffixes();
		mav.addObject(SUFFIXES_MODEL_KEY, suffixes);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.addOffenderSummary(mav, offender);
		return mav;
	}
	
	// Adds offender summary to a model and view
	private void addOffenderSummary(
			final ModelAndView mav, final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	// Returns whether the current user has the specified role
	private boolean hasRole(final String role) {
		for (GrantedAuthority authority :
				SecurityContextHolder.getContext().getAuthentication()
					.getAuthorities()) {
			if (role.equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
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
		binder.registerCustomEditor(Sex.class,
				this.sexPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class, 
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "birthDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "deathDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}