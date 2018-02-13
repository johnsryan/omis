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

import omis.demographics.dao.HairColorDao;
import omis.demographics.domain.HairColor;
import omis.instance.factory.InstanceFactory;

/**
 * HairColorDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 10, 2016)
 *@since OMIS 3.0
 *
 */
public class HairColorDelegate {
	
	private final HairColorDao hairColorDao;
	private final InstanceFactory<HairColor> hairColorInstanceFactory;

	/**
	 * Contructor for HairColorDelegate.
	 * @param hairColorDao hair color data access object
	 * @param hairColorInstanceFactory hair color instance factory
	 */
	public HairColorDelegate(final HairColorDao hairColorDao, 
			final InstanceFactory<HairColor> hairColorInstanceFactory) {
		this.hairColorDao = hairColorDao;
		this.hairColorInstanceFactory = hairColorInstanceFactory;
	}
	
	/**
	 * Returns a list of all HairColors.
	 * @return List of all HairColors
	 */
	public List<HairColor> findAll() {
		return this.hairColorDao.findAll();
	}
	
	/**
	 * Returns the hair color with the specified name.
	 * 
	 * @param name name of hair color to return
	 * @return hair color with specified name
	 */
	public HairColor findByName(final String name) {
		return this.hairColorDao.findByName(name);
	}

	/**
	 * Create person hair color.
	 *
	 * @param name name
	 * @param valid valid
	 * @return hair color
	 */
	public HairColor create(final String name, final boolean valid) {
		HairColor color = this.hairColorInstanceFactory.createInstance();
		color.setName(name);
		color.setValid(valid);
		return this.hairColorDao.makePersistent(color);
	}
}