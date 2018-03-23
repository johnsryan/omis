package omis.presentenceinvestigation.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.court.domain.Court;
import omis.person.domain.Person;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.user.domain.UserAccount;

/** Form for presentence investigation requests.
 * @author Ryan Johns
 * @author Annie Jacques
 * @version 0.1.3 (Mar 22, 2017)
 * @since OMIS 3.0 */ 
public class PresentenceInvestigationRequestForm {
	
	private UserAccount assignedUserAccount;
	
	private Date requestDate;
	
	private Date sentenceDate;
	
	private Date expectedCompletionDate;
	
	private String docketValue;
	
	private Court court;
	
	private Boolean createPerson;
	
	private Person person;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private Date submissionDate;
	
	private PresentenceInvestigationCategory category;
	
	private List<PresentenceInvestigationRequestNoteItem>
		presentenceInvestigationRequestNoteItems =
			new ArrayList<PresentenceInvestigationRequestNoteItem>();
	
	/**
	 * 
	 */
	public PresentenceInvestigationRequestForm() {
	}

	/**
	 * Returns the assignedUser
	 * @return assignedUserAccount - UserAccount
	 */
	public UserAccount getAssignedUserAccount() {
		return assignedUserAccount;
	}

	/**
	 * Sets the assignedUser
	 * @param assignedUserAccount - UserAccount
	 */
	public void setAssignedUserAccount(final UserAccount assignedUserAccount) {
		this.assignedUserAccount = assignedUserAccount;
	}

	/**
	 * Returns the requestDate
	 * @return requestDate - Date
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	/**
	 * Sets the requestDate
	 * @param requestDate - Date
	 */
	public void setRequestDate(final Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * Returns the sentenceDate
	 * @return sentenceDate - Date
	 */
	public Date getSentenceDate() {
		return sentenceDate;
	}

	/**
	 * Sets the sentenceDate
	 * @param sentenceDate - Date
	 */
	public void setSentenceDate(final Date sentenceDate) {
		this.sentenceDate = sentenceDate;
	}

	/**
	 * Returns the expectedCompletionDate
	 * @return expectedCompletionDate - Date
	 */
	public Date getExpectedCompletionDate() {
		return expectedCompletionDate;
	}

	/**
	 * Sets the expectedCompletionDate
	 * @param expectedCompletionDate - Date
	 */
	public void setExpectedCompletionDate(final Date expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
	}

	/**
	 * Returns the docketValue
	 * @return docketValue - String
	 */
	public String getDocketValue() {
		return docketValue;
	}

	/**
	 * Sets the docketValue
	 * @param docketValue - String
	 */
	public void setDocketValue(final String docketValue) {
		this.docketValue = docketValue;
	}

	/**
	 * Returns the court
	 * @return court - Court
	 */
	public Court getCourt() {
		return court;
	}

	/**
	 * Sets the court
	 * @param court - Court
	 */
	public void setCourt(final Court court) {
		this.court = court;
	}

	/**
	 * Returns the createPerson
	 * @return createPerson - Boolean
	 */
	public Boolean getCreatePerson() {
		return createPerson;
	}

	/**
	 * Sets the createPerson
	 * @param createPerson - Boolean
	 */
	public void setCreatePerson(final Boolean createPerson) {
		this.createPerson = createPerson;
	}

	/**
	 * Returns the person
	 * @return person - Person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Sets the person
	 * @param person - Person
	 */
	public void setPerson(final Person person) {
		this.person = person;
	}

	/**
	 * Returns the lastName
	 * @return lastName - String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the lastName
	 * @param lastName - String
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the firstName
	 * @return firstName - String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName
	 * @param firstName - String
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the middleName
	 * @return middleName - String
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middleName
	 * @param middleName - String
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Returns the suffix
	 * @return suffix - String
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix
	 * @param suffix - String
	 */
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}
	
	/**
	 * Returns the category
	 * @return category - PresentenceInvestigationCategory
	 */
	public PresentenceInvestigationCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category
	 * @param category - PresentenceInvestigationCategory
	 */
	public void setCategory(final PresentenceInvestigationCategory category) {
		this.category = category;
	}
	
	/** Sets submission date.
	 * @param submissionDate - submission date.
	 */
	public void setSubmissionDate(final Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	
	/** Gets submission date.
	 * @return submission date. 
	 */
	public Date getSubmissionDate() {
		return this.submissionDate;
	}

	/**
	 * Returns the presentenceInvestigationRequestNoteItems
	 * @return presentenceInvestigationRequestNoteItems -
	 * List<PresentenceInvestigationRequestNoteItem>
	 */
	public List<PresentenceInvestigationRequestNoteItem>
		getPresentenceInvestigationRequestNoteItems() {
		return presentenceInvestigationRequestNoteItems;
	}

	/**
	 * Sets the presentenceInvestigationRequestNoteItems
	 * @param presentenceInvestigationRequestNoteItems -
	 * List<PresentenceInvestigationRequestNoteItem>
	 */
	public void setPresentenceInvestigationRequestNoteItems(
			final List<PresentenceInvestigationRequestNoteItem>
				presentenceInvestigationRequestNoteItems) {
		this.presentenceInvestigationRequestNoteItems =
				presentenceInvestigationRequestNoteItems;
	}
	
	

}
