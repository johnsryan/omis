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
package omis.caseload.domain.impl;

import omis.caseload.domain.SupervisionLevelCategory;

/**
 * Implementation of supervision level category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public class SupervisionLevelCategoryImpl implements SupervisionLevelCategory {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String description;
	
	private Boolean valid;
	
	/**
	 * Instantiates an implementation of supervision level category. 
	 */
	public SupervisionLevelCategoryImpl() {
		// Default constructor.
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return description;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return valid;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SupervisionLevelCategory)) {
			return false;
		}
		SupervisionLevelCategory that = (SupervisionLevelCategory) obj;
		if (this.getDescription() == null) {
			throw new IllegalStateException("Description required.");
		}
		if (!this.getDescription().equals(that.getDescription())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getDescription() == null) {
			throw new IllegalStateException("Description required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDescription().hashCode();
		return hashCode;
	}
}