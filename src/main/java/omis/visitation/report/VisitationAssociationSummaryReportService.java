/*
 * OMIS - Offender Management Information System.
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
package omis.visitation.report;

import java.util.Date;
import java.util.List;

import omis.facility.domain.Facility;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.report.AlternateNameSummary;
import omis.visitation.domain.VisitationAssociation;

/**
 * Report service for visitor list.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jul 24, 2013)
 * @since OMIS 3.0
 */
public interface VisitationAssociationSummaryReportService {

	/**
	 * Return a list of visitor summary for the specified offender on the 
	 * specified date. The date should be specified with the time of day desired
	 * to decide on the currentlyVisiting property of each visitor summary.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitor summary
	 */
	List<VisitationAssociationSummary> summarizeVisitationAssociations(
			Offender offender, Date date);

	/**
	 * Return a list of visitor summary for the specified facility on the 
	 * specified date. The date should be specified with the time of day desired
	 * to decide on the currentlyVisiting property of each visitor summary.
	 * 
	 * @param facility facility
	 * @param dateTime date time
	 * @return list of visitor summary
	 */
	List<VisitationAssociationSummary>
		summarizeVisitationAssociationsByFacility(Facility facility,
				Date dateTime);

	/**
	 * Summarizes visitation associations for the specified offender within the
	 * specified start date and end date.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return list of visitation association summaries
	 */
	List<VisitationAssociationSummary> summarizeVisitationAssociationsInRange(
			Offender offender, Date startDate, Date endDate);
	
	/**
	 * Summarize alternative names.
	 *
	 *
	 * @param person person
	 * @param effectiveDate effective date
	 * @return list of alternative names
	 */
	List<AlternateNameSummary>  summarizeAlternativeNames(Person person, 
			Date effectiveDate);
	
	/**
	 * Returns whether a visitation association exists.
	 *
	 *
	 * @param offender offender
	 * @param visitor visitor
	 * @return true or false
	 */
	Boolean visitationAssociationExists(Offender offender, Person visitor);
	
	/**
	 * Finds visitation association.
	 *
	 *
	 * @param offender offender
	 * @param visitor visitor
	 * @return visitation association
	 */
	VisitationAssociation findVisitationAssociation(
			Offender offender, Person visitor);
	
	/**
	 * Returns whether association is an offender.
	 *
	 *
	 * @param person person
	 * @return is offender
	 */
	Boolean isOffender(Person person);
}