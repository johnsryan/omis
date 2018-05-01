package omis.family.report;

import java.util.List;

import omis.family.domain.FamilyAssociation;
import omis.offender.domain.Offender;
import omis.person.domain.Person;

/**
 * Report service for family association.
 * 
 * @author Yidong Li
 * @version 0.1.0 (June 22, 2015)
 * @since OMIS 3.0
 */
public interface FamilyAssociationReportService {

	/**
	 * Returns a list of offender family associations.
	 * 
	 * @param offender offender
	 * @return list of offender family association
	 */

	List<FamilyAssociationSummary> findByOffender(Offender offender); 

	/**
	 * Returns whether a family association exits.
	 *
	 *
	 * @param offender offender
	 * @param familyMember family member
	 * @return true or false
	 */
	Boolean familyAssociationExists(Offender offender, Person familyMember);
	
	/**
	 * Finds family association.
	 *
	 *
	 * @param offender offender
	 * @param familyMember family member
	 * @return family association
	 */
	FamilyAssociation findFamilyAssociation(
			Offender offender, Person familyMember);
	
	/**
	 * Returns whether association is an offender.
	 * 
	 * @param person person
	 * @return is offender
	 */
	Boolean isOffender(Person person);
}