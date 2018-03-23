/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package omis.chronologicalnote.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.offender.domain.Offender;

/** Implementation of chronological note.
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0 */

public class ChronologicalNoteImpl implements ChronologicalNote {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Offender offender;
	private Date date;
	private String narrative;
	private String title;
	private UpdateSignature updateSignature;
	private CreationSignature creationSignature;
	
	/** Constructor. */
	public ChronologicalNoteImpl() {
	}
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public Offender getOffender() {
		return this.offender;
	}

	/** {@inheritDoc} */
	@Override
	public void setOffender(final Offender offender) {
		this.offender = offender;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getNarrative() {
		return this.narrative;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setNarrative(final String narrative) {
		this.narrative = narrative;
	}

	/** {@inheritDoc} */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setTitle(final String title) {
		this.title = title;
	}
	
	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return this.date;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
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
	
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}
	
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNote)) {
			return false;
		}
		ChronologicalNote that = (ChronologicalNote) obj;
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		if (!this.getOffender().equals(that.getOffender())) {
			return false;
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (!this.getDate().equals(that.getDate())) {
			return false;
		}
		if (this.getTitle() == null) {
			throw new IllegalStateException("Title required");
		}
		if (!this.getTitle().equals(that.getTitle())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int number = 7;
		int hashCode = number;
		
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required");
		}
		hashCode += 29 * this.getOffender().hashCode();
		
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		hashCode += 29 * this.getDate().hashCode();
		
		if (this.getTitle() == null) {
			throw new IllegalStateException("Title required");
		}
		hashCode += 29 * this.getTitle().hashCode();
		return hashCode;
	}
}