/**
 * 
 */
package omis.separationneed.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.separationneed.domain.SeparationNeedReason;
import omis.separationneed.domain.SeparationNeedRemovalReason;

/**
 * Form for separation need.
 * 
 * @author Joel Norris 
 * @version 0.1.0 (Feb, 21 2013)
 * @since OMIS 3.0
 */
public class SeparationNeedForm implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Offender targetOffender;
	
	private Date date;
	
	private String creationComment;
	
	private Person reportingStaff;
	
	private Date removalDate;
	
	private SeparationNeedRemovalReason removalReason;
	
	private String removalComment;
	
	private List<SeparationNeedReason> separationNeedReasons =
			new ArrayList<SeparationNeedReason>();
	
	private List<SeparationNeedNoteItem> separationNeedNoteItems =
			new ArrayList<SeparationNeedNoteItem>();
	
	private Boolean createTargetOffender;

	/** Instantiates a default instance of separation need form. */
	public SeparationNeedForm() {
		//empty constructor
	}
	
	/**
	 * Returns the target offender.
	 * 
	 * @return target offender
	 */
	public Offender getTargetOffender() {
		return this.targetOffender;
	}

	/**
	 * Sets the target offender number.
	 * 
	 * @param targetOffender the target offender to set
	 */
	public void setTargetOffender(final Offender targetOffender) {
		this.targetOffender = targetOffender;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the creation comment.
	 * 
	 * @return creation comment
	 */
	public String getCreationComment() {
		return this.creationComment;
	}

	/**
	 * Sets the creation comment.
	 * 
	 * @param creationComment creation comment
	 */
	public void setCreationComment(final String creationComment) {
		this.creationComment = creationComment;
	}

	/**
	 * Returns the reporting staff.
	 * 
	 * @return reporting staff
	 */
	public Person getReportingStaff() {
		return this.reportingStaff;
	}

	/**
	 * Sets the reporting staff.
	 * 
	 * @param reportingStaff reporting staff
	 */
	public void setReportingStaff(final Person reportingStaff) {
		this.reportingStaff = reportingStaff;
	}

	/**
	 * Returns the removal date.
	 * 
	 * @return removal date
	 */
	public Date getRemovalDate() {
		return this.removalDate;
	}

	/**
	 * Sets the removal date.
	 * 
	 * @param removalDate removal date
	 */
	public void setRemovalDate(final Date removalDate) {
		this.removalDate = removalDate;
	}

	/**
	 * Returns the removal reason.
	 * 
	 * @return removal reason
	 */
	public SeparationNeedRemovalReason getRemovalReason() {
		return removalReason;
	}

	/**
	 * Sets the removal reason.
	 * 
	 * @param removalReason removal reason
	 */
	public void setRemovalReason(SeparationNeedRemovalReason removalReason) {
		this.removalReason = removalReason;
	}

	/**
	 * Returns the removal comment.
	 * 
	 * @return removal comment
	 */
	public String getRemovalComment() {
		return this.removalComment;
	}

	/**
	 * Sets the removal comment.
	 * 
	 * @param removalComment removal comment
	 */
	public void setRemovalComment(String removalComment) {
		this.removalComment = removalComment;
	}

	/**
	 * Returns separation need reasons.
	 * 
	 * @return separation need reasons
	 */
	public List<SeparationNeedReason> getSeparationNeedReasons() {
		return this.separationNeedReasons;
	}

	/**
	 * Sets the separation need reasons.
	 * 
	 * @param separationNeedReasonItems separation need reasons
	 */
	public void setSeparationNeedReasons(
			final List<SeparationNeedReason> separationNeedReasons) {
		this.separationNeedReasons = separationNeedReasons;
	}

	/**
	 * Returns the separation need note items.
	 * 
	 * @return separation need note items
	 */
	public List<SeparationNeedNoteItem> getSeparationNeedNoteItems() {
		return this.separationNeedNoteItems;
	}

	/**
	 * Sets the separation need note items.
	 * 
	 * @param separationNeedNoteItems separation need note items
	 */
	public void setSeparationNeedNoteItems(
			final List<SeparationNeedNoteItem> separationNeedNoteItems) {
		this.separationNeedNoteItems = separationNeedNoteItems;
	}

	/**
	 * Returns whether creating or editing target offender.
	 *
	 * @return the createTargetOffender
	 */
	public Boolean getCreateTargetOffender() {
		return this.createTargetOffender;
	}

	/**
	 * Sets whether creating or editing target offender.
	 *
	 * @param createTargetOffender create target offender
	 */
	public void setCreateTargetOffender(final Boolean createTargetOffender) {
		this.createTargetOffender = createTargetOffender;
	}
}