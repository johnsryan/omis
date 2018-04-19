package omis.questionnaire.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionNote;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionStatus;
import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.AnswerCardinality;
import omis.questionnaire.domain.AnswerValue;
import omis.questionnaire.domain.Question;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.report.AdministeredQuestionnaireReportService;
import omis.questionnaire.report.AdministeredQuestionnaireSectionSummary;
import omis.questionnaire.report.AdministeredQuestionnaireSummary;
import omis.questionnaire.report.QuestionSummary;
import omis.questionnaire.service.AdministeredQuestionnaireService;
import omis.questionnaire.web.form.AdministeredQuestionValueItem;
import omis.questionnaire.web.form.QuestionAnswerItem;
import omis.questionnaire.web.form.QuestionnaireForm;
import omis.questionnaire.web.form.QuestionnaireReviewForm;
import omis.questionnaire.web.form.SectionReviewItem;
import omis.questionnaire.web.validator.QuestionnaireFormValidator;
import omis.questionnaire.web.validator.QuestionnaireReviewFormValidator;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Administered Questionnaire Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 6, 2018)
 *@since OMIS 3.0
 *
 */

@Controller
@RequestMapping("/questionnaire/")
@PreAuthorize("hasRole('USER')")
public class AdministeredQuestionnaireController {
	
	/* View Names */
	
	private static final String CREATE_VIEW_NAME = 
			"/questionnaire/createAdministeredQuestionnaire";
	
	private static final String REVIEW_VIEW_NAME = "/questionnaire/review";
	
	private static final String
		ADMINISTERED_QUESTIONNAIRE_ACTION_MENU_VIEW_NAME =
		"questionnaire/includes/administeredQuestionnaireActionMenu";
	
	private static final String 
		ADMINISTERED_QUESTIONNAIRE_REVIEW_ACTION_MENU_VIEW_NAME =
			"questionnaire/includes/administeredQuestionnaireReviewActionMenu";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_LIST_REDIRECT = 
			"redirect:/questionnaire/administeredQuestionnaireList.html?"
			+ "offender=%d&questionnaireType=%d";
	
	private static final String USER_ACCOUNTS_VIEW_NAME =
			"user/json/userAccounts";
	
	/* Model Keys */
	
	private static final String QUESTIONNAIRE_TYPE_MODEL_KEY = 
			"questionnaireType";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String CREATE_FORM_MODEL_KEY = "questionnaireForm";
	
	private static final String REVIEW_FORM_MODEL_KEY = 
			"questionnaireReviewForm";
	
	private static final String 
		QUESTIONNAIRE_SECTION_QUESTION_SUMMARIES_MODEL_KEY =
			"sectionQuestionSummaries";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_SUMMARY_MODEL_KEY =
			"administeredQuestionnaireSummary";
	
	private static final String USER_ACCOUNTS_MODEL_KEY = "userAccounts";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY = "entity.exists";
	
	/* Bundle */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.questionnaire.msgs.form";
	
	/* Services */
	
	@Autowired
	@Qualifier("administeredQuestionnaireService")
	private AdministeredQuestionnaireService administeredQuestionnaireService;
	
	@Autowired
	@Qualifier("administeredQuestionnaireReportService")
	private AdministeredQuestionnaireReportService
		administeredQuestionnaireReportService;
	
	/* Helpers */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Property Editors */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
			administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionnaireTypePropertyEditorFactory")
	private PropertyEditorFactory questionnaireTypePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("answerValuePropertyEditorFactory")
	private PropertyEditorFactory answerValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionPropertyEditorFactory")
	private PropertyEditorFactory questionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("administeredQuestionnaireSectionStatusPropertyEditorFactory")
	private PropertyEditorFactory 
		administeredQuestionnaireSectionStatusPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	
	@Autowired
	private QuestionnaireFormValidator formValidator;
	
	@Autowired
	private QuestionnaireReviewFormValidator reviewFormValidator;
	
	/**
	 * Default constructor for QuestionnaireController.
	 *  
	 */
	public AdministeredQuestionnaireController() {
	}
	
	/* Model and Views */
	
	/**
	 * Returns a view for creating an administeredQuestionnaire.
	 * @param offender - offender
	 * @param questionnaireType - questionnaire type
	 * @return ModelAndView - View for creating an administeredQuestionnaire
	 */
	@RequestMapping(value = "/createAdministeredQuestionnaire.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "questionnaireType", required = true)
			final QuestionnaireType questionnaireType) {
		return this.prepareCreateMav(offender, questionnaireType, 
				new QuestionnaireForm());
	}
	
	
	/**
	 * Attempts to save the administered questionnaire and return to the
	 * questionnaire list screen.
	 * @param offender - offender
	 * @param questionnaireType - questionnaire type
	 * @param form - questionnaire form
	 * @param bindingResult - binding result
	 * @return ModelAndView - redirects to the questionnaire list screen or
	 * back to administered questionnaire create screen on creation error
	 * @throws DuplicateEntityFoundException - when administered questionnaire
	 * already exists for given offender, questionnaire type, and date
	 * @throws ParseException - Parsing the administered questionnaire date
	 */
	@RequestMapping(value = "/createAdministeredQuestionnaire.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "questionnaireType", required = true)
			final QuestionnaireType questionnaireType, 
			final QuestionnaireForm form, final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, ParseException {
		this.formValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareCreateMav(offender, questionnaireType, form);
		} else {
			//because just doing "new Date()" would give more than just 
				//"MM/dd/yyyy", and just simpleDateFormat returns a string. 
					//So...this was my solution.
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date date = df.parse(new SimpleDateFormat("MM/dd/yyyy")
					.format(new Date()));
			
			AdministeredQuestionnaire administeredQuestionnaire =
				this.administeredQuestionnaireService
				.createAdministeredQuestionnaire(
						offender, true, form.getComments(), 
						form.getAssessor().getUser(), date, questionnaireType);
			
			for (QuestionnaireSection questionnaireSection
					: this.administeredQuestionnaireService
					.findQuestionnaireSectionsByQuestionnaireType(
							questionnaireType)) {
				this.administeredQuestionnaireService
					.createAdministeredQuestionnaireSectionStatus(
							questionnaireSection, administeredQuestionnaire, 
							true, date);
			}
			
			return new ModelAndView(String.format(
					ADMINISTERED_QUESTIONNAIRE_LIST_REDIRECT,
					offender.getId(), questionnaireType.getId()));
		}
	}
	
	/**
	 * Removes the specified administered questionnaire and returns to the
	 * questionnaire list view.
	 * @param offender - offender
	 * @param administeredQuestionnaire - administered questionnaire to remove
	 * @return ModelAndView - questionnaire list view
	 */
	@RequestMapping(value = "/removeAdministeredQuestionnaire.html", 
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('QUESTIONNAIRE_DELETE') or hasRole('ADMIN')")
	public ModelAndView remove(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		QuestionnaireType questionnaireType = 
				administeredQuestionnaire.getQuestionnaireType();
		
		for (AdministeredQuestionnaireSectionStatus sectionStatus
				: this.administeredQuestionnaireService
		.findAdministeredQuestionnaireSectionStatusByAdministeredQuestionnaire(
				administeredQuestionnaire)) {
			AdministeredQuestionnaireSectionNote sectionNote =
				this.administeredQuestionnaireService
			.findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
					administeredQuestionnaire, 
					sectionStatus.getQuestionnaireSection());
			if (sectionNote != null) {
				this.administeredQuestionnaireService
				.removeAdministeredQuestionnaireSectionNote(sectionNote);
			}
			
			for (AllowedQuestion allowedQuestion
					: this.administeredQuestionnaireService
				.findAllowedQuestionsByQuestionSection(
						sectionStatus.getQuestionnaireSection())) {
				for (AdministeredQuestionValue administeredQuestionValue
						: this.administeredQuestionnaireService
					.findAdministeredQuestionValuesByQuestionAndQuestionnaire(
							allowedQuestion.getQuestion(), 
							administeredQuestionnaire)) {
					this.administeredQuestionnaireService
						.removeAdministeredQuestionValue(
							administeredQuestionValue);
				}
			}
			
			this.administeredQuestionnaireService
				.removeAdministeredQuestionnaireSectionStatus(sectionStatus);
		}
		this.administeredQuestionnaireService.removeAdministeredQuestionnaire(
				administeredQuestionnaire);
		
		return new ModelAndView(String.format(
				ADMINISTERED_QUESTIONNAIRE_LIST_REDIRECT, offender.getId(), 
				questionnaireType.getId()));
	}
	
	
	/**
	 * Returns a view of the questionnaire review screen.
	 * @param offender - offender
	 * @param administeredQuestionnaire - administered questionnaire
	 * being reviewed
	 * @return ModelAndView - view of the questionnaire review screen
	 */
	@RequestMapping(value = "/review.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView review(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		
		return this.prepareReviewMav(offender, administeredQuestionnaire, 
				new QuestionnaireReviewForm());
	}
	
	/**
	 * Attempts to finalize the administered questionnaire and return to the
	 * questionnaire list screen.
	 * @param offender - offender
	 * @param administeredQuestionnaire - administered questionnaire to be 
	 * finalized
	 * @param form - questionnaire review form
	 * @param bindingResult - binding result
	 * @return ModelAndView - redirects to the questionnaire list screen on
	 * successful finalization, or back to the review screen on error
	 * @throws DuplicateEntityFoundException - when somehow a duplicate 
	 * administered questionnaire already exists (should logically never happen
	 * from this location)
	 */
	@RequestMapping(value = "/review.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView finalize(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final QuestionnaireReviewForm form, 
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		
		this.reviewFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			
			return this.prepareReviewMav(offender, administeredQuestionnaire, 
					form);
		} else {
			this.administeredQuestionnaireService.editAdministeredQuestionnaire(
					administeredQuestionnaire, 
					administeredQuestionnaire.getAnswerer(), 
					false, administeredQuestionnaire.getComments(), 
					administeredQuestionnaire.getAssessor(), 
					administeredQuestionnaire.getDate(), 
					administeredQuestionnaire.getQuestionnaireType());
			
			return new ModelAndView(String.format(
					ADMINISTERED_QUESTIONNAIRE_LIST_REDIRECT, offender.getId(),
					administeredQuestionnaire.getQuestionnaireType().getId()));
		}
	}
	
	
	/**
	 * Displays the action menu on the questionnaire create screen.
	 * @param offender - offender
	 * @param questionnaireType - questionnaire type
	 * @return ModelAndView - view of action menu on the questionnaire
	 * create screen
	 */
	@RequestMapping(value = "/administeredQuestionnaireActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayQuestionnaireActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender, 
			@RequestParam(value = "questionnaireType", 
			required = true) final QuestionnaireType questionnaireType) {
		ModelMap map = new ModelMap();
		
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(QUESTIONNAIRE_TYPE_MODEL_KEY, questionnaireType);
		
		return new ModelAndView(
				ADMINISTERED_QUESTIONNAIRE_ACTION_MENU_VIEW_NAME, map);
	}
	
	
	/**
	 * Returns a prepared model and view for the questionnaire create screen.
	 * @param offender - offender
	 * @param questionnaireType - questionnaire type
	 * @param form - questionnaire form
	 * @return ModelAndView - prepared model and view for the questionnaire 
	 * create screen
	 */
	public ModelAndView prepareCreateMav(final Offender offender, 
			final QuestionnaireType questionnaireType, 
			final QuestionnaireForm form) {
		ModelMap map = new ModelMap();
		
		map.addAttribute(CREATE_FORM_MODEL_KEY, form);
		map.addAttribute(QUESTIONNAIRE_TYPE_MODEL_KEY, questionnaireType);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);


		return new ModelAndView(CREATE_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared model and view for the questionnaire review screen.
	 * @param offender - offender
	 * @param administeredQuestionnaire - administered questionnaire to be
	 * reviewed
	 * @param form - questionnaire review form
	 * @return ModelAndView - prepared model and view for the questionnaire
	 * review screen
	 */
	public ModelAndView prepareReviewMav(final Offender offender, final
			AdministeredQuestionnaire administeredQuestionnaire, 
			final QuestionnaireReviewForm form) {
		ModelMap map = new ModelMap();
		
		List<AdministeredQuestionnaireSectionStatus> sectionStatuses =
				this.administeredQuestionnaireService
		.findAdministeredQuestionnaireSectionStatusByAdministeredQuestionnaire(
				administeredQuestionnaire);
		
		LinkedHashMap<AdministeredQuestionnaireSectionSummary,
			List<QuestionSummary>> sectionQuestionSummaries = new LinkedHashMap
				<AdministeredQuestionnaireSectionSummary,
				List<QuestionSummary>>();
		List<QuestionSummary> questionSummaries;
		List<SectionReviewItem> sectionReviewItems = new 
				ArrayList<SectionReviewItem>();
		
		for (AdministeredQuestionnaireSectionStatus sectionStatus 
				: sectionStatuses) {
			questionSummaries = 
					this.administeredQuestionnaireReportService
					.findQuestionSummariesByQuestionnaireSection(
							sectionStatus.getQuestionnaireSection());
			
			findSectionSummary : for (
					AdministeredQuestionnaireSectionSummary sectionSummary
					: this.administeredQuestionnaireReportService
				.findAdministeredQuestionnaireSectionSummariesByAdministeredQuestionnaire(
						sectionStatus.getAdministeredQuestionnaire())) {
				if (sectionSummary.getQuestionnaireSectionId().equals(
						sectionStatus.getQuestionnaireSection().getId())) {
					sectionQuestionSummaries.put(sectionSummary,
							questionSummaries);
					break findSectionSummary;
				}
			}
		}
		
		for (AdministeredQuestionnaireSectionStatus sectionStatus
				: sectionStatuses) {
			
			List<AllowedQuestion> allowedQuestions =
					this.administeredQuestionnaireService
					.findAllowedQuestionsByQuestionSection(
							sectionStatus.getQuestionnaireSection());
			
			List<QuestionAnswerItem> questionAnswerItems = new
					ArrayList<QuestionAnswerItem>();
			AdministeredQuestionValue administeredQuestionValue;
			List<AdministeredQuestionValue> administeredQuestionValues;
			QuestionAnswerItem item;
			
			
			for (AllowedQuestion allowedQuestion : allowedQuestions) {
				item = new QuestionAnswerItem();
				
				item.setAnswerCardinality(allowedQuestion
						.getAnswerCardinality());
				
				if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.SINGLE)) {
					administeredQuestionValue = 
							this.administeredQuestionnaireService
					.findAdministeredQuestionValueByQuestionAndQuestionnaire(
					allowedQuestion.getQuestion(), administeredQuestionnaire);
					if (administeredQuestionValue != null) {
						item.setAnswerValue(administeredQuestionValue
								.getAnswerValue());
						item.setAnswerValueText(administeredQuestionValue
								.getAnswerValueText());
						item.setComments(administeredQuestionValue
								.getComments());
					}
				} else if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.MULTIPLE)) {
					administeredQuestionValues
						= this.administeredQuestionnaireService
					.findAdministeredQuestionValuesByQuestionAndQuestionnaire(
							allowedQuestion.getQuestion(), 
							administeredQuestionnaire);
					List<AdministeredQuestionValueItem> admnstrdQstnValueItems =
							new ArrayList<AdministeredQuestionValueItem>();
					
					String comments = null;
					String answerValueText = null;
					
					for (AdministeredQuestionValue value 
							: administeredQuestionValues) {
						AdministeredQuestionValueItem valueItem =
								new AdministeredQuestionValueItem();
						valueItem.setAnswerValue(value.getAnswerValue());
						valueItem.setAdministeredQuestionValue(value);
						admnstrdQstnValueItems.add(valueItem);
						
						if (comments == null && value.getComments() != null) {
							comments = value.getComments();
						}
						if (answerValueText == null
								&& value.getAnswerValueText() != null) {
							answerValueText = value.getAnswerValueText();
						}
						item.setAdministeredQuestionValue(value);
					}
					item.setAdministeredQuestionValueItems(
							admnstrdQstnValueItems);
					item.setComments(comments);
					item.setAnswerValueText(answerValueText);
				}
				item.setAllowedQuestion(allowedQuestion);
				item.setAnswerCardinality(allowedQuestion
						.getAnswerCardinality());
				questionAnswerItems.add(item);
			}
			
			AdministeredQuestionnaireSectionNote sectionNote = 
					this.administeredQuestionnaireService
			.findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
							sectionStatus.getAdministeredQuestionnaire(), 
							sectionStatus.getQuestionnaireSection());
			
			SectionReviewItem sectionReviewItem = new SectionReviewItem();
			
			sectionReviewItem.setQuestionAnswerItems(questionAnswerItems);
			if (sectionNote != null) {
				sectionReviewItem.setSectionNotes(sectionNote.getComments());
			}
			sectionReviewItem.setSectionStatus(sectionStatus);
			
			sectionReviewItems.add(sectionReviewItem);
		}
		AdministeredQuestionnaireSummary administeredQuestionnaireSummary =
				this.administeredQuestionnaireReportService.summarize(
						administeredQuestionnaire);
		
		form.setSectionReviewItems(sectionReviewItems);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_SUMMARY_MODEL_KEY, 
				administeredQuestionnaireSummary);
		map.addAttribute(REVIEW_FORM_MODEL_KEY, form);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY, 
				administeredQuestionnaire);
		map.addAttribute(
				QUESTIONNAIRE_SECTION_QUESTION_SUMMARIES_MODEL_KEY, 
				sectionQuestionSummaries);
		map.addAttribute(QUESTIONNAIRE_TYPE_MODEL_KEY, 
				administeredQuestionnaire.getQuestionnaireType());
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(REVIEW_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for the questionnaire review screen.
	 * @param offender - offender
	 * @param administeredQuestionnaire administered questionnaire
	 * @return ModelAndView - action menu for the questionnaire review screen
	 */
	@RequestMapping(value = "/administeredQuestionnaireReviewActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayQuestionnaireReviewActionMenu(
			@RequestParam(value = "offender", 
			required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
	
		ModelMap map = new ModelMap();
		
		map.addAttribute(QUESTIONNAIRE_TYPE_MODEL_KEY,
				administeredQuestionnaire.getQuestionnaireType());
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(
				ADMINISTERED_QUESTIONNAIRE_REVIEW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* JSON */
	
	/**
	 * Searches user accounts.
	 * 
	 * @param query query
	 * @return user accounts as JSON
	 */
	@RequestMapping(value = "/searchUserAccounts.json",
			method = RequestMethod.GET)
	public ModelAndView searchUserAccounts(
			@RequestParam(value = "term", required = true)
				final String query) {
		List<UserAccount> userAccounts
			= this.administeredQuestionnaireService.findUserAccounts(query);
		ModelAndView mav = new ModelAndView(USER_ACCOUNTS_VIEW_NAME);
		mav.addObject(USER_ACCOUNTS_MODEL_KEY, userAccounts);
		return mav;
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
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(QuestionnaireType.class, 
				this.questionnaireTypePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class, 
				this.administeredQuestionnairePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AnswerValue.class, 
				this.answerValuePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Question.class, 
				this.questionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				AdministeredQuestionnaireSectionStatus.class,
				this.administeredQuestionnaireSectionStatusPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class, 
				this.userAccountPropertyEditorFactory.createPropertyEditor());
	}
}
