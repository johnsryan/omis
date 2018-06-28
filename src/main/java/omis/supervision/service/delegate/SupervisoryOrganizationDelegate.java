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
package omis.supervision.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.organization.domain.Organization;
import omis.region.domain.State;
import omis.supervision.dao.SupervisoryOrganizationDao;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.SupervisoryOrganizationExistsException;

/**
 * Delegate for supervisory organizations.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (May 17, 2018)
 * @since OMIS 3.0
 */
public class SupervisoryOrganizationDelegate {

	private final InstanceFactory<SupervisoryOrganization>
	supervisoryOrganizationInstanceFactory;
	
	private final SupervisoryOrganizationDao supervisoryOrganizationDao;
	
	/**
	 * Instantiates delegate for supervisory organizations.
	 * 
	 * @param supervisoryOrganizationInstanceFactory instance factory
	 * for supervisory organizations
	 * @param supervisoryOrganizationDao data access object for supervisory
	 * organizations
	 */
	public SupervisoryOrganizationDelegate(
			final InstanceFactory<SupervisoryOrganization>
				supervisoryOrganizationInstanceFactory,
			final SupervisoryOrganizationDao supervisoryOrganizationDao) {
		this.supervisoryOrganizationInstanceFactory
			= supervisoryOrganizationInstanceFactory;
		this.supervisoryOrganizationDao = supervisoryOrganizationDao;
	}
	
	/**
	 * Returns supervisory organizations for correctional status.
	 * 
	 * @param correctionalStatus corrections status
	 * @return supervisory organizations for correctional status
	 */
	public List<SupervisoryOrganization> findForCorrectionalStatus(
			final CorrectionalStatus correctionalStatus) {
		return this.supervisoryOrganizationDao.findForCorrectionalStatus(
				correctionalStatus);
	}
	
	/**
	 * Creates supervisory organization.
	 * 
	 * @param name name
	 * @param alias alias
	 * @param parent parent
	 * @return supervisory organization
	 * @throws DuplicateEntityFoundException if supervisory organization exists
	 */
	public SupervisoryOrganization create(
			final String name, final String alias, final Organization parent)
				throws SupervisoryOrganizationExistsException {
		if (this.supervisoryOrganizationDao.find(name) != null) {
			throw new SupervisoryOrganizationExistsException(
					"Supervisory organization exists");
		}
		SupervisoryOrganization supervisoryOrganization
			= this.supervisoryOrganizationInstanceFactory.createInstance();
		supervisoryOrganization.setName(name);
		supervisoryOrganization.setAlias(alias);
		supervisoryOrganization.setParent(parent);
		return this.supervisoryOrganizationDao.makePersistent(
				supervisoryOrganization);
	}
	
	/**
	 * Returns supervisory organizations allowed for placement.
	 * 
	 * @return supervisory organizations allowed for placement
	 */
	public List<SupervisoryOrganization> findAllowedForPlacement() {
		return this.supervisoryOrganizationDao.findAllowedForPlacement();
	}
	
	/**
	 * Returns supervisory organizations allowed for placement in State.
	 * 
	 * @param state State
	 * @return supervisory organizations allowed for placement in State
	 */
	public List<SupervisoryOrganization> findAllowedForPlacementInState(
			final State state) {
		return this.supervisoryOrganizationDao
				.findAllowedForPlacementInState(state);
	}
	
	/**
	 * Returns a list of all supervisory organizations.
	 * 
	 * @return list of all supervisory organizations
	 */
	public List<SupervisoryOrganization> findAll() {
		return this.supervisoryOrganizationDao.findAll();
	}
}