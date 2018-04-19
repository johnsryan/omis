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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.testng.annotations.Test;

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.service.PlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests placement term removals.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Apr 16, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"supervision", "service"})
public class PlacementTermServiceRemoveTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("placementTermService")
	private PlacementTermService placementTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/**
	 * Tests removal of placement term.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 */
	public void testRemoval() throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld on probation
		Offender ernst = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("11/11/2011"),
				this.parseDateText("12/12/2012"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", true, (short) 1, true);
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate.create(
					ernst, dateRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "PO", null);
		SupervisoryOrganizationTerm poTerm
			= this.supervisoryOrganizationTermDelegate
				.create(ernst, dateRange, paroleOffice);
		PlacementTerm placementTerm
			= this.placementTermDelegate.create(
					ernst, dateRange, poTerm, paroleTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that placement term was removed
		assert placementTermDelegate.findForOffenderOnDate(ernst,
				DateRange.getStartDate(dateRange)) == null
				: "Placement term was not removed";
	}
	
	/**
	 * Tests that correctional status terms with same dates is removed with
	 * placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * @throws DuplicateEntityFoundException if entities exist
	 */
	public void testRemovalWithCorrectionalStatusTermOnSameDates()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld securely in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/30/2013"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, dateRange, secure);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate.create(
					"Prison", "PRS", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, dateRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that correctional status term was removed
		assert this.correctionalStatusTermDelegate.findForOffenderOnDate(
				blofeld, DateRange.getStartDate(dateRange)) == null
			: "Correctional status term was not removed";
	}
	
	/**
	 * Tests that supervisory organization term with same dates is removed with
	 * placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * @throws DuplicateEntityFoundException if entities exist 
	 */
	public void testRemovalWithSupervisoryOrganizationTermOnSameDates()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld securely in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/30/2013"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, dateRange, secure);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate.create(
					"Prison", "PRS", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, dateRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Assert that supervisory organization term was removed
		assert this.supervisoryOrganizationTermDelegate
			.findByOffenderOnEffectiveDate(
				blofeld, DateRange.getStartDate(dateRange)) == null
			: "Supervisory organization term was not removed";
	}
	
	/**
	 * Tests that correctional status term with different dates is adjusted when
	 * a placement term is removed.
	 * 
	 * <p>The correctional status term has a later end date than the placement
	 * term. This should result in the correctional status term being
	 * shortened to start on the end date of the placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 */
	public void testRemovalWithCorrectionalStatusTermWithLaterEndDate()
			throws DuplicateEntityFoundException {
	
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange statusRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("03/03/2003"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, statusRange, secure);
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("02/02/2002"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, dateRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that correctional status term was adjusted correctly
		CorrectionalStatusTerm adjustedSecureTerm
			= this.correctionalStatusTermDelegate.findForOffenderOnDate(
				blofeld, DateRange.getEndDate(dateRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getEndDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(statusRange))
			.performAssertions(adjustedSecureTerm);
	}
	
	/**
	 * Tests that correctional status term with different dates is adjusted when
	 * a placement term is removed.
	 * 
	 * <p>The correctional status term has a {@code null} end date where as
	 * the placement term doesn't. This should result in the correctional
	 * status term being shortened to start on the end date of the placement
	 * term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 */
	public void testRemovalWithCorrectionalStatusTermWithNullEndDate()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange statusRange = new DateRange(
				this.parseDateText("01/01/2001"), null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, statusRange, secure);
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("02/02/2002"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, dateRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that correctional status term was adjusted correctly
		CorrectionalStatusTerm adjustedSecureTerm
			= this.correctionalStatusTermDelegate.findForOffenderOnDate(
					blofeld, DateRange.getEndDate(dateRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getEndDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(statusRange))
			.performAssertions(adjustedSecureTerm);
	}
	
	/**
	 * Tests that supervisory organization term with different dates is adjusted
	 * when a placement term is removed.
	 * 
	 * <p>The supervisory organization term has a later end date than the
	 * placement term. This should result in the supervisory organization term
	 * being shortened to start on the end date of the placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 */
	public void testRemovalWithSupervisoryOrganizationTermWithLaterEndDate()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("02/02/2002"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, dateRange, secure);
		DateRange prisonRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("03/03/2003"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, prisonRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that supervisory organization term was adjusted correctly
		SupervisoryOrganizationTerm adjustedPrisonTerm
			= this.supervisoryOrganizationTermDelegate
				.findByOffenderOnEffectiveDate(
						blofeld, DateRange.getEndDate(dateRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getEndDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(prisonRange))
			.performAssertions(adjustedPrisonTerm);
	}
	
	/**
	 * Tests that supervisory organization term with different dates is adjusted
	 * when placement term is removed.
	 * 
	 * <p>The supervisory organization term has a {@code null} end date where as
	 * the placement term doesn't. This should result in the supervisory
	 * organization term being shortened to start on the end date of
	 * the placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 */
	public void testRemovalWithSupervisoryOrganizationTermWithNullEndDate()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("02/02/2002"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(blofeld, dateRange, secure);
		DateRange prisonRange = new DateRange(
				this.parseDateText("01/01/2001"), null);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, prisonRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				blofeld, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that supervisory organization term was adjusted correctly
		SupervisoryOrganizationTerm adjustedPrisonTerm
			= this.supervisoryOrganizationTermDelegate
				.findByOffenderOnEffectiveDate(
						blofeld, DateRange.getEndDate(dateRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getEndDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(prisonRange))
			.performAssertions(adjustedPrisonTerm);
	}
	
	/**
	 * Tests that correctional status term with different dates is adjusted when
	 * a placement term is removed.
	 * 
	 * <p>The correctional status term has an earlier start date than the
	 * placement term. This should result in the correctional status term
	 * being shortened to end on the start date of the placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 */
	public void testRemovalWithCorrectionalStatusTermWithEarlierStartDate()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld in prison, with a secure status
		// starting *before* his placement
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange secureRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("03/03/2003"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, secureRange, secure);
		DateRange dateRange = new DateRange(
				this.parseDateText("02/02/2002"),
				this.parseDateText("03/03/2003"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, dateRange, prison);
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that correctional status term was adjusted correctly
		CorrectionalStatusTerm adjustedSecureTerm
			= this.correctionalStatusTermDelegate
				.findForOffenderOnDate(offender,
						DateRange.getStartDate(secureRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(secureRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getStartDate(dateRange))
			.performAssertions(adjustedSecureTerm);
	}
	
	/**
	 * Tests that supervisory organization term with different dates is adjusted
	 * when a placement term is removed.
	 * 
	 * <p>The supervisory organization term has an earlier start date than the
	 * placement term. This should result in the supervisory organization term
	 * being shortened to end on the start date of the placement term.
	 * 
	 * <p>See {@link PlacementTermService#remove(PlacementTerm)}.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 */
	public void testRemovalWithSupervisoryOrganizationTermEarlierStartDate()
			throws DuplicateEntityFoundException {
		
		// Arrangements - places Blofeld in prison, with a prison term
		// starting *before* his placement
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("01/01/2001"),
				this.parseDateText("03/03/2003"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, prisonRange, prison);
		DateRange dateRange = new DateRange(
				this.parseDateText("02/02/2002"),
				this.parseDateText("03/03/2003"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, dateRange, secure);
		
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - removes placement term
		this.placementTermService.remove(placementTerm);
		
		// Asserts that supervisory organization was adjusted correctly
		SupervisoryOrganizationTerm adjustedPrisonTerm
			= this.supervisoryOrganizationTermDelegate
				.findByOffenderOnEffectiveDate(offender,
						DateRange.getStartDate(prisonRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(prisonRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getStartDate(dateRange))
			.performAssertions(adjustedPrisonTerm);
	}
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
}