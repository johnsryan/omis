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
package omis.supervision.service.testng;

import java.beans.PropertyEditor;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusExistsException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.CorrectionalStatusTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests remove method of correctional status term service.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 10, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class CorrectionalStatusTermServiceRemoveTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate
	supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	/* Service. */
	
	@Autowired
	@Qualifier("correctionalStatusTermService")
	private CorrectionalStatusTermService correctionalStatusTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Test methods. */
	
	/**
	 * Tests removal of correctional status term.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if correctional status term is
	 * associated with existing placement term
	 */
	public void testRemoval()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		
		// Arrangements - places offender on parole
		Offender offender = this.createOffender();
		CorrectionalStatus parole = this.createParole();
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate.create(
					offender, dateRange, parole);
		
		// Action - remove parole term
		this.correctionalStatusTermService.remove(paroleTerm);
		
		// Asserts that parole term was removed
		assert this.correctionalStatusTermDelegate.findForOffenderOnDate(
				offender, DateRange.getStartDate(dateRange)) == null
			: "Correctional status term was not removed";
	}
	
	/**
	 * Tests that {@code PlacementTermExistsException} is thrown on removal
	 * attempt where a correctional status term is associated with a placement
	 * term.
	 * 
	 * <p>{@code PlacementTermExistsException} is the wrong exception to throw
	 * as in this case it does not signify that a duplicate placement term
	 * exists ({@code PlacementTermExistsException} derives from
	 * {@code DuplicateEntityFoundException} which indicates a duplicate).
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusTermExistsException if correctional stauts
	 * term exists
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 * @throws PlacementTermExistsException if correctional status term is
	 * associated with a placement term - wrongly asserted (see above)
	 */
	// TODO Switch to a none-duplicate entity found derived exception - SA
	@Test(expectedExceptions = {PlacementTermExistsException.class})
	public void testPlacementTermExistsException()
			throws CorrectionalStatusExistsException,
				SupervisoryOrganizationExistsException,
				CorrectionalStatusTermExistsException,
				SupervisoryOrganizationTermExistsException,
				PlacementTermExistsException {
	
		// Arrangements - places offender on parole under PO office supervision
		Offender offender = this.createOffender();
		CorrectionalStatus parole = this.createParole();
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate
				.create(offender, dateRange, parole);
		SupervisoryOrganization poOffice = this.supervisoryOrganizationDelegate
				.create("PO Office", "POFF", null);
		SupervisoryOrganizationTerm poOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, dateRange, poOffice);
		try {
			this.placementTermDelegate.create(
					offender, dateRange, poOfficeTerm, paroleTerm, null, null, false);
		} catch (PlacementTermExistsException e) {
			
			// Re-throw as assertion error as PlacementTermExistsException
			// is wrongly thrown when correctional status term is
			// associated with placement term
			throw new AssertionError("Duplicate placement term exists", e);
		}
		
		// Action - attempt to remove correctional status term
		this.correctionalStatusTermService.remove(paroleTerm);
	}
	
	/* Helper methods. */
	
	// Returns parsed date text
	private Date parseDateText(final String dateText) {
		PropertyEditor propertyEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		propertyEditor.setAsText(dateText);
		return (Date) propertyEditor.getValue();
	}
	
	// Returns offender
	private Offender createOffender() {
		return this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
	}
	
	// Returns parole status
	private CorrectionalStatus createParole()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate.create(
				"Parole", "PAR", false, (short) 1, true);
	}
}