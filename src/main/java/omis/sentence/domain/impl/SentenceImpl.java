package omis.sentence.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.conviction.domain.Conviction;
import omis.conviction.domain.component.ConvictionCredit;
import omis.sentence.domain.LegalDispositionCategory;
import omis.sentence.domain.Sentence;
import omis.sentence.domain.SentenceCategory;
import omis.sentence.domain.SentenceLengthClassification;
import omis.sentence.domain.component.SentenceConnection;
import omis.term.domain.component.Term;

/**
 * Implementation of sentence.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.1 (May 9, 2017)
 * @since OMIS 3.0
 */
public class SentenceImpl
		implements Sentence {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Conviction conviction;
	
	private SentenceCategory category;

	private CreationSignature creationSignature;

	private UpdateSignature updateSignature;

	private Term prisonTerm;
	
	private Term probationTerm;

	private Term deferredTerm;
	
	private ConvictionCredit credit;
	
	private Date turnSelfInDate;
	
	private Date effectiveDate;
	
	private Date pronouncementDate;
	
	private SentenceLengthClassification lengthClassification;
	
	private LegalDispositionCategory legalDispositionCategory;
	
	private Boolean active;
	
	private SentenceConnection connection;
	
	private Integer changeOrder;
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setConviction(Conviction conviction) {
		this.conviction = conviction;
	}
	
	/** {@inheritDoc} */
	@Override
	public Conviction getConviction() {
		return this.conviction;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCategory(SentenceCategory category) {
		this.category = category;
	}
	
	/** {@inheritDoc} */
	@Override
	public SentenceCategory getCategory() {
		return this.category;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(
			final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setPrisonTerm(final Term prisonTerm) {
		this.prisonTerm = prisonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public Term getPrisonTerm() {
		return this.prisonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setProbationTerm(final Term probationTerm) {
		this.probationTerm = probationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public Term getProbationTerm() {
		return this.probationTerm;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDeferredTerm(Term deferredTerm) {
		this.deferredTerm = deferredTerm;
	}

	/** {@inheritDoc} */
	@Override
	public Term getDeferredTerm() {
		return this.deferredTerm;
	}

	/** {@inheritDoc} */
	@Override
	public void setCredit(ConvictionCredit convictionCredit) {
		this.credit = convictionCredit;
	}

	/** {@inheritDoc} */
	@Override
	public ConvictionCredit getCredit() {
		return this.credit;
	}

	/** {@inheritDoc} */
	@Override
	public void setTurnSelfInDate(Date turnSefInDate) {
		this.turnSelfInDate = turnSefInDate;
	}

	/** {@inheritDoc} */
	@Override
	public Date getTurnSelfInDate() {
		return this.turnSelfInDate;
	}

	/** {@inheritDoc} */
	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/** {@inheritDoc} */
	@Override
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	/** {@inheritDoc} */
	@Override
	public void setPronouncementDate(Date pronouncementDate) {
		this.pronouncementDate = pronouncementDate;
	}

	/** {@inheritDoc} */
	@Override
	public Date getPronouncementDate() {
		return this.pronouncementDate;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setLengthClassification(
			SentenceLengthClassification lengthClassification) {
		this.lengthClassification = lengthClassification;
	}
	
	/** {@inheritDoc} */
	@Override
	public SentenceLengthClassification getLengthClassification() {
		return this.lengthClassification;
	}
	
	/** {@inheritDoc} */
	@Override
	public LegalDispositionCategory getLegalDispositionCategory() {
		return legalDispositionCategory;
	}

	/** {@inheritDoc} */
	@Override
	public void setLegalDispositionCategory(
			LegalDispositionCategory legalDispositionCategory) {
		this.legalDispositionCategory = legalDispositionCategory;
	}
	

	/** {@inheritDoc} */
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getActive() {
		return this.active;
	}

	/** {@inheritDoc} */
	@Override
	public SentenceConnection getConnection() {
		return connection;
	}

	/** {@inheritDoc} */
	@Override
	public void setConnection(SentenceConnection connection) {
		this.connection = connection;
	}

	/** {@inheritDoc} */
	@Override
	public void setChangeOrder(Integer changeOrder) {
		this.changeOrder = changeOrder;
	}

	/** {@inheritDoc} */
	@Override
	public Integer getChangeOrder() {
		return changeOrder;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Sentence)) {
			return false;
		}
		Sentence that = (Sentence) obj;
		if (this.getConviction() == null) {
			throw new IllegalStateException("Conviction required");
		}
		if (!this.getConviction().equals(that.getConviction())) {
			return false;
		}
		if (this.getEffectiveDate() != null) {
			if (!this.getEffectiveDate().equals(that.getEffectiveDate())) {
				return false;
			}
		} else if (that.getEffectiveDate() != null) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getConviction() == null) {
			throw new IllegalStateException("Conviction required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getConviction().hashCode();
		if (this.getEffectiveDate() != null) {
			hashCode = 31 * hashCode + this.getEffectiveDate().hashCode();
		}
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including conviction
	 * offender name, counts and effective date.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		final String convictionOffenseName;
		final Integer convictionCounts;
		if (this.getConviction() != null) {
			if (conviction.getOffense() != null) {
				convictionOffenseName = this.getConviction().getOffense().getName();
			} else {
				convictionOffenseName = null;	
			}
			convictionCounts = this.getConviction().getCounts();
		} else {
			convictionOffenseName = null;
			convictionCounts = null;
		}
		return String.format("#%d [%s - %d] %s", this.getId(),
				convictionOffenseName, convictionCounts,
				this.getEffectiveDate());
	}
}