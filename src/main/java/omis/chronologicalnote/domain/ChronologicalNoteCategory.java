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
package omis.chronologicalnote.domain;

import java.io.Serializable;

/**
 * Chronological note category.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.0(January 29, 2018)
 * @since OMIS 3.0
 */
public interface ChronologicalNoteCategory extends Serializable {

	/**
	 * Returns id.
	 * 
	 * @return id
	 */
	Long getId();
	
	/**
	 * Sets id.
	 * 
	 * @param id id
	 */
	void setId(Long id);
	
	/**
	 * Returns name.
	 * 
	 * @return name
	 */
	String getName();
	
	/**
	 * Sets name.
	 * 
	 * @param name name
	 */
	void setName(String name);
	
	/**
	 * Returns group.
	 * 
	 * @return group
	 */
	ChronologicalNoteCategoryGroup getGroup();
	
	/**
	 * Sets group.
	 * 
	 * @param group group
	 */
	void setGroup(ChronologicalNoteCategoryGroup group);
	
	/**
	 * Returns short name.
	 * 
	 * @return short name
	 */
	String getShortName();
	
	/**
	 * Sets short name.
	 * 
	 * @param shortName short name
	 */
	void setShortName(String shortName);
	
	/**
	 * Returns whether valid applies.
	 * 
	 * @return valid
	 */
	Boolean getValid();
	
	/**
	 * Sets whether valid applies.
	 * 
	 * @param valid valid
	 */
	void setValid(Boolean valid);
	
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