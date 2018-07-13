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

import omis.instance.factory.InstanceFactory;
import omis.supervision.dao.AllowedSupervisoryOrganizationRuleDao;
import omis.supervision.domain.AllowedSupervisoryOrganizationRule;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.AllowedSupervisoryOrganizationRuleExistsException;

/**
 * Delegate for allowed supervisory organization rules.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 10, 2018)
 * @since OMIS 3.0
 */
public class AllowedSupervisoryOrganizationRuleDelegate {

	private final InstanceFactory<AllowedSupervisoryOrganizationRule>
	allowedSupervisoryOrganizationRuleInstanceFactory;
	
	private final AllowedSupervisoryOrganizationRuleDao
	allowedSupervisoryOrganizationRuleDao;
	
	/**
	 * Instantiates delegate for allowed supervisory organization rules.
	 * 
	 * @param allowedSupervisoryOrganizationRuleInstanceFactory instance
	 * factory for allowed supervisory organization rules
	 * @param allowedSupervisoryOrganizationRuleDao data access object for
	 * allowed supervisory organization rules
	 */
	public AllowedSupervisoryOrganizationRuleDelegate(
			final InstanceFactory<AllowedSupervisoryOrganizationRule>
			allowedSupervisoryOrganizationRuleInstanceFactory,
			final AllowedSupervisoryOrganizationRuleDao
			allowedSupervisoryOrganizationRuleDao) {
		this.allowedSupervisoryOrganizationRuleInstanceFactory
			= allowedSupervisoryOrganizationRuleInstanceFactory;
		this.allowedSupervisoryOrganizationRuleDao
			= allowedSupervisoryOrganizationRuleDao;
	}
	
	/**
	 * Creates allowed supervisory organization rule.
	 * 
	 * @param correctionalStatus correctional status
	 * @param supervisoryOrganization supervisory organization
	 * @return allowed supervisory organization rule
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public AllowedSupervisoryOrganizationRule create(
			final CorrectionalStatus correctionalStatus,
			final SupervisoryOrganization supervisoryOrganization)
				throws AllowedSupervisoryOrganizationRuleExistsException {
		if (this.allowedSupervisoryOrganizationRuleDao
				.find(correctionalStatus, supervisoryOrganization) != null) {
			throw new AllowedSupervisoryOrganizationRuleExistsException(
					"Allowed supervisory orgnaization rule exists");
		}
		AllowedSupervisoryOrganizationRule rule
			= this.allowedSupervisoryOrganizationRuleInstanceFactory
				.createInstance();
		rule.setCorrectionalStatus(correctionalStatus);
		rule.setSupervisoryOrganization(supervisoryOrganization);
		return this.allowedSupervisoryOrganizationRuleDao.makePersistent(rule);
	}
}