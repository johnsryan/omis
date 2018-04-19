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
package omis.region.domain.impl;

import omis.region.domain.City;
import omis.region.domain.CityCountyAssociation;
import omis.region.domain.County;

/**
 * Implementation of association of city to county.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2015)
 * @since OMIS 3.0
 */
public class CityCountyAssociationImpl
		implements CityCountyAssociation {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private City city;
	
	private County county;
	
	/** Instantiates a default association of city to county. */
	public CityCountyAssociationImpl() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setCity(City city) {
		this.city = city;
	}

	/** {@inheritDoc} */
	@Override
	public City getCity() {
		return this.city;
	}

	/** {@inheritDoc} */
	@Override
	public void setCounty(final County county) {
		this.county = county;
	}

	/** {@inheritDoc} */
	@Override
	public County getCounty() {
		return this.county;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CityCountyAssociation)) {
			return false;
		}
		CityCountyAssociation that = (CityCountyAssociation) obj;
		if (this.getCity() == null) {
			throw new IllegalStateException("City required");
		}
		if (!this.getCity().equals(that.getCity())) {
			return false;
		}
		if (this.getCounty() == null) {
			throw new IllegalStateException("County required");
		}
		if (!this.getCounty().equals(that.getCounty())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getCity() == null) {
			throw new IllegalStateException("City required");
		}
		if (this.getCounty() == null) {
			throw new IllegalStateException("County required");
		}
		int hashCode = 14;
		hashCode = 29 * this.getCity().hashCode() + hashCode;
		hashCode = 29 * this.getCounty().hashCode() + hashCode;
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including city name and
	 * county name.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		final String cityName;
		if (this.getCity() != null) {
			cityName = this.getCity().getName();
		} else {
			cityName = null;
		}
		final String countyName;
		if (this.getCounty() != null) {
			countyName = this.getCounty().getName();
		} else {
			countyName = null;
		}
		return String.format("#%d - %s, %s", this.getId(), cityName,
				countyName);
	}
}