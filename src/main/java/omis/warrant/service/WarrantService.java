package omis.warrant.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import omis.condition.domain.ConditionClause;
import omis.docket.domain.Docket;
import omis.jail.domain.Jail;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;
import omis.warrant.domain.WarrantCauseViolation;
import omis.warrant.domain.WarrantNote;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.exception.WarrantArrestExistsException;
import omis.warrant.exception.WarrantCauseViolationExistsException;
import omis.warrant.exception.WarrantExistsException;
import omis.warrant.exception.WarrantNoteExistsException;

/**
 * Warrant service.
 * 
 *@author Annie Jacques 
 *@author Joel Norris
 *@version 0.1.1 (April 4, 2018)
 *@since OMIS 3.0
 */
public interface WarrantService {
	
	/**
	 * Creates a Warrant with the specified properties
	 * @param offender - Offender
	 * @param date - Date
	 * @param addressee - String
	 * @param issuedBy - Person
	 * @param bondable - Boolean
	 * @param bondRecommendation - BigDecimal
	 * @param warrantReason - WarrantReasonCategory
	 * @param holdingJail holding jail
	 * @return Newly created Warrant
	 * @throws WarrantExistsException - When a Warrant already exists
	 * on given date for the specified offender
	 */
	public Warrant create(Offender offender, Date date,
			String addressee, Person issuedBy,
			Boolean bondable, BigDecimal bondRecommendation,
			WarrantReasonCategory warrantReason,
			Jail holdingJail) throws WarrantExistsException;
	
	/**
	 * Updates a Warrant with the specified properties
	 * @param warrant - Warrant to update
	 * @param date - Date
	 * @param addressee - String
	 * @param issuedBy - Person
	 * @param bondable - Boolean
	 * @param bondRecommendation - BigDecimal
	 * @param warrantReason - WarrantReasonCategory
	 * @param holdingJail holding jail
	 * @return Updated Warrant
	 * @throws WarrantExistsException - When a Warrant already exists
	 * on given date for the specified offender
	 */
	public Warrant update(Warrant warrant, Date date,
			String addressee, Person issuedBy,
			Boolean bondable, BigDecimal bondRecommendation,
			WarrantReasonCategory warrantReason, final Jail holdingJail)
					throws WarrantExistsException;
	
	/**
	 * Removes a Warrant
	 * @param warrant - Warrant to remove
	 */
	public void remove(Warrant warrant);
	
	/**
	 * Creates a warrant cause violation for the specified warrant.
	 * 
	 * @param warrant warrant
	 * @param conditionClause condition clause
	 * @param description description
	 * @return newly created warrant cause violation
	 * @throws WarrantCauseViolationExistsException thrown when a duplicate
	 * warrant cause violation
	 * is found
	 */
	public WarrantCauseViolation createWarrantCauseViolation(Warrant warrant,
			ConditionClause conditionClause, String description)
				throws WarrantCauseViolationExistsException;
	
	/**
	 * Updates the specified warrant cause violation.
	 * 
	 * @param warrantCauseViolation warrant cause violation
	 * @param conditionClause condition clause
	 * @param description description
	 * @return updated warrant cause violation
	 * @throws WarrantCauseViolationExistsException thrown when a duplicate
	 * warrant cause violaiton
	 * is found, excluding the specified warrant cause violation
	 */
	public WarrantCauseViolation updateWarrantCauseViolation(
			WarrantCauseViolation warrantCauseViolation,
			ConditionClause conditionClause, String description)
					throws WarrantCauseViolationExistsException;
	
	/**
	 * Removes a WarrantCauseViolation
	 * @param warrantCause - WarrantCauseViolation to remove
	 */
	public void removeWarrantCauseViolation(
			WarrantCauseViolation warrantCauseViolation);
	
	/**
	 * Creates a WarrantNote with the specified properties
	 * @param warrant - Warrant
	 * @param note - String
	 * @param date - Date
	 * @return Newly created WarrantNote
	 * @throws WarrantNoteExistsException - When a WarrantNote already exists
	 * with the given Note and Date for the specified Warrant
	 */
	public WarrantNote createWarrantNote(
			Warrant warrant, String note, Date date)
			throws WarrantNoteExistsException;
	
	/**
	 * Updates a WarrantNote with the specified properties
	 * @param warrantNote - WarrantNote to update
	 * @param note - String
	 * @param date - Date
	 * @return Updated WarrantNote
	 * @throws WarrantNoteExistsException - When a WarrantNote already exists
	 * with the given Note and Date for the specified Warrant
	 */
	public WarrantNote updateWarrantNote(
			WarrantNote warrantNote, String note, Date date)
					throws WarrantNoteExistsException;
	
	/**
	 * Removes a WarrantNote
	 * @param warrantNote - WarrantNote to remove
	 */
	public void removeWarrantNote(WarrantNote warrantNote);
	
	/**
	 * Creates a WarrantArrest with the specified properties
	 * @param warrant - Warrant
	 * @param date - Date
	 * @param jail - Jail
	 * @param contactByDate - Date
	 * @return Newly created WarrantArrest
	 * @throws WarrantArrestExistsException - When a WarrantArrest already
	 * exists for the specified Warrant
	 */
	public WarrantArrest createArrest(Warrant warrant, Date date,
			Jail jail, Date contactByDate)
					throws WarrantArrestExistsException;
	
	/**
	 * Updates specified WarrantArrest with the given properties
	 * @param warrantArrest - WarrantArrest to update
	 * @param date - Date
	 * @param jail - Jail
	 * @param contactByDate - Date
	 * @return Updated WarrantArrest
	 * @throws WarrantArrestExistsException - When another WarrantArrest
	 * already exists for this WarrantArrest's Warrant
	 */
	public WarrantArrest updateArrest(WarrantArrest warrantArrest,
			Date date, Jail jail, Date contactByDate)
					throws WarrantArrestExistsException;
	
	/**
	 * Removes a WarrantArrest
	 * @param warrantArrest - WarrentArrest to remove
	 */
	public void removeArrest(WarrantArrest warrantArrest);
	
	/**
	 * Returns a list of all WarrantCauseViolations with specified Warrant
	 * @param warrant - Warrant
	 * @return List of all WarrantCauseViolations with specified Warrant
	 */
	public List<WarrantCauseViolation> findWarrantCauseViolationsByWarrant(
			Warrant warrant);
	
	/**
	 * Returns a WarrantArrest with specified Warrant
	 * @param warrant - Warrant
	 * @return WarrantArrest with specified Warrant
	 */
	public WarrantArrest findWarrantArrestByWarrant(Warrant warrant);
	
	/**
	 * Returns a list of all WarrantNotes with specified Warrant
	 * @param warrant - Warrant
	 * @return List of all WarrantNotes with specified Warrant
	 */
	public List<WarrantNote> findWarrantNotesByWarrant(Warrant warrant);
	
	/**
	 * Returns a list of all Jails
	 * @return List of all Jails
	 */
	public List<Jail> findAllJails();

	/**
	 * Finds unique condition clauses for the specified offender.
	 * 
	 * @param offender offender
	 * @param date effective date
	 * @return list of condition clauses
	 */
	public List<ConditionClause> findUniqueConditionClausesByOffender(Offender offender, Date date);
	
	/**
	 * Finds dockets by the specified condition clause and offender.
	 * 
	 * @param clause condition clause
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return list of dockets
	 */
	public List<Docket> findDocketsByConditionClauseAndOffender(ConditionClause clause, Offender offender,
			Date effectiveDate);
}
