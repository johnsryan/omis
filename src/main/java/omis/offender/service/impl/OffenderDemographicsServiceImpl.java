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

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import omis.exception.DuplicateEntityFoundException;
import omis.immigration.domain.AlienResidence;
import omis.immigration.service.delegate.AlienResidenceDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderDemographicsService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.region.domain.State;
import omis.region.service.delegate.StateDelegate;
import omis.religion.domain.Religion;
import omis.religion.service.delegate.ReligionDelegate;

/**
 * Implementation of service for offender demographics.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @author Annie Jacques
 * @version 0.1.2 (Nov 10, 2016)
 * @since OMIS 3.0
 */
public class OffenderDemographicsServiceImpl
		implements OffenderDemographicsService {

	/* Delegates. */
	
	private final PersonDemographicsDelegate personDemographicsDelegate;
	
	private final EyeColorDelegate eyeColorDelegate;
		
	private final HairColorDelegate hairColorDelegate;
	
	private final ComplexionDelegate complexionDelegate;
	
	private final BuildDelegate buildDelegate;
	
	private final RaceDelegate raceDelegate;
	
	private final TribeDelegate tribeDelegate;
	
	private final MaritalStatusDelegate maritalStatusDelegate;
	
	private final ReligionDelegate religionDelegate;
	
	private final CitizenshipDelegate citizenshipDelegate;
	
	private final OffenderDelegate offenderDelegate;
	
	private final AlienResidenceDelegate alienResidenceDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final StateDelegate stateDelegate;
	
	/**
	 * Instantiates an implementation of service for offender demographics.
	 * 
	 * @param personDemographicsDelegate Delegate for person demographics
	 * @param eyeColorDelegate Delegate for eye color
	 * @param hairColorDelegate Delegate for hair color
	 * @param complexionDelegate Delegate for complexion
	 * @param buildDelegate Delegate for build
	 * @param raceDelegate Delegate for race
	 * @param tribeDelegate Delegate for tribe
	 * @param maritalStatusDelegate Delegate for marital statuses
	 * @param citizenshipDelegate Delegate for citizenship
	 * @param religionDelegate Delegate for religion
	 * @param offenderDelegate Delegate for offender
	 * @param alienResidenceDelegate Delegate for alien residence
	 * @param countryDelegate Delegate for country
	 * @param stateDelegate delegate for States
	 */
	public OffenderDemographicsServiceImpl(
			final PersonDemographicsDelegate personDemographicsDelegate,
			final EyeColorDelegate eyeColorDelegate,
			final HairColorDelegate hairColorDelegate,
			final ComplexionDelegate complexionDelegate,
			final BuildDelegate buildDelegate,
			final RaceDelegate raceDelegate, final TribeDelegate tribeDelegate,
			final MaritalStatusDelegate maritalStatusDelegate,
			final CitizenshipDelegate citizenshipDelegate,
			final ReligionDelegate religionDelegate,
			final OffenderDelegate offenderDelegate,
			final AlienResidenceDelegate alienResidenceDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate) {
		this.personDemographicsDelegate = personDemographicsDelegate;
		this.eyeColorDelegate = eyeColorDelegate;
		this.hairColorDelegate = hairColorDelegate;
		this.complexionDelegate = complexionDelegate;
		this.buildDelegate = buildDelegate;
		this.raceDelegate = raceDelegate;
		this.tribeDelegate = tribeDelegate;
		this.maritalStatusDelegate = maritalStatusDelegate;
		this.religionDelegate = religionDelegate;
		this.citizenshipDelegate = citizenshipDelegate;
		this.offenderDelegate = offenderDelegate;
		this.alienResidenceDelegate = alienResidenceDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
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
	public List<Complexion> findComplexions() {
		return this.complexionDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Build> findBuilds() {
		return this.buildDelegate.findAll();
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
	public List<MaritalStatus> findMaritalStatuses() {
		return this.maritalStatusDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Religion> findReligions() {
		return this.religionDelegate.findAll();
	}

	/** {@inheritDoc} 
	 * @throws DuplicateEntityFoundException */
	@Override
	public PersonDemographics updateDemographics(
			final Person person, final PersonPhysique physique,
			final PersonAppearance appearance, final DominantSide dominantSide,
			final Race race, final Boolean hispanicEthnicity,
			final Tribe tribe, final MaritalStatus maritalStatus){
		PersonDemographics personDemographics
			= this.personDemographicsDelegate.find(person);
		if (personDemographics == null) {
			return this.personDemographicsDelegate.create(person, appearance, 
					race, hispanicEthnicity, physique, dominantSide, 
					maritalStatus, tribe);
		}
		else{
			return this.personDemographicsDelegate.update(personDemographics,
					appearance, race, hispanicEthnicity, physique, dominantSide,
					maritalStatus, tribe);
		}
	}

	/** {@inheritDoc} */
	@Override
	public PersonDemographics findDemographics(final Offender offender) {
		if (this.offenderDelegate.findAsOffender(offender) == null) {
			throw new RuntimeException("None-offender specified");
		}
		return this.personDemographicsDelegate.find(offender);
	}

	/** {@inheritDoc} */
	@Override
	public Country findCountryOfCitizenship(final Offender offender) {
		Citizenship citizenship = this.citizenshipDelegate
				.findByOffender(offender);
		if (citizenship == null) {
			return null;
		} else {
			return citizenship.getCountry();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Boolean hasLegalResidenceStatus(final Offender offender) {
		if (this.alienResidenceDelegate.findByOffender(offender) != null) {
			return true;
		} else {
			return false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public AlienResidence changeLegalResidenceStatus(final Offender offender, 
			final Boolean legal) {
		AlienResidence residence = this.alienResidenceDelegate.findByOffender(
				offender);
		if (residence == null) {
			return this.alienResidenceDelegate
					.create(offender, null, null, legal, null);
		}
		else{
			return this.alienResidenceDelegate
					.update(residence, null, null, legal, null);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void removeAlienResidence(final Offender offender) {
		AlienResidence alienResidence = this.alienResidenceDelegate
				.findByOffender(offender);
		if (alienResidence != null) {
			this.alienResidenceDelegate.remove(alienResidence);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Citizenship changeCountryOfCitizenship(final Offender offender,
			final Country countryOfCitizenship) {
		Citizenship citizenship = this.citizenshipDelegate
				.findByOffender(offender);
		if (citizenship == null) {
			return this.citizenshipDelegate
					.create(offender, countryOfCitizenship, null);
		}
		else{
			return this.citizenshipDelegate
					.update(citizenship, countryOfCitizenship, null);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeCountryOfCitizienship(final Offender offender) {
		Citizenship citizenship = this.citizenshipDelegate
				.findByOffender(offender);
		if (citizenship != null) {
			this.citizenshipDelegate.remove(citizenship);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean findLegalResidenceStatus(Offender offender)
			throws EntityNotFoundException {
		AlienResidence alienResidence = this.alienResidenceDelegate
				.findByOffender(offender);
		if (alienResidence == null) {
			throw new EntityNotFoundException(
					"Alien residence not found");
		}
		return alienResidence.getLegal();
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