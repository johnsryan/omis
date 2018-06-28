package omis.employment.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.audit.service.delegate.VerificationMethodDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.domain.EmploymentNote;
import omis.employment.domain.EmploymentTerm;
import omis.employment.domain.component.Job;
import omis.employment.exception.EmployerExistsException;
import omis.employment.exception.EmploymentExistsException;
import omis.employment.exception.EmploymentNoteExistsException;
import omis.employment.service.EmploymentService;
import omis.employment.service.delegate.EmployerDelegate;
import omis.employment.service.delegate.EmploymentChangeReasonDelegate;
import omis.employment.service.delegate.EmploymentNoteDelegate;
import omis.employment.service.delegate.EmploymentTermDelegate;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;

/**
 * Implementation of employment service.
 * 
 * @author: Yidong Li
 * @author Josh Divine
 * @version: 0.1.1 (Dec 13, 2017)
 * @since: OMIS 3.0
 */

public class EmploymentServiceImpl implements EmploymentService {

	private final EmploymentTermDelegate employmentTermDelegate;
	private final EmploymentNoteDelegate employmentNoteDelegate;
	private final EmploymentChangeReasonDelegate endReasonDelegate;
	private final EmployerDelegate employerDelegate;
	private final OrganizationDelegate organizationDelegate;
	private final LocationDelegate locationDelegate;
	private final AddressDelegate addressDelegate;
	private final VerificationMethodDelegate verificationMethodDelegate;
	private final CountryDelegate countryDelegate;
	private final StateDelegate stateDelegate;
	private final CityDelegate cityDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	
	/**
	 * Instantiates an instance of employment service 
	 * @param employmentTermDelegate employment term delegate
	 * @param employmentNoteDelegate employment note delegate
	 * @param endReasonDelegate employment change reason delegate
	 * @param employerDelegate employer delegate
	 * @param organizationDelegate organization delegate
	 * @param locationDelegate location delegate
	 * @param addressDelegate address delegate
	 * @param verificationMethodDelegate verification method delegate
	 * @param countryDelegate country delegate
	 * @param stateDelegate state delegate
	 * @param cityDelegate city delegate
	 * @param zipCodeDelegate zipcode delegate
	 */
	public EmploymentServiceImpl(
			final EmploymentTermDelegate employmentTermDelegate,
			final EmploymentNoteDelegate employmentNoteDelegate,
			final EmploymentChangeReasonDelegate endReasonDelegate,
			final EmployerDelegate employerDelegate,
			final OrganizationDelegate organizationDelegate,
			final LocationDelegate locationDelegate,
			final AddressDelegate addressDelegate,
			final VerificationMethodDelegate verificationMethodDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final ZipCodeDelegate zipCodeDelegate) {
		this.employmentTermDelegate = employmentTermDelegate;
		this.employmentNoteDelegate = employmentNoteDelegate;
		this.employerDelegate = employerDelegate;
		this.endReasonDelegate = endReasonDelegate;
		this.organizationDelegate = organizationDelegate;
		this.locationDelegate = locationDelegate;
		this.addressDelegate = addressDelegate;
		this.verificationMethodDelegate = verificationMethodDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
	}	

	/** {@inheritDoc} */
	@Override
	public EmploymentTerm create(final Offender offender, 
			final Employer employer, final DateRange dateRange, final Job job,
			final Boolean convictedOfEmployerTheft,	
			final EmploymentChangeReason endReason,
			final VerificationSignature	verificationSignature)	
		throws EmploymentExistsException {
		return this.employmentTermDelegate.create(offender, dateRange, job, 
			convictedOfEmployerTheft, endReason, verificationSignature);
	}	
	
	/** {@inheritDoc} */
	@Override
	public EmploymentTerm update(final EmploymentTerm employmentTerm, 
		final DateRange dateRange, final Job job, 
		final Boolean convictedOfEmployerTheft,	
		final EmploymentChangeReason endReason, 
		final VerificationSignature	verificationSignature )
		throws EmploymentExistsException {
		return this.employmentTermDelegate.update(employmentTerm, dateRange, 
			job, convictedOfEmployerTheft, endReason, verificationSignature);
	}
	
	/** {@inheritDoc} */
	@Override
	public EmploymentNote addNote(final EmploymentTerm term, final String value,
		final Date date)
		throws EmploymentNoteExistsException {
		return this.employmentNoteDelegate.create(term, date, value);
	}
	
	/** {@inheritDoc} */
	@Override
	public EmploymentNote updateNote(final EmploymentNote note, 
		final String value,	final Date date) 
		throws EmploymentNoteExistsException {
		return this.employmentNoteDelegate.update(note, date, value);
	}
	
	/** {@inheritDoc} 
	 * @throws OrganizationExistsException 
	 * @throws LocationExistsException */
	@Override
	public Employer createEmployer(final String name, 
			final Long telephoneNumber, final Address address) 
					throws EmployerExistsException, 
					OrganizationExistsException, LocationExistsException {
		if(this.employerDelegate.findByNameAndAddress(name, address)!=null){
			throw new EmployerExistsException("Employer Already Exist");
		}
		Organization organization = this.organizationDelegate.findByName(name);
		if(organization==null){
			organization = this.organizationDelegate.create(name, null, null);
		}
		Location location = this.locationDelegate.create(organization, null, 
				address);
		return this.employerDelegate.create(location, telephoneNumber);
	}
	
	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String value,final ZipCode zipCode) 
		throws AddressExistsException{
		return this.addressDelegate.findOrCreate(value, null, null, null, 
			zipCode);
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeNote(final EmploymentNote note) {
		this.employmentNoteDelegate.remove(note);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<EmploymentNote> findAdditionalNotes(final EmploymentTerm term) {
		return this.employmentNoteDelegate.findNotes(term);
	}
	
	/** {@inheritDoc} */
	@Override
	public void remove(final EmploymentTerm employmentTerm) {
		this.employmentTermDelegate.remove(employmentTerm);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<EmploymentTerm> findByOffender(final Offender offender) {
		return this.employmentTermDelegate.findByOffender(offender);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<EmploymentChangeReason> findEmploymentChangeReasons( ){
		return this.endReasonDelegate.findEmploymentChangeReasons();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VerificationMethod> findVerificationMethods(){
		return this.verificationMethodDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries(){
		return this.countryDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country){
		return this.stateDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country){
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean hasStates(final Country country){
		if(this.stateDelegate.countByCountry(country)==0){
			return false;
		}
		else
			return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(final String name, final State state, 
		final Country country) throws CityExistsException{
		return this.cityDelegate.create(name, true, state, country);
	}
	
	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final String value, final String extension, 
		final City city) throws ZipCodeExistsException{
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesByCity(final City city){
		return this.zipCodeDelegate.findByCity(city);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state){
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Organization> findOrganizationByPartialName(
		final String partialName){
		return this.organizationDelegate.findByPartialName(partialName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Address> findAddresses(String query){
		return this.addressDelegate.findAddressesByValue(query);
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}
}