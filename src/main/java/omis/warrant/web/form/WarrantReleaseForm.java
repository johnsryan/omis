package omis.warrant.web.form;

import java.util.Date;

import omis.person.domain.Person;

/**
 * Warrant release form.
 * 
 * @author Annie Jacques 
 * @author Joel Norris
 * @version 0.1.1 (May 21, 2018)
 * @since OMIS 3.0
 */
public class WarrantReleaseForm {
	
	private String addressee;
	
	private Date releaseDate;
	
	private String instructions;
	
	private Person clearedBy;
	
	private Date clearedByDate;
	
	/** Instantiates a default warrant release form. */
	public WarrantReleaseForm() {
		//Default constructor.
	}

	/**
	 * Returns the addressee
	 * @return addressee - String
	 */
	public String getAddressee() {
		return addressee;
	}

	/**
	 * Sets the addressee
	 * @param addressee - String
	 */
	public void setAddressee(final String addressee) {
		this.addressee = addressee;
	}

	/**
	 * Returns the releaseDate
	 * @return releaseDate - Date
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets the releaseDate
	 * @param releaseDate - Date
	 */
	public void setReleaseDate(final Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Returns the instructions
	 * @return instructions - String
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * Sets the instructions
	 * @param instructions - String
	 */
	public void setInstructions(final String instructions) {
		this.instructions = instructions;
	}

	/**
	 * Returns the clearedBy
	 * @return clearedBy - Person
	 */
	public Person getClearedBy() {
		return clearedBy;
	}

	/**
	 * Sets the clearedBy
	 * @param clearedBy - Person
	 */
	public void setClearedBy(final Person clearedBy) {
		this.clearedBy = clearedBy;
	}

	/**
	 * Returns the clearedByDate
	 * @return clearedByDate - Date
	 */
	public Date getClearedByDate() {
		return clearedByDate;
	}

	/**
	 * Sets the clearedByDate
	 * @param clearedByDate - Date
	 */
	public void setClearedByDate(final Date clearedByDate) {
		this.clearedByDate = clearedByDate;
	}
	
	
}
