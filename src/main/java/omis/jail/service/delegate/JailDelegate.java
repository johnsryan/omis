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
package omis.jail.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.jail.dao.JailDao;
import omis.jail.domain.Jail;
import omis.jail.domain.JailCategory;
import omis.location.domain.Location;
import omis.organization.domain.Organization;

/**
 * Delegate for jails.
 *
 * @author Stephen Abson
 * @author Annie Jacques
 * @version 0.0.2 (May 15, 2017)
 * @since OMIS 3.0
 */
public class JailDelegate {

	/* Resources. */
	
	private final JailDao jailDao;
	
	private final InstanceFactory<Jail> jailInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for jails.
	 * 
	 * @param jailDao data access object for jails
	 * @param jailInstanceFactory - InstanceFactory for Jails
	 */
	public JailDelegate(final JailDao jailDao,
			final InstanceFactory<Jail> jailInstanceFactory) {
		this.jailDao = jailDao;
		this.jailInstanceFactory = jailInstanceFactory;
	}
	
	/* Methods. */
	
	/**
	 * Returns jails.
	 * 
	 * @return jails
	 */
	public List<Jail> findAll() {
		return this.jailDao.findAll();
	}

	/**
	 * Returns jails by organization.
	 * 
	 * @param organization organization
	 * @return jails by organization
	 */
	public List<Jail> findByOrganization(Organization organization) {
		return this.jailDao.findByOrganization(organization);
	}
	
	/**
	 * Creates a Jail (for Unit Testing)
	 * @param name - String
	 * @param location - Location
	 * @param category - JailCategory
	 * @param active - Boolean
	 * @param telephoneNumber - Long
	 * @return Created Jail
	 */
	public Jail create(final String name, final Location location,
			final JailCategory category, final Boolean active,
			final Long telephoneNumber){
		Jail jail = this.jailInstanceFactory.createInstance();
		
		jail.setCategory(category);
		jail.setLocation(location);
		jail.setName(name);
		jail.setActive(active);
		jail.setTelephoneNumber(telephoneNumber);
		
		return this.jailDao.makePersistent(jail);
	}
}