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

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Travel Destination.
 * @author Yidong Li
 * @version 0.1.0 (May 17, 2018)
 * @since OMIS 3.0 
 */
public class TravelDestination implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Long telephoneNumber;
	private Address address;
	private State state;
	private City city;
	private ZipCode zipCode;
	
	/** Constructor. */
	public TravelDestination() {
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public Long getTelephoneNumber() {
		return this.telephoneNumber;
	}

	public void setTelephoneNumber(final Long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}
	
	public State getState() {
		return this.state;
	}

	public void setState(final State state) {
		this.state = state;
	}
	
	public City getCity() {
		return this.city;
	}

	public void setCity(final City city) {
		this.city = city;
	}
	
	public ZipCode getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(final ZipCode zipCode) {
		this.zipCode = zipCode;
	}
}