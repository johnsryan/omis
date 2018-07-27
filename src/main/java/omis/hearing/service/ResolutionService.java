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
package omis.hearing.service;

import java.util.Date;
import java.util.List;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.component.Resolution;
import omis.hearing.exception.HearingStatusExistsException;
import omis.hearing.exception.InfractionExistsException;
import omis.offender.domain.Offender;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * Resolution Service.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
public interface ResolutionService {
	
	/**
	 * Creates a new Infraction with the specified properties.
	 * @param hearing - Hearing
	 * @param conditionViolation - ConditionViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Newly created Infraction
	 * @throws InfractionExistsException - When an Infraction already exists
	 * with given Hearing, ConditionViolation, and DisciplinaryCodeViolation.
	 */
	Infraction createInfraction(Hearing hearing,
			ConditionViolation conditionViolation,
			DisciplinaryCodeViolation disciplinaryCodeViolation,
			Resolution resolution, InfractionPlea plea)
					throws InfractionExistsException;
	
	/**
	 * Updates an Infraction with the specified properties.
	 * @param infraction - Infraction to update
	 * @param conditionViolation - ConditionViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Updated Infraction
	 * @throws InfractionExistsException - When an Infraction already exists
	 * with given Hearing, ConditionViolation, and DisciplinaryCodeViolation.
	 */
	Infraction updateInfraction(Infraction infraction,
			ConditionViolation conditionViolation,
			DisciplinaryCodeViolation disciplinaryCodeViolation,
			Resolution resolution, InfractionPlea plea)
					throws InfractionExistsException;
	
	/**
	 * Removes an Infraction.
	 * @param infraction - Infraction to remove
	 */
	void removeInfraction(Infraction infraction);
	
	/**
	 * Creates a HearingStatus with the specified properties.
	 * @param hearing - Hearing
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Newly Created HearingStatus
	 * @throws HearingStatusExistsException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	HearingStatus createHearingStatus(Hearing hearing, String description,
			Date date, HearingStatusCategory category)
					throws HearingStatusExistsException;
	
	/**
	 * Updates a HearingStatus with the specified properties.
	 * @param hearingStatus - HearingStatus to update
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Updated HearingStatus
	 * @throws HearingStatusExistsException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	HearingStatus updateHearingStatus(HearingStatus hearingStatus,
			String description, Date date,
			HearingStatusCategory category)
					throws HearingStatusExistsException;
	
	/**
	 * Removes a Hearing Status.
	 * @param hearingStatus - HearingStatus to remove
	 */
	void removeHearingStatus(HearingStatus hearingStatus);
	
	/**
	 * Creates an ImposedSanction with the specified properties.
	 * @param infraction - Infraction
	 * @param offender - Offender
	 * @param description - String
	 * @return Newly created ImposedSanction
	 * @throws DuplicateEntityFoundException - When an ImposedSanction already
	 * exists with given Infraction and Description
	 */
	ImposedSanction createImposedSanction(Infraction infraction,
			Offender offender, String description)
			throws DuplicateEntityFoundException;
	
	/**
	 * Updates an ImposedSanction with the specified properties.
	 * @param imposedSanction - ImposedSanction to update
	 * @param offender - Offender
	 * @param description - String
	 * @return Updated ImposedSanction
	 * @throws DuplicateEntityFoundException - When an ImposedSanction already
	 * exists with given Infraction and Description
	 */
	ImposedSanction updateImposedSanction(ImposedSanction imposedSanction,
			Offender offender, String description)
			throws DuplicateEntityFoundException;
	
	/**
	 * Removes an ImposedSanction.
	 * @param imposedSanction - ImposedSanction to remove
	 */
	void removeImposedSanction(ImposedSanction imposedSanction);
	
	/**
	 * Returns a list of Infractions by specified Hearing.
	 * @param hearing - Hearing
	 * @return List of Infractions by specified Hearing
	 */
	List<Infraction> findInfractionsByHearing(Hearing hearing);
	
	/**
	 * Returns an ImposedSanction found by specified Infraction.
	 * @param infraction - Infraction
	 * @return ImposedSanction found by specified Infraction
	 */
	ImposedSanction findImposedSanctionByInfraction(
			Infraction infraction);
	
	/**
	 * Returns a ConditionViolation with the specified ID.
	 * @param id - Long
	 * @return ConditionViolation with specified ID
	 */
	ConditionViolation findConditionViolationById(Long id);
	
	/**
	 * Returns a DisciplinaryCodeViolation with the specified ID.
	 * @param id - Long
	 * @return DisciplinaryCodeViolation with specified ID
	 */
	DisciplinaryCodeViolation findDisciplinaryCodeViolationById(Long id);
	
	/**
	 * Returns a list of all valid Infraction Pleas.
	 * @return List of all valid Infraction Pleas.
	 */
	List<InfractionPlea> findInfractionPleas();
}
