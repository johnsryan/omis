package omis.violationevent.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.condition.domain.Condition;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.exception.DuplicateEntityFoundException;
import omis.incident.domain.IncidentStatement;
import omis.io.FileRemover;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.substancetest.domain.SubstanceTest;
import omis.supervision.domain.SupervisoryOrganization;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.ViolationEventDocument;
import omis.violationevent.domain.ViolationEventNote;
import omis.violationevent.service.ViolationEventService;
import omis.violationevent.web.form.ConditionViolationItem;
import omis.violationevent.web.form.DisciplinaryCodeViolationItem;
import omis.violationevent.web.form.JurisdictionFilter;
import omis.violationevent.web.form.ViolationEventDocumentItem;
import omis.violationevent.web.form.ViolationEventForm;
import omis.violationevent.web.form.ViolationEventItemOperation;
import omis.violationevent.web.form.ViolationEventNoteItem;
import omis.violationevent.web.validator.ViolationEventFormValidator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * ViolationEventController.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Aug 30, 2017)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/violationEvent/")
@PreAuthorize("hasRole('USER')")
public class ViolationEventController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/violationEvent/edit";
	
	private static final String VIOLATION_EVENT_ACTION_MENU_VIEW_NAME =
			"/violationEvent/includes/violationEventActionMenu";
	
	private static final String
		DISCIPLINARY_CODE_VIOLATION_ITEMS_ACTION_MENU_VIEW_NAME =
			"/violationEvent/includes/disciplinaryCodeViolationItemsActionMenu";
	
	private static final String CONDITION_VIOLATION_ITEMS_ACTION_MENU_VIEW_NAME =
			"/violationEvent/includes/conditionViolationItemsActionMenu";
	
	private static final String VIOLATION_EVENT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
			= "/violationEvent/includes/violationEventNoteItemsActionMenu";
	
	private static final String DISCIPLINARY_CODE_OPTIONS_VIEW_NAME =
			"/violationEvent/includes/disciplinaryCodeOptions";
	
	private static final String DISCIPLINARY_CODE_DESCRIPTION_VIEW_NAME =
			"/violationEvent/includes/disciplinaryCodeDescription";
	
	private static final String CONDITION_OPTIONS_VIEW_NAME =
			"violationEvent/includes/conditionOptions";
	
	private static final String CONDITION_DESCRIPTION_VIEW_NAME =
			"violationEvent/includes/conditionDescription";
	
	private static final String JURISDICTION_OPTIONS_VIEW_NAME =
			"/violationEvent/includes/jurisdictionOptions";
	
	private static final String DISCIPLINARY_CODE_VIOLATION_ITEM_ROW_VIEW_NAME =
			"/violationEvent/includes/disciplinaryCodeViolationTableRow";
	
	private static final String CONDITION_VIOLATION_ITEM_ROW_VIEW_NAME =
			"/violationEvent/includes/conditionViolationTableRow";
	
	private static final String VIOLATION_EVENT_NOTE_ITEM_ROW_VIEW_NAME =
			"/violationEvent/includes/violationEventNoteTableRow";
	
	private static final String VIOLATION_EVENT_DOCUMENT_ITEM_ROW_VIEW_NAME =
			"/violationEvent/includes/violationEventDocumentItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_ROW_VIEW_NAME =
			"/violationEvent/includes/documentTagItemContent";
	
	/* Model Keys */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String VIOLATION_EVENT_MODEL_KEY = "violationEvent";
	
	private static final String FORM_MODEL_KEY = "violationEventForm";
	
	private static final String CATEGORY_MODEL_KEY = "category";
	
	private static final String CATEGORY_NAME_MODEL_KEY = "categoryName";
	
	private static final String DISCIPLINARY_CODE_VIOLATION_ITEM_INDEX_MODEL_KEY
			= "disciplinaryCodeViolationItemIndex";
	
	private static final String CONDITION_VIOLATION_ITEM_INDEX_MODEL_KEY
			= "conditionViolationItemIndex";
	
	private static final String VIOLATION_EVENT_NOTE_ITEM_INDEX_MODEL_KEY = 
			"violationEventNoteItemIndex";
	
	private static final String VIOLATION_EVENT_DOCUMENT_ITEM_INDEX_MODEL_KEY =
			"violationEventDocumentItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY =
			"documentTagItemIndex";
	
	private static final String JURISDICTIONS_MODEL_KEY = "jurisdictions";
	
	private static final String JURISDICTION_MODEL_KEY = "jurisdiction";
	
	private static final String DISCIPLINARY_CODES_MODEL_KEY =
			"disciplinaryCodes";
	
	private static final String CONDITIONS_MODEL_KEY = "conditions";
	
	private static final String DISCIPLINARY_CODE_DESCRIPTION_MODEL_KEY =
			"disciplinaryCodeDescription";
	
	private static final String DISCIPLINARY_CODE_VIOLATION_ITEM_MODEL_KEY =
			"disciplinaryCodeViolationItem";
	
	private static final String CONDITION_DESCRIPTION_MODEL_KEY =
			"conditionDescription";
	
	private static final String CONDITION_VIOLATION_ITEM_MODEL_KEY =
			"conditionViolationItem";
	
	private static final String VIOLATION_EVENT_NOTE_ITEM_MODEL_KEY =
			"violationEventNoteItem";
	
	private static final String VIOLATION_EVENT_DOCUMENT_ITEM_MODEL_KEY =
			"violationEventDocumentItem";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	
	private static final String POSITIVE_SUBSTANCE_TEST_MSG_KEY =
			"Positive Substance Test Result";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT =
			"redirect:/violationEvent/list.html?offender=%d";
	
	/* Message Keys */
		
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"entity.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
    /* Report names */
	
	private static final String VIOLATION_EVENT_LISTING_REPORT_NAME 
		= "/Compliance/ViolationEvents/Violation_Event_Listing";

	private static final String VIOLATION_EVENT_DETAILS_REPORT_NAME 
		= "/Compliance/ViolationEvents/Violation_Event_Details";

	/* Report parameter names */
	
	private static final String VIOLATION_EVENT_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String VIOLATION_EVENT_DETAILS_ID_REPORT_PARAM_NAME 
		= "VIOLATION_EVENT_ID";
	
	/* Report runners */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.violationevent.msgs.form";
	
	/* Service */
	
	@Autowired
	@Qualifier("violationEventService")
	private ViolationEventService violationEventService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("violationEventPropertyEditorFactory")
	private PropertyEditorFactory violationEventPropertyEditorFactory;
	
	@Autowired
	@Qualifier("violationEventNotePropertyEditorFactory")
	private PropertyEditorFactory violationEventNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("violationEventDocumentPropertyEditorFactory")
	private PropertyEditorFactory violationEventDocumentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("disciplinaryCodeViolationPropertyEditorFactory")
	private PropertyEditorFactory disciplinaryCodeViolationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("conditionViolationPropertyEditorFactory")
	private PropertyEditorFactory conditionViolationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("disciplinaryCodePropertyEditorFactory")
	private PropertyEditorFactory disciplinaryCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("conditionPropertyEditorFactory")
	private PropertyEditorFactory conditionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("supervisoryOrganizationPropertyEditorFactory")
	private PropertyEditorFactory supervisoryOrganizationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("incidentStatementPropertyEditorFactory")
	private PropertyEditorFactory incidentStatementPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceTestPropertyEditorFactory")
	private PropertyEditorFactory substanceTestPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("violationEventDocumentPersister")
	private DocumentPersister violationEventDocumentPersister;
	
	@Autowired
	@Qualifier("violationEventDocumentRetriever")
	private DocumentRetriever violationEventDocumentRetriever;
	
	@Autowired
	@Qualifier("violationEventDocumentRemover")
	private FileRemover violationEventDocumentRemover;	
	
	/* Validator */
	
	@Autowired
	@Qualifier("violationEventFormValidator")
	private ViolationEventFormValidator violationEventFormValidator;
	
	/**
	 * Default Constructor for ViolationEventController
	 */
	public ViolationEventController() {
	}
	
	/* Views */
	
	/**
	 * Returns a model and view for violationEvent creation
	 * @param offender - Offender
	 * @param category - ViolationEventCategory
	 * @param incidentStatement - IncidentStatement (optional to prepopulate
	 * some form properties)
	 * @param substanceTest - SubstanceTest (optional to prepopulate some
	 * form properties)
	 * @return ModelAndView - model and view for violationEvent creation
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_EVENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value="category", required=true)
				final ViolationEventCategory category,
			@RequestParam(value="incidentStatement", required=false)
				final IncidentStatement incidentStatement,
			@RequestParam(value="substanceTest", required=false)
				final SubstanceTest substanceTest){
		
		ViolationEventForm form = new ViolationEventForm();
		
		if(incidentStatement != null){
			List<SupervisoryOrganization> supervisoryOrganizations =
					this.violationEventService.findSupervisoryOrganizations();
			
			form.setEventDate(incidentStatement.getIncidentDate());
			form.setEventDetails(incidentStatement.getSummary());
			SupervisoryOrganization jurisdiction = (SupervisoryOrganization)
					incidentStatement.getJurisdiction().getOrganization();
			if(supervisoryOrganizations.contains(jurisdiction)){
				form.setJurisdiction(jurisdiction);
			}
		}
		if(substanceTest != null){
			form.setEventDate(substanceTest.getResultDate());
			form.setEventDetails(POSITIVE_SUBSTANCE_TEST_MSG_KEY);
		}
		
		return prepareEditMav(offender, category, form, new ModelMap());
	}
	
	/**
	 * Saves a ViolationEvent and returns to the list screen
	 * @param offender - Offender
	 * @param category - ViolationEventCategory
	 * @param form - VioaltionEventForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - back to the create screen on validation error,
	 * or to the list screen on successful save
	 * @throws DuplicateEntityFoundException - when ViolationEvent, 
	 * ViolationEventNote, ViolationEventDocument, DocumentTag,
	 * DisciplinaryCodeViolation, or ConditionViolation already exist
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('VIOLATION_EVENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value="category", required=true)
				final ViolationEventCategory category,
				final ViolationEventForm form,
				final BindingResult bindingResult)
						throws DuplicateEntityFoundException{
		
		this.violationEventFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			return prepareEditMav(offender, category, form, new ModelMap());
		}
		else{
			ViolationEvent violationEvent = this.violationEventService
					.createViolationEvent(offender, form.getJurisdiction(),
							form.getEventDate(), form.getEventDetails(),
							category);
			
			this.processItems(violationEvent, form);
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					offender.getId()));
		}
	}
	
	
	/**
	 * Returns a model and view for violationEvent editing
	 * @param violationEvent - ViolationEvent being edited
	 * @param category - ViolationEventCategory
	 * @return ModelAndView - model and view for violationEvent editing
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "violationEvent", required = true)
				final ViolationEvent violationEvent,
			@RequestParam(value="category", required=true)
				final ViolationEventCategory category){
		
		ModelMap map = new ModelMap();
		map.addAttribute(VIOLATION_EVENT_MODEL_KEY, violationEvent);
		return prepareEditMav(violationEvent.getOffender(), category,
				prepareEditForm(violationEvent), map);
	}
	
	/**
	 * Updates a ViolationEvent and returns to the list screen
	 * @param violationEvent - ViolationEvent being updated
	 * @param category - ViolationEventCategory
	 * @param form - ViolationEventForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - back to ViolationEvent Edit screen on form
	 * validation error, or to the list screen on successful update
	 * @throws DuplicateEntityFoundException - when ViolationEvent, 
	 * ViolationEventNote, ViolationEventDocument, DocumentTag,
	 * DisciplinaryCodeViolation, or ConditionViolation already exist
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('VIOLATION_EVENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
				@RequestParam(value = "violationEvent", required = true)
					final ViolationEvent violationEvent,
				@RequestParam(value="category", required=true)
					final ViolationEventCategory category,
					final ViolationEventForm form,
					final BindingResult bindingResult)
							throws DuplicateEntityFoundException{
		this.violationEventFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(VIOLATION_EVENT_MODEL_KEY, violationEvent);
			return prepareEditMav(violationEvent.getOffender(), category, form,
					map);
		}
		else{
			this.processItems(violationEvent, form);
			
			this.violationEventService.updateViolationEvent(
						violationEvent, form.getJurisdiction(),
						form.getEventDate(), form.getEventDetails(),
						category);
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					violationEvent.getOffender().getId()));
		}
	}
	
	/**
	 * Removes a violationEvent and its child entities and returns to the
	 * violationEvent list screen
	 * @param violationEvent - ViolationEvent to remove
	 * @return ModelAndView - redirect to violationEvent list screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_EVENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "violationEvent", required = true)
				final ViolationEvent violationEvent){
		
		List<DisciplinaryCodeViolation> disciplinaryCodeViolations =
				this.violationEventService
				.findDisciplinaryCodeViolationsByViolationEvent(violationEvent);
		
		List<ConditionViolation> conditionViolations =
				this.violationEventService
				.findConditionViolationsByViolationEvent(violationEvent);
		
		List<ViolationEventNote> violationEventNotes =
				this.violationEventService
				.findViolationEventNotesByViolationEvent(violationEvent);
		
		List<ViolationEventDocument> violationEventDocuments =
				this.violationEventService
				.findViolationEventDocumentsByViolationEvent(violationEvent);
		
		for(DisciplinaryCodeViolation violation : disciplinaryCodeViolations){
			this.violationEventService.removeDisciplinaryCodeViolation(violation);
		}
		
		for(ConditionViolation violation : conditionViolations){
			this.violationEventService.removeConditionViolation(violation);
		}
		
		for(ViolationEventNote note : violationEventNotes){
			this.violationEventService.removeViolationEventNote(note);
		}
		
		for(ViolationEventDocument violationEventDocument :
				violationEventDocuments){
			List<DocumentTag> tags = this.violationEventService
					.findDocumentTagsByDocument(
							violationEventDocument.getDocument());
			
			for(DocumentTag tag : tags){
				this.violationEventService.removeDocumentTag(tag);
			}
			
			this.violationEventService.removeViolationEventDocument(
					violationEventDocument);
			this.violationEventDocumentRemover.remove(
					violationEventDocument.getDocument().getFilename());
			this.violationEventService.removeDocument(
					violationEventDocument.getDocument());
		}
		
		this.violationEventService.removeViolationEvent(violationEvent);
		
		return new ModelAndView(String.format(LIST_REDIRECT,
				violationEvent.getOffender().getId()));
	}
	
	/* Item Row Views */
	
	/**
	 * Displays a disciplinary code violation item row
	 * @param disciplinaryCodeViolationItemIndex - Integer
	 * @param jurisdiction - SupervisoryOrganization
	 * @param eventDate - Date
	 * @return ModelAndView - view for a Disciplinary Code Violation item row
	 */
	@RequestMapping(value = "createDisciplinaryCodeViolationItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDisciplinaryCodeViolationItemRow(@RequestParam(
			value = "disciplinaryCodeViolationItemIndex", required = true)
				final Integer disciplinaryCodeViolationItemIndex,
			@RequestParam(value = "jurisdiction", required = true)
				final SupervisoryOrganization jurisdiction,
			@RequestParam(value = "eventDate", required = true)
				final Date eventDate) {
		ModelMap map = new ModelMap();
		DisciplinaryCodeViolationItem noteItem =
				new DisciplinaryCodeViolationItem();
		noteItem.setItemOperation(ViolationEventItemOperation.CREATE);
		map.addAttribute(DISCIPLINARY_CODE_VIOLATION_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(DISCIPLINARY_CODE_VIOLATION_ITEM_INDEX_MODEL_KEY,
				disciplinaryCodeViolationItemIndex);
		List<DisciplinaryCode> disciplinaryCodes = this.violationEventService
				.findDisciplinaryCodesByJurisdictionAndEventDate(
						jurisdiction, eventDate);
		map.put(DISCIPLINARY_CODES_MODEL_KEY, disciplinaryCodes);
		return new ModelAndView(DISCIPLINARY_CODE_VIOLATION_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays a condition violation item row
	 * @param conditionViolationItemIndex - Itenger
	 * @param offender - Offender
	 * @param eventDate - Date
	 * @return ModelAndView - view for a condition violation item row
	 */
	@RequestMapping(value = "createConditionViolationItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayConditionCodeViolationItemRow(@RequestParam(
			value = "conditionViolationItemIndex", required = true)
				final Integer conditionViolationItemIndex,
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "eventDate", required = true)
				final Date eventDate) {
		ModelMap map = new ModelMap();
		ConditionViolationItem noteItem =
				new ConditionViolationItem();
		noteItem.setItemOperation(ViolationEventItemOperation.CREATE);
		map.addAttribute(CONDITION_VIOLATION_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(CONDITION_VIOLATION_ITEM_INDEX_MODEL_KEY,
				conditionViolationItemIndex);
		List<Condition> conditions = this.violationEventService
				.findConditionsByOffenderAndEventDate(offender, eventDate);
		map.put(CONDITIONS_MODEL_KEY, conditions);
		return new ModelAndView(CONDITION_VIOLATION_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays a violation event note item row
	 * @param violationEventNoteItemIndex - Integer
	 * @return ModelAndView - view for a violation event note item row
	 */
	@RequestMapping(value = "createViolationEventNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayViolationEventNoteItemRow(@RequestParam(
			value = "violationEventNoteItemIndex", required = true)
				final Integer violationEventNoteItemIndex) {
		ModelMap map = new ModelMap();
		ViolationEventNoteItem noteItem = new ViolationEventNoteItem();
		noteItem.setItemOperation(ViolationEventItemOperation.CREATE);
		map.addAttribute(VIOLATION_EVENT_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(VIOLATION_EVENT_NOTE_ITEM_INDEX_MODEL_KEY,
				violationEventNoteItemIndex);
		return new ModelAndView(VIOLATION_EVENT_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Displays a violation event Document item row
	 * @param violationEventDocumentItemIndex - Integer
	 * @return ModelAndView - view for a violation event Document item row
	 */
	@RequestMapping(value = "createViolationEventDocumentItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayViolationEventDocumentItemRow(@RequestParam(
			value = "violationEventDocumentItemIndex", required = true)
				final Integer violationEventDocumentItemIndex) {
		ModelMap map = new ModelMap();
		ViolationEventDocumentItem noteItem = new ViolationEventDocumentItem();
		noteItem.setItemOperation(ViolationEventItemOperation.CREATE);
		map.addAttribute(VIOLATION_EVENT_DOCUMENT_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(VIOLATION_EVENT_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				violationEventDocumentItemIndex);
		return new ModelAndView(VIOLATION_EVENT_DOCUMENT_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays a Document Tag item row
	 * @param documentTagItemIndex - Integer
	 * @return ModelAndView - view for a Document Tag item row
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDocumentTagItemRow(@RequestParam(
			value = "violationEventDocumentItemIndex", required = true)
				final Integer violationEventDocumentItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setDocumentTagOperation(DocumentTagOperation.CREATE);
		map.addAttribute(VIOLATION_EVENT_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				violationEventDocumentItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/* Action Menu Views */
	
	/**
	 * Returns the model and view for violationEvent action menu
	 * @param offender - Offender
	 * @return ModelAndView - model and view for violationEvent action menu
	 */
	@RequestMapping(value = "/violationEventActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayViolationEventActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(VIOLATION_EVENT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view of disciplinary code violation items action menu
	 * @return ModelAndView - model and view of disciplinary code violation items
	 * action menu
	 */
	@RequestMapping(value = "disciplinaryCodeViolationItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayDisciplinaryCodeViolationItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				DISCIPLINARY_CODE_VIOLATION_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view of condition violation items action menu
	 * @return ModelAndView - model and view of condition violation items
	 * action menu
	 */
	@RequestMapping(value = "conditionViolationItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayConditionViolationItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				CONDITION_VIOLATION_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view of violation event note items action menu
	 * @return ModelAndView - model and view of violation event note items
	 * action menu
	 */
	@RequestMapping(value = "violationEventNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayViolationEventNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				VIOLATION_EVENT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* AJAX views */
	
	/**
	 * Displays the jurisdiction options
	 * @param secureJurisdiction - Boolean, whether to filter by secure
	 * jurisdictions or not
	 * @param currentJurisdiction - SupervisoryOrganization, current jurisdiction
	 * being used on the form
	 * @return ModelAndView - jurisdiction options
	 */
	@RequestMapping(value = "/showJurisdictionOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showJurisdictionOptions(
			@RequestParam(value = "jurisdictionFilter", required = true)
				final JurisdictionFilter jurisdictionFilter,
			@RequestParam(value = "currentJurisdiction", required = false)
				final SupervisoryOrganization currentJurisdiction){
		ModelMap map = new ModelMap();
		
		List<SupervisoryOrganization> jurisdictions =
				new ArrayList<SupervisoryOrganization>();
		
		switch(jurisdictionFilter){
		case SECURE_FACILITY:
			jurisdictions = this.violationEventService
				.findFacilitySupervisoryOrganizations();
			break;
		case TREATMENT_CENTER:
			jurisdictions = this.violationEventService
				.findTreatmentCenterSupervisoryOrganizations();
			break;
		case PRERELEASE_CENTER:
			jurisdictions = this.violationEventService
				.findPreReleaseCenterSupervisoryOrganizations();
			break;
		case COMMUNITY_SUPERVISION_OFFICE:
			jurisdictions = this.violationEventService
				.findCommunitySupervisionOfficeSupervisoryOrganizations();
			break;
		case ASSESSMENT_SANCTION_AND_RECOVATION_CENTER:
			jurisdictions = this.violationEventService
			.findAssessmentSanctionRevocationCenterSupervisoryOrganizations();
			break;
		default:
			jurisdictions = this.violationEventService
				.findSupervisoryOrganizations();
			break;
		}
			
		if(currentJurisdiction != null){
			map.addAttribute(JURISDICTION_MODEL_KEY, currentJurisdiction);
		}
		map.addAttribute(JURISDICTIONS_MODEL_KEY, jurisdictions);
		
		return new ModelAndView(JURISDICTION_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Displays the disciplinary code options based on given jurisdiction
	 * and event date
	 * @param jurisdiction - SupervisoryOrganization
	 * @param eventDate - Date
	 * @return ModelAndView - DisciplinaryCode options
	 */
	@RequestMapping(value = "/showDisciplinaryCodeOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showDisciplinaryCodeOptions(
			@RequestParam(value = "jurisdiction", required = true)
				final SupervisoryOrganization jurisdiction,
			@RequestParam(value = "eventDate", required = true)
				final Date eventDate){
		ModelMap map = new ModelMap();
		List<DisciplinaryCode> disciplinaryCodes = this.violationEventService
				.findDisciplinaryCodesByJurisdictionAndEventDate(
						jurisdiction, eventDate);
		map.put(DISCIPLINARY_CODES_MODEL_KEY, disciplinaryCodes);
		
		return new ModelAndView(DISCIPLINARY_CODE_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Displays the description of the selected disciplinary code
	 * @param disciplinaryCode - DisciplinaryCode
	 * @return ModelAndView - View of DisciplinaryCode description
	 */
	@RequestMapping(value = "/showDisciplinaryCodeDescription.html",
			method = RequestMethod.GET)
	public ModelAndView showDisciplinaryCodeDescription(
			@RequestParam(value = "disciplinaryCode", required = true)
				final DisciplinaryCode disciplinaryCode){
		ModelMap map = new ModelMap();
		
		map.put(DISCIPLINARY_CODE_DESCRIPTION_MODEL_KEY,
				disciplinaryCode.getDescription());
		
		return new ModelAndView(DISCIPLINARY_CODE_DESCRIPTION_VIEW_NAME, map);
	}
	
	/**
	 * Displays the condition options based on current Offender and Event Date
	 * @param offender - Offender
	 * @param eventDate - Date
	 * @return ModelAndView - Condition options
	 */
	@RequestMapping(value = "/showConditionOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showConditionOptions(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "eventDate", required = true)
				final Date eventDate){
		ModelMap map = new ModelMap();
		List<Condition> conditions = this.violationEventService
				.findConditionsByOffenderAndEventDate(
						offender, eventDate);
		map.put(CONDITIONS_MODEL_KEY, conditions);
		return new ModelAndView(CONDITION_OPTIONS_VIEW_NAME, map);
	}
	
	/**
	 * Displays the description of the selected condition
	 * @param condition - Condition
	 * @return ModelAndView - view of Condition description
	 */
	@RequestMapping(value = "/showConditionDescription.html",
			method = RequestMethod.GET)
	public ModelAndView showConditionDescription(
			@RequestParam(value = "condition", required = true)
				final Condition condition){
		ModelMap map = new ModelMap();
		
		map.put(CONDITION_DESCRIPTION_MODEL_KEY,
				condition.getConditionClause().getDescription());
		
		return new ModelAndView(CONDITION_DESCRIPTION_VIEW_NAME, map);
	}
	
	/* Reports */
	
	/**
	 * Returns the report for the specified offenders violation events.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/violationEventListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationEventListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VIOLATION_EVENT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VIOLATION_EVENT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified violation event.
	 * 
	 * @param violationEvent violation event
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/violationEventDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationEventDetails(@RequestParam(
			value = "violationEvent", required = true)
			final ViolationEvent violationEvent,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VIOLATION_EVENT_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(violationEvent.getId()));
		byte[] doc = this.reportRunner.runReport(
				VIOLATION_EVENT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Helper Methods */
	
	/**
	 * Prepares and returns a ModelAndView for ViolationEvent creation/editing
	 * @param offender - Offender
	 * @param category - ViolationEventCategory
	 * @param form - ViolationEventForm
	 * @param map - ModelMap
	 * @return ModelAndView - Prepared ModelAndView for ViolationEvent
	 * creation/editing
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final ViolationEventCategory category,
			final ViolationEventForm form, final ModelMap map){
		
		
		List<SupervisoryOrganization> jurisdictions =
				new ArrayList<SupervisoryOrganization>();
		if(form.getJurisdiction() != null && ViolationEventCategory.DISCIPLINARY
				.equals(category) && form.getEventDate() != null){
			if (form.getJurisdiction() instanceof SupervisoryOrganization) {
				map.addAttribute(DISCIPLINARY_CODES_MODEL_KEY,
						this.violationEventService
						.findDisciplinaryCodesByJurisdictionAndEventDate(
								(SupervisoryOrganization) form
								.getJurisdiction(), form.getEventDate()));
			} else {
				map.addAttribute(DISCIPLINARY_CODES_MODEL_KEY, null);
			}
		}
		else if(form.getEventDate() != null && ViolationEventCategory.SUPERVISION
				.equals(category)){
			map.addAttribute(CONDITIONS_MODEL_KEY, this.violationEventService
					.findConditionsByOffenderAndEventDate(offender,
							form.getEventDate()));
		}
		if(JurisdictionFilter.SECURE_FACILITY.equals(form.getJurisdictionFilter())){
			jurisdictions = this.violationEventService
					.findFacilitySupervisoryOrganizations();
		}
		else if(JurisdictionFilter.TREATMENT_CENTER.equals(
				form.getJurisdictionFilter())){
			jurisdictions = this.violationEventService
					.findTreatmentCenterSupervisoryOrganizations();
		}
		else if(JurisdictionFilter.PRERELEASE_CENTER.equals(
				form.getJurisdictionFilter())){
			jurisdictions = this.violationEventService
					.findPreReleaseCenterSupervisoryOrganizations();
		}
		else if(JurisdictionFilter.COMMUNITY_SUPERVISION_OFFICE
				.equals(form.getJurisdictionFilter())){
			jurisdictions = this.violationEventService
					.findCommunitySupervisionOfficeSupervisoryOrganizations();
		}
		else if(JurisdictionFilter.ASSESSMENT_SANCTION_AND_RECOVATION_CENTER
				.equals(form.getJurisdictionFilter())){
			jurisdictions = this.violationEventService
					.findAssessmentSanctionRevocationCenterSupervisoryOrganizations();
		}
		else {
			if(ViolationEventCategory.SUPERVISION.equals(category)){
				jurisdictions = this.violationEventService
						.findCommunitySupervisionOfficeSupervisoryOrganizations();
				form.setJurisdictionFilter(
						JurisdictionFilter.COMMUNITY_SUPERVISION_OFFICE);
			}
			else if(ViolationEventCategory.DISCIPLINARY.equals(category)){
				jurisdictions = this.violationEventService
						.findFacilitySupervisoryOrganizations();
				form.setJurisdictionFilter(
						JurisdictionFilter.SECURE_FACILITY);
			}
			else{
				throw new UnsupportedOperationException(
						"Violation Event Category Not Supported");
			}
		}
		
		int dcvIndex = 0;
		int cvIndex = 0;
		int vedIndex = 0;
		int venIndex = 0;
		List<Integer> dtIndexes = new ArrayList<Integer>();
		if(form.getDisciplinaryCodeViolationItems() != null){
			dcvIndex = form.getDisciplinaryCodeViolationItems().size();
		}
		if(form.getConditionViolationItems() != null){
			cvIndex = form.getConditionViolationItems().size();
		}
		if(form.getViolationEventDocumentItems() != null){
			vedIndex = form.getViolationEventDocumentItems().size();
			
			int vedCounter = 0;
			for(ViolationEventDocumentItem item :
					form.getViolationEventDocumentItems()){
				if(item.getTagItems() != null){
					dtIndexes.add(vedCounter, item.getTagItems().size());
				}
				else{
					dtIndexes.add(vedCounter, 0);
				}
				vedCounter++;
			}
		}
		if(form.getViolationEventNoteItems() != null){
			venIndex = form.getViolationEventNoteItems().size();
		}
		map.addAttribute(DISCIPLINARY_CODE_VIOLATION_ITEM_INDEX_MODEL_KEY,
				dcvIndex);
		map.addAttribute(CONDITION_VIOLATION_ITEM_INDEX_MODEL_KEY, cvIndex);
		map.addAttribute(VIOLATION_EVENT_NOTE_ITEM_INDEX_MODEL_KEY, venIndex);
		map.addAttribute(VIOLATION_EVENT_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				vedIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, dtIndexes);
		map.addAttribute(CATEGORY_MODEL_KEY, category);
		map.addAttribute(CATEGORY_NAME_MODEL_KEY,
				category.getName().substring(0,1).toUpperCase()
				.concat(category.getName().substring(1).toLowerCase()));
		map.addAttribute(JURISDICTIONS_MODEL_KEY, jurisdictions);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(FORM_MODEL_KEY, form);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared ViolationEventForm populated by supplied ViolationEvent
	 * @param violationEvent - ViolationEvent
	 * @return ViolationEventForm - prepared ViolationEventForm populated by
	 * supplied ViolationEvent
	 */
	private ViolationEventForm prepareEditForm(
			final ViolationEvent violationEvent){
		
		ViolationEventForm form = new ViolationEventForm();
		
		List<DisciplinaryCodeViolation> disciplinaryCodeViolations =
				this.violationEventService
				.findDisciplinaryCodeViolationsByViolationEvent(violationEvent);
		List<DisciplinaryCodeViolationItem> dcvItems =
				new ArrayList<DisciplinaryCodeViolationItem>();
		
		List<ConditionViolation> conditionViolations =
				this.violationEventService
				.findConditionViolationsByViolationEvent(violationEvent);
		List<ConditionViolationItem> cvItems =
				new ArrayList<ConditionViolationItem>();
		
		List<ViolationEventNote> violationEventNotes =
				this.violationEventService
				.findViolationEventNotesByViolationEvent(violationEvent);
		List<ViolationEventNoteItem> venItems =
				new ArrayList<ViolationEventNoteItem>();
		
		List<ViolationEventDocument> violationEventDocuments =
				this.violationEventService
				.findViolationEventDocumentsByViolationEvent(violationEvent);
		List<ViolationEventDocumentItem> vedItems =
				new ArrayList<ViolationEventDocumentItem>();
		
		for(DisciplinaryCodeViolation violation : disciplinaryCodeViolations){
			DisciplinaryCodeViolationItem item = new
					DisciplinaryCodeViolationItem();
			item.setDisciplinaryCode(violation.getDisciplinaryCode());
			item.setDisciplinaryCodeViolation(violation);
			item.setItemOperation(ViolationEventItemOperation.UPDATE);
			
			dcvItems.add(item);
		}
		
		for(ConditionViolation violation : conditionViolations){
			ConditionViolationItem item = new ConditionViolationItem();
			
			item.setCondition(violation.getCondition());
			item.setConditionViolation(violation);
			item.setItemOperation(ViolationEventItemOperation.UPDATE);
			
			cvItems.add(item);
		}
		
		for(ViolationEventNote note : violationEventNotes){
			ViolationEventNoteItem item = new ViolationEventNoteItem();
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setItemOperation(ViolationEventItemOperation.UPDATE);
			item.setViolationEventNote(note);
			
			venItems.add(item);
		}
		
		for(ViolationEventDocument document : violationEventDocuments){
			ViolationEventDocumentItem item = new ViolationEventDocumentItem();
			List<DocumentTag> documentTags = this.violationEventService
					.findDocumentTagsByDocument(document.getDocument());
			List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
			for(DocumentTag tag : documentTags){
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTag(tag);
				tagItem.setDocumentTagOperation(DocumentTagOperation.UPDATE);
				tagItem.setName(tag.getName());
				
				tagItems.add(tagItem);
			}
			
			item.setTagItems(tagItems);
			item.setDate(document.getDocument().getDate());
			item.setDocument(document.getDocument());
			item.setTitle(document.getDocument().getTitle());
			item.setViolationEventDocument(document);
			item.setFileExtension(document.getDocument().getFileExtension());
			item.setItemOperation(ViolationEventItemOperation.UPDATE);
			item.setData(this.violationEventDocumentRetriever
					.retrieve(document.getDocument()));
			
			vedItems.add(item);
		}
		
		if(this.violationEventService
			.findFacilitySupervisoryOrganizations().contains(
					violationEvent.getJurisdiction())){
			form.setJurisdictionFilter(JurisdictionFilter.SECURE_FACILITY);
		}
		else if(this.violationEventService
					.findTreatmentCenterSupervisoryOrganizations().contains(
							violationEvent.getJurisdiction())){
			form.setJurisdictionFilter(JurisdictionFilter.TREATMENT_CENTER);
		}
		else if(this.violationEventService
					.findPreReleaseCenterSupervisoryOrganizations().contains(
							violationEvent.getJurisdiction())){
			form.setJurisdictionFilter(JurisdictionFilter.PRERELEASE_CENTER);
		}
		else if(this.violationEventService
					.findCommunitySupervisionOfficeSupervisoryOrganizations()
					.contains(violationEvent.getJurisdiction())){
			form.setJurisdictionFilter(JurisdictionFilter
					.COMMUNITY_SUPERVISION_OFFICE);
		}
		else if(this.violationEventService
			.findAssessmentSanctionRevocationCenterSupervisoryOrganizations()
			.contains(violationEvent.getJurisdiction())){
			form.setJurisdictionFilter(JurisdictionFilter
					.ASSESSMENT_SANCTION_AND_RECOVATION_CENTER);
		}
		else {
			form.setJurisdictionFilter(JurisdictionFilter.SECURE_FACILITY);
		}
		
		form.setEventDate(violationEvent.getEvent().getDate());
		form.setEventDetails(violationEvent.getEvent().getDetails());
		if (violationEvent.getJurisdiction() instanceof SupervisoryOrganization) {
			form.setJurisdiction((SupervisoryOrganization) violationEvent
					.getJurisdiction());
		}
		form.setViolationEventDocumentItems(vedItems);
		form.setViolationEventNoteItems(venItems);
		form.setDisciplinaryCodeViolationItems(dcvItems);
		form.setConditionViolationItems(cvItems);
		
		return form;
	}
	
	/**
	 * Creates, updates, or removes DisciplinaryCodeViolations,
	 * ViolationEventNotes, and ViolationEventDocuments based on the given item
	 * lists
	 * @param violationEvent - ViolationEvent items are are being processed for
	 * @param form - ViolationEventForm
	 * @throws DuplicateEntityFoundException - When a DiscplinaryCodeViolation,
	 * ConditionViolation, ViolationEventNote, ViolationEventDocument, or
	 * DocumentTag already exist
	 */
	private void processItems(
			final ViolationEvent violationEvent,
			final ViolationEventForm form)
					throws DuplicateEntityFoundException{
		
		List<DisciplinaryCodeViolationItem> disciplinaryCodeViolationItems =
				form.getDisciplinaryCodeViolationItems();
		List<ConditionViolationItem> conditionViolationItems =
				form.getConditionViolationItems();
		List<ViolationEventNoteItem> violationEventNoteItems =
				form.getViolationEventNoteItems();
		List<ViolationEventDocumentItem> violationEventDocumentItems =
				form.getViolationEventDocumentItems();
		
		
		if(disciplinaryCodeViolationItems != null){
			for(DisciplinaryCodeViolationItem item :
					disciplinaryCodeViolationItems){
				if(ViolationEventItemOperation.CREATE.equals(
						item.getItemOperation())){
					this.violationEventService.createDisciplinaryCodeViolation(
							item.getDisciplinaryCode(), violationEvent);
				}
				if(ViolationEventItemOperation.UPDATE.equals(
						item.getItemOperation())){
					this.violationEventService.updateDisciplinaryCodeViolation(
							item.getDisciplinaryCodeViolation(),
							item.getDisciplinaryCode());
				}
				if(ViolationEventItemOperation.REMOVE.equals(
						item.getItemOperation())){
					this.violationEventService.removeDisciplinaryCodeViolation(
							item.getDisciplinaryCodeViolation());
				}
			}
		}
		
		if(conditionViolationItems != null){
			for(ConditionViolationItem item :
					conditionViolationItems){
				if(ViolationEventItemOperation.CREATE.equals(
						item.getItemOperation())){
					this.violationEventService.createConditionViolation(
							item.getCondition(), violationEvent);
				}
				if(ViolationEventItemOperation.UPDATE.equals(
						item.getItemOperation())){
					this.violationEventService.updateConditionViolation(
							item.getConditionViolation(),
							item.getCondition());
				}
				if(ViolationEventItemOperation.REMOVE.equals(
						item.getItemOperation())){
					this.violationEventService.removeConditionViolation(
							item.getConditionViolation());
				}
			}
		}
		
		if(violationEventNoteItems != null){
			for(ViolationEventNoteItem item : violationEventNoteItems){
				if(ViolationEventItemOperation.CREATE.equals(
						item.getItemOperation())){
					this.violationEventService.createViolationEventNote(
							item.getDate(), item.getDescription(),
							violationEvent);
				}
				if(ViolationEventItemOperation.UPDATE.equals(
						item.getItemOperation())){
					if(this.isNoteChanged(item.getViolationEventNote(),
							item.getDate(), item.getDescription())) {
						this.violationEventService.updateViolationEventNote(
								item.getViolationEventNote(), item.getDate(),
								item.getDescription());
					}
				}
				if(ViolationEventItemOperation.REMOVE.equals(
						item.getItemOperation())){
					this.violationEventService.removeViolationEventNote(
							item.getViolationEventNote());
				}
			}
		}
		
		if(violationEventDocumentItems != null){
			for(ViolationEventDocumentItem item : violationEventDocumentItems){
				if(ViolationEventItemOperation.CREATE.equals(
						item.getItemOperation())){
					final String fileExtension = item.getFileExtension();
					this.documentFilenameGenerator
							.setExtension(fileExtension);
					final String filename =
							this.documentFilenameGenerator.generate();
					Document document = this.violationEventService
							.createDocument(item.getDate(),
									filename, item.getFileExtension(),
									item.getTitle());
					this.violationEventDocumentPersister
							.persist(document, item.getData());
					
					this.violationEventService.createViolationEventDocument(
							document, violationEvent);
					
					if(item.getTagItems() != null){
						for(DocumentTagItem tagItem : item.getTagItems()){
							if(DocumentTagOperation.CREATE.equals(
									tagItem.getDocumentTagOperation())){
								this.violationEventService.createDocumentTag(
										document, tagItem.getName());
							}
						}
					}
				}
				if(ViolationEventItemOperation.UPDATE.equals(
						item.getItemOperation())){
					Document document = this.violationEventService
							.updateDocument(item.getDocument(),
									item.getTitle(), item.getDate());
					this.violationEventService
						.updateViolationEventDocument(
							item.getViolationEventDocument(), document);
					if(item.getTagItems() != null){
						for(DocumentTagItem tagItem : item.getTagItems()){
							if(DocumentTagOperation.CREATE.equals(
									tagItem.getDocumentTagOperation())){
								this.violationEventService.createDocumentTag(
										document, tagItem.getName());
							}
							if(DocumentTagOperation.UPDATE.equals(
									tagItem.getDocumentTagOperation())){
								this.violationEventService.updateDocumentTag(
										tagItem.getDocumentTag(),
										tagItem.getName());
							}
							if(DocumentTagOperation.REMOVE.equals(
									tagItem.getDocumentTagOperation())){
								this.violationEventService.removeDocumentTag(
										tagItem.getDocumentTag());
							}
						}
					}
				}
				if(ViolationEventItemOperation.REMOVE.equals(
						item.getItemOperation())){
					for(DocumentTag tag : this.violationEventService
							.findDocumentTagsByDocument(item.getDocument())){
						this.violationEventService.removeDocumentTag(tag);
					}
					this.violationEventService.removeViolationEventDocument(
							item.getViolationEventDocument());
					this.violationEventDocumentRemover.remove(
							item.getDocument().getFilename());
					this.violationEventService.removeDocument(
							item.getDocument());
				}
			}
		}
	}
	
	/** Retrieves document file.
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response. */
	@RequestMapping(value = "retrieveFile.html", method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.violationEventDocumentRetriever.retrieve(document);
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
	 * Checks if a Violation Event Note has been changed and returns true if it
	 * has
	 * @param note - Violation Event Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is different
	 * from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final ViolationEventNote note,
			final Date date, final String value) {
		if(!note.getDescription().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder){
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				ViolationEvent.class,
				this.violationEventPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				ViolationEventNote.class,
				this.violationEventNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ViolationEventDocument.class,
				this.violationEventDocumentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				DisciplinaryCodeViolation.class,
				this.disciplinaryCodeViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				DisciplinaryCode.class,
				this.disciplinaryCodePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ConditionViolation.class,
				this.conditionViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Condition.class,
				this.conditionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SupervisoryOrganization.class,
				this.supervisoryOrganizationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Document.class,
				this.documentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				DocumentTag.class,
				this.documentTagPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				IncidentStatement.class,
				this.incidentStatementPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTest.class,
				this.substanceTestPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
	
}
