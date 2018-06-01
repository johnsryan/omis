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
package omis.offenseterm.web.controller;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.contact.web.controller.delegate.ContactSummaryModelDelegate;
import omis.conviction.domain.Conviction;
import omis.conviction.domain.OffenseSeverity;
import omis.conviction.domain.component.ConvictionCredit;
import omis.conviction.domain.component.ConvictionFlags;
import omis.conviction.exception.ConvictionExistsException;
import omis.court.domain.Court;
import omis.courtcase.domain.CourtCase;
import omis.courtcase.domain.JurisdictionAuthority;
import omis.courtcase.domain.OffenderDangerDesignator;
import omis.courtcase.domain.component.CourtCaseFlags;
import omis.courtcase.domain.component.CourtCasePersonnel;
import omis.courtcase.exception.CourtCaseExistsException;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offense.domain.Offense;
import omis.offenseterm.service.OffenseTermService;
import omis.offenseterm.web.controller.delegate.OffenseItemSortDelegate;
import omis.offenseterm.web.form.OffenseItem;
import omis.offenseterm.web.form.OffenseItemConnection;
import omis.offenseterm.web.form.OffenseItemConnectionClassification;
import omis.offenseterm.web.form.OffenseItemOperation;
import omis.offenseterm.web.form.OffenseTermForm;
import omis.offenseterm.web.validator.OffenseTermFormValidator;
import omis.person.domain.Person;
import omis.region.domain.State;
import omis.sentence.domain.LegalDispositionCategory;
import omis.sentence.domain.Sentence;
import omis.sentence.domain.SentenceCategory;
import omis.sentence.domain.SentenceConnectionClassification;
import omis.sentence.domain.SentenceLengthClassification;
import omis.sentence.domain.TermRequirement;
import omis.sentence.domain.component.SentenceConnection;
import omis.sentence.exception.ConnectedSentenceExistsException;
import omis.sentence.exception.SentenceExistsException;
import omis.sentence.report.SentenceReportService;
import omis.sentence.report.SentenceSummary;
import omis.sentence.web.form.SentenceFields;
import omis.sentence.web.form.SentenceOperation;
import omis.term.domain.component.Term;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for offense terms.
 *
 * @author Stephen Abson
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.0.4 (Apr 14, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offenseTerm")
public class ManageOffenseTermController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "offenseTerm/edit";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL
		= "redirect:/offenseTerm/list.html?person=%d";
	
	/* Partial view names. */
	
	private static final String JUDGES_VIEW_NAME = "offenseTerm/json/judges";
	
	private static final String SENTENCE_CATEGORY_VIEW_NAME
		= "offenseTerm/json/sentenceCategory";
	                                                                                                                                                                                                                                                                                                                                                                                                                            
	private static final String OFFENSE_ITEM_VIEW_NAME
		= "offenseTerm/includes/offenseItem";
	
	private static final String SENTENCE_TERMS_VIEW_NAME
		= "sentence/includes/sentenceTerms";
	
	private static final String DATE_VALUE_VIEW_NAME = "common/json/dateValue";
	
	private static final String INTEGER_VALUE_VIEW_NAME
		= "common/json/integerValue";
	
	private static final String OFFENSE_VIEW_NAME = "offense/json/offense";
	
	private static final String OFFENSES_VIEW_NAME = "offense/json/offenses";

	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME
		= "offenseTerm/includes/offenseTermActionMenu";
	
	private static final String OFFENSES_ACTION_MENU_VIEW_NAME
		= "offenseTerm/includes/offensesActionMenu";
	
	/* Model keys. */
	
	private static final String OFFENSE_TERM_FORM_MODEL_KEY = "offenseTermForm";

	private static final String COURT_CASE_MODEL_KEY = "courtCase";

	private static final String EXISTING_DOCKETS_MODEL_KEY = "existingDockets";
	
	private static final String COURTS_MODEL_KEY = "courts";

	private static final String STATES_MODEL_KEY = "states";

	private static final String JURISDICTION_AUTHORITIES_MODEL_KEY
		= "jurisdictionAuthorities";

	private static final String OFFENDER_DANGER_DESIGNATORS_MODEL_KEY
		= "offenderDangerDesignators";
	
	private static final String PERSON_MODEL_KEY = "person";

	private static final String CURRENT_OFFENSE_ITEM_INDEX_MODEL_KEY
		= "currentOffenseItemIndex";

	private static final String OFFENSE_ITEM_INDEX_MODEL_KEY
		= "offenseItemIndex";

	private static final String OFFENSES_MODEL_KEY = "offenses";
	
	private static final String OFFENSE_SEVERITIES_MODEL_KEY
		= "offenseSeverities";

	private static final String OFFENSE_ITEM_MODEL_KEY = "offenseItem";

	private static final String SENTENCE_CATEGORIES_MODEL_KEY
		= "sentenceCategories";
	
	private static final String LEGAL_DISPOSITION_CATEGORIES_MODEL_KEY
		= "legalDispositionCategories";

	private static final String SENTENCE_LENGTH_CLASSIFICATIONS_MODEL_KEY
		= "sentenceLengthClassifications";

	private static final String JUDGES_MODEL_KEY = "judges";

	private static final String OTHER_SENTENCES_MODEL_KEY = "otherSentences";

	private static final String SENTENCE_CATEGORY_MODEL_KEY
		= "sentenceCategory";

	private static final String DATE_VALUE_MODEL_KEY = "dateValue";

	private static final String PRISON_YEARS_MODEL_KEY = "prisonYears";

	private static final String PRISON_MONTHS_MODEL_KEY = "prisonMonths";

	private static final String PRISON_DAYS_MODEL_KEY = "prisonDays";

	private static final String PROBATION_YEARS_MODEL_KEY = "probationYears";

	private static final String PROBATION_MONTHS_MODEL_KEY = "probationMonths";

	private static final String PROBATION_DAYS_MODEL_KEY = "probationDays";

	private static final String DEFERRED_YEARS_MODEL_KEY = "deferredTermYears";

	private static final String DEFERRED_MONTHS_MODEL_KEY = "deferredTermMonths";

	private static final String DEFERRED_DAYS_MODEL_KEY	= "deferredTermDays";

	private static final String SHOW_PRISON_TERM_MODEL_KEY = "showPrisonTerm";

	private static final String SHOW_PROBATION_TERM_MODEL_KEY
		= "showProbationTerm";

	private static final String SHOW_DEFERRED_TERM_MODEL_KEY
		= "showDeferredTerm";

	private static final String
	INACTIVE_SENTENCE_SUMMARIES_FOR_CONVICTION_MODEL_KEY
		= "inactiveSentenceSummariesForConviction";
	
	private static final String INTEGER_VALUE_MODEL_KEY = "integerValue";
	
	private static final String OFFENSE_MODEL_KEY = "offense";
	
	/* Message keys. */
	
	private static final String CONVICTION_EXISTS_MESSAGE_KEY
		= "offenseTermForm.conviction.exists";
	
	private static final String COURT_CASE_EXISTS_MESSAGE_KEY 
		= "offenseTermForm.courtCase.exists";
	
	private static final String DOCKET_EXISTS_MESSAGE_KEY 
		= "offenseTermForm.docket.exists";
	
	private static final String SENTENCE_EXISTS_MESSAGE_KEY 
		= "offenseTermForm.sentence.exists";

	private static final String CONNECTED_SENTENCE_EXISTS_MESSAGE_KEY 
		= "currentOffenseTerm.connectedSentenceExists";

	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = "omis.offenseterm.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("offenseTermService")
	private OffenseTermService offenseTermService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("docketPropertyEditorFactory")
	private PropertyEditorFactory docketPropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtCasePropertyEditorFactory")
	private PropertyEditorFactory courtCasePropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtPropertyEditorFactory")
	private PropertyEditorFactory courtPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offensePropertyEditorFactory")
	private PropertyEditorFactory offensePropertyEditorFactory;
	
	@Autowired
	@Qualifier("sentenceCategoryPropertyEditorFactory")
	private PropertyEditorFactory sentenceCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("legalDispositionCategoryPropertyEditorFactory")
	private PropertyEditorFactory legalDispositionCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("convictionPropertyEditorFactory")
	private PropertyEditorFactory convictionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("sentencePropertyEditorFactory")
	private PropertyEditorFactory sentencePropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;

	/* Validators. */
	
	@Autowired
	@Qualifier("offenseTermFormValidator")
	private OffenseTermFormValidator offenseTermFormValidator;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("sentenceReportService")
	private SentenceReportService sentenceReportService;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenseItemSortDelegate")
	private OffenseItemSortDelegate offenseItemSortDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for offense terms. */
	public ManageOffenseTermController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create court case.
	 * 
	 * @param person person
	 * @return screen to create court case
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "person", required = true)
				final Person person) {
		OffenseTermForm offenseTermForm = new OffenseTermForm();
		offenseTermForm.setAllowExistingDocket(true);
		offenseTermForm.setAllowDocketFields(true);
		List<Sentence> sentences
			= this.offenseTermService.findActiveSentences(person);
		List<Docket> existingDockets
			= this.offenseTermService.findDocketsWithoutCourtCase(person);
		ModelAndView mav = this.prepareMav(offenseTermForm, person);
		mav.addObject(OTHER_SENTENCES_MODEL_KEY, sentences);
		mav.addObject(EXISTING_DOCKETS_MODEL_KEY, existingDockets);
		return mav;
	}
	
	/**
	 * Shows screen to edit court case.
	 * 
	 * @param courtCase court case
	 * @param expandedSentence sentence to display expanded
	 * @return screen to edit court case
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase,
			@RequestParam(value = "expandedSentence", required = false)
				final Sentence expandedSentence) {
		OffenseTermForm offenseTermForm = new OffenseTermForm();
		offenseTermForm.setAllowDocketFields(false);
		offenseTermForm.getDocketFields().setCourt(
				courtCase.getDocket().getCourt());
		offenseTermForm.getDocketFields().setValue(
				courtCase.getDocket().getValue());
		offenseTermForm.getFields().setInterState(courtCase.getInterState());
		offenseTermForm.getFields().setInterStateNumber(
				courtCase.getInterStateNumber());
		offenseTermForm.getFields().setPronouncementDate(
				courtCase.getPronouncementDate());
		offenseTermForm.getFields().setJurisdictionAuthority(
				courtCase.getJurisdictionAuthority());
		offenseTermForm.getFields().setComments(courtCase.getComments());
		if (courtCase.getPersonnel() != null) {
			offenseTermForm.getFields().setJudge(
					courtCase.getPersonnel().getJudge());
			offenseTermForm.getFields().setDefenseAttorneyName(
					courtCase.getPersonnel().getDefenseAttorneyName());
			offenseTermForm.getFields().setProsecutingAttorneyName(
					courtCase.getPersonnel().getProsecutingAttorneyName());
		}
		offenseTermForm.getFields().setSentenceReviewDate(
				courtCase.getSentenceReviewDate());
		offenseTermForm.getFields().setDangerDesignator(
				courtCase.getDangerDesignator());
		if (courtCase.getFlags() != null) {
			offenseTermForm.getFields().setConvictionOverturned(
					courtCase.getFlags().getConvictionOverturned());
			offenseTermForm.getFields().setCriminallyConvictedYouth(
					courtCase.getFlags().getCriminallyConvictedYouth());
			offenseTermForm.getFields().setYouthTransfer(
					courtCase.getFlags().getYouthTransfer());
		}
		List<OffenseItem> offenses = new ArrayList<OffenseItem>();
		List<Conviction> convictions = this.offenseTermService
				.findConvictions(courtCase);
		Map<Conviction, List<SentenceSummary>>
			inactiveSentenceSummariesForConviction
				= new HashMap<Conviction, List<SentenceSummary>>();
		for (Conviction conviction : convictions) {
			OffenseItem offenseItem = new OffenseItem();
			offenseItem.setOperation(OffenseItemOperation.UPDATE);
			offenseItem.setConviction(conviction);
			offenseItem.getConvictionFields().setOffense(
					conviction.getOffense());
			offenseItem.getConvictionFields().setDate(
					conviction.getDate());
			offenseItem.getConvictionFields().setCounts(
					conviction.getCounts());
			offenseItem.getConvictionFields().setSeverity(
					conviction.getSeverity());
			if (conviction.getFlags() != null) {
				offenseItem.getConvictionFields().setViolentOffense(
						conviction.getFlags().getViolentOffense());
				offenseItem.getConvictionFields().setSexualOffense(
						conviction.getFlags().getSexualOffense());
				offenseItem.getConvictionFields().setParoleIneligible(
						conviction.getFlags().getParoleIneligible());
				offenseItem.getConvictionFields()
							.setSupervisedReleaseIneligible(
									conviction.getFlags()
										.getSupervisedReleaseIneligible());
			}
			Sentence sentence = this.offenseTermService
					.findActiveSentence(conviction);
			if (sentence != null) {
				offenseItem.setSentenceOperation(SentenceOperation.UPDATE);
				offenseItem.setSentence(sentence);
				if (sentence.getConnection() != null) {
					OffenseItemConnection connection;
					if (SentenceConnectionClassification.INITIAL
							.equals(sentence.getConnection()
									.getClassification())) {
						connection = OffenseItemConnection.createInitial();
					} else if (SentenceConnectionClassification.CONCURRENT
							.equals(sentence.getConnection()
									.getClassification())) {
						connection = OffenseItemConnection.createConcurrent();
					} else if (SentenceConnectionClassification.CONSECUTIVE
							.equals(sentence.getConnection()
									.getClassification())) {
						if (sentence.getConnection().getSentence()
								.getConviction().getCourtCase()
								.equals(courtCase)) {
							int consecutiveSentenceIndex = convictions
									.indexOf(sentence.getConnection()
											.getSentence().getConviction());
							if (consecutiveSentenceIndex > -1) {
								connection = OffenseItemConnection
									.createConsecutive(
											new Long(consecutiveSentenceIndex));
							} else {
								throw new UnsupportedOperationException(
									"Operation not supported - this is a known bug");
							}
						} else {
							connection = OffenseItemConnection
									.createConsecutiveOtherDocket(
											sentence.getConnection()
												.getSentence().getId());
						}
					} else {
						throw new UnsupportedOperationException(
									"Connection classification not supported");
					}
					offenseItem.setConnection(connection);
				}
				offenseItem.getSentenceFields()
					.setCategory(sentence.getCategory());
				offenseItem.getSentenceFields()
					.setLengthClassification(
							sentence.getLengthClassification());
				offenseItem.getSentenceFields().setLegalDispositionCategory(
							sentence.getLegalDispositionCategory());
				if (sentence.getPrisonTerm() != null) {
					offenseItem.getSentenceFields().setPrisonYears(
							sentence.getPrisonTerm().getYears());
					offenseItem.getSentenceFields().setPrisonMonths(
							sentence.getPrisonTerm().getMonths());
					offenseItem.getSentenceFields().setPrisonDays(
							sentence.getPrisonTerm().getDays());
					offenseItem.getSentenceFields().setPrisonTotalDays(
							this.offenseTermService
								.calculateTotalDays(sentence.getPrisonTerm()));
				}
				if (sentence.getProbationTerm() != null) {
					offenseItem.getSentenceFields().setProbationYears(
							sentence.getProbationTerm().getYears());
					offenseItem.getSentenceFields().setProbationMonths(
							sentence.getProbationTerm().getMonths());
					offenseItem.getSentenceFields().setProbationDays(
							sentence.getProbationTerm().getDays());
					offenseItem.getSentenceFields().setProbationTotalDays(
							this.offenseTermService
								.calculateTotalDays(
										sentence.getProbationTerm()));
				}
				if (sentence.getDeferredTerm() != null) {
					offenseItem.getSentenceFields().setDeferredYears(
							sentence.getDeferredTerm().getYears());
					offenseItem.getSentenceFields().setDeferredMonths(
							sentence.getDeferredTerm().getMonths());
					offenseItem.getSentenceFields().setDeferredDays(
							sentence.getDeferredTerm().getDays());
					offenseItem.getSentenceFields().setDeferredTotalDays(
							this.offenseTermService
								.calculateTotalDays(
										sentence.getDeferredTerm()));
				}
				offenseItem.getSentenceFields().setEffectiveDate(
						sentence.getEffectiveDate());
				offenseItem.getSentenceFields().setPronouncementDate(
						sentence.getPronouncementDate());
				if (sentence.getCredit() != null) {
					offenseItem.getSentenceFields().setJailTimeCredit(
							sentence.getCredit().getJailTime());
					offenseItem.getSentenceFields().setStreetTimeCredit(
							sentence.getCredit().getStreetTime());
				}
				offenseItem.getSentenceFields().setTurnSelfInDate(
						sentence.getTurnSelfInDate());
				if (sentence.equals(expandedSentence)) {
					offenseItem.setExpanded(true);
				} else {
					offenseItem.setExpanded(false);
				}
			} else {
				offenseItem.setExpanded(false);
			}
			offenses.add(offenseItem);
			inactiveSentenceSummariesForConviction.put(conviction,
					this.sentenceReportService
						.summarizeInactiveSentencesByConviction(conviction));
		}
		offenseTermForm.setOffenseItems(offenses);
		List<Sentence> otherSentences = this.offenseTermService
				.findActiveSentencesForOtherCourtCases(
						courtCase.getDocket().getPerson(), courtCase);
		ModelAndView mav = this.prepareMav(offenseTermForm,
				courtCase.getDocket().getPerson());
		mav.addObject(OTHER_SENTENCES_MODEL_KEY, otherSentences);
		mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
		mav.addObject(INACTIVE_SENTENCE_SUMMARIES_FOR_CONVICTION_MODEL_KEY,
				inactiveSentenceSummariesForConviction);
		return mav;
	}
	
	/**
	 * Saves court case.
	 * 
	 * @param person person
	 * @param offenseTermForm form for offense term
	 * @param bindingResult binding result
	 * @return redirect to offense term listing screen
	 * @throws CourtCaseExistsException if court case exists
	 * @throws ConvictionExistsException if conviction exists
	 * @throws SentenceExistsException if sentence exists
	 * @throws DocketExistsException 
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "person", required = true)
				final Person person,
			final OffenseTermForm offenseTermForm,
			final BindingResult bindingResult)
				throws CourtCaseExistsException,
					ConvictionExistsException,
					SentenceExistsException,
					DocketExistsException {
		
		// Performs form validation
		this.offenseTermFormValidator.validate(offenseTermForm, bindingResult);
		if (bindingResult.hasErrors()) {
			List<Sentence> sentences
				= this.offenseTermService.findActiveSentences(person);
			ModelAndView mav = this.prepareRedisplay(
					offenseTermForm, bindingResult, person);
			List<Docket> existingDockets
				= this.offenseTermService.findDocketsWithoutCourtCase(person);
			mav.addObject(OTHER_SENTENCES_MODEL_KEY, sentences);
			mav.addObject(EXISTING_DOCKETS_MODEL_KEY, existingDockets);
			return mav;
		}
		
		
		// Creates or uses existing docket
		Docket docket;
		if (offenseTermForm.getExistingDocket() != null) {
			docket = offenseTermForm.getExistingDocket();
		} else {
			docket = this.offenseTermService.createDocket(person,
						offenseTermForm.getDocketFields().getCourt(),
						offenseTermForm.getDocketFields().getValue());
		}
		
		// Creates court case
		CourtCase courtCase = this.offenseTermService.create(
				docket,
				offenseTermForm.getFields().getInterStateNumber(),
				offenseTermForm.getFields().getInterState(),
				offenseTermForm.getFields().getPronouncementDate(),
				offenseTermForm.getFields().getSentenceReviewDate(),
				offenseTermForm.getFields().getJurisdictionAuthority(),
				offenseTermForm.getFields().getDangerDesignator(),
				new CourtCasePersonnel(
						offenseTermForm.getFields().getJudge(),
						offenseTermForm.getFields().getDefenseAttorneyName(),
						offenseTermForm.getFields()
							.getProsecutingAttorneyName()),
				new CourtCaseFlags(
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getCriminallyConvictedYouth()),
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getYouthTransfer()),
						false,
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getConvictionOverturned())),
				offenseTermForm.getFields().getComments());
		
		// Tracks newly created sentences and their unordered index
		// This is used to consecutively connect sentences created inline
		// in order of dependency
		// Sentences must be created in order of dependency by still be
		// referrable by their original index
		Map<Long, Sentence> unorderedIndexedSentences
			= new HashMap<Long, Sentence>();
		
		// Processes offense item in order of dependency of consecutive
		// sentences
		List<OffenseItem> orderedOffenseItems
			= this.offenseItemSortDelegate.sort(offenseTermForm
					.getOffenseItems());
		for (int index = 0; index < orderedOffenseItems.size(); index++) {
			OffenseItem offenseItem = orderedOffenseItems.get(index);
			
			// Creates conviction
			Conviction conviction;
			if (OffenseItemOperation.CREATE.equals(
					offenseItem.getOperation())) {
				conviction = this.offenseTermService
					.createConviction(courtCase,
						offenseItem.getConvictionFields()
							.getOffense(),
						offenseItem.getConvictionFields()
							.getDate(),
						offenseItem.getConvictionFields()
							.getCounts(),
						offenseItem.getConvictionFields()
							.getSeverity(),
						new ConvictionFlags(
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getViolentOffense()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getSexualOffense()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getParoleIneligible()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getSupervisedReleaseIneligible())));
			} else {
				
				// Only creation of new conviction is allowed from
				// offense term create screen
				throw new UnsupportedOperationException(
						String.format("Operation not supported: %s",
								offenseItem.getOperation()));
			}
			
			// An offense term can also result in a sentence
			if (SentenceOperation.CREATE
					.equals(offenseItem.getSentenceOperation())) {
				
				// Determines connection classification to use and consecutive
				// sentence if required by classification
				SentenceConnection connection;
				if (OffenseItemConnectionClassification.INITIAL
						.equals(offenseItem.getConnection()
								.getClassification())) {
					connection = SentenceConnection.createInitial();
				} else if (OffenseItemConnectionClassification.CONCURRENT
						.equals(offenseItem.getConnection()
								.getClassification())) {
					connection = SentenceConnection.createConcurrent();
				} else if (OffenseItemConnectionClassification.CONSECUTIVE
						.equals(offenseItem.getConnection()
								.getClassification())) {
					Sentence consecutiveSentence = unorderedIndexedSentences
							.get(offenseItem.getConnection().getIndex());
					connection = SentenceConnection
							.createConsecutive(consecutiveSentence);
				} else if (OffenseItemConnectionClassification
						.CONSECUTIVE_OTHER_DOCKET.equals(
								offenseItem.getConnection()
									.getClassification())) {
					Sentence consecutiveSentence = this.resolveSentence(
							offenseItem.getConnection().getIndex().toString());
					connection = SentenceConnection
							.createConsecutive(consecutiveSentence);
				} else {
					throw new UnsupportedOperationException(
							String.format(
									"Unsupported connection classification: %s",
									offenseItem.getConnection()
										.getClassification()));
				}
				
				// Determines prison, probation and deferred terms if category
				// allows them
				final Term prisonTerm;
				if (TermRequirement.REQUIRED.equals(offenseItem
						.getSentenceFields().getCategory()
						.getPrisonRequirement()) || 
						TermRequirement.OPTIONAL.equals(offenseItem
								.getSentenceFields().getCategory()
								.getPrisonRequirement())) {
					prisonTerm = new Term(
						offenseItem.getSentenceFields()
							.getPrisonYears(),
						offenseItem.getSentenceFields()
							.getPrisonMonths(),
						offenseItem.getSentenceFields()
							.getPrisonDays());
				} else {
					prisonTerm = null;
				}
				final Term probationTerm;
				if (TermRequirement.REQUIRED.equals(offenseItem
						.getSentenceFields().getCategory()
						.getProbationRequirement()) || 
						TermRequirement.OPTIONAL.equals(offenseItem
								.getSentenceFields().getCategory()
								.getProbationRequirement())) {
					probationTerm = new Term(
						offenseItem.getSentenceFields()
							.getProbationYears(),
						offenseItem.getSentenceFields()
							.getProbationMonths(),
						offenseItem.getSentenceFields()
							.getProbationDays());
				} else {
					probationTerm = null;
				}
				final Term deferredTerm;
				if (TermRequirement.REQUIRED.equals(offenseItem
						.getSentenceFields().getCategory()
						.getDeferredRequirement()) || 
						TermRequirement.OPTIONAL.equals(offenseItem
								.getSentenceFields().getCategory()
								.getDeferredRequirement())) {
					deferredTerm = new Term(
						offenseItem.getSentenceFields()
							.getDeferredYears(),
						offenseItem.getSentenceFields()
							.getDeferredMonths(),
						offenseItem.getSentenceFields()
							.getDeferredDays());
				} else {
					deferredTerm = null;
				}
				
				// Determines credit
				ConvictionCredit credit = new ConvictionCredit(
						offenseItem.getSentenceFields().getJailTimeCredit(),
						offenseItem.getSentenceFields().getStreetTimeCredit());
				
				// Creates new sentence
				Sentence sentence = this.offenseTermService.sentence(
						conviction, connection,
						offenseItem.getSentenceFields().getCategory(),
						offenseItem.getSentenceFields()
							.getLengthClassification(),
						offenseItem.getSentenceFields()
							.getLegalDispositionCategory(),
						offenseItem.getSentenceFields()
							.getPronouncementDate(),
						credit,
						offenseItem.getSentenceFields()
							.getEffectiveDate(),
						offenseItem.getSentenceFields()
							.getTurnSelfInDate(),
						prisonTerm, probationTerm, deferredTerm);
				
				// Associates newly created sentences with unordered
				// offense items
				int unorderedIndex = offenseTermForm.getOffenseItems()
						.indexOf(offenseItem);
				unorderedIndexedSentences.put(
						new Long(unorderedIndex), sentence);
			} else if (offenseItem.getSentenceOperation() != null) {
				
				// Sentences can only be created (not updated or amended)
				// from offense term create screen
				throw new UnsupportedOperationException(
						String.format("Cannot perform %s sentence operation on"
								+ " new conviction",
							offenseItem.getSentenceOperation()));
			}
		}
		
		// Returns to offense term listing screen
		return new ModelAndView(String.format(REDIRECT_URL, person.getId()));
	}
	
	/**
	 * Updates court case.
	 * 
	 * @param courtCase court case
	 * @param offenseTermForm form for offense term
	 * @param bindingResult binding result
	 * @return redirect to offense term listing screen
	 * @throws ConnectedSentenceExistsException 
	 * @throws ConvictionExistsException 
	 * @throws SentenceExistsException 
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase,
			final OffenseTermForm offenseTermForm,
			final BindingResult bindingResult)
				throws ConnectedSentenceExistsException, 
				ConvictionExistsException, SentenceExistsException {
		this.offenseTermFormValidator.validate(offenseTermForm, bindingResult);
		if (bindingResult.hasErrors()) {
			List<Sentence> otherSentences = this.offenseTermService
					.findActiveSentencesForOtherCourtCases(
							courtCase.getDocket().getPerson(), courtCase);
			ModelAndView mav = this.prepareRedisplay(
					offenseTermForm, bindingResult,
					courtCase.getDocket().getPerson());
			mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
			mav.addObject(OTHER_SENTENCES_MODEL_KEY, otherSentences);
			return mav;
		}
		Boolean dismissed;
		if (courtCase.getFlags() != null) {
			dismissed = courtCase.getFlags().getDismissed();
		} else {
			dismissed = false;
		}
		this.offenseTermService.update(
				courtCase,
				offenseTermForm.getFields().getInterStateNumber(),
				offenseTermForm.getFields().getInterState(),
				offenseTermForm.getFields().getPronouncementDate(),
				offenseTermForm.getFields().getSentenceReviewDate(),
				offenseTermForm.getFields().getJurisdictionAuthority(),
				offenseTermForm.getFields().getDangerDesignator(),
				new CourtCasePersonnel(
					offenseTermForm.getFields().getJudge(),
					offenseTermForm.getFields().getDefenseAttorneyName(),
					offenseTermForm.getFields().getProsecutingAttorneyName()),
				new CourtCaseFlags(
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getCriminallyConvictedYouth()),
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getYouthTransfer()),
						dismissed,
						this.resolveCheckBoxValue(offenseTermForm.getFields()
							.getConvictionOverturned())),
				offenseTermForm.getFields().getComments());
		
		// Tracks newly created sentences and sentences for current court cases
		// that are allowed to be updated from this screen
		// Used to consecutively connect such sentences
		// New sentences must be created in order of dependency but still be
		// referrable by their original index
		Map<Long, Sentence> unorderedIndexedSentences
			= new HashMap<Long, Sentence>();
		
		// Processes offense item in order of dependency of consecutive
		// sentences
		List<OffenseItem> orderedOffenseItems
			= this.offenseItemSortDelegate.sort(offenseTermForm
					.getOffenseItems());
		for (int index = 0; index < orderedOffenseItems.size(); index++) {
			OffenseItem offenseItem = orderedOffenseItems.get(index);
			if (OffenseItemOperation.CREATE.equals(
					offenseItem.getOperation())
					|| OffenseItemOperation.UPDATE.equals(
							offenseItem.getOperation())) {
				
				// Each create/update operation results in a conviction
				// The conviction has flags
				Conviction conviction;
				ConvictionFlags convictionFlags = new ConvictionFlags(
						this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getViolentOffense()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getSexualOffense()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getParoleIneligible()),
							this.resolveCheckBoxValue(
								offenseItem.getConvictionFields()
									.getSupervisedReleaseIneligible()));
				
				// Creates conviction without charge
				if (OffenseItemOperation.CREATE.equals(
						offenseItem.getOperation())) {
					conviction = this.offenseTermService
						.createConviction(courtCase,
							offenseItem.getConvictionFields()
								.getOffense(),
							offenseItem.getConvictionFields()
								.getDate(),
							offenseItem.getConvictionFields()
								.getCounts(),
							offenseItem.getConvictionFields()
								.getSeverity(),
							convictionFlags);
				} else if (OffenseItemOperation.UPDATE.equals(
						offenseItem.getOperation())) {
					conviction = this.offenseTermService
						.updateConviction(
							offenseItem.getConviction(),
							offenseItem.getConvictionFields()
								.getOffense(),
							offenseItem.getConvictionFields()
								.getDate(),
							offenseItem.getConvictionFields()
								.getCounts(),
							offenseItem.getConvictionFields()
								.getSeverity(),
							convictionFlags);
				} else {
					throw new AssertionError("Logically impossible condition");
				}
				
				if (SentenceOperation.CREATE
						.equals(offenseItem.getSentenceOperation())
						|| SentenceOperation.UPDATE.equals(
								offenseItem.getSentenceOperation()) 
						|| SentenceOperation.AMEND.equals(
								offenseItem.getSentenceOperation())) {
					
					// Determines connection classification to use and
					// consecutive  sentence if required by classification
					SentenceConnection connection;
					if (OffenseItemConnectionClassification.INITIAL
							.equals(offenseItem.getConnection()
									.getClassification())) {
						connection = SentenceConnection.createInitial();
					} else if (OffenseItemConnectionClassification.CONCURRENT
							.equals(offenseItem.getConnection()
									.getClassification())) {
						connection = SentenceConnection.createConcurrent();
					} else if (OffenseItemConnectionClassification.CONSECUTIVE
							.equals(offenseItem.getConnection()
									.getClassification())) {
						Sentence consecutiveSentence = unorderedIndexedSentences
								.get(offenseItem.getConnection().getIndex());
						connection = SentenceConnection
								.createConsecutive(consecutiveSentence);
					} else if (OffenseItemConnectionClassification
							.CONSECUTIVE_OTHER_DOCKET.equals(
									offenseItem.getConnection()
										.getClassification())) {
						Sentence consecutiveSentence = this.resolveSentence(
							offenseItem.getConnection().getIndex().toString());
						connection = SentenceConnection
								.createConsecutive(consecutiveSentence);
					} else {
						throw new UnsupportedOperationException(
								String.format(
									"Unsupported connection classification: %s",
									offenseItem.getConnection()
										.getClassification()));
					}
					
					// Determines prison, probation and deferred terms if
					// category  allows them
					final Term prisonTerm;
					if (TermRequirement.REQUIRED.equals(offenseItem
							.getSentenceFields().getCategory()
							.getPrisonRequirement()) || 
							TermRequirement.OPTIONAL.equals(offenseItem
									.getSentenceFields().getCategory()
									.getPrisonRequirement())) {
						prisonTerm = new Term(
							offenseItem.getSentenceFields()
								.getPrisonYears(),
							offenseItem.getSentenceFields()
								.getPrisonMonths(),
							offenseItem.getSentenceFields()
								.getPrisonDays());
					} else {
						prisonTerm = null;
					}
					final Term probationTerm;
					if (TermRequirement.REQUIRED.equals(offenseItem
							.getSentenceFields().getCategory()
							.getProbationRequirement()) || 
							TermRequirement.OPTIONAL.equals(offenseItem
									.getSentenceFields().getCategory()
									.getProbationRequirement())) {
						probationTerm = new Term(
							offenseItem.getSentenceFields()
								.getProbationYears(),
							offenseItem.getSentenceFields()
								.getProbationMonths(),
							offenseItem.getSentenceFields()
								.getProbationDays());
					} else {
						probationTerm = null;
					}
					final Term deferredTerm;
					if (TermRequirement.REQUIRED.equals(offenseItem
							.getSentenceFields().getCategory()
							.getDeferredRequirement()) || 
							TermRequirement.OPTIONAL.equals(offenseItem
									.getSentenceFields().getCategory()
									.getDeferredRequirement())) {
						deferredTerm = new Term(
							offenseItem.getSentenceFields()
								.getDeferredYears(),
							offenseItem.getSentenceFields()
								.getDeferredMonths(),
							offenseItem.getSentenceFields()
								.getDeferredDays());
					} else {
						deferredTerm = null;
					}
					
					// Determines credit
					ConvictionCredit credit = new ConvictionCredit(
						offenseItem.getSentenceFields().getJailTimeCredit(),
						offenseItem.getSentenceFields().getStreetTimeCredit());
					
					// Creates, updates or amends sentence
					Sentence sentence;
					if (SentenceOperation.CREATE
							.equals(offenseItem.getSentenceOperation())) {
						sentence = this.offenseTermService.sentence(
								conviction, connection,
								offenseItem.getSentenceFields().getCategory(),
								offenseItem.getSentenceFields()
									.getLengthClassification(),
								offenseItem.getSentenceFields()
									.getLegalDispositionCategory(),
								offenseItem.getSentenceFields()
									.getPronouncementDate(),
								credit,
								offenseItem.getSentenceFields()
									.getEffectiveDate(),
								offenseItem.getSentenceFields()
									.getTurnSelfInDate(),
								prisonTerm, probationTerm, deferredTerm);
					} else if (SentenceOperation.UPDATE
								.equals(offenseItem.getSentenceOperation())) {
						sentence = this.offenseTermService.updateSentence(
								offenseItem.getSentence(), connection,
								offenseItem.getSentenceFields()
									.getCategory(),
								offenseItem.getSentenceFields()
									.getLengthClassification(),
								offenseItem.getSentenceFields()
									.getLegalDispositionCategory(),
								offenseItem.getSentenceFields()
									.getPronouncementDate(),
								credit,
								offenseItem.getSentenceFields()
									.getEffectiveDate(),
								offenseItem.getSentenceFields()
									.getTurnSelfInDate(),
								prisonTerm, probationTerm, deferredTerm);
					} else if (SentenceOperation.AMEND
							.equals(offenseItem.getSentenceOperation())) {
						sentence = this.offenseTermService.amendSentence(
								offenseItem.getSentence(), connection,
								offenseItem.getSentenceFields().getCategory(),
								offenseItem.getSentenceFields()
									.getLengthClassification(),
								offenseItem.getSentenceFields()
									.getLegalDispositionCategory(),
								offenseItem.getSentenceFields()
									.getPronouncementDate(),
								credit,
								offenseItem.getSentenceFields()
									.getEffectiveDate(),
								offenseItem.getSentenceFields()
									.getTurnSelfInDate(),
								prisonTerm, probationTerm, deferredTerm);
					} else {
						throw new AssertionError(
								"Logically impossible condition");
					}
					
					// Associates created, updated or amended sentences with
					// offense items
					int unorderedIndex = offenseTermForm.getOffenseItems()
							.indexOf(offenseItem);
					unorderedIndexedSentences.put(
							new Long(unorderedIndex), sentence);
				} else if (SentenceOperation.REMOVE.equals(
						offenseItem.getSentenceOperation())) {
					this.offenseTermService.removeSentence(
							offenseItem.getSentence());
				} else if (offenseItem.getSentenceOperation() != null) {
					throw new UnsupportedOperationException(
						String.format("Unsupported sentence operation: %s",
								offenseItem.getOperation()));
				}
			} else if (OffenseItemOperation.REMOVE.equals(
					offenseItem.getOperation())) {
				this.offenseTermService.removeConviction(
						offenseItem.getConviction());
			} else {
				
				// Only creation, update or removal is supported from edit
				// screen
				throw new UnsupportedOperationException(
						String.format("Operation not supported: %s",
								offenseItem.getOperation()));
			}
		}
		return new ModelAndView(String.format(REDIRECT_URL,
				courtCase.getDocket().getPerson().getId()));
	}
	
	/**
	 * Removes court case.
	 * 
	 * @param courtCase court case
	 * @return redirect to offense term listing screen
	 * @throws ConnectedSentenceExistsException if sentences exist that
	 * are connected to sentences on court case
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase)
						throws ConnectedSentenceExistsException {
		Person person = courtCase.getDocket().getPerson();
		this.offenseTermService.remove(courtCase);
		return new ModelAndView(String.format(REDIRECT_URL, person.getId()));
	}
	
	/**
	 * Dismisses a court case.
	 * 
	 * @param courtCase court case
	 * @return redirect to offense term listing screen
	 */
	@RequestMapping(value = "/dismissDocket.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENSE_TERM_REMOVE')")
	public ModelAndView dismissDocket(
			@RequestParam(value = "courtCase", required = true)
				final CourtCase courtCase) {
		Person person = courtCase.getDocket().getPerson();
		this.offenseTermService.dismiss(courtCase);
		return new ModelAndView(String.format(REDIRECT_URL, person.getId()));
	}
	
	/* AJAX invokable methods. */
	
	/**
	 * Returns form item to create offense.
	 * 
	 * @param offenseItemIndex offense item index
	 * @param person person
	 * @param courtCase court case
	 * @return form item to create offense
	 */
	@RequestMapping("/createOffense.html")
	public ModelAndView createOffense(
			@RequestParam(value = "itemIndex", required = true)
				final Integer itemIndex,
			@RequestParam(value = "person", required = true)
				final Person person,
			@RequestParam(value = "courtCase", required = false)
				final CourtCase courtCase) {
		//List<Offense> offenses = this.offenseTermService.findOffenses();
		List<SentenceCategory> sentenceCategories = this.offenseTermService
				.findSentenceCategories();
		List<LegalDispositionCategory> legalDispositionCategories
			= this.offenseTermService.findLegalDispositionCategories();
		List<Sentence> otherSentences;
		if (courtCase != null) {
			otherSentences = this.offenseTermService
				.findActiveSentencesForOtherCourtCases(
						courtCase.getDocket().getPerson(), courtCase);
		} else {
			otherSentences
				= this.offenseTermService.findActiveSentences(person);
		}
		OffenseItem offenseItem = new OffenseItem();
		offenseItem.setExpanded(true);
		offenseItem.setOperation(OffenseItemOperation.CREATE);
		SentenceFields sentenceFields = new SentenceFields();
		sentenceFields.setLengthClassification(
				SentenceLengthClassification.NOT_LIFE);
		offenseItem.setSentenceFields(sentenceFields);
		ModelAndView mav = new ModelAndView(OFFENSE_ITEM_VIEW_NAME);
		mav.addObject(OFFENSE_ITEM_MODEL_KEY, offenseItem);
		mav.addObject(OFFENSE_ITEM_INDEX_MODEL_KEY, itemIndex);
		//mav.addObject(OFFENSES_MODEL_KEY, offenses);
		mav.addObject(OFFENSE_SEVERITIES_MODEL_KEY, OffenseSeverity.values());
		mav.addObject(SENTENCE_CATEGORIES_MODEL_KEY, sentenceCategories);
		mav.addObject(LEGAL_DISPOSITION_CATEGORIES_MODEL_KEY,
				legalDispositionCategories);
		mav.addObject(SENTENCE_LENGTH_CLASSIFICATIONS_MODEL_KEY,
				SentenceLengthClassification.values());
		mav.addObject(OTHER_SENTENCES_MODEL_KEY, otherSentences);
		mav.addObject(PERSON_MODEL_KEY, person);
		if (courtCase != null) {
			mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
		}
		return mav;
	}
	
	/**
	 * Shows sentence terms.
	 * 
	 * @param category category
	 * @param prisonYears prison years
	 * @param prisonMonths prison months
	 * @param prisonDays prison days
	 * @param probationYears probation years
	 * @param probationMonths probation months
	 * @param probationDays probation days
	 * @param deferredYears deferred years
	 * @param deferredMonths deferred months
	 * @param deferredDays deferred days
	 * @return sentence terms
	 */
	@RequestMapping(
			value = "/showSentenceTerms.html", method = RequestMethod.GET)
	public ModelAndView showSentenceTerms(
			@RequestParam(value = "category", required = true)
				final SentenceCategory category,
			@RequestParam(value = "prisonYears", required = false)
				final Integer prisonYears,
			@RequestParam(value = "prisonMonths", required = false)
				final Integer prisonMonths,
			@RequestParam(value = "prisonDays", required = false)
				final Integer prisonDays,
			@RequestParam(value = "probationYears", required = false)
				final Integer probationYears,
			@RequestParam(value = "probationMonths", required = false)
				final Integer probationMonths,
			@RequestParam(value = "probationDays", required = false)
				final Integer probationDays,
			@RequestParam(value = "deferredYears", required = false)
				final Integer deferredYears,
			@RequestParam(value = "deferredMonths", required = false)
				final Integer deferredMonths,
			@RequestParam(value = "deferredDays", required = false)
				final Integer deferredDays) {
		ModelAndView mav = new ModelAndView(SENTENCE_TERMS_VIEW_NAME);
		if (category != null) {
			boolean bShowPrisonTerm = (TermRequirement.REQUIRED.equals(
					category.getPrisonRequirement()) || 
					TermRequirement.OPTIONAL.equals(category
							.getPrisonRequirement()));
			boolean bShowProbationTerm = (TermRequirement.REQUIRED.equals(
					category.getProbationRequirement()) || 
					TermRequirement.OPTIONAL.equals(category
							.getProbationRequirement()));
			boolean bShowDeferredTerm = (TermRequirement.REQUIRED.equals(
					category.getDeferredRequirement()) || 
					TermRequirement.OPTIONAL.equals(category
							.getDeferredRequirement()));
			mav.addObject(SHOW_PRISON_TERM_MODEL_KEY, bShowPrisonTerm);
			mav.addObject(SHOW_PROBATION_TERM_MODEL_KEY, bShowProbationTerm);
			mav.addObject(SHOW_DEFERRED_TERM_MODEL_KEY, bShowDeferredTerm);
			if (bShowPrisonTerm) {
				mav.addObject(PRISON_YEARS_MODEL_KEY, prisonYears);
				mav.addObject(PRISON_MONTHS_MODEL_KEY, prisonMonths);
				mav.addObject(PRISON_DAYS_MODEL_KEY, prisonDays);
			}
			if (bShowProbationTerm) {
				mav.addObject(PROBATION_YEARS_MODEL_KEY, probationYears);
				mav.addObject(PROBATION_MONTHS_MODEL_KEY, probationMonths);
				mav.addObject(PROBATION_DAYS_MODEL_KEY, probationDays);
			}
			if (bShowDeferredTerm) {
				mav.addObject(DEFERRED_YEARS_MODEL_KEY, deferredYears);
				mav.addObject(DEFERRED_MONTHS_MODEL_KEY, deferredMonths);
				mav.addObject(DEFERRED_DAYS_MODEL_KEY, deferredDays);
			}
		}
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
		List<Person> judges = this.offenseTermService
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
		List<Offense> offenses = this.offenseTermService
				.findOffenses(query.toUpperCase());
		mav.addObject(OFFENSES_MODEL_KEY, offenses);
		return mav;
	}
	
	/**
	 * Calculates sentence effective date.
	 * 
	 * @param pronouncementDate pronouncement date
	 * @param jailTimeCredit jail time credit
	 * @param streetTimeCredit street time credit
	 * @return calculated sentence effective date
	 */
	@RequestMapping(
			value = "/calculateSentenceEffectiveDate.json",
			method = RequestMethod.GET)
	public ModelAndView calculateSentenceEffectiveDate(
			@RequestParam(value = "pronouncementDate", required = true)
				final Date pronouncementDate,
			@RequestParam(value = "jailTimeCredit", required = true)
				final Integer jailTimeCredit,
			@RequestParam(value = "streetTimeCredit", required = true)
				final Integer streetTimeCredit) {
		Date effectiveDate = this.offenseTermService
				.calculateSentenceEffectiveDate(
						pronouncementDate,
						new ConvictionCredit(
								jailTimeCredit,
								streetTimeCredit));
		ModelAndView mav = new ModelAndView(DATE_VALUE_VIEW_NAME);
		mav.addObject(DATE_VALUE_MODEL_KEY, effectiveDate);
		return mav;
	}
	
	/**
	 * Returns sentence category.
	 * 
	 * @param sentenceCategory sentence category to return 
	 * @return sentence category
	 */
	@RequestMapping(
			value = "/findSentenceCategory.json", method = RequestMethod.GET)
	public ModelAndView findSentencCategory(
			@RequestParam(value = "sentenceCategory", required = true)
				final SentenceCategory sentenceCategory) {
		ModelAndView mav = new ModelAndView(SENTENCE_CATEGORY_VIEW_NAME);
		mav.addObject(SENTENCE_CATEGORY_MODEL_KEY, sentenceCategory);
		return mav;
	}
	
	/**
	 * Calculates total days.
	 * 
	 * @param years years
	 * @param months months
	 * @param days days
	 * @return calculation for total days
	 */
	@RequestMapping(
			value = "/calculateTotalDays.json", method = RequestMethod.GET)
	public ModelAndView calculateTotalDays(
			@RequestParam(value = "years", required = false)
				final Integer years,
			@RequestParam(value = "months", required = false)
				final Integer months,
			@RequestParam(value = "days", required = false)
				final Integer days) {
		Integer totalDays = this.offenseTermService
				.calculateTotalDays(new Term(years, months, days));
		ModelAndView mav = new ModelAndView(INTEGER_VALUE_VIEW_NAME);
		mav.addObject(INTEGER_VALUE_MODEL_KEY, totalDays);
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
	
	/* Action menus. */
	
	/**
	 * Shows action menu for screen to create/edit court case.
	 * 
	 * @param person person
	 * @param courtCase court case
	 * @return action menu for screen to create/edit court case
	 */
	@RequestMapping(
			value = "/offenseTermActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "person", required = true)
				final Person person,
			@RequestParam(value = "courtCase", required = false)
			final CourtCase courtCase) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PERSON_MODEL_KEY, person);
		if (courtCase != null) {
			mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
		}
		return mav;
	}
	
	/**
	 * Shows action menu for offenses.
	 * 
	 * @param person person
	 * @param courtCase court case
	 * @return action menu for offenses
	 */
	@RequestMapping(
			value = "/offensesActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showOffensesActionMenu(
			@RequestParam(value = "person", required = true)
				final Person person,
			@RequestParam(value = "courtCase", required = false)
				final CourtCase courtCase) {
		ModelAndView mav = new ModelAndView(OFFENSES_ACTION_MENU_VIEW_NAME);
		mav.addObject(PERSON_MODEL_KEY, person);
		if (courtCase != null) {
			mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
		}
		return mav;
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles conviction exists exceptions.
	 * 
	 * @param exception conviction exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(ConvictionExistsException.class)
	public ModelAndView handleConvictionExistsException(
			final ConvictionExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CONVICTION_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
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
	 * Handles sentence exists exceptions.
	 * 
	 * @param exception sentence exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(SentenceExistsException.class)
	public ModelAndView handleSentenceExistsException(
			final SentenceExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				SENTENCE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Handles sentence exists exceptions.
	 * 
	 * @param exception sentence exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(ConnectedSentenceExistsException.class)
	public ModelAndView handleConnectedSentenceExistsException(
			final ConnectedSentenceExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CONNECTED_SENTENCE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	
	/* Helper methods. */
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final OffenseTermForm offenseTermForm,
			final BindingResult bindingResult,
			final Person person) {
		ModelAndView mav = this.prepareMav(offenseTermForm, person);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ OFFENSE_TERM_FORM_MODEL_KEY, bindingResult);
		return mav;
	}

	// Prepares model and view
	private ModelAndView prepareMav(
			final OffenseTermForm offenseTermForm,
			final Person person) {
		int currentOffenseItemIndex = offenseTermForm.getOffenseItems().size();
		List<Court> courts = this.offenseTermService.findCourts();
		List<State> states = this.offenseTermService.findHomeStates();
		//List<Offense> offenses = this.offenseTermService.findOffenses();
		List<SentenceCategory> sentenceCategories = this.offenseTermService
				.findSentenceCategories();
		List<LegalDispositionCategory> legalDispositionCategories
			= this.offenseTermService.findLegalDispositionCategories();
		OffenderSummary offenderSummary = this.offenderReportService
				.summarizeIfOffender(person);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(CURRENT_OFFENSE_ITEM_INDEX_MODEL_KEY,
				currentOffenseItemIndex);
		mav.addObject(OFFENSE_TERM_FORM_MODEL_KEY, offenseTermForm);
		mav.addObject(COURTS_MODEL_KEY, courts);
		mav.addObject(STATES_MODEL_KEY, states);
		mav.addObject(JURISDICTION_AUTHORITIES_MODEL_KEY,
				JurisdictionAuthority.values());
		mav.addObject(OFFENDER_DANGER_DESIGNATORS_MODEL_KEY,
				OffenderDangerDesignator.values());
		mav.addObject(OFFENSE_SEVERITIES_MODEL_KEY, OffenseSeverity.values());
		mav.addObject(SENTENCE_LENGTH_CLASSIFICATIONS_MODEL_KEY,
				SentenceLengthClassification.values());
		mav.addObject(SENTENCE_CATEGORIES_MODEL_KEY, sentenceCategories);
		mav.addObject(LEGAL_DISPOSITION_CATEGORIES_MODEL_KEY,
				legalDispositionCategories);
		//mav.addObject(OFFENSES_MODEL_KEY, offenses);
		mav.addObject(PERSON_MODEL_KEY, person);
		if (offenderSummary != null) {
			
			// TODO - Add service method that returns offender form person - SA
			this.offenderSummaryModelDelegate.add(mav.getModelMap(),
					(Offender) person);
		} else {
			this.contactSummaryModelDelegate.add(mav.getModelMap(), person);
		}
		return mav;
	}
	
	// Returns true if value is true; false otherwise
	private Boolean resolveCheckBoxValue(final Boolean value) {
		if (value != null && value) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Entity resolvers. */
	
	// Resolves sentence from text
	// This is required - and justified - as a sentences are passed in
	// connections by their ID (the text) for consecutive to  other docket
	// connections
	private Sentence resolveSentence(final String text) {
		PropertyEditor propertyEditor
			= this.sentencePropertyEditorFactory.createPropertyEditor();
		propertyEditor.setAsText(text);
		return (Sentence) propertyEditor.getValue();		
	}
	
	/* Init binders. */
	
	/**
	 * Registers property editors.
	 * 
	 * @param binder data binder
	 */
	@InitBinder
	protected void registerCustomEditors(
			final WebDataBinder binder) {
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(CourtCase.class,
				this.courtCasePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Docket.class,
				this.docketPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Court.class,
				this.courtPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offense.class,
				this.offensePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(SentenceCategory.class,
				this.sentenceCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LegalDispositionCategory.class,
				this.legalDispositionCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Conviction.class,
				this.convictionPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Sentence.class,
				this.sentencePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		
		// OffenseItemConnection is not a domain object - it is purely a web
		// component. OffenseItemConnections are built from their own value
		// property. The property editor resolves OffenseItemConnections by
		// invoking a constructor that builds one from a value. The value
		// can be used as a view element value and be submitted by the form.
		binder.registerCustomEditor(OffenseItemConnection.class,
				new PropertyEditorSupport() {
			
			/** {@inheritDoc} */
			@Override
			public void setAsText(final String text) {
				if (text != null && !text.isEmpty()) {
					OffenseItemConnection connection
						= new OffenseItemConnection(text);
					this.setValue(connection);
				}
			}
			
			/** {@inheritDoc} */
			@Override
			public String getAsText() {
				if (this.getValue() != null) {
					OffenseItemConnection connection
						= (OffenseItemConnection) this.getValue();
					return connection.getValue();
				} else {
					return null;
				}
			}
		});
	}
}