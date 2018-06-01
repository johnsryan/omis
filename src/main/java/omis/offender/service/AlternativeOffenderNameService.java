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
package omis.offender.service;

import java.util.List;

import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.PersonName;
import omis.person.domain.Suffix;
import omis.person.exception.AlternativeNameAssociationExistsException;
import omis.person.exception.PersonNameExistsException;

/**
 * Service for alternative offender names.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Dec 4, 2013)
 * @since OMIS 3.0
 */
public interface AlternativeOffenderNameService {

	/**
	 * Creates a new alternative name association between the specified
	 * offender and a person name with the specified name properties.
	 * 
	 * @param offender offender
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param dateRange date range
	 * @param category category
	 * @return new alternative name association
	 * @throws PersonNameExistsException if person name exists
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 */
	AlternativeNameAssociation associate(Offender offender, String lastName, 
			String firstName, String middleName, String suffix, 
			DateRange dateRange, AlternativeNameCategory category) 
					throws PersonNameExistsException,
						AlternativeNameAssociationExistsException;
	
	/**
	 * Updates the specified alternative name association with the specified
	 * properties.
	 * 
	 * @param association alternative name association
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param dateRange date range
	 * @param category category
	 * @return new alternative name association
	 * @throws PersonNameExistsException if person name exists
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 */
	AlternativeNameAssociation updateAssociation(
			AlternativeNameAssociation association, String lastName,
			String firstName, String middleName, String suffix, 
			DateRange dateRange, AlternativeNameCategory category)
					throws PersonNameExistsException,
						AlternativeNameAssociationExistsException;
	
	/**
	 * Removes an alternative offender name.
	 * 
	 * @param alternativeNameAssociation association of alternative offender
	 * name to remove
	 */
	void remove(AlternativeNameAssociation alternativeNameAssociation);
	
	/**
	 * Returns alternative names for offender.
	 * 
	 * <p>Alternative offender names are names not equal to
	 * {@code offender.getName()}.
	 * 
	 * @param offender offender
	 * @return alternative names for offender
	 */
	List<PersonName> findAlternativeNames(Offender offender);
	
	/**
	 * Returns alternative name associations for offender.
	 * 
	 * @param offender offender
	 * @return alternative name associations for offender
	 */
	List<AlternativeNameAssociation> findAssociations(Offender offender);
	
	/**
	 * Returns alternative name categories.
	 * 
	 * @return alternative name categories
	 */
	List<AlternativeNameCategory> findCategories();
	
	/**
	 * Returns suffixes.
	 * 
	 * @return suffixes
	 */
	List<Suffix> findSuffixes();
}