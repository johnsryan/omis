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
package omis.asrc.service.delegate;

import java.util.List;

import omis.asrc.dao.AssessmentSanctionRevocationCenterDao;
import omis.asrc.domain.AssessmentSanctionRevocationCenter;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;

/**
 * Delegate for assessment sanction revocation center.
 * 
 * @author Josh Divine
 * @version 0.1.1 (May 17, 2018)
 * @since OMIS 3.0
 */
public class AssessmentSanctionRevocationCenterDelegate {

	/* Resources. */
	
	private final AssessmentSanctionRevocationCenterDao 
		assessmentSanctionRevocationCenterDao;
	
	private final InstanceFactory<AssessmentSanctionRevocationCenter> 
		assessmentSanctionRevocationCenterInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing assessment sanction revocation 
	 * centers.
	 * 
	 * @param assessmentSanctionRevocationCenterDao data access object for 
	 * assessment sanction revocation centers
	 * @param assessmentSanctionRevocationCenterInstanceFactory instance factory
	 *  for assessment sanction revocation centers
	 */
	public AssessmentSanctionRevocationCenterDelegate(
			final AssessmentSanctionRevocationCenterDao 
				assessmentSanctionRevocationCenterDao,
			final InstanceFactory<AssessmentSanctionRevocationCenter> 
				assessmentSanctionRevocationCenterInstanceFactory) {
		this.assessmentSanctionRevocationCenterDao 
			= assessmentSanctionRevocationCenterDao;
		this.assessmentSanctionRevocationCenterInstanceFactory 
			= assessmentSanctionRevocationCenterInstanceFactory;
	}
	
	/**
	 * Creates a new assessment sanction revocation center.
	 * 
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return created assessment sanction revocation center
	 * @throws DuplicateEntityFoundException thrown when a duplicate assessment
	 * sanction revocation center is found
	 */
	public AssessmentSanctionRevocationCenter create(Location location, 
			String name, Long telephoneNumber) 
					throws DuplicateEntityFoundException {
		if (this.assessmentSanctionRevocationCenterDao.find(location, name, 
				telephoneNumber) != null) {
			throw new DuplicateEntityFoundException("Duplicate assessment "
					+ "sanction revocation center found");
		}
		AssessmentSanctionRevocationCenter assessmentSanctionRevocationCenter 
			= this.assessmentSanctionRevocationCenterInstanceFactory
			.createInstance();
		assessmentSanctionRevocationCenter.setLocation(location);
		assessmentSanctionRevocationCenter.setName(name);
		assessmentSanctionRevocationCenter.setTelephoneNumber(telephoneNumber);
		return this.assessmentSanctionRevocationCenterDao.makePersistent(
				assessmentSanctionRevocationCenter);
	}
	
	/**
	 * Updates the specified assessment sanction revocation center.
	 * 
	 * @param assessmentSanctionRevocationCenter assessment sanction revocation
	 * center
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return updated assessment sanction revocation center
	 * @throws DuplicateEntityFoundException thrown when a duplicate assessment
	 * sanction revocation center is found
	 */
	public AssessmentSanctionRevocationCenter update(
		AssessmentSanctionRevocationCenter assessmentSanctionRevocationCenter,
		Location location, String name, Long telephoneNumber) 
				throws DuplicateEntityFoundException {
		if (this.assessmentSanctionRevocationCenterDao.findExcluding(location, 
				name, telephoneNumber, assessmentSanctionRevocationCenter) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate assessment "
					+ "sanction revocation center found");
		}
		assessmentSanctionRevocationCenter.setLocation(location);
		assessmentSanctionRevocationCenter.setName(name);
		assessmentSanctionRevocationCenter.setTelephoneNumber(telephoneNumber);
		return this.assessmentSanctionRevocationCenterDao.makePersistent(
				assessmentSanctionRevocationCenter);
	}
	
	/**
	 * Removes the specified assessment sanction revocation center
	 * 
	 * @param assessmentSanctionRevocationCenter assessment sanction revocation
	 * center
	 */
	public void remove(AssessmentSanctionRevocationCenter 
			assessmentSanctionRevocationCenter) {
		this.assessmentSanctionRevocationCenterDao.makeTransient(
				assessmentSanctionRevocationCenter);
	}

	/**
	 * Returns a list of all assessment, sanction, revocation centers.
	 * 
	 * @return list of all assessment, sanction, revocation centers
	 */
	public List<AssessmentSanctionRevocationCenter> findAll() {
		return this.assessmentSanctionRevocationCenterDao.findAll();
	}
}