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
package omis.paroleboardlocation.web.controller;

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
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.ParoleBoardLocationService;
import omis.paroleboardlocation.web.form.ParoleBoardLocationForm;
import omis.paroleboardlocation.web.validator.ParoleBoardLocationFormValidator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing parole board locations.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 21, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardLocation")
public class ManageParoleBoardLocationController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardLocation/edit";

	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardLocation/includes/paroleBoardLocationActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/paroleBoardLocation/list.html";
	
	/* Model keys. */
	
	private static final String PAROLE_BOARD_LOCATION_FORM_MODEL_KEY = 
			"paroleBoardLocationForm";
	
	private static final String PAROLE_BOARD_LOCATION_MODEL_KEY = 
			"paroleBoardLocation";
	
	private static final String LOCATIONS_MODEL_KEY = "locations";
	
	/* Message keys. */


	private static final String PAROLE_BOARD_LOCATION_EXISTS_MESSAGE_KEY = 
			"paroleBoardLocation.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.paroleboardlocation.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleBoardLocationService")
	private ParoleBoardLocationService paroleBoardLocationService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardLocationPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardLocationPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("paroleBoardLocationFormValidator")
	private ParoleBoardLocationFormValidator paroleBoardLocationFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Constructors. */
	
	/** Instantiates controller for parole board locations. */
	public ManageParoleBoardLocationController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create a parole board location.
	 * 
	 * @return screen to create parole board location
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_CREATE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create() {
		ParoleBoardLocationForm paroleBoardLocationForm = 
				new ParoleBoardLocationForm();
		ModelAndView mav = this.prepareMav(paroleBoardLocationForm, null);
		return mav;
	}
	
	/**
	 * Shows screen to edit a parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @return screen to edit a parole board location
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "paroleBoardLocation", required = true)
				final ParoleBoardLocation paroleBoardLocation) {
		ParoleBoardLocationForm paroleBoardLocationForm = 
				new ParoleBoardLocationForm();
		paroleBoardLocationForm.setLocation(paroleBoardLocation.getLocation());
		paroleBoardLocationForm.setVideoConferenceCapable(
				paroleBoardLocation.getVideoConferenceCapable());
		ModelAndView mav = this.prepareMav(paroleBoardLocationForm, 
				paroleBoardLocation);
		mav.addObject(PAROLE_BOARD_LOCATION_MODEL_KEY, paroleBoardLocation);
		return mav;
	}
	
	/**
	 * Saves a parole board location.
	 * 
	 * @param paroleBoardLocationForm parole board location form
	 * @param bindingResult binding result
	 * @return redirect to parole board location listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_CREATE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			final ParoleBoardLocationForm paroleBoardLocationForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.paroleBoardLocationFormValidator.validate(
				paroleBoardLocationForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleBoardLocationForm, 
					null, bindingResult);
			return mav;
		}
		this.paroleBoardLocationService.createParoleBoardLocation(
				paroleBoardLocationForm.getLocation(), 
				paroleBoardLocationForm.getVideoConferenceCapable());
		
		return new ModelAndView(REDIRECT_URL);
	}
	
	/**
	 * Updates a parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @param paroleBoardLocationForm parole board location form
	 * @param bindingResult binding result
	 * @return redirect to parole board location listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "paroleBoardLocation", required = true)
				final ParoleBoardLocation paroleBoardLocation,
			final ParoleBoardLocationForm paroleBoardLocationForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.paroleBoardLocationFormValidator.validate(
				paroleBoardLocationForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleBoardLocationForm, 
					paroleBoardLocation, bindingResult);
			return mav;
		}
		
		this.paroleBoardLocationService.updateParoleBoardLocation(
				paroleBoardLocation,
				paroleBoardLocationForm.getLocation(),
				paroleBoardLocationForm.getVideoConferenceCapable());
		return new ModelAndView(REDIRECT_URL);
	}
	
	/**
	 * Removes a parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @return redirect to parole board location listing screen
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_LOCATION_REMOVE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "paroleBoardLocation", required = true)
				final ParoleBoardLocation paroleBoardLocation) {
		this.paroleBoardLocationService.removeParoleBoardLocation(
				paroleBoardLocation);
		return new ModelAndView(REDIRECT_URL); 
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a parole board location.
	 * 
	 * @return action menu for screen to create/edit a parole board location
	 */
	@RequestMapping(value = "/paroleBoardLocationActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu() {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		return mav;
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity exists exceptions.
	 * 
	 * @param exception duplicate entity exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleParoleBoardLocationExistsException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PAROLE_BOARD_LOCATION_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final ParoleBoardLocationForm paroleBoardLocationForm,
			final ParoleBoardLocation paroleBoardLocation) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_LOCATION_FORM_MODEL_KEY, 
				paroleBoardLocationForm);
		List<Location> locations = this.paroleBoardLocationService
				.findUnassociatedSupervisoryOrganizationLocations(
						paroleBoardLocation);
		mav.addObject(LOCATIONS_MODEL_KEY, locations);
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final ParoleBoardLocationForm paroleBoardLocationForm,
			final ParoleBoardLocation paroleBoardLocation,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(paroleBoardLocationForm, 
				paroleBoardLocation);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ PAROLE_BOARD_LOCATION_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(ParoleBoardLocation.class,
				this.paroleBoardLocationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
				this.locationPropertyEditorFactory.createPropertyEditor());
	}
}