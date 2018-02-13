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
package omis.criminalassociation.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.criminalassociation.domain.CriminalAssociation;
import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.criminalassociation.domain.component.CriminalAssociationFlags;
import omis.criminalassociation.exception.CriminalAssociationCategoryExistsException;
import omis.criminalassociation.exception.CriminalAssociationExistsException;
import omis.criminalassociation.service.CriminalAssociationService;
import omis.criminalassociation.service.delegate.CriminalAssociationCategoryDelegate;
import omis.criminalassociation.service.delegate.CriminalAssociationServiceDelegate;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests service to update criminal associations.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 6, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"criminalassociation"})
public class CriminalAssociationServiceUpdateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("criminalAssociationCategoryDelegate")
	private CriminalAssociationCategoryDelegate 
			criminalAssociationCategoryDelegate;
	
	@Autowired
	@Qualifier("criminalAssociationServiceDelegate")
	private CriminalAssociationServiceDelegate 
			criminalAssociationServiceDelegate;
	
	/* Service to test. */
	@Autowired
	@Qualifier("criminalAssociationService")
	private CriminalAssociationService 
			criminalAssociationService;
	
	/** Tests the creation of criminal association service.
	 * 
	 * @throws ReflexiveRelationshipException reflexive relationship exception
	 * @throws CriminalAssociationExistsException criminal association exists
	 * @throws CriminalAssociationCategoryExistsException criminal association
	 * category exists
	 */
	@Test
	public void testCriminalAssociationUpdate() 
			throws CriminalAssociationExistsException, 
			ReflexiveRelationshipException, 
			CriminalAssociationCategoryExistsException {
		//Arrangements
		Person criminalAssociate 
			= this.criminalAssociationService.createCriminalAssociate(
				"Associate", "Criminal", "Two", "M");		
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Criminal", "Offender", "One", "M");		
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("12/01/2017"));
		CriminalAssociationCategory category 
			= this.criminalAssociationCategoryDelegate.create("categoryName", 
					(short) 1, true);
		CriminalAssociationFlags flags = new CriminalAssociationFlags();
		flags.setWitness(true);	
		Person newCriminalAssociate = this.criminalAssociationService
				.updateCriminalAssociate(criminalAssociate, "newLast", 
						"newFirst", null, null);
		CriminalAssociation association 
			= this.criminalAssociationServiceDelegate.associateCriminally(
					offender, newCriminalAssociate, dateRange, category, flags);
		CriminalAssociationCategory newCategory 
			= this.criminalAssociationCategoryDelegate.create(
				"newCategoryName", (short) 1, true);
		dateRange = new DateRange(this.parseDateText("04/01/2017"), 
			this.parseDateText("12/01/2017"));
		//Action
		this.criminalAssociationService.update(association, 
				dateRange, newCategory, flags);
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("relationship.firstPerson", offender)
			.addExpectedValue("relationship.secondPerson", criminalAssociate)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("criminalAssociationCategory", newCategory)
			.addExpectedValue("criminalAssociationFlags", flags)
			.performAssertions(association);		
	}
	
	/**
	 * Tests {@code CriminalAssociationExistsException} is thrown.
	 *
	 *
	 * @throws ReflexiveRelationshipException reflexive relationship exception
	 * @throws CriminalAssociationExistsException criminal association exists
	 * @throws CriminalAssociationCategoryExistsException criminal association
	 * category exists
	 */
	@Test(expectedExceptions = {CriminalAssociationExistsException.class})
	public void testCriminalAssociationExistsException() 
			throws CriminalAssociationCategoryExistsException, 
			CriminalAssociationExistsException, ReflexiveRelationshipException {
		//Arrangements
		Person criminalAssociate 
			= this.criminalAssociationService.createCriminalAssociate(
				"Associate", "Criminal", "Two", "M");		
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Criminal", "Offender", "One", "M");		
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("12/01/2017"));
		CriminalAssociationCategory category 
			= this.criminalAssociationCategoryDelegate.create("categoryName", 
					(short) 1, true);
		CriminalAssociationFlags flags = new CriminalAssociationFlags();
		flags.setWitness(true);	
		Person newCriminalAssociate = this.criminalAssociationService
				.updateCriminalAssociate(criminalAssociate, "newLast", 
						"newFirst", null, null);
		this.criminalAssociationServiceDelegate.associateCriminally(
					offender, newCriminalAssociate, dateRange, category, flags);
		CriminalAssociationCategory newCategory 
			= this.criminalAssociationCategoryDelegate.create(
				"newCategoryName", (short) 1, true);
		
		CriminalAssociation criminalAssociation 
			= this.criminalAssociationServiceDelegate.associateCriminally(
				offender, newCriminalAssociate, dateRange, newCategory, flags);
		//Action
		this.criminalAssociationService.update(criminalAssociation, 
				dateRange, category, flags);
	}
	
	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}