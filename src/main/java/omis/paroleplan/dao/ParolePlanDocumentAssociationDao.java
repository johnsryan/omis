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
package omis.paroleplan.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.document.domain.Document;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;

/**
 * Data access object for parole plan document association.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public interface ParolePlanDocumentAssociationDao 
		extends GenericDao<ParolePlanDocumentAssociation> {

	/**
	 * Returns the parole plan document association for the specified parole 
	 * plan and document.
	 * 
	 * @param parolePlan parole plan
	 * @param document document
	 * @return parole plan document association
	 */
	ParolePlanDocumentAssociation find(ParolePlan parolePlan, Document document);

	/**
	 * Returns a list of parole plan document associations for the specified 
	 * parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return list of parole plan document associations
	 */
	List<ParolePlanDocumentAssociation> findByParolePlan(ParolePlan parolePlan);
}