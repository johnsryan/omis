package omis.visitation.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.facility.domain.Facility;
import omis.offender.domain.Offender;
import omis.visitation.domain.Visit;
import omis.visitation.domain.VisitationAssociation;

/**
 * Visit DAO.
 * 
 * @author Joel Norris
 * @version 0.1.1 (Sept 23, 2014)
 * @since OMIS 3.0
 */
public interface VisitDao 
	extends GenericDao<Visit> {

	/**
	 * Return a list of all visits for the specified offender.
	 * 
	 * @param offender offender
	 * @return list of visit
	 */
	List<Visit> findVisitByOffender(Offender offender);

	/**
	 * Returns the visit that exists with the same properties.
	 * 
	 * @param visitationAssociation visitation association
	 * @param startDate start date
	 * @return whether a visit exists with this information
	 */
	Visit findVisit(VisitationAssociation visitationAssociation,
			Date startDate);

	/**
	 * Returns the visit that exists with the same properties excluding the
	 * specified visit.
	 * 
	 * @param visit visit
	 * @param startDate start date
	 * @param endDate end date
	 * @return whether a visit exists with this information
	 */
	Visit findVisitExcluding(Visit visit, VisitationAssociation association,
			Date startDate, Date endDate);

	/**
	 * Returns all visits for the specified visitation association within the
	 * specified start and end time.
	 * 
	 * @param visitationAssociation visitation association
	 * @param startTime start date
	 * @param endTime end date
	 * @return list of visits in range
	 */
	List<Visit> findVisitsInRange(VisitationAssociation visitationAssociation,
			Date startDate, Date endDate);

	/**
	 * Returns all visits after the specified start date and before the
	 * specified end date, except for the specified visit.
	 * 
	 * @param association visitation association
	 * @param startDate start date
	 * @param endDate end date
	 * @param visit visit
	 * @return list of visits
	 */
	List<Visit> findVisitsInRangeExcluding(VisitationAssociation association,
			Date startDate, Date endDate, Visit visit);

	/**
	 * Returns all visits for the specified offender on the specified date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visits 
	 */
	List<Visit> findVisitsByOffenderOnDate(Offender offender, Date date);

	/**
	 * Returns all offenders that have visits on the specified date at the
	 * specified facility.
	 * 
	 * @param facility facility
	 * @param date date
	 * @return list of offenders with visits on the specified date
	 */
	List<Offender> findOffendersWithVisitsByFacilityOnDate(Facility facility,
			Date date);

	/**
	 * Returns all visits for the specified visitation association.
	 * 
	 * @param association visitation association
	 * @return list of visits
	 */
	List<Visit> findVisitsByAssociation(VisitationAssociation association);
}