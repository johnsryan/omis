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
package omis.paroleboardcondition.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementAssociableDocument;
import omis.condition.service.delegate.AgreementAssociableDocumentDelegate;
import omis.condition.service.delegate.AgreementDelegate;
import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.paroleboardcondition.service.ParoleBoardConditionService;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests method to remove agreement associable documents.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class ParoleBoardConditionServiceRemoveAgreementAssociableDocumentTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private OffenderDelegate offenderDelegate;

	@Autowired
	private AgreementDelegate agreementDelegate;
	
	@Autowired
	private DocumentDelegate documentDelegate;
	
	@Autowired
	private AgreementAssociableDocumentDelegate 
			agreementAssociableDocumentDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardConditionService")
	private ParoleBoardConditionService paroleBoardConditionService;

	/* Test methods. */

	/**
	 * Tests the removal of agreement associable documents.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testRemoveAgreementAssociableDocument() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender,
				this.parseDateText("05/12/2017"),
				this.parseDateText("05/15/2019"), null, null);
		Document document = this.documentDelegate.create(
				this.parseDateText("05/12/2017"), "documentFileName",
				".doc", "Document Title");
		AgreementAssociableDocument agreementAssociableDocument = this
				.agreementAssociableDocumentDelegate.create(agreement, document);

		// Action
		this.paroleBoardConditionService.removeAgreementAssociableDocument(
				agreementAssociableDocument);

		// Assertions
		assert !this.agreementAssociableDocumentDelegate
			.findByAgreement(agreement).contains(agreementAssociableDocument)
			: "Agreement Associable Document was not removed.";
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