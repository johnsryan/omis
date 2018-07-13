/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;
import omis.travelpermit.exception.TravelPermitNoteExistsException;
import omis.travelpermit.service.TravelPermitService;
import omis.travelpermit.service.delegate.TravelMethodDelegate;
import omis.travelpermit.service.delegate.TravelPermitDelegate;
import omis.travelpermit.service.delegate.TravelPermitNoteDelegate;
import omis.travelpermit.service.delegate.TravelPermitPeriodicityDelegate;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Implementation of service for travel permit.
 * 
 * @author Yidong Li
 * @version 0.1.1 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public class TravelPermitServiceImpl implements TravelPermitService {
	private final TravelMethodDelegate travelMethodDelegate;
	private final TravelPermitDelegate travelPermitDelegate;
	private final TravelPermitNoteDelegate travelPermitNoteDelegate;
	private final TravelPermitPeriodicityDelegate travelPermitPeriodicityDelegate; 
	private final AddressDelegate addressDelegate;
	private final CityDelegate cityDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	private final StateDelegate stateDelegate;
	private final CountryDelegate countryDelegate;
	private final UserAccountDelegate userAccountDelegate;
	
	
	/**
	 * Instantiates an implementation of service for travel permit.
	 * 
	 * @param travelMethodDelegate travel method delegate
	 * @param travelPermitDelegate travel permit delegate
	 * @param travelPermitNoteDelegate travel permit note delegate
	 * @param travelPermitPeriodicityDelegate travel permit periodicity delegate
	 * @param addressDelegate address delegate
	 * @param cityDelegate city delegate
	 * @param zipCodeDelegate zip code delegate
	 * @param stateDelegate state delegate
	 * @param countryDelegate country delegate
	 * @param userAccountDelegate user account delegate
	 * 
	 */
	public TravelPermitServiceImpl(
			final TravelMethodDelegate travelMethodDelegate,
			final TravelPermitDelegate travelPermitDelegate,
			final TravelPermitNoteDelegate travelPermitNoteDelegate,
			final TravelPermitPeriodicityDelegate travelPermitPeriodicityDelegate, 
			final AddressDelegate addressDelegate,
			final CityDelegate cityDelegate,
			final ZipCodeDelegate zipCodeDelegate,
			final StateDelegate stateDelegate,
			final CountryDelegate countryDelegate,
			final UserAccountDelegate userAccountDelegate) {
			this.addressDelegate = addressDelegate;
			this.cityDelegate = cityDelegate;
			this.travelMethodDelegate = travelMethodDelegate;
			this.travelPermitDelegate = travelPermitDelegate;
			this.travelPermitNoteDelegate = travelPermitNoteDelegate;
			this.travelPermitPeriodicityDelegate
			= travelPermitPeriodicityDelegate;
			this.zipCodeDelegate = zipCodeDelegate;
			this.stateDelegate = stateDelegate;
			this.countryDelegate = countryDelegate;
			this.userAccountDelegate = userAccountDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public TravelPermit create(final Offender offender,
			final String purpose,
			final DateRange dateRange,
			final TravelPermitPeriodicity periodicity,
			final TravelPermitIssuance issuance,
			final TravelTransportation transportation,
			final TravelDestination destination,
			final OtherTravelers otherTravelers)
				throws TravelPermitExistsException {
		return this.travelPermitDelegate.createTravelPermit(offender,
			periodicity, issuance, transportation, destination, dateRange,
			purpose, otherTravelers);
	}

	/** {@inheritDoc} */
	@Override
	public TravelPermit update(TravelPermit travelPermit, String purpose,
			DateRange dateRange, Offender offender,
			TravelPermitPeriodicity periodicity,
			TravelPermitIssuance issuance, TravelTransportation transportation,
			TravelDestination destination, OtherTravelers otherTravelers)
			throws TravelPermitExistsException {
		return this.travelPermitDelegate.updateTravelPermit(travelPermit,
			periodicity, issuance, transportation, destination, dateRange,
			purpose, otherTravelers);
		}
	
	/** {@inheritDoc} */
	@Override
	public void remove(final TravelPermit travelPermit){
		this.travelPermitDelegate.removePermit(travelPermit);
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermitNote createNote(final TravelPermit travelPermit,
		final Date date, final String value)
		throws TravelPermitNoteExistsException {
		return this.travelPermitNoteDelegate.createTravelPermitNote(
			travelPermit, date, value);
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermitNote updateNote(final TravelPermitNote travelPermitNote,
			final Date date, final String value)
			throws TravelPermitNoteExistsException {
		return this.travelPermitNoteDelegate.updateTravelPermitNote(
			travelPermitNote, date, value);
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeNote(final TravelPermitNote travelPermitNote){
		this.travelPermitNoteDelegate.removeTravelPermitNote(travelPermitNote);
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String number, final ZipCode address)
		throws AddressExistsException {
		return this.addressDelegate.findOrCreate(number, null, null, null,
			address);
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(final State state, final String name,
		final Country country) throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}
	
	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final City city, final String value,
		final String extension) throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermit> findByOffender(Offender offender){
		return this.travelPermitDelegate.findByOffender(offender);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermitNote> findNotes(final TravelPermit travelPermit){
		return this.travelPermitNoteDelegate.findNotes(travelPermit);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelMethod> findTravelMethods(){
		return this.travelMethodDelegate.findTravelMethods();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermitPeriodicity> findPeriodicity(){
		return this.travelPermitPeriodicityDelegate
			.findTravelPermitPeriodicities();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Address> findAddresses(final String query){
		return this.addressDelegate.findAddressesByValue(query, 5);
		
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodes(final City city){
		return this.zipCodeDelegate.findByCity(city);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStates(){
		return this.stateDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country country){
		return this.stateDelegate.countByCountry(country) > 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state){
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country){
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries(){
		return this.countryDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public Country findHomeCountry(){
		if(this.stateDelegate.findHomeState()!=null){
			return this.stateDelegate.findHomeState().getCountry();
		} else {
			return null;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country){
		return this.stateDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<UserAccount> searchUserAccounts(final String query){
		return this.userAccountDelegate.search(query);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(final Country country){
		return this.cityDelegate.findByCountryWithoutState(country);
	}
}