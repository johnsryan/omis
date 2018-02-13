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
package omis.parolereview.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.offender.domain.OffenderAssociable;
import omis.staff.domain.StaffAssignment;

/**
 * Parole review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public interface ParoleReview 
	extends Creatable, Updatable, OffenderAssociable {

	/**
	 * Sets the ID of the parole review.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the parole review.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the staff assignment of the parole review.
	 * 
	 * @param staffAssignment staff assignment
	 */
	void setStaffAssignment(StaffAssignment staffAssignment);
	
	/**
	 * Return the staff assignment of the parole review.
	 * 
	 * @return staff assignment
	 */
	StaffAssignment getStaffAssignment();
	
	/**
	 * Sets the date of the parole review.
	 * 
	 * @param date date
	 */
	void setDate(Date date);
	
	/**
	 * Return the date of the parole review.
	 * 
	 * @return date
	 */
	Date getDate();
	
	/**
	 * Sets the text of the parole review.
	 * 
	 * @param text text
	 */
	void setText(String text);
	
	/**
	 * Return the text of the parole review.
	 * 
	 * @return text
	 */
	String getText();
	
	/**
	 * Sets the parole endorsement category of the parole review.
	 * 
	 * @param endorsement parole endorsement category
	 */
	void setEndorsement(ParoleEndorsementCategory endorsement);
	
	/**
	 * Returns the parole endorsement category of the parole review.
	 * 
	 * @return parole endorsement category of the parole review
	 */
	ParoleEndorsementCategory getEndorsement();
	
	/**
	 * Sets the staff role category of the parole review.
	 * 
	 * @param staffRole staff role category
	 */
	void setStaffRole(StaffRoleCategory staffRole);
	
	/**
	 * Returns the staff role category of the parole review.
	 * 
	 * @return staff role category of the parole review
	 */
	StaffRoleCategory getStaffRole();
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}