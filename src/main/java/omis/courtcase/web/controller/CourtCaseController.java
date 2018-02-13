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
package omis.courtcase.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.contact.web.controller.delegate.ContactSummaryModelDelegate;
import omis.content.RequestContentMapping;
import omis.content.RequestContentType;
import omis.court.domain.Court;
import omis.courtcase.domain.Charge;
import omis.courtcase.domain.CourtCase;
import omis.courtcase.domain.CourtCaseNote;
import omis.courtcase.domain.JurisdictionAuthority;
import omis.courtcase.domain.OffenderDangerDesignator;
import omis.courtcase.domain.component.CourtCaseFlags;
import omis.courtcase.domain.component.CourtCasePersonnel;
import omis.courtcase.exception.ChargeExistsException;
import omis.courtcase.exception.CourtCaseExistsException;
import omis.courtcase.exception.CourtCaseNoteExistsException;
import omis.courtcase.service.CourtCaseService;
import omis.courtcase.web.form.ChargeItem;
import omis.courtcase.web.form.ChargeItemOperation;
import omis.courtcase.web.form.CourtCaseForm;
import omis.courtcase.web.form.CourtCaseNoteItem;
import omis.courtcase.web.form.CourtCaseNoteItemOperation;
import omis.courtcase.web.validator.CourtCaseFormValidator;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offense.domain.Offense;
import omis.person.domain.Person;
import omis.region.domain.State;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;
import omis.web.form.FormOperation;

/**
 * Controller for court cases.
 * 
 * @author Stephen Abson
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.8 (Feb 6, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/courtCase")
@PreAuthorize("hasRole('USER')")
public class CourtCaseController {
	
	/* Redirect URLs. */
	
	private static final String SAVE_AND_CREATE_CONVICTIONS_REDIRECT_URL
		= "/conviction/create.html";

	private static final String SAVE_AND_EDIT_CONVICTIONS_REDIRECT_URL
		= "/conviction/edit.html";
	
	private static final String LIST_REDIRECT_URL = "/courtCase/list.html";
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "courtCase/edit";
	
	private static final String JUDGES_VIEW_NAME
		= "courtCase/json/judges";
	
	private static final String COURT_CASE_ACTION_MENU_VIEW_NAME 
		= "courtCase/includes/courtCaseActionMenu";
	
	private static final String COURT_CASES_ACTION_MENU_VIEW_NAME 
		= "courtCase/includes/courtCasesActionMenu";
	
	private static final String COURT_CASE_LIST_ITEM_ACTION_MENU_VIEW_NAME 
		= "courtCase/includes/courtCaseListItemActionMenu";
	
	private static final String CHARGE_TABLE_ACTION_MENU_VIEW_NAME 
		= "courtCase/includes/chargeTableActionMenu";
	
	private static final String COURT_CASE_NOTES_ACTION_MENU_VIEW_NAME 
		= "courtCase/includes/courtCaseNotesActionMenu";
	
	private static final String OFFENSE_VIEW_NAME = "offense/json/offense";
	
	private static final String OFFENSES_VIEW_NAME = "offense/json/offenses";
	
	/* Model keys. */
	
	private static final String COURTS_MODEL_KEY = "courts";
	
	private static final String COURT_CASE_MODEL_KEY = "courtCase";

	private static final String COURT_CASE_FORM_MODEL_KEY = "courtCaseForm";

	private static final String DEFENDANT_MODEL_KEY = "defendant";

	private static final String JUDGES_MODEL_KEY = "judges";
	
	private static final String CHARGE_INDEX_MODEL_KEY = "chargeIndex";

	private static final String OFFENSES_MODEL_KEY = "offenses";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String PREVIOUS_URL_MODEL_KEY = "previousUrl";

	private static final String CURRENT_CHARGE_INDEX_MODEL_KEY
		= "currentChargeIndex";
	private static final String CHARGE_ITEM_OPERATION_MODEL_KEY 
		= "operation";
	private static final String COURT_CASE_NOTE_INDEX_MODEL_KEY 
		= "courtCaseNoteIndex";
	private static final String CURRENT_COURT_CASE_NOTE_INDEX_MODEL_KEY 
		= "currentCourtCaseNoteIndex";
	private static final String COURT_CASE_NOTE_ITEM_OPERATION_MODEL_KEY
		= "courtCaseNoteOperation";
	private static final String JURISDICTION_AUTHORITIES_MODEL_KEY
		= "jurisdictionAuthorities";
	private static final String DANGER_DESIGNATORS_MODEL_KEY
		= "dangerDesignators";
	private static final String STATES_MODEL_KEY = "states";
	
	private static final String DOCKET_MODEL_KEY = "docket";
	
	private static final String HAS_CONVICTIONS_MODEL_KEY = "hasConvictions";
	
	private static final String OFFENSE_MODEL_KEY = "offense";
	
	private static final String EXISTING_DOCKETS_MODEL_KEY = "existingDockets";
	
	/* Message keys. */
	
	private static final String CHARGE_EXISTS_MESSAGE_KEY = "charge.exists";
	
	private static final String COURT_CASE_EXISTS_MESSAGE_KEY 
		= "courtCase.exists";
	
	private static final String DOCKET_EXISTS_MESSAGE_KEY = "docket.exists";
	
	private static final String COURT_CASE_NOTE_EXISTS_MESSAGE_KEY
		= "courtCaseNote.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = "omis.courtcase.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("courtCaseService")
	private CourtCaseService courtCaseService;
	
	/* Report service. */
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("courtCasePropertyEditorFactory")
	private PropertyEditorFactory courtCasePropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtPropertyEditorFactory")
	private PropertyEditorFactory courtPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offensePropertyEditorFactory")
	private PropertyEditorFactory offensePropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("chargePropertyEditorFactory")
	private PropertyEditorFactory chargePropertyEditorFactory;

	@Autowired
	@Qualifier("courtCaseNotePropertyEditorFactory")
	private PropertyEditorFactory courtCaseNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("docketPropertyEditorFactory")
	private PropertyEditorFactory docketPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("courtCaseFormValidator")
	private CourtCaseFormValidator courtCaseFormValidator;
	
	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;
	
	@Autowired
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Constructors. */
	
	/** Instantiates a default court case controller. */
	public CourtCaseController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	
	
	/**
	 * Displays the form for creating a new court case.
	 * 
	 * @param defendant defendant for whom to create new court case
	 * @param redirectUrl URL to which to redirect
	 * @param passDefendant whether defendant should be passed to URL to which
	 * to redirect
	 * @return model and view to form for new court case
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_CREATE')")
	public ModelAndView create(
			@RequestParam(value = "defendant", required = false)
				final Person defendant,
			@RequestParam(value = "docket", required = false)
				final Docket docket,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant) {
		if (docket == null && defendant == null) {
			throw new IllegalArgumentException("Defendant or docket required.");
		}
		CourtCaseForm courtCaseForm = new CourtCaseForm();
		Person person = null;
		if (docket != null) {
			courtCaseForm.setAllowDocket(false);
			courtCaseForm.setAllowExistingDocket(false);
			person = docket.getPerson();
		} else {
			courtCaseForm.setAllowDocket(true);
			courtCaseForm.setAllowExistingDocket(true);
			person = defendant;
		}
		List<Docket> existingDockets = this.courtCaseService
				.findDocketsWithoutCourtCase(person);
		final ModelMap modelMap = this.prepareModel(courtCaseForm, redirectUrl, 
				defendant, docket);
		modelMap.addAttribute(EXISTING_DOCKETS_MODEL_KEY, existingDockets);
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Displays the form for editing an existing court case.
	 * 
	 * @param courtCase court case to edit
	 * @param redirectUrl URL to which to redirect
	 * @param passDefendant whether defendant should be passed to URL to which
	 * to redirect
	 * @return model and view to form for editing court case
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_VIEW')")
	public ModelAndView edit(
			@RequestParam(value = "courtCase", required = false)
				final CourtCase courtCase,
			@RequestParam(value = "docket", required = false)
				final Docket docket,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant) {
		CourtCase crtCase;
		if (docket != null) {
			crtCase = this.courtCaseService.findByDocket(docket);
		} else if (courtCase != null) {
			crtCase = courtCase;
		} else {
			throw new IllegalArgumentException(
					"Court case or docket required.");
		}
		final CourtCaseForm courtCaseForm;
		Person defendant;
		Docket modelDocket;
		if (crtCase != null) {
			if (crtCase.getFlags() != null && 
					crtCase.getFlags().getDismissed()) {
				throw new UnsupportedOperationException(
						"Not allowed to view a dismissed court case.");
			}
			courtCaseForm = this.prepareForm(crtCase, 
					this.courtCaseService.findCharges(crtCase),
					this.courtCaseService.findNotes(crtCase));
			defendant = crtCase.getDocket().getPerson();
			modelDocket = crtCase.getDocket();
		} else {
			courtCaseForm = new CourtCaseForm();
			courtCaseForm.getDocketFields().setValue(docket.getValue());
			courtCaseForm.getDocketFields().setCourt(docket.getCourt());
			defendant = docket.getPerson();
			modelDocket = docket;
		}
		courtCaseForm.setAllowDocket(false);
		courtCaseForm.setAllowExistingDocket(false);
		final ModelMap modelMap = prepareModel(courtCaseForm, redirectUrl,
				defendant, modelDocket);
		modelMap.put(COURT_CASE_MODEL_KEY, crtCase);
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/* Prepared model. */
	private CourtCaseForm prepareForm(final CourtCase courtCase, 
			final List<Charge> charges, 
			final List<CourtCaseNote> courtCaseNotes) {
		final CourtCaseForm courtCaseForm = new CourtCaseForm();
		courtCaseForm.getDocketFields().setValue(courtCase.getDocket().getValue());
		courtCaseForm.setInterStateNumber(courtCase.getInterStateNumber());
		courtCaseForm.setInterState(courtCase.getInterState());
		courtCaseForm.setPronouncementDate(courtCase.getPronouncementDate());
		courtCaseForm.setJurisdictionAuthority(courtCase
				.getJurisdictionAuthority());
		courtCaseForm.setDangerDesignator(courtCase.getDangerDesignator());
		courtCaseForm.setSentenceReviewDate(courtCase.getSentenceReviewDate());
		courtCaseForm.getDocketFields().setCourt(courtCase.getDocket().getCourt());
		courtCaseForm.setCriminallyConvictedYouth(courtCase.getFlags()
				.getCriminallyConvictedYouth());
		courtCaseForm.setYouthTransfer(courtCase.getFlags()
				.getYouthTransfer());
		courtCaseForm.setConvictionOverturned(courtCase.getFlags()
				.getConvictionOverturned());
		courtCaseForm.setDismissed(courtCase.getFlags().getDismissed());
		courtCaseForm.setComments(courtCase.getComments());
		courtCaseForm.setJudge(courtCase.getPersonnel().getJudge());
		courtCaseForm.setDefenseAttorneyName(courtCase.getPersonnel()
				.getDefenseAttorneyName());
		courtCaseForm.setProsecutingAttorneyName(courtCase.getPersonnel()
				.getProsecutingAttorneyName());
		
		for (Charge charge : charges) {
			ChargeItem chargeItem
				= new ChargeItem();
			chargeItem.setCharge(charge);
			chargeItem.setFileDate(charge.getFileDate());
			chargeItem.setCount(charge.getCounts());
			chargeItem.setDate(charge.getDate());
			chargeItem.setOffense(charge.getOffense());
			chargeItem.setOperation(ChargeItemOperation.EDIT);
			courtCaseForm.getCharges().add(chargeItem);
		}
		for (CourtCaseNote courtCaseNote : courtCaseNotes) {
			CourtCaseNoteItem courtCaseNoteItem = new CourtCaseNoteItem();
			courtCaseNoteItem.setCourtCaseNote(courtCaseNote);
			courtCaseNoteItem.setDate(courtCaseNote.getDate());
			courtCaseNoteItem.setValue(courtCaseNote.getValue());
			courtCaseNoteItem.setOperation(CourtCaseNoteItemOperation.EDIT);
			courtCaseForm.getNoteItems().add(courtCaseNoteItem);
		}
		return courtCaseForm;
	}
	
	// Prepares a model and view to display court case form
	private ModelMap prepareModel(final CourtCaseForm courtCaseForm,
			final String previousUrl, final Person defendant, 
			final Docket docket) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(COURT_CASE_FORM_MODEL_KEY, courtCaseForm);
		List<Court> courts = this.courtCaseService.findCourts();
		modelMap.put(COURTS_MODEL_KEY, courts);
		int noteCount;
		if (courtCaseForm.getNoteItems() != null) {
			noteCount = courtCaseForm.getNoteItems().size();
		} else {
			noteCount = 0;
		}
		modelMap.put(PREVIOUS_URL_MODEL_KEY, previousUrl);
		int chargeCount;
		if (courtCaseForm.getCharges() == null) {
			chargeCount = 0;
		} else {
			chargeCount = courtCaseForm.getCharges().size();
		}
		modelMap.put(CURRENT_CHARGE_INDEX_MODEL_KEY, chargeCount);
		modelMap.put(CURRENT_COURT_CASE_NOTE_INDEX_MODEL_KEY, noteCount);
		Person person;
		if (docket != null) {
			person = docket.getPerson();
		} else {
			person = defendant;
		}
		modelMap.put(DEFENDANT_MODEL_KEY, person);	
		modelMap.put(DOCKET_MODEL_KEY, docket);
		if (this.offenderReportService.summarizeIfOffender(person) != null) {
			// TODO - Add service method that returns offender from person - SA
			this.offenderSummaryModelDelegate.add(modelMap, (Offender) person);
		} else {
			this.contactSummaryModelDelegate.add(modelMap, person);
		}
		modelMap.put(JURISDICTION_AUTHORITIES_MODEL_KEY, 
				JurisdictionAuthority.values());
		modelMap.put(DANGER_DESIGNATORS_MODEL_KEY, 
				OffenderDangerDesignator.values());
		modelMap.put(STATES_MODEL_KEY, this.courtCaseService.findStates());
		return modelMap;
	}
	
	/**
	 * Saves a new court case.
	 * 
	 * @param defendant defendant
	 * @param redirectUrl URL to which to redirect
	 * @param passDefendant whether defendant should be passed to URL to which
	 * to redirect
	 * @param operation form operation
	 * @param courtCaseForm court case form
	 * @param result binding result
	 * @return redirect to specified URL or listing screen
	 * @throws CourtCaseExistsException if court case exists
	 * @throws DocketExistsException  if docket exists
	 * @throws ChargeExistsException if charge exists
	 * @throws CourtCaseNoteExistsException  if court case note exists
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_CREATE')")
	public ModelAndView save(
			@RequestParam(value = "defendant", required = false)
				final Person defendant,
			@RequestParam(value = "docket", required = false)
				final Docket docket,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant,
			@RequestParam(value = "operation", required = true)
				final FormOperation operation,
			final CourtCaseForm courtCaseForm,
			final BindingResult result) throws CourtCaseExistsException, 
			DocketExistsException, ChargeExistsException, 
			CourtCaseNoteExistsException {
		if (defendant == null && docket == null) {
			throw new IllegalArgumentException("Defendant or docket required.");
		}
		// Performs validation
		this.courtCaseFormValidator.validate(courtCaseForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(courtCaseForm, 
					redirectUrl, result, defendant, docket, null);
			Person person = null;
			if (docket != null) {
				person = docket.getPerson();
			} else {
				person = defendant;
			}
			List<Docket> existingDockets = this.courtCaseService
					.findDocketsWithoutCourtCase(person);
			mav.addObject(EXISTING_DOCKETS_MODEL_KEY, existingDockets);
			return mav;
		}
		
		// Creates initial court case with first charge
		ChargeItem firstChargeItem = courtCaseForm.getCharges().get(0);
		
		CourtCasePersonnel courtCasePersonnel = new CourtCasePersonnel();
		courtCasePersonnel.setDefenseAttorneyName(courtCaseForm
				.getDefenseAttorneyName());
		courtCasePersonnel.setProsecutingAttorneyName(courtCaseForm
				.getProsecutingAttorneyName());
		courtCasePersonnel.setJudge(courtCaseForm.getJudge());
		
		CourtCaseFlags courtCaseFlags = new CourtCaseFlags();
		courtCaseFlags.setCriminallyConvictedYouth(courtCaseForm
				.getCriminallyConvictedYouth());
		courtCaseFlags.setYouthTransfer(courtCaseForm.getYouthTransfer());
		courtCaseFlags.setConvictionOverturned(courtCaseForm
				.getConvictionOverturned());
		courtCaseFlags.setDismissed(courtCaseForm.getDismissed());
		
		CourtCase courtCase;
		Docket existingDocket = null;
		if (docket != null) {
			existingDocket = docket;
		} else if (courtCaseForm.getExistingDocket() != null) {
			existingDocket = courtCaseForm.getExistingDocket();
		}
		if (existingDocket != null) {
			courtCase = this.courtCaseService.createFromDocket(existingDocket, 
					courtCaseForm.getInterStateNumber(), 
					courtCaseForm.getInterState(), 
					courtCaseForm.getPronouncementDate(), 
					courtCaseForm.getJurisdictionAuthority(), 
					courtCaseForm.getSentenceReviewDate(), 
					courtCaseForm.getDangerDesignator(), 
					firstChargeItem.getOffense(), firstChargeItem.getDate(),
					firstChargeItem.getFileDate(), firstChargeItem.getCount(), 
					courtCasePersonnel, courtCaseFlags, 
					courtCaseForm.getComments());
		} else {
			courtCase = this.courtCaseService.create(defendant, 
					courtCaseForm.getDocketFields().getCourt(), 
					courtCaseForm.getDocketFields().getValue(), 
					courtCaseForm.getInterStateNumber(),
					courtCaseForm.getInterState(), 
					courtCaseForm.getPronouncementDate(),
					courtCaseForm.getJurisdictionAuthority(),
					courtCaseForm.getSentenceReviewDate(),
					courtCaseForm.getDangerDesignator(), courtCasePersonnel, 
					courtCaseFlags, courtCaseForm.getComments(),
					firstChargeItem.getOffense(), firstChargeItem.getDate(),
					firstChargeItem.getFileDate(), 
					firstChargeItem.getCount());
		}
		// Adds remaining charges if they exist
		if (courtCaseForm.getCharges().size() > 1) {
			for (ChargeItem chargeItem : courtCaseForm.getCharges()
					.subList(1, courtCaseForm.getCharges().size())) {
				this.courtCaseService.createCharge(courtCase,
						chargeItem.getOffense(), chargeItem.getDate(),
						chargeItem.getFileDate(), chargeItem.getCount());
			}
		}
		this.processNotes(courtCase, courtCaseForm.getNoteItems());
		// Redirect on completion
		if (FormOperation.SAVE_AND_CONTINUE.equals(operation)) {
			return new ModelAndView(
					"redirect:" + SAVE_AND_CREATE_CONVICTIONS_REDIRECT_URL
					+ "?courtCase=" + courtCase.getId());
		} else if (FormOperation.SAVE.equals(operation)) {
			String url;
			Boolean passDefend = passDefendant;
			if (redirectUrl != null) {
				url = redirectUrl;
			} else {
				url = LIST_REDIRECT_URL;
				passDefend = true;
			}
			if (existingDocket != null) {
				return buildRedirectMav(url, passDefend, 
						existingDocket.getPerson());
			} else {
				return buildRedirectMav(url, passDefend, defendant);	
			}
		} else {
			throw new IllegalArgumentException(
					String.format("Unsupported form operation: %s",
							operation));
		}
	}
	
	/**
	 * Updates an existing court case.
	 * 
	 * @param courtCase court case to update
	 * @param docket docket of court case to update
	 * @param redirectUrl URL to which to redirect
	 * @param passDefendant whether defendant should be passed to URL to which
	 * to redirect
	 * @param operation form operation
	 * @param courtCaseForm court case form
	 * @param result binding result
	 * @return redirect to specified URL or listing screen
	 * @throws ChargeExistsException if charge exists
	 * @throws CourtCaseNoteExistsException if court case note exists
	 * @throws CourtCaseExistsException if court case exists
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_EDIT')")
	public ModelAndView update(
			@RequestParam(value = "courtCase", required = false)
				final CourtCase courtCase,
			@RequestParam(value = "docket", required = false)
				final Docket docket,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant,
			@RequestParam(value = "operation", required = true)
				final FormOperation operation,
			final CourtCaseForm courtCaseForm,
			final BindingResult result) 
					throws CourtCaseNoteExistsException, ChargeExistsException, CourtCaseExistsException {
		CourtCase crtCase;
		if (docket != null) {
			crtCase = this.courtCaseService.findByDocket(docket);
		} else if (courtCase != null) {
			crtCase = courtCase;
		} else {
			throw new IllegalArgumentException(
					"Court case or docket required.");
		}
		this.courtCaseFormValidator.validate(courtCaseForm, result);
		if (result.hasErrors()) {
			if (crtCase != null) {
				return this.prepareRedisplayMav(courtCaseForm, redirectUrl, 
						result, crtCase.getDocket().getPerson(), 
						crtCase.getDocket(), crtCase);
			} else {
				return this.prepareRedisplayMav(courtCaseForm, redirectUrl, 
						result, docket.getPerson(), docket, null);
			}
		} 
		
		CourtCasePersonnel courtCasePersonnel = new CourtCasePersonnel();
		courtCasePersonnel.setDefenseAttorneyName(courtCaseForm
				.getDefenseAttorneyName());
		courtCasePersonnel.setProsecutingAttorneyName(courtCaseForm
				.getProsecutingAttorneyName());
		courtCasePersonnel.setJudge(courtCaseForm.getJudge());
		
		CourtCaseFlags courtCaseFlags = new CourtCaseFlags();
		courtCaseFlags.setCriminallyConvictedYouth(courtCaseForm
				.getCriminallyConvictedYouth());
		courtCaseFlags.setYouthTransfer(courtCaseForm.getYouthTransfer());
		courtCaseFlags.setConvictionOverturned(courtCaseForm
				.getConvictionOverturned());
		courtCaseFlags.setDismissed(courtCaseForm.getDismissed());
		List<ChargeItem> charges = new ArrayList<ChargeItem>();
		
		if (crtCase != null) {
			this.courtCaseService.update(crtCase, 
					courtCaseForm.getInterStateNumber(),
					courtCaseForm.getInterState(),
					courtCaseForm.getPronouncementDate(),
					courtCaseForm.getJurisdictionAuthority(),
					courtCaseForm.getSentenceReviewDate(),
					courtCaseForm.getDangerDesignator(), courtCasePersonnel, 
					courtCaseFlags, courtCaseForm.getComments());
			charges = courtCaseForm.getCharges();
			
		} else {
			ChargeItem firstChargeItem = courtCaseForm.getCharges().get(0);
			crtCase = this.courtCaseService.createFromDocket(docket, 
					courtCaseForm.getInterStateNumber(), 
					courtCaseForm.getInterState(), 
					courtCaseForm.getPronouncementDate(), 
					courtCaseForm.getJurisdictionAuthority(), 
					courtCaseForm.getSentenceReviewDate(), 
					courtCaseForm.getDangerDesignator(), 
					firstChargeItem.getOffense(), firstChargeItem.getDate(),
					firstChargeItem.getFileDate(), firstChargeItem.getCount(), 
					courtCasePersonnel, courtCaseFlags, 
					courtCaseForm.getComments());
			if (courtCaseForm.getCharges().size() > 1) {
				charges = courtCaseForm.getCharges().subList(1, 
						courtCaseForm.getCharges().size());
			}
		}
		this.processCharges(crtCase, charges);
		this.processNotes(crtCase, courtCaseForm.getNoteItems());
		
		if (FormOperation.SAVE_AND_CONTINUE.equals(operation)) {
			if (this.courtCaseService.hasConvictions(crtCase)) {
				return new ModelAndView(
						"redirect:" + SAVE_AND_EDIT_CONVICTIONS_REDIRECT_URL
						+ "?courtCase=" + crtCase.getId());
			} else {
				return new ModelAndView(
						"redirect:" + SAVE_AND_CREATE_CONVICTIONS_REDIRECT_URL
						+ "?courtCase=" + crtCase.getId());
			}
		} else if (FormOperation.SAVE.equals(operation)) {
			String url;
			Boolean passDefendantFlag = passDefendant;
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				url = redirectUrl;
			} else {
				url = LIST_REDIRECT_URL;
				passDefendantFlag = true;
			}
			return buildRedirectMav(url, passDefendantFlag,
					crtCase.getDocket().getPerson());
		} else {
			throw new IllegalArgumentException(
					String.format("Unsupported form operation: %s",
							operation));
		}
	}
	
	/* Process charges. */
	private void processCharges(final CourtCase courtCase, 
			final List<ChargeItem> chargeItems) 
					throws ChargeExistsException {
		for (ChargeItem chargeItem: chargeItems) {
			if (chargeItem.getOperation() != null) {
				if (ChargeItemOperation.CREATE.equals(
						chargeItem.getOperation())) {
					this.courtCaseService.createCharge(courtCase, 
							chargeItem.getOffense(), chargeItem.getDate(), 
							chargeItem.getFileDate(), chargeItem.getCount());
				}
				if (ChargeItemOperation.EDIT.equals(
						chargeItem.getOperation())) {
					this.courtCaseService.updateCharge(chargeItem.getCharge(), 
							chargeItem.getOffense(), chargeItem.getDate(), 
							chargeItem.getFileDate(), chargeItem.getCount());
				}
				if (ChargeItemOperation.REMOVE.equals(
						chargeItem.getOperation())) {
					this.courtCaseService.removeCharge(
							chargeItem.getCharge());
				}
			}
		}
	}
	
	/* Process notes. */
	private void processNotes(final CourtCase courtCase, 
			final List<CourtCaseNoteItem> noteItems) 
					throws CourtCaseNoteExistsException {
		for (CourtCaseNoteItem noteItem : noteItems) {
			if (noteItem.getOperation() != null) {
				if (CourtCaseNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.courtCaseService.addNote(courtCase, noteItem.getDate(),
							noteItem.getValue());
				} else if (CourtCaseNoteItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.courtCaseService.updateNote(
							noteItem.getCourtCaseNote(), noteItem.getDate(), 
							noteItem.getValue());
				} else if (CourtCaseNoteItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.courtCaseService.removeNote(
							noteItem.getCourtCaseNote());
				}
				
			}
		}
	}
	
 	/** court case action menu.
	 * @param defendant defendant
	 * @return action menu. */
	@RequestContentMapping(nameKey = "courtCaseActionMenuNameKey",
			descriptionKey = "courtCaseActionMenuDescriptionKey",
			messageBundle = "omis.courtcase.msgs.courtCase",
			screenType = RequestContentType.AJAX_REQUEST)
	@RequestMapping(value = "courtCaseActionMenu.html", method = RequestMethod.GET)
	public ModelAndView courtCaseActionMenu(
			@RequestParam("defendant") final Person defendant) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(DEFENDANT_MODEL_KEY, defendant);
		return new ModelAndView(COURT_CASE_ACTION_MENU_VIEW_NAME, modelMap);
	}
	
	/** Court case action menu.
	 * @param defendant defendant
	 * @return action menu. */
	@RequestContentMapping(nameKey = "courtCasesActionMenuNameKey",
			descriptionKey = "courtCasesActionMenuDescriptionKey",
			messageBundle = "omis.courtcase.msgs.courtCase",
			screenType = RequestContentType.AJAX_REQUEST)
	@RequestMapping(value = "courtCasesActionMenu.html", method = RequestMethod.GET)
	public ModelAndView courtCasesActionMenu(@RequestParam("defendant") 
	final Person defendant) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(DEFENDANT_MODEL_KEY, defendant);
		modelMap.put(OFFENDER_MODEL_KEY, (Offender) defendant);
		return new ModelAndView(COURT_CASES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/** Court case list item action menu.
	 * @param courtCase - court case.
	 * @return action menu. */
	@RequestContentMapping(nameKey = "courtCaseListItemActionActionNameKey",
			descriptionKey = "courtCaseListItemActionDescriptionKey",
			messageBundle = "omis.courtcase.msgs.courtCase",
			screenType = RequestContentType.AJAX_REQUEST)
	@RequestMapping(value = "courtCaseListItemActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView courtCaseListItemActionMenu(
			@RequestParam("courtCase") final CourtCase courtCase,
			@RequestParam("convictionCount") final Integer convictionCount) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(COURT_CASE_MODEL_KEY, courtCase);
		Boolean hasConvictions = false;
		if (convictionCount > 0) {
			hasConvictions = true;
		}
		modelMap.put(HAS_CONVICTIONS_MODEL_KEY, hasConvictions);
		return new ModelAndView(COURT_CASE_LIST_ITEM_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/** Charges action menu.
	 * @param chargeIndex - charge index.
	 * @return action menu. */
	@RequestMapping(value = "chargeTableActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView chargesActionMenu(
			@RequestParam("chargeIndex") final Long chargeIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(CHARGE_INDEX_MODEL_KEY, chargeIndex);
		return new ModelAndView(CHARGE_TABLE_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/** 
	 * Court case note action menu.
	 * 
	 * @param courtCaseNoteIndex - court case note index.
	 * @return action menu. 
	 */
	@RequestMapping(value = "courtCaseNotesActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView courtCaseNotesActionMenu(
			@RequestParam("courtCaseNoteIndex") final Long courtCaseNoteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(COURT_CASE_NOTE_INDEX_MODEL_KEY, courtCaseNoteIndex);
		return new ModelAndView(COURT_CASE_NOTES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	// Prepares a model and view to redisplay the court case edit form 
	private ModelAndView prepareRedisplayMav(final CourtCaseForm courtCaseForm,
			final String redirectUrl, final BindingResult result,
			final Person person, final Docket docket, 
			final CourtCase courtCase) {
		ModelMap modelMap = this.prepareModel(courtCaseForm, redirectUrl, 
				person, docket);
		modelMap.put(BindingResult.MODEL_KEY_PREFIX
				+ COURT_CASE_FORM_MODEL_KEY, result);
		modelMap.put(COURT_CASE_MODEL_KEY, courtCase);
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Removes a court case.
	 * 
	 * @param courtCase court case to remove
	 * @param redirectUrl optional URL to which to redirect after removal;
	 * if not specified, redirect to listing screen
	 * @param passDefendant whether to pass the defendant to the URL
	 * @return redirect to optional URL or listing screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_REMOVE')")
	public ModelAndView remove(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant) {
		Person defendant = courtCase.getDocket().getPerson();
		this.courtCaseService.remove(courtCase);
		return buildRedirectMav(LIST_REDIRECT_URL, true, defendant);
	}
	
	@RequestMapping(value = "/dismissDocket.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_REMOVE')")
	public ModelAndView dismissDocket(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase,
			@RequestParam(value = "redirectUrl", required = false)
				final String redirectUrl,
			@RequestParam(value = "passDefendant", required = false)
				final Boolean passDefendant) {
		this.courtCaseService.dismiss(courtCase);
		return buildRedirectMav(LIST_REDIRECT_URL, true, 
				courtCase.getDocket().getPerson());
	}
	
	// Builds a model and view to redirect optionally to the specified URL;
	// otherwise to the listing screen
	private ModelAndView buildRedirectMav(final String redirectUrl,
			final Boolean passDefendant, final Person defendant) {
		String url = redirectUrl;
		if (passDefendant != null && passDefendant) {
			if (defendant == null) {
				throw new IllegalArgumentException("Defendant required");
			}
			url = url + "?defendant=" + defendant.getId();
		}
		return new ModelAndView("redirect:" + url);
	}
	
	/* Invokable AJAX calls. */
	
	/**
	 * Displays table row of charge inputs.
	 * 
	 * @param chargeIndex index of charge for which to display row of inputs
	 * @return table row of charge inputs
	 */
	@RequestMapping(value = "/addCharge.html", method = RequestMethod.GET)
	public ModelAndView addCharge(
			@RequestParam(value = "chargeIndex", required = true)
				final int chargeIndex) {
		ModelAndView mav = new ModelAndView(
				"courtCase/includes/chargeTableRow");
		mav.addObject(CHARGE_INDEX_MODEL_KEY, chargeIndex);
		mav.addObject(CHARGE_ITEM_OPERATION_MODEL_KEY, 
				ChargeItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Displays table row of court case note inputs.
	 * 
	 * @param courtCaseNoteIndex index of court case note for which to display 
	 * row of inputs
	 * @return table row of court case note inputs
	 */
	@RequestMapping(value = "/addCourtCaseNote.html", method = RequestMethod.GET)
	public ModelAndView addCourtCaseNote(
			@RequestParam(value = "courtCaseNoteIndex", required = true)
				final int courtCaseNoteIndex) {
		ModelAndView mav = new ModelAndView(
				"courtCase/includes/courtCaseNoteTableRow");
		mav.addObject(COURT_CASE_NOTE_INDEX_MODEL_KEY, courtCaseNoteIndex);
		mav.addObject(COURT_CASE_NOTE_ITEM_OPERATION_MODEL_KEY, 
				CourtCaseNoteItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Returns judges by query.
	 * 
	 * @param query query
	 * @return judges by query
	 */
	@RequestMapping(value = "/searchJudges.json", method = RequestMethod.GET)
	public ModelAndView searchJudges(
			@RequestParam(value = "term", required = true)
				final String query) {
		List<Person> judges = this.courtCaseService
				.findJudges(query.toUpperCase(), new Date());
		ModelAndView mav = new ModelAndView(JUDGES_VIEW_NAME);
		mav.addObject(JUDGES_MODEL_KEY, judges);
		return mav;
	}
	
	/**
	 * Returns judges by query.
	 * 
	 * @param query query
	 * @return judges by query
	 */
	@RequestMapping(value = "/searchOffenses.json", method = RequestMethod.GET)
	public ModelAndView searchOffenses(
			@RequestParam(value = "term", required = true)
				final String query) {
		ModelAndView mav = new ModelAndView(OFFENSES_VIEW_NAME);
		List<Offense> offenses = this.courtCaseService
				.findOffenses(query.toUpperCase());
		mav.addObject(OFFENSES_MODEL_KEY, offenses);
		return mav;
	}
	
	/**
	 * Returns URL of offense.
	 * 
	 * @param offense offense
 	 * @return URL of offense
	 */
	@RequestMapping(value = "/findOffense.json", method = RequestMethod.GET)
	public ModelAndView findOffenseUrl(
			@RequestParam(value = "offense", required = true)
				final Offense offense) {
		ModelAndView mav = new ModelAndView(OFFENSE_VIEW_NAME);
		mav.addObject(OFFENSE_MODEL_KEY, offense);
		return mav;
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles court case exists exceptions.
	 * 
	 * @param exception court case exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(CourtCaseExistsException.class)
	public ModelAndView handleCourtCaseExistsException(
			final CourtCaseExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				COURT_CASE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles docket exists exceptions.
	 * 
	 * @param exception docket exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DocketExistsException.class)
	public ModelAndView handleDocketExistsException(
			final DocketExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				DOCKET_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles charge exists exceptions.
	 * 
	 * @param exception charge exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(ChargeExistsException.class)
	public ModelAndView handleChargeExistsException(
			final ChargeExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CHARGE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles court case note exists exceptions.
	 * 
	 * @param exception court case note exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(CourtCaseNoteExistsException.class)
	public ModelAndView handleCourtCaseNoteExistsException(
			final CourtCaseNoteExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				COURT_CASE_NOTE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Init binder. */
	
	/**
	 * Initializes and binds property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(CourtCase.class,
				this.courtCasePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Court.class,
				this.courtPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offense.class,
				this.offensePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Charge.class, this
				.chargePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(CourtCaseNote.class, 
				this.courtCaseNotePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class, 
				this.statePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Docket.class, 
				this.docketPropertyEditorFactory
					.createPropertyEditor());
	}
}