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
package omis.bopp.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Board home controller.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 17, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/bopp")
@PreAuthorize("hasRole('USER')")
public class BoardHomeController {

	private static final String HOME_VIEW_NAME = "/bopp/home";
	
	/**
	 * Displays the board home model and view.
	 * 
	 * @return board home model and view.
	 */
	@RequestMapping(value = "/home.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOPP_VIEW') or "
					+ "hasRole('ADMIN')")
	public ModelAndView showContent() {
		return new ModelAndView(HOME_VIEW_NAME);
	}
	
	/**
	 * Init binder.
	 * 
	 * @param binder web data binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
	}
}