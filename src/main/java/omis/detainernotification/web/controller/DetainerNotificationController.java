package omis.detainernotification.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.detainernotification.domain.Detainer;
import omis.detainernotification.domain.DetainerActivityCategory;
import omis.detainernotification.domain.DetainerAgency;
import omis.detainernotification.domain.DetainerJurisdictionCategory;
import omis.detainernotification.domain.DetainerNote;
import omis.detainernotification.domain.DetainerType;
import omis.detainernotification.domain.DetainerWarrantCancellationReason;
import omis.detainernotification.domain.DetainerWarrantProcessingStatus;
import omis.detainernotification.domain.Direction;
import omis.detainernotification.domain.InterstateAgreementDetainer;
import omis.detainernotification.domain.InterstateAgreementInitiatedByCategory;
import omis.detainernotification.domain.InterstateDetainerActivity;
import omis.detainernotification.domain.component.DetainerWarrantCancellation;
import omis.detainernotification.service.DetainerService;
import omis.detainernotification.web.form.DetainerNoteItem;
import omis.detainernotification.web.form.DetainerNoteItemOperation;
import omis.detainernotification.web.form.DetainerNotificationForm;
import omis.detainernotification.web.form.DetainerNotificationItemOperation;
import omis.detainernotification.web.form.DetainerWarrantProcessingStatusFields;
import omis.detainernotification.web.form.DocumentTagItem;
import omis.detainernotification.web.form.InterstateDetainerActivityItem;
import omis.detainernotification.web.validator.DetainerNotificationFormValidator;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.io.FileRemover;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * DetainerNotificationController.java
 * 
 *@author Annie Jacques 
 *@author Joel Norris
 *@author Ryan Johns
 *@version 0.1.3 (Nov 20, 2017)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/detainerNotification/")
@PreAuthorize("hasRole('USER')")
public class DetainerNotificationController {

	/* VIEW NAMES */
	
	private static final String EDIT_VIEW_NAME = "detainerNotification/edit";
	
	private static final String DETAINER_NOTIFICATIONS_ACTION_MENU_VIEW_NAME 
		= "/detainerNotification/includes/detainerNotificationsActionMenu";
	
	private static final String DETAINER_NOTIFICATION_ACTION_MENU_VIEW_NAME 
	= "/detainerNotification/includes/detainerNotificationActionMenu";
	
	private static final String DETAINER_NOTIFICATIONS_ROW_ACTION_MENU_VIEW_NAME 
	= "/detainerNotification/includes/detainerNotificationsRowActionMenu";
	
	private static final String INTERSTATE_DETAINER_ACTIVITY_ITEM_VIEW_NAME =
			"/detainerNotification/includes/interstateDetainerActivityItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_CONTENT_VIEW_NAME =
			"/detainerNotification/includes/documentTagItemContent";
	
	private static final String DETAINER_ACTIVITY_CATEGORY_OPTIONS_VIEW_NAME =
			"/detainerNotification/includes/detainerActivityCategoryOptions";
	
	private static final String DIRECTION_OPTIONS_VIEW_NAME =
			"/detainerNotification/includes/directionOptions";
	
	private static final String DETAINER_NOTES_ACTION_MENU_VIEW_NAME
		= "/detainerNotification/includes/detainerNotesActionMenu";
	
	private static final String DETAINER_NOTE_ITEM_ROW_VIEW_NAME = 
			"/detainerNotification/includes/detainerNoteItemTableRow";
	
	private static final String COMPLEX_OPTIONS_VIEW_NAME
		= "/detainerNotification/includes/complexOptions";
	
	private static final String UNIT_OPTIONS_VIEW_NAME
		= "detainerNotification/includes/unitOptions";
	
	/* MODEL KEYS */
	
	private static final String FORM_MODEL_KEY = "detainerNotificationForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String DETAINER_MODEL_KEY = "detainer";
	
	private static final String DETAINER_TYPE_MODEL_KEY = "detainerType";
	
	private static final String DETAINER_AGENCY_MODEL_KEY = "detainerAgency";
	
	private static final String ADDRESS_UNIT_DESIGNATOR_MODEL_KEY 
		= "addressUnitDesignator";
	
	private static final String STREET_SUFFIX_MODEL_KEY = "streetSuffix";
	
	private static final String COUNTRY_MODEL_KEY = "country";
	
	private static final String STATE_MODEL_KEY = "state";
	
	private static final String INTERSTATE_DETAINER_ACTIVITY_ITEM_MODEL_KEY =
			"interstateDetainerActivityItem";

	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	
	private static final String INTERSTATE_DETAINER_ACTIVITY_ITEM_INDEX_MODEL_KEY =
			"interstateDetainerActivityItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY =
			"documentTagItemIndex";
	
	private static final String DETAINER_ACTIVITY_CATEGORIES_MODEL_KEY =
			"detainerActivityCategories";
	
	private static final String DIRECTIONS_MODEL_KEY =
			"directions";
	
	private static final String INITIATED_BY_VALUES_MODEL_KEY =
			"initiatedByValues";
	
	private static final String CANCELLATION_REASON_VALUES_MODEL_KEY =
			"cancellationReasonValues";
	
	private static final String JURISDICTION_VALUES_MODEL_KEY =
			"jurisdictionValues";
	
	private static final String FACILITIES_MODEL_KEY = "facilities";
	
	private static final String DETAINER_TERM_NOTE_ITEM_MODEL_KEY
		= "detainerNoteItem";
	
	private static final String DETAINER_NOTE_ITEM_INDEX_MODEL_KEY
		= "detainerNoteItemIndex";
	
	private static final String UNITS_MODEL_KEY = "units";
	
	private static final String COMPLEXES_MODEL_KEY = "complexes";
	
	/* PROPERTIES */
	
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = 
			"addressFields";
	
	private static final String 
		DETAINER_WARRANT_PROCESSING_STATUS_FIELDS_PROPERTY_NAME 
		= "detainerWarrantProcessingStatusFields";
	
	private static final String CREATING_DETAINER_AGENCY_PROPERTY_NAME 
		= "creatingDetainerAgency";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT 
		= "redirect:/detainerNotification/list.html?offender=%d";
	
	/* Message Keys */
		
	private static final String ENTITY_EXISTS_MESSAGE_KEY = "entity.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
		
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME
	= "omis.detainernotification.msgs.form";

	

	

	/* Services. */
	
	@Autowired
	private DetainerService detainerService;
	
		
	/* PROPERTY EDITORS */
	
	@Autowired
	@Qualifier("detainerPropertyEditorFactory")
	private PropertyEditorFactory detainerPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	OffenderPropertyEditorFactory offenderPropertyEditorFactory;

	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
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
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("streetSuffixPropertyEditorFactory")
	private PropertyEditorFactory streetSuffixPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressUnitDesignatorPropertyEditorFactory")
	private PropertyEditorFactory addressUnitDesignatorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("detainerTypePropertyEditorFactory")
	private PropertyEditorFactory detainerTypePropertyEditorFactory;
	
	@Autowired
	@Qualifier("detainerAgencyPropertyEditorFactory")
	private PropertyEditorFactory detainerAgencyPropertyEditorFactory;
	
	@Autowired
	@Qualifier("facilityPropertyEditorFactory")
	private PropertyEditorFactory facilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("interstateDetainerActivityPropertyEditorFactory")
	private PropertyEditorFactory interstateDetainerActivityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("detainerActivityCategoryPropertyEditorFactory")
	private PropertyEditorFactory detainerActivityCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("detainerNotePropertyEditorFactory")
	private PropertyEditorFactory detainerNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("complexPropertyEditorFactory")
	private PropertyEditorFactory complexPropertyEditorFactory;
	
	@Autowired
	@Qualifier("unitPropertyEditorFactory")
	private PropertyEditorFactory unitPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	@Autowired
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("detainerNotificationDocumentPersister")
	private DocumentPersister detainerNotificationDocumentPersister;
	
	@Autowired
	@Qualifier("detainerNotificationDocumentRetriever")
	private DocumentRetriever detainerNotificationDocumentRetriever;
	
	@Autowired
	@Qualifier("detainerNotificationDocumentRemover")
	private FileRemover detainerNotificationDocumentRemover;
	
	/* Validators. */
	
	@Autowired
	private DetainerNotificationFormValidator detainerNotificationFormValidator;
	
	
	
	/* Screens */
	
	/**
	 * Returns a model and view for the create detainer screen
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('DETAINER_NOTIFICATION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "offender", required = true)
		final Offender offender) {
		DetainerNotificationForm form = 
				new DetainerNotificationForm();
		DetainerWarrantProcessingStatusFields fields
		= new DetainerWarrantProcessingStatusFields();
		fields.setOtherFacility(false);
		form.setDetainerWarrantProcessingStatusFields(fields);
		return this.prepareEditMav(offender, new ModelMap(), form);
	}
	
	/**
	 * Creates a new detainer and returns user to the list screen (if no errors 
	 * are found in the form), returns to the create screen if errors are found
	 * @param offender - Offender
	 * @param form - detainer notification form
	 * @param bindingResult - binding result
	 * @return ModelAndView
	 * @throws DuplicateEntityFoundException - when a duplicate detainer or 
	 * detainer agency (if being created) already exists
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('DETAINER_NOTIFICATION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
			final Offender offender, final DetainerNotificationForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.detainerNotificationFormValidator.validate(
				form, bindingResult);
		if(bindingResult.hasErrors()){
			return this.prepareEditMav(offender, new ModelMap(),
					form);
		} else {
			this.processDetainer(null, form);
			return new ModelAndView(String.format(
					LIST_REDIRECT, offender.getId()));
		}
	}
	
	/**
	 * ModelAndView to edit a specified detainer and its child entities
	 * @param detainer - detainer
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('DETAINER_NOTIFICATION_VIEW') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value= "detainer", required = true)
		final Detainer detainer){
		DetainerNotificationForm form = 
				new DetainerNotificationForm();
		DetainerWarrantProcessingStatusFields fields
		= new DetainerWarrantProcessingStatusFields();
		fields.setOtherFacility(false);
		form.setDetainerWarrantProcessingStatusFields(fields);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute(DETAINER_MODEL_KEY, detainer);
		return this.prepareEditMav(detainer.getOffender(), modelMap,
				this.prepareForm(detainer));
	}
	
	/**
	 * Updates the specified detainer and any applicable child entities and 
	 * returns the user to the list screen (if no errors), or back to the edit 
	 * screen (if there are errors)
	 * @param detainer - detainer
	 * @param form - detainer notification form
	 * @param bindingResult - binding result
	 * @return ModelAndView
	 * @throws DuplicateEntityFoundException - when duplicate entities are found
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('DETAINER_NOTIFICATION_EDIT') or "
			+ "hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value= "detainer", required = true)
		final Detainer detainer, final DetainerNotificationForm form, 
		final BindingResult bindingResult)
				throws DuplicateEntityFoundException {
		this.detainerNotificationFormValidator.validate(
				form, bindingResult);
		if(bindingResult.hasErrors()){
			return this.prepareEditMav(detainer.getOffender(), new ModelMap(),
					form);
		} else {
			this.processDetainer(detainer, form);
			return new ModelAndView(String.format(
					LIST_REDIRECT, detainer.getOffender().getId()));
		}
	}
	
	/**
	 * Removes specified detainer and its child entities and returns the user
	 * to the list screen
	 * @param detainer - detainer
	 * @return ModelAndView
	 */
	@RequestMapping(value= "remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('DETAINER_NOTIFICATION_REMOVE')")
	public ModelAndView remove(@RequestParam(value = "detainer", required = true)
			final Detainer detainer){
		if(detainerService.findDetainerWarrantProcessingStatusByDetainer(detainer) 
				!= null){
			this.detainerService.removeDetainerWarrantProcessingStatus(
				detainerService.findDetainerWarrantProcessingStatusByDetainer(
						detainer));
		}
		InterstateAgreementDetainer interstateAgreementDetainer =
				detainerService.findInterstateAgreementDetainerByDetainer(detainer);
		if(detainerService.findInterstateAgreementDetainerByDetainer(detainer) 
				!= null){
			
			List<InterstateDetainerActivity> activities = this.detainerService
					.findDetainerActivityByInterstateAgreementDetainer(
					interstateAgreementDetainer);
			
			for(InterstateDetainerActivity activity : activities){
				if(activity.getDocument() != null){
					List<DocumentTag> tags = this.detainerService
							.findDocumentTagsByDocument(activity.getDocument());
					for(DocumentTag tag : tags){
						this.detainerService.removeDocumentTag(tag);
					}
					this.detainerNotificationDocumentRemover.remove(
							activity.getDocument().getFilename());
					this.detainerService.removeDocument(activity.getDocument());
				}
				this.detainerService.removeDetainerActivity(activity);
			}
			
			this.detainerService.removeInterstateAgreementDetainer(
				detainerService.findInterstateAgreementDetainerByDetainer(
						detainer));
		}
		this.detainerService.remove(detainer);
		return new ModelAndView(String.format(
				LIST_REDIRECT, detainer.getOffender().getId()));
	}
	
	
	/**
	 * Displays the view for an InterstateDetainerActivityItem
	 * @param interstateDetainerActivityItemIndex - Integer
	 * @param initiatedBy - InterstateAgreementInitiatedByCategory, to
	 * populate the DetainerActivityCategory list
	 * @return ModelAndView - View for an InterstateDetainerActivityItem
	 */
	@RequestMapping(value = "createInterstateDetainerActivityItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayInterstateDetainerActivityItem(
			@RequestParam(value = "interstateDetainerActivityItemIndex",
				required = true)
				final Integer interstateDetainerActivityItemIndex,
			@RequestParam(value = "initiatedBy", required = true)
				final InterstateAgreementInitiatedByCategory initiatedBy){
		ModelMap map = new ModelMap();
		
		InterstateDetainerActivityItem item =
				new InterstateDetainerActivityItem();
		
		item.setItemOperation(DetainerNotificationItemOperation.CREATE);
		item.setFileDate(new Date());
		
		map.addAttribute(DETAINER_ACTIVITY_CATEGORIES_MODEL_KEY,
				this.detainerService
				.findDetainerActivityCategoriesByInitiatedBy(initiatedBy));
		map.addAttribute(INTERSTATE_DETAINER_ACTIVITY_ITEM_MODEL_KEY, item);
		map.addAttribute(INTERSTATE_DETAINER_ACTIVITY_ITEM_INDEX_MODEL_KEY,
				interstateDetainerActivityItemIndex);
		map.addAttribute(DIRECTIONS_MODEL_KEY, Direction.values());
		
		return new ModelAndView(INTERSTATE_DETAINER_ACTIVITY_ITEM_VIEW_NAME, map);
	}
	
	/**
	 * Displays a DocumentTag item
	 * @param psychologicalDocumentItemIndex - Integer
	 * @param documentTagItemIndex - Integer
	 * @return ModelAndView - DocumentTag item content
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDocumentTagItemRow(@RequestParam(
			value = "interstateDetainerActivityItemIndex", required = true)
				final Integer interstateDetainerActivityItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setItemOperation(DetainerNotificationItemOperation.CREATE);
		map.addAttribute(INTERSTATE_DETAINER_ACTIVITY_ITEM_INDEX_MODEL_KEY,
				interstateDetainerActivityItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_CONTENT_VIEW_NAME,
				map);
	}
	
	/* Action menus */
	
	/**
	 * Returns a model and view for detainer notifications action menu
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value="/detainerNotificationsActionMenu.html", 
			method=RequestMethod.GET)
	public ModelAndView displayDetainerNotificationsActionMenu(
			@RequestParam(value="offender", required=true) 
			final Offender offender){
		ModelMap map = new ModelMap();
		
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(DETAINER_NOTIFICATIONS_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	
	/**
	 * Returns a model and view for detainer notifications rows action menu
	 * @param detainer - detainer
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value="/detainerNotificationsRowActionMenu.html",
			method=RequestMethod.GET)
	public ModelAndView displayDetainerNotificationsRowActionMenu(
		@RequestParam(value = "detainer", 
			required = true)
		final Detainer detainer, 
		@RequestParam(value = "offender", required = true) final Offender offender) {
			ModelMap map = new ModelMap();
			map.addAttribute(DETAINER_MODEL_KEY, 
						detainer);
			map.addAttribute(OFFENDER_MODEL_KEY, offender);
			return new ModelAndView(
					DETAINER_NOTIFICATIONS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view for detainer notification action menu
	 * @param offender - offender
	 * @return ModelAndView
	 */
	@RequestMapping(value="/detainerNotificationActionMenu.html", 
			method=RequestMethod.GET)
	public ModelAndView displayDetainerNotificationActionMenu(
			@RequestParam(value="offender", required=true) 
			final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(DETAINER_NOTIFICATION_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/**
	 * Returns the model and view for detainer notes action menu.
	 * 
	 * @param detainer detainer
	 * @return model and view for detainer notes action menu
	 */
	@RequestMapping(value = "/detainerNoteItemsActionMenu.html",
			method=RequestMethod.GET)
	public ModelAndView displayDetainerNotesActionMenu(@RequestParam(
			value = "detainerNoteItemIndex", required = true)
			final Integer detainerNoteItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(DETAINER_NOTE_ITEM_INDEX_MODEL_KEY,
				detainerNoteItemIndex);
		return new ModelAndView(DETAINER_NOTES_ACTION_MENU_VIEW_NAME, map);
	}
		
	/* Option Views */
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country for address fields snippet.
	 * 
	 * @param country country
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show state options
	 */
	@RequestMapping(value = "addressFieldsStateOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showAddressFieldsStateOptions(
		@RequestParam(value = "country", required = true)
			final Country country,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName) {
		List<State> states 
			= this.detainerService.findAllStates(country);
		return this.addressFieldsControllerDelegate.showStateOptions(states, 
			addressFieldsPropertyName);
	}
	
	/**
	 * Returns the city options view with a collection of cities for the
	 * specified state for address fields snippet
	 * 
	 * @param state state
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show city options
	 */
	@RequestMapping(value = "addressFieldsCityOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showAddressFieldsCityOptions(
		@RequestParam(value = "state", required = false)
			final State state,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName,
				@RequestParam(value = "country", required = false)
		final Country country) {
		if(state!=null) {
			return this.addressFieldsControllerDelegate.showCityOptions(
				this.detainerService.findCitiesByState(state),
				addressFieldsPropertyName);
		} else {
			return this.addressFieldsControllerDelegate.showCityOptions(
				this.detainerService.findCitiesByCountryWithoutState(country),
				addressFieldsPropertyName);
		}
	}
	
	
	/**
	 * Returns the zip code options view with a collection of zip codes for the
	 * specified city for address field snippet.
	 * 
	 * @param city city
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show zip code options
	 */
	@RequestMapping(value = "addressFieldsZipCodeOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showAddressFieldsZipCodeOptions(
		@RequestParam(value = "city", required = true)
			final City city,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName) {
		return this.addressFieldsControllerDelegate.showZipCodeOptions(
			this.detainerService.findZipCodes(city), 
			addressFieldsPropertyName);
	}
	
	/**
	 * Returns the view for DetainerActivityCategory options
	 * @param initiatedBy - InterstateAgreementInitiatedByCategory
	 * @return ModelAndView - View for DetainerActivityCategory options
	 */
	@RequestMapping(value = "/showDetainerActivityCategoryOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showDetainerActivityCategoryOptions(
			@RequestParam(value = "initiatedBy", required = true)
				final InterstateAgreementInitiatedByCategory initiatedBy){
		ModelMap map = new ModelMap();
		List<DetainerActivityCategory> categories = this.detainerService
				.findDetainerActivityCategoriesByInitiatedBy(initiatedBy);
		map.put(DETAINER_ACTIVITY_CATEGORIES_MODEL_KEY, categories);
		
		return new ModelAndView(DETAINER_ACTIVITY_CATEGORY_OPTIONS_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns the view for Direction options
	 * @param interstateDetainerActivityItemIndex - Integer
	 * @param category - DetainerActivityCategory
	 * @return ModelAndView - View for Direction options
	 */
	@RequestMapping(value = "/showDirectionOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showDirectionOptions(
			@RequestParam(value = "interstateDetainerActivityItemIndex",
			required = true)
			final Integer interstateDetainerActivityItemIndex,
			@RequestParam(value = "category", required = true)
				final DetainerActivityCategory category){
		ModelMap map = new ModelMap();
		List<Direction> directions = new ArrayList<Direction>();
		if(category != null){
			if(category.getSendable())
				directions.add(Direction.SENT);
			if(category.getReceivable())
				directions.add(Direction.RECEIVED);
		}
		
		map.addAttribute(DIRECTIONS_MODEL_KEY, directions);
		map.addAttribute(INTERSTATE_DETAINER_ACTIVITY_ITEM_INDEX_MODEL_KEY,
				interstateDetainerActivityItemIndex);
		
		return new ModelAndView(DIRECTION_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Displays a new detainer note item row.
	 * 
	 * @param detainerNoteItemIndex detainer term note item index
	 * @return model and view for detainer note item row
	 */
	@RequestMapping(value = "createDetainerNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDetainerNoteItemRow(@RequestParam(
			value = "detainerNoteItemIndex", required = true)
			final Integer detainerNoteItemIndex) {
		ModelMap map = new ModelMap();
		DetainerNoteItem item = new DetainerNoteItem();
		item.setOperation(DetainerNoteItemOperation.CREATE);
		map.addAttribute(DETAINER_TERM_NOTE_ITEM_MODEL_KEY, item);
		map.addAttribute(DETAINER_NOTE_ITEM_INDEX_MODEL_KEY, 
				detainerNoteItemIndex);
		return new ModelAndView(DETAINER_NOTE_ITEM_ROW_VIEW_NAME, map);
	 }
	
	/**
	 * Returns complex options for the specified facility.
	 * 
	 * @param facility facility
	 * @return model and view of complex options
	 */
	@RequestMapping(value = "complexOptions.html", method = RequestMethod.GET)
	public ModelAndView showComplexOptions(
			@RequestParam(value = "facility", required = true)
			final Facility facility) {
		ModelMap map = new ModelMap();
		map.addAttribute(COMPLEXES_MODEL_KEY, this.detainerService
				.findComplexesByFacility(facility));
		return new ModelAndView(COMPLEX_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Returns unit options for the specified facility and complex.
	 * 
	 * @param facility facility
	 * @param complex complex
	 * @return model and view of unit options
	 */
	@RequestMapping(value = "unitOptions.html", method = RequestMethod.GET)
	public ModelAndView showUnitOptions(
			@RequestParam(value = "facility", required = true)
			final Facility facility, @RequestParam(value = "complex",
			required = false) final Complex complex) {
		ModelMap map = new ModelMap();
		map.addAttribute(UNITS_MODEL_KEY, this.detainerService
				.findUnits(facility, complex));
		return new ModelAndView(UNIT_OPTIONS_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	/**
	 * Returns a prepared ModelAndView for editing/creating a Detainer
	 * @param offender - Offender
	 * @param map - ModelMap
	 * @param form - DetainerNotificationForm
	 * @return ModelAndView - prepared ModelAndView for editing/creating a
	 * Detainer
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final ModelMap map, final DetainerNotificationForm form){
		map.put(FORM_MODEL_KEY, form);
		map.put(ADDRESS_FIELDS_PROPERTY_NAME, form.getAddressFields());
		map.put(DETAINER_WARRANT_PROCESSING_STATUS_FIELDS_PROPERTY_NAME, 
			form.getDetainerWarrantProcessingStatusFields());
		map.put(CREATING_DETAINER_AGENCY_PROPERTY_NAME, 
				(form.getCreatingDetainerAgency() != null ?
						form.getCreatingDetainerAgency() : false));
		map.put(DETAINER_TYPE_MODEL_KEY,
				this.detainerService.findAllDetainerTypes());
		map.put(DETAINER_AGENCY_MODEL_KEY,
				this.detainerService.findAllDetainerAgencies());
		map.put(STREET_SUFFIX_MODEL_KEY, 
				this.detainerService.findAllStreetSuffixes());
		map.put(ADDRESS_UNIT_DESIGNATOR_MODEL_KEY, 
				this.detainerService.findAllAddressUnitDesignators());
		map.put(COUNTRY_MODEL_KEY, this.detainerService.findAllCountries());
		map.put(STATE_MODEL_KEY, this.detainerService.findAllStates(null));
		map.put(FACILITIES_MODEL_KEY, this.detainerService.findAllFacilities());
		DetainerWarrantProcessingStatusFields fields
			= form.getDetainerWarrantProcessingStatusFields(); 
		if (form.getDetainerWarrantProcessingStatusFields() != null) {
			map.addAttribute(COMPLEXES_MODEL_KEY,
					this.detainerService.findComplexesByFacility(
							fields.getFacility()));
			map.addAttribute(UNITS_MODEL_KEY,
					this.detainerService.findUnits(fields.getFacility(),
							fields.getComplex()));
		}
		if(form.getInitiatedBy() != null){
			map.addAttribute(DETAINER_ACTIVITY_CATEGORIES_MODEL_KEY,
					this.detainerService
					.findDetainerActivityCategoriesByInitiatedBy(
							form.getInitiatedBy()));
		}
		map.addAttribute(DIRECTIONS_MODEL_KEY, Direction.values());
		map.addAttribute(INITIATED_BY_VALUES_MODEL_KEY,
				InterstateAgreementInitiatedByCategory.values());
		map.addAttribute(CANCELLATION_REASON_VALUES_MODEL_KEY,
				DetainerWarrantCancellationReason.values());
		map.addAttribute(JURISDICTION_VALUES_MODEL_KEY,
				DetainerJurisdictionCategory.values());
		if(form.getInterstateDetainerActivityItems() != null){
			List<Integer> tagIndexes = new ArrayList<Integer>();
			for(int i = 0; i < form.getInterstateDetainerActivityItems().size();
					i++){
				if(form.getInterstateDetainerActivityItems().get(i)
						!= null){
					tagIndexes.add(i,
							(form.getInterstateDetainerActivityItems()
								.get(i).getDocumentTagItems() != null ?
								form.getInterstateDetainerActivityItems()
								.get(i).getDocumentTagItems().size() : 0));
				}
				else{
					tagIndexes.add(i, 0);
				}
			}
			map.addAttribute(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, tagIndexes);
		}
		final Integer noteItemIndex;
		if (form.getNoteItems() != null) {
			noteItemIndex = form.getNoteItems().size(); 
		} else {
			noteItemIndex = 0;
		}
		map.addAttribute(DETAINER_NOTE_ITEM_INDEX_MODEL_KEY, noteItemIndex);
		map.addAttribute(INTERSTATE_DETAINER_ACTIVITY_ITEM_INDEX_MODEL_KEY,
			(form.getInterstateDetainerActivityItems() != null ?
					form.getInterstateDetainerActivityItems().size() : 0));
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		final List<State> states = new ArrayList<State>();
		final List<City> cities = new ArrayList<City>();
		final List<ZipCode> zipCodes = new ArrayList<ZipCode>();
		if (form.getAddressFields() != null) {
			Country country = form.getAddressFields().getCountry();
			if (country != null) {
				states.addAll(this.detainerService.findAllStates(country));
				State state = form.getAddressFields().getState();
				if (state != null) {
					cities.addAll(this.detainerService
							.findCitiesByState(state));
					City city = form.getAddressFields().getCity();
					if (city != null) {
						zipCodes.addAll(this.detainerService
								.findZipCodes(city));
					} else  {
						zipCodes.clear();
					}
				} else {
					cities.addAll(this.detainerService
							.findCitiesByCountry(country));
				}
			} else {
				states.clear();
				cities.clear();
				zipCodes.clear();
			}
		}
		this.addressFieldsControllerDelegate.prepareEditAddressFields(map, 
				this.detainerService.findAllCountries(),
				states, cities, zipCodes,
				ADDRESS_FIELDS_PROPERTY_NAME);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns a populated form based on a Detainer
	 * @param detainer - Detainer form is being populated with
	 * @return DetainerNotificationForm - populated form
	 */
	private DetainerNotificationForm prepareForm(final Detainer detainer){
		DetainerNotificationForm form = new DetainerNotificationForm();
		
		InterstateAgreementDetainer interstateAgreementDetainer =
				this.detainerService.findInterstateAgreementDetainerByDetainer(
						detainer);
		
		DetainerWarrantProcessingStatus detainerWarrantProcessingStatus = 
				this.detainerService
					.findDetainerWarrantProcessingStatusByDetainer(detainer);
		if(interstateAgreementDetainer != null){
			List<InterstateDetainerActivity> activities = this.detainerService
					.findDetainerActivityByInterstateAgreementDetainer(
							interstateAgreementDetainer);
			List<InterstateDetainerActivityItem> activityItems =
					new ArrayList<InterstateDetainerActivityItem>();
			
			for(InterstateDetainerActivity activity : activities){
				InterstateDetainerActivityItem item =
						new InterstateDetainerActivityItem();
				
				List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
				List<DocumentTag> tags = this.detainerService
						.findDocumentTagsByDocument(activity.getDocument());
				for(DocumentTag tag : tags){
					DocumentTagItem tagItem = new DocumentTagItem();
					
					tagItem.setDocumentTag(tag);
					tagItem.setName(tag.getName());
					tagItem.setItemOperation(
							DetainerNotificationItemOperation.UPDATE);
					
					tagItems.add(tagItem);
					
				}
				if(activity.getDocument() != null){
					item.setData(this.detainerNotificationDocumentRetriever
							.retrieve(activity.getDocument()));
					item.setDocument(activity.getDocument());
					item.setFileDate(activity.getDocument().getDate());
					item.setFileExtension(activity.getDocument().getFileExtension());
					item.setTitle(activity.getDocument().getTitle());
					item.setDocumentTagItems(tagItems);
					item.setFilename(activity.getDocument().getFilename());
				}
				
				item.setActivityDate(activity.getActivityDate());
				item.setCategory(activity.getCategory());
				item.setDirection(activity.getDirection());
				item.setInterstateDetainerActivity(activity);
				item.setItemOperation(DetainerNotificationItemOperation.UPDATE);
				
				activityItems.add(item);
			}
			
			form.setProsecutorReceivedDate(interstateAgreementDetainer
					.getProsecutorReceivedDate());
			form.setInterstateDetainerActivityItems(activityItems);
			
			form.setUsingInterstateAgreementDetainer(true);
			form.setInitiatedBy(interstateAgreementDetainer.getInitiatedBy());
		}
		if(detainerWarrantProcessingStatus != null){
			form.setProcessed(true);
			DetainerWarrantCancellation cancellation = new
					DetainerWarrantCancellation();
			if(detainerWarrantProcessingStatus.getCancellation() != null){
				cancellation.setDate(detainerWarrantProcessingStatus
						.getCancellation().getDate()); 
				cancellation.setReason(detainerWarrantProcessingStatus
						.getCancellation().getReason());
			}
			final Boolean otherFacility;
			if (detainerWarrantProcessingStatus.getFacilityName() != null) {
				otherFacility = true;
			} else {
				otherFacility = false;
			}
			DetainerWarrantProcessingStatusFields fields
				= new DetainerWarrantProcessingStatusFields(
					detainerWarrantProcessingStatus.getSentToFacilityDate(), 
					detainerWarrantProcessingStatus.getFacility(),
					detainerWarrantProcessingStatus.getUnit(), 
					detainerWarrantProcessingStatus.getComplex(),
					detainerWarrantProcessingStatus.getFacilityName(),
					otherFacility,
					detainerWarrantProcessingStatus.getInmateServedDate(), 
					detainerWarrantProcessingStatus.getRefusedToSign(),
					detainerWarrantProcessingStatus.getWaiverRequired(), 
					cancellation,
					detainerWarrantProcessingStatus.getRefusedToSignComment(),
					detainerWarrantProcessingStatus.getWaiverRequiredComment());
			
			form.setDetainerWarrantProcessingStatusFields(
					fields);
		}
		
		form.setAlternateOffenderId(
				detainer.getAlternateOffenderId());
		form.setOffenseDescription(
				detainer.getOffenseDescription());
		form.setCourtCaseNumber(detainer.getCourtCaseNumber());
		form.setDetainerType(detainer.getDetainerType());
		form.setJurisdiction(detainer.getJurisdiction());
		form.setReceiveDate(detainer.getReceiveDate());
		form.setIssueDate(detainer.getIssueDate());
		form.setWarrantNumber(detainer.getWarrantNumber());
		form.setDetainerAgency(detainer.getDetainerAgency());
		List<DetainerNote> notes = this.detainerService
				.findNotesByDetainer(detainer);
		if (form.getNoteItems().isEmpty()) {
			List<DetainerNoteItem> noteItems
				= new ArrayList<DetainerNoteItem>();
			for(DetainerNote note : notes) {
				noteItems.add(new DetainerNoteItem(note.getId(), note.getValue(),
						note.getDate(), note, DetainerNoteItemOperation.UPDATE));
			}
			form.setNoteItems(noteItems);
		}
		return form;
	}
	
	/**
	 * Creates a new Detainer or updates an existing Detainer and child entities
	 * from a DetainerNotificationForm
	 * @param Detainer - Detainer to be updated
	 * @param form - DetainerNotificationForm
	 * @throws DuplicateEntityFoundException - When a Detainer, DetainerAgency,
	 * or InterstateDetainerActivity already exist with given properties
	 */
	private void processDetainer(
			Detainer detainer,
			final DetainerNotificationForm form)
			throws DuplicateEntityFoundException{
		
		DetainerAgency detainerAgency;
		//creating new detainer agency
		if (form.getCreatingDetainerAgency()) { 
			//using address
			if (form.getUsingAddress()) {
				City city;
				if (form.getAddressFields().getNewCity()) {
					city = this.detainerService.createCity(
							form.getAddressFields().getCityName(), true,
							form.getAddressFields().getState(),
							form.getAddressFields().getCountry());
				} else {
					city = form.getAddressFields().getCity();
				}
				
				ZipCode zipCode;
				if (form.getAddressFields().getNewZipCode()) {
					zipCode = this.detainerService.createZipCode(
						city, form.getAddressFields().getZipCodeValue(),
						form.getAddressFields().getZipCodeExtension(), true);
				} else {
					zipCode = form.getAddressFields().getZipCode();
				}
				
				Address address = this.detainerService.createAddress(
					form.getAddressFields().getValue(),
					zipCode); 
				
				detainerAgency = this.detainerService.createDetainerAgency(
					form.getAgencyName(), true,
					form.getTelephoneNumber(), 
					address);
			} else {
				//not using address
				detainerAgency = this.detainerService.createDetainerAgency(
					form.getAgencyName(), true, 
					form.getTelephoneNumber(), null);
			}
		} else {
			//not creating detainer agency
			detainerAgency = form.getDetainerAgency();
		}
		
		if (detainer != null) {
			detainer = this.detainerService.update(detainer,
					form.getAlternateOffenderId(), 
					form.getOffenseDescription(), 
					form.getCourtCaseNumber(), 
					form.getDetainerType(), detainerAgency,
					form.getJurisdiction(), 
					form.getReceiveDate(), 
					form.getIssueDate(),
						form.getWarrantNumber());
		} else {
			detainer = this.detainerService.create(
					form.getOffender(), 
					form.getAlternateOffenderId(), 
					form.getOffenseDescription(), 
					form.getCourtCaseNumber(), 
					form.getDetainerType(), detainerAgency,
					form.getJurisdiction(), 
					form.getReceiveDate(), 
					form.getIssueDate(),
					form.getWarrantNumber());
		}
		
		DetainerWarrantProcessingStatus detainerWarrantProcessingStatus =
				this.detainerService
				.findDetainerWarrantProcessingStatusByDetainer(detainer);
		if (Boolean.TRUE.equals(form.getProcessed())) {
			final String facilityName;
			if (form.getDetainerWarrantProcessingStatusFields() != null) {
				if (form.getDetainerWarrantProcessingStatusFields()
					.getOtherFacility() != null) {
					facilityName =
							form.getDetainerWarrantProcessingStatusFields()
								.getFacilityName();
				} else {
					facilityName = null;
				}
			} else {
				facilityName = null;
			}
			if (detainerWarrantProcessingStatus != null) {
				this.detainerService.updateDetainerWarrantProcessingStatus(
						detainerWarrantProcessingStatus, 
					form.getDetainerWarrantProcessingStatusFields()
						.getSentToFacilityDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getFacility(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getUnit(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getComplex(),
					form.getDetainerWarrantProcessingStatusFields()
						.getInmateServedDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getRefusedToSign(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getWaiverRequired(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getCancellation().getDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getCancellation().getReason(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getRefusedToSignComment(),
					form.getDetainerWarrantProcessingStatusFields()
						.getWaiverRequiredComment(), facilityName);
			} else if (form.getDetainerWarrantProcessingStatusFields()
					!= null) {
				this.detainerService
					.createDetainerWarrantProcessingStatus(detainer, 
					form.getDetainerWarrantProcessingStatusFields()
						.getSentToFacilityDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getFacility(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getUnit(),
					form.getDetainerWarrantProcessingStatusFields()
						.getComplex(),
					form.getDetainerWarrantProcessingStatusFields()
						.getInmateServedDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getRefusedToSign(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getWaiverRequired(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getCancellation().getDate(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getCancellation().getReason(), 
					form.getDetainerWarrantProcessingStatusFields()
						.getRefusedToSignComment(),
					form.getDetainerWarrantProcessingStatusFields()
						.getWaiverRequiredComment(), facilityName);
			}
		} else {
			if (detainerWarrantProcessingStatus != null) {
				this.detainerService.removeDetainerWarrantProcessingStatus(
						detainerWarrantProcessingStatus);
			}
		}
		if (form.getUsingInterstateAgreementDetainer()) {
			InterstateAgreementDetainer interstateAgreementDetainer =
					this.detainerService
					.findInterstateAgreementDetainerByDetainer(detainer);
			if (interstateAgreementDetainer != null) {
				//if updating interstate agency detainer
				this.detainerService.updateInterstateAgreementDetainer(
						detainerService
						.findInterstateAgreementDetainerByDetainer(detainer),
					form.getProsecutorReceivedDate(),
						form.getInitiatedBy());
			} else {
				//creating a new Interstate Agreement Detainer
				interstateAgreementDetainer =
					this.detainerService.createInterstateAgreementDetainer(
							detainer, form.getProsecutorReceivedDate(),
							form.getInitiatedBy());
			}
			
			if (form.getInterstateDetainerActivityItems() != null) {
				this.processItems(form.getInterstateDetainerActivityItems(),
						interstateAgreementDetainer);
			}
		} else {
			//"usingInterstateAgreementDetainer" unchecked
			//removing interstate agreement detainer if detainer originally
			//                                                         had one
			if (detainerService
				.findInterstateAgreementDetainerByDetainer(detainer) != null) {
				
				InterstateAgreementDetainer interstateAgreementDetainer =
						this.detainerService
						.findInterstateAgreementDetainerByDetainer(detainer);
				
				if (form.getInterstateDetainerActivityItems() != null) {
					List<InterstateDetainerActivityItem> items =
							new ArrayList<InterstateDetainerActivityItem>();
					List<DocumentTagItem> tagItems =
							new ArrayList<DocumentTagItem>();
					for (InterstateDetainerActivityItem item
							: form.getInterstateDetainerActivityItems()) {
						if (item.getDocumentTagItems() != null) {
							for (DocumentTagItem tagItem
									: item.getDocumentTagItems()) {
								tagItem.setItemOperation(
									DetainerNotificationItemOperation.REMOVE);
								tagItems.add(tagItem);
							}
							item.setDocumentTagItems(tagItems);
						}
						item.setItemOperation(DetainerNotificationItemOperation
								.REMOVE);
						items.add(item);
					}
					this.processItems(items, interstateAgreementDetainer);
				}
				this.detainerService.removeInterstateAgreementDetainer(
						interstateAgreementDetainer);
			}
		}
		this.processDetainerNoteItems(form.getNoteItems(), detainer);
	}
	
	/**
	 * Processes InterstateDetainerActivity items for creation, updating, or removal
	 * @param items - List of InterstateDetainActivityItems
	 * @param interstateAgreementDetainer - InterstateAgreementDetainer the items
	 * are being processed for
	 * @throws DuplicateEntityFoundException - When and InterstateDetainerActivity,
	 * Document, or DocumentTag already exist with given properties
	 */
	private void processItems(
			final List<InterstateDetainerActivityItem> items,
			final InterstateAgreementDetainer interstateAgreementDetainer)
					throws DuplicateEntityFoundException{
		for(InterstateDetainerActivityItem item : items){
			if(DetainerNotificationItemOperation.CREATE.equals(
					item.getItemOperation())){
				
				//if title is not null, then a document is being created,
				//(documents are not required)
				if(item.getTitle() != null 
						&& item.getTitle().trim().length() > 0){
					final String fileExtension = item.getFileExtension();
					this.documentFilenameGenerator
							.setExtension(fileExtension);
					final String filename =
							this.documentFilenameGenerator.generate();
					
					Document document = this.detainerService.createDocument(
							item.getFileDate(), filename, fileExtension,
							item.getTitle());
					
					this.detainerNotificationDocumentPersister
							.persist(document, item.getData());
					
					if(item.getDocumentTagItems() != null){
						for(DocumentTagItem tagItem : item.getDocumentTagItems()){
							if(DetainerNotificationItemOperation.CREATE.equals(
									tagItem.getItemOperation())){
								this.detainerService.createDocumentTag(document,
										tagItem.getName());
							}
						}
					}
					this.detainerService.createDetainerActivity(
							interstateAgreementDetainer, item.getActivityDate(),
							item.getDirection(), document, item.getCategory());
				}
				else{//No document being made
					this.detainerService.createDetainerActivity(
							interstateAgreementDetainer, item.getActivityDate(),
							item.getDirection(), null, item.getCategory());
				}
				
			}
			else if(DetainerNotificationItemOperation.UPDATE.equals(
					item.getItemOperation())){

				Document document = null;
				
				if(item.getDocument() != null){//Updating existing document
					document = this.detainerService.updateDocument(
							item.getDocument(), item.getFileDate(),
							item.getTitle());
					
					if(item.getDocumentTagItems() != null){
						for(DocumentTagItem tagItem : item.getDocumentTagItems()){
							if(DetainerNotificationItemOperation.CREATE.equals(
									tagItem.getItemOperation())){
								this.detainerService.createDocumentTag(document,
										tagItem.getName());
							}
							else if(DetainerNotificationItemOperation.UPDATE.equals(
									tagItem.getItemOperation())){
								this.detainerService.updateDocumentTag(
										tagItem.getDocumentTag(), tagItem.getName());
							}
							else if(DetainerNotificationItemOperation.REMOVE.equals(
									tagItem.getItemOperation())){
								this.detainerService.removeDocumentTag(
										tagItem.getDocumentTag());
							}
						}
					}
				}
				else{
					//Creating new document
					if(item.getTitle() != null 
							&& item.getTitle().trim().length() > 0){
						final String fileExtension = item.getFileExtension();
						this.documentFilenameGenerator
								.setExtension(fileExtension);
						final String filename =
								this.documentFilenameGenerator.generate();
						
						document = this.detainerService.createDocument(
								item.getFileDate(), filename, fileExtension,
								item.getTitle());
						
						this.detainerNotificationDocumentPersister
								.persist(document, item.getData());
						
						if(item.getDocumentTagItems() != null){
							for(DocumentTagItem tagItem : item.getDocumentTagItems()){
								if(DetainerNotificationItemOperation.CREATE.equals(
										tagItem.getItemOperation())){
									this.detainerService.createDocumentTag(document,
											tagItem.getName());
								}
							}
						}
					}
				}
				this.detainerService.updateDetainerActivity(
						item.getInterstateDetainerActivity(),
						item.getActivityDate(), item.getDirection(),
						document, item.getCategory());
			}
			else if(DetainerNotificationItemOperation.REMOVE.equals(
					item.getItemOperation())){
				this.detainerService.removeDetainerActivity(
						item.getInterstateDetainerActivity());
				for(DocumentTag tag : this.detainerService
						.findDocumentTagsByDocument(item.getDocument())){
					this.detainerService.removeDocumentTag(tag);
				}
				if(item.getDocument() != null){
					this.detainerNotificationDocumentRemover.remove(
							item.getDocument().getFilename());
					this.detainerService.removeDocument(item.getDocument());
				}
			}
		}
	}
	
	/*
	 * Processes the specified list of detainer note items
	 * according to their specified operation values.
	 * 
	 * @param items detainer note items
	 * @param detainer detainer
	 * @throws DuplicateEntityFoundException thrown when a duplicate note 
	 * is found
	 */
	private void processDetainerNoteItems(
			final List<DetainerNoteItem> items, final Detainer detainer)
		throws DuplicateEntityFoundException {
		if (items != null) {
			for (DetainerNoteItem item : items) {
				if (DetainerNoteItemOperation.CREATE
						.equals(item.getOperation())) {
					this.detainerService.createNote(detainer, 
							item.getDate(), item.getValue());
				} else if (DetainerNoteItemOperation.UPDATE
						.equals(item.getOperation())) {
					if(this.isNoteChanged(item.getNote(), item.getDate(),
							item.getValue())) {
						this.detainerService.updateNote(
								item.getNote(), item.getDate(),
								item.getValue());
					}
				} else if (DetainerNoteItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.detainerService.removeNote(
							item.getNote());
				}
			}
		}
	}
	
	/**
	 * Checks if a Detainer Note has been changed and returns true if it has
	 * @param note - Detainer Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is different
	 * from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final DetainerNote note,
			final Date date, final String value) {
		if(!note.getValue().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 *  Retrieves document file.
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response.
	 */
	@RequestMapping(value = "retrieveFile.html", method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.detainerNotificationDocumentRetriever
				.retrieve(document);
		httpServletResponse.setContentType("application/octet-stream");
		httpServletResponse.setHeader("Content-Disposition", 
				"attachment; filename=\"" + document.getFilename() + "\"");
		try {
			OutputStream outputStream = httpServletResponse.getOutputStream();
			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
		} catch (IOException ioException) {
			throw new RuntimeException(String.format(ERROR_WRITING_FILE_MSG, 
					document.getFilename()));
		}
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
	
	
	
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Detainer.class, 
				this.detainerPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Country.class, 
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class, 
			this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class, 
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class, 
				this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class, 
				this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class, 
				this.addressUnitDesignatorPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(DetainerType.class, 
				this.detainerTypePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DetainerAgency.class, 
				this.detainerAgencyPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Facility.class, 
				this.facilityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Complex.class, 
				this.complexPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Unit.class, 
				this.unitPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DetainerActivityCategory.class, 
				this.detainerActivityCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(InterstateDetainerActivity.class, 
				this.interstateDetainerActivityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(
				DetainerNote.class,
				this.detainerNotePropertyEditorFactory
				.createPropertyEditor());
	}
}
