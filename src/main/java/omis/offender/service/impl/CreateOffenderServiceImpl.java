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
package omis.offender.service.impl;

import java.util.Date;
import java.util.List;

import omis.citizenship.domain.Citizenship;
import omis.citizenship.service.delegate.CitizenshipDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.PersonDemographics;
import omis.demographics.domain.Race;
import omis.demographics.domain.Sex;
import omis.demographics.domain.Tribe;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.demographics.service.delegate.BuildDelegate;
import omis.demographics.service.delegate.ComplexionDelegate;
import omis.demographics.service.delegate.EyeColorDelegate;
import omis.demographics.service.delegate.HairColorDelegate;
import omis.demographics.service.delegate.MaritalStatusDelegate;
import omis.demographics.service.delegate.PersonDemographicsDelegate;
import omis.demographics.service.delegate.RaceDelegate;
import omis.demographics.service.delegate.TribeDelegate;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.exception.OperationNotAuthorizedException;
import omis.immigration.domain.AlienResidence;
import omis.immigration.service.delegate.AlienResidenceDelegate;
import omis.media.domain.Photo;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderExistsException;
import omis.offender.service.CreateOffenderService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.offenderflag.domain.OffenderFlag;
import omis.offenderflag.domain.OffenderFlagCategory;
import omis.offenderflag.service.delegate.OffenderFlagCategoryDelegate;
import omis.offenderflag.service.delegate.OffenderFlagDelegate;
import omis.offenderphoto.domain.OffenderPhotoAssociation;
import omis.offenderphoto.service.delegate.OffenderPhotoAssociationDelegate;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.religion.domain.Religion;
import omis.religion.domain.ReligiousPreference;
import omis.religion.service.delegate.ReligionDelegate;
import omis.religion.service.delegate.ReligiousPreferenceDelegate;

/**
 * Implementation of create offender service.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @author Annie Jacques
 * @version 0.1.0 (Nov 10, 2016)
 * @since OMIS 3.0
 */
public class CreateOffenderServiceImpl
		implements CreateOffenderService {
	
	/* Delegates. */
	
	private final OffenderDelegate offenderDelegate;
	
	private PersonDemographicsDelegate personDemographicsDelegate;
	
	private ReligiousPreferenceDelegate religiousPreferenceDelegate;
	
	private CitizenshipDelegate citizenshipDelegate;
	
	private AlienResidenceDelegate alienResidenceDelegate;
	
	private OffenderFlagDelegate offenderFlagDelegate;
	
	private OffenderPhotoAssociationDelegate offenderPhotoAssociationDelegate;
	
	private CountryDelegate countryDelegate;
	
	private StateDelegate stateDelegate;
	
	private CityDelegate cityDelegate;
	
	private BuildDelegate buildDelegate;
	
	private ComplexionDelegate complexionDelegate;
	
	private EyeColorDelegate eyeColorDelegate;
	
	private HairColorDelegate hairColorDelegate;
	
	private MaritalStatusDelegate maritalStatusDelegate;
	
	private RaceDelegate raceDelegate;
	
	private TribeDelegate tribeDelegate;
	
	private SuffixDelegate suffixDelegate;
	
	private ReligionDelegate religionDelegate;
	
	private OffenderFlagCategoryDelegate offenderFlagCategoryDelegate;
	
	
	
	/**
	 * 
	 * Instantiates an instance of create offender service.
	 * 
	 * @param offenderDelegate offender delegate
	 * @param personDemographicsDelegate person demographics delegate
	 * @param religiousPreferenceDelegate religious preference delegate
	 * @param citizenshipDelegate citizenship delegate
	 * @param alienResidenceDelegate alien residence delegate
	 * @param offenderFlagDelegate offender flag delegate
	 * @param offenderPhotoAssociationDelegate offender photo association
	 * delegate
	 * @param countryDelegate country delegate
	 * @param stateDelegate State delegate
	 * @param cityDelegate city delegate
	 * @param buildDelegate build delegate
	 * @param complexionDelegate complexion delegate
	 * @param eyeColorDelegate eye color delegate
	 * @param hairColorDelegate hair color delegate
	 * @param maritalStatusDelegate marital status delegate
	 * @param raceDelegate race delegate
	 * @param tribeDelegate tribe delegate
	 * @param suffixDelegate suffix delegate
	 * @param religionDelegate religion delegate
	 * @param offenderFlagCategoryDelegate offender flag category delegate
	 */
	public CreateOffenderServiceImpl(
			final OffenderDelegate offenderDelegate,
			final PersonDemographicsDelegate personDemographicsDelegate,
			final ReligiousPreferenceDelegate religiousPreferenceDelegate,
			final CitizenshipDelegate citizenshipDelegate,
			final AlienResidenceDelegate alienResidenceDelegate,
			final OffenderFlagDelegate offenderFlagDelegate,
			final OffenderPhotoAssociationDelegate
				offenderPhotoAssociationDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final BuildDelegate buildDelegate,
			final ComplexionDelegate complexionDelegate,
			final EyeColorDelegate eyeColorDelegate,
			final HairColorDelegate hairColorDelegate,
			final MaritalStatusDelegate maritalStatusDelegate,
			final RaceDelegate raceDelegate,
			final TribeDelegate tribeDelegate,
			final SuffixDelegate suffixDelegate,
			final ReligionDelegate religionDelegate, 
			final OffenderFlagCategoryDelegate offenderFlagCategoryDelegate) {
		this.offenderDelegate = offenderDelegate;
		this.personDemographicsDelegate = personDemographicsDelegate;
		this.religiousPreferenceDelegate = religiousPreferenceDelegate;
		this.citizenshipDelegate = citizenshipDelegate;
		this.alienResidenceDelegate = alienResidenceDelegate;
		this.offenderFlagDelegate = offenderFlagDelegate;
		this.offenderPhotoAssociationDelegate = offenderPhotoAssociationDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.buildDelegate = buildDelegate;
		this.complexionDelegate = complexionDelegate;
		this.eyeColorDelegate = eyeColorDelegate;
		this.hairColorDelegate = hairColorDelegate;
		this.maritalStatusDelegate = maritalStatusDelegate;
		this.raceDelegate = raceDelegate;
		this.tribeDelegate = tribeDelegate;
		this.suffixDelegate = suffixDelegate;
		this.religionDelegate = religionDelegate;
		this.offenderFlagCategoryDelegate = offenderFlagCategoryDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public Offender create(final String lastName, final String firstName,
			final String middleName, final String suffix, 
			final Integer socialSecurityNumber, final String stateIdNumber, 
			final Date birthDate, final Country birthCountry, 
			final City birthPlace, final Sex sex) 
		throws DuplicateEntityFoundException {
		State birthState;
		if (birthPlace != null) {
			birthState = birthPlace.getState();
		} else {
			birthState = null;
		}
		return this.offenderDelegate.create(lastName, firstName, middleName,
				suffix, socialSecurityNumber, stateIdNumber, birthDate,
				birthCountry, birthState, birthPlace, sex);
	}
	
	/** {@inheritDoc} */
	@Override
	public Offender convertPerson(final Person person, final String lastName,
			final String firstName, final String middleName,
			final String suffix, final Integer socialSecurityNumber,
			final String stateIdNumber, final  Date birthDate,
			final Country birthCountry, final City birthPlace,
			final Sex sex) throws OffenderExistsException {
		State birthState;
		if (birthPlace != null) {
			birthState = birthPlace.getState();
		} else {
			birthState = null;
		}
		Offender offender = this.offenderDelegate.convertPerson(person); 
		offender = this.offenderDelegate.updateName(
				offender, lastName, firstName, middleName, suffix);
		offender = this.offenderDelegate.updateIdentity(offender,
				socialSecurityNumber, stateIdNumber, birthDate,
				birthCountry, birthState, birthPlace, sex, null, null);
		return offender;
	}

	/** {@inheritDoc} */
	@Override
	public PersonDemographics addDemographics(final Offender offender,
			final PersonPhysique physique, final PersonAppearance appearance,
			final DominantSide dominantSide, final Race race,
			final Boolean hispanicEthnicity, final Tribe tribe,
			final MaritalStatus maritalStatus) 
		throws DuplicateEntityFoundException {
		
		return this.personDemographicsDelegate.create(offender, appearance,
				race, hispanicEthnicity, physique, dominantSide, maritalStatus,
				tribe);
	}

	/** {@inheritDoc} 
	 * @throws OperationNotAuthorizedException 
	 * @throws DateConflictException 
	 * @throws DuplicateEntityFoundException */
	@Override
	public ReligiousPreference addReligiousPreference(final Offender offender,
			final Religion religion) throws DuplicateEntityFoundException,
				DateConflictException, OperationNotAuthorizedException {
		return this.religiousPreferenceDelegate
				.save(offender, religion, null, null, null);
	}

	/** {@inheritDoc} */
	@Override
	public Citizenship setCountryOfCitizenship(final Offender offender,
			final Country country){
		return this.citizenshipDelegate.create(offender, country, null);
	}

	/** {@inheritDoc} */
	@Override
	public AlienResidence setLegalResidenceStatus(final Offender offender,
			final Boolean legal) {
		return this.alienResidenceDelegate
				.create(offender, null, null, legal, null);
	}

	/** {@inheritDoc} 
	 * @throws DuplicateEntityFoundException */
	@Override
	public OffenderFlag setFlag(final Offender offender,
			final OffenderFlagCategory category, final Boolean value)
					throws DuplicateEntityFoundException {
		return this.offenderFlagDelegate.create(offender, category, value);
	}

	/** {@inheritDoc} 
	 * @throws DuplicateEntityFoundException */
	@Override
	public OffenderPhotoAssociation associateProfilePhoto(
			final Offender offender, final String filename, 
			final Date photoDate) throws DuplicateEntityFoundException {
		Photo photo = this.offenderPhotoAssociationDelegate
				.createPhoto(offender, filename, photoDate);
		return this.offenderPhotoAssociationDelegate
				.create(offender, photo, true);
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(
			final String name, final State state, final Country country)
				throws DuplicateEntityFoundException {
		return this.cityDelegate.create(name, true, state, country);
	}

	/** {@inheritDoc} */
	@Override
	public List<Build> findBuilds() {
		return this.buildDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Complexion> findComplexions() {
		return this.complexionDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<EyeColor> findEyeColors() {
		return this.eyeColorDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<HairColor> findHairColors() {
		return this.hairColorDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<MaritalStatus> findMaritalStatuses() {
		return maritalStatusDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Race> findRaces() {
		return this.raceDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Tribe> findTribes() {
		return this.tribeDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Suffix> findSuffixes() {
		return this.suffixDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Religion> findReligions() {
		return this.religionDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<OffenderFlagCategory> findRequiredCategories() {
		return this.offenderFlagCategoryDelegate.findRequired();
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasRequiredFlags(final Offender offender) {
		List<OffenderFlagCategory> categories
			= this.offenderFlagCategoryDelegate.findRequired();
		return this.offenderFlagDelegate.countMissingFlags(offender,
				categories.toArray(new OffenderFlagCategory[] { })) == 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isHomeCountry(final Country country) {
		State homeState = this.stateDelegate.findHomeState();
		if (homeState != null) {
			return country.equals(homeState.getCountry());
		} else {
			return false;
		}
	}
}