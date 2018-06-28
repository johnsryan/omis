package omis.employment.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.address.web.form.AddressFields;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.VerificationMethodService;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.domain.EmploymentNote;
import omis.employment.domain.EmploymentTerm;
import omis.employment.domain.WagePaymentFrequency;
import omis.employment.domain.WorkShiftFrequency;
import omis.employment.domain.component.Job;
import omis.employment.domain.component.Wage;
import omis.employment.domain.component.WorkShift;
import omis.employment.exception.EmployerExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.employment.report.EmployerReportService;
import omis.employment.report.EmployerSummary;
import omis.employment.report.EmploymentReportService;
import omis.employment.report.EmploymentSummary;
import omis.employment.service.ChangeEmployerService;
import omis.employment.service.EmploymentService;
import omis.employment.web.form.EmployerChangeForm;
import omis.employment.web.form.EmployerSearchForm;
import omis.employment.web.form.EmploymentForm;
import omis.employment.web.validator.ChangeEmployerFormValidator;
import omis.employment.web.validator.EmploymentFormNoEmployerValidator;
import omis.employment.web.validator.EmploymentFormValidator;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.user.domain.UserAccount;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/** 
 * Controller for employment.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @versio 0.1.1 (Nov 21, 2017)
 * @since OMIS 3.0 
 */
@Controller
@RequestMapping("/employment")
@PreAuthorize("hasRole('USER')")
public class EmploymentController {
	/* views */
	private static final String EDIT_VIEW_NAME = "employment/edit";
	private static final String CREATE_VIEW_NAME = "employment/create";
	private static final String EMPLOYMENT_ACTION_MENU_VIEW_NAME
		= "employment/includes/employmentActionMenu";
	private static final String EMPLOYMENTS_ACTION_MENU_VIEW_NAME
		= "employment/includes/employmentsActionMenu";
	private static final String LIST_VIEW_NAME
		= "employment/list";
	private static final String VIEW_NAME = "employment/search";
	private static final String EMPLOYER_SEARCH_ACTION_MENU_VIEW_NAME
		= "employment/includes/employerSearchActionMenu";
	private static final String CHANGE_EMPLOYER_VIEW_NAME 
		= "employment/changeEmployer";
	private static final String EMPLOYMENT_ROW_ACTION_MENU_VIEW_NAME 
		= "/employment/includes/employmentRowActionMenu";
	private static final String ADDRESSES_VIEW_NAME
		= "address/json/addresses";
	private static final String CHANGE_EMPLOYER_ACTION_MENU_VIEW_NAME
		= "employment/includes/changeEmployerActionMenu";
		
	/* model keys */
	private static final String EMPLOYMENT_SUMMARY_ITEMS_MODEL_KEY 
		= "employmentSummaryItems";
	private static final String EMPLOYMENT_HISTORY_EDIT_FORM_MODEL_KEY 
		= "employmentForm";
	private static final String WORK_SHIFT_FREQUENCY_MODEL_KEY = "workShifts";
	private static final String WAGE_PAYMENT_FREQUENCY_MODEL_KEY 
		= "paymentFrequencys";
	private static final String EMPLOYMENT_CHANGE_REASON_MODEL_KEY 
		= "terminationReasons";
	private static final String VERIFICATION_METHOD_METHODS_MODEL_KEY 
		= "verificationMethods";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String EMPLOYMENT_TERM_MODEL_KEY = "employmentTerm";
	private static final String OFFENDER_SUMMARY_MODEL_KEY = "offenderSummary";
	private static final String EMPLOYER_SEARCH_FORM_MODEL_KEY 
		= "employerSearchForm";
	private static final String EMPLOYER_SUMMARIES_MODEL_KEY 
		= "employerSummaries";
	private static final String EMPLOYER_STATUS_MODEL_KEY = "employerStatus";
	private static final String EMPLOYER_CHANGE_FORM_MODEL_KEY 
		= "employerChangeForm";
	private static final String EMPLOYER_MODEL_KEY = "employer";
	private static final String CREATE_MODEL_KEY = "create";
   	private static final String ADDRESSES_MODEL_KEY
		= "addresses";
   	private static final String NEW_ADDRESS_MODEL_KEY = "newAddress";
   	private static final String CREATE_EMPLOYER_MODEL_KEY = "createEmployer";
   	private static final String CREATE_ADDRESS_MODEL_KEY = "createAddress";
			
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/employment/list.html?offender=%d";
	
	private static final String EDIT_REDIRECT
		= "redirect:/employment/edit.html?employmentTerm=%d";
	
	/* Property editor. */
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("employmentTermPropertyEditorFactory")
	private PropertyEditorFactory employmentTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("employmentNotePropertyEditorFactory")
	private PropertyEditorFactory employmentNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("employmenChangeReasonPropertyEditorFactory")
	private PropertyEditorFactory employmenChangeReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("employerPropertyEditorFactory")
	private PropertyEditorFactory employerPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory1;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressUnitDesignatorPropertyEditorFactory")
	private PropertyEditorFactory addressUnitDesignatorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("streetSuffixPropertyEditorFactory")
	private PropertyEditorFactory streetSuffixPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("verificationMethodPropertyEditorFactory")
	private PropertyEditorFactory verificationMethodPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;
	
	/* Services. */
	@Autowired
	@Qualifier("employmentService")
	private EmploymentService employmentService; 
	
	@Autowired
	@Qualifier("verificationMethodService")
	private VerificationMethodService verificationMethodService;
	
	@Autowired
	@Qualifier("employmentReportService")
	private EmploymentReportService employmentReportService;
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("employerReportService")
	private EmployerReportService employerReportService;
	
	@Autowired
	@Qualifier("changeEmployerService")
	private ChangeEmployerService changeEmployerService;
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Message keys. */
	
	private static final String EMPLOYMENT_EXISTS_MESSAGE_KEY 
		= "employment.exists";
	
	private static final String EMPLOYER_EXISTS_MESSAGE_KEY 
		= "employer.exists";
	
	private static final String CITY_EXISTS_EXCEPTION_MESSAGE_KEY
		= "city.exists";

	private static final String ZIP_CODE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "zipCode.exists";
	
	private static final String ADDRESS_EXISTS_MESSAGE_KEY
		= "address.exists";
	
	private static final String ORGANIZATION_EXISTS_MESSAGE_KEY
		= "organization.exists";
	
	private static final String LOCATION_EXISTS_MESSAGE_KEY
		= "location.exists";
	
	/* Message bundles. */

	private static final String ERROR_BUNDLE_NAME
		= "omis.employment.msgs.form";

	private static final String ADDRESS_ERROR_BUNDLE_NAME
		= "omis.address.msgs.form";

	private static final String LOCATION_ERROR_BUNDLE_NAME
		= "omis.location.msgs.form";

	/* Report names. */
	
	private static final String EMPLOYMENT_LISTING_REPORT_NAME 
		= "/CaseManagement/Employment/Employment_Listing";

	private static final String EMPLOYMENT_DETAILS_REPORT_NAME 
		= "/CaseManagement/Employment/Employment_Details";
	
	/* Report parameter names. */
	
	private static final String EMPLOYMENT_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String EMPLOYMENT_DETAILS_ID_REPORT_PARAM_NAME 
		= "EMPLOYMENT_TERM_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Validator */
	@Autowired
	@Qualifier("employmentFormValidator")
	private EmploymentFormValidator employmentFormValidator;
	
	@Autowired
	@Qualifier("changeEmployerFormValidator")
	private ChangeEmployerFormValidator changeEmployerFormValidator;
	
	@Autowired
	@Qualifier("employmentFormNoEmployerValidator")
	private EmploymentFormNoEmployerValidator employmentFormNoEmployerValidator;

	/* Field formats. */
	private static final String USER_ACCOUNT_LABEL_FORMAT = "%s, %s (%s)";
	
	/* Property Name */
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	
	/* Constructor. */
	/** Instantiates a default employment controller. */
	public EmploymentController() {
		// Default instantiation
	}
	
	/**
	 * Displays screen to create a new employment record.
	 * 
	 * @param offender offender for whom to create a new employment record
	 * @return model and view to create a new employment record
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "exist", required = true)
		final Boolean exist,
		@RequestParam(value = "employer", required = false)
		final Employer employer) {
			EmploymentForm employmentForm = new EmploymentForm();
			EmploymentTerm employmentTerm = null;
			employmentForm.setEmploymentAddressOperation(
					EmploymentAddressOperation.EXISTING);
			employmentForm.setExistingEmployer(employer);
			State homeState = this.employmentService.findHomeState();
			if (homeState != null) {
				AddressFields addressFields = new AddressFields();
				addressFields.setCountry(homeState.getCountry());
				employmentForm.setAddressFields(addressFields);
			}
			return this.prepareEditMav(employmentForm, offender, 
				employmentTerm, exist, employer, true);
	}
	
	/**
	 * Displays a list of employment record.
	 * 
	 * @param offender offender
	 * @return view to display the list of employment records
	 */
	@RequestMapping(value="/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", required = true) 
		final Offender offender) {
		List<EmploymentSummary> EmploymentSummaryItems 
			= new ArrayList<EmploymentSummary>();
		EmploymentSummaryItems = this.employmentReportService
			.findByOffender(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		mav.addObject(EMPLOYMENT_SUMMARY_ITEMS_MODEL_KEY,
			EmploymentSummaryItems);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender(offender);
		mav.addObject(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);
		return mav;
	}
	
	/** Edit an existing employment record. 
	 * @param employmentTerm employmentTerm.
	 * @return edited employment record view. */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
		@RequestParam(value = "employmentTerm", required = true)
		final EmploymentTerm employmentTerm) {
		EmploymentForm employmentForm 
			= new EmploymentForm();
		employmentForm.setConvictedOfEmployerTheft(
			employmentTerm.getConvictedOfEmployerTheft());
		employmentForm.setDaysOff(employmentTerm.getJob()
			.getWorkShift().getWorkShiftDays());
		if(employmentTerm.getDateRange()!=null){
			if(employmentTerm.getDateRange().getEndDate()!=null){
				employmentForm.setEndDate(employmentTerm
					.getDateRange().getEndDate());
			}
			if(employmentTerm.getDateRange().getStartDate()!=null){
				employmentForm.setStartDate(employmentTerm
					.getDateRange().getStartDate());
			}
		}
		if(employmentTerm.getJob()!=null){
			if(employmentTerm.getJob().getWorkShift()!=null){
				if(employmentTerm.getJob().getWorkShift().getScheduleEndTime()!=null){
					employmentForm.setEndTime(employmentTerm
						.getJob().getWorkShift().getScheduleEndTime());
				}
				if(employmentTerm.getJob().getWorkShift().getScheduleStartTime()!=null){
					employmentForm.setStartTime(employmentTerm
						.getJob().getWorkShift().getScheduleStartTime());
				}
			}
		}
		employmentForm.setExtension(employmentTerm.getJob()
			.getPhoneExtension());
		if(employmentTerm.getJob()!=null){
			if(employmentTerm.getJob().getWage()!=null){
				if(employmentTerm.getJob().getWage().getHoursPerWeek()!=null){
					employmentForm.setHoursPerWeek(employmentTerm
						.getJob().getWage().getHoursPerWeek());
				}
			}
		}
		employmentForm.setJobTitle(employmentTerm.getJob()
			.getJobTitle());
		if(employmentTerm.getJob()!=null){
			if(employmentTerm.getJob().getWage()!=null){
				if(employmentTerm.getJob().getWage().getWagePaymentFrequency()
					!=null){
					employmentForm.setPaymentFrequency(employmentTerm
						.getJob().getWage().getWagePaymentFrequency());
				}
			}
		}
		
		employmentForm.setTerminationReason(employmentTerm
			.getEndReason());
		employmentForm.setVaries(employmentTerm.getJob()
			.getWorkShift().getTimesMayVary());
		employmentForm.setVerificationDate(employmentTerm
			.getVerificationSignature().getDate());
		employmentForm.setVerificationMethod(employmentTerm
			.getVerificationSignature().getMethod());
		employmentForm.setVerified(employmentTerm
			.getVerificationSignature().getResult());
		employmentForm.setVerificationUserAccount(employmentTerm
			.getVerificationSignature().getUserAccount());
		if (employmentTerm.getVerificationSignature()
			.getUserAccount() != null) {
			UserAccount userAccount = employmentTerm.getVerificationSignature()
				.getUserAccount(); 
			employmentForm.setVerificationUserAccountLabel(
				String.format(USER_ACCOUNT_LABEL_FORMAT,
				userAccount.getUser().getName().getLastName(),
				userAccount.getUser().getName().getFirstName(),
				userAccount.getUsername()));
		}
		if(employmentTerm.getJob()!=null){
			if(employmentTerm.getJob().getWage()!=null){
				if(employmentTerm.getJob().getWage().getAmount() !=null){
					employmentForm.setWageAmount(employmentTerm
						.getJob().getWage().getAmount());
				}
			}
		}
		employmentForm.setWorkShift(employmentTerm.getJob()
			.getWorkShift().getWorkShiftFrequency());
		
		employmentForm.setSupervisorName(employmentTerm.getJob().getSupervisorName());
		employmentForm.setEmploymentAddressOperation(EmploymentAddressOperation.EXISTING);
		Employer employer = employmentTerm.getJob().getEmployer();
		employmentForm.setExistingEmployer(employmentTerm.getJob().getEmployer());
		Long telephoneNumberLong = employmentTerm.getJob().getEmployer().getTelephoneNumber();
		if(telephoneNumberLong!=null){
			String telephoneNumberString = telephoneNumberLong.toString();
			employmentForm.setTelephoneNumber(telephoneNumberString);
		}
		ModelAndView mav = prepareEditMav(employmentForm, 
			employmentTerm.getOffender(), employmentTerm, true, employer, false); 
		return mav; 
	}
	
	/**
	 * Saves a new created employment record.
	 * 
	 * @param offender offender
	 * @param employmentForm employment history edit/create Form
	 * @param result binding result
	 * @return redirect to list employment history by offender
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws LocationExistsException 
	 * @throws OrganizationExistsException 
	 * @throws EmployerExistsException 
	 * @throws EmploymentExistsException 
	 * @throws DuplicateEntityFoundException if the vehicle association exists
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "employmentTerm", required = true)
		final EmploymentTerm employmentTerm,
		final EmploymentForm employmentForm,
		final BindingResult result) throws CityExistsException, 
			ZipCodeExistsException, EmployerExistsException, 
			OrganizationExistsException, LocationExistsException, 
			EmploymentExistsException {
		this.employmentFormValidator.validate(employmentForm,result);
		if (result.hasErrors()) {
			return this.prepareRedisplayEditMav(offender, 
				employmentForm, result, employmentTerm, true);
		} 

		VerificationSignature verificationSignature = new VerificationSignature(
			employmentForm.getVerificationUserAccount(),
			employmentForm.getVerificationDate(),
			employmentForm.getVerified(),
			employmentForm.getVerificationMethod());
		DateRange dateRange = new DateRange(employmentForm.
			getStartDate(),	employmentForm.getEndDate());
		
		WorkShift workShift = new WorkShift();
		workShift.setScheduleStartTime(employmentForm.getStartTime());
		workShift.setScheduleEndTime(employmentForm.getEndTime());
		
		workShift.setTimesMayVary(employmentForm.getVaries());
		workShift.setWorkShiftFrequency(employmentForm.getWorkShift());
		workShift.setWorkShiftDays(employmentForm.getDaysOff());
		
		Job job= new Job();
		job.setJobTitle(employmentForm.getJobTitle());
		job.setPhoneExtension(employmentForm.getExtension());
		job.setSupervisorName(employmentForm.getSupervisorName());
		
		Wage wage = new Wage();
		wage.setAmount(employmentForm.getWageAmount());
		wage.setHoursPerWeek(employmentForm.getHoursPerWeek());
		wage.setWagePaymentFrequency(employmentForm
			.getPaymentFrequency());
		
		if(employmentForm.getExistingEmployer()==null) {
			if((employmentForm.getEmploymentAddressOperation()!=null)
				&&(employmentForm.getEmploymentAddressOperation()
						.equals(EmploymentAddressOperation.NEW))){
				
				ZipCode newZipCode;
				City newCity;
				Address address;
				if(employmentForm.getAddressFields().getNewCity()){
					if(this.employmentService.hasStates(employmentForm.getAddressFields().getCountry())){
					newCity = this.employmentService.createCity(
						employmentForm.getAddressFields().getCityName(), 
						employmentForm.getAddressFields().getState(), 
						employmentForm.getAddressFields().getCountry());
					}
					else {
						newCity = this.employmentService.createCity(
						employmentForm.getAddressFields().getCityName(), 
						null, 
						employmentForm.getAddressFields().getCountry());			
					}
					newZipCode = this.employmentService.createZipCode(
						employmentForm.getAddressFields().getZipCodeValue(), 
						employmentForm.getAddressFields().getZipCodeExtension(), 
						newCity);
					try {
						address = this.employmentService.createAddress(
							employmentForm.getAddressFields().getValue(), 
							newZipCode);
					} catch (AddressExistsException e){
						throw new RuntimeException("Address exist", e);
					}
				}
				else {  // Existing city
					if(employmentForm.getAddressFields().getNewZipCode()){  // New zip code
						newZipCode = this.employmentService.createZipCode(
							employmentForm.getAddressFields().getZipCodeValue(), 
							employmentForm.getAddressFields().getZipCodeExtension(), 
							employmentForm.getAddressFields().getCity());
						try {
							address = this.employmentService.createAddress(
								employmentForm.getAddressFields().getValue(), 
								newZipCode);
						} catch (AddressExistsException e){
							throw new RuntimeException("Address exist", e);
						}
					}
					else {  // Existing zip code
						try {
							address = this.employmentService.createAddress(
							employmentForm.getAddressFields().getValue(),
							employmentForm.getAddressFields().getZipCode());
						} catch (AddressExistsException e){
							throw new RuntimeException("Address exist", e);
						}
					}
				}
				
				String telephoneNumberString = employmentForm.getTelephoneNumber();
				String updatedTelephoneNumberString;
				if(telephoneNumberString!=null&&!telephoneNumberString.isEmpty()){				
					updatedTelephoneNumberString 
						= telephoneNumberString.replaceAll("[(,), ,-]", "");
					Long telephoneNumberLong 
						= Long.valueOf(updatedTelephoneNumberString);	
					Employer employer = this.employmentService
						.createEmployer(employmentForm.getEmployerName(), 
						telephoneNumberLong,address);
					job.setEmployer(employer);
				}
				else {
					Employer employer = this.employmentService
						.createEmployer(employmentForm.getEmployerName(), 
						null,address);
					job.setEmployer(employer);
				}
			}
			else {
				Address address = employmentForm.getAddress();
				String telephoneNumberString = employmentForm.getTelephoneNumber();
				String updatedTelephoneNumberString;
				if(telephoneNumberString!=null&&!telephoneNumberString.isEmpty()){				
					updatedTelephoneNumberString 
						= telephoneNumberString.replaceAll("[(,), ,-]", "");
					Long telephoneNumberLong 
						= Long.valueOf(updatedTelephoneNumberString);	
					Employer employer = this.employmentService
						.createEmployer(employmentForm.getEmployerName(), 
						telephoneNumberLong,address);
					job.setEmployer(employer);
				}
				else {
					Employer employer = this.employmentService
						.createEmployer(employmentForm.getEmployerName(), 
						null,address);
					job.setEmployer(employer);
				}
			}
		}
		else {
			job.setEmployer(employmentForm.getExistingEmployer());
		}
		
		job.setWorkShift(workShift);
		job.setWage(wage);
		
		this.employmentService.create(offender, job.getEmployer(),dateRange, job, 
			employmentForm.getConvictedOfEmployerTheft(), 
			employmentForm.getTerminationReason(), 
			verificationSignature);
		
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Updates an existing employment record.
	 * 
	 * @param employmentTerm employment term to be updated
	 * @param employmentForm employment history edit form
	 * @param result binding result
	 * @param offender offender
	 * @param employer employer
	 * @return redirect to list of employment terms
	 * @throws EmploymentExistsException 
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "employmentTerm", required = true)
		final EmploymentTerm employmentTerm,
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "employer", required = true)
		final Employer employer,
		final EmploymentForm employmentForm,
		final BindingResult result) throws EmploymentExistsException {	
		this.employmentFormNoEmployerValidator.validate(employmentForm, 
			result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayEditMav(
				employmentTerm.getOffender(), employmentForm, result, 
				employmentTerm, false);
			mav.addObject(EMPLOYMENT_TERM_MODEL_KEY, employmentTerm);
			return mav;
		}
			
		VerificationSignature verificationSignature = new VerificationSignature(
			employmentForm.getVerificationUserAccount(),
			employmentForm.getVerificationDate(),
			employmentForm.getVerified(),
			employmentForm.getVerificationMethod());
		DateRange dateRange = new DateRange(employmentForm.
			getStartDate(),	employmentForm.getEndDate());
			
		WorkShift workShift = new WorkShift();
		workShift.setScheduleStartTime(employmentForm.getStartTime());
		workShift.setScheduleEndTime(employmentForm.getEndTime());
		
		
		
		workShift.setTimesMayVary(employmentForm.getVaries());
		workShift.setWorkShiftFrequency(employmentForm.getWorkShift());
		workShift.setWorkShiftDays(employmentForm.getDaysOff());
		
		Job job= new Job();
		job.setJobTitle(employmentForm.getJobTitle());
		job.setPhoneExtension(employmentForm.getExtension());
		
		job.setSupervisorName(employmentForm.getSupervisorName());
		
		Wage wage = new Wage();
		wage.setAmount(employmentForm.getWageAmount());
		wage.setHoursPerWeek(employmentForm.getHoursPerWeek());
		wage.setWagePaymentFrequency(employmentForm
			.getPaymentFrequency());
		
		job.setEmployer(employer);
		job.setWorkShift(workShift);
		job.setWage(wage);
		this.employmentService.update(
			employmentTerm, dateRange, job, 
			employmentForm.getConvictedOfEmployerTheft(),
			employmentForm.getTerminationReason(),
			verificationSignature);

		return new ModelAndView(String.format(LIST_REDIRECT,
			employmentTerm.getOffender().getId()));
	}
	
	/**
	 * Removes an existing employment record.
	 * 
	 * @param employmentTerm employment record to be removed
	 * @return redirect to list religious preferences
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "employmentTerm", required = true)
			final EmploymentTerm employmentTerm) {
		Offender offender = employmentTerm.getOffender();
		this.employmentService.remove(employmentTerm);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	private ModelAndView prepareEditMav(
		final EmploymentForm employmentForm,
		final Offender offender, final EmploymentTerm employmentTerm,
		final Boolean employerStatus, final Employer employer, 
		final Boolean createNew) {
		ModelMap map;
		if(createNew){
			map = new ModelMap(CREATE_VIEW_NAME);
			map.addAttribute(CREATE_MODEL_KEY, true);
			if(employmentForm.getEmploymentAddressOperation()!=null){
				if(employmentForm.getEmploymentAddressOperation().equals(
					EmploymentAddressOperation.EXISTING)){
					map.addAttribute(NEW_ADDRESS_MODEL_KEY, false);
				}
				if(employmentForm.getEmploymentAddressOperation().equals(
					EmploymentAddressOperation.NEW)){
					map.addAttribute(NEW_ADDRESS_MODEL_KEY, true);
				}
			}
			if(employmentForm.getEmploymentAddressOperation()==null){
				employmentForm.setEmploymentAddressOperation(
					EmploymentAddressOperation.EXISTING);
				map.addAttribute(NEW_ADDRESS_MODEL_KEY, false);
			}
		}
		else { 
			map = new ModelMap(EDIT_VIEW_NAME);
			map.addAttribute(CREATE_MODEL_KEY, false);
			map.addAttribute(NEW_ADDRESS_MODEL_KEY, true);
			employmentForm.setEmploymentAddressOperation(
				EmploymentAddressOperation.CURRENT);
		}
		
		map.addAttribute(EMPLOYMENT_HISTORY_EDIT_FORM_MODEL_KEY, 
			employmentForm);
		List<EmploymentChangeReason> terminationReasons 
			= this.employmentService.findEmploymentChangeReasons();
		map.addAttribute(EMPLOYMENT_CHANGE_REASON_MODEL_KEY,terminationReasons); 
		map.addAttribute(WORK_SHIFT_FREQUENCY_MODEL_KEY, 
			WorkShiftFrequency.values());
		map.addAttribute(WAGE_PAYMENT_FREQUENCY_MODEL_KEY,
			WagePaymentFrequency.values());
		List<VerificationMethod> verificationMethods
			= this.verificationMethodService.findAll();
		map.addAttribute(VERIFICATION_METHOD_METHODS_MODEL_KEY, 
			verificationMethods);
		map.addAttribute(EMPLOYER_STATUS_MODEL_KEY, 
			employerStatus);
		map.addAttribute(EMPLOYMENT_TERM_MODEL_KEY, employmentTerm);
		OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender(offender);
		map.addAttribute(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);
		map.addAttribute(EMPLOYER_MODEL_KEY, employer);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		List<Country> countries = this.employmentService.findCountries();
		List<State> states = this.employmentService.findStatesByCountry(null);
		List<City> cities = this.employmentService.findCitiesByState(null);
		List<ZipCode> zipCodes = this.employmentService.findZipCodesByCity(null);
				
		if(employmentForm.getAddressFields()!=null){
			if(employmentForm.getAddressFields().getCity()!=null){
				if(this.employmentService.hasStates(
					employmentForm.getAddressFields().getCountry())){
					this.addressFieldsControllerDelegate.prepareEditAddressFields(
						map, countries, this.employmentService.findStatesByCountry(
						employmentForm.getAddressFields().getCountry()), 
						this.employmentService.findCitiesByState(
							employmentForm.getAddressFields().getCity().getState()), 
						this.employmentService.findZipCodesByCity(
							employmentForm.getAddressFields().getCity()), 
						ADDRESS_FIELDS_PROPERTY_NAME);
				} else {  // No state
					this.addressFieldsControllerDelegate
					.prepareEditAddressFields(map, countries, null, 
						this.employmentService.findCitiesByCountry(
							employmentForm.getAddressFields().getCity().getCountry()), 
						this.employmentService.findZipCodesByCity(
							employmentForm.getAddressFields().getCity()), 
						ADDRESS_FIELDS_PROPERTY_NAME);
				}
			} else {  // no city
				if(employmentForm.getAddressFields().getCountry()!=null){
					if(this.employmentService.hasStates(
						employmentForm.getAddressFields().getCountry())){
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(map, countries, 
							this.employmentService.findStatesByCountry(
								employmentForm.getAddressFields().getCountry()), 
							this.employmentService.findCitiesByState(
								employmentForm.getAddressFields().getState()), 
							null, ADDRESS_FIELDS_PROPERTY_NAME);
					} else { // No state
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(map, countries, null, 
							this.employmentService.findCitiesByCountry(
								employmentForm.getAddressFields().getCountry()), 
							null, ADDRESS_FIELDS_PROPERTY_NAME);
					}
				} else { // No country
					this.addressFieldsControllerDelegate
					.prepareEditAddressFields(map, countries, null, null, null, 
					ADDRESS_FIELDS_PROPERTY_NAME);
				}
			}
		} else {
			this.addressFieldsControllerDelegate.prepareEditAddressFields(map, 
			countries, states, cities, zipCodes, ADDRESS_FIELDS_PROPERTY_NAME);
		}
		
		if(createNew){
			ModelAndView mav = new ModelAndView(CREATE_VIEW_NAME, map);
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			return mav;
		}
		else {
			ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME, map);
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			return mav;
		}
	}	
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(
		final Offender offender,
		final EmploymentForm employmentForm,
		final BindingResult result, final EmploymentTerm employmentTerm,
		final Boolean createNew) {
		Employer employer = employmentForm.getExistingEmployer();
		Boolean employerStatus;
		if(employmentForm.getExistingEmployer()==null){
			employerStatus = false;
		} else {
			employerStatus = true;
		}
		
		UserAccount userAccount = employmentForm
			.getVerificationUserAccount(); 
		if (userAccount != null) {
			employmentForm.setVerificationUserAccountLabel(
				String.format(USER_ACCOUNT_LABEL_FORMAT,
						userAccount.getUser().getName().getLastName(),
						userAccount.getUser().getName().getFirstName(),
						userAccount.getUsername()));
		}
		
		ModelAndView mav = this.prepareEditMav(
			employmentForm, offender, employmentTerm, employerStatus, employer, 
			createNew);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ EMPLOYMENT_HISTORY_EDIT_FORM_MODEL_KEY, result);
		return mav;
	}	

	/**
	 * Returns a view for an employment record action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return view for employment action menu
	 */
	@RequestMapping(value = "/employmentActionMenu.html",method =RequestMethod.GET)
	public ModelAndView employmentActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender,
		@RequestParam(value = "employerStatus",
		required = true) final Boolean employerStatus,
		@RequestParam(value = "employmentTerm",
		required = true) final EmploymentTerm employmentTerm) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(EMPLOYER_STATUS_MODEL_KEY, employerStatus);
		map.addAttribute(EMPLOYMENT_TERM_MODEL_KEY, employmentTerm);
		return new ModelAndView(EMPLOYMENT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for all employments records action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for employments action menu
	 */
	@RequestMapping(value = "/employmentsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView employmentsActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(EMPLOYMENTS_ACTION_MENU_VIEW_NAME, map);
	}	
	
	/**
	 * Returns a view for an employment record action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return view for employment action menu
	 */
	@RequestMapping(value = "/updateEmployerActionMenu.html",method =RequestMethod.GET)
	public ModelAndView updateEmployerActionMenu(
		@RequestParam(value = "employmentTerm",
		required = true) final EmploymentTerm employmentTerm) {
		ModelMap map = new ModelMap();
		map.addAttribute(EMPLOYMENT_TERM_MODEL_KEY, employmentTerm);
		return new ModelAndView(CHANGE_EMPLOYER_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays form to search for employer.
	 * 
	 * @param offender offender
	 * @return form to search for victim
	 */
	@RequestMapping(value = "/search.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('EMPLOYER_LIST') or hasRole('ADMIN')")
	public ModelAndView searchEmployer(@RequestParam(value = "offender",
			required = true) final Offender offender) {
		EmployerSearchForm employerSearchForm = new EmployerSearchForm();
		employerSearchForm.setOffender(offender);
		return this.prepareMav(employerSearchForm);
	}
	
	/**
	 * Search for employer.
	 * 
	 * @return search result
	 */
	@RequestMapping(value = "/search.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EMPLOYER_LIST') or hasRole('ADMIN')")
	public ModelAndView search(final EmployerSearchForm employerSearchForm) {
		List<EmployerSummary> employerSummaries=null;
		if(employerSearchForm.getEmployerName()!=null){
			employerSummaries 
				= this.employerReportService.summarizeByName(employerSearchForm
				.getEmployerName());
		}
			
		ModelAndView mav = this.prepareMav(employerSearchForm);
		mav.addObject(EMPLOYER_SUMMARIES_MODEL_KEY, employerSummaries);
		return mav;
	}
	
	/**
	 * Returns a view for action menu .
	 * 
	 * @param offender offender
	 * @return model and view for employer search action menu
	 */
	@RequestMapping(value = "/employerSearchActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView employerSearchActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(EMPLOYER_SEARCH_ACTION_MENU_VIEW_NAME, map);
	}
	
	// Returns model and view to search for employer
	private ModelAndView prepareMav(final EmployerSearchForm employerSearchForm){
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(EMPLOYER_SEARCH_FORM_MODEL_KEY, employerSearchForm);
		return mav;
	}
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country.
	 * 
	 * @param country country
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show state options
	 */
	@RequestMapping(value = "stateOptions.html", method = RequestMethod.GET)
	public ModelAndView showStateOptions(
			@RequestParam(value = "country", required = true)
				final Country country,
			@RequestParam(value = "addressFieldsPropertyName", required = true)
				final String addressFieldsPropertyName) {
		List<State> states = this.employmentService.findStatesByCountry(country);
		return this.addressFieldsControllerDelegate.showStateOptions(states, 
			addressFieldsPropertyName);
	}
	
	/**
	 * Returns the city options view with a collection of cities for the
	 * specified state.
	 * 
	 * @param state state
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show city options
	 */
	@RequestMapping(value = "cityOptions.html", method = RequestMethod.GET)
	public ModelAndView showCityOptions(
		@RequestParam(value = "state", required = false)
			final State state,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName,
			@RequestParam(value = "country", required = false)
		final Country country) {
		if(state!=null) {
			return this.addressFieldsControllerDelegate.showCityOptions(
				this.employmentService.findCitiesByState(state),
				addressFieldsPropertyName);
		}
		else {
			return this.addressFieldsControllerDelegate.showCityOptions(
				this.employmentService.findCitiesByCountry(country),
				addressFieldsPropertyName);
		}
	}
	
	/**
	 * Returns the zip code options view with a collection of zip codes for the
	 * specified city.
	 * 
	 * @param city city
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show zip code options
	 */
	@RequestMapping(value = "zipCodeOptions.html", method = RequestMethod.GET)
	public ModelAndView showZipCodeOptions(
		@RequestParam(value = "city", required = true)
			final City city,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName) {
		return this.addressFieldsControllerDelegate.showZipCodeOptions(
			this.employmentService.findZipCodesByCity(city), 
			addressFieldsPropertyName);
	}
	
	/**
	 * Returns a view for changing the employer of certain employment term.
	 * 
	 * @param offender offender
	 * @return model and view for employments action menu
	 */
	@RequestMapping(value = "/changeEmployer.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView changeEmployer(@RequestParam(value = "offender",
		required = true) final Offender offender,
		@RequestParam(value = "employmentTerm",
		required = true) final EmploymentTerm employmentTerm) {
		ModelMap map = new ModelMap();
		EmployerChangeForm employerChangeForm = new EmployerChangeForm();
		employerChangeForm.setNewEmployer(false);
		employerChangeForm.setNewAddress(false);
		employerChangeForm.setEmploymentTerm(employmentTerm);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(EMPLOYER_CHANGE_FORM_MODEL_KEY, employerChangeForm);
		List<Country> countries = this.employmentService.findCountries();
		List<State> states = this.employmentService.findStatesByCountry(null);
		List<City> cities = this.employmentService.findCitiesByState(null);
		List<ZipCode> zipCodes = this.employmentService.findZipCodesByCity(null);

		this.addressFieldsControllerDelegate.prepareEditAddressFields(map, 
			countries, states, cities, zipCodes, ADDRESS_FIELDS_PROPERTY_NAME);
		
		map.addAttribute(CREATE_EMPLOYER_MODEL_KEY, false);
		map.addAttribute(CREATE_ADDRESS_MODEL_KEY, false);
		return new ModelAndView(CHANGE_EMPLOYER_VIEW_NAME, map);
	}	
	
	/**
	 * Redisplay the view for changing the employer of certain employment term.
	 * 
	 * @param offender offender
	 * @return model and view for employments action menu
	 */
	private ModelAndView redisplayChangeEmployer(final Offender offender,
		final EmployerChangeForm employerChangeForm) {
		ModelMap map = new ModelMap();
		EmploymentTerm employmentTerm = employerChangeForm.getEmploymentTerm();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(EMPLOYER_CHANGE_FORM_MODEL_KEY, employerChangeForm);
		map.addAttribute(EMPLOYMENT_TERM_MODEL_KEY, employmentTerm);

		List<Country> countries = this.employmentService.findCountries();
		List<State> states = this.employmentService.findStatesByCountry(null);
		List<City> cities = this.employmentService.findCitiesByState(null);
		List<ZipCode> zipCodes = this.employmentService.findZipCodesByCity(null);
		
		if(employerChangeForm.getAddressFields()!=null){
			if(employerChangeForm.getAddressFields().getCity()!=null){
				if(this.employmentService.hasStates(
						employerChangeForm.getAddressFields().getCountry())){
					this.addressFieldsControllerDelegate
					.prepareEditAddressFields(map, countries, 
					this.employmentService.findStatesByCountry(
						employerChangeForm.getAddressFields().getCountry()), 
					this.employmentService.findCitiesByState(
						employerChangeForm.getAddressFields().getCity().getState()), 
					this.employmentService.findZipCodesByCity(
						employerChangeForm.getAddressFields().getCity()), 
					ADDRESS_FIELDS_PROPERTY_NAME);
				} else {  // No state
					this.addressFieldsControllerDelegate.prepareEditAddressFields(
						map, countries, null, this.employmentService
						.findCitiesByCountry(employerChangeForm
							.getAddressFields().getCity().getCountry()), 
					this.employmentService.findZipCodesByCity(
						employerChangeForm.getAddressFields().getCity()), 
					ADDRESS_FIELDS_PROPERTY_NAME);
				}
			} else {  // no city
				if(employerChangeForm.getAddressFields().getCountry()!=null){
					if(this.employmentService.hasStates(
						employerChangeForm.getAddressFields().getCountry())){
						this.addressFieldsControllerDelegate
						.prepareEditAddressFields(map, countries, 
							this.employmentService.findStatesByCountry(
							employerChangeForm.getAddressFields().getCountry()), 
							this.employmentService.findCitiesByState(
							employerChangeForm.getAddressFields().getState()), 
							null, ADDRESS_FIELDS_PROPERTY_NAME);
					} else { // No state
						this.addressFieldsControllerDelegate
						.prepareEditAddressFields(map, countries, null, 
							this.employmentService.findCitiesByCountry(
							employerChangeForm.getAddressFields().getCountry()), 
							null, ADDRESS_FIELDS_PROPERTY_NAME);
					}
				} else { // No country
					this.addressFieldsControllerDelegate.prepareEditAddressFields(
					map, countries, null, null, null, 
					ADDRESS_FIELDS_PROPERTY_NAME);
				}
			}
		} else {
			this.addressFieldsControllerDelegate.prepareEditAddressFields(map, 
			countries, states, cities, zipCodes, ADDRESS_FIELDS_PROPERTY_NAME);
		}
		
		map.addAttribute(CREATE_EMPLOYER_MODEL_KEY, employerChangeForm.getNewEmployer());
		map.addAttribute(CREATE_ADDRESS_MODEL_KEY, employerChangeForm.getNewAddress());
		
		return new ModelAndView(CHANGE_EMPLOYER_VIEW_NAME, map);
	}	
	
	/**
	 * Returns a view of edited employment term with changed employer.
	 * 
	 * @param offender offender
	 * @return model and view for employments action menu
	 * @throws CityExistsException 
	 * @throws ZipCodeExistsException 
	 * @throws LocationExistsException 
	 * @throws OrganizationExistsException 
	 * @throws EmployerExistsException 
	 * @throws EmploymentExistsException 
	 * @throws DuplicateEntityFoundException 
	 */
	@RequestMapping(value = "/changeEmployer.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('EMPLOYMENT_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView updateEmployemntTermWithChangedEmployer(
		@RequestParam(value = "offender", required = true) 
		final Offender offender,
		final EmployerChangeForm employerChangeForm,
		final BindingResult result) throws CityExistsException, 
			ZipCodeExistsException, EmployerExistsException, 
			OrganizationExistsException, LocationExistsException, 
			EmploymentExistsException {
		this.changeEmployerFormValidator.validate(employerChangeForm,result);
		if (result.hasErrors()) {
			return this.redisplayChangeEmployer(offender, 
				employerChangeForm);
		} 
		
		
		Employer employer;
		if(employerChangeForm.getNewEmployer()==true){
			if(employerChangeForm.getNewAddress()==true){
				
				City addressFieldsCity;
				Address addressFieldsAddress;
				ZipCode addressFieldsZipCode;
				if(employerChangeForm.getAddressFields().getNewCity()){
					if(this.employmentService.hasStates(employerChangeForm.getAddressFields().getCountry())){
						addressFieldsCity = this.employmentService.createCity(
						employerChangeForm.getAddressFields().getCityName(), 
						employerChangeForm.getAddressFields().getState(), 
						employerChangeForm.getAddressFields().getCountry());
					}
					else {
						addressFieldsCity = this.employmentService.createCity(
						employerChangeForm.getAddressFields().getCityName(), 
						null, 
						employerChangeForm.getAddressFields().getCountry());
					}
					addressFieldsZipCode = this.employmentService.createZipCode(
						employerChangeForm.getAddressFields().getZipCodeValue(), 
						employerChangeForm.getAddressFields().getZipCodeExtension(), 
						addressFieldsCity);
				}
				else {
					addressFieldsCity = employerChangeForm.getAddressFields().getCity();
					if(employerChangeForm.getAddressFields().getNewZipCode()){
						addressFieldsZipCode = this.employmentService.createZipCode(
							employerChangeForm.getAddressFields().getZipCodeValue(), 
							employerChangeForm.getAddressFields().getZipCodeExtension(), 
							addressFieldsCity);
					}
					else {
						addressFieldsZipCode = employerChangeForm.getAddressFields().getZipCode();
					}
				}
				
				try {
					addressFieldsAddress = this.employmentService.createAddress(
						employerChangeForm.getAddressFields().getValue(),
						addressFieldsZipCode);
				} catch (AddressExistsException e){
					throw new RuntimeException("Address exist", e);
				}
						
				String telephoneNumberString = employerChangeForm.getTelephoneNumber();
				String updatedTelephoneNumberString;
				if(telephoneNumberString!=null&&!telephoneNumberString.isEmpty()){				
					updatedTelephoneNumberString 
						= telephoneNumberString.replaceAll("[(,), ,-]", "");
					Long telephoneNumberLong 
						= Long.valueOf(updatedTelephoneNumberString);	
					employer = this.employmentService
						.createEmployer(employerChangeForm.getNewEmployerName(), 
						telephoneNumberLong, addressFieldsAddress);
				}
				else {
					employer = this.employmentService
						.createEmployer(employerChangeForm.getNewEmployerName(), 
						null, addressFieldsAddress);
				}
			}
			else {
				Address address = employerChangeForm.getAddressSearchResult();
				
				String telephoneNumberString = employerChangeForm.getTelephoneNumber();
				String updatedTelephoneNumberString;
				if(telephoneNumberString!=null&&!telephoneNumberString.isEmpty()){				
					updatedTelephoneNumberString 
						= telephoneNumberString.replaceAll("[(,), ,-]", "");
					Long telephoneNumberLong 
						= Long.valueOf(updatedTelephoneNumberString);	
					employer = this.employmentService
						.createEmployer(employerChangeForm.getNewEmployerName(), 
						telephoneNumberLong, address);
				}
				else {
					employer = this.employmentService
						.createEmployer(employerChangeForm.getNewEmployerName(), 
						null, address);
				}
			}
		}
		else {
			employer = employerChangeForm.getExistingEmployer();
		}
		
		EmploymentTerm updatedEmploymentTerm = this.changeEmployerService.change(
			employerChangeForm.getEmploymentTerm(), employer);
		return new ModelAndView(String.format(EDIT_REDIRECT, 
			updatedEmploymentTerm.getId()));
	}	
	
	/**
	 * Returns a list of sorted organizations matching the partial name.
	 * @param partialName partial name
	 * @return model and view to show sorted organizations
	 */
	@RequestMapping(value = "/findOrganizationsByPartialName.json",
		method = RequestMethod.GET)
	public ModelAndView findByParitalName(
		@RequestParam(value = "term") final String partialName) {
		ModelAndView mav = new ModelAndView("organization/json/names");
		List<Organization> organizations 
			= this.employmentService.findOrganizationByPartialName(partialName);
		mav.addObject("organizations", organizations);
		return mav;
	}
	
	/**
	 * Returns a list of addresses matching the partial name.
	 * @param query partial name
	 * @return a list of addresses
	 */
	@RequestMapping(value = "/findAddressesByPartialName.json",
		method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findAddressByParitalName(
		@RequestParam(value = "term", required = false) final String query) 
		throws IOException {
		ModelAndView mav = new ModelAndView(ADDRESSES_VIEW_NAME);
		List<Address> addresses;
		if (StringUtility.hasContent(query)) {
			addresses = this.employmentService.findAddresses(query);
		} else {
			addresses = Collections.emptyList();
		}
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}

	/**
	 * Returns employment row action menu
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value="/employmentRowActionMenu.html",
			method=RequestMethod.GET)
	public ModelAndView displayEmploymentRowActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender, 
			@RequestParam(value = "employmentTerm", 
			required = true) final EmploymentTerm emplymentTerm){
		
		ModelMap map = new ModelMap();
		
		map.addAttribute(EMPLOYMENT_TERM_MODEL_KEY, emplymentTerm);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
			EMPLOYMENT_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Employment exists exception.
	 *
	 *
	 * @param employmentExistsException employment exists exception
	 * @return model and view error message
	 */
	@ExceptionHandler(EmploymentExistsException.class)
	public ModelAndView handleEmploymemtExistsException(
			final EmploymentExistsException employmentExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				EMPLOYMENT_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				employmentExistsException);
	}

	/**
	 * Employer exists exception.
	 *
	 *
	 * @param employerExistsException employer exists exception
	 * @return model and view error message
	 */
	@ExceptionHandler(EmployerExistsException.class)
	public ModelAndView handleEmployerExistsException(
			final EmployerExistsException employerExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				EMPLOYER_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				employerExistsException);
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
				CITY_EXISTS_EXCEPTION_MESSAGE_KEY, ADDRESS_ERROR_BUNDLE_NAME, 
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
				ZIP_CODE_EXISTS_EXCEPTION_MESSAGE_KEY, 
				ADDRESS_ERROR_BUNDLE_NAME, exception);
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
				ADDRESS_EXISTS_MESSAGE_KEY, ADDRESS_ERROR_BUNDLE_NAME, 
				exception);
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
				ORGANIZATION_EXISTS_MESSAGE_KEY, LOCATION_ERROR_BUNDLE_NAME, 
				exception);
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
				LOCATION_ERROR_BUNDLE_NAME, exception);
	}

	/**
	 * Returns the report for the specified offenders employment.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/employmentListingReport.html",
			method = RequestMethod.GET)
	public ResponseEntity<byte []> reportEmploymentListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(EMPLOYMENT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				EMPLOYMENT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified employment term.
	 * 
	 * @param employmentTerm employment term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/employmentDetailsReport.html",
			method = RequestMethod.GET)
	public ResponseEntity<byte []> reportEmploymentDetails(@RequestParam(
			value = "employmentTerm", required = true)
			final EmploymentTerm employmentTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(EMPLOYMENT_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(employmentTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				EMPLOYMENT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(EmploymentChangeReason.class,
			this.employmenChangeReasonPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(EmploymentTerm.class,
			this.employmentTermPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(EmploymentNote.class,
			this.employmentNotePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Employer.class,
			this.employerPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
				this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
			this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Person.class,
			this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(VerificationMethod.class,
			this.verificationMethodPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "startDate",
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "startTime",
			this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endDate",
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endTime",
			this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class,
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Country.class, 
			this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
			this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class,
			this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class,
			this.addressUnitDesignatorPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class,
			this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
			this.locationPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Address.class,
			this.addressPropertyEditorFactory.createPropertyEditor());
	}
}
