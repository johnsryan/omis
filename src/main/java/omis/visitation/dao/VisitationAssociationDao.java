package omis.visitation.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.visitation.domain.VisitationAssociation;

/**
 * Visitation Association DAO.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.1 (June 1, 2016)
 * @since OMIS 3.0
 */
public interface VisitationAssociationDao 
	extends GenericDao<VisitationAssociation> {

	/**
	 * Return a list of all visitors associated with the specified offender.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitor
	 */
	List<VisitationAssociation> findVisitationAssociationByOffender(
			Offender offender, Date date);

	/**
	 * Return a list of all visitation associations with the specified offender
	 * that are either approved or have special visit privilege.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitation associations
	 */
	List<VisitationAssociation> findApprovedVisitationAssociationsByOffender(
			Offender offender, Date date);

	/**
	 * Return a list of all visitation association with the specified offender
	 * that have special visit privileges.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitation associations
	 */
	List<VisitationAssociation> 
	findSpecialVisitVisitationAssociationsByOffender(Offender offender, 
			Date date);

	/**
	 * Returns the visitation association exists with the specified
	 * relationship and start date.
	 * 
	 * @param relationship relationship
	 * @param startDate start date
	 * @return whether visitation association exists
	 */
	VisitationAssociation find(Relationship relationship, Date startDate);

	/**
	 * Returns the visitation association that exists during the specified 
	 * date range for the specified relationship 
	 * 
	 * @param relationship relationship
	 * @param startDate start date
	 * @param endDate end date
	 * @return whether visitation association exists
	 */
	VisitationAssociation findByDateRange(Relationship relationship, 
			Date startDate, Date endDate);

	/**
	 * Returns the visitation association with the matching relationship and
	 * start date, unless it is the specified association.
	 * 
	 * @param association visitation association to exclude
	 * @param relationship relationship
	 * @param startDate start date
	 * @return matching visitation association
	 */
	VisitationAssociation findExcluding(VisitationAssociation association,
			Relationship relationship, Date startDate);

	/**
	 * Returns the visitation association for the specified relationship on
	 * the specified date.
	 * 
	 * @param relationship relationship
	 * @param date date
	 * @return visitation association
	 */
	VisitationAssociation findOnDate(Relationship relationship, Date date);

	/**
	 * Returns visitation associations that are either approved, or are marked
	 * as special visit for the specified offender on the specified date.
	 * The results will be ordered by approved visitation association first,
	 * special visit visitation associations second, and sub ordered by last
	 * name within those groups.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitation associations
	 */
	List<VisitationAssociation> findVisitableVisitationAssociations(
			Offender offender, Date date);
	
	/**
	 * Find if there is dateRange overlap
	 * 
	 * @param relationship
	 * @param dateRange date range
	 * @return count of overlaps
	 */
	long findDateRangeOverLap(Relationship relationship, 
		DateRange dateRange);

	/**
	 * Finds if there is a date range overlap excluding the specified visitation
	 * association.
	 * 
	 * @param relationship relationship
	 * @param dateRange date range
	 * @param association visitation association
	 * @return count of overlaps
	 */
	long findDateRangeOverLapExcluding(Relationship relationship,
			DateRange dateRange, VisitationAssociation association);

	/**
	 * Returns all visitation associations for the specified person.
	 * 
	 * @param person person
	 * @return visitation associations
	 */
	List<VisitationAssociation> findByPerson(Person person);

	/**
	 * Returns count of visitation associations by relationship.
	 * 
	 * @param relationship relationship by which to return count
	 * @return count of visitation associations by relationship
	 */
	long countByRelationship(Relationship relationship);

	/**
	 * Removes visitation associations by relationship.
	 * 
	 * @param relationship relationship by which to remove visitation
	 * associations
	 * @return count of visitation associations removed
	 */
	int removeByRelationship(Relationship relationship);
}