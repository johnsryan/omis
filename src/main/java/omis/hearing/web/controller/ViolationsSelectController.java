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
package omis.hearing.web.controller;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import omis.beans.factory.PropertyEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.component.Resolution;
import omis.hearing.report.ViolationSummary;
import omis.hearing.report.ViolationSummaryReportService;
import omis.hearing.service.HearingService;
import omis.hearing.service.ResolutionService;
import omis.hearing.web.form.GoToOption;
import omis.hearing.web.form.ViolationSelectionItem;
import omis.hearing.web.form.ViolationsSelectForm;
import omis.hearing.web.validator.ViolationsSelectFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEventCategory;

/**
 * Resolution Controller.
 * 
 *@author Annie Wahl
 *@version 0.1.1 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/hearing/violations/")
@PreAuthorize("hasRole('USER')")
public class ViolationsSelectController {
	
	/* View Names */
	
	private static final String SELECT_VIEW_NAME = "/hearing/violations/select";
	
	private static final String SELECT_VIOLATIONS_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/selectViolationsActionMenu";
	
	private static final String VIOLATIONS_LIST_REDIRECT =
			"redirect:/hearing/violations/list.html?offender=%d";
	
	private static final String HEARINGS_LIST_REDIRECT =
			"redirect:/hearing/list.html?offender=%d";
	
	private static final String ADJUDICATE_REDIRECT =
			"redirect:/hearing/resolution/create.html?offender=%d&hearing=%d"
			+ "&resolutionCategory=%s&violationCategory=%s";
	
	/* Model Keys */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String HEARING_MODEL_KEY = "hearing";
	
	private static final String RESOLUTION_CATEGORY_MODEL_KEY =
			"resolutionCategory";
	
	private static final String VIOLATION_CATEGORY_MODEL_KEY =
			"violationCategory";
	
	private static final String GO_TO_OPTION_MODEL_KEY = "goTo";
	
	private static final String FORM_MODEL_KEY = "violationsSelectForm";
	
	/* Service */
	
	@Autowired
	@Qualifier("violationSummaryReportService")
	private ViolationSummaryReportService violationSummaryReportService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("disciplinaryCodeViolationPropertyEditorFactory")
	private PropertyEditorFactory
		disciplinaryCodeViolationPropertyEditorFactory;

	@Autowired
	@Qualifier("conditionViolationPropertyEditorFactory")
	private PropertyEditorFactory conditionViolationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingPropertyEditorFactory")
	private PropertyEditorFactory hearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validator */
	
	@Autowired
	@Qualifier("violationsSelectFormValidator")
	private ViolationsSelectFormValidator violationsSelectFormValidator;
	
	/* Service */
	
	@Autowired
	@Qualifier("resolutionService")
	private ResolutionService resolutionService;
	
	@Autowired
	@Qualifier("hearingService")
	private HearingService hearingService;
	
	/**
	 * Default Constructor for ViolationsSelectController.
	 */
	public ViolationsSelectController() {
	}

	
	/**
	 * Returns the ModelAndView for selecting Violations.
	 * @param offender - Offender
	 * @param hearing - Hearing
	 * @param resolutionCategory - ResolutionClassificationCategory
	 * @param violationCategory - ViolationEventCategory
	 * @param goToOption - GoToOption, determines where to redirect on
	 * form submission
	 * @return ModelAndView for selecting Violations
	 */
	@RequestMapping(value = "/select.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole ('VIOLATION_EDIT')"
			+ " or hasRole ('VIOLATION_CREATE')")
	public ModelAndView select(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "hearing", required = false)
				final Hearing hearing,
			@RequestParam(value = "resolutionCategory", required = true)
				final ResolutionClassificationCategory resolutionCategory,
			@RequestParam(value = "violationCategory", required = true)
				final ViolationEventCategory violationCategory,
			@RequestParam(value = "goToOption", required = false)
				final GoToOption goToOption) {
		
		ModelMap map = new ModelMap();
		ViolationsSelectForm form = new ViolationsSelectForm();
		List<ViolationSummary> summaries = new ArrayList<ViolationSummary>();
		
		if (ViolationEventCategory.DISCIPLINARY.equals(violationCategory)) {
			summaries = this.violationSummaryReportService
					.findUnresolvedDisciplinaryViolationSummariesByOffender(
							offender);
		} else if (ViolationEventCategory.SUPERVISION
				.equals(violationCategory)) {
			summaries = this.violationSummaryReportService
					.findUnresolvedConditionViolationSummariesByOffender(
							offender);
		} else {
			throw new UnsupportedOperationException(
					"Violation Category Not Supported");
		}
		
		List<ViolationSelectionItem> violationSelectionItems =
				new ArrayList<ViolationSelectionItem>();
		
		for (ViolationSummary summary : summaries) {
			ViolationSelectionItem item = new ViolationSelectionItem();
			
			item.setViolationSummary(summary);
			violationSelectionItems.add(item);
		}
		
		if (hearing != null) {
			List<ViolationSummary> summariesByHearing =
					this.violationSummaryReportService
					.findAllViolationSummariesByHearing(hearing);
			
			for (ViolationSummary summary : summariesByHearing) {
				ViolationSelectionItem item = new ViolationSelectionItem();
				
				item.setViolationSummary(summary);
				item.setSelected(true);
				violationSelectionItems.add(item);
			}
		}
		
		form.setViolationSelectionItems(violationSelectionItems);
		form.setOffender(offender);
		form.setResolutionCategory(resolutionCategory);
		form.setViolationCategory(violationCategory);
		form.setHearing(hearing);
		
		map.addAttribute(HEARING_MODEL_KEY, hearing);
		map.addAttribute(VIOLATION_CATEGORY_MODEL_KEY, violationCategory);
		map.addAttribute(RESOLUTION_CATEGORY_MODEL_KEY, resolutionCategory);
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(GO_TO_OPTION_MODEL_KEY, goToOption);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(SELECT_VIEW_NAME, map);
	}
	
	/**
	 * Creates Infractions with no resolutions to associate selected Violations
	 * to specified Hearing (or removes a previously associated Infraction if
	 * the corresponding Violation was deselected).
	 * @param offender - Offender
	 * @param hearing - Hearing
	 * @param resolutionCategory - ResolutionClassificationCategory
	 * @param violationCategory - ViolationEventCategory
	 * @param goToOption - GoToOption, used to determine where to redirect 
	 * to on form submission
	 * @param form - ViolationsSelectForm
	 * @param bindingResult - BindingResult
	 * @return Depending on the value of GoToOption, returns a ModelAndView
	 * redirecting to either the Hearings List, Violations List, or Adjudication
	 * Resolution creation.
	 * @throws DuplicateEntityFoundException - When an Infraction already exists
	 * with specified ConditionCodeViolation or DisciplinaryCodeViolation for
	 * the given Hearing (should never occur) 
	 */
	@RequestMapping(value = "/select.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole ('VIOLATION_EDIT')"
			+ " or hasRole ('VIOLATION_CREATE')")
	public ModelAndView selected(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "hearing", required = false)
				final Hearing hearing,
			@RequestParam(value = "resolutionCategory", required = true)
				final ResolutionClassificationCategory resolutionCategory,
			@RequestParam(value = "violationCategory", required = true)
				final ViolationEventCategory violationCategory,
			@RequestParam(value = "goToOption", required = true)
				final GoToOption goToOption,
			final ViolationsSelectForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		
		if (GoToOption.ADJUDICATE.equals(goToOption)) {
			this.violationsSelectFormValidator.validate(form, bindingResult);
		}
		
		if (bindingResult.hasErrors()) {
			ModelMap map = new ModelMap();
			//The summaries are not carried forward to the POST, so
				//repopulating them
			for (int i = 0; i < form.getViolationSelectionItems().size(); i++) {
				if (ViolationEventCategory.DISCIPLINARY
						.equals(violationCategory)) {
					form.getViolationSelectionItems().get(i)
						.setViolationSummary(
							this.violationSummaryReportService.summarize(
									form.getViolationSelectionItems().get(i)
									.getDisciplinaryCodeViolation()));
				} else if (ViolationEventCategory.SUPERVISION
						.equals(violationCategory)) {
					form.getViolationSelectionItems().get(i)
						.setViolationSummary(
							this.violationSummaryReportService.summarize(
									form.getViolationSelectionItems().get(i)
									.getConditionViolation()));
				}
			}
			
			map.addAttribute(HEARING_MODEL_KEY, hearing);
			map.addAttribute(VIOLATION_CATEGORY_MODEL_KEY, violationCategory);
			map.addAttribute(RESOLUTION_CATEGORY_MODEL_KEY, resolutionCategory);
			map.addAttribute(FORM_MODEL_KEY, form);
			map.addAttribute(OFFENDER_MODEL_KEY, offender);
			map.addAttribute(GO_TO_OPTION_MODEL_KEY, goToOption);
			this.offenderSummaryModelDelegate.add(map, offender);
			
			return new ModelAndView(SELECT_VIEW_NAME, map);
		} else {
			List<ConditionViolation> conditionViolationsInHearing =
					new ArrayList<ConditionViolation>();
			List<DisciplinaryCodeViolation>
				disciplinaryCodeViolationsInHearing =
					new ArrayList<DisciplinaryCodeViolation>();
			List<Infraction> infractionsByHearing =
					this.resolutionService.findInfractionsByHearing(hearing);
			
			for (Infraction infraction : infractionsByHearing) {
				if (infraction.getDisciplinaryCodeViolation() != null) {
					disciplinaryCodeViolationsInHearing.add(
							infraction.getDisciplinaryCodeViolation());
				} else if (infraction.getConditionViolation() != null) {
					conditionViolationsInHearing.add(
							infraction.getConditionViolation());
				}
			}
			
			for (ViolationSelectionItem item
					: form.getViolationSelectionItems()) {
				if (ViolationEventCategory.DISCIPLINARY
						.equals(violationCategory)) {
					if (!item.getSelected()) {
						//Item is not selected, check infractions and see if
						//it is already an infraction. If it is, remove it, 
						//if not, nothing to do to it.
						for (Infraction infraction : infractionsByHearing) {
							if (infraction.getDisciplinaryCodeViolation()
									.equals(
										item.getDisciplinaryCodeViolation())) {
								ImposedSanction sanction
									= this.resolutionService
										.findImposedSanctionByInfraction(
												infraction);
								if (sanction != null) {
									this.resolutionService
									.removeImposedSanction(sanction);
								}
								this.resolutionService.removeInfraction(
										infraction);
							}
						}
					} else if (item.getSelected()
							&& !(disciplinaryCodeViolationsInHearing.contains(
								item.getDisciplinaryCodeViolation()))) {
						//Item is selected and not an infraction, create it.
						if (HearingStatusCategory.DISMISSED.equals(
								this.hearingService
								.findLatestHearingStatus(hearing)
								.getCategory())) {
							Resolution resolution = new Resolution();
							resolution.setCategory(
									ResolutionClassificationCategory.DISMISSED);
							resolution.setDate(new Date());
							
							this.resolutionService.createInfraction(
								hearing, null,
								item.getDisciplinaryCodeViolation(), resolution,
								null);
						} else {
							this.resolutionService.createInfraction(hearing,
									null, item.getDisciplinaryCodeViolation(),
									null, null);
						}
					}
					//else: item is selected and is already an infraction for
					//that hearing, so nothing necessary to do
				} else if (ViolationEventCategory.SUPERVISION.equals(
						violationCategory)) {
					if (item.getSelected() == null
							|| !item.getSelected()) {
						//Item is not selected, check infractions and see if
						//it is already an infraction. If it is, remove it,
						//if not, nothing to do to it.
						for (Infraction infraction : infractionsByHearing) {
							if (infraction.getConditionViolation().equals(
									item.getConditionViolation())) {
								this.resolutionService.removeInfraction(
										infraction);
							}
						}
					} else if (item.getSelected()
							&& !(conditionViolationsInHearing.contains(
									item.getConditionViolation()))) {
						///Item is selected and not an infraction, create it.
						if (HearingStatusCategory.DISMISSED.equals(
								this.hearingService
								.findLatestHearingStatus(hearing)
								.getCategory())) {
							Resolution resolution = new Resolution();
							resolution.setCategory(
									ResolutionClassificationCategory.DISMISSED);
							resolution.setDate(new Date());
							
							this.resolutionService.createInfraction(
								hearing,
								item.getConditionViolation(), null, resolution,
								null);
						} else {
							this.resolutionService.createInfraction(hearing,
									item.getConditionViolation(),
									null, null, null);
						}
					}
					//else: item is selected and is already an infraction for
					//that hearing, so nothing necessary to do
				} else {
					throw new UnsupportedOperationException(
							"Violation Category Not Supported.");
				}
			}
			
			if (GoToOption.VIOLATIONS_LIST.equals(goToOption)) {
				return new ModelAndView(String.format(VIOLATIONS_LIST_REDIRECT,
						offender.getId()));
			} else if (GoToOption.HEARINGS_LIST.equals(goToOption)) {
				return new ModelAndView(String.format(HEARINGS_LIST_REDIRECT,
						offender.getId()));
			} else if (GoToOption.ADJUDICATE.equals(goToOption)) {
				return new ModelAndView(String.format(ADJUDICATE_REDIRECT,
						offender.getId(), hearing.getId(), resolutionCategory,
						violationCategory));
			} else {
				//An error would be occurring at this point
				return new ModelAndView(String.format(VIOLATIONS_LIST_REDIRECT,
						offender.getId()));
			}
		}
	}
	
	/**
	 * Displays the Select Violations action menu.
	 * @param offender - Offender
	 * @return ModelAndView - Select Violations action menu
	 */
	@RequestMapping(value = "/selectViolationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displaySelectViolationsActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(SELECT_VIOLATIONS_ACTION_MENU_VIEW_NAME, map);
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
		binder.registerCustomEditor(
				DisciplinaryCodeViolation.class,
				this.disciplinaryCodeViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ConditionViolation.class,
				this.conditionViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Hearing.class, this.hearingPropertyEditorFactory
				.createPropertyEditor());
	}
	
}
