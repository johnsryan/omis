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
package omis.boardhearing.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.CancellationCategory;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Board Hearing Implementation.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingImpl implements BoardHearing {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private ParoleBoardItinerary itinerary;

	private Date hearingDate;
	
	private ParoleEligibility paroleEligibility;
	
	private BoardHearingCategory category;
	
	private CancellationCategory cancellation;
	
	private Boolean videoConference;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**{@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/**{@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/**{@inheritDoc} */
	@Override
	public ParoleBoardItinerary getItinerary() {
		return this.itinerary;
	}

	/**{@inheritDoc} */
	@Override
	public void setItinerary(final ParoleBoardItinerary itinerary) {
		this.itinerary = itinerary;
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingCategory getCategory() {
		return this.category;
	}

	/**{@inheritDoc} */
	@Override
	public void setCategory(final BoardHearingCategory category) {
		this.category = category;
	}
	
	/**{@inheritDoc} */
	@Override
	public Date getHearingDate() {
		return this.hearingDate;
	}

	/**{@inheritDoc} */
	@Override
	public void setHearingDate(final Date hearingDate) {
		this.hearingDate = hearingDate;
	}

	/**{@inheritDoc} */
	@Override
	public ParoleEligibility getParoleEligibility() {
		return this.paroleEligibility;
	}

	/**{@inheritDoc} */
	@Override
	public void setParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		this.paroleEligibility = paroleEligibility;
	}
	
	/**{@inheritDoc} */
	@Override
	public CancellationCategory getCancellation() {
		return this.cancellation;
	}

	/**{@inheritDoc} */
	@Override
	public void setCancellation(final CancellationCategory cancellation) {
		this.cancellation = cancellation;
	}
	
	/**{@inheritDoc} */
	@Override
	public Boolean getVideoConference() {
		return this.videoConference;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setVideoConference(final Boolean videoConference) {
		this.videoConference = videoConference;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BoardHearing)) {
			return false;
		}
		
		BoardHearing that = (BoardHearing) obj;
		
		if (this.getParoleEligibility() == null) {
			throw new IllegalStateException("ParoleEligibility required.");
		}
		if (this.getItinerary() == null) {
			throw new IllegalStateException("Itinerary required.");
		}
		if (this.getVideoConference() == null) {
			throw new IllegalStateException("Video Conference required.");
		}
		
		if (!this.getParoleEligibility().equals(that.getParoleEligibility())) {
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getParoleEligibility() == null) {
			throw new IllegalStateException("ParoleEligibility required.");
		}
		if (this.getItinerary() == null) {
			throw new IllegalStateException("Itinerary required.");
		}
		if (this.getVideoConference() == null) {
			throw new IllegalStateException("Video Conference required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getParoleEligibility().hashCode();
		hashCode = 29 * hashCode + this.getItinerary().hashCode();
		
		return hashCode;
	}
}