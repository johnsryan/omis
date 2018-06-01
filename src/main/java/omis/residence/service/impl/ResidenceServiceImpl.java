/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.residence.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.delegate.VerificationMethodDelegate;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.exception.LocationNotAllowedException;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.AllowedResidentialLocationRuleExistsException;
import omis.residence.exception.NonResidenceTermExistsException;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.ResidenceService;
import omis.residence.service.delegate.AllowedResidentialLocationRuleDelegate;
import omis.residence.service.delegate.NonResidenceTermDelegate;
import omis.residence.service.delegate.ResidenceTermDelegate;

/**
 * Residence service implementation.
 * 
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @author Joel Norris
 * @version 0.1.2 (January 11, 2017)
 * @since  OMIS 3.0
 *
 */
public class ResidenceServiceImpl implements ResidenceService {

	/* Delegates. */
	private final ResidenceTermDelegate residenceTermDelegate;
	private final AddressDelegate addressDelegate;
	private final NonResidenceTermDelegate nonResidenceTermDelegate;
	private final LocationDelegate locationDelegate;
	private final OrganizationDelegate organizationDelegate;		
	private final StateDelegate stateDelegate;
	private final CityDelegate cityDelegate;
	private final AllowedResidentialLocationRuleDelegate
		allowedResidentialLocationRuleDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	private final VerificationMethodDelegate verificationMethodDelegate;
	
	/**
	 * Residence service implementation.
	 * 
	 * @param stateDelegate state delegate
	 * @param cityDelegate city delegate
	 * @param allowedResidentialLocationRuleDelegate allowed residential 
	 * location rule delegate
	 * @param zipCodeDelegate zip code delegate
	 * @param residenceTermDelegate residence term delegate
	 * @param addressDelegate address delegate
	 * @param nonResidenceTermDelegate non residence term delegate
	 * @param locationDelegate location delegate
	 * @param organizationDelegate organization delegate
	 * @param verificationMethodDelegate verification method delegate
	 */
	public ResidenceServiceImpl(
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final AllowedResidentialLocationRuleDelegate 
				allowedResidentialLocationRuleDelegate,
			final ZipCodeDelegate zipCodeDelegate,
			final ResidenceTermDelegate residenceTermDelegate,
			final AddressDelegate addressDelegate,
			final NonResidenceTermDelegate nonResidenceTermDelegate,
			final LocationDelegate locationDelegate,
			final OrganizationDelegate organizationDelegate,
			final VerificationMethodDelegate verificationMethodDelegate) {
			
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.allowedResidentialLocationRuleDelegate 
				= allowedResidentialLocationRuleDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
		this.residenceTermDelegate = residenceTermDelegate;
		this.addressDelegate = addressDelegate;
		this.nonResidenceTermDelegate = nonResidenceTermDelegate;
		this.locationDelegate = locationDelegate;
		this.organizationDelegate = organizationDelegate;
		this.verificationMethodDelegate = verificationMethodDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String number, final String designator, 
			final String coordinates, final BuildingCategory category, 
			final ZipCode zipCode) throws AddressExistsException {
		
		return this.addressDelegate.findOrCreate(
				number, designator, coordinates, category, zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public Address updateAddress(final Address address, final String number,
			final String designator, final String coordinates, 
			final BuildingCategory category, final ZipCode zipCode) 
					throws AddressExistsException {
		
		return this.addressDelegate.update(
				address, number, designator, coordinates, category, zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public Location createLocation(final String organization, 
			final DateRange dateRange, final Address address, 
			final ResidenceStatus status) 					
					throws LocationExistsException, 
					OrganizationExistsException, 
					AllowedResidentialLocationRuleExistsException {
		
		Organization newOrganization 
			= this.organizationDelegate.create(organization, null, null);
		Location newLocation = this.locationDelegate.create(
				newOrganization, dateRange, address);
		this.allowedResidentialLocationRuleDelegate.create(newLocation, status);
		return newLocation;
	}
	
	/** {@inheritDoc} */
	@Override
	public Location updateLocation(final Location location,
			final Organization organization, final DateRange dateRange, 
			final Address address) throws LocationExistsException {
		
		return this.locationDelegate.update(
				location, organization, dateRange, address);
	}
	
	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm createHomelessTerm(final Person person,
			final DateRange dateRange, final City city, final State state, 
			final String notes, final Boolean confirmed)
			throws ResidenceStatusConflictException, 
			NonResidenceTermExistsException {
		//gets list of non residence terms by dateRange
		List<NonResidenceTerm> nonResidenceTerms = this.nonResidenceTermDelegate
				.findNonResidenceTermByPersonAndDateRange(
						person, dateRange);
		//if it is then loop thru the residence terms  
		List<ResidenceTerm> residenceTerms 
			= this.residenceTermDelegate.findAssociatedResidenceTerms(
					person, dateRange);
		if (residenceTerms.size() > 0) {
			// for each term check to see if the category is not primary
			for (ResidenceTerm term : residenceTerms) {
				if (!ResidenceCategory.SECONDARY.equals(
						term.getCategory())) {
					throw new ResidenceStatusConflictException(
							"Conflicting residence status active "
							+ "within this date range");
				}
			}
		} else if (nonResidenceTerms.size() > 0) {				
			throw new ResidenceStatusConflictException(
					"Conflicting non residence status active "
					+ "within this date range");
		} 
		return this.nonResidenceTermDelegate.createHomelessTerm(
				person, dateRange, city, state, notes, confirmed);			
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm updateHomelessTerm(
			final NonResidenceTerm nonResidenceTerm, final DateRange dateRange, 
			final City city, final State state, final String notes, 
			final Boolean confirmed) 
					throws ResidenceStatusConflictException, 
					NonResidenceTermExistsException {
		//get list of nonresidenceTerms
		List<NonResidenceTerm> nonResidenceTerms 
			= this.nonResidenceTermDelegate
				.findNonResidenceTermByPersonAndDateRangeExcluding(
						nonResidenceTerm.getPerson(), dateRange, 
						nonResidenceTerm);
		//if it is then loop thru the residence terms  
		List<ResidenceTerm> residenceTerms 
			= this.residenceTermDelegate.findResidenceTermsByPerson(
					nonResidenceTerm.getPerson(), dateRange);
		if (residenceTerms.size() > 0) {
			// for each term check to see if the category is not primary
			for (ResidenceTerm term : residenceTerms) {
				if (!ResidenceCategory.SECONDARY.equals(
						term.getCategory())) {
					throw new ResidenceStatusConflictException(
							"Conflicting residence status active "
							+ "within this date range");
				}
			}
		} else if (nonResidenceTerms.size() > 0) {				
			throw new ResidenceStatusConflictException(
					"Conflicting non residence status active "
					+ "within this date range");
		} 
		return this.nonResidenceTermDelegate.updateHomelessTerm(
				nonResidenceTerm, dateRange, city, state, notes, confirmed);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findAllowedLocations(
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDelegate
				.findAllowedLocations(residenceStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findAllowedLocationsInState(final State state,
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDelegate
				.findAllowedLocationsInState(state, residenceStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findAllowedLocationsInCity(final City city,
			final ResidenceStatus residenceStatus) {
		return this.allowedResidentialLocationRuleDelegate
				.findAllowedLocationsInCity(city, residenceStatus);
	}

	/** {@inheritDoc} */
	@Override
	public void removeNonResidenceTerm(
			final NonResidenceTerm nonResidenceTerm) {
		this.nonResidenceTermDelegate.removeNonResidenceTerm(nonResidenceTerm);	
	}

	/** {@inheritDoc} */
	@Override
	public void removeResidenceTerm(final ResidenceTerm residenceTerm) {	
		this.residenceTermDelegate.removeResidenceTerm(residenceTerm);
	}

	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesInState(final State state) {
		return this.zipCodeDelegate.findInStates(state);
	}

	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesInCity(final City city) {
		return this.zipCodeDelegate.findByCity(city);
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStatesInHomeCountry() {		
		return this.stateDelegate.findInHomeCountry();
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesInState(final State state) {
		return this.cityDelegate.findByState(state);
	}

	/** {@inheritDoc} */
	@Override
	public List<VerificationMethod> findAllVerificationMethods() {
		return this.verificationMethodDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public ResidenceTerm createResidenceTerm(final Person person,
			final DateRange dateRange, final Boolean primary, 
			final Address address, final Boolean fosterCare, 
			final Boolean confirmed, final String notes,
			final VerificationSignature verificationSignature)
			throws PrimaryResidenceExistsException, 
			ResidenceStatusConflictException,
			ResidenceTermExistsException, NonResidenceTermExistsException { 
		final ResidenceCategory residenceCategory;
		final ResidenceStatus residenceStatus;
		if (primary) {	
			residenceCategory = ResidenceCategory.PRIMARY;
			if (fosterCare) {
				residenceStatus = ResidenceStatus.FOSTER_CARE;
			} else {
				residenceStatus = ResidenceStatus.RESIDENT;
			}
		} else {
			residenceCategory = ResidenceCategory.SECONDARY;
			residenceStatus = ResidenceStatus.RESIDENT;
		}		
		List<ResidenceTerm> residenceTerms = this.residenceTermDelegate
				.findResidenceTermsByPerson(person, dateRange);	
		//need to do a lookup to see if nonResidenceTerm falls within dateRange
		List<NonResidenceTerm> nonResidenceTerms = 
				this.nonResidenceTermDelegate.findAssociatedNonResidenceTerms(
						person, dateRange);
		//loop thru residence terms
		for (ResidenceTerm term : residenceTerms) {		
			//if residence term in list is primary and the new residence 
			//term is not equal to secondary
			if (ResidenceCategory.PRIMARY.equals(term.getCategory())
						&& (!ResidenceCategory.SECONDARY.equals(
								residenceCategory))) {
				throw new PrimaryResidenceExistsException(
						"Conflicting primary residence found "
								+ "within date range");
			} 
		}
		if  (nonResidenceTerms.size() > 0) {
			for (NonResidenceTerm nterm : nonResidenceTerms) {
				if (ResidenceCategory.PRIMARY.equals(residenceCategory) 
						&& ResidenceStatus.HOMELESS.equals(nterm.getStatus())) {
					if (nterm.getDateRange().getEndDate() == null) {
						if (dateRange.getStartDate().getTime() 
								< nterm.getDateRange().getStartDate()
								.getTime()) {
							throw new ResidenceStatusConflictException(
									"Residence status date range conflict");
						}
						DateRange dateRange2 = new DateRange();
						dateRange2.setStartDate(
								nterm.getDateRange().getStartDate());
						dateRange2.setEndDate(dateRange.getStartDate());
						this.nonResidenceTermDelegate.updateHomelessTerm(
								nterm, dateRange2, nterm.getCity(), 
								nterm.getState(), nterm.getNotes(), 
								nterm.getConfirmed());
					} 
				}
			}
		}
		return this.residenceTermDelegate.createResidenceTerm(person, dateRange,
				residenceCategory, address, residenceStatus, confirmed, notes, 
				verificationSignature);
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceTerm updateResidenceTerm(final ResidenceTerm residenceTerm,
			final DateRange dateRange, final Boolean primary, 
			final Address address, final Boolean fosterCare, 
			final Boolean confirmed, final String notes,
			final VerificationSignature verificationSignature)
			throws ResidenceTermExistsException,
				PrimaryResidenceExistsException {
		final ResidenceCategory residenceCategory;
		final ResidenceStatus residenceStatus;
		if (primary) {	
			residenceCategory = ResidenceCategory.PRIMARY;
			if (fosterCare) {
				residenceStatus = ResidenceStatus.FOSTER_CARE;
			} else {
				residenceStatus = ResidenceStatus.RESIDENT;
			}
		} else {
			residenceCategory = ResidenceCategory.SECONDARY;
			residenceStatus = ResidenceStatus.RESIDENT;
		}
		List<ResidenceTerm> residenceTerms = this.residenceTermDelegate
				.findResidenceTermsByPersonExcluding(
						residenceTerm.getPerson(), residenceTerm, address, 
						dateRange);	
		for (ResidenceTerm term : residenceTerms) {
			if (ResidenceCategory.PRIMARY.equals(term.getCategory())
					&& (!ResidenceCategory.SECONDARY.equals(
							residenceCategory))) {
				throw new PrimaryResidenceExistsException(
						"Conflicting primary residence found "
								+ "within date range");
			}
		}
		return this.residenceTermDelegate.updateResidenceTerm(
				residenceTerm, dateRange, residenceCategory, address, 
				residenceStatus, confirmed, notes, verificationSignature);
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm createNonResidenceTerm(final Person person,
			final DateRange dateRange, final ResidenceStatus status, 
			final Location location, final Boolean confirmed, 
			final String notes,
			final VerificationSignature verificationSignature)
			throws LocationNotAllowedException,
				ResidenceStatusConflictException, 
				NonResidenceTermExistsException {
		if (this.allowedResidentialLocationRuleDelegate
				.find(location, status) == null) {
			throw new LocationNotAllowedException("Location not allowed");
		}
		return this.nonResidenceTermDelegate.createNonResidenceTerm(
				person, dateRange, status, location, confirmed, notes, 
				verificationSignature);
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm updateNonResidenceTerm(
			final NonResidenceTerm nonResidenceTerm, final DateRange dateRange,
			final ResidenceStatus status, final Location location, 
			final Boolean confirmed, final String notes, 
			final VerificationSignature verificationSignature)
			throws LocationNotAllowedException, 
				ResidenceStatusConflictException, 
				NonResidenceTermExistsException {
		if (this.allowedResidentialLocationRuleDelegate
				.find(location, status) == null) {
			throw new LocationNotAllowedException("Location not allowed");
		} 
		return this.nonResidenceTermDelegate.updateNonResidenceTerm(
				nonResidenceTerm, dateRange, status, location, confirmed, 
				notes, verificationSignature);
	}

	/** {@inheritDoc} */
	@Override
	public State findHomeState() {		
		return this.stateDelegate.findHomeState();
	}

	/** {@inheritDoc} */
	@Override
	public List<ResidenceTerm> findResidenceTermsByOffender(
			final Offender offender, final Date date) {
		return this.residenceTermDelegate.findResidenceTermsByOffender(
				offender, date);
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceTerm findPrimaryResidence(final Date date, 
			final Person person) {
		return this.residenceTermDelegate.findByPersonAndDate(person, date,
				ResidenceCategory.PRIMARY);
	}

	/** {@inheritDoc} */
	@Override
	public List<NonResidenceTerm> findNonResidenceTerms(final Date date, 
			final Person person) {
		return this.nonResidenceTermDelegate
				.findNonResidenceTermsByPersonAndDate(person, date);
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm endNonResidenceTerm(final NonResidenceTerm term, 
			final Date endDate)
			throws NonResidenceTermExistsException {
		final DateRange dateRange;
		if (term.getDateRange() == null) {
			dateRange = new DateRange(null, endDate);
		} else {
			dateRange = new DateRange(term.getDateRange().getStartDate(), 
					endDate);
		}
		
		return this.nonResidenceTermDelegate.updateNonResidenceTerm(term,
				dateRange, term.getStatus(), term.getLocation(),
				term.getConfirmed(), term.getNotes(), 
				term.getVerificationSignature());
	}

	/** {@inheritDoc} */
	@Override
	public City createCity(final String name, final State state, 
			final Country country) throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}

	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final String value, final String extension, 
			final City city) throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
}