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

import omis.beans.factory.PropertyEditorFactory;
import omis.country.domain.Country;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.Height;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.PersonDemographics;
import omis.demographics.domain.Race;
import omis.demographics.domain.Tribe;
import omis.demographics.domain.Weight;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderDemographicsService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offender.web.form.AlienResidenceLegality;
import omis.offender.web.form.OffenderDemographicsForm;
import omis.offender.web.validator.OffenderDemographicsFormValidator;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for offender demographics.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.1 (Oct 2, 2014)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/offender/demographics")
@PreAuthorize("hasRole('USER')")
public class OffenderDemographicsController {

	/* Views. */
	
	private static final String EDIT_VIEW_NAME = "offender/demographics/edit";
	
	private static final String ACTION_MENU_VIEW_NAME
		= "offender/demographics/includes/offenderDemographicsActionMenu";
	
	private static final String BOOLEAN_VIEW_NAME = "common/json/booleanValue";
	
	/* Redirects. */
	
	private static final String OFFENDER_PROFILE_REDIRECT
		= "redirect:/offender/profile.html?offender=%d";
	
	/* Model keys. */
	
	private static final String OFFENDER_DEMOGRAPHICS_FORM_MODEL_KEY
		= "offenderDemographicsForm";
	
	private static final String BUILDS_MODEL_KEY = "builds";

	private static final String COMPLEXIONS_MODEL_KEY = "complexions";

	private static final String EYE_COLORS_MODEL_KEY = "eyeColors";

	private static final String HAIR_COLOR_MODEL_KEY = "hairColors";
	
	private static final String MARITAL_STATUSES_MODEL_KEY = "maritalStatuses";
	
	private static final String DOMINANT_SIDES_MODEL_KEY = "dominantSides";
	
	private static final String RACES_MODEL_KEY = "races";
	
	private static final String TRIBES_MODEL_KEY = "tribes";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String COUNTRIES_MODEL_KEY = "countries";
	
	private static final String BOOLEAN_VALUE_MODEL_KEY = "booleanValue";
	
	private static final String HOME_COUNTRY_CITIZEN_MODEL_KEY
		= "homeCountryCitizen";
	
	private static final String PERSON_DEMOGRAPHICS_MODEL_KEY 
		= "personDemographics";

	/* Services. */
	
	@Autowired
	@Qualifier("offenderDemographicsService")
	private OffenderDemographicsService offenderDemographicsService;
	
	/* Report services. */
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
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
	
	/* Validators. */
	
	@Autowired
	@Qualifier("offenderDemographicsFormValidator")
	private OffenderDemographicsFormValidator offenderDemographicsFormValidator;

	/* Report names. */

	private static final String DEMOGRAPHICS_DETAILS_REPORT_NAME 
		= "/BasicInformation/Demographics/Demographics_Details";

	/* Report parameter names. */

	private static final String DEMOGRAPHICS_DETAILS_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

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
	
	/* Constructor. */
	
	/** Instantiates a controller for offender demographics. */
	public OffenderDemographicsController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows form to update offender demographics.
	 * 
	 * @param offender offender
	 * @return form to update offender demographics
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_DEMOGRAPHICS_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
				@RequestParam(value = "offender", required = true)
			final Offender offender) {
		OffenderDemographicsForm offenderDemographicsForm =
				new OffenderDemographicsForm();
		PersonDemographics  personDemographics
			= this.offenderDemographicsService.findDemographics(offender);
		if ( personDemographics != null) {
			if (personDemographics.getAppearance() != null) {
				offenderDemographicsForm.setEyeColor(
						personDemographics.getAppearance().getEyeColor());
				offenderDemographicsForm.setHairColor(
						personDemographics.getAppearance().getHairColor());
				offenderDemographicsForm.setComplexion(
						personDemographics.getAppearance().getComplexion());
			}
			if (personDemographics.getPhysique() != null) {
				if (personDemographics.getPhysique().getHeight() != null) {
					offenderDemographicsForm.setHeightFeet(
							personDemographics.getPhysique()
								.getHeight().getFeet());
					offenderDemographicsForm.setHeightInches(
							personDemographics.getPhysique()
								.getHeight().getInches());
				}
				if (personDemographics.getPhysique().getWeight() != null) {
					offenderDemographicsForm.setWeightPounds(
							personDemographics.getPhysique()
								.getWeight().getPounds());
				}
				offenderDemographicsForm.setBuild(
						personDemographics.getPhysique().getBuild());
			}
			offenderDemographicsForm.setDominantSide(
					personDemographics.getDominantSide());
			offenderDemographicsForm.setRace(personDemographics.getRace());
			offenderDemographicsForm.setHispanicEthnicity(
					personDemographics.getHispanicEthnicity());
			offenderDemographicsForm.setTribe(personDemographics.getTribe());
			offenderDemographicsForm.setMaritalStatus(personDemographics
					.getMaritalStatus());	
		}
		
		Country countryOfCitizenship = this.offenderDemographicsService
			.findCountryOfCitizenship(offender);
		offenderDemographicsForm.setCountryOfCitizenship(countryOfCitizenship);
		if (this.offenderDemographicsService.hasLegalResidenceStatus(offender)) 
			{
			Boolean legal = this.offenderDemographicsService
					.findLegalResidenceStatus(offender);
			AlienResidenceLegality alienResidenceLegality;
			if (legal != null) {
				if (legal) {
					alienResidenceLegality = AlienResidenceLegality.LEGAL;
				} else {
					alienResidenceLegality = AlienResidenceLegality.ILLEGAL;
				}
			} else {
				alienResidenceLegality = AlienResidenceLegality.UNKNOWN;
			}
			offenderDemographicsForm
				.setAlienResidenceLegality(alienResidenceLegality);
		}
		ModelAndView mav = this.prepareEditMav(
				offender, offenderDemographicsForm);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(PERSON_DEMOGRAPHICS_MODEL_KEY, personDemographics);
		return mav;
	}
	
	/**
	 * Updates offender demographics.
	 * 
	 * @param offender offender
	 * @param form offender demographics form
	 * @param result binding result
	 * @return redirect to edit offender demographics
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('OFFENDER_DEMOGRAPHICS_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			final OffenderDemographicsForm form,
			final BindingResult result) {
		this.offenderDemographicsFormValidator.validate(
				form, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(
					offender, form, result);
			mav.addObject(OFFENDER_MODEL_KEY, offender);
			return mav;
		}
		if (form.getCountryOfCitizenship() != null) {
			this.offenderDemographicsService.changeCountryOfCitizenship(
					offender, form.getCountryOfCitizenship());
			if (this.offenderDemographicsService.isHomeCountry(
					form.getCountryOfCitizenship())) {
				if (this.offenderDemographicsService
						.hasLegalResidenceStatus(offender)) {
					this.offenderDemographicsService
						.removeAlienResidence(offender);
				}
			}
		} else {
			this.offenderDemographicsService.removeCountryOfCitizienship(
					offender);
		}
		if (form.getAlienResidenceLegality() != null) {
			if (form.getCountryOfCitizenship() != null) {
				if (this.offenderDemographicsService.isHomeCountry(
						form.getCountryOfCitizenship())) {
					throw new IllegalArgumentException(
							"Immigration status not allowed for citizen");
				}
			}
			AlienResidenceLegality alienResidenceLegality
				= form.getAlienResidenceLegality();
			Boolean legal;
			if (AlienResidenceLegality.LEGAL.equals(alienResidenceLegality)) {
				legal = true;
			} else if (AlienResidenceLegality
					.ILLEGAL.equals(alienResidenceLegality)) {
				legal = false;
			} else if (AlienResidenceLegality
					.UNKNOWN.equals(alienResidenceLegality)) {
				legal = null;
			} else {
				throw new AssertionError("Unknown alien residence legality "
						+ alienResidenceLegality.name());
			}
			this.offenderDemographicsService
				.changeLegalResidenceStatus(offender, legal);
		} else {
			this.offenderDemographicsService.removeAlienResidence(
					offender);
		}
		boolean hispanicEthnicity;
		if (form.getHispanicEthnicity() != null
				&& form.getHispanicEthnicity()) {
			hispanicEthnicity = true;
		} else {
			hispanicEthnicity = false;
		}
		this.offenderDemographicsService.updateDemographics(offender, 
				new PersonPhysique(new Height(form.getHeightFeet(), 
						form.getHeightInches()), new Weight(
								form.getWeightPounds()), form.getBuild()), 
						new PersonAppearance(form.getEyeColor(), 
								form.getHairColor(), form.getComplexion()), 
						form.getDominantSide(), form.getRace(),
						hispanicEthnicity, form.getTribe(),
						form.getMaritalStatus());
		return new ModelAndView(String.format(OFFENDER_PROFILE_REDIRECT,
				offender.getId()));
	}
	
	/**
	 * Returns the report for the specified offenders legal name and identity.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/demographicsDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_DEMOGRAPHICS_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportDemographicsDetails(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DEMOGRAPHICS_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				DEMOGRAPHICS_DETAILS_REPORT_NAME,
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
		boolean homeCountry = this.offenderDemographicsService
				.isHomeCountry(country);
		ModelAndView mav = new ModelAndView(BOOLEAN_VIEW_NAME);
		mav.addObject(BOOLEAN_VALUE_MODEL_KEY, homeCountry);
		return mav;
	}
	
	/* Helper methods. */
	
	// Prepares model and view to edit offender demographics
	private ModelAndView prepareEditMav(
			final Offender offender, 
			final OffenderDemographicsForm offenderDemographicsForm) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(OFFENDER_DEMOGRAPHICS_FORM_MODEL_KEY,
				offenderDemographicsForm);
		mav.addObject(BUILDS_MODEL_KEY,
				this.offenderDemographicsService.findBuilds());
		mav.addObject(COMPLEXIONS_MODEL_KEY,
				this.offenderDemographicsService.findComplexions());
		mav.addObject(EYE_COLORS_MODEL_KEY,
				this.offenderDemographicsService.findEyeColors());
		mav.addObject(HAIR_COLOR_MODEL_KEY,
				this.offenderDemographicsService.findHairColors());
		mav.addObject(MARITAL_STATUSES_MODEL_KEY,
				this.offenderDemographicsService.findMaritalStatuses());
		mav.addObject(RACES_MODEL_KEY,
				this.offenderDemographicsService.findRaces());
		mav.addObject(TRIBES_MODEL_KEY,
				this.offenderDemographicsService.findTribes());
		mav.addObject(DOMINANT_SIDES_MODEL_KEY, DominantSide.values());
		mav.addObject(COUNTRIES_MODEL_KEY,
				this.offenderDemographicsService.findCountries());
		boolean homeCountryCitizen;
		if (offenderDemographicsForm.getCountryOfCitizenship() != null) {
			homeCountryCitizen = this.offenderDemographicsService.isHomeCountry(
					offenderDemographicsForm.getCountryOfCitizenship());
		} else {
			homeCountryCitizen = false;
		}
		mav.addObject(HOME_COUNTRY_CITIZEN_MODEL_KEY, homeCountryCitizen);
		this.addOffenderSummary(mav, offender);
		return mav;
	}
	
	// Adds offender summary
	private void addOffenderSummary(final ModelAndView mav,
			final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	// Prepares model and view to redisplay form
	private ModelAndView prepareRedisplayMav(
			final Offender offender,
			final OffenderDemographicsForm offenderDemographicsForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(
				offender, offenderDemographicsForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ OFFENDER_DEMOGRAPHICS_FORM_MODEL_KEY, result);
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
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Build.class,
				this.buildPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Complexion.class,
				this.complexionPropertyEditorFactory.createPropertyEditor());
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
		binder.registerCustomEditor(DominantSide.class,
				this.dominantSidePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
				this.countryPropertyEditorFactory.createPropertyEditor());
	}
}