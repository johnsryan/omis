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
 * Tests method to update parole eligibilities.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Nov 29, 2017)
 * @since OMIS 3.0
 */
public class ParoleEligibilityServiceUpdateTests
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

	/* Services. */

	@Autowired
	@Qualifier("paroleEligibilityService")
	private ParoleEligibilityService paroleEligibilityService;

	/* Test methods. */

	/**
	 * Tests the update of the eligibility date for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate eligibility
	 * date is found.
	 */
	@Test
	public void testEligibilityDateUpdate() 
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		Date newHearingEligibilityDate = this.parseDateText("02/01/2017");

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, newHearingEligibilityDate, 
					appearanceCategory, statusCategory, statusDate, 
					statusReason, statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", 
					newHearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the appearance category for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate appearance
	 * category is found.
	 */
	@Test
	public void testAppearanceCategoryUpdate() 
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		AppearanceCategory newAppearanceCategory = this
				.appearanceCategoryDelegate.create("Initial2", true);

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					newAppearanceCategory, statusCategory, statusDate, 
					statusReason, statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", newAppearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the status category for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate status
	 * category is found.
	 */
	@Test
	public void testStatusCategoryUpdate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
			"John", "Jay", null);
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		EligibilityStatusCategory newStatusCategory 
			= EligibilityStatusCategory.INELIGIBLE;

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					appearanceCategory, newStatusCategory, statusDate, 
					statusReason, statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", newStatusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the status date for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate status
	 * date is found.
	 */
	@Test
	public void testStatusDateUpdate() throws DuplicateEntityFoundException {
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		Date newStatusDate = this.parseDateText("02/01/2017");

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					appearanceCategory, statusCategory, newStatusDate, 
					statusReason, statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", newStatusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the status reason for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate status
	 * reason is found.
	 */
	@Test
	public void testStatusReasonUpdate() throws DuplicateEntityFoundException {
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		EligibilityStatusReason newStatusReason 
			= eligibilityStatusReasonDelegate.create("Programming complete",
					statusCategory, true);

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					appearanceCategory, statusCategory, statusDate, 
					newStatusReason, statusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", newStatusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the status comment for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate status
	 * comment is found.
	 */
	@Test
	public void testStatusCommentUpdate() throws DuplicateEntityFoundException {
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		String newStatusComment = "Programming complete this month.";

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					appearanceCategory, statusCategory, statusDate, 
					statusReason, newStatusComment, reviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", newStatusComment)
			.addExpectedValue("reviewDate", reviewDate)
			.performAssertions(paroleEligibility);
	}
	
	/**
	 * Tests the update of the review date for a parole eligibility.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate review
	 * date is found.
	 */
	@Test
	public void testReviewDateUpdate() throws DuplicateEntityFoundException {
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
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
			.create(offender, hearingEligibilityDate, reviewDate, 
					new ParoleEligibilityStatus(statusDate, statusComment, 
					statusCategory, statusReason), appearanceCategory);
		Date newReviewDate = this.parseDateText("02/01/2017");

		// Action
		paroleEligibility = this.paroleEligibilityService
			.update(paroleEligibility, hearingEligibilityDate, 
					appearanceCategory, statusCategory, statusDate, 
					statusReason, statusComment, newReviewDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("hearingEligibilityDate", hearingEligibilityDate)
			.addExpectedValue("appearanceCategory", appearanceCategory)
			.addExpectedValue("status.category", statusCategory)
			.addExpectedValue("status.date", statusDate)
			.addExpectedValue("status.reason", statusReason)
			.addExpectedValue("status.comment", statusComment)
			.addExpectedValue("reviewDate", newReviewDate)
			.performAssertions(paroleEligibility);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException thrown when a duplicate updated
	 * parole eligibility is found.
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
		
		Date newHearingEligibilityDate = this.parseDateText("01/01/2017");
				
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, newHearingEligibilityDate, 
				reviewDate, new ParoleEligibilityStatus(statusDate, 
				statusComment, statusCategory, statusReason), 
				appearanceCategory);
		
		// Action
		this.paroleEligibilityService.update(paroleEligibility, 
				hearingEligibilityDate, appearanceCategory, statusCategory, 
				statusDate, statusReason, statusComment, reviewDate);
			
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