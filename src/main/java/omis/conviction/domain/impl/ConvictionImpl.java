package omis.conviction.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.conviction.domain.Conviction;
import omis.conviction.domain.OffenseSeverity;
import omis.conviction.domain.component.ConvictionFlags;
import omis.courtcase.domain.CourtCase;
import omis.offense.domain.Offense;

/**
 * Implementation of conviction.
 * 
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.3 (May 2, 2017)
 * @since OMIS 3.0
 */
public class ConvictionImpl
		implements Conviction {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private UpdateSignature updateSignature;

	private CreationSignature creationSignature;

	private Offense offense;

	private Long date;

	private Integer counts;
	
	private CourtCase courtCase;
	
	private OffenseSeverity severity;
	
	private ConvictionFlags flags;
	
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
	public void setOffense(final Offense offense) {
		this.offense = offense;
	}

	/** {@inheritDoc} */
	@Override
	public Offense getOffense() {
		return this.offense;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		if (date != null) {
			this.date = date.getTime();
		} else {
			this.date = null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		if (this.date != null) {
			return new Date(this.date);
		} else {
			return null;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCounts(final Integer counts) {
		this.counts = counts;
	}

	/** {@inheritDoc} */
	@Override
	public Integer getCounts() {
		return this.counts;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCourtCase(final CourtCase courtCase) {
		this.courtCase = courtCase;
	}
	
	/** {@inheritDoc} */
	@Override
	public CourtCase getCourtCase() {
		return this.courtCase;
	}

	/** {@inheritDoc} */
	@Override
	public void setFlags(final ConvictionFlags flags) {
		this.flags = flags;
	}
	
	/** {@inheritDoc} */
	@Override
	public ConvictionFlags getFlags() {
		return this.flags;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setSeverity(final OffenseSeverity severity) {
		this.severity = severity;
	}
	
	/** {@inheritDoc} */
	@Override
	public OffenseSeverity getSeverity() {
		return this.severity;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Conviction)) {
			return false;
		}
		Conviction that = (Conviction) obj;
		if (this.getCourtCase() == null) {
			throw new IllegalStateException("Court case required");
		}
		if (!this.getCourtCase().equals(that.getCourtCase())) {
			return false;
		}
		if (this.getOffense() == null) {
			throw new IllegalStateException("Offense required");
		}
		if (!this.getOffense().equals(that.getOffense())) {
			return false;
		}
		if (this.getCounts() == null) {
			throw new IllegalStateException("Count required");
		}
		if (!this.getCounts().equals(that.getCounts())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getCourtCase() == null) {
			throw new IllegalStateException("Court case required");
		}
		if (this.getOffense() == null) {
			throw new IllegalStateException("Offense required");
		}
		if (this.getCounts() == null) {
			throw new IllegalStateException("Count required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getCourtCase().hashCode();
		hashCode = 29 * hashCode + this.getOffense().hashCode();
		hashCode = 29 * hashCode + this.getCounts().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including offense name
	 * and counts.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		final String offenseName;
		if (this.getOffense() != null) {
			offenseName = this.getOffense().getName();
		} else {
			offenseName = null;
		}
		return String.format("#%d - %s %d", this.getId(), offenseName,
				this.getCounts());
	}
}