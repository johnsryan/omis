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
import omis.supervision.dao.AllowedCorrectionalStatusChangeReasonRuleDao;
import omis.supervision.domain.AllowedCorrectionalStatusChangeReasonRule;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.exception.AllowedCorrectionalStatusChangeReasonRuleExistsException;

/**
 * Delegate for allowed correctional status change reason rules.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 23, 2016)
 * @since OMIS 3.0
 */
public class AllowedCorrectionalStatusChangeReasonRuleDelegate {

	private final InstanceFactory<AllowedCorrectionalStatusChangeReasonRule>
	allowedCorrectionalStatusChangeReasonRuleInstanceFactory;
	
	private final AllowedCorrectionalStatusChangeReasonRuleDao
	allowedCorrectionalStatusChangeReasonRuleDao;
	
	/**
	 * Instantiates delegate for allowed correctional status change reason
	 * rules.
	 * 
	 * @param allowedCorrectionalStatusChangeReasonRuleInstanceFactory
	 * instance factory for allowed correctional status change reason rule
	 * @param allowedCorrectionalStatusChangeReasonRuleDao data access object
	 * for allowed correctional status change reason rules
	 */
	public AllowedCorrectionalStatusChangeReasonRuleDelegate(
			final InstanceFactory<AllowedCorrectionalStatusChangeReasonRule>
			allowedCorrectionalStatusChangeReasonRuleInstanceFactory,
			final AllowedCorrectionalStatusChangeReasonRuleDao
			allowedCorrectionalStatusChangeReasonRuleDao) {
		this.allowedCorrectionalStatusChangeReasonRuleInstanceFactory
			= allowedCorrectionalStatusChangeReasonRuleInstanceFactory;
		this.allowedCorrectionalStatusChangeReasonRuleDao
			= allowedCorrectionalStatusChangeReasonRuleDao;
	}
	
	/**
	 * Returns allowed correctional status change reason rule.
	 * 
	 * @param fromCorrectionalStatus from correctional status
	 * @param toCorrectionalStatus to correctional status
	 * @param changeReason change reason
	 * @return allowed correctional status change reason rule
	 */
	public AllowedCorrectionalStatusChangeReasonRule find(
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus,
			final PlacementTermChangeReason changeReason) {
		return this.allowedCorrectionalStatusChangeReasonRuleDao
				.find(fromCorrectionalStatus, toCorrectionalStatus,
						changeReason);
	}
	
	/**
	 * Creates allowed correctional status change reason rule.
	 * 
	 * @param fromCorrectionalStatus from correctional status
	 * @param toCorrectionalStatus to correctional status
	 * @param changeReason change reason
	 * @return allowed correctional status change reason rule
	 * @throws AllowedCorrectionalStatusChangeReasonRuleExistsException if
	 * allowed correctional status change reason rule exists
	 */
	public AllowedCorrectionalStatusChangeReasonRule create(
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus,
			final PlacementTermChangeReason changeReason)
				throws
					AllowedCorrectionalStatusChangeReasonRuleExistsException {
		if (this.allowedCorrectionalStatusChangeReasonRuleDao
				.find(fromCorrectionalStatus, toCorrectionalStatus,
						changeReason) != null) {
			throw new AllowedCorrectionalStatusChangeReasonRuleExistsException(
					"Allowed correctional status change reason rule");
		}
		AllowedCorrectionalStatusChangeReasonRule rule
			= this.allowedCorrectionalStatusChangeReasonRuleInstanceFactory
				.createInstance();
		rule.setChangeReason(changeReason);
		rule.setFromCorrectionalStatus(fromCorrectionalStatus);
		rule.setToCorrectionalStatus(toCorrectionalStatus);
		return this.allowedCorrectionalStatusChangeReasonRuleDao
				.makePersistent(rule);
	}
}