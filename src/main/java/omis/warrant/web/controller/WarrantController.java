/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.warrant.web.controller;

import java.math.BigDecimal;
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

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionClause;
import omis.courtcase.domain.CourtCase;
import omis.docket.domain.Docket;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Facility;
import omis.jail.domain.Jail;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.region.domain.County;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;
import omis.warrant.domain.WarrantCancellation;
import omis.warrant.domain.WarrantCauseViolation;
import omis.warrant.domain.WarrantNote;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.exception.WarrantArrestExistsException;
import omis.warrant.exception.WarrantCauseViolationExistsException;
import omis.warrant.exception.WarrantExistsException;
import omis.warrant.exception.WarrantNoteExistsException;
import omis.warrant.exception.WarrantReleaseExistsException;
import omis.warrant.service.WarrantCancellationService;
import omis.warrant.service.WarrantReleaseService;
import omis.warrant.service.WarrantService;
import omis.warrant.web.form.WarrantCancellationForm;
import omis.warrant.web.form.WarrantCauseViolationItem;
import omis.warrant.web.form.WarrantForm;
import omis.warrant.web.form.WarrantItemOperation;
import omis.warrant.web.form.WarrantNoteItem;
import omis.warrant.web.form.WarrantReleaseForm;
import omis.warrant.web.validator.WarrantCancellationFormValidator;
import omis.warrant.web.validator.WarrantFormValidator;
import omis.warrant.web.validator.WarrantReleaseFormValidator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Warrant controller.
 * 
 * @author Annie Jacques
 * @author Joel Norris
 * @author Sierra Rosales
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @version 0.1.1 (April 25, 2018)
 * @since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/warrant/")
@PreAuthorize("hasRole('USER')")
public class WarrantController {
	
	/* Views Names */
	
	private static final String EDIT_VIEW_NAME = "/warrant/edit";
	
	private static final String CANCEL_VIEW_NAME = "/warrant/cancel";
	
	private static final String RELEASE_VIEW_NAME = "/warrant/release";
	
	private static final String WARRANT_ACTION_MENU_VIEW_NAME =
			"/warrant/includes/warrantActionMenu";
	
	private static final String WARRANT_RELEASE_ACTION_MENU_VIEW_NAME =
			"/warrant/includes/warrantReleaseActionMenu";
	
	private static final String WARRANT_CANCELLATION_ACTION_MENU_VIEW_NAME =
			"/warrant/includes/warrantCancellationActionMenu";
	
	private static final String WARRANT_NOTES_ACTION_MENU_VIEW_NAME =
			"/warrant/includes/warrantNoteItemsActionMenu";
	
	private static final String WARRANT_CAUSE_VIOLATIONS_ACTION_MENU_VIEW_NAME =
			"/warrant/includes/warrantCauseViolationItemsActionMenu";
	
	private static final String WARRANT_NOTE_ITEM_ROW_VIEW_NAME =
			"/warrant/includes/warrantNoteTableRow";
	
	private static final String VIOLATION_TO_WIT_ITEM_VIEW_NAME =
			"warrant/includes/violationToWitItemRow";
	
	private static final String CONDITION_CLAUSE_DOCKETS_VIEW_NAME = 
			"warrant/includes/conditionClauseDockets";
	
	/* Model Keys */
	
	private static final String WARRANT_FORM_MODEL_KEY = "warrantForm";
	
	private static final String WARRANT_RELEASE_FORM_MODEL_KEY =
			"warrantReleaseForm";
	
	private static final String WARRANT_CANCELLATION_FORM_MODEL_KEY =
			"warrantCancellationForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String WARRANT_MODEL_KEY = "warrant";
	
	private static final String WARRANT_ARREST_MODEL_KEY = "warrantArrest";
	
	private static final String WARRANT_RELEASE_MODEL_KEY = "warrantRelease";
	
	private static final String WARRANT_CANCELLATION_MODEL_KEY =
			"warrantCancellation";
	
	private static final String WARRANT_REASON_CATEGORY_MODEL_KEY =
			"warrantReasonCategory";
	
	private static final String JAILS_MODEL_KEY = "jails";
	
	private static final String WARRANT_NOTE_ITEM_MODEL_KEY = "warrantNoteItem";
	
	private static final String WARRANT_CAUSE_VIOLATION_ITEM_MODEL_KEY =
			"warrantCauseViolationItem";
	
	private static final String WARRANT_NOTE_ITEM_INDEX_MODEL_KEY =
			"warrantNoteItemIndex";
	
	private static final String WARRANT_CAUSE_VIOLATION_ITEM_INDEX_MODEL_KEY =
			"warrantCauseViolationItemIndex";
	
	private static final String VIOLATION_TO_WIT_ITEM_INDEX_MODEL_KEY = "violationToWitItemIndex";
	
	private static final String CONDITION_CLAUSES_MODEL_KEY = "conditionClauses";
	
	private static final String CLAUSE_TO_DOCKETS_MAP_MODEL_KEY = "clauseToDocketsMap";
	
	private static final String DOCKETS_MODEL_KEY = "dockets";
	
	private static final String WARRANT_EXISTS_EXCEPTION_MESSAGE_KEY
	= "warrant.Exists";
	
	private static final String WARRANT_NOTE_EXISTS_EXCEPTION_MESSAGE_KEY
	= "warrantNote.Exists";
	
	private static final String
	WARRANT_CAUSE_VIOLATION_EXISTS_EXCEPTION_MESSAGE_KEY
	= "warrantCauseViolation.Exists";
	
	private static final String
	WARRANT_ARREST_EXISTS_EXCEPTION_MESSAGE_KEY = "warrantArrest.Exists";
	
	private static final String
	WARRANT_RELEASE_EXISTS_EXCEPTION_MESSAGE_KEY = "warrantRelease.Exists";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT =
			"redirect:/warrant/list.html?offender=%d";
	
	/* Message Keys */
		
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"entity.exists";
	
	/* Report names. */
	
	private static final String WARRANT_LISTING_REPORT_NAME =
			"/Compliance/Warrants/Warrants_Listing";
	
	private static final String WARRANT_DETAILS_REPORT_NAME =
			"/Compliance/Warrants/Warrant_Details";		
	
	private static final String AUTHORIZATION_TO_CANCEL_REPORT_NAME =
			"/Compliance/Warrants/Authorization_to_Cancel_Warrant_Pick_Up_Hold";	
	
	private static final String AUTHORIZATION_TO_CANCEL_ISC_REPORT_NAME =
			"/Compliance/Warrants/Authorization_to_Cancel_Warrant_Interstate_Offender";	
	
	private static final String AUTHORIZATION_TO_PICK_UP_AND_HOLD_REPORT_NAME =
			"/Compliance/Warrants/Authorization_to_Pick_Up_and_Hold";	
	
	private static final String AUTHORIZATION_TO_RELEASE_OFFENDER_REPORT_NAME =
			"/Compliance/Warrants/Authorization_to_Release_Offender";	
	
	private static final String WARRANT_TO_ARREST_ISC_REPORT_NAME =
			"/Compliance/Warrants/Warrant_to_Arrest_Interstate_Offender";
	
	private static final String WARRANT_TO_ARREST_REPORT_NAME =
			"/Compliance/Warrants/Warrant_to_Arrest";	
	
	private static final String WARRANT_CANCELLATION_DETAILS_REPORT_NAME =
			"/Compliance/Warrants/Warrant_Cancellation_Details";	
	
	/* Report parameter names. */
	
	private static final String DOC_ID_REPORT_PARAM_NAME =
			"DOC_ID";	
	
	private static final String WARRANT_ID_REPORT_PARAM_NAME =
			"WARRANT_ID";	
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.warrant.msgs.form";
	
	/* Service */
	
	@Autowired
	@Qualifier("warrantService")
	private WarrantService warrantService;
	
	@Autowired
	@Qualifier("warrantReleaseService")
	private WarrantReleaseService warrantReleaseService;

	@Autowired
	@Qualifier("warrantCancellationService")
	private WarrantCancellationService warrantCancellationService;
	
	/* Property editor factories */
	
	@Autowired
	@Qualifier("warrantPropertyEditorFactory")
	private PropertyEditorFactory warrantPropertyEditorFactory;
	
	@Autowired
	@Qualifier("warrantReleasePropertyEditorFactory")
	private PropertyEditorFactory warrantReleasePropertyEditorFactory;
	
	@Autowired
	@Qualifier("warrantCancellationPropertyEditorFactory")
	private PropertyEditorFactory warrantCancellationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("warrantArrestPropertyEditorFactory")
	private PropertyEditorFactory warrantArrestPropertyEditorFactory;
	
	@Autowired
	@Qualifier("warrantNotePropertyEditorFactory")
	private PropertyEditorFactory warrantNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("warrantCauseViolationPropertyEditorFactory")
	private PropertyEditorFactory warrantCauseViolationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtCasePropertyEditorFactory")
	private PropertyEditorFactory courtCasePropertyEditorFactory;
	
	@Autowired
	@Qualifier("conditionPropertyEditorFactory")
	private PropertyEditorFactory conditionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("jailPropertyEditorFactory")
	private PropertyEditorFactory jailPropertyEditorFactory;
	
	@Autowired
	@Qualifier("countyPropertyEditorFactory")
	private PropertyEditorFactory countyPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("facilityPropertyEditorFactory")
	private PropertyEditorFactory facilityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("conditionClausePropertyEditorFactory")
	private PropertyEditorFactory conditionClausePropertyEditorFactory;
	
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
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validators */
	
	@Autowired
	@Qualifier("warrantFormValidator")
	private WarrantFormValidator warrantFormValidator;
	
	@Autowired
	@Qualifier("warrantReleaseFormValidator")
	private WarrantReleaseFormValidator warrantReleaseFormValidator;
	
	@Autowired
	@Qualifier("warrantCancellationFormValidator")
	private WarrantCancellationFormValidator warrantCancellationFormValidator;
	
	/**
	 * Default constructor for WarrantController
	 */
	public WarrantController() {
	}
	
	/* Warrant */
	
	/**
	 * Displays the Warrant creation screen
	 * @param offender - Offender the Warrant is for
	 * @param category - WarrantReasonCategory
	 * @return ModelAndView - Warrant creation screen
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value="warrantReasonCategory", required=true)
				final WarrantReasonCategory category){
		return this.prepareWarrantMav(offender, category,
				new WarrantForm(), new ModelMap());
	}
	
	/**
	 * Creates a Warrant and returns to the Warrant list screen
	 * @param offender - Offender the Warrant is for
	 * @param category - WarrantReasonCategory
	 * @param form - WarrantForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - Back to the Warrant create screen on form error,
	 * or to the Warrant list screen on successful save
	 * @throws WarrantCauseViolationExistsException 
	 * @throws WarrantNoteExistsException 
	 * @throws WarrantArrestExistsException 
	 * @throws DuplicateEntityFoundException - When a WarrantCauseViolation 
	 * already exists with the given Condition and CourtCase, or a WarrantNote
	 * already exists with the given Note and Date, for the specified Warrant
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value="warrantReasonCategory", required=true)
				final WarrantReasonCategory category,
			final WarrantForm form, final BindingResult bindingResult)
					throws WarrantExistsException, WarrantNoteExistsException, WarrantCauseViolationExistsException, WarrantArrestExistsException{
		
		this.warrantFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_FORM_MODEL_KEY, bindingResult);
			return this.prepareWarrantMav(offender, category,
				form, map);
		} else {
			Warrant warrant = this.warrantService.create(offender,
					form.getDate(), form.getAddressee(), form.getIssuedBy(),
					form.getBondRecommended(), (form.getBondRecommended()
							? new BigDecimal(form.getBondRecommendation())
									: null), category, form.getHoldingJail());
			
			if(form.getArrested()){
				this.warrantService
						.createArrest(warrant, form.getArrestDate(),
								form.getArrestJail(), form.getDeterminationDeadline());
			}
			
			this.processWarrantItems(warrant,
					form.getWarrantNoteItems(),
					form.getWarrantCauseViolationItems());
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					offender.getId()));
		}
	}
	
	/**
	 * Displays the Warrant Edit screen
	 * @param warrant - Warrant being edited
	 * @return ModelAndView - Warrant edit screen
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "warrant", required = true)
				final Warrant warrant){
		ModelMap map = new ModelMap();
		map.addAttribute(WARRANT_MODEL_KEY, warrant);
		WarrantArrest arrest = this.warrantService
				.findWarrantArrestByWarrant(warrant);
		if(arrest != null){
			map.addAttribute(WARRANT_ARREST_MODEL_KEY, arrest);
		}
		return this.prepareWarrantMav(
				warrant.getOffender(),
				warrant.getWarrantReason(),
				this.prepareWarrantForm(warrant), map);
	}
	
	/**
	 * Updates the specified Warrant and returns to the Warrant list screen
	 * @param warrant - Warrant to update
	 * @param form - WarrantForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - Back to the Warrant Edit screen on form error,
	 * or to the Warrant list screen on successful update
	 * @throws WarrantCauseViolationExistsException 
	 * @throws WarrantNoteExistsException 
	 * @throws WarrantArrestExistsException 
	 * @throws DuplicateEntityFoundException - When a WarrantCauseViolation 
	 * already exists with the given Condition and CourtCase, or a WarrantNote
	 * already exists with the given Note and Date, for the specified Warrant
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WARRANT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "warrant", required = true)
				final Warrant warrant,
			final WarrantForm form, final BindingResult bindingResult)
						throws WarrantExistsException, WarrantNoteExistsException,
						WarrantCauseViolationExistsException, WarrantArrestExistsException{
		this.warrantFormValidator.validate(form, bindingResult);
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_FORM_MODEL_KEY, bindingResult);
			map.addAttribute(WARRANT_MODEL_KEY, warrant);
			WarrantArrest arrest = this.warrantService
					.findWarrantArrestByWarrant(warrant);
			if(arrest != null){
				map.addAttribute(WARRANT_ARREST_MODEL_KEY, arrest);
			}
			return this.prepareWarrantMav(
					warrant.getOffender(),
					warrant.getWarrantReason(),
					form, map);
		}
		this.warrantService.update(warrant,
				form.getDate(), form.getAddressee(), form.getIssuedBy(),
				form.getBondRecommended(), (form.getBondRecommended()
						? new BigDecimal(form.getBondRecommendation())
								: null),
				warrant.getWarrantReason(), form.getHoldingJail());
		if(form.getArrested()){
			WarrantArrest warrantArrest =
					this.warrantService.findWarrantArrestByWarrant(warrant);
			if(warrantArrest == null){
				this.warrantService.createArrest(
						warrant, form.getArrestDate(),
						form.getArrestJail(), form.getDeterminationDeadline());
			} else {
				this.warrantService.updateArrest(warrantArrest,
						form.getArrestDate(), form.getArrestJail(),
						form.getDeterminationDeadline());
			}
		}
		this.processWarrantItems(warrant,
				form.getWarrantNoteItems(),
				form.getWarrantCauseViolationItems());
		return new ModelAndView(String.format(LIST_REDIRECT,
				warrant.getOffender().getId()));
	}
	
	/**
	 * Removes the specified warrant, and any related warrant specific objects.
	 * 
	 * @param warrant warrant
	 * @return model and view for warrants listing screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView removeWarrant(@RequestParam(value = "warrant", required = true)
			final Warrant warrant) {
		List<WarrantNote> notes = this.warrantService.findWarrantNotesByWarrant(warrant);
		for (WarrantNote note : notes) {
			this.warrantService.removeWarrantNote(note);
		}
		this.warrantService.remove(warrant);
		return new ModelAndView(String.format(LIST_REDIRECT,
				warrant.getOffender().getId()));
	}
	
	/**
	 * Displays a WarrantNoteItem row view
	 * @param warrantNoteItemIndex - current item index
	 * @return ModelAndView - WarrantNoteItem row
	 */
	@RequestMapping(value = "createWarrantNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayWarrantNoteItemRow(@RequestParam(
			value = "warrantNoteItemIndex", required = true)
				final Integer warrantNoteItemIndex) {
		ModelMap map = new ModelMap();
		WarrantNoteItem noteItem = new WarrantNoteItem();
		noteItem.setItemOperation(WarrantItemOperation.CREATE);
		map.addAttribute(WARRANT_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(WARRANT_NOTE_ITEM_INDEX_MODEL_KEY,
				warrantNoteItemIndex);
		return new ModelAndView(WARRANT_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
//	/**
//	 * Displays a WarrantCauseViolationItem row view
//	 * @param warrantCauseViolationItemIndex - current item index
//	 * @param courtCase - CourtCase
//	 * @return ModelAndView - WarrantCauseViolationItem row
//	 */
//	@RequestMapping(value = "createWarrantCauseViolationItem.html",
//			method = RequestMethod.GET)
//	public ModelAndView displayWarrantCauseViolationItemRow(
//			@RequestParam(value = "warrantCauseViolationItemIndex",
//			required = true) final Integer warrantCauseViolationItemIndex,
//			@RequestParam(value = "courtCase",
//			required = true) final CourtCase courtCase) {
//		ModelMap map = new ModelMap();
//		WarrantCauseViolationItem violationItem =
//				new WarrantCauseViolationItem();
//		violationItem.setCourtCase(courtCase);
//		violationItem.setItemOperation(WarrantItemOperation.CREATE);
//		
//		List<Condition> conditions = this.warrantService
//				.findConditionsByCourtCase(courtCase);
//		map.addAttribute(CONDITIONS_MODEL_KEY, conditions);
//		map.addAttribute(WARRANT_CAUSE_VIOLATION_ITEM_MODEL_KEY, violationItem);
//		map.addAttribute(WARRANT_CAUSE_VIOLATION_ITEM_INDEX_MODEL_KEY,
//				warrantCauseViolationItemIndex);
//		return new ModelAndView(WARRANT_CAUSE_VIOLATION_ITEM_ROW_VIEW_NAME, map);
//	}
	
	/* Warrant Release */
	
	/**
	 * Creates a model and view to create a release for the specified warrant.
	 * 
	 * @param warrant warrant
	 * @return model and view to create warrant release
	 */
	@RequestMapping(value = "/createWarrantRelease.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_RELEASE_CREATE') or hasRole('ADMIN')")
	public ModelAndView createWarrantRelease(
			@RequestParam(value = "warrant", required = true)
			final Warrant warrant) {
		ModelMap map = new ModelMap();
		return this.prepareWarrantReleaseMav(map, warrant,
				new WarrantReleaseForm());
	}
	
	/**
	 * Creates a warrant release for the specified warrant with values from the
	 * specified warrant release form.
	 * 
	 * @param warrant warrant
	 * @param form warrant release form
	 * @param result binding result
	 * @return model and view for warrant list redirect
	 * @throws WarrantReleaseExistsException thrown when a warrant release already exists
	 */
	@RequestMapping(value = "/createWarrantRelease.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WARRANT_RELEASE_CREATE') or hasRole('ADMIN')")
	public ModelAndView saveWarrantRelease(
			@RequestParam(value = "warrant", required = true)
			final Warrant warrant, WarrantReleaseForm form,
			BindingResult result) throws WarrantReleaseExistsException { 
		this.warrantReleaseFormValidator.validate(form, result); 
		if(result.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_RELEASE_FORM_MODEL_KEY, result);
			return this.prepareWarrantReleaseMav(map, warrant, form);
		}
		this.warrantReleaseService.create(warrant, form.getInstructions(),
				form.getReleaseDate(), form.getAddressee(), form.getClearedBy(),
				form.getClearedByDate());
		return new ModelAndView(String.format(LIST_REDIRECT,
				warrant.getOffender().getId()));
	}
	
	/**
	 * Prepares model and view for viewing/editing the specified warrant release.
	 * 
	 * @param release warrant release
	 * @return model and view to view/edit warrant release
	 */
	@RequestMapping(value = "/editWarrantRelease.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_RELEASE_VIEW') or hasRole('ADMIN')")
	public ModelAndView editWarrantRelease(@RequestParam(value = "warrantRelease", required = true)
			final WarrantRelease release) {
		ModelMap map = new ModelMap();
		WarrantReleaseForm form = new WarrantReleaseForm();
		form.setAddressee(release.getAddressee());
		if (release.getClearSignature() != null) {
			form.setClearedBy(release.getClearSignature().getPerson());
			form.setClearedByDate(release.getClearSignature().getDate());
		}
		form.setInstructions(release.getInstructions());
		form.setReleaseDate(release.getReleaseTimestamp());
		map.addAttribute(WARRANT_RELEASE_MODEL_KEY, release);
		return this.prepareWarrantReleaseMav(map, release.getWarrant(),
				form);
	}
	
	/**
	 * Updates the specified warrant release with values from the specified
	 * warrant release form.
	 * 
	 * @param release warrant release
	 * @param form warrant release form
	 * @param result binding result
	 * @return
	 * @throws WarrantReleaseExistsException
	 */
	@RequestMapping(value = "/editWarrantRelease.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WARRANT_RELEASE_EDIT') or hasRole('ADMIN')")
	public ModelAndView updateWarrantRelease(@RequestParam(value = "warrantRelease", required = true)
			final WarrantRelease release, final WarrantReleaseForm form,
			final BindingResult result) throws WarrantReleaseExistsException {
		this.warrantReleaseFormValidator.validate(form, result); 
		if(result.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_RELEASE_FORM_MODEL_KEY, result);
			map.addAttribute(WARRANT_RELEASE_MODEL_KEY, release);
			return this.prepareWarrantReleaseMav(map, release.getWarrant(), form);
		}
		this.warrantReleaseService.update(release, form.getInstructions(), form.getReleaseDate(),
				form.getAddressee(), form.getClearedBy(), form.getClearedByDate());
		return new ModelAndView(String.format(LIST_REDIRECT,
				release.getWarrant().getOffender().getId()));
	}
	
//	/**
//	 * Displays the WarrantRelease edit screen
//	 * @param warrant - Warrant the WarrantRelease is for
//	 * @return ModelAndView - WarrantRelease edit screen
//	 */
//	@RequestMapping(value = "/release.html", method = RequestMethod.GET)
//	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
//	public ModelAndView editWarrantRelease(
//			@RequestParam(value = "warrant", required = true)
//				final Warrant warrant){
//		return this.prepareWarrantReleaseMav(warrant,
//				this.prepareWarrantReleaseForm(warrant));
//	}
//	
//	/**
//	 * Creates or Updates a WarrantRelease and returns to the Warrant list screen
//	 * @param warrant - Warrant
//	 * @param form - WarrantReleaseForm
//	 * @param bindingResult - BindingResult
//	 * @return ModelAndView - Back to the WarrantRelease edit screen on form
//	 * error, or to the Warrant list screen on successful save/update
//	 * @throws DuplicateEntityFoundException - When another WarrantRelease
//	 * already exists for the specified Warrant (shouldn't happen from this spot)
//	 */
//	@RequestMapping(value = "/release.html", method = RequestMethod.POST)
//	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
//	public ModelAndView updateWarrantRelease(
//			@RequestParam(value = "warrant", required = true)
//				final Warrant warrant,
//			final WarrantReleaseForm form, final BindingResult bindingResult)
//					throws WarrantReleaseExistsException {
//		
//		this.warrantReleaseFormValidator.validate(form, bindingResult);
//		
//		if(bindingResult.hasErrors()){
//			return this.prepareWarrantReleaseMav(warrant, form);
//		}
//		else{
//			WarrantRelease warrantRelease = this.warrantReleaseService
//					.findByWarrant(warrant);
//			
//			if(warrantRelease == null){
//				this.warrantReleaseService.create(warrant,
//						form.getInstructions(), form.getReleaseDate(),
//						form.getAddressee(), form.getClearedBy(),
//						form.getClearedByDate());
//			}
//			else{
//				this.warrantReleaseService.update(warrantRelease,
//						form.getInstructions(),form.getReleaseDate(),
//						form.getAddressee(), form.getClearedBy(),
//						form.getClearedByDate());
//			}
//			
//			return new ModelAndView(String.format(LIST_REDIRECT,
//					warrant.getOffender().getId()));
//		}
//	}
	
	
	/* Warrant Cancellation */
	
	
	/**
	 * Creates a view to cancel the specified warrant.
	 * 
	 * @param warrant warrant
	 * @return model and view for creating a warrant cancellation
	 */
	@RequestMapping(value = "/createWarrantCancellation", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_CANCELLATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView createWarrantCancellation(
			@RequestParam(value = "warrant", required = true)
				final Warrant warrant) {
		return this.prepareWarrantCancellationMav(new ModelMap(), warrant,
				new WarrantCancellationForm());
	}
	
	/**
	 * Creates a warrant cancellation for the specified warrant with values from
	 * the specified warrant cancellation form.
	 * 
	 * @param warrant warrant
	 * @param form warrant cancellation form
	 * @param result binding result
	 * @return model and view for list redirect, or return to warrant cancellation form
	 * 
	 * @throws DuplicateEntityFoundException Thrown when a duplicate warrant cancellation is found for the specified warrant.
	 */
	@RequestMapping(value = "/createWarrantCancellation", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WARRANT_CANCELLATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView saveWarrantCancellation(
			@RequestParam(value = "warrant", required = true)
			final Warrant warrant, final WarrantCancellationForm form,
			final BindingResult result) throws DuplicateEntityFoundException {
		this.warrantCancellationFormValidator.validate(form, result);
		if(result.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_CANCELLATION_FORM_MODEL_KEY, result);
			return this.prepareWarrantCancellationMav(map, warrant, form);
		}
		this.warrantCancellationService.create(warrant, form.getDate(), form.getComment(), form.getClearedBy(), form.getClearedByDate());
		return new ModelAndView(String.format(LIST_REDIRECT,
				warrant.getOffender().getId()));
	}
	
	/**
	 * Prepares the edit warrant cancellation view with information from the specified warrant cancellation.
	 * 
	 * @param cancellation warrant cancellation
	 * @return model and view to edit warrant cancellation
	 */
	@RequestMapping(value = "/editWarrantCancellation", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_CANCELLATION_VIEW') or hasRole('ADMIN')")
	public ModelAndView editWarrantCancellation(
			@RequestParam(value = "warrantCancellation", required = true)
				final WarrantCancellation cancellation) {
		ModelMap map = new ModelMap();
		WarrantCancellationForm form = new WarrantCancellationForm();
		form.setDate(cancellation.getDate());
		form.setComment(cancellation.getComment());
		form.setClearedBy(cancellation.getClearSignature()
				.getPerson());
		form.setClearedByDate(cancellation.getClearSignature()
				.getDate());
		map.addAttribute(WARRANT_CANCELLATION_MODEL_KEY, cancellation);
		return this.prepareWarrantCancellationMav(map,
				cancellation.getWarrant(), form);
	}
	
	/**
	 * Updates the specified warrant cancellation with values from the specified warrant cancellation form.
	 * 
	 * @param cancellation warrant cancellation
	 * @param form warrant cancellation form
	 * @param result binding result
	 * @return model and view for list redirect, or return to warrant cancellation form
	 * @throws DuplicateEntityFoundException thrown when a duplicate warrant cancellation is found
	 */
	public ModelAndView updateWarrantCancellation(
			@RequestParam(value = "warrantCancellation", required = true)
				final WarrantCancellation cancellation, final WarrantCancellationForm form,
				final BindingResult result) throws DuplicateEntityFoundException {
		this.warrantCancellationFormValidator.validate(form, result);
		if(result.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ WARRANT_FORM_MODEL_KEY, result);
			return this.prepareWarrantCancellationMav(map, cancellation.getWarrant(), form);
		}
		this.warrantCancellationService.update(cancellation, form.getDate(), form.getComment(),
				form.getClearedBy(), form.getClearedByDate());
		return new ModelAndView(String.format(LIST_REDIRECT,
				cancellation.getWarrant().getOffender().getId()));
	}

//	/**
//	 * Displays the WarrantCancellation edit screen
//	 * @param warrant - Warrant the WarrantCancellation is for
//	 * @return ModelAndView - WarrantCancellation edit screen
//	 */
//	@RequestMapping(value = "/cancel.html", method = RequestMethod.GET)
//	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
//	public ModelAndView editWarrantCancellation(
//			@RequestParam(value = "warrant", required = true)
//				final Warrant warrant){
//		
//		return this.prepareWarrantCancellationMav(warrant,
//				this.prepareWarrantCancellationForm(warrant));
//	}
//	
//	/**
//	 * Creates or Updates a WarrantCancellation and returns to the Warrant list
//	 * screen
//	 * @param warrant - Warrant
//	 * @param form - WarrantCancellationForm
//	 * @param bindingResult - BindingResult
//	 * @return ModelAndView - back to the WarrantCancellation edit screen on
//	 * form error, or to the Warrant list screen on successful save/update
//	 * @throws DuplicateEntityFoundException - When another WarrantCancellation 
//	 * already exists for the specified Warrant (shouldn't happen from this spot)
//	 */
//	@RequestMapping(value = "/cancel.html", method = RequestMethod.POST)
//	@PreAuthorize("hasRole('WARRANT_CREATE') or hasRole('ADMIN')")
//	public ModelAndView updateWarrantCancellation(
//			@RequestParam(value = "warrant", required = true)
//				final Warrant warrant,
//			final WarrantCancellationForm form,
//			final BindingResult bindingResult)
//					throws DuplicateEntityFoundException{
//		
//		this.warrantCancellationFormValidator.validate(form, bindingResult);
//		
//		if(bindingResult.hasErrors()){
//			return this.prepareWarrantCancellationMav(warrant, form);
//		}
//		else{
//			WarrantCancellation warrantCancellation =
//					this.warrantCancellationService.findByWarrant(warrant);
//			
//			if(warrantCancellation == null){
//				this.warrantCancellationService.create(warrant, form.getDate(),
//						form.getComment(), form.getClearedBy(),
//						form.getClearedByDate());
//			}
//			else{
//				this.warrantCancellationService.update(warrantCancellation,
//						form.getDate(), form.getComment(), form.getClearedBy(),
//						form.getClearedByDate());
//			}
//			
//			return new ModelAndView(String.format(LIST_REDIRECT,
//					warrant.getOffender().getId()));
//		}
//	}
	
	/* Action Menus */
	
	/**
	 * Displays the Warrant action menu
	 * @param offender - Offender
	 * @return ModelAndView - Warrant action menu
	 */
	@RequestMapping(value = "/warrantActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayWarrantActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(WARRANT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the WarrantCauseViolationItems action menu
	 * @param offender - Offender
	 * @return ModelAndView - WarrantCauseViolationItems action menu
	 */
	@RequestMapping(value = "/warrantCauseViolationItemsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayWarrantCauseViolationItemsActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender,
			@RequestParam(value = "violationToWitItemIndex", required = true)
			final Integer violationToWitItemIndex){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(VIOLATION_TO_WIT_ITEM_INDEX_MODEL_KEY, violationToWitItemIndex);
		return new ModelAndView(
				WARRANT_CAUSE_VIOLATIONS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the WarrantNoteItems action menu
	 * @return ModelAndView - WarrantNoteItems action menu
	 */
	@RequestMapping(value = "/warrantNoteItemsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayWarrantNoteItemsActionMenu(){
		ModelMap map = new ModelMap();
		return new ModelAndView(
				WARRANT_NOTES_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays a violation to wit item row.
	 * 
	 * @param offender offender
	 * @return model and view for violation to wit item row
	 */
	@RequestMapping(value = "createViolationToWitItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayViolationToWitRowItem(
		@RequestParam(value = "offender", required = true) final Offender offender,
		@RequestParam(value = "violationToWitItemIndex", required = true)
		final Integer violationToWitItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(CONDITION_CLAUSES_MODEL_KEY,
				this.warrantService.findUniqueConditionClausesByOffender(offender, new Date()));
		map.addAttribute(VIOLATION_TO_WIT_ITEM_INDEX_MODEL_KEY, violationToWitItemIndex);
		WarrantCauseViolationItem item = new WarrantCauseViolationItem();
		item.setItemOperation(WarrantItemOperation.CREATE);
		map.addAttribute(WARRANT_CAUSE_VIOLATION_ITEM_MODEL_KEY, item);
		return new ModelAndView(VIOLATION_TO_WIT_ITEM_VIEW_NAME, map);
	}
	
	
	/**
	 * Displays the WarrantRelease action menu
	 * @param offender - Offender
	 * @return ModelAndView - WarrantRelease action menu
	 */
	@RequestMapping(value = "/warrantReleaseActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayWarrantReleaseActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(WARRANT_RELEASE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the WarrantCancellation action menu
	 * @param offender - Offender
	 * @return ModelAndView - WarrantCancellation action menu
	 */
	@RequestMapping(value = "/warrantCancellationActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayWarrantCancellationActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(WARRANT_CANCELLATION_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays view for dockets that for the specified condition clause.
	 * 
	 * @param conditionClause condition clause
	 * @param offender
	 * @param effectiveDate
	 * @return
	 */
	@RequestMapping(value = "/displayConditionClauseDockets.html",
			method = RequestMethod.GET)
	public ModelAndView displayConditionClauseDockets(@RequestParam(value = "conditionClause", required = true)
		final ConditionClause conditionClause,
		@RequestParam(value = "offender", required = true) final Offender offender,
		@RequestParam(value = "effectiveDate", required = false) Date effectiveDate) {
		ModelMap map = new ModelMap();
		Date date = new Date();
		if (effectiveDate != null) {
			date = effectiveDate;
		}
		List<Docket> dockets = this.warrantService.findDocketsByConditionClauseAndOffender(conditionClause, offender, date);
		map.addAttribute(DOCKETS_MODEL_KEY, dockets);
		return new ModelAndView(CONDITION_CLAUSE_DOCKETS_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	/**
	 * Prepares a ModelAndView for Warrant creation/editing
	 * @param offender - Offender
	 * @param category - WarrantReasonCategory
	 * @param form - WarrantForm
	 * @param map - ModelMap
	 * @return ModelAndView for Warrant creation/editing
	 */
	private ModelAndView prepareWarrantMav(
			final Offender offender, final WarrantReasonCategory category,
			final WarrantForm form, final ModelMap map){
		
		if(!(WarrantReasonCategory.AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER
				.equals(category))){
			form.setBondRecommended(false);
		}
		map.addAttribute(WARRANT_FORM_MODEL_KEY, form);
		map.addAttribute(WARRANT_REASON_CATEGORY_MODEL_KEY, category);
		map.addAttribute(JAILS_MODEL_KEY, this.warrantService.findAllJails());
		map.addAttribute(WARRANT_NOTE_ITEM_INDEX_MODEL_KEY,
				(form.getWarrantNoteItems() != null
				? form.getWarrantNoteItems().size() : 0));
		map.addAttribute(WARRANT_CAUSE_VIOLATION_ITEM_INDEX_MODEL_KEY, 
				(form.getWarrantCauseViolationItems() != null
				? form.getWarrantCauseViolationItems().size() : 0));
		map.addAttribute(VIOLATION_TO_WIT_ITEM_INDEX_MODEL_KEY, 
				(form.getWarrantCauseViolationItems() != null
				? form.getWarrantCauseViolationItems().size() : 0));
		List<ConditionClause> conditionClauses = this.warrantService
				.findUniqueConditionClausesByOffender(offender, new Date());
		Map<String, List<Docket>> clauseToDocketMap = new HashMap<String, List<Docket>>();
		for(ConditionClause conditionClause : conditionClauses) {
			List<Docket> dockets = this.warrantService.findDocketsByConditionClauseAndOffender(conditionClause, offender, new Date());
			clauseToDocketMap.put(conditionClause.getConditionTitle().getTitle(), dockets);
		}
		map.addAttribute(CLAUSE_TO_DOCKETS_MAP_MODEL_KEY, clauseToDocketMap);
		map.addAttribute(CONDITION_CLAUSES_MODEL_KEY, conditionClauses);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Populates a WarrantForm with the properties of the specified Warrant
	 * @param warrant - Warrant
	 * @return Prepared WarrantForm
	 */
	private WarrantForm prepareWarrantForm(final Warrant warrant){
		WarrantForm form = new WarrantForm();
		
		form.setAddressee(warrant.getAddressee());
		form.setDate(warrant.getDate());
		form.setIssuedBy(warrant.getIssuedBy());
		form.setHoldingJail(warrant.getHoldingJail());
		
		if(warrant.getBondable()){
			form.setBondRecommended(true);
			if(warrant.getBondRecommendation() != null){
				form.setBondRecommendation(
						warrant.getBondRecommendation().toPlainString());
			}
		}
		else{
			form.setBondRecommended(false);
		}
		
		WarrantArrest arrest = this.warrantService
				.findWarrantArrestByWarrant(warrant);
		
		if(arrest != null){
			form.setArrested(true);
			form.setArrestDate(arrest.getDate());
			form.setArrestJail(arrest.getJail());
			form.setDeterminationDeadline(arrest.getDeterminationDeadline());
		}
		
		List<WarrantNote> warrantNotes = this.warrantService
				.findWarrantNotesByWarrant(warrant);
		List<WarrantCauseViolation> warrantCauseViolations = this.warrantService
				.findWarrantCauseViolationsByWarrant(warrant);
		List<WarrantCauseViolationItem> warrantCauseViolationItems =
				new ArrayList<WarrantCauseViolationItem>();
		List<WarrantNoteItem> warrantNoteItems = new ArrayList<WarrantNoteItem>();
		for(WarrantNote note : warrantNotes){
			WarrantNoteItem noteItem = new WarrantNoteItem();
			noteItem.setWarrantNote(note);
			noteItem.setNote(note.getValue());
			noteItem.setDate(note.getDate());
			noteItem.setItemOperation(WarrantItemOperation.UPDATE);
			
			warrantNoteItems.add(noteItem);
		}
		
		for(WarrantCauseViolation violation : warrantCauseViolations){
			WarrantCauseViolationItem violationItem =
					new WarrantCauseViolationItem();
			violationItem.setWarrantCauseViolation(violation);
			violationItem.setConditionClause(violation.getConditionClause());
			violationItem.setDescription(violation.getDescription());
			violationItem.setItemOperation(WarrantItemOperation.UPDATE);
			
			warrantCauseViolationItems.add(violationItem);
		}
		
		form.setWarrantNoteItems(warrantNoteItems);
		form.setWarrantCauseViolationItems(warrantCauseViolationItems);
		
		return form;
	}
	
	/*
	 * Prepares model and view for editing a warrant release.
	 * 
	 * @param map model map
	 * @param warrant warrant
	 * @param form warrant release form
	 * @return populate model and view
	 */
	private ModelAndView prepareWarrantReleaseMav(final ModelMap map,
			final Warrant warrant,
			final WarrantReleaseForm form){
		map.addAttribute(WARRANT_RELEASE_FORM_MODEL_KEY, form);
		
		map.addAttribute(WARRANT_REASON_CATEGORY_MODEL_KEY,
				warrant.getWarrantReason());
		map.addAttribute(OFFENDER_MODEL_KEY, warrant.getOffender());
		this.offenderSummaryModelDelegate.add(map, warrant.getOffender());
		
		return new ModelAndView(RELEASE_VIEW_NAME, map);
	}
	
	/*
	 * Prepares a model and view for the warrant cancellation edit screen.
	 *  
	 * @param map model map
	 * @param warrant warrant
	 * @param form warrant cancellation form
	 * @return model and view for editing or creating a warrant cancellation
	 */
	private ModelAndView prepareWarrantCancellationMav(final ModelMap map,
			final Warrant warrant,
			final WarrantCancellationForm form){
		map.addAttribute(WARRANT_CANCELLATION_FORM_MODEL_KEY, form);
		map.addAttribute(WARRANT_REASON_CATEGORY_MODEL_KEY,
				warrant.getWarrantReason());
		map.addAttribute(OFFENDER_MODEL_KEY, warrant.getOffender());
		this.offenderSummaryModelDelegate.add(map, warrant.getOffender());
		return new ModelAndView(CANCEL_VIEW_NAME, map);
	}
	
	/**
	 * Processes WarrantNoteItems and WarrantCauseViolationItems for the
	 * specified Warrant
	 * @param warrant - Warrant
	 * @param warrantNoteItems - WarrantNoteItems to process
	 * @param warrantCauseViolationItems - WarrantCauseViolationItems to process
	 * @throws DuplicateEntityFoundException - When a WarrantCauseViolation 
	 * already exists with the given Condition and CourtCase, or a WarrantNote
	 * already exists with the given Note and Date, for the specified Warrant
	 */
	private void processWarrantItems(final Warrant warrant,
			final List<WarrantNoteItem> warrantNoteItems,
			final List<WarrantCauseViolationItem> warrantCauseViolationItems)
					throws WarrantExistsException, WarrantNoteExistsException,
		WarrantCauseViolationExistsException {
		if(warrantNoteItems != null){
			for(WarrantNoteItem item : warrantNoteItems){
				if(WarrantItemOperation.CREATE.equals(
						item.getItemOperation())){
					this.warrantService.createWarrantNote(warrant,
							item.getNote(), item.getDate());
				}
				else if(WarrantItemOperation.UPDATE.equals(
						item.getItemOperation())){
					if(this.isNoteChanged(item.getWarrantNote(), item.getDate(),
							item.getNote())) {
						this.warrantService.updateWarrantNote(item.getWarrantNote(),
								item.getNote(), item.getDate());
					}
				}
				else if(WarrantItemOperation.REMOVE.equals(
						item.getItemOperation())){
					this.warrantService.removeWarrantNote(item.getWarrantNote());
				}
			}
		}
		
		if(warrantCauseViolationItems != null){
			for(WarrantCauseViolationItem item : warrantCauseViolationItems){
				if(WarrantItemOperation.CREATE.equals(
						item.getItemOperation())){
					this.warrantService.createWarrantCauseViolation(warrant,
							item.getConditionClause(), item.getDescription());
				}
				else if(WarrantItemOperation.UPDATE.equals(
						item.getItemOperation())){
					this.warrantService.updateWarrantCauseViolation(
							item.getWarrantCauseViolation(), item.getConditionClause(), item.getDescription());
				}
				else if(WarrantItemOperation.REMOVE.equals(
						item.getItemOperation())){
					this.warrantService.removeWarrantCauseViolation(
							item.getWarrantCauseViolation());
				}
			}
		}
	}
	
	/**
	 * Checks if a Warrant Note has been changed and returns true if it has
	 * @param note - Warrant Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is different
	 * from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final WarrantNote note,
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
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders warrants.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/warrantListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWarrantListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				WARRANT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}	
	
	/**
	 * Returns the detail report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/warrantDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWarrantDetails(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				WARRANT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}		
	
	/**
	 * Returns the authorization to pick up and hold report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/authPickUpHoldReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAuthPickUpHold(@RequestParam(
			value = "warrant", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				AUTHORIZATION_TO_PICK_UP_AND_HOLD_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	
	/**
	 * Returns the authorization to release offender report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/authReleaseOffenderReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAuthRelease(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				AUTHORIZATION_TO_RELEASE_OFFENDER_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the warrant to arrest report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/warrantToArrestReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWarrantToArrest(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				WARRANT_TO_ARREST_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}	
	
	/**
	 * Returns the warrant to arrest isc report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/warrantToArrestIscReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWarrantToArrestIsc(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				WARRANT_TO_ARREST_ISC_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	
	/**
	 * Returns the warrant cancellation authorization form for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/authCancelWarrantReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAuthCancellation(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				AUTHORIZATION_TO_CANCEL_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	
	/**
	 * Returns the warrant cancellation authorization form for the specified isc warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/authCancelISCWarrantReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportAuthCancellationISC(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				AUTHORIZATION_TO_CANCEL_ISC_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the warrant cancellation details report for the specified warrant.
	 * 
	 * @param Warrant warrant
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/warrantCancellationDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WARRANT_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWarrantCancellationDetails(@RequestParam(
			value = "offender", required = true)
			final Warrant warrant,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WARRANT_ID_REPORT_PARAM_NAME,
				Long.toString(warrant.getId()));
		byte[] doc = this.reportRunner.runReport(
				WARRANT_CANCELLATION_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
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
		binder.registerCustomEditor(Warrant.class,
				this.warrantPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(WarrantRelease.class,
				this.warrantReleasePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(WarrantCancellation.class,
				this.warrantCancellationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(WarrantArrest.class,
				this.warrantArrestPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(WarrantNote.class,
				this.warrantNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(WarrantCauseViolation.class,
				this.warrantCauseViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				CourtCase.class,
				this.courtCasePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Condition.class,
				this.conditionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Jail.class,
				this.jailPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				County.class,
				this.countyPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Person.class,
				this.personPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Facility.class,
				this.facilityPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ConditionClause.class,
				this.conditionClausePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
	}
	
	/**
	 * Handles {@code WarrantExistsException}.
	 * 
	 * @param WarrantExistsException warrant already exists exception
	 * @return screen to handle {@code WarrantExistsException}
	 */
	@ExceptionHandler(WarrantExistsException.class)
	public ModelAndView handleWarrantExistsException(
		final WarrantExistsException warrantExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			WARRANT_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, warrantExistsException);
	}
	
	/**
	 * Handles {@code WarrantNoteExistsException}.
	 * 
	 * @param WarrantNoteExistsException warrant note already exists exception
	 * @return screen to handle {@code WarrantNoteExistsException}
	 */
	@ExceptionHandler(WarrantNoteExistsException.class)
	public ModelAndView handleWarrantNoteExistsException(
		final WarrantNoteExistsException warrantNoteExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			WARRANT_NOTE_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, warrantNoteExistsException);
	}
	
	/**
	 * Handles {@code WarrantCauseViolationExistsException}.
	 * 
	 * @param WarrantCauseViolationExistsException warrant cause violation
	 * already exists exception
	 * @return screen to handle {@code WarrantCauseViolationExistsException}
	 */
	@ExceptionHandler(WarrantCauseViolationExistsException.class)
	public ModelAndView handleWarrantCauseViolationExistsException(
		final WarrantCauseViolationExistsException
		warrantCauseViolationExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			WARRANT_CAUSE_VIOLATION_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, warrantCauseViolationExistsException);
	}
	
	/**
	 * Handles {@code WarrantArrestExistsException}.
	 * 
	 * @param WarrantArrestExistsException warrant arrest
	 * already exists exception
	 * @return screen to handle {@code WarrantArrestExistsException}
	 */
	@ExceptionHandler(WarrantArrestExistsException.class)
	public ModelAndView handleWarrantArrestExistsException(
		final WarrantArrestExistsException warrantArrestExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			WARRANT_ARREST_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, warrantArrestExistsException);
	}
	
	/**
	 * Handles {@code WarrantReleaseExistsException}.
	 * 
	 * @param WarrantReleaseExistsException warrant release
	 * already exists exception
	 * @return screen to handle {@code WarrantReleaseExistsException}
	 */
	@ExceptionHandler(WarrantReleaseExistsException.class)
	public ModelAndView handleWarrantReleaseExistsException(
		final WarrantReleaseExistsException warrantReleaseExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			WARRANT_RELEASE_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, warrantReleaseExistsException);
	}
}
