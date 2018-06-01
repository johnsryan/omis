package omis.paroleeligibility.service.testng;

import omis.paroleeligibility.service.ParoleEligibilityService;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.ParoleEligibilityNote;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;

public class ParoleEligibilityServiceCreateNoteTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("appearanceCategoryDelegate")
	private AppearanceCategoryDelegate appearanceCategoryDelegate;
	
	@Autowired
	@Qualifier("eligibilityStatusReasonDelegate")
	private EligibilityStatusReasonDelegate eligibilityStatusReasonDelegate;

	/* Services. */

	@Autowired
	@Qualifier("paroleEligibilityService")
	private ParoleEligibilityService paroleEligibilityService;

	/* Test methods. */

	
	@Test
	public void testCreateParoleEligibilityNote() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Smith", "John", "Jay", null);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Initial", true);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Programming incomplete",
						EligibilityStatusCategory.APPEARING, true);
		Date reviewDate = this.parseDateText("02/01/2017");
		Date statusDate = this.parseDateText("01/01/2017");
		String statusComment = "Programming complete next month.";
		EligibilityStatusCategory statusCategory 
				= EligibilityStatusCategory.APPEARING;
		Date date = this.parseDateText("01/01/2017");
		String description = "Something.";
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, hearingEligibilityDate, reviewDate, 
						new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason), appearanceCategory);
		// Action
		ParoleEligibilityNote paroleEligibilityNote 
			= this.paroleEligibilityService.createParoleEligibilityNote(
					paroleEligibility, date, description);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("paroleEligibility", paroleEligibility)
			.addExpectedValue("date", date)
			.addExpectedValue("description", description)
			.performAssertions(paroleEligibilityNote);
	}

	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Smith", "John", "Jay", null);
		Date hearingEligibilityDate = this.parseDateText("01/01/2017");
		AppearanceCategory appearanceCategory = this.appearanceCategoryDelegate
				.create("Initial", true);
		EligibilityStatusReason statusReason = eligibilityStatusReasonDelegate
				.create("Programming incomplete",
						EligibilityStatusCategory.APPEARING, true);
		Date reviewDate = this.parseDateText("02/01/2017");
		Date statusDate = this.parseDateText("01/01/2017");
		String statusComment = "Programming complete next month.";
		EligibilityStatusCategory statusCategory 
				= EligibilityStatusCategory.APPEARING;
		Date date = this.parseDateText("01/01/2017");
		String description = "Something.";
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, hearingEligibilityDate, reviewDate, 
						new ParoleEligibilityStatus(statusDate, statusComment, 
						statusCategory, statusReason), appearanceCategory);
		this.paroleEligibilityService.createParoleEligibilityNote(
				paroleEligibility, date, description);
		
		// Action
		this.paroleEligibilityService.createParoleEligibilityNote(
				paroleEligibility, date, description);
	}

	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
	
}