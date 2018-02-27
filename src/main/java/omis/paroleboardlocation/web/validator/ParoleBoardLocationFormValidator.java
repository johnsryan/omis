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
package omis.paroleboardlocation.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.paroleboardlocation.web.form.ParoleBoardLocationForm;

/**
 * Validator for parole board location.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 21, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationFormValidator implements Validator {

	/**
	 * Instantiates a default parole board location form validator.
	 */
	public ParoleBoardLocationFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ParoleBoardLocationForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ParoleBoardLocationForm form = (ParoleBoardLocationForm) target;
		if (form.getLocation() == null) {
			errors.rejectValue("location", 
					"paroleBoardLocation.location.empty");
		}
		if (form.getVideoConferenceCapable() == null) {
			errors.rejectValue("videoConferenceCapable", 
					"paroleBoardLocation.videoConferenceCapable.empty");
		}
	}
}