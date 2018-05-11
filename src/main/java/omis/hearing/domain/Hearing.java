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
package omis.hearing.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.hearing.domain.component.Subject;
import omis.location.domain.Location;
import omis.user.domain.UserAccount;

/**
 * Hearing.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (May 3, 2018)
 * @since OMIS 3.0
 */
public interface Hearing extends Creatable, Updatable{
	
	/**
	 * Returns the location
	 * @return location
	 */
	public Location getLocation();
	
	/**
	 * Sets the location
	 * @param location
	 */
	public void setLocation(Location location);
	
	/**
	 * Returns the hearing date
	 * @return date
	 */
	public Date getDate();
	
	/**
	 * Sets the hearing date
	 * @param date
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the hearing officer.
	 * 
	 * @return user account
	 */
	public UserAccount getOfficer();
	
	
	/**
	 * Sets the hearing officer.
	 * 
	 * @param officer user account
	 */
	public void setOfficer(UserAccount officer);
	
	/**
	 * Returns the hearing Category
	 * @return category - HearingCategory
	 */
	public HearingCategory getCategory();
	
	/**
	 * Sets the hearing Category
	 * @param category - HearingCategory
	 */
	public void setCategory(HearingCategory category);
	
	/**
	 * Returns the hearing Subject
	 * @return Subject
	 */
	public Subject getSubject();
	
	/**
	 * Sets the hearing Subject
	 * @param subject
	 */
	public void setSubject(Subject subject);
	
	/** Gets id.
	 * @return id. */
	public Long getId();
	
	/** Sets id.
	 * @param id - id. */
	public void setId(Long id);
	
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
	public boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	public int hashCode();
}