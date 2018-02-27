package omis.adaaccommodation.web.controller;

import java.util.ArrayList;
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

import omis.adaaccommodation.domain.Accommodation;
import omis.adaaccommodation.domain.AccommodationCategory;
import omis.adaaccommodation.domain.AccommodationIssuance;
import omis.adaaccommodation.domain.AccommodationNote;
import omis.adaaccommodation.domain.Authorization;
import omis.adaaccommodation.domain.AuthorizationSourceCategory;
import omis.adaaccommodation.domain.Disability;
import omis.adaaccommodation.domain.DisabilityClassificationCategory;
import omis.adaaccommodation.report.AccommodationIssuanceReportService;
import omis.adaaccommodation.report.AccommodationReportService;
import omis.adaaccommodation.report.AccommodationSummary;
import omis.adaaccommodation.service.AccommodationIssuanceService;
import omis.adaaccommodation.service.AccommodationService;
import omis.adaaccommodation.service.AuthorizationService;
import omis.adaaccommodation.service.DisabilityService;
import omis.adaaccommodation.web.form.AccommodationForm;
import omis.adaaccommodation.web.form.AccommodationIssuanceForm;
import omis.adaaccommodation.web.form.AccommodationNoteItem;
import omis.adaaccommodation.web.form.AccommodationNoteItemOperation;
import omis.adaaccommodation.web.validator.AccommodationFormValidator;
import omis.adaaccommodation.web.validator.AccommodationIssuanceFormValidator;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.content.RequestContentMapping;
import omis.content.RequestContentType;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.user.domain.UserAccount;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Accommodation controller for ADA relatated operations.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jul 23, 2015)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/adaAccommodation")
@PreAuthorize("hasRole('USER')")
public class AccommodationController {

	/* Redirect view names. */

	private static final String LIST_REDIRECT_VIEW_NAME
		= "redirect:/adaAccommodation/list.html?offender=%d";
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "adaAccommodation/edit";
	
	private static final String EDIT_ISSUANCE_VIEW_NAME 
		= "adaAccommodation/editIssuance";
	
	private static final String LIST_VIEW_NAME = "adaAccommodation/list";
	
	private static final String ACCOMMODATIONS_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationsActionMenu";

	private static final String ACCOMMODATION_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationActionMenu";
	
	private static final String ACCOMMODATION_ISSUANCE_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationIssuanceActionMenu";
	
	private static final String ACCOMMODATION_NOTES_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationNotesActionMenu";
	
	private static final String ACCOMMODATION_NOTE_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationNoteActionMenu";
	
	private static final String ACCOMMODATIONS_ROW_ACTION_MENU_VIEW_NAME
		= "adaAccommodation/includes/accommodationsRowActionMenu";
	
	private static final String 
		ACCOMMODATION_ISSUANCES_ROW_ACTION_MENU_VIEW_NAME
			= "adaAccommodation/includes/accommodationIssuancesRowActionMenu";
	
	private static final String ASSOCIATED_ISSUANCES_VIEW_NAME
		= "adaAccommodation/includes/associatedIssuancesRow";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String ACCOMMODATION_FORM_MODEL_KEY 
		= "accommodationForm";
	
	private static final String ACCOMMODATION_CATEGORIES_MODEL_KEY
		= "accommodationCategories";
	
	private static final String DISABILITY_CATEGORIES_MODEL_KEY
		= "disabilityCategories";
	
	private static final String AUTHORIZATION_SOURCE_CATEGORIES_MODEL_KEY
		= "authorizationSourceCategories";
	
	private static final String CURRENT_NOTE_INDEX_MODEL_KEY 
		= "currentNoteIndex";
	
	private static final String NOTE_INDEX_MODEL_KEY = "noteIndex";
	
	private static final String ACCOMMODATION_NOTE_ITEM_MODEL_KEY
		= "accommodationNote";
	
	private static final String ACCOMMODATION_MODEL_KEY
		= "accommodation";

	private static final String ACCOMMODATION_ISSUANCE_FORM_MODEL_KEY
		= "issuanceForm";
	
	private static final String ACCOMMODATION_ISSUANCE_MODEL_KEY
		= "issuance";	
	
	private static final String ACCOMMODATION_ISSUANCES_MODEL_KEY = "issuances";
	
	private static final String ISSUANCES_EXIST_MODEL_KEY = "issuancesExist";
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME
		= "omis.adaaccommodation.msgs.form";
	
	/* Message Keys. */
	private static final String EXISTS_MESSAGE_KEY
		= "accommodation.exists";
	
	/* Report names. */
	
	private static final String ACCOMMODATION_LISTING_REPORT_NAME 
		= "/Safety/ADAAccommodations/ADA_Accommodation_Listing";

	private static final String ACCOMMODATION_DETAILS_FULL_REPORT_NAME 
		= "/Safety/ADAAccommodations/ADA_Accommodation_Details";

	private static final String ACCOMMODATION_DETAILS_REDACTED_REPORT_NAME 
		= "/Safety/ADAAccommodations/ADA_Accommodation_Details_Redacted";

	/* Report parameter names. */
	
	private static final String ACCOMMODATION_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String ACCOMMODATION_DETAILS_ID_REPORT_PARAM_NAME 
		= "ACCOMMODATION_ID";
	
	/* Services. */
	
	@Autowired
	@Qualifier("accommodationReportService")
	private AccommodationReportService accommodationReportService;
	
	@Autowired
	@Qualifier("accommodationService")
	private AccommodationService accommodationService;
	
	@Autowired
	@Qualifier("disabilityService")
	private DisabilityService disabilityService; 
	
	@Autowired
	@Qualifier("authorizationService")
	private AuthorizationService authorizationService;
	
	@Autowired
	@Qualifier("accommodationIssuanceService")
	private AccommodationIssuanceService accommodationIssuanceService;
	
	@Autowired
	@Qualifier("issuanceReportService")
	private AccommodationIssuanceReportService issuanceReportService;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("accommodationFormValidator")
	private AccommodationFormValidator accommodationFormValidator;

	@Autowired
	@Qualifier("accommodationIssuanceFormValidator")
	private AccommodationIssuanceFormValidator 
	accommodationIssuanceFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("accommodationPropertyEditorFactory")
	private PropertyEditorFactory accommodationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("accommodationNotePropertyEditorFactory")
	private PropertyEditorFactory accommodationNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("accommodationCategoryPropertyEditorFactory")
	private PropertyEditorFactory accommodationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("disabilityPropertyEditorFactory")
	private PropertyEditorFactory disabilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("disabilityCategoryPropertyEditorFactory")
	private PropertyEditorFactory disabilityCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("authorizationPropertyEditorFactory")
	private PropertyEditorFactory authorizationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("authorizationCategoryPropertyEditorFactory")
	private PropertyEditorFactory authorizationCategoryPropertyEditorFactory;

	@Autowired
	@Qualifier("accommodationIssuancePropertyEditorFactory")
	private PropertyEditorFactory accommodationIssuancePropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired	
	private CustomDateEditorFactory customDateEditorFactory;
		
	/* Constructors. */	

	/** Instantiates a default controller for ADA accommodations. */
	public AccommodationController() {
		//Default controller.
	}
	
	/* Screens. */
	
	/**
	 * Displays a list of accommodations for  a specified offender.
	 * 
	 * @param offender offender
	 * @return model and view to a list of accommodations
	 */
	@RequestContentMapping(nameKey = "accommodationListingScreenName",
			descriptionKey = "accommodationListingScreenDescription",
			messageBundle = "omis.adaaccommodation.msgs.adaAccommodation",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping("list.html")
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		List<AccommodationSummary> accommodationSummaries = 
				this.accommodationReportService
				.findByOffenderAccommodation(offender);	
		
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);	
		mav.addObject("accommodationSummaries", accommodationSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Displays issuances associated with selected accommodation.
	 * 
	 * @param accommodation accommodation
	 * @return model and view of issuances
	 */
	@RequestContentMapping(nameKey = "showAssociatedIssuancesName",
			descriptionKey = "showAssociatedIssuancesDescription",
			messageBundle = "omis.adaaccommodation.msgs.adaAccommodation",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping(value = "showAssociatedIssuances.html", 
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')")
	public ModelAndView showAssociatedIssuances(
			@RequestParam(value = "accommodation", required = true)
			final Accommodation accommodation) {
		ModelMap map = new ModelMap();
			map.addAttribute(ACCOMMODATION_ISSUANCES_MODEL_KEY, 
					this.issuanceReportService.findByAccommodation(
							accommodation));
		return new ModelAndView(ASSOCIATED_ISSUANCES_VIEW_NAME, map);
	}
	
	/**
	 * Displays a form allowing a new accommodation to be created.
	 * 
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		int noteIndex = 0;
		AccommodationForm accommodationForm = new AccommodationForm();
		accommodationForm.setTemporaryAuthorization(true);
		ModelAndView mav = this.prepareMav(accommodationForm, offender, 
				noteIndex, null);
		return mav;
	}
	
	/**
	 * Displays a new form allowing a new accommodation issuance to be created.
	 * 
	 * @param accommodation accommodation
	 * @return model and view
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "issuance/create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_ISSUANCE_CREATE') or hasRole('ADMIN')")
	public ModelAndView createIssuance(
			@RequestParam(value = "accommodation", required = true)
			final Accommodation accommodation)
				throws DuplicateEntityFoundException {
		ModelAndView mav = new ModelAndView();
		AccommodationIssuanceForm issuanceForm 
			= new AccommodationIssuanceForm();
		if (accommodation != null) {
		mav = this.prepareIssuanceMav(issuanceForm, 
				accommodation.getDisability().getOffender(), accommodation);
		} else {
			throw new IllegalStateException("Accommodation needed.");
		}
		
		return mav;
	}
	
	/**
	 * Save a new accommodaton.
	 * 
	 * @param offender offender
	 * @param accommodationForm accommodation form
	 * @param result result
	 * @return model and view to redirect list URL
	 * @throws DuplicateEntityFoundException
	 */
	@RequestContentMapping(nameKey = "accommodationCreateSubmitName",
			descriptionKey = "accommodationCreateSubmitDescription",
			messageBundle = "omis.adaaccommodation.msgs.adaAccommodation",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender, 
				final AccommodationForm accommodationForm,
				final BindingResult result) 
						throws DuplicateEntityFoundException {
		this.accommodationFormValidator.validate(accommodationForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(offender, 
					accommodationForm, result);
			return mav;
		}
		Disability newDisability = this.disabilityService.create(
				offender, accommodationForm.getDisabilityDescription(), 
				accommodationForm.getDisabilityCategory());
		Accommodation newAccommodation = this.accommodationService.create(
				newDisability, 
				accommodationForm.getAccommodationDescription(), 
				accommodationForm.getAccommodationCategory());		
		this.authorizationService.create(
				newAccommodation, accommodationForm.getAuthorizationDate(), 
				accommodationForm.getAuthorizationUser(), 
				accommodationForm.getAuthorizationSourceCategory(), 
				accommodationForm.getAuthorizationComments(), 
				accommodationForm.getStartDate(),
				accommodationForm.getEndDate());
		for (AccommodationNoteItem accommodationNoteItem
				: accommodationForm.getAccommodationNotes()) {
			if (AccommodationNoteItemOperation.CREATE.equals(
					accommodationNoteItem.getOperation())) {
				this.accommodationService.addAccommodationNote(
						newAccommodation, accommodationNoteItem.getDate(), 
						accommodationNoteItem.getText());
			} else if (AccommodationNoteItemOperation.UPDATE.equals(
					accommodationNoteItem.getOperation())) {
				this.accommodationService.updateAccommmodationNote(
						accommodationNoteItem.getAccommodationNote(), 
						accommodationNoteItem.getDate(), 
						accommodationNoteItem.getText());
			} else if (AccommodationNoteItemOperation.REMOVE.equals(
					accommodationNoteItem.getOperation())) {
				this.accommodationService.removeAccommodationNote(
						accommodationNoteItem.getAccommodationNote());
			} else {
				throw new UnsupportedOperationException(
						"Operation not supported: " 
								+ accommodationNoteItem.getOperation());
			}
	}
	return new ModelAndView(String.format(
			LIST_REDIRECT_VIEW_NAME, newDisability.getOffender().getId()));
	}
	
	/**
	 * Save a new accommodation issuance.
	 * 
	 * @param accommodation accommodation
	 * @param issuanceForm issuance form
	 * @param result result
	 * @return model and view to list redirect URL
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "issuance/create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_ISSUANCE_CREATE') or hasRole('ADMIN')")
	public ModelAndView saveIssuance(
			@RequestParam(value = "accommodation", required = true)
			final Accommodation accommodation,
			final AccommodationIssuanceForm issuanceForm,
			final BindingResult result) 
					throws DuplicateEntityFoundException {
		this.accommodationIssuanceFormValidator.validate(issuanceForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareIssuanceMav(issuanceForm, 
					accommodation.getDisability().getOffender(), accommodation);
			mav.addObject(BindingResult.MODEL_KEY_PREFIX 
							+ ACCOMMODATION_ISSUANCE_FORM_MODEL_KEY, 
							result);
			return mav;
		}
			AccommodationIssuance newIssuance = 
					this.accommodationIssuanceService
			.create(accommodation, issuanceDayTimeCombined(
					issuanceForm.getDay(), issuanceForm.getTime()), 
					issuanceForm.getIssuer(), issuanceForm.getText());
			newIssuance.setAccommodation(accommodation);
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				accommodation.getDisability().getOffender().getId()));
	}
	
	/**
	 * Adds an accommodation note to the accommodation in view.
	 * 
	 * @param noteIndex note index
	 * @return model and view to current accommodation form
	 */
	@RequestContentMapping(nameKey = "addAccommodationNoteName",
				descriptionKey = "addAccoommodationNoteDescription", 
				messageBundle = "omis.adaaccommodation.msgs.adaAccommodation",
				screenType = RequestContentType.AJAX_REQUEST)
	@RequestMapping(value = "addAccommodationNote.html",
				method = RequestMethod.GET)
	public ModelAndView addAccommodationNote(
			@RequestParam(value = "noteIndex", required = true)
			final int noteIndex) {		
		AccommodationNoteItem item = new AccommodationNoteItem();
		item.setOperation(AccommodationNoteItemOperation.CREATE);		
		ModelAndView mav = new ModelAndView(
				"adaAccommodation/includes/accommodationNoteTableRow");	
		mav.addObject(ACCOMMODATION_NOTE_ITEM_MODEL_KEY, item);
		mav.addObject(NOTE_INDEX_MODEL_KEY, noteIndex);
		return mav;
	}
	
	/**
	 * Displays a form allowing an existing accommodation to be edited.
	 * 
	 * @param accommodation accommodation
	 * @return model and view screen of existing accommodation for editing
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "accommodationSummaries", required = true)
				final Accommodation accommodation) {
		AccommodationForm accommodationForm = new AccommodationForm();		
		accommodationForm.setDisabilityCategory(
				accommodation.getDisability().getDisabilityClassification());
		accommodationForm.setDisabilityDescription(
				accommodation.getDisability().getDescription());
		accommodationForm.setAccommodationCategory(
				accommodation.getAccommodationCategory());
		accommodationForm.setAccommodationDescription(
				accommodation.getDescription());	
	
		Authorization authorization = 
				this.authorizationService
				.findAuthorizationByAccommodation(accommodation);
		if (authorization != null) {
		accommodationForm.setAuthorizationSourceCategory(
				authorization.getAuthorizationSource());
		accommodationForm.setAuthorizationComments(authorization.getComments());
		accommodationForm.setStartDate(
				authorization.getAuthorizationTerm().getStartDate());
		if (authorization.getAuthorizationTerm().getEndDate() != null) {
			accommodationForm.setTemporaryAuthorization(true);
		} else {
			accommodationForm.setTemporaryAuthorization(false);
		}
		accommodationForm.setEndDate(
				authorization.getAuthorizationTerm().getEndDate());		
		accommodationForm.setAuthorizationUser(
				authorization.getAuthorizationSignature().getUserAccount());
		accommodationForm.setAuthorizationDate(
				authorization.getAuthorizationSignature().getDate());
		}
		int currentNoteIndex = 0;
		List<AccommodationNoteItem> noteItems 
			= new ArrayList<AccommodationNoteItem>();
		List<AccommodationNote> notes = this.accommodationService
		.findAccommodationNotesByAccommodation(accommodation);
		for (AccommodationNote note : notes) {
			AccommodationNoteItem item = new AccommodationNoteItem();
			item.setOperation(AccommodationNoteItemOperation.UPDATE);
			item.setDate(note.getDate());
			item.setText(note.getText());
			item.setAccommodation(note.getAccommodation());
			item.setAccommodationNote(note);
			item.setAccommodationNoteId(note.getId());
			noteItems.add(currentNoteIndex, item);
			currentNoteIndex++;
		} 
		accommodationForm.setAccommodationNotes(noteItems);	
		ModelAndView mav = this.prepareMav(accommodationForm,
				accommodation.getDisability().getOffender(), currentNoteIndex,
				authorization);
		return mav;
	}
	
	/**
	 * Displays form allowing the current issuance to be edited.
	 * 
	 * @param issuance issuance
	 * @return model and view of existing issuance for editing
	 */
	@RequestMapping(value = "editIssuance.html",
				method = RequestMethod.GET)
	@PreAuthorize(
			"hasRole('ADA_ACCOMMODATION_ISSUANCE_VIEW') or hasRole('ADMIN')")
	public ModelAndView editIssuance(
		@RequestParam(value="issuance", required = true)
			final AccommodationIssuance issuance) {
			AccommodationIssuanceForm issuanceForm 
			= new AccommodationIssuanceForm();	
			issuanceForm.setAccommodation(issuance.getAccommodation());
			issuanceForm.setTime(issuance.getTimestamp());
			issuanceForm.setDay(issuance.getTimestamp());
			issuanceForm.setText(issuance.getDescription());
			ModelAndView mav = this.prepareIssuanceMav(issuanceForm, 
					issuance.getAccommodation().getDisability().getOffender(), 
					issuanceForm.getAccommodation());
			mav.addObject("issuance", issuance);
			return mav;
	}

	/**
	 * Updates the specified accommodation with values from 
	 * the current form view.
	 * 
	 * @param accommodation accommodation
	 * @param accommodationForm accommodation form
	 * @param result result
	 * @return model and view to redirect list URL
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "accommodationSummaries", required = true)
				final Accommodation accommodation,
				final AccommodationForm accommodationForm, 
				final BindingResult result) 
						throws DuplicateEntityFoundException {
		
	this.accommodationFormValidator.validate(accommodationForm, result);
	if (result.hasErrors()) {
		ModelAndView mav = this.prepareRedisplayMav(
				accommodation.getDisability().getOffender(), accommodationForm, 
				result);
		return mav;
	}
	Authorization authorization = 
			this.authorizationService
			.findAuthorizationByAccommodation(accommodation);
	Disability updatedDisability = this.disabilityService
			.update(accommodation.getDisability(), 
			accommodationForm.getDisabilityDescription(), 
			accommodationForm.getDisabilityCategory());
	Accommodation updatedAccommodation = this.accommodationService
			.update(accommodation, 
					accommodationForm.getAccommodationDescription(),
					accommodationForm.getAccommodationCategory());
	this.authorizationService.update(authorization, 
			accommodationForm.getAuthorizationDate(), 
			accommodationForm.getAuthorizationUser(),
			accommodationForm.getAuthorizationSourceCategory(), 
			accommodationForm.getAuthorizationComments(), 
			accommodationForm.getStartDate(),
			accommodationForm.getEndDate()); 
	for (AccommodationNoteItem accommodationNoteItem : 
		accommodationForm.getAccommodationNotes()) {
		if (AccommodationNoteItemOperation.UPDATE.equals(
				accommodationNoteItem.getOperation())) {
			this.accommodationService.updateAccommmodationNote(
					accommodationNoteItem.getAccommodationNote(), 
					accommodationNoteItem.getDate(), 
					accommodationNoteItem.getText());
		} else if (AccommodationNoteItemOperation.CREATE.equals(
				accommodationNoteItem.getOperation())) {				
			this.accommodationService.addAccommodationNote(
					updatedAccommodation, accommodationNoteItem.getDate(), 
					accommodationNoteItem.getText());
		} else if (AccommodationNoteItemOperation.REMOVE.equals(
				accommodationNoteItem.getOperation())) {
			this.accommodationService.removeAccommodationNote(
					accommodationNoteItem.getAccommodationNote());
		} else {
			throw new UnsupportedOperationException(
					"Operation not supported: " 
							+ accommodationNoteItem.getOperation());
		}
	}
	return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,  
					updatedDisability.getOffender().getId()));
	}

	/**
	 * Updates the specified issuance with values from the current view.
	 * 
	 * @param issuance issuance
	 * @param issuanceForm issuance form
	 * @param result result
	 * @return model and view to redirect list URL
	 * @throws DuplicateEntityFoundException
	 */
	@RequestMapping(value = "editIssuance.html",
				method = RequestMethod.POST)
	@PreAuthorize(
			"hasRole('ADA_ACCOMMODATION_ISSUANCE_EDIT') or hasRole('ADMIN')")		
	public ModelAndView updateIssuance(
		@RequestParam(value = "issuance", required = true) 
			final AccommodationIssuance issuance,	
			final AccommodationIssuanceForm issuanceForm,
			final BindingResult result)
				throws DuplicateEntityFoundException {
		this.accommodationIssuanceFormValidator.validate(issuanceForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareIssuanceMav(
					issuanceForm, 
					issuance.getAccommodation().getDisability().getOffender(), 
					issuance.getAccommodation());
			mav.addObject(BindingResult.MODEL_KEY_PREFIX 
					+ ACCOMMODATION_ISSUANCE_FORM_MODEL_KEY, 
					result);
			mav.addObject("issuance", issuance);	
		return mav;		
		}
		AccommodationIssuance updatedIssuance =
		this.accommodationIssuanceService.update(issuance, 
				issuanceDayTimeCombined(issuanceForm.getDay(), 
						issuanceForm.getTime()), issuanceForm.getText());

		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
			updatedIssuance.getAccommodation().getDisability()
			.getOffender().getId()));
	}
	
	/**
	 * Remove an accommodation.
	 * 
	 * @param accommodation accommodation
	 * @return model and view
	 */
	@RequestMapping(value = "remove.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "accommodation", required = true)
				final Accommodation accommodation) {
		Offender offender = accommodation.getDisability().getOffender();
		Authorization authorization =	
				this.authorizationService
				.findAuthorizationByAccommodation(accommodation);		
				this.authorizationService.remove(authorization);
		List<AccommodationIssuance> issuances =
				this.accommodationIssuanceService
				.findByAccommodation(accommodation);
		for (AccommodationIssuance issuance : issuances) {
			this.accommodationIssuanceService.remove(issuance);
		}
		List<AccommodationNote> accommodationNotes =
				this.accommodationService
				.findAccommodationNotesByAccommodation(accommodation);
		for (AccommodationNote accommodationNote : accommodationNotes) {
			this.accommodationService
			.removeAccommodationNote(accommodationNote);
		}	
		
		this.accommodationService.remove(accommodation);
		this.disabilityService.remove(accommodation.getDisability());
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				offender.getId()));
	}
	
	/**
	 * Remove an issuance.
	 * 
	 * @param issuance issuance
	 * @return model and view
	 */
	@RequestMapping(value = "removeIssuance.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_ISSUANCE_REMOVE') "
			+ "or hasRole('ADMIN')")		
	public ModelAndView removeIssuance(
		@RequestParam(value = "issuance", required = true) 
			final AccommodationIssuance issuance) {
		
		Offender offender = 
				issuance.getAccommodation().getDisability().getOffender();
		this.accommodationIssuanceService.remove(issuance);
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME, 
				offender.getId()));
	}

	/*
	 * Model and view of the accommodation form.
	 * 
	 * @param accommodationForm accommodation form
	 * @param offender offender
	 * @param noteIndex note index
	 * @return model and view
	 */
	private ModelAndView prepareMav(final AccommodationForm accommodationForm,
			final Offender offender, final int noteIndex, 
			final Authorization authorization) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);		
		mav.addObject(ACCOMMODATION_FORM_MODEL_KEY, accommodationForm);
		mav.addObject(ACCOMMODATION_CATEGORIES_MODEL_KEY, 
				this.accommodationService.findAllAccommodationCategories());
		mav.addObject(DISABILITY_CATEGORIES_MODEL_KEY, 
				this.disabilityService
				.findAllDisabilityClassificationCategories());	
		mav.addObject(AUTHORIZATION_SOURCE_CATEGORIES_MODEL_KEY, 
				this.authorizationService
				.findAllAuthorizationSourceCategories());
		mav.addObject(CURRENT_NOTE_INDEX_MODEL_KEY, noteIndex);
		mav.addObject("authorization", authorization);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Model and view of issuance form.
	 * 
	 * @param accommodationIssuanceForm accommodation issuance form
	 * @param offender offender
	 * @param accommodation accommodation
	 * @return model and view
	 */
	private ModelAndView prepareIssuanceMav(
			final AccommodationIssuanceForm accommodationIssuanceForm, 
			final Offender offender, final Accommodation accommodation) {
		ModelAndView mav = new ModelAndView(EDIT_ISSUANCE_VIEW_NAME);
		mav.addObject(ACCOMMODATION_ISSUANCE_FORM_MODEL_KEY, 
				accommodationIssuanceForm);
		mav.addObject("summarize", this.accommodationReportService
				.summarize(accommodation));
		mav.addObject(DISABILITY_CATEGORIES_MODEL_KEY, 
				this.disabilityService
				.findAllDisabilityClassificationCategories());	
		mav.addObject(AUTHORIZATION_SOURCE_CATEGORIES_MODEL_KEY, 
				this.authorizationService
				.findAllAuthorizationSourceCategories());
		mav.addObject(ACCOMMODATION_MODEL_KEY, 
				accommodationIssuanceForm.getAccommodation());
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}

	/*
	 * Model and view of the accommodation form with binding results.
	 * 
	 * @param offender offender
	 * @param accommodationForm accommodation form
	 * @param result binding results
	 * @return model and view
	 */
	private ModelAndView prepareRedisplayMav(final Offender offender, 
			final AccommodationForm accommodationForm, 
			final BindingResult result) {
		int noteIndexCount;
		if (accommodationForm.getAccommodationNotes() == null) {			
			noteIndexCount =  0;
		} else {
			noteIndexCount = accommodationForm.getAccommodationNotes().size();
		}
		ModelAndView mav = this.prepareMav(
				accommodationForm, offender, noteIndexCount, null);
		mav.addObject(
				BindingResult.MODEL_KEY_PREFIX + ACCOMMODATION_FORM_MODEL_KEY, 
				result);
		return mav;
	}
		
	/**
	 * Returns a view for an accommodation action menu pertaining to the 
	 * specified person.
	 * 
	 * @param offender offender
	 * @return model and view for accommodations action menu
	 */
	@RequestMapping(value = "/accommodationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView accommodationsActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(ACCOMMODATIONS_ACTION_MENU_VIEW_NAME, map);	
	}
	
	/**
	 * Returns a view for an accommodation action menu pertaining to the 
	 * specified person.
	 * 
	 * @param offender offender
	 * @return model and view for accommodation action menu
	 */
	@RequestMapping(value = "/accommodationActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView accommodationActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(ACCOMMODATION_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the accommodation issuance action menu.
	 * 
	 * @param accommodation accommodation
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "/accommodationIssuanceActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView accommodationIssuanceActionMenu(
			@RequestParam(value = "accommodation", required = false)
				final Accommodation accommodation,
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(ACCOMMODATION_MODEL_KEY, accommodation);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
				ACCOMMODATION_ISSUANCE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Combine the day and time for date.
	 * 
	 * @param day day
	 * @param time time
	 * @return date manipulator
	 */
	private Date issuanceDayTimeCombined(final Date day, final Date time) {
		return DateManipulator.getDateAtTimeOfDay(day, time);
	}
	
	/**
	 * Returns a view of the accommodation notes action menu.
	 * 
	 * @return model and view
	 */
	@RequestMapping(value = "/accommodationNotesActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView accommodationNotesActionMenu() {
		ModelMap map = new ModelMap();
		return new ModelAndView(ACCOMMODATION_NOTES_ACTION_MENU_VIEW_NAME, map);	
	}
	
	/**
	 * Returns a view of the accommodation note action menu.
	 * 
	 * @param accommodation accommodation
	 * @return model and view
	 */
	@RequestMapping(value = "/accommodationNoteActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView accommodationNoteActionMenu(
			@RequestParam(value = "accommodation", required = true)
				final Accommodation accommodation) {
		ModelMap map = new ModelMap();
		map.addAttribute(ACCOMMODATION_MODEL_KEY, accommodation);
		return new ModelAndView(ACCOMMODATION_NOTE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view of the accommodations row action menu.
	 * 
	 * @param offender offender
	 * @param accommodation accommodation
	 * @return model and view
	 */
	@RequestMapping(value = "/accommodationsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView accommodationsRowActionMenu(
			@RequestParam(value = "accommodationSummaries", required = true)
				final Accommodation accommodation) {
		ModelMap map = new ModelMap();
		//adding boolean to accommodation action menu
		map.addAttribute(ISSUANCES_EXIST_MODEL_KEY, 
				this.accommodationReportService.hasIssuances(accommodation));
		map.addAttribute(ACCOMMODATION_MODEL_KEY, accommodation);
		return new ModelAndView(ACCOMMODATIONS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view of the accommodation issuances row action menu.
	 * 
	 * @param issuance issuance
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "/accommodationIssuancesRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView accommodationIssuancesRowActionMenu(
			@RequestParam(value = "issuance", required = true)
				final AccommodationIssuance issuance) {
		ModelMap map = new ModelMap();
		map.addAttribute(ACCOMMODATION_ISSUANCE_MODEL_KEY, issuance);
		return new ModelAndView(
				ACCOMMODATION_ISSUANCES_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	

	/**
	 * Returns the report for the specified offenders accommodations.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/adaAccommodationListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_LIST') "
			+ "or hasRole('ADA_ACCOMMODATION_ISSUANCE_LIST')"+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAccomidationListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ACCOMMODATION_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				ACCOMMODATION_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified ada accommodation.
	 * 
	 * @param accommodation offender accommodation
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/adaAccommodationDetailsFullReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_CREATE') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAccomidationDetailsFull(@RequestParam(
			value = "accommodation", required = true)
			final Accommodation accommodation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ACCOMMODATION_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(accommodation.getId()));
		byte[] doc = this.reportRunner.runReport(
				ACCOMMODATION_DETAILS_FULL_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified ada accommodation.
	 * 
	 * @param accommodation offender caution
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/adaAccommodationDetailsRedcatedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADA_ACCOMMODATION_ISSUANCE_LIST') or hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAccomidationDetailsRedacted(@RequestParam(
			value = "accommodation", required = true)
			final Accommodation accommodation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(ACCOMMODATION_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(accommodation.getId()));
		byte[] doc = this.reportRunner.runReport(
				ACCOMMODATION_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the duplicate entity found exception.
	 * 
	 * @param exception exception
	 * @return business exception
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
			return this.businessExceptionHandlerDelegate.prepareModelAndView(
					EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
					exception); 
	}
	
	/* Init binder. */
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(Accommodation.class, 
				this.accommodationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AccommodationNote.class, 
				this.accommodationNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AccommodationCategory.class, 
				this.accommodationCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Disability.class, 
				this.disabilityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(DisabilityClassificationCategory.class, 
				this.disabilityCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Authorization.class, 
				this.authorizationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AuthorizationSourceCategory.class, 
				this.authorizationCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AccommodationIssuance.class, 
				this.accommodationIssuancePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
				this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Person.class, 
				this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				Date.class,
				"time", 
				this.customDateEditorFactory
				.createCustomTimeOnlyEditor(true));
	}
}