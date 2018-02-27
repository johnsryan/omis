package omis.program.web.controller;

import java.beans.PropertyEditor;
import java.util.Date;
import java.util.List;

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
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationTerm;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.exception.ProgramPlacementConflictException;
import omis.program.exception.ProgramPlacementExistsAfterException;
import omis.program.exception.ProgramPlacementExistsBeforeException;
import omis.program.exception.ProgramPlacementExistsException;
import omis.program.service.ProgramPlacementService;
import omis.program.web.form.ProgramPlacementForm;
import omis.program.web.validator.ProgramPlacementFormValidator;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller to manage program placements.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/program")
@PreAuthorize("hasRole('USER')")
public class ManageProgramPlacementController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "program/edit";
	
	/* Action menu view names. */
	
	private static final String ACTION_MENU_VIEW_NAME
		= "program/includes/programPlacementActionMenu";
	
	/* Model keys. */
	
	private static final String PROGRAMS_MODEL_KEY = "programs";
	
	private static final String PROGRAM_PLACEMENT_FORM_MODEL_KEY
		= "programPlacementForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String PROGRAM_PLACEMENT_MODEL_KEY
		= "programPlacement";
	
	/* Redirects. */
	
	private static final String REDIRECT
		= "redirect:/program/list.html?offender=%d";

	/* Error messages. */
	
	private static final String DUPLICATE_ENTITY_FOUND_MESSAGE_KEY
		= "programPlacement.exists";

	private static final String PROGRAM_PLACEMENT_CONFLICT_MESSAGE_KEY
		= "programPlacement.conflictingExists";
	
	private static final String PROGRAM_PLACEMENT_EXISTS_AFTER_MESSAGE_KEY
		= "programPlacement.existsAfter";
	
	private static final String PROGRAM_PLACEMENT_EXISTS_BEFORE_MESSAGE_KEY
		= "programPlacement.existsBefore";
	
	private static final String OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY
		= "offender.notUnderSupervisionOnDate";
	
	/* Bundles. */
	
	private static final String ERROR_BUNDLE = "omis.program.msgs.form";
	
	/* Service */

	@Autowired
	@Qualifier("programPlacementService")
	private ProgramPlacementService programPlacementService;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("programPlacementFormValidator")
	private ProgramPlacementFormValidator programPlacementFormValidator;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("programPlacementPropertyEditorFactory")
	private PropertyEditorFactory programPlacementPropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermPropertyEditorFactory")
	private PropertyEditorFactory placementTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationTermPropertyEditorFactory")
	private PropertyEditorFactory locationTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("programPropertyEditorFactory")
	private PropertyEditorFactory programPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Constructors. */

	/** Instantiates controller to manage program placements. */
	public ManageProgramPlacementController() {
		// Default instantiation
	}
	
	/**
	 * Shows screen to create program placement.
	 * 
	 * @param offender offender
	 * @return screen to create program placement
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PROGRAM_PLACEMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "placementTerm", required = false)
				final PlacementTerm defaultPlacementTerm,
			@RequestParam(value = "locationTerm", required = false)
				final LocationTerm defaultLocationTerm,
			@RequestParam(value = "effectiveDate", required = false)
				final Date defaultEffectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String defaultEffectiveTime) {
		
		// Determines effective date
		Date effectiveDate;
		if (defaultEffectiveDate != null) {
			effectiveDate = DateManipulator.getDateAtTimeOfDay(
					defaultEffectiveDate,
					this.parseTimeText(defaultEffectiveTime));
		} else {
			effectiveDate = new Date();
		}
		
		// Finds programs offered
		List<Program> programs = this.findOfferedProgramsForOffender(
				offender, defaultPlacementTerm, defaultLocationTerm,
				effectiveDate);
		
		// Prepares form, returns prepared model and view with form
		ProgramPlacementForm programPlacementForm = new ProgramPlacementForm();
		return this.prepareEditMav(programPlacementForm, programs, offender);
	}

	/**
	 * Saves program placement.
	 * 
	 * @param offender offender
	 * @param defaultPlacementTerm default placement term
	 * @param defaultLocationTerm default location term
	 * @param defaultEffectiveDate default effective date
	 * @param defaultEffectiveTime default effective time
	 * @param programPlacementForm form for program placements
	 * @param result result
	 * @return redirect to list program placements for offender
	 * @throws ProgramPlacementExistsException if program placement exists
	 * @throws ProgramPlacementConflictException if conflict program placement
	 * exists
	 * @throws ProgramPlacementExistsBeforeException if program placement
	 * exists before
	 * @throws ProgramPlacementExistsAfterException if program placement exists
	 * after
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PROGRAM_PLACEMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "placementTerm", required = false)
				final PlacementTerm defaultPlacementTerm,
			@RequestParam(value = "locationTerm", required = false)
				final LocationTerm defaultLocationTerm,
			@RequestParam(value = "effectiveDate", required = false)
				final Date defaultEffectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String defaultEffectiveTime,
			final ProgramPlacementForm programPlacementForm,
			final BindingResult result)
					throws ProgramPlacementExistsException,
						ProgramPlacementConflictException,
						ProgramPlacementExistsBeforeException,
						ProgramPlacementExistsAfterException, OffenderNotUnderSupervisionException {
		
		// Determines effective date
		Date effectiveDate;
		if (programPlacementForm.getStartDate() != null) {
			effectiveDate = programPlacementForm.getStartDate();
		} else if (defaultEffectiveDate != null) {
			effectiveDate = DateManipulator.getDateAtTimeOfDay(
					defaultEffectiveDate, this.parseTimeText(
							defaultEffectiveTime)); 
		} else {
			effectiveDate = new Date();
		}
		
		// Validates form, redisplays on error
		this.programPlacementFormValidator.validate(
				programPlacementForm, result);
		if (result.hasErrors()) {
			List<Program> programs = this.findOfferedProgramsForOffender(
					offender, defaultPlacementTerm, defaultLocationTerm,
					effectiveDate);
			return this.prepareRedisplay(programPlacementForm, result, programs,
					offender);
		}
		
		// Determines placement term and optional location term for program
		// placement - if supplied, use; otherwise, look up
		PlacementTerm placementTerm;
		if (defaultPlacementTerm != null) {
			placementTerm = defaultPlacementTerm;
		} else {
			placementTerm = this.programPlacementService
					.findPlacementTerm(offender, effectiveDate);
		}
		LocationTerm locationTerm;
		if (defaultLocationTerm != null) {
			locationTerm = defaultLocationTerm;
		} else {
			locationTerm = this.programPlacementService
					.findLocationTerm(offender, effectiveDate);
		}
		
		// Creates program placement, redirects to listing screen
		this.programPlacementService.create(
				offender, placementTerm, locationTerm,
				new DateRange(
						programPlacementForm.getStartDate(),
						programPlacementForm.getEndDate()),
				programPlacementForm.getProgram());
		return new ModelAndView(String.format(REDIRECT, offender.getId()));
	}
	
	/**
	 * Shows screen to edit program placement.
	 * 
	 * @param programPlacement program placement
	 * @return screen edit program placement
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PROGRAM_PLACEMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "programPlacement", required = true)
				final ProgramPlacement programPlacement) {
		
		// Find programs offered by location if at a location or supervisory
		// organization otherwise
		List<Program> programs;
		if (programPlacement.getLocationTerm() != null) {
			programs = this.programPlacementService
					.findProgramsOfferedAtLocation(
							programPlacement.getLocationTerm()
								.getLocation());
		} else {
			programs = this.programPlacementService
					.findProgramsOfferedBySupervisoryOrganization(
							programPlacement.getPlacementTerm()
								.getSupervisoryOrganizationTerm()
									.getSupervisoryOrganization());
		}
		
		// Prepares form, returns prepared model and view with form
		ProgramPlacementForm programPlacementForm = new ProgramPlacementForm();
		programPlacementForm.setProgram(programPlacement.getProgram());
		programPlacementForm.setStartDate(
				DateRange.getStartDate(programPlacement.getDateRange()));
		programPlacementForm.setEndDate(
				DateRange.getEndDate(programPlacement.getDateRange()));
		ModelAndView mav = this.prepareEditMav(
				programPlacementForm, programs, programPlacement.getOffender());
		mav.addObject(PROGRAM_PLACEMENT_MODEL_KEY, programPlacement);
		return mav;
	}
	
	/**
	 * Updates program placement.
	 * 
	 * @param programPlacement program placement to update 
	 * @param programPlacementForm form for program placement
	 * @param result binding result
	 * @return redirect to list program placements for offender
	 * @throws ProgramPlacementExistsException if program placement exists
	 * @throws ProgramPlacementConflictException if conflict program placement
	 * exists
	 * @throws ProgramPlacementExistsBeforeException if program placement
	 * exists before
	 * @throws ProgramPlacementExistsAfterException if program placement exists
	 * after
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PROGRAM_PLACEMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "programPlacement", required = true)
				final ProgramPlacement programPlacement,
			final ProgramPlacementForm programPlacementForm,
			final BindingResult result)
				throws ProgramPlacementExistsException,
					ProgramPlacementConflictException,
					ProgramPlacementExistsBeforeException,
					ProgramPlacementExistsAfterException, OffenderNotUnderSupervisionException {
		
		// Validates form, redisplays on error
		this.programPlacementFormValidator.validate(
				programPlacementForm, result);
		if (result.hasErrors()) {
			Date effectiveDate;
			if (programPlacementForm.getStartDate() != null) {
				effectiveDate = programPlacementForm.getStartDate();
			} else {
				effectiveDate = new Date();
			}
			List<Program> programs = this.findOfferedProgramsForOffender(
					programPlacement.getOffender(),
					programPlacement.getPlacementTerm(),
					programPlacement.getLocationTerm(),
					effectiveDate);
			ModelAndView mav = this.prepareRedisplay(
					programPlacementForm, result, programs,
					programPlacement.getOffender());
			mav.addObject(PROGRAM_PLACEMENT_MODEL_KEY, programPlacement);
			return mav;
		}
		
		// Updates program placements, redirects to listing screen
		this.programPlacementService.update(
				programPlacement,
				new DateRange(
						programPlacementForm.getStartDate(),
						programPlacementForm.getEndDate()),
				programPlacementForm.getProgram());
		return new ModelAndView(String.format(REDIRECT, 
				programPlacement.getOffender().getId()));
	}
	
	/**
	 * Removes program placement.
	 * 
	 * @param programPlacement program placement
	 * @return redirect to list program placements
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PROGRAM_PLACEMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "programPlacement", required = true)
				final ProgramPlacement programPlacement) {
		final Offender offender = programPlacement.getOffender();
		this.programPlacementService.remove(programPlacement);
		return new ModelAndView(String.format(
				REDIRECT, offender.getId()));
	}
	
	/* URL invokable methods. */
	
	/**
	 * Returns action menuu for program placement.
	 * 
	 * @param offender offender
	 * @return action menu for program placement
	 */
	@RequestMapping(
			value = "/programPlacementActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/* Exception handlers. */
	
	/**
	 * Handles {@code ProgramPlacementExistsException}. 
	 * 
	 * @param exception exception thrown
	 * @return screen to handle {@code ProgramPlacementExistsException}
	 */
	@ExceptionHandler
	public ModelAndView handleProgramPlacementExistsException(
			final ProgramPlacementExistsException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(DUPLICATE_ENTITY_FOUND_MESSAGE_KEY,
						ERROR_BUNDLE, exception);
	}
	
	/**
	 * Handles {@code ProgramPlacementConflictException}.
	 * 
	 * @param exception exception thrown
	 * @return screen to handle {@code ProgramPlacementConflictException}
	 */
	@ExceptionHandler
	public ModelAndView handleProgramPlacementConflictException(
			final ProgramPlacementConflictException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(PROGRAM_PLACEMENT_CONFLICT_MESSAGE_KEY,
						ERROR_BUNDLE, exception);
	}
	
	/**
	 * Handles {@code ProgramPlacementExistsAfterException}.
	 * 
	 * @param exception exception thrown
	 * @return screen to handle {@code ProgramPlacementExistsAfterException}
	 */
	@ExceptionHandler
	public ModelAndView handleProgramPlacementExistsAfterException(
			final ProgramPlacementExistsAfterException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(PROGRAM_PLACEMENT_EXISTS_AFTER_MESSAGE_KEY,
						ERROR_BUNDLE, exception);
	}
	
	/**
	 * Handles {@code ProgamPlacementExistsBeforeException}.
	 * 
	 * @param exception exception thrown
	 * @return screen to handle {@code ProgramPlacementExistsBeforeException}
	 */
	@ExceptionHandler
	public ModelAndView handleProgramPlacementExistsBeforeException(
			final ProgramPlacementExistsBeforeException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(
						PROGRAM_PLACEMENT_EXISTS_BEFORE_MESSAGE_KEY,
						ERROR_BUNDLE, exception);
	}
	
	/**
	 * Handles {@code OffenderNotUnderSupervisionException}.
	 * 
	 * @param exception exception thrown
	 * @return screen to handle {@code OffenderNotUnderSupervisionException}
	 */
	@ExceptionHandler
	public ModelAndView handleOffenderNotUnderSupervisionException(
			final OffenderNotUnderSupervisionException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(
						OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY,
						ERROR_BUNDLE, exception);
	}
	
	/* Helper methods. */
	
	// Prepares redirect
	private ModelAndView prepareRedisplay(
			final ProgramPlacementForm programPlacementForm,
			final BindingResult result,
			final List<Program> programs,
			final Offender offender) {
		ModelAndView mav = this.prepareEditMav(
				programPlacementForm, programs, offender);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ PROGRAM_PLACEMENT_FORM_MODEL_KEY, result);
		return mav;
	}
	
	// Prepares edit model and view
	private ModelAndView prepareEditMav(
			final ProgramPlacementForm programPlacementForm,
			final List<Program> programs,
			final Offender offender) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PROGRAMS_MODEL_KEY, programs);
		mav.addObject(PROGRAM_PLACEMENT_FORM_MODEL_KEY, programPlacementForm);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	// Finds programs offered for offender on date
	private List<Program> findOfferedProgramsForOffender(
			final Offender offender,
			final PlacementTerm defaultPlacementTerm,
			final LocationTerm defaultLocationTerm,
			final Date effectiveDate) {
		
		// Programs available for offender based on supervisory organization
		// or location
		List<Program> programs;
		
		// Checks whether location term is provided - otherwise finds for
		// offender on effective date
		Location location;
		if (defaultLocationTerm != null) {
			location = defaultLocationTerm.getLocation();
		} else {
			LocationTerm locationTerm = this.programPlacementService
					.findLocationTerm(offender, effectiveDate);
			if (locationTerm != null) {
				location = locationTerm.getLocation();
			} else {
				location = null;
			}
		}
		
		// Uses location of location term if found to look up programs
		// offered at location, otherwise looks up placement term
		if (location != null) {
			programs = this.programPlacementService
					.findProgramsOfferedAtLocation(location);
		} else {
			
			// Uses supervisory organization of 
			SupervisoryOrganization supervisoryOrganization;
			if (defaultPlacementTerm != null) {
				supervisoryOrganization = defaultPlacementTerm
						.getSupervisoryOrganizationTerm()
						.getSupervisoryOrganization();
			} else {
				PlacementTerm placementTerm = this.programPlacementService
						.findPlacementTerm(offender, effectiveDate);
				if (placementTerm != null) {
					supervisoryOrganization = placementTerm
							.getSupervisoryOrganizationTerm()
							.getSupervisoryOrganization();
				} else {
					throw new AssertionError("Offender is not placed");
				}
			}
			programs = this.programPlacementService
					.findProgramsOfferedBySupervisoryOrganization(
							supervisoryOrganization);
		}
			
		// Returns programs offered
		return programs;
	}
	
	// Parses time
	private Date parseTimeText(final String timeText) {
		if (timeText != null && !timeText.isEmpty()) {
			PropertyEditor propertyEditor
				= this.customDateEditorFactory.createCustomTimeOnlyEditor(true);
			propertyEditor.setAsText(timeText);
			return (Date) propertyEditor.getValue();
		} else {
			return null;
		}
	}
	
	/* Init Binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(ProgramPlacement.class,
				this.programPlacementPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(PlacementTerm.class,
				this.placementTermPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LocationTerm.class,
				this.locationTermPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Program.class,
				this.programPropertyEditorFactory
					.createPropertyEditor());
	}
}