package omis.locationterm.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Data access object for location term.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 7, 2013)
 * @since OMIS 3.0
 */
public interface LocationTermDao
		extends GenericDao<LocationTerm> {

	/**
	 * Returns location terms for offender.
	 * 
	 * @param offender offender
	 * @return location terms for offender
	 */
	List<LocationTerm> findByOffender(Offender offender);
	
	/**
	 * Returns the location term.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return location term
	 */
	LocationTerm find(Offender offender, Date startDate, Date endDate);
	
	/**
	 * Returns location term that is not one of the excluded location terms.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationTerms excluded location terms
	 * @return location term
	 */
	LocationTerm findExcluding(Offender offender, Date startDate, Date endDate,
			LocationTerm... excludedLocationTerms);
	/**
	 * Returns the location term that is applicable for the specified offender
	 * during the specified date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return location term
	 */
	LocationTerm findByOffenderOnDate(Offender offender, Date date);

	/**
	 * Returns number of location terms for offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of location terms for offender between dates
	 */
	long count(Offender offender, Date startDate, Date endDate);

	/**
	 * Returns number of location terms for offender between dates that are not
	 * excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationTerms location terms to exclude
	 * @return number of location terms for offender between dates
	 */
	long countExcluding(Offender offender, Date startDate, Date endDate,
			LocationTerm... excludedLocationTerms);
	
	/**
	 * Returns location terms for offender at supervisory organization between
	 * dates.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return location terms for offender at supervisory organization between
	 * dates
	 */
	List<LocationTerm> findBySupervisoryOrganizationBetweenDates(
			SupervisoryOrganization supervisoryOrganization, Offender offender,
			Date startDate, Date endDate);

	/**
	 * Returns location terms by offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return location terms by offender between dates
	 */
	List<LocationTerm> findByOffenderBetweenDates(Offender offender,
			Date startDate, Date endDate);
	
	/**
	 * Returns number of location terms for an offender after the specified date 
	 * that are not excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param excludedLocationTerm excluded location term
	 * @return number of location terms for an offender after the specified date
	 */
	long countAfterDateExcluding(Offender offender, Date startDate, 
			LocationTerm excludedLocationTerm);
	
	/**
	 * Ends location term on date. 
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return ended location term
	 * @deprecated update location term instead
	 */
	@Deprecated
	LocationTerm endLocationTerm(Offender offender, Date effectiveDate);
}