/*
 * OMIS - Offender Management Information System.
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
package omis.visitation.web.controller;

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
import org.springframework.ui.Model;
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
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.person.report.AlternateNameSummary;
import omis.person.web.delegate.PersonFieldsControllerDelegate;
import omis.person.web.form.PersonFields;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.visitation.domain.Visit;
import omis.visitation.domain.VisitMethod;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.report.VisitSummary;
import omis.visitation.report.VisitSummaryReportService;
import omis.visitation.report.VisitationAssociationSummary;
import omis.visitation.report.VisitationAssociationSummaryReportService;
import omis.visitation.service.VisitationAssociationService;
import omis.visitation.validator.VisitationAssociationFormValidator;
import omis.visitation.web.form.VisitationAssociationForm;
import omis.visitation.web.form.VisitorCheckInForm;
import omis.visitation.web.form.VisitorLogForm;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for visitation association.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (May 7, 2015)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/visitation")
@PreAuthorize("hasRole('USER')")
public class VisitationAssociationController {
	
	/* Redirect URLs. */
	
	private static final String LIST_REDIRECT_URL = 
			"redirect:list.html?offender=";
	
	//private static final String INDEX_REDIRECT_URL = "redirect:index.html";
	
	/* View names. */
	
	private static final String VISITATION_ASSOCIATION_EDIT_VIEW_NAME = 
			"visitation/visitationAssociation/edit";
	
	private static final String VISITATION_ASSOCIATIONS_ACTION_MENU_VIEW_NAME
		= "visitation/visitationAssociation/includes/"
				+ "visitationAssociationsActionMenu";
	
	private static final String VISITATION_ASSOCIATION_ACTION_MENU_VIEW_NAME
		= "visitation/visitationAssociation/includes/"
				+ "visitationAssociationActionMenu";
	
	private static final String VISITS_ACTION_MENU_VIEW_NAME
		= "visitation/visit/includes/visitsActionMenu";
	
	private static final String VISITATION_LIST_VIEW_NAME = "visitation/list";
	
	private static final String
		VISITATION_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME 
		= "visitation/visitationAssociation/includes/"
				+ "visitationAssociationRowActionMenu"; 
	
	private static final String ALTERNATE_NAME_SUMMARIES_LIST_VIEW_NAME =
		"visitation/includes/associatedAlternateNameRow";
	
	private static final String 
		VISITATION_ASSOCIATION_ROW_SEARCH_ACTION_MENU_VIEW_NAME
		= "visitation/visitationAssociation/includes/"
				+ "visitationAssociationRowSearchActionMenu";
	
	/* Model Keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String VISITOR_MODEL_KEY = "visitor";
	
	private static final String VISITATION_ASSOCIATION_FORM_MODEL_KEY = 
			"visitationAssociationForm";
	
	private static final String VISITATION_ASSOCIATION_MODEL_KEY
		= "visitationAssociation";
	
	private static final String VISITATION_ASSOCIATION_CATEGORIES_MODEL_KEY
		= "visitationAssociationCategories";
	
	private static final String VISITATION_ASSOCIATION_SUMMARIES_MODEL_KEY = 
			"visitationAssociationSummaries";
	
	private static final String VISIT_SUMMARIES_MODEL_KEY = "visitSummaries";
	
	private static final String CURRENTLY_VISITING_MODEL_KEY
		= "currentlyVisiting";
	
	private static final String VISITOR_LOG_FORM_MODEL_KEY = "visitorLogForm";
	
	private static final String VISITOR_CHECK_IN_FORM_MODEL_KEY
		= "visitorCheckInForm";
	
	private static final String ERRORS_PRESENT_MODEL_KEY = "errorsPresent";
	
	private static final String VISIT_MODEL_KEY = "visit";
	
	private static final String VISIT_METHODS_MODEL_KEY = "visitMethods";
	
	private static final String DATE_MODEL_KEY = "date";
	
	private static final String START_DATE_MODEL_KEY = "startDate";
	
	private static final String END_DATE_MODEL_KEY = "endDate";
	
	private static final String IS_OFFENDER_MODEL_KEY = "isOffender";
	
	private static final String ALTERNATE_NAMES_SUMMARIES_MODEL_KEY = 
			"alternateNameSummaries";
	
	private static final String VISITATION_ASSOCIATION_EXISTS_MODEL_KEY
		= "visitationAssociationExists";
	
	
	
	/* Property names. */
	
	private static final String PERSON_FIELDS_PROPERTY_NAME = "personFields";
	
	/* Services. */
	
	@Autowired
	@Qualifier("visitationAssociationService")
	private VisitationAssociationService visitationAssociationService;
	
	@Autowired
	@Qualifier("visitationAssociationSummaryReportService")
	private VisitationAssociationSummaryReportService
		visitationAssociationReportService;
	
	@Autowired
	@Qualifier("visitSummaryReportService")
	private VisitSummaryReportService visitSummaryReportService;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("visitationAssociationFormValidator")
	private VisitationAssociationFormValidator 
		visitationAssociationFormValidator;
	
	/* Property editors. */
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("visitationAssociationPropertyEditorFactory")
	private PropertyEditorFactory visitationAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("visitationAssociationCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
		visitationAssociationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("visitPropertyEditorFactory")
	private PropertyEditorFactory visitPropertyEditorFactory;
	
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;

	/* Helpers. */
	
	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("personFieldsControllerDelegate")
	private PersonFieldsControllerDelegate personFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Report names. */
	
	private static final String VISITATION_LISTING_REPORT_NAME 
		= "/Relationships/Visitation/Visitation_Listing_Visitor_List";
	
	private static final String VISITATION_LISTING_LEGACY_REPORT_NAME 
		= "/Relationships/Visitation/Visitation_Listing_Visitor_List_Legacy";
	
	private static final String VISITATION_LISTING_LEGACY_REDACTED_REPORT_NAME 
		= "/Relationships/Visitation/"
			+ "Visitation_Listing_Visitor_List_Legacy_Redacted";
	
	private static final String VISITATION_LISTING_REDACTED_REPORT_NAME 
		= "/Relationships/Visitation/Inmate_Visitor_List";

	private static final String VISITATION_DETAILS_REPORT_NAME 
		= "/Relationships/Visitation/Visitation_Details_Approved_Visitor";
	
	private static final String VISITATION_DETAILS_REDACTED_REPORT_NAME 
		= "/Relationships/Visitation/"
				+ "Visitation_Details_Approved_Visitor_Redacted";
	
	private static final String VISITOR_LOG_LEGACY_REPORT_NAME 
		= "/Relationships/Visitation/Visitor_Log_Legacy";

	/* Report parameter names. */
	
	private static final String VISITATION_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String VISITATION_LISTING_LEGACY_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String 
		VISITATION_LISTING_LEGACY_REDACTED_ID_REPORT_PARAM_NAME 
			= "DOC_ID";
	
	private static final String VISITOR_LOG_LEGACY_ID_REPORT_PARAM_NAME 
		= "DOC_ID";
	
	private static final String VISITATION_LISTING_REDACTED_ID_REPORT_PARAM_NAME
	    = "DOC_ID";

	private static final String VISITATION_DETAILS_ID_REPORT_PARAM_NAME 
		= "VISIT_ASSOC_ID";

	private static final String VISITATION_DETAILS_REDACTED_ID_REPORT_PARAM_NAME
		= "VISIT_ASSOC_ID";	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;

	
	/* Message keys. */
	
	private final static String DATE_CONFLICT_EXCEPTION_MESSAGE_KEY
		= "visitationAssociaitonDateConflict";
	
	private final static String DUPLICATE_ENTITY_FOUND_EXCEPTION_MESSAGE_KEY
		= "duplicateVisitationAssociationFound";
	private final static String  REFLEXIVE_RELATIONSHIP_EXCEPTION_MESSAGE_KEY =
			"reflexiveRelationship";
	
	/* Bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.visitation.msgs.form";
	
	/* Constructor. */
	
	/**
	 * Instantiates a default instance of visitation association controller.
	 */
	public VisitationAssociationController() {
		//Default constructor.
	}
	
	/* URL mapped methods. */
	
	/**
	 * List the visitor summaries for the specified offender.
	 * 
	 * @param offender offender
	 * @param date date
	 * @param startDate start date
	 * @param endDate end date
	 * @param model model
	 * @return model and view for visitation association and visit list screen
	 */
	@RequestContentMapping(nameKey = "visitationListScreenName",
			descriptionKey = "visitationListScreenDescription",
			messageBundle = "omis.visitation.msgs.visitation",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", 
			required = false) final Offender offender, 
			@RequestParam(value = "date", required = false) final Date date,
			@RequestParam(value = "startDate", required = false)
				final Date startDate,
			@RequestParam(value = "endDate", required = false)
				final Date endDate, final Model model) {
		ModelMap map = new ModelMap();
		//Set the model object to a Map.
		Map<String, ?> attributes = model.asMap();
		//Check for an attribute of binding results for the badge number form
		if (attributes.containsKey(BindingResult.MODEL_KEY_PREFIX 
				+ VISITOR_CHECK_IN_FORM_MODEL_KEY)) {
			//Add the errors object, binding results, and current visitation
			//association to the model map to be used when the listing screen
			//is presented. This helps decide whether to show the form or not
			//on load.
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX 
					+ VISITOR_CHECK_IN_FORM_MODEL_KEY,
					attributes.get(BindingResult.MODEL_KEY_PREFIX 
				+ VISITOR_CHECK_IN_FORM_MODEL_KEY));
			map.addAttribute(ERRORS_PRESENT_MODEL_KEY, true);
			map.addAttribute(VISITOR_CHECK_IN_FORM_MODEL_KEY, 
					attributes.get(VISITOR_CHECK_IN_FORM_MODEL_KEY));
			map.addAttribute(VISITATION_ASSOCIATION_MODEL_KEY,
					attributes.get(VISITATION_ASSOCIATION_MODEL_KEY));
		} else {
			//If no binding results are present, a blank form must be supplied
			//on the model map in order to appease spring form tags on the jsp.
			VisitorCheckInForm form = new VisitorCheckInForm();
			form.setMethod(VisitMethod.PHYSICALLY_PRESENT);
			map.addAttribute(VISITOR_CHECK_IN_FORM_MODEL_KEY, form);
		}
		VisitorLogForm form = new VisitorLogForm();
		final List<VisitationAssociationSummary> visitationAssociationSummaries;
		if (date != null) {
			form.setDate(date);
			form.setSingleDate(true);
			visitationAssociationSummaries =
					this.visitationAssociationReportService
					.summarizeVisitationAssociations(offender, date);
		} else if (startDate != null || endDate != null) {
			form.setStartDate(startDate);
			form.setEndDate(endDate);
			form.setSingleDate(false);
			visitationAssociationSummaries =
					this.visitationAssociationReportService
					.summarizeVisitationAssociationsInRange(offender, startDate,
							endDate);
		} else {
			form.setDate(new Date());
			form.setSingleDate(true);
			visitationAssociationSummaries = 
					this.visitationAssociationReportService
					.summarizeVisitationAssociations(offender, new Date());
		}
		/*List<AlternateNameSummary> AlternateNameSummaries = 
			this.visitationAssociationReportService.summarizeAlternativeNames(
				visitationAssociationSummaries.get(0).getSecondPerson(), 
				new Date());*/
		
		List<VisitSummary> visitSummaries = this.visitSummaryReportService
					.findVisitSummariesByOffenderOnDate(offender, new Date(),
							new Date());
		return this.prepareListMav(visitationAssociationSummaries, form,
				visitSummaries, offender, map);
	}
	
	/**
	 * List visitor summaries and visit summaries for the specified offender.
	 * 
	 * @param offender offender
	 * @param form visitor log form
	 * @param result binding result
	 * @return model and view for visitation association and visit list screen
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_LIST') or hasRole('ADMIN')")
	public ModelAndView visitLog(
			@RequestParam(value = "offender", required = true)
				final Offender offender, final VisitorLogForm form,
				final BindingResult result) {
		ModelMap map = new ModelMap();
		final List<VisitationAssociationSummary> visitationAssociationSummaries;
		final List<VisitSummary> visitSummaries;
		if (form.getSingleDate()) {
			visitSummaries = this.visitSummaryReportService
					.findVisitSummariesByOffenderOnDate(offender,
							form.getDate(), new Date());
			visitationAssociationSummaries =
					this.visitationAssociationReportService
					.summarizeVisitationAssociations(offender, new Date());
		} else {
			visitationAssociationSummaries =
					this.visitationAssociationReportService
					.summarizeVisitationAssociationsInRange(offender,
							form.getStartDate(), form.getEndDate());
			visitSummaries = this.visitSummaryReportService
					.findVisitSummariesByOffenderInDateRange(offender,
							form.getStartDate(), form.getEndDate(), new Date());
		}
		return this.prepareListMav(visitationAssociationSummaries, form,
				visitSummaries, offender, map);
	}

	/**
	 * Create a new visitation association between and offender and person.
	 * 
	 * @param offender offender
	 * @param person person
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "visitationAssociationCreateScreenName",
			descriptionKey = "visitationAssociationCreateScreenDescription",
			messageBundle = "omis.visitation.msgs.visitation",
			screenType = RequestContentType.DETAIL_SCREEN)
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "offender", required = false) 
			final Offender offender, @RequestParam(value = "visitor",
			required = false) final Person person) {
		ModelMap map = new ModelMap();
		VisitationAssociationForm form = new VisitationAssociationForm();
		if (person != null) {
			//Create summary object and add to map
			form.setPerson(person);
			form.setNewVisitor(false);
			this.contactSummaryModelDelegate.add(map, person);
		} else {
			form.setNewVisitor(true);
			List<String> suffixesNames = this.visitationAssociationService
					.findSuffixNames();
			List<Country> countries = this.visitationAssociationService
					.findCountries();
			State homeState = this.visitationAssociationService.findHomeState();
			List<State> states = new ArrayList<State>();
			List<City> cities = new ArrayList<City>();
			PersonFields personFields = new PersonFields();
			if (homeState != null) {
				states.addAll(this.visitationAssociationService
						.findStates(homeState.getCountry()));
				cities.addAll(this.visitationAssociationService
						.findCities(homeState));
				personFields.setBirthCountry(homeState.getCountry());
				personFields.setBirthState(homeState);
			}
			this.personFieldsControllerDelegate
				.prepareEditPersonFields(map, suffixesNames, countries, states, 
						cities, PERSON_FIELDS_PROPERTY_NAME);
			form.setPersonFields(personFields);
		}
		form.setAssociatedOffender(offender);
		return this.prepareEditMav(map, form, offender);
	}
	
	/**
	 * Saves a new visitation association for the specified offender with
	 * information from the specified visitation association form.
	 * 
	 * @param form visitation association form
	 * @param offender offender
	 * @param result binding result
	 * @return model and view
	 * @throws DateConflictException thrown when a visitation association with
	 * the anticipated visitor to the offender is found that overlaps the
	 * anticipated date range
	 * @throws DuplicateEntityFoundException thrown when a visitation 
	 * association is found with the anticipated relationship and start date
	 * @throws ReflexiveRelationshipException thrown when both people in the 
	 * relationship are the same
	 */
	@RequestContentMapping(nameKey = "visitationAssociationSaveName",
			descriptionKey = "visitationAssociationSaveDescription",
			messageBundle = "omis.visitation.msgs.visitation",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/create.html", 
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_CREATE') or " 
			+ "hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
			final Offender offender,
			final VisitationAssociationForm form, final BindingResult result) 
					throws DuplicateEntityFoundException, DateConflictException,
					ReflexiveRelationshipException {
		this.visitationAssociationFormValidator.validate(
				form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			if (form.getNewVisitor()) {
				this.personFieldsControllerDelegate.prepareEditPersonFields(map,
						this.visitationAssociationService.findSuffixNames(),
						this.visitationAssociationService.findCountries(),
						this.visitationAssociationService.findStates(
								form.getPersonFields().getBirthCountry()),
						this.visitationAssociationService.findCities(
								form.getPersonFields().getBirthState()),
						PERSON_FIELDS_PROPERTY_NAME);
			} else {
				this.contactSummaryModelDelegate.add(map, form.getPerson());
			}
			return this.prepareEditMav(map, form, offender);
		}
		VisitationApproval approval = new VisitationApproval(form.getApproved(),
				form.getDecisionDate());
		VisitationAssociationFlags flags = new VisitationAssociationFlags(
				form.getMoney(), form.getNonContact(), form.getCourtOrder(),
				form.getSpecialVisit());
		final Person visitor;
		if (form.getNewVisitor()) {
			PersonFields personFields = form.getPersonFields();
			Integer socialSecurityNumber;
			if (personFields.getSocialSecurityNumber() != null
					&& !personFields.getSocialSecurityNumber().isEmpty()) {
				socialSecurityNumber = Integer.valueOf(personFields
						.getSocialSecurityNumber().replaceAll("-", ""));
			} else {
				socialSecurityNumber = null;
			}
			visitor = this.visitationAssociationService.createVisitor(
					personFields.getLastName(), personFields.getFirstName(),
					personFields.getMiddleName(), personFields.getSuffix(),
					socialSecurityNumber,
					personFields.getBirthDate(), personFields.getBirthCity(),
					personFields.getBirthState(),
					personFields.getBirthCountry(), personFields.getSex(),
					personFields.getStateIdNumber(),
					personFields.getDeathDate(), personFields.getDeceased());
		} else {
			visitor = form.getPerson();
		}
		this.visitationAssociationService.associate(
				form.getAssociatedOffender(), visitor,
				form.getCategory(), approval, form.getStartDate(),
				form.getEndDate(), flags, form.getNotes(), 
				form.getGuardianship());
		String url = LIST_REDIRECT_URL + offender.getId();
		return new ModelAndView(url);
	}
	
	/**
	 * Displays the visitation association form with information about the 
	 * specified visitation association for the purpose of viewing or editing.
	 * 
	 * @param visitationAssociation visitation association
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "visitationAssociationEditScreenName",
			descriptionKey = "visitationAssociationEditScreenDescription",
			messageBundle = "omis.visitation.msgs.visitation",
			screenType = RequestContentType.DETAIL_SCREEN)	
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') or " 
			+ "hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "visitationAssociation", required = true)
				final VisitationAssociation visitationAssociation) {
		ModelMap map = new ModelMap();
		this.contactSummaryModelDelegate.add(map, visitationAssociation
				.getRelationship().getSecondPerson());
		VisitationAssociationForm form = new VisitationAssociationForm();
		this.prepareVisitationAssociationForm(form, visitationAssociation);
		map.addAttribute(VISITATION_ASSOCIATION_MODEL_KEY, 
				visitationAssociation);
		return this.prepareEditMav(map, form, (Offender) visitationAssociation
				.getRelationship().getFirstPerson());
	}
	
	/**
	 * Updates the specified visitation association with values from the 
	 * specified visitation association form. If the form contains errors, 
	 * the user is returned to the editVisitationAssociation screen.
	 * 
	 * @param visitationAssociation visitation association
	 * @param form visitation association form
	 * @param result binding result
	 * @return model and view
	 * @throws DateConflictException thrown when a visitation association with
	 * the anticipated visitor to the offender is found that overlaps the
	 * anticipated date range
	 * @throws DuplicateEntityFoundException thrown when a visitation 
	 * association is found with the anticipated relationship and start date 
	 */
	@RequestContentMapping(nameKey = "visitationAssociationUpdateName",
			descriptionKey = "visitationAssociationUpdateDescription",
			messageBundle = "omis.visitation.msgs.visitation",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_EDIT') or " 
			+ "hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(value = "visitationAssociation",
			required = true) final VisitationAssociation visitationAssociation,
			final VisitationAssociationForm form, final BindingResult result) 
		throws DuplicateEntityFoundException, DateConflictException {
		this.visitationAssociationFormValidator.validate(form, result);
		Offender offender = (Offender) visitationAssociation.getRelationship()
				.getFirstPerson();
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			this.contactSummaryModelDelegate.add(map, visitationAssociation
					.getRelationship().getSecondPerson());
			map.addAttribute(VISITATION_ASSOCIATION_MODEL_KEY, 
					visitationAssociation);
			return this.prepareEditMav(map, form, offender);
		}
		VisitationApproval approval = new VisitationApproval(form.getApproved(),
				form.getDecisionDate());
		VisitationAssociationFlags flags = new VisitationAssociationFlags(
				form.getMoney(), form.getNonContact(), form.getCourtOrder(),
				form.getSpecialVisit());
		this.visitationAssociationService.update(visitationAssociation, 
				form.getCategory(), approval, form.getStartDate(), 
				form.getEndDate(), flags, form.getNotes(), 
				form.getGuardianship());
		return new ModelAndView(LIST_REDIRECT_URL + offender.getId());
	}
	
	/**
	 * Removes a model and view of visitation association.
	 *
	 *
	 * @param visitationAssociation visitation association
	 * @return new model and view
	 */
	@RequestMapping(value = "remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_REMOVE') or " 
			+ "hasRole('ADMIN')")
	public ModelAndView remove(@RequestParam(value = "visitationAssociation", 
		required = true) final VisitationAssociation visitationAssociation) {
		Offender offender = (Offender) visitationAssociation.getRelationship()
				.getFirstPerson();
		//TODO:remove visits or throw business exception before removing visitation association
		this.visitationAssociationService.remove(visitationAssociation);		
		return new ModelAndView(String.format(
				LIST_REDIRECT_URL + offender.getId()));		
	}
	
	/**
	 * Displays the visitation associations action menu.
	 * 
	 * @param offender offender
	 * @return visitation associations action menu model and view
	 */
	@RequestMapping(value = "visitationAssociationsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayVisitationAssociationsActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(VISITATION_ASSOCIATIONS_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays the visitation association action menu.
	 * 
	 * @param offender offender
	 * @return visitation association action menu model and view
	 */
	@RequestMapping(value = "visitationAssociationActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayVisitationAssociationActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(VISITATION_ASSOCIATION_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Displays the visits action menu.
	 * 
	 * @param offender offender
	 * @return visits action menu model and view
	 */
	@RequestMapping(value = "visitsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayVisitsActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(VISITS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the visitation association row action menu.
	 * 
	 * @param association visitation association
	 * @param currentlyVisiting currently visiting
	 * @param visit visit
	 * @param date date
	 * @param startDate start date
	 * @param endDate end date
	 * @return model and view for visitation association row action menu
	 */
	@RequestMapping(value = "visitationAssociationRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayVisitationAssociaitonRowActionMenu(
			@RequestParam(value = "visitationAssociation", required = true)
					final VisitationAssociation association,
				@RequestParam(value = "currentlyVisiting", required = true)
					final Boolean currentlyVisiting,
				@RequestParam(value = "visit", required = false)
					final Visit visit,
				@RequestParam(value = "date", required = false)final Date date,
				@RequestParam(value = "startDate", required = false)
					final Date startDate,
				@RequestParam(value = "endDate", required = false)
					final Date endDate) {
		ModelMap map = new ModelMap();
		map.addAttribute(VISITATION_ASSOCIATION_MODEL_KEY, association);
		map.addAttribute(CURRENTLY_VISITING_MODEL_KEY, currentlyVisiting);
		map.addAttribute(VISIT_MODEL_KEY, visit);
		map.addAttribute(DATE_MODEL_KEY, date);
		map.addAttribute(START_DATE_MODEL_KEY, startDate);
		map.addAttribute(END_DATE_MODEL_KEY, endDate);
		final Boolean isOffender;
		if (this.visitationAssociationService.isOffender(
				association.getRelationship().getSecondPerson())) {
			isOffender = true;
			map.addAttribute(OFFENDER_MODEL_KEY, (Offender) association
					.getRelationship().getSecondPerson());
		} else {
			isOffender = false;
		}
		map.addAttribute(IS_OFFENDER_MODEL_KEY,
				isOffender);
		return new ModelAndView(
				VISITATION_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Return model and view of visitation association row search action menu.
	 *
	 *
	 * @param offender offender
	 * @param visitor visitor
	 * @return model and view
	 */
	@RequestMapping(value = "visitationAssociationRowSearchActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView visitationAssociationRowSearchActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "visitor", required = true)
				final Person visitor) {
		ModelMap map = new ModelMap();
		map.addAttribute(IS_OFFENDER_MODEL_KEY, 
				this.visitationAssociationReportService.isOffender(visitor));
		map.addAttribute(OFFENDER_MODEL_KEY,  offender);	
		map.addAttribute(VISITOR_MODEL_KEY, visitor);
		map.addAttribute(VISITATION_ASSOCIATION_EXISTS_MODEL_KEY, 
				this.visitationAssociationReportService
				.visitationAssociationExists(offender, visitor));
		map.addAttribute(VISITATION_ASSOCIATION_MODEL_KEY, 
				this.visitationAssociationReportService
				.findVisitationAssociation(offender, visitor));
		return new ModelAndView(
				VISITATION_ASSOCIATION_ROW_SEARCH_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Lists alternate names. 
	 * 
	 * @param person person
	 * @param effectiveDate effective date
	 * @return A list of alternate names
	 *
	 */
	@RequestMapping("/showAssociatedAlternateNames.html")
	public ModelAndView listAlternateNameSummaries(
			@RequestParam(value = "person", required = true)
				final Person person,
			@RequestParam(value = "effectiveDate", required = true)
				final Date effectiveDate) {
		Date queryDate = new Date();
		if (effectiveDate != null) {
			queryDate = effectiveDate;
		} 
		
		List<AlternateNameSummary> alternateNameSummaries
			= this.visitationAssociationReportService.summarizeAlternativeNames(
				person, queryDate);
		ModelAndView mav = new ModelAndView(
			ALTERNATE_NAME_SUMMARIES_LIST_VIEW_NAME);
		mav.addObject(ALTERNATE_NAMES_SUMMARIES_MODEL_KEY, 
			alternateNameSummaries);
		return mav;
	}
		
	/**
	 * Displays state options for the specified country.
	 * 
	 * @param country country
	 * @return model and view for state options 
	 */
	@RequestMapping(value = "stateOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showStateOptions(
			@RequestParam(value = "country", required = true)
				final Country country) {
		return this.personFieldsControllerDelegate.showStateOptions(
				this.visitationAssociationService.findStates(country),
				PERSON_FIELDS_PROPERTY_NAME);
	}
	
	/**
	 * Returns city options for the specified state or country.
	 * 
	 * @param state state
	 * @param country country
	 * @return model and view for city options
	 */
	@RequestMapping(value = "cityOptions.html",
			method = RequestMethod.GET)
	public ModelAndView showCityOptions(
			@RequestParam(value = "state", required = false)
				final State state,
			@RequestParam(value = "country", required = false)
				final Country country) {
		final List<City> cities;
		if (state != null) {
			cities = this.visitationAssociationService.findCities(state);
		} else if (country != null) {
			cities = this.visitationAssociationService
					.findCitiesByCountry(country);
		} else {
			cities = Collections.emptyList();
		}
		return this.personFieldsControllerDelegate.showCityOptions(
				cities, PERSON_FIELDS_PROPERTY_NAME);
	}
	
	/**
	 * Return model and view to dissociate visitation association.
	 *
	 *
	 * @param visitationAssociation visitation association
	 * @param effectiveDate effective date
	 * @return model and view
	 */
	@RequestMapping(value = "dissociateVisitationAssociation.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_REMOVE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView dissociateVisitationAssociation(
			@RequestParam(value = "visitationAssociation", required = true)
					final VisitationAssociation visitationAssociation,
				@RequestParam(value = "effectiveDate", required = false)
					final Date effectiveDate) {
		final Date date;
		if (effectiveDate != null) {
			date = effectiveDate;
		} else {
			date = new Date();
		}
		this.visitationAssociationService
			.dissociate(visitationAssociation, date);
		Offender offender = (Offender) visitationAssociation.getRelationship()
				.getFirstPerson();
		String url = LIST_REDIRECT_URL + offender.getId();
		return new ModelAndView(url);
	}
	
	/* Exception Handlers. */
	
	@ExceptionHandler(DateConflictException.class)
	public ModelAndView handleDateConflictException(
			final DateConflictException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				DATE_CONFLICT_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				DUPLICATE_ENTITY_FOUND_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}
	
	@ExceptionHandler(ReflexiveRelationshipException.class)
	public ModelAndView handleReflexiveRelationshipException(
			final ReflexiveRelationshipException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				REFLEXIVE_RELATIONSHIP_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders visitors.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitationListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITATION_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the legacy report for the specified offenders visitors.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitationListingLegacyReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('VISITATION_ASSOCIATION_VIEW') "
			+ "and hasRole('OFFENDER_SSN_VIEW')) "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationListingLegacy(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITATION_LISTING_LEGACY_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_LISTING_LEGACY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the legacy redacted report for the specified offenders visitors.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitationListingLegacyRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationListingLegacyRedacted(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(
				VISITATION_LISTING_LEGACY_REDACTED_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_LISTING_LEGACY_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the legacy report for the specified offenders visits.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitorLogLegacyReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitorLogLegacy(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITOR_LOG_LEGACY_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VISITOR_LOG_LEGACY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the redacted report for the specified offenders visitors.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "inmateVisitorListReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationListingRedacted(@
			RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITATION_LISTING_REDACTED_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_LISTING_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified visitation association.
	 * 
	 * @param visitationAssociation visitation association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitationDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('VISITATION_ASSOCIATION_VIEW') "
			+ "and hasRole('OFFENDER_SSN_VIEW')) "
			+ "or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationDetails(@RequestParam(
			value = "visitationAssociation", required = true)
			final VisitationAssociation visitationAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITATION_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(visitationAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the redacted report for the specified visitation association.
	 * 
	 * @param visitationAssociation visitation association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/visitationDetailsRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VISITATION_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVisitationDetailsRedacted(
			@RequestParam(value = "visitationAssociation", required = true)
			final VisitationAssociation visitationAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VISITATION_DETAILS_REDACTED_ID_REPORT_PARAM_NAME,
				Long.toString(visitationAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VISITATION_DETAILS_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/* Helper methods. */
	
	/*
	 * Prepares the specified visitation association form with values from
	 * the specified visitation association.
	 * 
	 * @param form visitation association form
	 * @param visitationAssociation visitation association
	 * @return populated visitation association form
	 */
	private VisitationAssociationForm prepareVisitationAssociationForm(
			final VisitationAssociationForm form,
			final VisitationAssociation visitationAssociation) {
		VisitationAssociationFlags flags = visitationAssociation.getFlags();
		if (flags != null) {
			form.setCourtOrder(flags.getCourtOrder());
			form.setSpecialVisit(flags.getSpecialVisit());
			form.setMoney(flags.getMoney());
			form.setNonContact(flags.getNonContact());
		}
		form.setStartDate(DateRange.getStartDate(visitationAssociation
				.getDateRange()));
		form.setEndDate(DateRange.getEndDate(visitationAssociation
				.getDateRange()));
		VisitationApproval approval = visitationAssociation.getApproval();
		if (approval != null) {
			form.setApproved(approval.getApproved());
			form.setDecisionDate(approval.getDecisionDate());
		}
		form.setAssociatedOffender((Offender) visitationAssociation
				.getRelationship().getFirstPerson());
		form.setPerson(visitationAssociation.getRelationship()
				.getSecondPerson());
		form.setCategory(visitationAssociation.getCategory());
		form.setGuardianship(visitationAssociation.getGuardianship());
		form.setNotes(visitationAssociation.getNotes());
		return form;
	}
	
	/*
	 * Prepares a model and view for editing a visitation association.
	 * 
	 * @param map model map
	 * @param form visitation association form
	 * @return visitation association model and view
	 */
	private ModelAndView prepareEditMav(final ModelMap map,
			final VisitationAssociationForm form, final Offender offender) {
		if (offender != null) {
			map.addAttribute(OFFENDER_MODEL_KEY, offender);
			this.offenderSummaryModelDelegate.add(map, offender);
		}
		
		map.addAttribute(VISITATION_ASSOCIATION_FORM_MODEL_KEY, form);
		map.addAttribute(VISITATION_ASSOCIATION_CATEGORIES_MODEL_KEY,
				this.visitationAssociationService.findCategories());
		return new ModelAndView(VISITATION_ASSOCIATION_EDIT_VIEW_NAME, map);
	}

	/*
	 * Prepares the list screen for offender visitations.
	 * 
	 * @param visitationAssociationSummaries visitation association summaries
	 * @param form visit log form
	 * @param visitSummaries visit summaries
	 * @param offender offender
	 * @param map model map
	 * @return model and view for visitation list screen
	 */
	private ModelAndView prepareListMav(
			final List<VisitationAssociationSummary>
				visitationAssociationSummaries, final VisitorLogForm form,
			final List<VisitSummary> visitSummaries, final Offender offender,
			final ModelMap map) {
		map.addAttribute(VISIT_METHODS_MODEL_KEY, VisitMethod.values());
		map.addAttribute(VISITATION_ASSOCIATION_SUMMARIES_MODEL_KEY,
				visitationAssociationSummaries);
		map.addAttribute(VISIT_SUMMARIES_MODEL_KEY,
				visitSummaries);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(VISITOR_LOG_FORM_MODEL_KEY, form);
		map.addAttribute(VISITOR_CHECK_IN_FORM_MODEL_KEY, 
				new VisitorCheckInForm());
		this.offenderSummaryModelDelegate.add(map, offender);
		return new ModelAndView(VISITATION_LIST_VIEW_NAME, map); 
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				Person.class, 
				this.personPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				VisitationAssociation.class,
				this.visitationAssociationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				VisitationAssociationCategory.class,
				this.visitationAssociationCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Visit.class,
				this.visitPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Country.class,
				this.countryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				State.class,
				this.statePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				City.class,
				this.cityPropertyEditorFactory
				.createPropertyEditor());
	}
}