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
package omis.paroleboardcondition.dao;

import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.dao.GenericDao;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;

/**
 * Parole Board Agreement Data Access Object.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public interface ParoleBoardAgreementDao
		extends GenericDao<ParoleBoardAgreement> {
	
	/**
	 * Finds a parole board agreement by the specified properties.
	 * 
	 * @param agreement agreement
	 * @param hearingAnalysis hearing analysis
	 * @param boardHearing board hearing
	 * @param category parole board agreement category
	 * @return parole board agreement
	 */
	ParoleBoardAgreement find(Agreement agreement, BoardHearing boardHearing, 
			HearingAnalysis hearingAnalysis, 
			ParoleBoardAgreementCategory category);
	
	/**
	 * Finds a parole board agreement by the specified properties excluding
	 * given parole board agreement.
	 * 
	 * @param agreement agreement
	 * @param hearingAnalysis hearing analysis
	 * @param boardHearing board hearing
	 * @param category parole board agreement category
	 * @param paroleBoardAgreementExcluding parole board agreement to exclude.
	 * @return parole board agreement
	 */
	ParoleBoardAgreement findExcluding(Agreement agreement, 
			BoardHearing boardHearing, HearingAnalysis hearingAnalysis, 
			ParoleBoardAgreementCategory category,
			ParoleBoardAgreement paroleBoardAgreementExcluding);
}
