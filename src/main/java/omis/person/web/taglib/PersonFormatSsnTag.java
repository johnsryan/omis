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
package omis.person.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Displays formatted person social security number.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */

public class PersonFormatSsnTag extends SimpleTagSupport {

	// Tag attributes. 
	
	//pattern to look for
	private static final String SSN_PATTERN = "^(\\d{3})(\\d{2})(\\d{4})$";

	//masked SSN format
	private static final String MASKED_SSN = "XXX-XX-$3";
	
	//unmasked SSN format
	private static final String UNMASKED_SSN = "$1-$2-$3";
	
	//the value of the SSN
	private Integer value;
	
	//boolean to decide what format to output
	private Boolean masked;

	/**
	 * Sets the value of the social security number.
	 * 
	 * @param value value
	 */
	public void setValue(final Integer value) {
		this.value = value;
	}

	/**
	 * Sets whether a social security number is masked.
	 * 
	 * @param masked masked
	 */
	public void setMasked(final Boolean masked) {
		this.masked = masked;
	}

	/*
	 * Returns JSP writer.
	 * 
	 * @return JSP writer
	 */
	private JspWriter getJspWriter() {
		return getJspContext().getOut();
	}
	
	/** {@inheritDoc} */
	@Override	
	public void doTag() throws JspException, IOException {
		if (this.value != null) {
			String ssn = String.format("%09d", value);
			if (masked != null) {
				if (masked) {
						 this.getJspWriter().println(ssn
							.replaceAll(SSN_PATTERN, MASKED_SSN));
				} else {
					this.getJspWriter().println(ssn
							.replaceAll(SSN_PATTERN, UNMASKED_SSN));
				}
			} else {
				this.getJspWriter().println("");	
			}
		}
	} 
}