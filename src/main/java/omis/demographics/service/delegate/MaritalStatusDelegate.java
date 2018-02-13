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
package omis.demographics.service.delegate;

import java.util.List;

import omis.demographics.dao.MaritalStatusDao;
import omis.demographics.domain.MaritalStatus;
import omis.instance.factory.InstanceFactory;

/**
 * MaritalStatusDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 10, 2016)
 *@since OMIS 3.0
 *
 */
public class MaritalStatusDelegate {
	
	private final MaritalStatusDao maritalStatusDao;
	private final InstanceFactory<MaritalStatus> maritalStatusInstanceFactory;

	/**
	 * Contructor for MaritalStatusDelegate.
	 * @param maritalStatusDao marital status data access object
	 * @param maritalStatusInstanceFactory marital status instance factory
	 */
	public MaritalStatusDelegate(final MaritalStatusDao maritalStatusDao, 
			final InstanceFactory<MaritalStatus> maritalStatusInstanceFactory) {
		this.maritalStatusDao = maritalStatusDao;
		this.maritalStatusInstanceFactory = maritalStatusInstanceFactory;
	}
	
	/**
	 * Returns a list of all MaritalStatus.
	 * @return List of all MaritalStatuss
	 */
	public List<MaritalStatus> findAll() {
		return this.maritalStatusDao.findAll();
	}
	
	/**
	 * Returns a new marital status.
	 *
	 *
	 * @param name name
	 * @param valid valid
	 * @return marital status
	 */
	public MaritalStatus create(final String name, final Boolean valid) {
		
		MaritalStatus maritalStatus 
			= this.maritalStatusInstanceFactory.createInstance();
		maritalStatus.setName(name);
		maritalStatus.setValid(valid);
		return this.maritalStatusDao.makePersistent(maritalStatus);		
	}	
}