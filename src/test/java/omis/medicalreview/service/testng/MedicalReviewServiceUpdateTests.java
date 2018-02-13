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
package omis.medicalreview.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;
import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;
import omis.medicalreview.domain.MedicalReviewNote;
import omis.medicalreview.service.MedicalReviewService;
import omis.medicalreview.service.delegate.MedicalHealthClassificationDelegate;
import omis.medicalreview.service.delegate.MedicalReviewDelegate;
import omis.medicalreview.service.delegate.MedicalReviewDocumentAssociationDelegate;
import omis.medicalreview.service.delegate.MedicalReviewNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Medical Review Service Update Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 5, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewServiceUpdateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	@Autowired
	@Qualifier("medicalReviewService")
	private MedicalReviewService medicalReviewService;
	
	@Autowired
	private MedicalReviewDelegate medicalReviewDelegate;
	
	@Autowired
	private MedicalReviewNoteDelegate medicalReviewNoteDelegate;

	@Autowired
	private MedicalReviewDocumentAssociationDelegate
		medicalReviewDocumentAssociationDelegate;
	
	@Autowired
	private MedicalHealthClassificationDelegate
		medicalHealthClassificationDelegate;
	
	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private DocumentDelegate documentDelegate;
	
	/**
	 * Tests Medical Review update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity exists.
	 */
	@Test
	public void testMedicalReviewUpdate()
			throws DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final MedicalHealthClassification healthClassification =
				this.medicalHealthClassificationDelegate.create(
						"Classification Test", true);
		final Date date = this.parseDateText("02/02/2018");
		final String text = "Medical Review Text";
		MedicalReview medicalReview = this.medicalReviewDelegate
				.create(offender, date, text, healthClassification);
		final MedicalHealthClassification healthClassification2 =
				this.medicalHealthClassificationDelegate.create(
						"Neeeeewwwwww Classification Test", true);
		final Date date2 = this.parseDateText("04/04/2018");
		final String text2 = "Updated Medical Review Text";
		
		medicalReview = this.medicalReviewService.updateMedicalReview(
				medicalReview, offender, date2, text2, healthClassification2);
		
		PropertyValueAsserter.create()
				.addExpectedValue("offender", offender)
				.addExpectedValue("healthClassification", healthClassification2)
				.addExpectedValue("date", date2)
				.addExpectedValue("text", text2)
				.performAssertions(medicalReview);
	}
	
	/**
	 * Tests Medical Review Note update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity exists.
	 */
	@Test
	public void testMedicalReviewNoteUpdate()
			throws DuplicateEntityFoundException {
		final MedicalReview medicalReview = this.createMedicalReview();
		final Date date = this.parseDateText("03/10/2018");
		final String description = "Note description";
		MedicalReviewNote medicalReviewNote =
				this.medicalReviewNoteDelegate.create(medicalReview,
						description, date);
		final Date date2 = this.parseDateText("04/15/2018");
		final String description2 = "Updated Note description";
		
		medicalReviewNote = this.medicalReviewService.updateMedicalReviewNote(
				medicalReviewNote, medicalReview, description2, date2);
		
		PropertyValueAsserter.create()
				.addExpectedValue("medicalReview", medicalReview)
				.addExpectedValue("date", date2)
				.addExpectedValue("description", description2)
				.performAssertions(medicalReviewNote);
	}

	/**
	 * Tests Medical Review Document Association update.
	 * @throws DuplicateEntityFoundException - When a duplicate entity exists.
	 */
	@Test
	public void testMedicalReviewDocumentAssociationUpdate()
			throws DuplicateEntityFoundException {
		final MedicalReview medicalReview = this.createMedicalReview();
		final Document document = this.documentDelegate.create(
				this.parseDateText("02/10/2018"), "document", ".doc",
				"Important Document");
		MedicalReviewDocumentAssociation documentAssociation =
				this.medicalReviewDocumentAssociationDelegate
				.create(medicalReview, document);
		final Document document2 = this.documentDelegate.create(
				this.parseDateText("03/21/2018"), "document21", ".doc",
				"Important Document Copy");
		
		this.medicalReviewService.updateMedicalReviewDocumentAssociation(
				documentAssociation, medicalReview, document2);
		
		PropertyValueAsserter.create()
				.addExpectedValue("medicalReview", medicalReview)
				.addExpectedValue("document", document2)
				.performAssertions(documentAssociation);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
	private MedicalReview createMedicalReview()
			throws DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final MedicalHealthClassification healthClassification =
				this.medicalHealthClassificationDelegate.create(
						"Classification Test", true);
		final Date date = this.parseDateText("02/02/2018");
		final String text = "Medical Review Text";
		
		return this.medicalReviewDelegate.create(offender, date, text,
						healthClassification);
	}
	
}
