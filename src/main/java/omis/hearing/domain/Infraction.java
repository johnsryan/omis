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
package omis.hearing.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.hearing.domain.component.Resolution;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * Infraction.
 * 
 *@author Annie Wahl
 *@version 0.1.1 (Feb 27, 2018)
 *@since OMIS 3.0
 *
 */
public interface Infraction extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the Infraction.
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the ID of the Infraction.
	 * @param id - Long
	 */
	void setId(Long id);
	
	/**
	 * Returns the ConditionViolation for the Infraction.
	 * @return conditionViolation - ConditionViolation
	 */
	ConditionViolation getConditionViolation();
	
	/**
	 * Sets the ConditionViolation for the Infraction.
	 * @param conditionViolation - ConditionViolation
	 */
	void setConditionViolation(ConditionViolation conditionViolation);
	
	/**
	 * Returns the DisciplinaryCodeViolation for the Infraction.
	 * @return disciplinaryCodeViolation - DisciplinaryCodeViolation
	 */
	DisciplinaryCodeViolation getDisciplinaryCodeViolation();
	
	/**
	 * Sets the DisciplinaryCodeViolation for the Infraction.
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 */
	void setDisciplinaryCodeViolation(
			DisciplinaryCodeViolation disciplinaryCodeViolation);
	
	/**
	 * Returns the Hearing for the Infraction.
	 * @return hearing - Hearing
	 */
	Hearing getHearing();
	
	/**
	 * Sets the Hearing for the Infraction.
	 * @param hearing - Hearing
	 */
	void setHearing(Hearing hearing);
	
	/**
	 * Returns the Resolution for the Infraction.
	 * @return resolution - Resolution
	 */
	Resolution getResolution();
	
	/**
	 * Sets the Resolution for the Infraction.
	 * @param resolution - Resolution
	 */
	void setResolution(Resolution resolution);
	
	/**
	 * Returns the Plea for the Infraction.
	 * @return plea - InfractionPlea
	 */
	InfractionPlea getPlea();
	
	/**
	 * Sets the Plea for the Infraction.
	 * @param plea - InfractionPlea
	 */
	void setPlea(InfractionPlea plea);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
}
