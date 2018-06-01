package omis.paroleeligibility.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.ParoleEligibilityNote;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;
import omis.paroleeligibility.service.ParoleEligibilityService;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityNoteDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

public class ParoleEligibilityServiceUpdateNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("eligibilityStatusReasonDelegate")
	private EligibilityStatusReasonDelegate eligibilityStatusReasonDelegate;
	
	@Autowired
	@Qualifier("appearanceCategoryDelegate")
	private AppearanceCategoryDelegate appearanceCategoryDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityNoteDelegate")
	private ParoleEligibilityNoteDelegate paroleEligibilityNoteDelegate;

	/* Services. */

	@Autowired
	@Qualifier("paroleEligibilityService")
	private ParoleEligibilityService paroleEligibilityService;

	/* Test methods. */

	/**
	 * Tests the update of the date for a parole eligibility note.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate date is
	 * found.
	 */
	@Test
	public void testUpdateParoleEligibilityNoteDate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Smith", "John", "Jay", null);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
			.create("Initial", true);
		EligibilityStatusCategory statusCategory 
			= EligibilityStatusCategory.APPEARING;
		Date statusDate = this.parseDateText("01/01/2017");
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
			.create("Programming incomplete", statusCategory, true);
		String statusComment = "Programming complete next month.";
		Date reviewDate = this.parseDateText("01/01/2017");
		Date date = this.parseDateText("01/01/2017");
		String description = "Something";
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, hearingEligibilityDate, reviewDate, 
						new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason), appearanceCategory);
		ParoleEligibilityNote paroleEligibilityNote 
			= this.paroleEligibilityNoteDelegate.create(paroleEligibility, 
					description, date);
		Date newDate = this.parseDateText("02/01/2017");

		// Action
		paroleEligibilityNote = this.paroleEligibilityService
				.updateParoleEligibilityNote(paroleEligibilityNote, newDate, 
				description);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("date", newDate)
			.addExpectedValue("description", description)
			.performAssertions(paroleEligibilityNote);
	}
	
	/**
	 * Tests the update of the description for a parole eligibility note.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate description
	 * is found.
	 */
	@Test
	public void testUpdateParoleEligibilityNoteDescription() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Smith", "John", "Jay", null);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
			.create("Initial", true);
		EligibilityStatusCategory statusCategory 
			= EligibilityStatusCategory.APPEARING;
		Date statusDate = this.parseDateText("01/01/2017");
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
			.create("Programming incomplete", statusCategory, true);
		String statusComment = "Programming complete next month.";
		Date reviewDate = this.parseDateText("01/01/2017");
		Date date = this.parseDateText("01/01/2017");
		String description = "Something";
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, hearingEligibilityDate, reviewDate, 
						new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason), appearanceCategory);
		ParoleEligibilityNote paroleEligibilityNote 
			= this.paroleEligibilityNoteDelegate.create(paroleEligibility, 
					description, date);
		String newDescription = "Something else";

		// Action
		paroleEligibilityNote = this.paroleEligibilityService
				.updateParoleEligibilityNote(paroleEligibilityNote, date, 
				newDescription);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("date", date)
			.addExpectedValue("description", newDescription)
			.performAssertions(paroleEligibilityNote);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate updated
	 * parole eligibility note is found.
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Smith", "John", "Jay", null);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
			.create("Initial", true);
		EligibilityStatusCategory statusCategory 
			= EligibilityStatusCategory.APPEARING;
		Date statusDate = this.parseDateText("01/01/2017");
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
			.create("Programming incomplete", statusCategory, true);
		String statusComment = "Programming complete next month.";
		Date reviewDate = this.parseDateText("01/01/2017");
		Date date = this.parseDateText("01/01/2017");
		String description = "Something";
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, hearingEligibilityDate, reviewDate, 
						new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason), appearanceCategory);
		this.paroleEligibilityNoteDelegate.create(paroleEligibility, 
					description, date);
		
		// Action
		this.paroleEligibilityService.createParoleEligibilityNote(paroleEligibility, 
				date, description);
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
