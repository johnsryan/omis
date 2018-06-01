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
package omis.offender.web.controller;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.demographics.domain.Sex;
import omis.location.domain.Location;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.report.OffenderSearchSummary;
import omis.offender.report.OffenderSearchSummaryReportService;
import omis.offender.web.form.OffenderSearchForm;
import omis.offender.web.validator.OffenderSearchFormValidator;
import omis.person.report.AlternateNameSummary;

/**
 * Controller to search for offender.
 * 
 * @author Sheronda Vaughn
 * @version 0.0.1 (Dec 02, 2015)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offender")
public class OffenderSearchController {
	
	/* View names. */
	private static final String VIEW_NAME = "offender/editSearch";
	
	private static final String  SEARCH_RESULTS_ACTION_MENU_VIEW_NAME
		= "offender/includes/searchResultsActionMenu";
	
	private static final String SEARCH_CRITERIA_ACTION_MENU_VIEW_NAME
		= "offender/includes/searchCriteriaActionMenu";
	
	/* Model Keys. */
	private static final String OFFENDER_SEARCH_FORM_MODEL_KEY 
		= "offenderSearchForm";
	
	private static final String OFFENDER_SEARCH_SUMMARIES_MODEL_KEY
		= "searchSummaries";
	
	private static final String SEX_MODEL_KEY
		= "sex";
	
	private static final String SEXES_MODEL_KEY
		= "sexes";
	
	private static final String LOCATIONS_MODEL_KEY
		= "locations";
	private static final String ALTERNATE_NAME_SUMMARIES_MAP_MODEL_KEY = "altNameSummaries";
	
	/* Report services. */
	
	@Autowired
	@Qualifier("offenderSearchSummaryService")
	private OffenderSearchSummaryReportService offenderSearchSummaryService;

	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("offenderSearchFormValidator")
	private OffenderSearchFormValidator offenderSearchFormValidator;
	
	/* Constructors. */
	
	public OffenderSearchController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays form to search for offender.
	 * 
	 * @return offender search form
	 */
	@RequestMapping(value = "/advancedOffenderSearch.html", 
			method = RequestMethod.GET)
	public ModelAndView searchOffender() {
		OffenderSearchForm offenderSearchForm = new OffenderSearchForm();
		return this.prepareMav(offenderSearchForm);
	}

	/**
	 * Searches for offender. 
	 * 
	 * @param offenderSearchForm offender search form
	 * @return model and view of search summary results
	 */
	@RequestMapping(value = "/advancedOffenderSearch.html", 
			method = RequestMethod.POST)
	public ModelAndView search(final OffenderSearchForm offenderSearchForm,
			final BindingResult result) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		this.offenderSearchFormValidator.validate(offenderSearchForm, result);
		if (result.hasErrors()) {	
			mav = this.prepareRedisplayMav(offenderSearchForm, result);
			return mav;
		}		
		Sex sex = null;
		if (offenderSearchForm.getSex() != null) {
			if (Sex.FEMALE.equals(offenderSearchForm.getSex())) {
				sex = Sex.FEMALE;
			} else {
				sex = Sex.MALE;
			}
		} else {
			mav.addObject(SEXES_MODEL_KEY, Sex.values());
		}
		List<OffenderSearchSummary> searchSummaries 
			= new ArrayList<OffenderSearchSummary>();
		searchSummaries = this.offenderSearchSummaryService.searchForOffender(
				offenderSearchForm.getDocIdNumber(), 
				offenderSearchForm.getFirstName(),
				offenderSearchForm.getMiddleName(),
				offenderSearchForm.getLastName(),
				offenderSearchForm.getSex(),
				offenderSearchForm.getBirthDate(),
				offenderSearchForm.getSocialSecurityNumber());
		
		//  Create a map and have it hold collections of alternate name summaries that will be displayed
		//  along with the offender search summaries.
		
		Map<Long, List<AlternateNameSummary>> altNameMap = new HashMap<Long, List<AlternateNameSummary>>();
		for(OffenderSearchSummary summary : searchSummaries) {
			List<AlternateNameSummary> altNameSummaries = this.offenderSearchSummaryService
					.findAlternateNameSummariesByOffender(summary.getOffender());
			altNameMap.put(summary.getOffender().getId(), altNameSummaries);
		}
		mav.addObject(ALTERNATE_NAME_SUMMARIES_MAP_MODEL_KEY, altNameMap);
		mav.addObject(OFFENDER_SEARCH_SUMMARIES_MODEL_KEY, searchSummaries);
		mav.addObject(SEX_MODEL_KEY, sex);
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		
		return mav;
	}

	/**
	 * Search criteria action menu.
	 * 
	 * @return model and view
	 */
	@RequestMapping("/searchCriteriaActionMenu.html")
	public ModelAndView showSearchCriteriaActionMenu() {
		ModelAndView mav = new ModelAndView(
				SEARCH_CRITERIA_ACTION_MENU_VIEW_NAME);		
		return mav;		
	}
	
	/**
	 * Search results action menu.
	 * 
	 * @return model and view
	 */
	@RequestMapping("/searchResultsActionMenu.html")
	public ModelAndView showSearchResultsActionMenu() {
		ModelAndView mav = new ModelAndView(
				SEARCH_RESULTS_ACTION_MENU_VIEW_NAME);		
		return mav;	
	}
			
	/* Helper methods. */
	
	// Returns model and view to search for offender
	private ModelAndView prepareMav(final OffenderSearchForm 
			offenderSearchForm){
		
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(OFFENDER_SEARCH_FORM_MODEL_KEY, offenderSearchForm);	
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		mav.addObject(LOCATIONS_MODEL_KEY, offenderSearchForm.getLocations());
		return mav;
	}
	
	// Returns model and view to redisplay offender search
	private ModelAndView prepareRedisplayMav(
			final OffenderSearchForm form, final BindingResult result) {
		ModelAndView mav = this.prepareMav(form);		
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		mav.addObject(BindingResult.MODEL_KEY_PREFIX 
				+ OFFENDER_SEARCH_FORM_MODEL_KEY, 
				result);
		return mav;
	}
	
	/* Init binder. */
	
	/**
	 * Init binder.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	public void intiBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Location.class, 
				this.locationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class, 
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
	}
}