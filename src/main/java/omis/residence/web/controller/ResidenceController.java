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
package omis.residence.web.controller;

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

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.content.RequestContentMapping;
import omis.content.RequestContentType;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.exception.LocationNotAllowedException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.organization.exception.OrganizationExistsException;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.residence.domain.AllowedResidentialLocationRule;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.AllowedResidentialLocationRuleExistsException;
import omis.residence.exception.NonResidenceTermExistsException;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.report.ResidenceReportService;
import omis.residence.report.ResidenceSummary;
import omis.residence.report.ResidenceType;
import omis.residence.service.ResidenceService;
import omis.residence.web.form.ExistingResidenceOperation;
import omis.residence.web.form.ResidenceForm;
import omis.residence.web.form.ResidenceStatusOption;
import omis.residence.web.validator.ResidenceFormValidator;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Residence controller for residence related operations.
 * 
 * @author Sheronda Vaughn
 * @author Joel Norris
 * @author Yidong Li
 * @version 0.1.2 (January 19, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/residence")
@PreAuthorize("hasRole('USER')")
public class ResidenceController {

	/* Redirect view names. */

	private static final String LIST_REDIRECT_VIEW_NAME
		= "redirect:/residence/list.html?offender=%d&residenceType=%s";
	/* View names. */

	private static final String LIST_VIEW_NAME = "residence/list";

	private static final String EDIT_VIEW_NAME = "residence/edit";

	private static final String RESIDENCES_ACTION_MENU_VIEW_NAME
		= "residence/includes/residencesActionMenu";

	private static final String RESIDENCE_ACTION_MENU_VIEW_NAME
		= "residence/includes/residenceActionMenu";

	private static final String CITY_OPTIONS_VIEW_NAME
		= "residence/includes/cityOptions";

	private static final String ZIP_CODE_OPTIONS_VIEW_NAME
		= "residence/includes/zipCodeOptions";	

	private static final String ALLOWED_LOCATION_OPTIONS_VIEW_NAME
		= "residence/includes/allowedResidenceLocationOptions";

	private static final String RESIDENCE_ROW_ACTION_MENU_VIEW_NAME
		= "residence/includes/residenceRowActionMenu";
	
	private static final String NON_RESIDENCE_ROW_ACTION_MENU_VIEW_NAME
		= "residence/includes/nonResidenceRowActionMenu";
	
	private static final String NON_RESIDENCE_TERMS_TABLE_CONTENT_VIEW_NAME
		= "residence/invludes/nonResidenceTableContent";

	
	/* Model keys. */

	private static final String RESIDENCE_SUMMARY_MODEL_KEY 
		= "residenceSummaries";

	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String RESIDENCE_FORM_MODEL_KEY = "residenceForm";

	private static final String CITIES_MODEL_KEY = "cities";

	private static final String STATES_MODEL_KEY = "states";

	private static final String ZIP_CODES_MODEL_KEY = "zipCodes";

	private static final String EFFECTIVE_DATE_MODEL_KEY = "effectiveDate";

	private static final String RESIDENCE_TYPE_MODEL_KEY = "residenceType";

	private static final String VERIFICATION_METHODS_MODEL_KEY 
		= "verificationMethods";

	private static final String ALLOWED_LOCATIONS_MODEL_KEY 
		= "locations";

	private static final String RESIDENCE_STATUS_OPTIONS_MODEL_KEY
		= "residenceStatusOptions";

	private static final String STATUS_OPTION_MODEL_KEY
		= "residenceStatusOption";

	private static final String RESIDENCE_TERM_MODEL_KEY
		= "residenceTerm";
	
	private static final String NON_RESIDENCE_TERM_MODEL_KEY
		= "nonResidenceTerm";
	
	private static final String EXISTING_RESIDENCE_SUMMARY_MODEL_KEY
		= "existingResidenceSummary";
	
	private static final String STATUS_OPTIONS_MODEL_KEY = "statusOptions";
	
	private static final String NON_RESIDENCE_TERMS_PARAM_NAME
		= "nonResidenceTerms";
		
	/* Message Keys. */

	private static final String PRIMARY_RESIDENCE_EXISTS_MESSAGE_KEY
		= "primaryResidence.exists";

	private static final String RESIDENCE_STATUS_CONFLICT_EXISTS_MESSAGE_KEY
		= "residenceStatusConflict.exists";

	private static final String LOCATION_NOT_ALLOWED_EXISTS_MESSAGE_KEY
		= "locationNotAllowed.exists";
	
	private static final String RESIDENCE_TREM_EXISTS_EXCEPTION_MESSAGE_KEY
		= "residenceTerm.Exists";
	
	private static final String NONRESIDENCE_TERM_EXISTS_EXCEPTION_MESSAGE_KEY
		= "nonResidenceTerm.exists";
	
	private static final String CITY_EXISTS_EXCEPTION_MESSAGE_KEY
		= "city.exists";
	
	private static final String ZIP_CODE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "zipCode.exists";
	
	private static final String ADDRESS_EXISTS_MESSAGE_KEY
		= "address.exists";
	
	private static final String ORGANIZATION_EXISTS_MESSAGE_KEY
		= "organization.exists";

	private static final 
		String ALLOWED_RESIDENTIAL_LOCATION_RULE_EXISTS_MESSAGE_KEY
		= "allowedResidentialLocationRule.exists";
	
	private static final 
	String LOCATION_EXISTS_MESSAGE_KEY
		= "location.exists";
	
	/* Message bundles. */

	private static final String ERROR_BUNDLE_NAME
		= "omis.residence.msgs.form";

	/* Services. */

	@Autowired
	@Qualifier("residenceReportService")
	private ResidenceReportService residenceReportService;

	@Autowired
	@Qualifier("residenceService")
	private ResidenceService residenceService;

	/* Validators. */

	@Autowired
	@Qualifier("residenceFormValidator")
	private ResidenceFormValidator
		residenceFormValidator;

	/* Helpers. */

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Property editors. */

	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;

	@Autowired
	@Qualifier("residenceTermPropertyEditorFactory")
	private PropertyEditorFactory residenceTermPropertyEditorFactory;

	@Autowired
	@Qualifier("nonResidenceTermPropertyEditorFactory")
	private PropertyEditorFactory nonResidenceTermPropertyEditorFactory;

	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;

	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;

	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;

	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;

	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;

	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;

	@Autowired
	@Qualifier("allowedResidentialLocationRulePropertyEditorFactory")
	private PropertyEditorFactory 
		allowedResidentialLocationRulePropertyEditorFactory;

	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;

	@Autowired
	@Qualifier("verificationMethodPropertyEditorFactory")
	private PropertyEditorFactory verificationMethodPropertyEditorFactory;

	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;

	/* Report names. */
	
	private static final String RESIDENCE_LISTING_REPORT_NAME 
		= "/CaseManagement/Residence/Residence_Listing";

	private static final String RESIDENCE_LISTING_LEGACY_REPORT_NAME 
		= "/CaseManagement/Residence/Residence_Listing_Legacy";
	
	private static final String RESIDENCE_DETAILS_REPORT_NAME 
		= "/CaseManagement/Residence/Residence_Details";

	private static final String NON_RESIDENCE_DETAILS_REPORT_NAME 
		= "/CaseManagement/Residence/Non_Resident_Details";
	
	/* Report parameter names. */
	
	private static final String RESIDENCE_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String RESIDENCE_DETAILS_ID_REPORT_PARAM_NAME 
		= "RESIDENCE_ID";

	private static final String NON_RESIDENCE_DETAILS_ID_REPORT_PARAM_NAME 
		= "NON_RESD_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */

	/** Instantiates a default controller for residences. */
	public ResidenceController() {
		//Default controller.
	}

	/* Screens. */

	/**
	 * Displays a list of residences for a specified offender.
	 * 
	 * @param offender offender
	 * @param residenceType residence type
	 * @param effectiveDate effective date
	 * @return model and view to a list of residences
	 */
	@RequestContentMapping(nameKey = "residenceListingScreenName",
			descriptionKey = "residenceListingScreenDescription",
			messageBundle = "omis.residence.msgs.residence",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping("/list.html")
	@PreAuthorize("hasRole('RESIDENCE_TERM_LIST') "
			+ "or hasRole('NON_RESIDENCE_TERM_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "residenceType", required = false)
			final ResidenceType residenceType,
			@RequestParam(value = "effectiveDate", required = false)
			final Date effectiveDate) {
		Date queryDate = new Date();
		if (effectiveDate != null) {
			queryDate = effectiveDate;			
		}
		List<ResidenceSummary> residenceSummaries = this.residenceReportService
				.findByOffender(offender, queryDate);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(RESIDENCE_SUMMARY_MODEL_KEY, residenceSummaries);	
		mav.addObject(RESIDENCE_TYPE_MODEL_KEY, residenceType);
		mav.addObject(EFFECTIVE_DATE_MODEL_KEY, queryDate);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}

	/**
	 * Displays a form allowing a new residence address to be created.
	 * 
	 * @param offender offender
	 * @param residenceStatusOption residence status option
	 * @return model and view
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_TERM_CREATE') "
			+ "or hasRole('NON_RESIDENCE_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
			final Offender offender, 
			@RequestParam(value = "residenceStatusOption", required = true)
			final ResidenceStatusOption residenceStatusOption) {
		ResidenceForm residenceForm = new ResidenceForm();
		residenceForm.setStatusOption(residenceStatusOption);
		State state = this.residenceService.findHomeState();
		residenceForm.setState(state);	
		ResidenceTerm term = null;
		if (ResidenceStatusOption.PRIMARY_RESIDENCE
				.equals(residenceStatusOption) 
				|| ResidenceStatusOption.FOSTER_CARE.equals(
						residenceStatusOption)) {
			term = this.residenceService
					.findPrimaryResidence(new Date(), offender);
			residenceForm.setExistingPrimaryResidence(term);
			residenceForm.setNonResidenceTerms(
					this.residenceService.findNonResidenceTerms(
					new Date(),  offender));
		}
		ModelAndView mav = this.prepareMav(residenceForm, offender,
				residenceStatusOption);
		mav.addObject(EXISTING_RESIDENCE_SUMMARY_MODEL_KEY, 
				this.residenceReportService.summarizeByResidenceTerm(term));
		return mav;					
	}

	/**
	 * Save a new residence address.
	 * 
	 * @param offender offender
	 * @param residenceForm residenceForm
	 * @param result result
	 * @return model and view to redirect list URL
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @throws LocationNotAllowedException location not allowed exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws NonResidenceTermExistsException 
	 * @throws AllowedResidentialLocationRuleExistsException 
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws AddressExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 */
	@RequestContentMapping(nameKey = "residenceCreateSubmitName",
			descriptionKey = "residenceCreateSubmitDescription",
			messageBundle = "omis.residence.msgs.residence",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "create.html",
		method = RequestMethod.POST)
	@PreAuthorize("hasRole('RESIDENCE_TERM_CREATE') "
			+ "or hasRole('NON_RESIDENCE_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			final ResidenceForm residenceForm,
			final BindingResult result) 
					throws PrimaryResidenceExistsException, 
					ResidenceStatusConflictException, 
					LocationNotAllowedException,
					ResidenceTermExistsException,
					NonResidenceTermExistsException, 
					AllowedResidentialLocationRuleExistsException, 
					CityExistsException, ZipCodeExistsException, 
					AddressExistsException, LocationExistsException, 
					OrganizationExistsException {
		this.residenceFormValidator.validate(residenceForm, result);	
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(offender,
					residenceForm, result, null, null);	
			return mav;
		}
		City newCity;
		ZipCode newZipCode;
		if (residenceForm.getCreateNewCity()) {
			newCity = this.residenceService.createCity(
					residenceForm.getCityName(), residenceForm.getState(), 
					residenceForm.getState().getCountry());
		} else {
			newCity = residenceForm.getCity();
		}
		
		if (residenceForm.getCreateNewZipCode() != null
				&& residenceForm.getCreateNewZipCode()) {
			newZipCode = this.residenceService.createZipCode(
					residenceForm.getZipCodeValue(), 
					residenceForm.getZipCodeExtension(), 
					newCity);
		} else {
			newZipCode = residenceForm.getZipCode();
		}		
		
		final ResidenceStatus status;
		final ResidenceType residenceType;
		if (ResidenceStatusOption.PRIMARY_RESIDENCE.equals(
				residenceForm.getStatusOption())
				|| ResidenceStatusOption.SECONDARY_RESIDENCE.equals(
						residenceForm.getStatusOption())
						|| ResidenceStatusOption.FOSTER_CARE.equals(
								residenceForm.getStatusOption())) {
			boolean primary;
			boolean fosterCare;	
			Address createNewAddress = this.residenceService.createAddress(
					residenceForm.getValue(), null, 
					null,
					null, 
					newZipCode);
			residenceForm.getStatusOption();
			if (ResidenceStatusOption.PRIMARY_RESIDENCE
					.equals(residenceForm.getStatusOption())) {
				primary = true;
				fosterCare = false;
				status = ResidenceStatus.RESIDENT;
			} else if (ResidenceStatusOption.FOSTER_CARE
					.equals(residenceForm.getStatusOption())) {
				primary = true;
				fosterCare = true;
				status = ResidenceStatus.FOSTER_CARE;
			} else {
				primary = false;
				fosterCare = false;
				status = ResidenceStatus.RESIDENT;
			}
			if (primary && residenceForm.getEndConflictingNonResidenceTerms()) {
				for (NonResidenceTerm term : residenceForm
						.getNonResidenceTerms()) {
					this.residenceService.endNonResidenceTerm(term, 
							residenceForm.getStartDate());
				}
			}
			if (primary && residenceForm.getExistingPrimaryResidence() 
					!= null) {
				ResidenceTerm primaryResidence = residenceForm
						.getExistingPrimaryResidence();
				if (ExistingResidenceOperation.HISTORICAL.equals(
						residenceForm.getExistingResidenceOperation())) {
					this.residenceService.updateResidenceTerm(
							primaryResidence, new DateRange(DateRange
									.getStartDate(primaryResidence
											.getDateRange()), 
									residenceForm
									.getExistingHistoricalEndDate()), 
							primary, primaryResidence.getAddress(), 
							ResidenceStatus.FOSTER_CARE.equals(
									primaryResidence.getStatus()), 
							primaryResidence.getConfirmed(), 
							primaryResidence.getNotes(), 
							primaryResidence.getVerificationSignature());
				} else if (ExistingResidenceOperation.SECONDARY.equals(
						residenceForm.getExistingResidenceOperation())) {
					this.residenceService.updateResidenceTerm(
							primaryResidence, 
							primaryResidence.getDateRange(), false, 
							primaryResidence.getAddress(), 
							ResidenceStatus.FOSTER_CARE.equals(
									primaryResidence.getStatus()), 
							primaryResidence.getConfirmed(), 
							primaryResidence.getNotes(), 
							primaryResidence.getVerificationSignature());
				}
			}
			this.residenceService.createResidenceTerm(
					offender, new DateRange(residenceForm.getStartDate(), 
							residenceForm.getEndDate()), 
							primary,
							createNewAddress, fosterCare, 
							residenceForm.getConfirmed(), 
							residenceForm.getResidenceComment(), 
							new VerificationSignature(
									residenceForm.getVerifiedByUserAccount(),
									residenceForm.getVerificationDate(),
									residenceForm.getVerified(),
									residenceForm.getVerificationMethod()));
			residenceType = ResidenceType.RESIDENCE;
		} else if (ResidenceStatusOption.GROUP_HOME.equals(
				residenceForm.getStatusOption()) 
				|| ResidenceStatusOption.HOTEL.equals(
						residenceForm.getStatusOption())) {		
			if (ResidenceStatusOption.GROUP_HOME.equals(
					residenceForm.getStatusOption())) {
				status = ResidenceStatus.GROUP_HOME;
			} else {
				status = ResidenceStatus.HOTEL;
			}	
			
			Location createNewLocation;
			
			if (residenceForm.getCreateNewLocation()) {
				Address createNewAddress = this.residenceService.createAddress(
					residenceForm.getValue(), null, null, null, 
					newZipCode);
			
				createNewLocation = this.residenceService.createLocation(
						residenceForm.getLocationName(),  
						new DateRange(residenceForm.getStartDate(), 
								residenceForm.getEndDate()), 
						createNewAddress, status);
			} else {
				createNewLocation = residenceForm.getLocation();
			}
			this.residenceService.createNonResidenceTerm(
				offender, new DateRange(residenceForm.getStartDate(), 
						residenceForm.getEndDate()), status,
						createNewLocation, residenceForm.getConfirmed(), 
						residenceForm.getResidenceComment(), 
						new VerificationSignature(
								residenceForm.getVerifiedByUserAccount(),
								residenceForm.getVerificationDate(),
								residenceForm.getVerified(),
								residenceForm.getVerificationMethod()));
			residenceType = ResidenceType.NONRESIDENCE;		
		} else if (ResidenceStatusOption.HOMELESS.equals(
				residenceForm.getStatusOption())) {
			this.residenceService.createHomelessTerm(
					offender, new DateRange(residenceForm.getStartDate(), 
							residenceForm.getEndDate()), 
							newCity, 
							residenceForm.getState(), 
							residenceForm.getResidenceComment(),
							residenceForm.getConfirmed());
			residenceForm.getConfirmed();
			residenceType = ResidenceType.NONRESIDENCE;
			status = ResidenceStatus.HOMELESS;
		} else {
			throw new UnsupportedOperationException(
					"Status not supported: " 
							+ residenceForm.getStatusOption());
		}	
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				offender.getId(), residenceType.getName()));
	}

	/**
	 * Displays a form allowing an existing residence to be edited.
	 * 
	 * @param residenceTerm residence term
	 * @return model and view screen of existing residence for editing
	 */
	@RequestMapping(value = "editResidenceTerm.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_TERM_VIEW') or hasRole('ADMIN')")
	public ModelAndView editResidenceTerm(
			@RequestParam(value = "residenceTerm", required = true)
			final ResidenceTerm residenceTerm) {		
		Person person = residenceTerm.getPerson();
		final Offender offender = (Offender) person;
		ResidenceForm residenceForm = new ResidenceForm();
		if (ResidenceStatus.RESIDENT.equals(
				residenceTerm.getStatus())
				|| ResidenceStatus.FOSTER_CARE.equals(
						residenceTerm.getStatus())) {
			if (ResidenceStatus.RESIDENT.equals(
					residenceTerm.getStatus())) {
				if (ResidenceCategory.PRIMARY.equals(
						residenceTerm.getCategory())) {
					residenceForm.setStatusOption(
							ResidenceStatusOption.PRIMARY_RESIDENCE);
				} else {
					residenceForm.setStatusOption(
							ResidenceStatusOption.SECONDARY_RESIDENCE);
				}
			} else { 
				residenceForm.setStatusOption(
						ResidenceStatusOption.FOSTER_CARE);
			}
			residenceForm.setConfirmed(residenceTerm.getConfirmed());
			if (residenceTerm.getDateRange() != null) {
				residenceForm.setStartDate(
						residenceTerm.getDateRange().getStartDate());
				residenceForm.setEndDate(residenceTerm.getDateRange()
						.getEndDate());
			}
			residenceForm.setValue(residenceTerm.getAddress().getValue());
			residenceForm.setState(
					residenceTerm.getAddress().getZipCode().getCity()
					.getState());
			residenceForm.setCity(
					residenceTerm.getAddress().getZipCode().getCity());
			residenceForm.setZipCode(residenceTerm.getAddress().getZipCode());
			residenceForm.setResidenceComment(residenceTerm.getNotes());
			if (residenceTerm.getVerificationSignature() != null) {
				residenceForm.setVerifiedByUserAccount(
						residenceTerm.getVerificationSignature()
						.getUserAccount());
				residenceForm.setVerificationDate(
						residenceTerm.getVerificationSignature().getDate());
				residenceForm.setVerificationMethod(
						residenceTerm.getVerificationSignature().getMethod());
			}
		} else {
			throw new IllegalArgumentException(
					"Residence term status not supported: " 
							+ residenceTerm.getStatus());
		}
		ModelAndView mav = this.prepareMav(residenceForm, 
				offender, residenceForm.getStatusOption());
		mav.addObject(RESIDENCE_TERM_MODEL_KEY, residenceTerm);
		return mav;	
	}

	/**
	 * Displays a form allowing an existing residence to be edited.
	 * 

	 * @param nonResidenceTerm non residence term
	 * @return model and view screen of existing residence for editing
	 */
	@RequestMapping(value = "editNonResidenceTerm.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('NON_RESIDENCE_TERM_VIEW') or hasRole('ADMIN')")
	public ModelAndView editNonResidenceTerm(
			@RequestParam(value = "nonResidenceTerm", required = true)
			final NonResidenceTerm nonResidenceTerm) {
		Offender offender = (Offender) nonResidenceTerm.getPerson();
		ResidenceForm residenceForm = new ResidenceForm();		
		if (ResidenceStatus.GROUP_HOME.equals(
				nonResidenceTerm.getStatus()) 
				|| ResidenceStatus.HOTEL.equals(
						nonResidenceTerm.getStatus())) {
			if (ResidenceStatus.GROUP_HOME.equals(
					nonResidenceTerm.getStatus())) {
				residenceForm.setStatusOption(ResidenceStatusOption.GROUP_HOME);
			} else {
				residenceForm.setStatusOption(ResidenceStatusOption.HOTEL);
			}	
			residenceForm.setConfirmed(nonResidenceTerm.getConfirmed());
			residenceForm.setLocation(nonResidenceTerm.getLocation());
			if (nonResidenceTerm.getLocation() != null) {
				residenceForm.setState(nonResidenceTerm.getLocation()
						.getAddress()
					.getZipCode().getCity().getState());
				residenceForm.setCity(nonResidenceTerm.getLocation()
						.getAddress()
					.getZipCode().getCity());	
			}		
			if (nonResidenceTerm.getDateRange() != null) {
				residenceForm.setStartDate(
							nonResidenceTerm.getDateRange().getStartDate());
				residenceForm.setEndDate(
							nonResidenceTerm.getDateRange().getEndDate());
			}
			residenceForm.setResidenceComment(nonResidenceTerm.getNotes());
			if (nonResidenceTerm.getVerificationSignature() != null) {
				residenceForm.setVerifiedByUserAccount(
						nonResidenceTerm.getVerificationSignature()
						.getUserAccount());
				residenceForm.setVerificationDate(
						nonResidenceTerm.getVerificationSignature().getDate());
				residenceForm.setVerificationMethod(
						nonResidenceTerm.getVerificationSignature()
						.getMethod());
			}
		} else if (ResidenceStatus.HOMELESS.equals(
				nonResidenceTerm.getStatus())) {
			residenceForm.setStatusOption(ResidenceStatusOption.HOMELESS);
			residenceForm.setState(nonResidenceTerm.getState());
			residenceForm.setCity(nonResidenceTerm.getCity());
			if (nonResidenceTerm.getDateRange() != null) {
				residenceForm.setStartDate(
						nonResidenceTerm.getDateRange().getStartDate());
				residenceForm.setEndDate(
						nonResidenceTerm.getDateRange().getEndDate());
			}
			residenceForm.setResidenceComment(nonResidenceTerm.getNotes());
			residenceForm.setConfirmed(nonResidenceTerm.getConfirmed());
		} else {
			throw new IllegalStateException(
					"Illegal Status");
		}
		ModelAndView mav = this.prepareMav(residenceForm, 
				offender, residenceForm.getStatusOption());
		mav.addObject(NON_RESIDENCE_TERM_MODEL_KEY, nonResidenceTerm);
		return mav;	
	}

	/*
	 * Model and view of the residence form.
	 * 
	 * @param residenceForm residence form
	 * @param person person
	 * @return model and view
	 */
	private ModelAndView prepareMav(final ResidenceForm residenceForm,
			final Offender offender, final ResidenceStatusOption statusOption) {
		ResidenceStatus status;
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(STATUS_OPTIONS_MODEL_KEY, ResidenceStatusOption.values());
		mav.addObject(RESIDENCE_FORM_MODEL_KEY, residenceForm);		
		mav.addObject(STATES_MODEL_KEY, 
				this.residenceService.findStatesInHomeCountry());	
		if (ResidenceStatusOption.GROUP_HOME.equals(
				residenceForm.getStatusOption())) {		
			status = ResidenceStatus.GROUP_HOME;
		} else { 
			status = ResidenceStatus.HOTEL;				
		}					
		if (residenceForm.getState() != null) {	
			mav.addObject(CITIES_MODEL_KEY, 
					this.residenceService.findCitiesInState(
							residenceForm.getState()));
			if (residenceForm.getCity() != null) {
				mav.addObject(ZIP_CODES_MODEL_KEY, this.residenceService
					.findZipCodesInCity(residenceForm.getCity()));	
				mav.addObject(ALLOWED_LOCATIONS_MODEL_KEY, 
					this.residenceService.findAllowedLocationsInCity(
							residenceForm.getCity(), status));				
			} else {
				mav.addObject(ALLOWED_LOCATIONS_MODEL_KEY, 
					this.residenceService.findAllowedLocationsInState(
							residenceForm.getState(), status));
			}
		}
		mav.addObject(VERIFICATION_METHODS_MODEL_KEY, 
				this.residenceService.findAllVerificationMethods());	
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(STATUS_OPTION_MODEL_KEY, statusOption);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}

	/**
	 * Model and view of the residence form with binding results.
	 * 
	 * @param residenceForm residence form
	 * @param result binding results
	 * @return model and view
	 */
	private ModelAndView prepareRedisplayMav(final Offender offender,
			final ResidenceForm residenceForm,
			final BindingResult result, final ResidenceTerm residenceTerm,
			final NonResidenceTerm nonResidenceTerm) {
		ModelAndView mav = this.prepareMav(residenceForm, offender,
				residenceForm.getStatusOption());
		if ((ResidenceStatusOption.PRIMARY_RESIDENCE
				.equals(residenceForm.getStatusOption()) 
				|| ResidenceStatusOption.FOSTER_CARE.equals(
						residenceForm.getStatusOption())) 
				&& residenceForm.getExistingPrimaryResidence() != null) {
			List<ResidenceSummary> residences = this.residenceReportService
					.findByOffender(offender, new Date());
			for (ResidenceSummary residence : residences) {
				if (ResidenceCategory.PRIMARY.equals(residence.getCategory())) {
					mav.addObject(EXISTING_RESIDENCE_SUMMARY_MODEL_KEY,
							residence);
					break;
				}
			}
		}
		mav.addObject(BindingResult.MODEL_KEY_PREFIX + RESIDENCE_FORM_MODEL_KEY,
				result);
		if (residenceTerm != null) {
			mav.addObject(RESIDENCE_TERM_MODEL_KEY, residenceTerm);
		} else if (nonResidenceTerm != null) {
			mav.addObject(NON_RESIDENCE_TERM_MODEL_KEY, nonResidenceTerm);
		}
		return mav;
	}

	/**
	 * Updates the specified residence address with the values from the 
	 * current form view. 
	 * 
	 * @param residenceTerm residence term
	 * @param nonResidenceTerm non residence term
	 * @param residenceForm residence form 
	 * @param result result
	 * @return model and view to redirect list URL
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 * @throws CityExistsException city exists exception
	 * @throws ZipCodeExistsException zip code exists exception
	 * @throws NonResidenceTermExistsException nonResidence term exists 
	 * exception
	 * @throws LocationNotAllowedException location not allowed exception
	 * @throws AllowedResidentialLocationRuleExistsException 
	 * @throws AddressExistsException 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException 
	 */
	@RequestMapping(value = {"editResidenceTerm.html", 
		"editNonResidenceTerm.html"},
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('RESIDENCE_TERM_EDIT') "
			+ "or hasRole('NON_RESIDENCE_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "residenceTerm", required = false)
			final ResidenceTerm residenceTerm,
			@RequestParam(value = "nonResidenceTerm", required = false)
			final NonResidenceTerm nonResidenceTerm,
			final ResidenceForm residenceForm,
			final BindingResult result) 
					throws PrimaryResidenceExistsException, 
					ResidenceStatusConflictException, 
					LocationNotAllowedException,
					ResidenceTermExistsException,
					CityExistsException, ZipCodeExistsException,
					NonResidenceTermExistsException, 
					AllowedResidentialLocationRuleExistsException, 
					AddressExistsException, LocationExistsException, 
					OrganizationExistsException {
		this.residenceFormValidator.validate(residenceForm, result);
		final Offender offender;
		if (residenceTerm != null) {
			offender = (Offender) residenceTerm.getPerson();
		} else if (nonResidenceTerm != null) {
			offender = (Offender) nonResidenceTerm.getPerson();
		} else {
			throw new IllegalArgumentException(
					"Residence or non residence term required");
		}
		if (result.hasErrors()) {			
			ModelAndView mav = this.prepareRedisplayMav(offender,
					residenceForm, result, residenceTerm, nonResidenceTerm);	
			return mav;
		}
		City newCity;
		ZipCode newZipCode;
		if (residenceForm.getCreateNewCity()) {
			newCity = this.residenceService.createCity(
					residenceForm.getCityName(), residenceForm.getState(), 
					residenceForm.getState().getCountry());
		} else {
			newCity = residenceForm.getCity();
		}
		
		if (residenceForm.getCreateNewZipCode() != null
				&& residenceForm.getCreateNewZipCode()) {
			newZipCode = this.residenceService.createZipCode(
					residenceForm.getZipCodeValue(), 
					residenceForm.getZipCodeExtension(), 
					newCity);
		} else {
			newZipCode = residenceForm.getZipCode();
		}		
		
		final ResidenceStatus status;
		final ResidenceType residenceType;
		if (ResidenceStatusOption.PRIMARY_RESIDENCE.equals(
				residenceForm.getStatusOption())
				|| ResidenceStatusOption.SECONDARY_RESIDENCE.equals(
						residenceForm.getStatusOption())
						|| ResidenceStatusOption.FOSTER_CARE.equals(
								residenceForm.getStatusOption())) {
			boolean primary;
			boolean fosterCare;	

			Address updatedAddress = this.residenceService.updateAddress(
					residenceTerm.getAddress(), residenceForm.getValue(), null, 
					null, null, newZipCode);
			if (ResidenceStatusOption.PRIMARY_RESIDENCE.equals(
					residenceForm.getStatusOption())) {				
				primary = true;
				fosterCare = false;
				status = ResidenceStatus.RESIDENT;
				residenceForm.setStatusOption(
						ResidenceStatusOption.PRIMARY_RESIDENCE);
			} else if (ResidenceStatusOption.FOSTER_CARE.equals(
					residenceForm.getStatusOption())) {
				primary = true;
				fosterCare = true;
				status = ResidenceStatus.FOSTER_CARE;	
				residenceForm.setStatusOption(
						ResidenceStatusOption.FOSTER_CARE);
			} else {
				primary = false;
				fosterCare = false;
				status = ResidenceStatus.RESIDENT;
				residenceForm.setStatusOption(
						ResidenceStatusOption.SECONDARY_RESIDENCE);
			}
			this.residenceService.updateResidenceTerm(
					residenceTerm, new DateRange(residenceForm.getStartDate(), 
							residenceForm.getEndDate()), 
							primary, updatedAddress, 
							fosterCare, residenceForm.getConfirmed(), 
							residenceForm.getResidenceComment(), 
							new VerificationSignature(
									residenceForm.getVerifiedByUserAccount(),
									residenceForm.getVerificationDate(),
									residenceForm.getVerified(),
									residenceForm.getVerificationMethod()));
			residenceType = ResidenceType.RESIDENCE;		
		} else if (ResidenceStatusOption.GROUP_HOME.equals(
				residenceForm.getStatusOption()) 
				|| ResidenceStatusOption.HOTEL.equals(
						residenceForm.getStatusOption())) {
			if (ResidenceStatusOption.GROUP_HOME.equals(
					residenceForm.getStatusOption())) {
				status = ResidenceStatus.GROUP_HOME;
				residenceForm.setStatusOption(ResidenceStatusOption.GROUP_HOME);
			} else {
				status = ResidenceStatus.HOTEL;
				residenceForm.setStatusOption(ResidenceStatusOption.HOTEL);
			}
			Location createNewUpdatedLocation;			
			if (residenceForm.getCreateNewLocation()) {
				Address createNewAddress = this.residenceService.createAddress(
					residenceForm.getValue(), null, null, null, 
					newZipCode);
			
				createNewUpdatedLocation = this.residenceService.createLocation(
						residenceForm.getLocationName(),  
						new DateRange(residenceForm.getStartDate(), 
								residenceForm.getEndDate()), 
						createNewAddress, status);
			} else {
				createNewUpdatedLocation = residenceForm.getLocation();
			}
			this.residenceService.updateNonResidenceTerm(nonResidenceTerm, 
					new DateRange(residenceForm.getStartDate(), 
							residenceForm.getEndDate()), 
							status, 
							createNewUpdatedLocation, 
							residenceForm.getConfirmed(), 
							residenceForm.getResidenceComment(), 
							new VerificationSignature(
									residenceForm.getVerifiedByUserAccount(),
									residenceForm.getVerificationDate(),
									residenceForm.getVerified(),
									residenceForm.getVerificationMethod()));
			residenceType = ResidenceType.NONRESIDENCE;
		} else if (ResidenceStatusOption.HOMELESS.equals(
				residenceForm.getStatusOption())) {
			this.residenceService.updateHomelessTerm(nonResidenceTerm, 
					new DateRange(residenceForm.getStartDate(), 
							residenceForm.getEndDate()), 
							newCity, 
							residenceForm.getState(), 
							residenceForm.getResidenceComment(),
							residenceForm.getConfirmed());
			residenceType = ResidenceType.NONRESIDENCE;
			status = ResidenceStatus.HOMELESS;
			residenceForm.setStatusOption(ResidenceStatusOption.HOMELESS);
		} else {
			throw new UnsupportedOperationException(
					"Status not supported: " 
							+ residenceForm.getStatusOption());
		}
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				offender.getId(), residenceType.getName()));
	}

	/**
	 * Remove a residence term or non residence term.
	 * 
	 * @param residenceTerm residence term
	 * @param nonResidenceTerm non residence term
	 * @return model and view 
	 */
	@RequestMapping(value = "remove.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_REMOVE') "
			+ "or hasRole('NON_RESIDENCE_TERM_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "residenceTerm", required = false)
			final ResidenceTerm residenceTerm, 
			@RequestParam(value = "nonResidenceTerm", required = false)
			final NonResidenceTerm nonResidenceTerm) {
		final Person person;
		final ResidenceType residenceType;
		if (residenceTerm != null) {			
			person = residenceTerm.getPerson();
			residenceType = ResidenceType.RESIDENCE;
			this.residenceService.removeResidenceTerm(residenceTerm);
		} else {
			person = nonResidenceTerm.getPerson();
			residenceType = ResidenceType.NONRESIDENCE;
			this.residenceService.removeNonResidenceTerm(nonResidenceTerm);
		}
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				person.getId(), residenceType.getName()));
	}

	/**
	 * Returns a view for a residences action menu pertaining to the 
	 * specified person.
	 * 
	 * @param offender offender
	 * @return model and view for residences action menu
	 */
	@RequestMapping(value = "/residencesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView residencesActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(RESIDENCE_STATUS_OPTIONS_MODEL_KEY,
				ResidenceStatusOption.values());
		return new ModelAndView(RESIDENCES_ACTION_MENU_VIEW_NAME, map);
	}

	/**
	 * Returns a view for a residence action menu pertaining to the 
	 * specified person.
	 * 
	 * @param offender offender 
	 * @return model and view for residence action menu
	 */
	@RequestMapping(value = "/residenceActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView residenceActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(RESIDENCE_ACTION_MENU_VIEW_NAME, map);
	}	

	/**
	 * Returns a view for city options.
	 * 
	 * @param state state
	 * @return model and view for city options
	 */
	@RequestMapping(value = "/showCityOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showCityOptions(
			@RequestParam(value = "state", required = false)
			final State state) {
		ModelMap map = new ModelMap();
		map.addAttribute(CITIES_MODEL_KEY, 
				this.residenceService.findCitiesInState(state));
		return new ModelAndView(CITY_OPTIONS_VIEW_NAME, map);
	}

	/**
	 * Returns a view for zip code options.
	 * 
	 * @param city city
	 * @return model and view for zip code options
	 */
	@RequestMapping(value = "/showZipCodeOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showZipCodeOptions(
			@RequestParam(value = "city", required = false)
			final City city) {
		ModelMap map = new ModelMap();
		map.addAttribute(ZIP_CODES_MODEL_KEY, 
				this.residenceService.findZipCodesInCity(city));

		return new ModelAndView(ZIP_CODE_OPTIONS_VIEW_NAME, map);
	}

	/**
	 * Returns a view of allowed residence location options.
	 * 
	 * @param residenceStatus residence status
	 * @param city city
	 * @param state state
	 * @param residenceForm residence form
	 * @return model and view of allowed residence location options
	 */
	@RequestMapping(value = "/showAllowedResidenceLocationOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showAllowedResidenceLocationOptions(
			@RequestParam(value = "residenceStatus", required = true)
			final ResidenceStatus residenceStatus,
			@RequestParam(value = "city", required = false)
			final City city,
			@RequestParam(value = "state", required = false)
			final State state,
			final ResidenceForm residenceForm) {
		ModelMap map = new ModelMap();
		if (city != null) {
			map.addAttribute(ALLOWED_LOCATIONS_MODEL_KEY, 
					this.residenceService.findAllowedLocationsInCity(
							city, residenceStatus)); 	
		} else if (state != null) {			
			map.addAttribute(ALLOWED_LOCATIONS_MODEL_KEY, 
					this.residenceService.findAllowedLocationsInState(
							state, residenceStatus));
		} 
//		else {
//			throw new IllegalStateException("City or State not selected.");
//		}
		return new ModelAndView(ALLOWED_LOCATION_OPTIONS_VIEW_NAME, map);
	}

	/**
	 * Returns residence row action menu.
	 * @param residenceTerm - residence term
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/residenceRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayResidenceTermRowActionMenu(
			@RequestParam(value = "residenceTerm", 
			required = true) final ResidenceTerm residenceTerm) {
		
		ModelMap map = new ModelMap();
		
		map.addAttribute(RESIDENCE_TERM_MODEL_KEY, residenceTerm);
		return new ModelAndView(
				RESIDENCE_ROW_ACTION_MENU_VIEW_NAME, map);
	}

	/**
	 * Returns non residence row action menu.
	 * @param nonResidenceTerm - non residence term
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/nonResidenceRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayNonResidenceTermRowActionMenu(
			@RequestParam(value = "nonResidenceTerm", 
			required = true) final NonResidenceTerm nonResidenceTerm) {
		
		ModelMap map = new ModelMap();
		
		map.addAttribute(NON_RESIDENCE_TERM_MODEL_KEY, nonResidenceTerm);
		return new ModelAndView(
				NON_RESIDENCE_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the content of the non residence term table.
	 * 
	 * @param date effective date
	 * @param person person
	 * @return model and view for contents of non residence term table
	 */
	@RequestMapping(value = "/displayNonResidenceTermsTableContent.html", 
			method = RequestMethod.GET)
	public ModelAndView displayNonResidenceTermsTableContent(
			@RequestParam(value = "date", required = true)final Date date,
			@RequestParam(value = "person", required = true)
			final Person person) {
		ModelMap map = new ModelMap();
		map.addAttribute(NON_RESIDENCE_TERMS_PARAM_NAME,
				this.residenceService.findNonResidenceTerms(date, person));
		return new ModelAndView(
				NON_RESIDENCE_TERMS_TABLE_CONTENT_VIEW_NAME, map);
	}
	
	/**
	 * Primary residence exists exception.
	 * 
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(PrimaryResidenceExistsException.class)
	public ModelAndView handlePrimaryResidenceExistsException(
			final PrimaryResidenceExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PRIMARY_RESIDENCE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}

	/**
	 * Residence status conflict exception.
	 * 
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(ResidenceStatusConflictException.class)
	public ModelAndView handleResidenceStatusConflictException(
			final ResidenceStatusConflictException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				RESIDENCE_STATUS_CONFLICT_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}

	/**
	 * Location not allowed exception.
	 * 
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(LocationNotAllowedException.class)
	public ModelAndView handleLocationNotAllowedException(
			final LocationNotAllowedException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_NOT_ALLOWED_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles {@code ResidenceTermExistsException}.
	 * 
	 * @param residenceTermExistsException exception thrown
	 * @return screen to handle {@code ResidenceTermExistsException}
	 */
	@ExceptionHandler(ResidenceTermExistsException.class)
	public ModelAndView handleResidenceTermExistsException(
		final ResidenceTermExistsException residenceTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			RESIDENCE_TREM_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, residenceTermExistsException);
	}
	
	/**
	 * NonResidence term exists exception.
	 *
	 *
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(NonResidenceTermExistsException.class)
	public ModelAndView handleNonResidenceTermExistsException(
			final NonResidenceTermExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				NONRESIDENCE_TERM_EXISTS_EXCEPTION_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * City exists exception.
	 *
	 *
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(CityExistsException.class)
	public ModelAndView handleCityExistsException(
			final CityExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CITY_EXISTS_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Zip code exists exception.
	 *
	 *
	 * @param exception exception
	 * @return exception
	 */
	@ExceptionHandler(ZipCodeExistsException.class)
	public ModelAndView handleZipCodeExistsException(
			final ZipCodeExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ZIP_CODE_EXISTS_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Address exists exception.
	 *
	 *
	 * @param exception exception
	 * @return address exception
	 */
	@ExceptionHandler(AddressExistsException.class)
	public ModelAndView handleAddresExistsException(
			final AddressExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ADDRESS_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
	}

	/**
	 * Organization exists exception.
	 *
	 *
	 * @param exception exception
	 * @return organization exception
	 */
	@ExceptionHandler(OrganizationExistsException.class)
	public ModelAndView handleOrganizationExistsException(
			final OrganizationExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ORGANIZATION_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * Allowed residential location rule exists exception.
	 *
	 *
	 * @param exception exception
	 * @return allowed residential location rule exception
	 */
	@ExceptionHandler(AllowedResidentialLocationRuleExistsException.class)
	public ModelAndView handleAllowedResidentialLocationRuleExistsException(
			final AllowedResidentialLocationRuleExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ALLOWED_RESIDENTIAL_LOCATION_RULE_EXISTS_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * Location exists exception.
	 *
	 *
	 * @param exception exception
	 * @return location exception
	 */
	@ExceptionHandler(LocationExistsException.class)
	public ModelAndView handleLocationExistsException(
			final LocationExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_EXISTS_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders residences.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/residenceListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportResidenceListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(RESIDENCE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				RESIDENCE_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders residences legacy.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/residenceListingLegacyReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportResidenceListingLegacy(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(RESIDENCE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				RESIDENCE_LISTING_LEGACY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified residence.
	 * 
	 * @param residenceTerm residence term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/residenceDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('RESIDENCE_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportResidenceDetails(@RequestParam(
			value = "residenceTerm", required = true)
			final ResidenceTerm residenceTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(RESIDENCE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(residenceTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				RESIDENCE_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified non-residence.
	 * 
	 * @param nonResidenceTerm residence term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/nonResidenceDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('NON_RESIDENCE_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportNonResidenceDetails(@RequestParam(
			value = "nonResidenceTerm", required = true)
			final ResidenceTerm nonResidenceTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(NON_RESIDENCE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(nonResidenceTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				NON_RESIDENCE_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Init binder. */

	/**
	 * Init binder.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(Person.class, 
				this.personPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ResidenceTerm.class, 
				this.residenceTermPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(NonResidenceTerm.class, 
				this.nonResidenceTermPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Address.class, 
				this.addressPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(City.class, 
				this.cityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class, 
				this.zipCodePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Location.class, 
				this.locationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AllowedResidentialLocationRule.class, 
				this.allowedResidentialLocationRulePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(UserAccount.class,
				this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(VerificationMethod.class,
				this.verificationMethodPropertyEditorFactory
				.createPropertyEditor());
	}
}	