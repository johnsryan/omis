package omis.contact.web.controller.delegate;

import java.util.Map;

import omis.contact.report.ContactReportService;
import omis.contact.report.ContactSummary;
import omis.person.domain.Person;

/**
 * Controller delegate for contact summaries.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 12, 2016)
 * @since OMIS 3.0
 */
public class ContactSummaryModelDelegate {
	
	private final ContactReportService contactReportService;
	
	private final String CONTACT_SUMMARY_MODEL_KEY = "contactSummary";
	
	/**
	 * Model delegate for contact summaries.
	 * 
	 * @param contactReportService report service for contacts
	 */
	public ContactSummaryModelDelegate(
			final ContactReportService contactReportService) {
		this.contactReportService = contactReportService;
	}

	/**
	 * Adds contact summary for person to model.
	 * 
	 * @param modelMap model map
	 * @param person person
	 */
	public void add(final Map<String, Object> modelMap, final Person person) {
		ContactSummary contactSummary = this.contactReportService
				.summarizeByPerson(person);
		System.out.println(String.format("ContactSummary: %s, %s, %d", contactSummary.getPersonLastName(), contactSummary.getPersonFirstName(), contactSummary.getPersonId()));
		modelMap.put(CONTACT_SUMMARY_MODEL_KEY, contactSummary);
		System.out.println(String.format("ContactSummary: %s, %s, %d", contactSummary.getPersonLastName(), contactSummary.getPersonFirstName(), contactSummary.getPersonId()));
	}
}