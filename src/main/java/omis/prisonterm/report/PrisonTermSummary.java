package omis.prisonterm.report;

import java.util.Date;

import omis.prisonterm.domain.PrisonTermStatus;

/**
 * Summary for prison term.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Oct 17, 2017)
 * @since OMIS 3.0
 */

public class PrisonTermSummary {
	private final Long id;
	
	private final Long offenderId;
	
	private final String offenderLastName;
	
	private final String offenderFirstName;
	
	private final String offenderMiddleName;
	
	private final String offenderSuffix;
	
	private final Integer offenderNumber;
	
	private final Date actionDate;
	
	private final Integer preSentenceCredits;
	
	private final Date sentenceDate;
	
	private final Integer sentenceTermYears;
	
	private final Integer sentenceTermDays;
	
	private final Date paroleEligibilityDate;
	
	private final Date projectedDischargeDate;
	
	private final Date maximumDischargeDate;
	
	private final Boolean sentenceToFollow;
	
	private final String comments;
	
	private final PrisonTermStatus status;
	
	/**
	 * Instantiates a prison term summary with the specified values.
	 * 
	 * @param id
	 * @param offenderId
	 * @param offenderLastName
	 * @param offenderFirstName
	 * @param offenderMiddleName
	 * @param offenderSuffix
	 * @param offenderNumber
	 * @param actionDate
	 * @param preSentenceCredits
	 * @param sentenceDate
	 * @param sentenceTermYears
	 * @param sentenceTermDays
	 * @param paroleEligibilityDate
	 * @param projectedDischargeDate
	 * @param maximumDischargeDate
	 * @param sentenceToFollow
	 * @param comments
	 * @param prisonTermStatus
	 */
	public PrisonTermSummary(
			final Long id,
			final Long offenderId,
			final String offenderLastName,
			final String offenderFirstName,
			final String offenderMiddleName,
			final String offenderSuffix,
			final Integer offenderNumber,
			final Date actionDate,
			final Integer preSentenceCredits,
			final Date sentenceDate,
			final Integer sentenceTermYears,
			final Integer sentenceTermDays,
			final Date paroleEligibilityDate,
			final Date projectedDischargeDate,
			final Date maximumDischargeDate,
			final Boolean sentenceToFollow,
			final String comments,
			final PrisonTermStatus prisonTermStatus) {
		this.id = id;
		this.offenderId = offenderId;
		this.offenderLastName = offenderLastName;
		this.offenderFirstName = offenderFirstName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderSuffix = offenderSuffix;
		this.offenderNumber = offenderNumber;
		this.actionDate = actionDate;
		this.preSentenceCredits = preSentenceCredits;
		this.sentenceDate = sentenceDate;
		this.sentenceTermYears = sentenceTermYears;
		this.sentenceTermDays = sentenceTermDays;
		this.paroleEligibilityDate = paroleEligibilityDate;
		this.projectedDischargeDate = projectedDischargeDate;
		this.maximumDischargeDate = maximumDischargeDate;
		this.sentenceToFollow = sentenceToFollow;
		this.comments = comments;
		this.status = prisonTermStatus;
	}
	
	/**
	 * @param paroleEligibilityDate
	 * @param projectedDischargeDate
	 */
	public PrisonTermSummary(
			final Date paroleEligibilityDate,
			final Date projectedDischargeDate,
			final Boolean sentenceToFollow) {
		this.id = null;
		this.offenderId = null;
		this.offenderLastName = null;
		this.offenderFirstName = null;
		this.offenderMiddleName = null;
		this.offenderSuffix = null;
		this.offenderNumber = null;
		this.actionDate = null;
		this.preSentenceCredits = null;
		this.sentenceDate = null;
		this.sentenceTermYears = null;
		this.sentenceTermDays = null;
		this.paroleEligibilityDate = paroleEligibilityDate;
		this.projectedDischargeDate = projectedDischargeDate;
		this.maximumDischargeDate = null;
		this.sentenceToFollow = sentenceToFollow;
		this.comments = null;
		this.status = null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the offenderId
	 */
	public Long getOffenderId() {
		return offenderId;
	}

	/**
	 * @return the offenderLastName
	 */
	public String getOffenderLastName() {
		return offenderLastName;
	}

	/**
	 * @return the offenderFirstName
	 */
	public String getOffenderFirstName() {
		return offenderFirstName;
	}

	/**
	 * @return the offenderMiddleName
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * @return the offenderSuffix
	 */
	public String getOffenderSuffix() {
		return offenderSuffix;
	}

	/**
	 * @return the offenderNumber
	 */
	public Integer getOffenderNumber() {
		return offenderNumber;
	}

	/**
	 * @return the actionDate
	 */
	public Date getActionDate() {
		return actionDate;
	}

	/**
	 * @return the preSentenceCredits
	 */
	public Integer getPreSentenceCredits() {
		return preSentenceCredits;
	}

	/**
	 * @return the sentenceDate
	 */
	public Date getSentenceDate() {
		return sentenceDate;
	}

	/**
	 * @return the sentenceTermYears
	 */
	public Integer getSentenceTermYears() {
		return sentenceTermYears;
	}

	/**
	 * @return the sentenceTermDays
	 */
	public Integer getSentenceTermDays() {
		return sentenceTermDays;
	}

	/**
	 * @return the paroleEligibilityDate
	 */
	public Date getParoleEligibilityDate() {
		return paroleEligibilityDate;
	}

	/**
	 * @return the projectedDischargeDate
	 */
	public Date getProjectedDischargeDate() {
		return projectedDischargeDate;
	}

	/**
	 * @return the maximumDischargeDate
	 */
	public Date getMaximumDischargeDate() {
		return maximumDischargeDate;
	}

	/**
	 * @return the sentenceToFollow
	 */
	public Boolean getSentenceToFollow() {
		return sentenceToFollow;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return the prisonTermStatus
	 */
	public PrisonTermStatus getStatus() {
		return status;
	}
}
