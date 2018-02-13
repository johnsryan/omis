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
package omis.paroleboardcondition.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Parole Board Agreement.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public interface ParoleBoardAgreement extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the Parole Board Agreement.
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the ID of the Parole Board Agreement.
	 * @param id - Long
	 */
	void setId(Long id);
	
	/**
	 * Returns the Agreement for the Parole Board Agreement.
	 * @return agreement - Agreement
	 */
	Agreement getAgreement();
	
	/**
	 * Sets the Agreement for the Parole Board Agreement.
	 * @param agreement - Agreement
	 */
	void setAgreement(Agreement agreement);
	
	/**
	 * Returns the Category for the Parole Board Agreement.
	 * @return category - ParoleBoardAgreementCategory
	 */
	ParoleBoardAgreementCategory getCategory();
	
	/**
	 * Sets the Category for the Parole Board Agreement.
	 * @param category - ParoleBoardAgreementCategory
	 */
	void setCategory(ParoleBoardAgreementCategory category);
	
	/**
	 * Returns the hearing analysis for the parole board agreement.
	 * 
	 * @return hearing analysis
	 */
	HearingAnalysis getHearingAnalysis();
	
	/**
	 * Sets the hearing analysis for the parole board agreement.
	 * 
	 * @param hearingAnalysis hearing analysis
	 */
	void setHearingAnalysis(HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns the board hearing for the parole board agreement.
	 * 
	 * @return board hearing
	 */
	BoardHearing getBoardHearing();
	
	/**
	 * Sets the board hearing for the parole board agreement.
	 * 
	 * @param boardHearing board hearing
	 */
	void setBoardHearing(BoardHearing boardHearing);
	
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
