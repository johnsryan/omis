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
package omis.locationterm.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.locationterm.service.EndLocationTermService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;

/**
 * Controller to end location terms.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Feb 5, 2018)
 * @since OMIS 3.0
 * @deprecated update location terms instead
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/locationTerm")
@Deprecated
public class EndLocationTermController {
	
	/* View names. */
	
	private static final String SUCCESS_VIEW_NAME
		= "locationTerm/locationTermEnded";
	
	/* Services. */
	
	@Autowired
	@Qualifier("endLocationTermService")
	private EndLocationTermService endLocationTermService;

	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* URL invokable methods. */
	
	/**
	 * Ends location term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return 
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('LOCATION_TERM_EDIT')")
	@RequestMapping("/endLocationTerm.html")
	public ModelAndView endLegacyLocationTerm(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate) {
		Date effectiveDateToUse;
		if (effectiveDate != null) {
			effectiveDateToUse = effectiveDate;
		} else {
			effectiveDateToUse = new Date();
		}
		this.endLocationTermService.endLocationTerm(
				offender, effectiveDateToUse);
		ModelAndView mav = new ModelAndView(SUCCESS_VIEW_NAME);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param dataBinder data binder
	 */
	@InitBinder
	protected void registerCustomEditors(
			final WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		dataBinder.registerCustomEditor(Date.class,
				this.customDateEditorFactory
					.createCustomDateOnlyEditor(true));
	}
}