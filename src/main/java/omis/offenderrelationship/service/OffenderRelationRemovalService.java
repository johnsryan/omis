package omis.offenderrelationship.service;

import omis.person.domain.Person;

/**
 * Offender relation removal service.
 * 
 * @author Joel Norris
 * @version 0.1.0 (June 19, 2018)
 * @since OMIS 3.0
 */
public interface OffenderRelationRemovalService {

	/**
	 * Removes the specified person.
	 * 
	 * @param person person
	 * @throws IllegalArgumentException thrown when the specified person is either an offender, or has
	 * existing relationships
	 */
	void remove(Person person) throws IllegalArgumentException;
}