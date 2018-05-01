package omis.offenderrelationship.report;

import java.util.Date;
import java.util.List;

import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;

/**
 * Service to report offender relationships.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @version 0.0.1 (Jun 30, 2015)
 * @since OMIS 3.0
 */
public interface OffenderRelationshipReportService {

	/**
	 * Finds summaries of offender relationships by last and first name.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param effectiveDate effective date
	 * @param approximateMatch approximate match
	 * @return summaries of offender relationships by last and first name
	 */
	List<OffenderRelationshipSummary> summarizeByName(String lastName,
			String firstName, Date effectiveDate, Boolean approximateMatch);

	/**
	 * Finds summaries of offender relationships by offender number.
	 * 
	 * @param offenderNumber offender number
	 * @param effectiveDate effective date
	 * @return summaries of offender relationships by offender number
	 */
	List<OffenderRelationshipSummary> summarizeByOffenderNumber(
			Integer offenderNumber, Date effectiveDate);

	/**
	 * Finds summaries of offender relationships by social security number.
	 * 
	 * @param socialSecurityNumber social security number
	 * @param effectiveDate effective date
	 * @return summaries of offender relationships by social security number
	 */
	List<OffenderRelationshipSummary> summarizeBySocialSecurityNumber(
			Integer socialSecurityNumber, Date effectiveDate);

	/**
	 * Finds summaries of offender relationships by birth date.
	 * 
	 * @param birthDate birth date
	 * @param effectiveDate effective date
	 * @return summaries of offender relationships by birth date
	 */
	List<OffenderRelationshipSummary> summarizeByBirthDate(Date birthDate,
			Date effectiveDate);
	
	/**
	 * Count the person.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param approximateMatch approximate match
	 * @return number of people
	 */
	Integer countByPerson(String lastName, String firstName, 
		Boolean approximateMatch);
	
	/**
	 * Get the maximumResults.
	 * 
	 * @return maximum results
	 */
	Integer getMaximumResults();
	
	/**
	 * Check if the search result exceeds the limit.
	 * @param lastName last name
	 * @param firstName first name
	 * @param approximateMatch approximate search or exact search
	 * @return true or false
	 */
	Boolean exceedsMaximumResults(String lastName, String firstName, 
		Boolean approximateMatch);
	
	/**
	 * Check if the relationship exist.
	 *
	 *
	 * @param offender offender
	 * @param relation relations
	 * @return true or false 
	 */
	Boolean relationshipExists(Offender offender, Person relation);	
	
	/**
	 * Find a relationship by offender and relation.
	 *
	 *
	 * @param offender offender
	 * @param relation relation
	 * @return relationship
	 */
	Relationship findRelationship(Offender offender, Person relation);
}