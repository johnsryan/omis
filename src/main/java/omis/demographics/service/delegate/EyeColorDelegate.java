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

import omis.demographics.dao.EyeColorDao;
import omis.demographics.domain.EyeColor;
import omis.instance.factory.InstanceFactory;

/**
 * EyeColorDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 10, 2016)
 *@since OMIS 3.0
 *
 */
public class EyeColorDelegate {
	
	private final EyeColorDao eyeColorDao;
	private final InstanceFactory<EyeColor> eyeColorInstanceFactory;

	/**
	 * Contructor for EyeColorDelegate.
	 * @param eyeColorDao eye color data access object
	 * @param eyeColorInstanceFactory eye color instance factory
	 */
	public EyeColorDelegate(final EyeColorDao eyeColorDao, 
			final InstanceFactory<EyeColor> eyeColorInstanceFactory) {
		this.eyeColorDao = eyeColorDao;
		this.eyeColorInstanceFactory = eyeColorInstanceFactory;
	}
	
	/**
	 * Returns a list of all EyeColors.
	 * @return List of all EyeColors
	 */
	public List<EyeColor> findAll() {
		return this.eyeColorDao.findAll();
	}
	
	/**
	 * Returns an EyeColor by name.
	 * @param name - String
	 * @return EyeColor
	 */
	public EyeColor findByName(final String name) {
		return this.eyeColorDao.findByName(name);
	}
	
	/**
	 * Create a new eye color.
	 *
	 *
	 * @param name name
	 * @param valid valid
	 * @return eye color
	 */
	public EyeColor create(final String name, final Boolean valid) {
		EyeColor eyeColor = this.eyeColorInstanceFactory.createInstance();
		eyeColor.setName(name);
		eyeColor.setValid(valid);
		return this.eyeColorDao.makePersistent(eyeColor);
	}
}