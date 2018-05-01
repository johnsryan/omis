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
package omis.victim.report;

import java.util.List;

import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.victim.domain.VictimAssociation;

/**
 * Report service for victims.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 8, 2015)
 * @since OMIS 3.0
 */
public interface VictimAssociationReportService {

	/**
	 * Returns summary of victims by offender.
	 * 
	 * @param offender offender
	 * @return summary of victims by offender
	 */
	List<VictimAssociationSummary> findSummariesByOffender(Offender offender);
	
	/**
	 * Return summary of victim association.
	 * 
	 * @param victimAssociation - VictimAssociation
	 * @return summary of victim association
	 */
	VictimAssociationSummary summarizeVictimAssociation(
			VictimAssociation victimAssociation);
	
	/**
	 * Returns summary of victim associations by victim.
	 * 
	 * @param victim victim
	 * @return summary of victim associations by victim
	 */
	List<VictimAssociationSummary> findSummariesByVictim(Person victim);
	
	/**
	 * Returns victim document associations for the specified victim and docket.
	 * 
	 * @param victim victim
	 * @return list of victim document associations
	 */
	List<VictimDocumentAssociationSummary> 
		findDocumentAssociationSummariesByVictim(Person victim);

	/**
	 * Returns whether association has notes.
	 * 
	 * @param victimAssociation association
	 * @return whether association has notes
	 */
	boolean hasNotes(VictimAssociation victimAssociation);
	
	
	/**
	 * Returns whether association is an offender.
	 * @param person person
	 * @return is offender
	 */
	boolean isOffender(Person person);
	
	/**
	 * Returns whether a victim association exists.
	 *
	 *
	 * @param offender offender
	 * @param victim victim
	 * @return true or false
	 */
	Boolean victimAssociationExists(Offender offender, Person victim);
	
	/**
	 * Finds victim association.
	 *
	 *
	 * @param offender offender
	 * @param victim victim
	 * @return victim association
	 */
	VictimAssociation findVictimAssociation(Offender offender, Person victim);
}