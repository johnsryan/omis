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
package omis.facility.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.facility.dao.UnitDao;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.instance.factory.InstanceFactory;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Unit delegate.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.3 (May 23, 2018)
 * @since OMIS 3.0
 */
public class UnitDelegate {

	/* Data access objects. */
	
	private UnitDao unitDao;
	
	/* Instance factories. */
	
	private InstanceFactory<Unit> unitInstanceFactory;
	
	/* Exception messages. */
	
	private static final String DUPLICATE_UNIT_FOUND_MESSAGE
		= "Duplicate unit found";
	
	/* Constructor. */
	
	/**
	 * Instantiates an instance of unit delegate with the specified unit data
	 * access object and unit instance factory.
	 * 
	 * @param unitDao unit data access object
	 * @param unitInstanceFactory unit instance factory
	 */
	public UnitDelegate(final UnitDao unitDao,
			final InstanceFactory<Unit> unitInstanceFactory) {
		this.unitDao = unitDao;
		this.unitInstanceFactory = unitInstanceFactory;
	}
	
	/**
	 * Creates a new unit with the specified values.
	 * 
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param valid whether valid applies
	 * @param faciilty facility
	 * @param complex complex
	 * @return newly created unit
	 * @throws DuplicateEntityFoundException thrown when a duplicate
	 * complex is found
	 */
	public Unit create(final String name, final String abbreviation,
			final Boolean valid, final Facility facility,
			final Complex complex) throws DuplicateEntityFoundException {
		if (this.unitDao.find(name, facility) != null) {
			throw new DuplicateEntityFoundException(
				DUPLICATE_UNIT_FOUND_MESSAGE);
		}
		Unit unit = this.unitInstanceFactory.createInstance();
		unit.setName(name);
		unit.setAbbreviation(abbreviation);
		unit.setValid(valid);
		unit.setComplex(complex);
		unit.setFacility(facility);
		return this.unitDao.makePersistent(unit);
	}
	
	/**
	 * Updates the specified unit.
	 * 
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param valid valid
	 * @param complex complex
	 * @param unit unit to update
	 * @return updated unit
	 * @throws DuplicateEntityFoundException thrown when a duplicate unit is
	 * found, except for the unit being updated 
	 */
	public Unit update(final String name, final String abbreviation,
			final Boolean valid, final Complex complex, final Unit unit)
		throws DuplicateEntityFoundException {
		if (this.unitDao.findExcluding(name, unit.getFacility(), unit) != null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_UNIT_FOUND_MESSAGE);
		}
		unit.setName(name);
		unit.setAbbreviation(abbreviation);
		unit.setValid(valid);
		unit.setComplex(complex);
		return this.unitDao.makePersistent(unit);
	}
	
	/**
	 * Removes the specified unit.
	 * 
	 * @param unit unit to remove
	 */
	public void remove(final Unit unit) {
		this.unitDao.makeTransient(unit);
	}
	
	/**
	 * Returns the unit with the specified facility and complex.
	 * 
	 * @param facility facility
	 * @param complex complex
	 * @return list of units
	 */
	public List<Unit> findUnits(final Facility facility, final Complex complex) {
		return this.unitDao.findUnits(facility, complex);
	}
	
	/**
	 * Returns the unit with the specified facility.
	 * 
	 * @param facility facility
	 * @return list of units
	 */
	public List<Unit> findByFacility(final Facility facility) {
		return this.unitDao.findByFacility(facility);
	}

	/**
	 * Returns a list of units for the specified supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @return list of units
	 */
	public List<Unit> findBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		return this.unitDao.findBySupervisoryOrganization(
				supervisoryOrganization);
	}
}