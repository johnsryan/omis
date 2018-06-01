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
package omis.treatment.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.treatment.dao.TreatmentCenterDao;
import omis.treatment.domain.TreatmentCenter;

/**
 * Delegate for treatment center.
 * 
 * @author Josh Divine
 * @version 0.1.1 (May 17, 2018)
 * @since OMIS 3.0
 */
public class TreatmentCenterDelegate {

	/* Resources. */
	
	private final TreatmentCenterDao treatmentCenterDao;
	
	private final InstanceFactory<TreatmentCenter> 
		treatmentCenterInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing treatment center.
	 * 
	 * @param treatmentCenterDao data access object for treatment center
	 * @param treatmentCenterInstanceFactory instance factory for treatment 
	 * center
	 */
	public TreatmentCenterDelegate(
			final TreatmentCenterDao treatmentCenterDao,
			final InstanceFactory<TreatmentCenter> 
				treatmentCenterInstanceFactory) {
		this.treatmentCenterDao = treatmentCenterDao;
		this.treatmentCenterInstanceFactory = treatmentCenterInstanceFactory;
	}

	/**
	 * Creates a new treatment center.
	 * 
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return created treatment center
	 * @throws DuplicateEntityFoundException thrown when a duplicate treatment 
	 * center is found
	 */
	public TreatmentCenter create(Location location, String name, 
			Long telephoneNumber) throws DuplicateEntityFoundException {
		if (this.treatmentCenterDao.find(location, name, telephoneNumber) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate treatment center"
					+ " found");
		}
		TreatmentCenter treatmentCenter 
			= this.treatmentCenterInstanceFactory.createInstance();
		treatmentCenter.setLocation(location);
		treatmentCenter.setName(name);
		treatmentCenter.setTelephoneNumber(telephoneNumber);
		return this.treatmentCenterDao.makePersistent(treatmentCenter);
	}
	
	/**
	 * Updates the specified treatment center.
	 * 
	 * @param treatmentCenter treatment center
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return updated treatment center
	 * @throws DuplicateEntityFoundException thrown when a duplicate treatment 
	 * center is found
	 */
	public TreatmentCenter update(TreatmentCenter treatmentCenter, 
			Location location, String name, Long telephoneNumber) 
				throws DuplicateEntityFoundException {
		if (this.treatmentCenterDao.findExcluding(location, 
				name, telephoneNumber, treatmentCenter) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate treatment center "
					+ "found");
		}
		treatmentCenter.setLocation(location);
		treatmentCenter.setName(name);
		treatmentCenter.setTelephoneNumber(telephoneNumber);
		return this.treatmentCenterDao.makePersistent(treatmentCenter);
	}
	
	/**
	 * Removes the specified treatment center.
	 * 
	 * @param treatmentCenter treatment center
	 */
	public void remove(TreatmentCenter treatmentCenter) {
		this.treatmentCenterDao.makeTransient(treatmentCenter);
	}

	/**
	 * Returns a list of all treatment centers.
	 * 
	 * @return list of all treatment centers
	 */
	public List<TreatmentCenter> findAll() {
		return this.treatmentCenterDao.findAll();
	}
}