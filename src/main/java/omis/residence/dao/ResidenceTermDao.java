package omis.residence.dao;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.dao.GenericDao;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;

/**
 * Data access object for residence term.
 * 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 19, 2015)
 * @since  OMIS 3.0
 */
public interface ResidenceTermDao 
					extends GenericDao<ResidenceTerm> {

	/**
	 * Returns the residence term excluding the one in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param address address
	 * @param term term
	 * @return residence term
	 */
	ResidenceTerm findExcluding(Person person, DateRange dateRange,
			Address address, ResidenceTerm term);

	/**
	 * Returns the locations within this date range excluding the 
	 * residence term in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param residenceTerm residence term
	 * @return location
	 */
	List<Location> findWithinDateRangeExcluding(Person person,
			DateRange dateRange, ResidenceTerm residenceTerm);

	/**
	 * Returns the locations within this date range.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return location
	 */
	List<Location> findWithinDateRange(Person person, DateRange dateRange);

	/**
	 * Returns the residence term.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param address address
	 * @return residence term
	 */
	ResidenceTerm find(Person person, DateRange dateRange, Address address);

	/**
	 * Returns the residence terms by person and dateRange.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return list of residence terms
	 */
	List<ResidenceTerm> findResidenceTermsByPerson(Person person,
			DateRange dateRange);
	
	/**
	 * Returns the residence terms by person and date range 
	 * excluding the one in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param address address
	 * @param residenceTerm residence term
	 * @return list of residence terms
	 */
	List<ResidenceTerm> findResidenceTermsByPersonExcluding(Person
			person, ResidenceTerm residenceTerm, Address address, 
			DateRange dateRange);

	/**
	 * Returns the primary residence term for the specified person on the
	 * specified date.
	 * 
	 * @param person person
	 * @param date date
	 * @param category residence category
	 * @return primary residence term
	 */
	ResidenceTerm findByPersonAndDate(Person person, Date date, 
			ResidenceCategory category);
	
	/**
	 * Returns the residence term for the specified person with the specified
	 * effective date, status and category.
	 * 
	 * @param person person
	 * @param effectiveDate effective date
	 * @param status status 
	 * @param category category
	 * @return residence term
	 */
	ResidenceTerm findOnDate(Person person, Date effectiveDate, 
			ResidenceStatus status, ResidenceCategory category);

	/**
	 * Returns a list of residence terms associated with the person in view.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @return residence terms
	 */
	List<ResidenceTerm> findAssociatedResidenceTerms(Person person,
			DateRange dateRange);	
	
	/**
	 * Returns a list of residence terms by offender.
	 *
	 *
	 * @param offender offender
	 * @param date date
	 * @return residence terms
	 */
	List<ResidenceTerm> findResidenceTermsByOffender(
			Offender offender, Date date);
}