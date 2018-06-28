package omis.victim.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.victim.domain.VictimAssociation;

/**
 * Data access object for victim associations.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 20, 2015)
 * @since OMIS 3.0
 */
public interface VictimAssociationDao
		extends GenericDao<VictimAssociation> {

	/**
	 * Finds victim association.
	 * 
	 * @param relationship relationship
	 * @return victim association
	 */
	VictimAssociation find(Relationship relationship);
	
	/**
	 * Finds victim association excluding specified associations.
	 * 
	 * @param relationship relationship
	 * @param excludedAssociations victim associations to exclude
	 * @return victim association
	 */
	VictimAssociation findExcluding(Relationship relationship,
			VictimAssociation... excludedAssociations);
	
	/**
	 * Finds victim associations by offender.
	 * 
	 * @param offender offender
	 * @return victim associations by offender
	 */
	List<VictimAssociation> findByOffender(Offender offender);
	
	/**
	 * Find associations by victim.
	 * 
	 * @param victim victim
	 * @return associations by victim
	 */
	List<VictimAssociation> findByVictim(Person victim);

	/**
	 * Returns the victim association for the specified relationship on the
	 * specified date.
	 * 
	 * @param relationship relationship
	 * @param date date
	 * @return victim association
	 */
	VictimAssociation findOnDate(Relationship relationship, Date date);

	/**
	 * Removes victim associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return number of victim associations removed by relationship
	 */
	int removeByRelationship(Relationship relationship);

	/**
	 * Returns count of victim associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of victim associations by relationship
	 */
	long countByRelationship(Relationship relationship);
}