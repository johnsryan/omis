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
package omis.criminalassociation.service;

import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.contact.domain.Contact;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.criminalassociation.domain.CriminalAssociation;
import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.criminalassociation.domain.component.CriminalAssociationFlags;
import omis.criminalassociation.exception.CriminalAssociationExistsException;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;

/**
 * Services for association.
 * 
 * @author Joel Norris 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Apr, 15 2015)
 * @since OMIS 3.0
 *
 */
public interface CriminalAssociationService {
	
	/**
	 * Checks for an existing relationship between two offenders
	 * if one does not exist, create new relationship 
	 * offender is always the "firstPerson" value of relationship. The
	 * relationship is then set to the specified association.
	 * 
	 * @param offender offender
	 * @param associate associate
	 * @param dateRange date range
	 * @param category criminal association category
	 * @param criminalAssociationFlags criminal association flags
	 * @return CriminalAssociation criminal association
	 * @throws CriminalAssociationExistsException criminal association 
	 * exists exception
	 * @throws ReflexiveRelationshipException reflexive relationship exception
	 */
	CriminalAssociation associate(Offender offender, Person associate, 
		DateRange dateRange, CriminalAssociationCategory category, 
		CriminalAssociationFlags criminalAssociationFlags) 
		throws CriminalAssociationExistsException, 
		ReflexiveRelationshipException;
	
	/**
	 * Updates an existing criminal association.
	 * 
	 * @param criminalAssociation criminal association
	 * @param  criminalAssociationCategory  criminal association category
	 * @param criminalAssociationFlags criminal association flags
	 * @param dateRange date range
	 * @return criminal association
	 */
	CriminalAssociation update(CriminalAssociation criminalAssociation, 
		DateRange dateRange,
		CriminalAssociationCategory criminalAssociationCategory, 
		CriminalAssociationFlags criminalAssociationFlags);
	
	/**
	 * Removes the specified criminal association.
	 * 
	 * @param criminalAssociation criminal association
	 */
	void remove(CriminalAssociation criminalAssociation);
	
	/**
	 * Find all criminal association categories.
	 * 
	 * @return list of criminal association categories 
	 */
	List<CriminalAssociationCategory> findCategories();
	
	/**
	 * Create criminal associate.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @return Person person 
	 */
	Person createCriminalAssociate(String lastName, String firstName, 
		String middleName, String suffix);
		
	/**
	 * Update criminal associate.
	 * 
	 * @param criminalAssociate criminal associate that need to be updated
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @return Person person 
	 */
	Person updateCriminalAssociate(Person criminalAssociate, String lastName, 
		String firstName, String middleName, String suffix);
	
	/**
	 * Create address with the specified value and zip code.
	 * 
	 * @param value value
	 * @param zipCode zip code
	 * @return address address
	 */
	Address createAddress(String value, ZipCode zipCode);
	
	/**
	 * Create residence term.
	 * 
	 * @param person person
	 * @param address address
	 * @return residenceTerm residence term
	 * @throws DuplicateEntityFoundException duplicate entity found exception
	 * @throws ResidenceStatusConflictException 
	 * residence status conflict exception
	 * @throws PrimaryResidenceExistsException 
	 * primary residence exists exception
	 */
	ResidenceTerm createResidenceTerm(Person person, Address address) 
		throws DuplicateEntityFoundException, 
		PrimaryResidenceExistsException, 
		ResidenceStatusConflictException,
		ResidenceTermExistsException;
	
	/**
	 * Add telephone number.
	 * 
	 * @param person person
	 * @param telephoneNumber telephone number
	 * @return contact contact
	 * @throws TelephoneNumberExistsException telephone number 
	 */
	Contact addTelephoneNumber(Person person, Long telephoneNumber) 
		throws TelephoneNumberExistsException;
	
	/**
	 * Returns a list of all criminal associations for the specified offender
	 * as the main offender.
	 * 
	 * @param offender offender
	 * @return list of criminal associations
	 */
	List<CriminalAssociation> findByOffender(Offender offender); 

	/**
	 * Returns a list of all criminal associations for the specified offender 
	 * as the other offender.
	 * 
	 * @param offender offender
	 * @return list of criminal associations
	 */
	List<CriminalAssociation> findByOtherOffender(Offender offender); 
}