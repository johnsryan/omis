package omis.person.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Person fields.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jun 9, 2015)
 * @since OMIS 3.0
 */
public class PersonFields implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	
	private String lastName;
	
	private String middleName;
	
	private String suffix;
	
	private Sex sex;
	
	private Date birthDate;
	
	private Country birthCountry;
	
	private State birthState;
	
	private City birthCity;
	
	private String socialSecurityNumber;
	
	private String stateIdNumber;
	
	private Boolean deceased;
	
	private Date deathDate;
	
	private Boolean newCity;
	
	private String cityName;
	
	/**
	 * Instantiates a default instance of person fields. 
	 */
	public PersonFields() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an instance of person fields with the specified properties.
	 * 
	 * @param firstName first name
	 * @param lastName last name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param sex sex
	 * @param birthDate date of birth
	 * @param birthCountry birth country
	 * @param birthState birth state
	 * @param birthCity birth city
	 * @param socialSecurityNumber social security number
	 * @param stateId state id
	 * @param deceased deceased
	 * @param deathDate date of death
	 */
	public PersonFields(final String firstName, final String lastName,
		final String middleName, final String suffix, final Sex sex,
		final Date birthDate, final Country birthCountry,
		final State birthState, final City birthCity,
		final String socialSecurityNumber, final String stateId,
		final Boolean deceased, final Date deathDate,
		final Boolean newCity,final String cityName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.suffix = suffix;
		this.sex = sex;
		this.birthDate = birthDate;
		this.birthCountry = birthCountry;
		this.birthState = birthState;
		this.birthCity = birthCity;
		this.socialSecurityNumber = socialSecurityNumber;
		this.stateIdNumber = stateId;
		this.deceased = deceased;
		this.deathDate = deathDate;
		this.newCity = newCity;
		this.cityName = cityName;
	}

	/**
	 * Returns first name.
	 * 
	 * @return first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets first name.
	 * 
	 * @param firstName first name
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns last name.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets last name.
	 * 
	 * @param lastName last name
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns middle name.
	 * 
	 * @return middle name
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * Sets middle name.
	 * 
	 * @param middleName middle name
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Returns suffix.
	 * 
	 * @return suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Sets suffix.
	 * 
	 * @param suffix suffix
	 */
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Returns sex.
	 * 
	 * @return sex
	 */
	public Sex getSex() {
		return this.sex;
	}

	/**
	 * Sets sex.
	 * 
	 * @param sex sex
	 */
	public void setSex(final Sex sex) {
		this.sex = sex;
	}

	/**
	 * Returns date of birth.
	 * 
	 * @return date of birth
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Sets date of birth.
	 * 
	 * @param birthDate date of birth
	 */
	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Returns birth country.
	 * 
	 * @return birth country
	 */
	public Country getBirthCountry() {
		return this.birthCountry;
	}

	/**
	 * Sets birth country.
	 * 
	 * @param birthCountry birth country
	 */
	public void setBirthCountry(final Country birthCountry) {
		this.birthCountry = birthCountry;
	}

	/**
	 * Returns birth state.
	 * 
	 * @return birth state
	 */
	public State getBirthState() {
		return this.birthState;
	}

	/**
	 * Sets birth state.
	 * 
	 * @param birthState birth state
	 */
	public void setBirthState(State birthState) {
		this.birthState = birthState;
	}

	/**
	 * Returns birth city.
	 * 
	 * @return birth city
	 */
	public City getBirthCity() {
		return this.birthCity;
	}

	/**
	 * Sets birth city.
	 * 
	 * @param birthCity birth city
	 */
	public void setBirthCity(final City birthCity) {
		this.birthCity = birthCity;
	}

	/**
	 * Returns social security number.
	 * 
	 * @return social security number
	 */
	public String getSocialSecurityNumber() {
		return this.socialSecurityNumber;
	}

	/**
	 * Sets social security number.
	 * 
	 * @param socialSecurityNumber social security number
	 * @return social security number 
	 */
	public void setSocialSecurityNumber(final String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * Returns state id.
	 * 
	 * @return state id
	 */
	@Deprecated
	public String getStateId() {
		return this.stateIdNumber;
	}

	/**
	 * Sets state id.
	 * 
	 * @param stateId state id
	 */
	@Deprecated
	public void setStateId(final String stateId) {
		this.stateIdNumber = stateId;
	}

	/**
	 * Returns state id number.
	 * 
	 * @return state id number
	 */
	public String getStateIdNumber() {
		return this.stateIdNumber;
	}

	/**
	 * Sets state id number.
	 * 
	 * @param stateIdNumber state id number
	 */
	public void setStateIdNumber(final String stateIdNumber) {
		this.stateIdNumber = stateIdNumber;
	}

	/**
	 * Returns whether deceased applies.
	 * 
	 * @return deceased
	 */
	public Boolean getDeceased() {
		return this.deceased;
	}

	/**
	 * Sets whether deceased applies.
	 * 
	 * @param deceased applies
	 */
	public void setDeceased(final Boolean deceased) {
		this.deceased = deceased;
	}

	/**
	 * Returns date of death.
	 * 
	 * @return date of death
	 */
	public Date getDeathDate() {
		return this.deathDate;
	}

	/**
	 * Sets date of death.
	 * 
	 * @param deathDate date of death
	 */
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	/**
	 * Returns whether new city applies.
	 * 
	 * @return new city
	 */
	public Boolean getNewCity() {
		return this.newCity;
	}

	/**
	 * Sets whether new city applies.
	 * 
	 * @param newCity new city
	 */
	public void setNewCity(final Boolean newCity) {
		this.newCity = newCity;
	}

	/**
	 * Returns city name.
	 * 
	 * @return city name
	 */
	public String getCityName() {
		return this.cityName;
	}

	/**
	 * Sets city name.
	 * 
	 * @param cityName city name
	 */
	public void setCityName(final String cityName) {
		this.cityName = cityName;
	}
}