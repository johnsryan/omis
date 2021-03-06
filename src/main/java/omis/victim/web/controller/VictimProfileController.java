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
package omis.victim.web.controller;

import omis.beans.factory.PropertyEditorFactory;
import omis.person.domain.Person;
import omis.victim.web.controller.delegate.VictimSummaryModelDelegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for victim profile.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 5, 2015)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/victim")
public class VictimProfileController {
	
	/* View Names */
	
	private static final String VIEW_NAME = "victim/profile";
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("victimSummaryModelDelegate")
	private VictimSummaryModelDelegate victimSummaryModelDelegate;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	/* Constructors */

	/** Instantiates controller for victim profile. */
	public VictimProfileController() {
		// Default instantiation
	}

	/* URL Invokable Methods */
	
	/**
	 * Displays victim profile.
	 * 
	 * @param victim victim for whom to display profile
	 * @return victim profile
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_PROFILE_VIEW')")
	@RequestMapping(value = "/profile.html", method = RequestMethod.GET)
	public ModelAndView showProfile(
			@RequestParam(value = "victim", required = true)
				final Person victim) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		this.victimSummaryModelDelegate.add(mav.getModelMap(), victim);
		return mav;
	}
	
	/* Init Binders */
	
	/**
	 * Registers property editors.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	protected void registerPropertyEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
	}
}