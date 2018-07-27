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
import java.math.BigDecimal;

/**
 * Color value.
 * 
 * @author Joel Norris
 * @author Josh Divine 
 * @version 0.1.2 (May 11, 2018)
 * @since OMIS 3.0
 */
public class ColorValue implements Serializable {

	private static final long serialVersionUID = 1L;
	private Short hue;
	private Short saturation;
	private Short lightness;
	private BigDecimal opacity;
		
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
	@Deprecated
	public ColorValue(final Short hue, final Short saturation) {
		this.hue = hue;
		this.saturation = saturation;
	}
	
	/**
	 * Instantiates a color value with the specified hue, saturation, opacity, and lightness.
	 * <p>
	 * This color value is to support typical HSLA (Hue Saturation Lightness Alpha) values
	 * for use in variables in generated CSS3 files.
	 * 
	 * @param hue hue
	 * @param saturation saturation
	 * @param lightness lightness
	 * @param opacity opacity
	 */
	public ColorValue(final Short hue, final Short saturation,
			final Short lightness, final BigDecimal opacity) {
		this.hue = hue;
		this.saturation = saturation;
		this.lightness = lightness;
		this.opacity = opacity;
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

	/**
	 * Returns the lightness.
	 * 
	 * @return lightness
	 */
	public Short getLightness() {
		return this.lightness;
	}

	/**
	 * Sets the lightness.
	 * 
	 * @param lightness lightness
	 */
	public void setLightness(final Short lightness) {
		this.lightness = lightness;
	}

	/**
	 * Returns the opacity.
	 * 
	 * @return opacity
	 */
	public BigDecimal getOpacity() {
		return this.opacity;
	}

	/**
	 * Sets the opacity.
	 * 
	 * @param opacity opacity
	 */
	public void setOpacity(final BigDecimal opacity) {
		this.opacity = opacity;
	}
}