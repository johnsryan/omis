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
package omis.userpreference.domain;

import java.io.Serializable;

/**
 * Color value.
 * 
 * @author Joel Norris
 * @author Josh Divine 
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class ColorValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short hue;
	
	private Short saturation;
	
	/**
	 * Instantiates a default instance of color value.
	 */
	public ColorValue() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an instance of color value with the specified hue and
	 * saturation.
	 * 
	 * @param hue hue
	 * @param saturation saturation
	 */
	public ColorValue(final Short hue, final Short saturation) {
		this.hue = hue;
		this.saturation = saturation;
	}

	/**
	 * Returns the hue.
	 * 
	 * @return hue
	 */
	public Short getHue() {
		return this.hue;
	}

	/**
	 * Sets the hue.
	 * 
	 * @param hue hue
	 */
	public void setHue(Short hue) {
		this.hue = hue;
	}

	/**
	 * Returns the saturation.
	 * 
	 * @return saturation
	 */
	public Short getSaturation() {
		return this.saturation;
	}

	/**
	 * Sets the saturation.
	 * 
	 * @param saturation saturation
	 */
	public void setSaturation(Short saturation) {
		this.saturation = saturation;
	}
}