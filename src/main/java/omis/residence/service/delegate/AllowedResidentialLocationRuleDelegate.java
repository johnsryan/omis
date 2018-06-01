package omis.residence.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.dao.AllowedResidentialLocationRuleDao;
import omis.residence.domain.AllowedResidentialLocationRule;
import omis.residence.domain.ResidenceStatus;
import omis.residence.exception.AllowedResidentialLocationRuleExistsException;

/**
 * Allowed residential location rule delegate.
 *
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jul 20, 2017)
 * @since OMIS 3.0
 *
 */
public class AllowedResidentialLocationRuleDelegate {

	/* Data access objects. */
	
	private final AllowedResidentialLocationRuleDao
		allowedResidentialLocationRuleDao;
	
	/* Instance factories. */
	
	private final InstanceFactory<AllowedResidentialLocationRule> 
		allowedResidentialLocationRuleInstanceFactory;
	
	/* Constructor. */

	/**
	 * Instantiates an allowed residential location rule delegate with the 
	 * specified data access object and instance factory.
	 * 
	 * @param allowedResidentialLocationRuleDao allowed residential location 
	 * rule dao
	 * @param allowedResidentialLocationRuleInstanceFactoryallowed residential 
	 * location rule instance factory
	 */
	public AllowedResidentialLocationRuleDelegate(
			final AllowedResidentialLocationRuleDao
				allowedResidentialLocationRuleDao,
			final InstanceFactory<AllowedResidentialLocationRule>
				allowedResidentialLocationRuleInstanceFactory) {
		this.allowedResidentialLocationRuleDao 
			= allowedResidentialLocationRuleDao;
		this.allowedResidentialLocationRuleInstanceFactory = 
				allowedResidentialLocationRuleInstanceFactory;
	}
	
	/* Methods. */
	
	/**
	 *  Creates a allowed residential location rule.
	 * 
	 * @param location location
	 * @param status residence status
	 * @return allowed residential location rule
	 * @throws AllowedResidentialLocationRuleExistsException if duplicate entity exists
	 */
	public AllowedResidentialLocationRule create(final Location location, 
			final ResidenceStatus status) throws LocationExistsException, 
	AllowedResidentialLocationRuleExistsException {
		if (this.allowedResidentialLocationRuleDao.find(location, status) 
				!= null) {
			throw new AllowedResidentialLocationRuleExistsException();
		}
		AllowedResidentialLocationRule allowedResidentialLocationRule = 
				this.allowedResidentialLocationRuleInstanceFactory
				.createInstance();
		populateAllowedResidentialLocationRule(allowedResidentialLocationRule, 
				location, status);
		return this.allowedResidentialLocationRuleDao.makePersistent(
				allowedResidentialLocationRule);
	}
	
	/**
	 *  Updates a allowed residential location rule.
	 * 
	 * @param allowedResidentialLocationRule allowed residential location rule
	 * @param location location
	 * @param status residence status
	 * @return allowed residential location rule
	 * @throws AllowedResidentialLocationRuleExistsException if duplicate entity exists
	 */
	public AllowedResidentialLocationRule update(
			final AllowedResidentialLocationRule allowedResidentialLocationRule,
			final Location location, final ResidenceStatus status) 
					throws AllowedResidentialLocationRuleExistsException {
		if (this.allowedResidentialLocationRuleDao.findExcluding(location, 
				status, allowedResidentialLocationRule) != null) {
			throw new AllowedResidentialLocationRuleExistsException();
		}
		populateAllowedResidentialLocationRule(allowedResidentialLocationRule, 
				location, status);
		return this.allowedResidentialLocationRuleDao.makePersistent(
				allowedResidentialLocationRule);
	}

	/**
	 * Removes a allowed residential location rule.
	 * 
	 * @param allowedResidentialLocationRule allowed residential location rule
	 */
	public void remove(
			AllowedResidentialLocationRule allowedResidentialLocationRule) {
		this.allowedResidentialLocationRuleDao.makeTransient(
				allowedResidentialLocationRule);
	}

	
	/**
	 * Returns a list of all allowed locations.
	 * 
	 * @param residenceStatus residence status
	 * @return list of allowed locations
	 */
	public List<Location> findAllowedLocations(
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDao
				.findAllowedLocations(residenceStatus);
	}

	/**
	 * Returns a list of all allowed locations within specified state.
	 * 
	 * @param state state
	 * @param residenceStatus residence status
	 * @return list of allowed locations
	 */
	public List<Location> findAllowedLocationsInState(final State state, 
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDao
				.findAllowedLocationsInState(state, residenceStatus);
	}

	/**
	 * Returns a list of all allowed locations within specified city.
	 * 
	 * @param city city
	 * @param residenceStatus residence status
	 * @return list of allowed locations
	 */
	public List<Location> findAllowedLocationsInCity(final City city, 
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDao
				.findAllowedLocationsInCity(city, residenceStatus);
	}

	/**
	 * Returns the allowed residential location rule based on location 
	 * and residence status.
	 *
	 *
	 * @param location location
	 * @param residenceStatus residence status
	 * @return allowed residential location rule
	 */
	public AllowedResidentialLocationRule find(final Location location,
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDao
				.find(location, residenceStatus);
	}
	
	// Populates a allowed residential location rule
	private void populateAllowedResidentialLocationRule(
			final AllowedResidentialLocationRule allowedResidentialLocationRule,
			final Location location, final ResidenceStatus status) {
		allowedResidentialLocationRule.setLocation(location);
		allowedResidentialLocationRule.setStatus(status);
	}	
}