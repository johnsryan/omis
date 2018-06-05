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
 * 
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 04, 2018)
 * @since OMIS 3.0 
 */
public class TravelDestination implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Long telephoneNumber;
	private Address address;
	private State state;
	private City city;
	private ZipCode zipCode;
	
	/* Constructors. */
	
	/** 
	 * Instantiates a default instance of travel destination.
	 */
	public TravelDestination() {
		//Default constructor.
	}
	
	/**
	 * Instantiates a travel destination with the specified values.
	 * 
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @param address address
	 * @param state state
	 * @param city city
	 * @param zipCode zip code
	 */
	public TravelDestination(final String name, final Long telephoneNumber,
			final Address address, final State state,
			final City city, final ZipCode zipCode) {
		this.name = name;
		this.telephoneNumber = telephoneNumber;
		this.address = address;
		this.state = state;
		this.city = city;
		this.zipCode = zipCode;
	}
	
	/* Getters and Setters */
	
	/**
	 * Returns name.
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Returns telephone number.
	 * 
	 * @return telephone number
	 */
	public Long getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/**
	 * Sets telephone number.
	 * 
	 * @param telephoneNumber telephone number
	 */
	public void setTelephoneNumber(final Long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	/**
	 * Returns address.
	 * 
	 * @return address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * Sets address.
	 * 
	 * @param address address
	 */
	public void setAddress(final Address address) {
		this.address = address;
	}
	
	/**
	 * Returns state.
	 * 
	 * @return state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Sets state.
	 * 
	 * @param state state
	 */
	public void setState(final State state) {
		this.state = state;
	}
	
	/**
	 * Returns city.
	 * 
	 * @return city
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * Sets city.
	 * 
	 * @param city city
	 */
	public void setCity(final City city) {
		this.city = city;
	}
	
	/**
	 * Returns zip code.
	 * 
	 * @return zip code
	 */
	public ZipCode getZipCode() {
		return this.zipCode;
	}

	/**
	 * Sets zip code.
	 * 
	 * @param zipCode zip code
	 */
	public void setZipCode(final ZipCode zipCode) {
		this.zipCode = zipCode;
	}
}