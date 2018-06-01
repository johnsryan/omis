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
 * @version 0.1.0 (May 17, 2018)
 * @since OMIS 3.0 
 */
public class TravelTransportation implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String number;
	private String description;
	private TravelMethod method;
	
	/** Constructor. */
	public TravelTransportation() {
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	
	public TravelMethod getMethod() {
		return this.method;
	}

	public void setMethod(final TravelMethod method) {
		this.method = method;
	}
}