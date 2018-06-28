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
package omis.supervision.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.locationterm.exception.LocationTermExistsException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.EndPlacementTermService;
import omis.supervision.web.form.EndPlacementTermForm;
import omis.supervision.web.validator.EndPlacementTermFormValidator;
import omis.util.DateManipulator;

/**
 * Controller for ending a placement term.
 * 
 * @author Joshua Divine
 * @author Stephen Abson
 * @version 0.1.0 (Feb 16, 2017)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/supervision/placementTerm")
@PreAuthorize("hasRole('USER')")
public class EndPlacementTermController {

	/* View names. */
	
	private static final String END_PLACEMENT_TERM_VIEW_NAME
		= "supervision/placementTerm/endPlacementTerm";
	
	/* Redirects. */
	
	private static final String LIST_BY_OFFENDER_REDIRECT
			= "redirect:/supervision/placementTerm/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String PLACEMENT_TERM_END_CHANGE_REASONS_MODEL_KEY
		= "endChangeReasons";
	
	private static final String PLACEMENT_TERM_MODEL_KEY = "placementTerm";
	
	private static final String END_PLACEMENT_TERM_FORM_MODEL_KEY 
		= "endPlacementTermForm";
	
	/* Services. */
	
	@Autowired
	@Qualifier("endPlacementTermService")
	private EndPlacementTermService endPlacementTermService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermPropertyEditorFactory")
	private PropertyEditorFactory placementTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("placementTermChangeReasonPropertyEditorFactory")
	private PropertyEditorFactory
			placementTermChangeReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	

	/* Validators. */
	
	@Autowired
	@Qualifier("endPlacementTermFormValidator")
	private EndPlacementTermFormValidator endPlacementTermFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructor. */
	
	/** Instantiates a default controller for ending placement terms. */
	public EndPlacementTermController() {
		// Default instantiation
	}
	
/* Screens. */
	
	/**
	 * Displays a screen for ending a placement term.
	 * 
	 * @param offender offender
	 * @param placementTerm placement term
	 * @param effectiveDate effective date
	 * @param effectiveTime effective time
	 * @return screen for ending a placement term
	 */
	@RequestMapping(value = "/endPlacementTerm.html", 
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_EDIT')")
	public ModelAndView endPlacementTerm(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "placementTerm", required = true)
			final PlacementTerm placementTerm) {
		return this.prepareEditMav(new EndPlacementTermForm(), offender, 
				placementTerm); 
	}

	/**
	 * Ends an existing placement term.
	 * 
	 * @param offender offender
	 * @param placementTerm placement term
	 * @param endPlacementTermForm end placement term form
	 * @param result result
	 * @return model and view
	 * @throws LocationTermExistsException 
	 * @throws PlacementTermExistsException 
	 * @throws SupervisoryOrganizationTermExistsException 
	 * @throws CorrectionalStatusTermExistsException 
	 */
	@RequestMapping(value = "/endPlacementTerm.html", 
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_TERM_EDIT')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "placementTerm", required = true)
			final PlacementTerm placementTerm,
			final EndPlacementTermForm endPlacementTermForm,
			final BindingResult result)
					throws CorrectionalStatusTermExistsException,
						SupervisoryOrganizationTermExistsException,
						PlacementTermExistsException,
						LocationTermExistsException {
		this.validate(placementTerm, endPlacementTermForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(offender, placementTerm, 
					endPlacementTermForm, result);
		}
		this.endPlacementTermService.endPlacementTerm(placementTerm, 
				DateManipulator.getDateAtTimeOfDay(
						endPlacementTermForm.getEndDate(), 
						endPlacementTermForm.getEndTime()), 
				endPlacementTermForm.getEndChangeReason());
		return prepareListRedirect(offender);
	}
	
	/* Helper methods. */
	
	// Returns a model and view to redirect to placement terms screen
	private ModelAndView prepareListRedirect(final Offender offender) {
		return new ModelAndView(String.format(LIST_BY_OFFENDER_REDIRECT,
				offender.getId()));
	}
	
	// Returns a model and view to redisplay edit screen
	private ModelAndView prepareRedisplayMav(
			final Offender offender,
			final PlacementTerm placementTerm,
			final EndPlacementTermForm endPlacementTermForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(
				endPlacementTermForm, offender, placementTerm);
		mav.addObject(
			BindingResult.MODEL_KEY_PREFIX + END_PLACEMENT_TERM_FORM_MODEL_KEY,
			result);
		return mav;
	}
	
	// Prepares edit form model and view
	private ModelAndView prepareEditMav(
			final EndPlacementTermForm endPlacementTermForm,
			final Offender offender, final PlacementTerm placementTerm) {
		// Effective placement term is on effective date - add to model and view
		ModelAndView mav = new ModelAndView(END_PLACEMENT_TERM_VIEW_NAME);
		mav.addObject(END_PLACEMENT_TERM_FORM_MODEL_KEY, endPlacementTermForm);
		mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
		List<PlacementTermChangeReason> endChangeReasons 
			= this.endPlacementTermService.findAllowedEndChangeReasons(
						placementTerm.getCorrectionalStatusTerm()
						.getCorrectionalStatus());
		mav.addObject(PLACEMENT_TERM_END_CHANGE_REASONS_MODEL_KEY, 
				endChangeReasons);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	// Validates form
	private void validate(
			final PlacementTerm placementTerm,
			final EndPlacementTermForm endPlacementTermForm,
			final Errors errors) {
		if (endPlacementTermForm.getEndDate() != null
				&& placementTerm.getDateRange().getStartDate().after(
						DateManipulator.getDateAtTimeOfDay(
								endPlacementTermForm.getEndDate(),
								endPlacementTermForm.getEndTime()))) {
			errors.rejectValue("endTime",
					"endPlacementTermForm.endDate.beforeStartDate");
		}
		this.endPlacementTermFormValidator.validate(endPlacementTermForm, 
				errors);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(PlacementTerm.class,
			this.placementTermPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(PlacementTermChangeReason.class,
			this.placementTermChangeReasonPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "endDate",
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endTime",
				this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
	}
}
