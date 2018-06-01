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
package omis.offender.domain.impl;

import omis.offender.domain.Offender;
import omis.person.domain.impl.PersonImpl;

/**
 * Implementation of an offender.
 * 
 * @author Stephen Abson
 * @author Jason Nelson
 * @version 0.1.10 (March 6, 2013)
 * @since OMIS 3.0
 */
public class OffenderImpl
		extends PersonImpl implements Offender  {
	
	private static final long serialVersionUID = 1L;
	
	private Integer offenderNumber;
	
	private String comments;
	
	/** Instantiates an offender implementation. */
	public OffenderImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setOffenderNumber(final Integer offenderNumber) {
		this.offenderNumber = offenderNumber;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/** {@inheritDoc} */
	@Override
	public void setComments(final String comments) {
		this.comments = comments;
	}

	/** {@inheritDoc} */
	@Override
	public String getComments() {
		return this.comments;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Offender)) {
			return false;
		}
		Offender that = (Offender) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (this.getIdentity() != null && that.getIdentity() != null) {
			if (this.getIdentity().getSocialSecurityNumber() != null) {
				if (!this.getIdentity().getSocialSecurityNumber().equals(
						that.getIdentity().getSocialSecurityNumber())) {
					return false;
				}
			} else if (that.getIdentity().getSocialSecurityNumber() != null) {
				return false;
			}
			if (this.getIdentity().getBirthDate() != null) {
				if (!this.getIdentity().getBirthDate().equals(
						that.getIdentity().getBirthDate())) {
					return false;
				}
			} else if (that.getIdentity().getBirthDate() != null) {
				return false;
			}
			if (this.getIdentity().getBirthCountry() != null) {
				if (!this.getIdentity().getBirthCountry().equals(
						that.getIdentity().getBirthCountry())) {
					return false;
				}
			} else if (that.getIdentity().getBirthCountry() != null) {
				return false;
			}
			if (this.getIdentity().getBirthPlace() != null) {
				if (!this.getIdentity().getBirthPlace().equals(
						that.getIdentity().getBirthPlace())) {
					return false;
				}
			} else if (that.getIdentity().getBirthPlace() != null) {
				return false;
			}
			if (this.getIdentity().getSex()!=null) {
				if (!this.getIdentity().getSex().equals(
						that.getIdentity().getSex())) {
					return false;
				}
			} else if (that.getIdentity().getSex()!=null) {
				return false;
			}
		} else if (!(that.getIdentity() == null 
				&& this.getIdentity() == null)) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (getName() == null) {
			throw new IllegalArgumentException("Name required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getName().hashCode();
		if (this.getIdentity() != null) {
			if (this.getIdentity().getSocialSecurityNumber() != null) {
				hashCode = 29 * hashCode + this.getIdentity()
						.getSocialSecurityNumber().hashCode();
			}
			if (this.getIdentity().getBirthDate() != null) {
				hashCode = 29 * hashCode + this.getIdentity().getBirthDate()
						.hashCode();
			}
			if (this.getIdentity().getBirthCountry() != null) {
				hashCode = 29 * hashCode + this.getIdentity().getBirthCountry()
						.hashCode();
			}
			if (this.getIdentity().getBirthPlace() != null) {
				hashCode = 29 * hashCode + this.getIdentity().getBirthPlace()
						.hashCode();
			}
			if (this.getIdentity().getSex() != null) {
				hashCode = 29 * hashCode + this.getIdentity().getSex()
						.hashCode();
			}
		}
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format("#%d - %s, %s", this.getOffenderNumber(),
				this.getName().getLastName(), this.getName().getFirstName());
	}
}