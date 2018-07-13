package omis.relationship.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;

/**
 * Data access object for relationships.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 21, 2013)
 * @since OMIS 3.0
 */
public interface RelationshipDao
		extends GenericDao<Relationship> {

	/**
	 * Returns an exact match for the relationship between the first and second
	 * person. If a reverse relationship exists, it will <b>not</b> be returned.
	 * 
	 * <p>If an exact relationship is not found, {@code null} will be returned.
	 * 
	 * @param firstPerson first person
	 * @param secondPerson second person
	 * @return relationship between first and second relationship; {@code null}
	 * if the exact relationship exists (the reverse relationship will not be
	 * returned)
	 */
	Relationship findByPeople(Person firstPerson, Person secondPerson);

	/**
	 * Returns relationships for the specified person.
	 * 
	 * @param person person
	 * @return list of relationships
	 */
	List<Relationship> findByPerson(Person person);
}