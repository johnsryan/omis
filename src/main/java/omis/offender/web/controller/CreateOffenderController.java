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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.Height;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.Race;
import omis.demographics.domain.Sex;
import omis.demographics.domain.Tribe;
import omis.demographics.domain.Weight;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.exception.BusinessException;
import omis.exception.DuplicateEntityFoundException;
import omis.io.FilenameGenerator;
import omis.media.io.PhotoPersister;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.CreateOffenderService;
import omis.offender.web.form.AlienResidenceLegality;
import omis.offender.web.form.CreateOffenderFlagItem;
import omis.offender.web.form.CreateOffenderFlagItemValue;
import omis.offender.web.form.CreateOffenderForm;
import omis.offender.web.validator.CreateOffenderFormValidator;
import omis.offenderflag.domain.OffenderFlagCategory;
import omis.offenderphoto.domain.OffenderPhotoAssociation;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.religion.domain.Religion;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller to create a new offender.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.4 (March 27, 2013)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/offender")
@PreAuthorize("hasRole('USER')")
public class CreateOffenderController {
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "offender/edit";

	private static final String STATE_OPTIONS_VIEW_NAME
		= "region/includes/stateOptions";

	private static final String CITY_OPTIONS_VIEW_NAME
		= "region/includes/cityOptions";
	
	private static final String BOOLEAN_VIEW_NAME = "common/json/booleanValue";
	
	/* Redirects. */
	
	private static final String PROFILE_REDIRECT
		= "redirect:/offender/profile.html?offender=%d";
	
	/* Model keys. */

	private static final String BUILDS_MODEL_KEY = "builds";

	private static final String COMPLEXIONS_MODEL_KEY = "complexions";

	private static final String EYE_COLORS_MODEL_KEY = "eyeColors";

	private static final String HAIR_COLOR_MODEL_KEY = "hairColors";
	
	private static final String MARITAL_STATUSES_MODEL_KEY = "maritalStatuses";

	private static final String COUNTRIES_MODEL_KEY = "countries";

	private static final String SUFFIXES_MODEL_KEY = "suffixes";

	private static final String SEXES_MODEL_KEY = "sexes";

	private static final String DOMINANT_SIDES_MODEL_KEY = "dominantSides";

	private static final String RELIGIONS_MODEL_KEY = "religions";
	
	private static final String RACES_MODEL_KEY = "races";
	
	private static final String TRIBES_MODEL_KEY = "tribes";

	private static final String CREATE_OFFENDER_FORM_MODEL_KEY
		= "createOffenderForm";
	
	private static final String STATES_MODEL_KEY = "states";
	
	private static final String CITIES_MODEL_KEY = "cities";
	
	private static final String BOOLEAN_VALUE_MODEL_KEY = "booleanValue";
	
	private static final String HOME_COUNTRY_CITIZEN_MODEL_KEY
		= "homeCountryCitizen";

	/* Errors messages. */
	
	private static final String DUPLICATE_ENTITY_FOUND_MESSAGE_KEY
		= "birthCity.exists";

	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.offender.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("createOffenderService")
	private CreateOffenderService createOffenderService;
	
	@Autowired
	@Qualifier("offenderPhotoPersister")
	private PhotoPersister offenderPhotoPersister;
	
	@Autowired
	@Qualifier("photoFilenameGenerator")
	private FilenameGenerator photoFilenameGenerator;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("sexPropertyEditorFactory")
	private PropertyEditorFactory sexPropertyEditorFactory;
	
	@Autowired
	@Qualifier("buildPropertyEditorFactory")
	private PropertyEditorFactory buildPropertyEditorFactory;
	
	@Autowired
	@Qualifier("complexionPropertyEditorFactory")
	private PropertyEditorFactory complexionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("eyeColorPropertyEditorFactory")
	private PropertyEditorFactory eyeColorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hairColorPropertyEditorFactory")
	private PropertyEditorFactory hairColorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("maritalStatusPropertyEditorFactory")
	private PropertyEditorFactory maritalStatusPropertyEditorFactory;

	@Autowired
	@Qualifier("racePropertyEditorFactory")
	private PropertyEditorFactory racePropertyEditorFactory;
	
	@Autowired
	@Qualifier("tribePropertyEditorFactory")
	private PropertyEditorFactory tribePropertyEditorFactory;
	
	@Autowired
	@Qualifier("dominantSidePropertyEditorFactory")
	private PropertyEditorFactory dominantSidePropertyEditorFactory;
	
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("religionPropertyEditorFactory")
	private PropertyEditorFactory religionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderFlagCategoryPropertyEditorFactory")
	private PropertyEditorFactory offenderFlagCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("createOffenderFormValidator")
	private CreateOffenderFormValidator createOffenderFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Constructor. */
	
	/** Instantiates a default controller for offenders. */
	public CreateOffenderController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays a form to capture new offender information.
	 * 
	 * @param person optional person to convert
	 * @return model and view to form to capture offender information
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "person", required = false)
				final Person person) {
		CreateOffenderForm createOffenderForm = new CreateOffenderForm();
		if (person != null) {
			createOffenderForm.setLastName(person.getName().getLastName());
			createOffenderForm.setFirstName(person.getName().getFirstName());
			createOffenderForm.setMiddleName(person.getName().getMiddleName());
			createOffenderForm.setSuffix(person.getName().getSuffix());
			if (person.getIdentity() != null) {
				createOffenderForm.setBirthDate(
						person.getIdentity().getBirthDate());
				createOffenderForm.setBirthState(
						person.getIdentity().getBirthState());
				createOffenderForm.setBirthCountry(
						person.getIdentity().getBirthCountry());
				createOffenderForm.setBirthPlace(
						person.getIdentity().getBirthPlace());
				createOffenderForm.setSocialSecurityNumber(
						person.getIdentity().getSocialSecurityNumber()
							.toString());
				createOffenderForm.setStateIdNumber(
						person.getIdentity().getStateIdNumber());
				createOffenderForm.setSex(person.getIdentity().getSex());
			}
		}
		List<OffenderFlagCategory> categories
			= this.createOffenderService.findRequiredCategories();
		for (OffenderFlagCategory category : categories) {
			CreateOffenderFlagItem categoryItem
				= new CreateOffenderFlagItem();
			categoryItem.setCategory(category);
			createOffenderForm.getFlagItems().add(categoryItem);
		}
		State homeState = this.createOffenderService.findHomeState();
		createOffenderForm.setBirthState(homeState);
		createOffenderForm.setBirthCountry(homeState.getCountry());
		createOffenderForm.setCreateNewBirthPlace(false);
		return prepareEditMav(createOffenderForm);
	}
	
	/**
	 * Creates and saves a new offender.
	 * 
	 * @param person optional person to convert
	 * @param createOffenderForm form capturing offender information
	 * @param result binding result
	 * @return redirect to profile screen
	 * @throws BusinessException if a business rule is violated
	 */
	@PreAuthorize("hasRole('OFFENDER_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "person", required = false)
				final Person person,
			final CreateOffenderForm createOffenderForm,
			final BindingResult result)
				throws BusinessException {
		this.createOffenderFormValidator.validate(createOffenderForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(createOffenderForm, result);
		}
		Integer socialSecurityNumber;
		if (createOffenderForm.getSocialSecurityNumber() != null
				&& !"".equals(createOffenderForm.getSocialSecurityNumber())) {
			socialSecurityNumber = Integer.valueOf(createOffenderForm
					.getSocialSecurityNumber().replaceAll("-", ""));
		} else {
			socialSecurityNumber = null;
		}
		City birthPlace;
		if (createOffenderForm.getCreateNewBirthPlace() != null
				&& createOffenderForm.getCreateNewBirthPlace()) {
			birthPlace = this.createOffenderService.createCity(
					createOffenderForm.getBirthPlaceName(),
					createOffenderForm.getBirthState(),
					createOffenderForm.getBirthCountry());
		} else {
			birthPlace = createOffenderForm.getBirthPlace();
		}
		Offender offender;
		if (person != null) {
			offender = this.createOffenderService.convertPerson(
					person,
					createOffenderForm.getLastName(), 
					createOffenderForm.getFirstName(), 
					createOffenderForm.getMiddleName(), 
					createOffenderForm.getSuffix(), 
					socialSecurityNumber, 
					createOffenderForm.getStateIdNumber(), 
					createOffenderForm.getBirthDate(), 
					createOffenderForm.getBirthCountry(), 
					birthPlace, 
					createOffenderForm.getSex());
		} else {
			offender = this.createOffenderService.create(
				createOffenderForm.getLastName(), 
				createOffenderForm.getFirstName(), 
				createOffenderForm.getMiddleName(), 
				createOffenderForm.getSuffix(), 
				socialSecurityNumber, 
				createOffenderForm.getStateIdNumber(), 
				createOffenderForm.getBirthDate(), 
				createOffenderForm.getBirthCountry(), 
				birthPlace, 
				createOffenderForm.getSex());
		}
		this.createOffenderService.addDemographics(offender, 
			new PersonPhysique(
				new Height(createOffenderForm.getHeightFeet(),
						createOffenderForm.getHeightInches()),
				new Weight(createOffenderForm.getWeightPounds()),
				createOffenderForm.getBuild()), 
			new PersonAppearance(createOffenderForm.getEyeColor(),
					createOffenderForm.getHairColor(),
					createOffenderForm.getComplexion()),
					createOffenderForm.getDominantSide(),
					createOffenderForm.getRace(),
					createOffenderForm.getHispanicEthnicity(),
					createOffenderForm.getTribe(),
					createOffenderForm.getMaritalStatus());
		if (createOffenderForm.getReligion() != null) {
			this.createOffenderService.addReligiousPreference(offender, 
					createOffenderForm.getReligion());
		}
		if (createOffenderForm.getCountryOfCitizenship() != null) {
			this.createOffenderService.setCountryOfCitizenship(offender, 
					createOffenderForm.getCountryOfCitizenship());
		}
		if (createOffenderForm.getAlienResidenceLegality() != null) {
			if (createOffenderForm.getCountryOfCitizenship() != null) {
				if (this.createOffenderService.isHomeCountry(
								createOffenderForm.getCountryOfCitizenship())) {
					throw new IllegalArgumentException(
							"Immigration status not allowed for citizen");
				}
			}
			Boolean alienResidenceLegality;
			if (AlienResidenceLegality.LEGAL
					.equals(createOffenderForm.getAlienResidenceLegality())) {
				alienResidenceLegality = true;
			} else if (AlienResidenceLegality.ILLEGAL
					.equals(createOffenderForm.getAlienResidenceLegality())) {
				alienResidenceLegality = false;
			} else {
				alienResidenceLegality = null;
			}
			this.createOffenderService.setLegalResidenceStatus(offender, 
					alienResidenceLegality);
		}
		for (CreateOffenderFlagItem flagItem
					: createOffenderForm.getFlagItems()) {
			Boolean value;
			if (CreateOffenderFlagItemValue.YES
					.equals(flagItem.getValue())) {
				value = true;
			} else if (CreateOffenderFlagItemValue.NO
					.equals(flagItem.getValue())) {
				value = false;
			} else if (CreateOffenderFlagItemValue.UNKNOWN
					.equals(flagItem.getValue())) {
				value = null;
			} else {
				throw new IllegalArgumentException("Unknown or null value: "
					 + flagItem.getValue());
			}
			this.createOffenderService.setFlag(offender, flagItem.getCategory(), 
					value);
		}
		if (this.createOffenderService.findRequiredCategories().size() > 0) {
			if (!this.createOffenderService.hasRequiredFlags(offender)) {
				throw new BusinessException("Required flags missing");
			}
		}
		if (createOffenderForm.getPhotoData() != null
				&& createOffenderForm.getPhotoData().length > 0
				&& createOffenderForm.getPhotoDate() != null) {
			String filename = this.photoFilenameGenerator.generate();
			OffenderPhotoAssociation association = 
					this.createOffenderService.associateProfilePhoto(offender, 
							filename, createOffenderForm.getPhotoDate());
			this.offenderPhotoPersister.persist(
					association.getPhoto(), createOffenderForm.getPhotoData());
		} else if ((createOffenderForm.getPhotoData() != null
					&& createOffenderForm.getPhotoData().length > 0)
				|| createOffenderForm.getPhotoDate() != null) {
			throw new BusinessException("Both photo and date required");
		}
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
		List<State> states = this.createOffenderService
				.findStatesByCountry(country);
		ModelAndView mav = new ModelAndView(STATE_OPTIONS_VIEW_NAME);
		mav.addObject(STATES_MODEL_KEY, states);
		return mav;
	}
	
	/**
	 * Returns options for cities by country.
	 * 
	 * <p>By default, if the country has States, no cities will be returned.
	 * To return cities if the country has States, {@code allowIfHasStates}
	 * must be set to {@code true}.
	 * 
	 * @param country country for which to return cities
	 * @param allowIfHasStates whether to return cities when country has States
	 * @return options for cities by country
	 */
	@RequestMapping(value = "/findCitiesByCountry.html",
			method = RequestMethod.GET) 
	public ModelAndView findCitiesByCountry(
			@RequestParam(value = "country", required = true)
				final Country country,
			@RequestParam(value = "allowIfHasStates", required = false)
				final Boolean allowIfHasStates) {
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		if ((allowIfHasStates != null && allowIfHasStates)
				|| !this.createOffenderService.hasStates(country)) {
			List<City> citites = this.createOffenderService
					.findCitiesByCountry(country);
			mav.addObject(CITIES_MODEL_KEY, citites);
		}
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
		List<City> cities = this.createOffenderService
				.findCitiesByState(state);
		ModelAndView mav = new ModelAndView(CITY_OPTIONS_VIEW_NAME);
		mav.addObject(CITIES_MODEL_KEY, cities);
		return mav;
	}
	
	/* AJAX invokable methods. */
	
	/**
	 * Returns whether country is home country.
	 * 
	 * @param country country
	 * @return whether country is home country
	 */
	@RequestMapping(value = "/isHomeCountry.json", method = RequestMethod.GET)
	public ModelAndView isHomeCountry(
			@RequestParam(value = "country", required = true)
				final Country country) {
		boolean homeCountry = this.createOffenderService
				.isHomeCountry(country);
		ModelAndView mav = new ModelAndView(BOOLEAN_VIEW_NAME);
		mav.addObject(BOOLEAN_VALUE_MODEL_KEY, homeCountry);
		return mav;
	}
	
	/* Exception handlers. */
	
	/**
	 * Handles {@code DuplicateEntityFoundException}.
	 * 
	 * @param duplicateEntityFoundException exception
	 * @return screen to handle {@code DuplicateEntityFoundException}
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException duplicateEntityFoundException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				DUPLICATE_ENTITY_FOUND_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				duplicateEntityFoundException);
	}
	
	/* Helper methods. */
	
	// Prepare model and view to display edit screen
	private ModelAndView prepareEditMav(
			final CreateOffenderForm createOffenderForm) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		if (createOffenderForm.getBirthState() != null) {
			mav.addObject(STATES_MODEL_KEY,
					this.createOffenderService.findStatesByCountry(
							createOffenderForm.getBirthState().getCountry()));
			mav.addObject(CITIES_MODEL_KEY,
					this.createOffenderService.findCitiesByState(
							createOffenderForm.getBirthState()));
		} else if (!this.createOffenderService.hasStates(
				createOffenderForm.getBirthCountry())) {
			mav.addObject(CITIES_MODEL_KEY,
					this.createOffenderService.findCitiesByCountry(
							createOffenderForm.getBirthCountry()));
		}
		mav.addObject(BUILDS_MODEL_KEY,
				this.createOffenderService.findBuilds());
		mav.addObject(COMPLEXIONS_MODEL_KEY, 
				this.createOffenderService.findComplexions());
		mav.addObject(EYE_COLORS_MODEL_KEY,
				this.createOffenderService.findEyeColors());
		mav.addObject(HAIR_COLOR_MODEL_KEY,
				this.createOffenderService.findHairColors());
		mav.addObject(MARITAL_STATUSES_MODEL_KEY,
				this.createOffenderService.findMaritalStatuses());
		mav.addObject(RACES_MODEL_KEY,
				this.createOffenderService.findRaces());
		mav.addObject(TRIBES_MODEL_KEY,
				this.createOffenderService.findTribes());
		mav.addObject(COUNTRIES_MODEL_KEY, this.createOffenderService
				.findCountries());
		mav.addObject(SUFFIXES_MODEL_KEY, this.createOffenderService
				.findSuffixes());
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		mav.addObject(DOMINANT_SIDES_MODEL_KEY, DominantSide.values());
		mav.addObject(RELIGIONS_MODEL_KEY,
				this.createOffenderService.findReligions());
		boolean homeCountryCitizen;
		if (createOffenderForm.getCountryOfCitizenship() != null) {
			homeCountryCitizen = this.createOffenderService.isHomeCountry(
					createOffenderForm.getCountryOfCitizenship());
		} else {
			homeCountryCitizen = false;
		}
		mav.addObject(HOME_COUNTRY_CITIZEN_MODEL_KEY, homeCountryCitizen);
		mav.addObject(CREATE_OFFENDER_FORM_MODEL_KEY, createOffenderForm);
		return mav;
	}
	
	// Prepares model and view to redisplay edit screen
	private ModelAndView prepareRedisplayMav(
			final CreateOffenderForm offenderForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(offenderForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ CREATE_OFFENDER_FORM_MODEL_KEY, result);
		return mav;
	}
	
	/* Init binder. */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Build.class,
				this.buildPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Complexion.class,
				this.complexionPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(EyeColor.class,
				this.eyeColorPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(HairColor.class,
				this.hairColorPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(MaritalStatus.class,
				this.maritalStatusPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Race.class,
				this.racePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Tribe.class,
				this.tribePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OffenderFlagCategory.class,
				this.offenderFlagCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Sex.class,
				this.sexPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DominantSide.class,
				this.dominantSidePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Religion.class,
				this.religionPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
	}
}