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
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;
import omis.paroleeligibility.service.ParoleEligibilityService;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create parole eligibilities.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Nov 30, 2017)
 * @since OMIS 3.0
 */
public class ParoleEligibilityServiceCreateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
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

	/**
	 * Test the method to create a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate parole
	 * eligibility is found.
	 */
	@Test
	public void testCreate() throws DuplicateEntityFoundException {
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
		Date reviewDate = this.parseDateText("02/01/2017");

		// Action
		ParoleEligibility paroleEligibility = this.paroleEligibilityService
				.create(offender, hearingEligibilityDate, appearanceCategory, 
						statusCategory, statusDate, statusReason,
						statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate parole
	 * eligibility is found.
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
		Date reviewDate = this.parseDateText("02/01/2017");
		
		this.paroleEligibilityDelegate.create(offender, hearingEligibilityDate, 
				reviewDate, new ParoleEligibilityStatus(statusDate, 
				statusComment, statusCategory, statusReason), 
				appearanceCategory);
		
		// Action
		this.paroleEligibilityService.create(offender, hearingEligibilityDate, 
				appearanceCategory, statusCategory, statusDate, statusReason, 
				statusComment, reviewDate);
			
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
