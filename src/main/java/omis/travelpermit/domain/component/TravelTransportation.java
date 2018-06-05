package omis.travelpermit.domain.component;
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
import java.io.Serializable;

import omis.travelpermit.domain.TravelMethod;

/**
 * Travel transportation.
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 06, 2018)
 * @since OMIS 3.0 
 */
public class TravelTransportation implements Serializable {
	private static final long serialVersionUID = 1L;
	private String number;
	private String description;
	private TravelMethod method;
	
	/** 
	 * Instantiates a default instance of travel transportation.
	 */
	public TravelTransportation() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an instance of travel transportation with the specified
	 * number, description and travel method.
	 * 
	 * @param number number
	 * @param description description
	 * @param method travel method
	 */
	public TravelTransportation(final String number,
			final String description, final TravelMethod method) {
		this.number = number;
		this.description = description;
		this.method = method;
	}
	
	/**
	 * Returns number.
	 * 
	 * @return number
	 */
	public String getNumber() {
		return this.number;
	}

	/**
	 * Sets number.
	 * 
	 * @param number number
	 */
	public void setNumber(final String number) {
		this.number = number;
	}
	
	/**
	 * Returns description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * Returns method {@link TravelMethod}.
	 * @return
	 */
	public TravelMethod getMethod() {
		return this.method;
	}

	/**
	 * Sets travel method {@link TravelMethod}.
	 * 
	 * @param method travel method
	 */
	public void setMethod(final TravelMethod method) {
		this.method = method;
	}
}