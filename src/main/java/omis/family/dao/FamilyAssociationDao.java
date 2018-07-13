package omis.family.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.datatype.DateRange;
import omis.family.domain.FamilyAssociation;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;

/**
 * Data access object for family associations.
 * 
 * @author Ryan Johns
 * @author Stephen Abson
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Nov 21, 2017)
 * @since OMIS 3.0
 */
public interface FamilyAssociationDao
		extends GenericDao<FamilyAssociation> {

	/**
	 * Returns the family association with the specified relationship.
	 *  
	 * @param relationship relationship
	 * @param dateRange date range
	 * 
	 * @return family association
	 */
	FamilyAssociation find(Relationship relationship,
			DateRange dateRange);
	
	/**
	 * Returns the family association with the specified relationship, excluding
	 * the specified family association.
	 * 
	 * @param association family association
	 * @param dateRange date range
	 * @param relationship relationship
	 * @return family association
	 */
	FamilyAssociation findExcluding(
			FamilyAssociation association, Relationship relationship,
			DateRange dateRange);

	/**
	 * Returns the family association for the specified relationship on the
	 * specified date.
	 * 
	 * @param relationship relationship
	 * @param date date
	 * @return family association
	 */
	FamilyAssociation findOnDate(Relationship relationship, Date date);
	
	/**
	 * Find if there is dateRange overlap.
	 * 
	 * @param dateRange date range
	 * @param relationship relationship
	 * @return count of overlaps
	 */
	long findDateRangeOverLap(Relationship relationship, DateRange dateRange);
	
	/**
	 * Returns all existing family associations.
	 * 
	 * @return A list of family associations
	 */
	List<FamilyAssociation> findAll();
	
	/**
	 * Returns the number of conflicting family association.
	 *  
	 * @param relationship relationship
	 * @param dateRange date range
	 * 
	 * @return family association
	 */
	Long findConflicting(Relationship relationship,
		DateRange dateRange);
	
	/**
	 * Returns the number of conflicting family association excluding the one
	 * being updated.
	 *  
	 * @param relationship relationship
	 * @param dateRange date range
	 * @param familyAssociation family association 
	 * 
	 * @return family association
	 */
	Long findConflictingExcluding(Relationship relationship,
		DateRange dateRange, FamilyAssociation familyAssociation);
	
	/**
	 * Returns all existing family associations of a specific offender.
	 * @param offender offender
	 * 
	 * @return A list of family associations
	 */
	List<FamilyAssociation> findByOffender(Offender offender);

	/**
	 * Returns all family associations for the specified person.
	 * 
	 * @param person person
	 * @return list of family associations
	 */
	List<FamilyAssociation> findByPerson(Person person);

	/**
	 * Returns count of family associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of family associations by relationship
	 */
	long countByRelationship(Relationship relationship);

	/**
	 * Removes family associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return number of family associations removed
	 */
	int removeByRelationship(Relationship relationship);
}