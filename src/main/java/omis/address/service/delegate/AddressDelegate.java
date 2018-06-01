package omis.address.service.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import omis.address.dao.AddressDao;
import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.util.AddressMatcher;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Delegate for managing addresses.
 *
 * @author Stephen Abson
 * @author Ryan Johns
 * @version 0.0.2 (Jan 25, 2016)
 * @since OMIS 3.0
 */
public class AddressDelegate {

	/* Resources. */
	
	private final AddressDao addressDao;
	
	private final InstanceFactory<Address> addressInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing addresses.
	 * 
	 * @param addressDao data access object for addresses
	 * @param addressInstanceFactory instance factory for addresses
	 * @param auditComponentRetriever audit component retriever
	 */
	public AddressDelegate(
			final AddressDao addressDao,
			final InstanceFactory<Address> addressInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.addressDao = addressDao;
		this.addressInstanceFactory = addressInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Methods. */
	
	/**
	 * Either returns existing address with the specified value and zipcode, or creates a new address.
	 * 
	 * @param number street number
	 * @param designator designator
	 * @param coordinates coordinates
	 * @param buildingCategory building category
	 * @param street street
	 * @param unit unit
	 * @param zipCode ZIP code
	 * @return created address
	 */
	public Address findOrCreate(final String value, final String designator,
		final String coordinates, final BuildingCategory buildingCategory,
		final ZipCode zipCode) {
		Address address = this.addressDao.find(value, zipCode);
		if (address == null) {
			address = this.addressInstanceFactory.createInstance();
			address.setCreationSignature(new CreationSignature(
					this.auditComponentRetriever.retrieveUserAccount(),
					this.auditComponentRetriever.retrieveDate()));
		}
		this.populateAddress(address, value, designator, coordinates,
				buildingCategory, zipCode);
		return this.addressDao.makePersistent(address);
	}
	
	/**
	 * Updates address.
	 * 
	 * @param address address
	 * @param number number
	 * @param designator designator
	 * @param coordinates coordinates
	 * @param buildingCategory building category
	 * @param street street
	 * @param unit unit
	 * @param zipCode ZIP code
	 * @return updated address
	 * @throws DuplicateEntityFoundException if address exists
	 */
	public Address update(final Address address, final String value,
		final String designator, final String coordinates,
		final BuildingCategory buildingCategory, final ZipCode zipCode)
		throws AddressExistsException {
		if (this.addressDao.findExcluding(value, zipCode, address)!=null) {
			throw new AddressExistsException(
					"Duplicate address found");
		}
		this.populateAddress(address, value, designator, coordinates,
				buildingCategory, zipCode);
		return this.addressDao.makePersistent(address);
	}
	
	/**
	 * Removes address.
	 * 
	 * @param address address to remove
	 */
	public void remove(final Address address) {
		this.addressDao.makeTransient(address);
	}
	
	/**
	 * Returns addresses.
	 * 
	 * @return addresses
	 */
	public List<Address> findAll() {
		return this.addressDao.findAll();
	}
	
	/** Returns addresses by search string.
	 * @param searchString - search string.
	 * @param streetSuffixes - List of known street suffixes. This should be
	 * populated to include all possible recognized street suffixes used within 
	 * an address. 
	 * @return addresses matching search criteria or an empty list if no 
	 * results found. */
	@Deprecated
	public List<Address> findAddressByQueryWithKnownSuffixes(
			final String searchString, 
			final List<StreetSuffix> streetSuffixes) {
		AddressMatcher addressMatcher = new AddressMatcher(
				this.suffixSearchTerms(streetSuffixes));
		final List<Address> results;		
		if (addressMatcher.find(searchString)) {
			final String streetNumber = 
					addressMatcher.parse(AddressMatcher.STREET_NUMBER);
			final String streetName =
					addressMatcher.parse(AddressMatcher.STREET_NAME);
			final String streetSuffix =
					addressMatcher.parse(AddressMatcher.STREET_SUFFIX);
			final String cityName =
					addressMatcher.parse(AddressMatcher.CITY);
			final String stateName =
					addressMatcher.parse(AddressMatcher.STATE);
			final String zipCode =
					addressMatcher.parse(AddressMatcher.ZIP_CODE);
			
			if ("".equals(
					addressMatcher.parse(AddressMatcher.LINE_TWO))) {
				results = this.addressDao.findByAddressFields(streetNumber, 
						streetName, streetSuffix);
			} else {
				results = this.addressDao.findByAddressFields(streetNumber, 
						streetName, streetSuffix, cityName, stateName, zipCode);
			}
		} else {
			results = Collections.emptyList();
		}
		return results;
	}
	
	/**
	 * Finds address by rough match of value, and/or exact match of zip code, city, state, or country.
	 * 
	 * @param value value
	 * @param zipCode zip code
	 * @param city city
	 * @param state state
	 * @return list of matching addresses
	 */
	public List<Address> findAddressByQuery(final String value,
			final ZipCode zipCode, final City city, final State state) {
		return this.addressDao.findByAddressFields(value, zipCode, city, state);
	}
	
	/**
	 * Finds addresses by rough match of value.
	 * 
	 * @param value value to match
	 * @return list of matching addresses
	 */
	@Deprecated
	public List<Address> findAddressesByValue(final String value) {
		return this.addressDao.findAddressesByValue(value);
	}
	
	/**
	 * Finds addresses by rough match of value.
	 * 
	 * @param value value to match
	 * @param maxResults max results to return
	 * @return list of matching addresses
	 */
	public List<Address> findAddressesByValue(final String value, 
			final int maxResults) {
		return this.addressDao.findAddressesByValue(value, maxResults);
	}
	
	/* Helper methods. */
	
	// Populates address
	private void populateAddress(final Address address, final String value,
			final String designator, final String coordinates,
			final BuildingCategory buildingCategory, final ZipCode zipCode) {
		address.setValue(value);
		address.setDesignator(designator);
		address.setCoordinates(coordinates);
		address.setBuildingCategory(buildingCategory);
		address.setZipCode(zipCode);
		address.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}
	
	// Constructs suffix pattern for matcher via known suffixes.
	private List<String> suffixSearchTerms(
			final List<StreetSuffix> streetSuffixList) {
		final List<String> result = new ArrayList<String>();
		Iterator<StreetSuffix> suffixIterator = 
				streetSuffixList.iterator();
		while (suffixIterator.hasNext()) {
			StreetSuffix ss = suffixIterator.next();
			result.add(ss.getAbbreviation());
			result.add(ss.getName());
		}
		return result;
	}
}