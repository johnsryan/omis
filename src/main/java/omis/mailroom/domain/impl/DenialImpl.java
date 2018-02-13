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
package omis.mailroom.domain.impl;

import java.util.Date;

import omis.mail.domain.Mail;
import omis.mailroom.domain.Denial;

/** Implementation of denial.
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0 */
public class DenialImpl implements Denial {
	private static final long serialVersionUID = 1L;
	private static final int[] HASHS = {3,5};
	private static final String NO_MAIL_MSG = "Mail required";
	private static final String NO_DATE_MSG = "Date required";
	private Long id;
	private Mail mail;
	private String comments;
	private Date date;
	private Boolean returned;
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}
	
	/** {@inhertiDoc} */
	@Override
	public Mail getMail() {
		return this.mail;
	}
	
	/** {@inheritDoc} */
	@Override 
	public String getComments() {
		return this.comments;
	}
	
	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return this.date; 
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean getReturned() {
		return this.returned;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setMail(final Mail mail) {
		this.mail = mail;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setComments(final String comments) {
		this.comments = comments;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public void setReturned(final Boolean returned) {
		this.returned = returned;
	}
	
	 /** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		this.checkState();
		
		if (this == obj) {
			result = true;
		} else {
			if (obj instanceof Denial) {
				Denial that = (Denial) obj;
				
				if (this.getDate().equals(that.getDate())
						&& this.getMail().equals(that.getMail())) {
					result = true;
				}
			}
		}
		return result;
	}
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	public int hashCode() {
		this.checkState();
		
		return this.getDate().hashCode() * HASHS[0]
				+ this.getMail().hashCode() * HASHS[1];
	}
	
	private void checkState() {
		if (this.getMail() == null) {
			throw new IllegalStateException(NO_MAIL_MSG);
		}
		if (this.getDate() == null) {
			throw new IllegalStateException(NO_DATE_MSG);
		}
	}
}
