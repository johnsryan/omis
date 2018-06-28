package omis.residence.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;

/**
 * Data access object for non residence term.
 * 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 19, 2015)
 * @since  OMIS 3.0
 */
public interface NonResidenceTermDao 
					extends GenericDao<NonResidenceTerm> {

	/**
	 * Returns the non residence term excluding the one in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param nonResidenceTerm non residence term
	 * @return non residence term
	 */
	NonResidenceTerm findExcluding(Person person, DateRange dateRange,
			Location location, State state, City city, ResidenceStatus status, 
			NonResidenceTerm nonResidenceTerm);

	/**
	 * Returns the non residence term.
	 * 
	 * @param person person
	 * @param dateRange dateRange
	 * @param location location 
	 * @param status status
	 * @return non residence term
	 */
	NonResidenceTerm find(Person person, DateRange dateRange,
			Location location, State state, City city, ResidenceStatus status);

	/**
	 * Returns a list of locations excluding the one in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param nonResidenceTerm  non residence term
	 * @return locations
	 */
	List<Location> findWithinDateRangeExcluding(Person person,
			DateRange dateRange, NonResidenceTerm nonResidenceTerm);

	/**
	 * Returns a list of locations.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return locations
	 */
	List<Location> findWithinDateRange(Person person, DateRange dateRange);

	/**
	 * Returns a list of non residence terms.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return non residence terms
	 */
	List<NonResidenceTerm> findNonResidenceTermByPersonAndDateRange(
			Person person, DateRange dateRange);

	/**
	 * Returns a list of non residence terms, excluding the one in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param nonResidenceTerm non residence term
	 * @return non residence terms
	 */
	List<NonResidenceTerm> findNonResidenceTermByPersonAndDateRangeExcluding(
			Person person, DateRange dateRange,
			NonResidenceTerm nonResidenceTerm);

	/**
	 * Returns non residence terms for the specified person within the 
	 * dateRange of this residenceTerm.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return non residence term
	 */
	List<NonResidenceTerm> findAssociatedNonResidenceTerms(Person person,
			DateRange dateRange);

	/**
	 * Returns non residence terms for the specified person on the 
	 * specified date.
	 * 
	 * @param person person
	 * @param date effective date
	 * @return list of non residence terms
	 */
	List<NonResidenceTerm> findByPersonAndDate(Person person, Date date);
}