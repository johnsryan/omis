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
package omis.offender.web.form;

import java.io.Serializable;

import omis.country.domain.Country;
import omis.demographics.domain.Build;
import omis.demographics.domain.Complexion;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.Race;
import omis.demographics.domain.Tribe;

/**
 * Form for offender demographics.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 7, 2014)
 * @since OMIS 3.0
 */
public class OffenderDemographicsForm
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private EyeColor eyeColor;
	
	private HairColor hairColor;
	
	private Race race;
	
	private Boolean hispanicEthnicity;
	
	private Tribe tribe;
	
	private MaritalStatus maritalStatus;
	
	private Complexion complexion;
	
	private Integer heightFeet;
	
	private Integer heightInches;
	
	private Integer weightPounds;
	
	private Build build;
	
	private DominantSide dominantSide;
	
	private Country countryOfCitizenship;
	
	private AlienResidenceLegality alienResidenceLegality;
	
	/** Form for offender demographics. */
	public OffenderDemographicsForm() {
		// Default instantiation
	}

	/**
	 * Returns the eye color.
	 * 
	 * @return eye color
	 */
	public EyeColor getEyeColor() {
		return this.eyeColor;
	}

	/**
	 * Sets the eye color.
	 * 
	 * @param eyeColor eye color
	 */
	public void setEyeColor(final EyeColor eyeColor) {
		this.eyeColor = eyeColor;
	}

	/**
	 * Returns the hair color.
	 * 
	 * @return hair color
	 */
	public HairColor getHairColor() {
		return this.hairColor;
	}

	/**
	 * Sets the hair color.
	 * 
	 * @param hairColor hair color
	 */
	public void setHairColor(final HairColor hairColor) {
		this.hairColor = hairColor;
	}

	/**
	 * Returns the race.
	 * 
	 * @return race
	 */
	public Race getRace() {
		return this.race;
	}

	/**
	 * Sets the race.
	 * 
	 * @param race race
	 */
	public void setRace(final Race race) {
		this.race = race;
	}

	/**
	 * Returns whether offender is of Hispanic ethnicity.
	 * 
	 * @return whether offender is of Hispanic ethnicity;
	 */
	public Boolean getHispanicEthnicity() {
		return this.hispanicEthnicity;
	}
	
	/**
	 * Sets whether offender is of Hispanic ethnicity.
	 * 
	 * @param hispanicEthnicity whether offender is of hispanic ethnicity
	 */
	public void setHispanicEthnicity(final Boolean hispanicEthnicity) {
		this.hispanicEthnicity = hispanicEthnicity;
	}
	
	/**
	 * Returns the tribe.
	 * 
	 * @return tribe
	 */
	public Tribe getTribe() {
		return this.tribe;
	}

	/**
	 * Sets the tribe.
	 * 
	 * @param tribe tribe
	 */
	public void setTribe(final Tribe tribe) {
		this.tribe = tribe;
	}

	/**
	 * Returns the marital status.
	 * 
	 * @return marital status
	 */
	public MaritalStatus getMaritalStatus() {
		return this.maritalStatus;
	}

	/**
	 * Sets the marital status.
	 * 
	 * @param maritalStatus marital status
	 */
	public void setMaritalStatus(final MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Returns the complexion.
	 * 
	 * @return complexion
	 */
	public Complexion getComplexion() {
		return this.complexion;
	}

	/**
	 * Sets the complexion.
	 * 
	 * @param complexion complexion
	 */
	public void setComplexion(final Complexion complexion) {
		this.complexion = complexion;
	}
	
	/**
	 * Returns the height in feet.
	 * 
	 * @return height in feet
	 */
	public Integer getHeightFeet() {
		return this.heightFeet;
	}

	/**
	 * Sets the height in feet.
	 * 
	 * @param heightFeet height in feet
	 */
	public void setHeightFeet(final Integer heightFeet) {
		this.heightFeet = heightFeet;
	}

	/**
	 * Returns the height in inches.
	 * 
	 * @return height in inches
	 */
	public Integer getHeightInches() {
		return this.heightInches;
	}

	/**
	 * Sets the height in inches.
	 * 
	 * @param heightInches height in inches
	 */
	public void setHeightInches(final Integer heightInches) {
		this.heightInches = heightInches;
	}

	/**
	 * Returns the weight in pounds.
	 * 
	 * @return weight in pounds
	 */
	public Integer getWeightPounds() {
		return this.weightPounds;
	}

	/**
	 * Sets the weight in pounds.
	 * 
	 * @param weightPounds weight in pounds
	 */
	public void setWeightPounds(final Integer weightPounds) {
		this.weightPounds = weightPounds;
	}

	/**
	 * Returns the build.
	 * 
	 * @return build
	 */
	public Build getBuild() {
		return this.build;
	}

	/**
	 * Sets the build.
	 * 
	 * @param build build
	 */
	public void setBuild(final Build build) {
		this.build = build;
	}

	/**
	 * Sets the dominant side.
	 * 
	 * @return dominant side
	 */
	public DominantSide getDominantSide() {
		return this.dominantSide;
	}
	
	/**
	 * Returns the dominant side.
	 * 
	 * @param dominantSide dominant side
	 */
	public void setDominantSide(final DominantSide dominantSide) {
		this.dominantSide = dominantSide;
	}

	/**
	 * Returns the country of citizenship.
	 * 
	 * @return country of citizenship
	 */
	public Country getCountryOfCitizenship() {
		return this.countryOfCitizenship;
	}

	/**
	 * Sets country of citizenship.
	 * 
	 * @param countryOfCitizenship country of citizenship
	 */
	public void setCountryOfCitizenship(final Country countryOfCitizenship) {
		this.countryOfCitizenship = countryOfCitizenship;
	}

	/**
	 * Returns the legality of alien residence.
	 * 
	 * @return legality of alien residence
	 */
	public AlienResidenceLegality getAlienResidenceLegality() {
		return this.alienResidenceLegality;
	}

	/**
	 * Sets the legality of alien residence.
	 * 
	 * @param alienResidenceLegality legality of alien residence
	 */
	public void setAlienResidenceLegality(
			final AlienResidenceLegality alienResidenceLegality) {
		this.alienResidenceLegality = alienResidenceLegality;
	}
}