package omis.hearing.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.service.DisciplinaryCodeService;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.component.Resolution;
import omis.hearing.service.ResolutionService;
import omis.hearing.service.delegate.InfractionPleaDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.ViolationEventService;

/**
 * Resolution Service Update Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class ResolutionServiceUpdateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("resolutionService")
	private ResolutionService resolutionService;
	
	@Autowired
	@Qualifier("violationEventService")
	private ViolationEventService violationEventService;
	
	@Autowired
	@Qualifier("disciplinaryCodeService")
	private DisciplinaryCodeService disciplinaryCodeService;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;

	@Autowired
	private InfractionPleaDelegate infractionPleaDelegate;
	
	@Test
	public void testInfractionUpdate() throws DuplicateEntityFoundException {
		final Resolution resolution2 = new Resolution();
		resolution2.setCategory(ResolutionClassificationCategory.DISMISSED);
		resolution2.setDate(this.parseDateText("06/06/2017"));
		resolution2.setDescision("Resolution Decision2");
		resolution2.setReason("Resolution Reason2");
		resolution2.setDisposition(DispositionCategory.NO_FINDING);
		resolution2.setAuthority(this.personDelegate.create("Big", "Little",
				"Medium", null));
		final InfractionPlea plea2 = this.infractionPleaDelegate
				.create("Guilty", true);
		Infraction infraction = this.resolutionService.createInfraction(
				null, null, null, resolution2, plea2);
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("Batcave21", "TBC21", null);
		final DisciplinaryCode disciplinaryCode = this.disciplinaryCodeService
				.createDisciplinaryCode("Code Value", "Code Description",
						"Extended Description");
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final ViolationEvent violationEvent = this.violationEventService
				.createViolationEvent(offender, supervisoryOrganization,
						this.parseDateText("05/05/2017"), "Event Details",
						ViolationEventCategory.DISCIPLINARY);
		final DisciplinaryCodeViolation disciplinaryCodeViolation =
				this.violationEventService.createDisciplinaryCodeViolation(
						disciplinaryCode, violationEvent);
		
		final String descision = "Resolution Decision";
		final String reason = "Resolution Reason";
		final ResolutionClassificationCategory resolutionCategory =
				ResolutionClassificationCategory.FORMAL;
		final DispositionCategory disposition = DispositionCategory.NOT_GUILTY;
		final Person person = this.personDelegate.create("Buttehead2", "Joel2", 
				"Trevor2", null);
		final Resolution resolution = new Resolution();
		resolution.setCategory(resolutionCategory);
		resolution.setDate(this.parseDateText("05/01/2017"));
		resolution.setDescision(descision);
		resolution.setReason(reason);
		resolution.setDisposition(disposition);
		resolution.setAuthority(person);
		final InfractionPlea plea = this.infractionPleaDelegate
				.create("Not Guilty", true);
		
		infraction = this.resolutionService.updateInfraction(infraction,
				null, disciplinaryCodeViolation, resolution, plea);
		
		assert disciplinaryCodeViolation.equals(
				infraction.getDisciplinaryCodeViolation())
		: String.format("Wrong disciplinaryCodeViolation for infraction: "
				+ "%s found; %s expected",
				infraction.getDisciplinaryCodeViolation()
				.getDisciplinaryCode().getValue(),
				disciplinaryCodeViolation.getDisciplinaryCode().getValue());
		assert descision.equals(infraction.getResolution().getDescision())
		: String.format("Wrong descision for infraction: %s found; %s expected",
				infraction.getResolution().getDescision(), descision);
		assert reason.equals(infraction.getResolution().getReason())
		: String.format("Wrong reason for infraction: %s found; %s expected",
				infraction.getResolution().getReason(), reason);
		assert resolutionCategory.equals(
				infraction.getResolution().getCategory())
		: String.format("Wrong resolutionCategory for infraction: "
				+ "%s found; %s expected",
				infraction.getResolution().getCategory(), resolutionCategory);
		assert disposition.equals(infraction.getResolution().getDisposition())
		: String.format("Wrong disposition for infraction: "
				+ "%s found; %s expected",
				infraction.getResolution().getDisposition(), disposition);
		assert plea.equals(infraction.getPlea())
		: String.format("Wrong plea for infraction: %s found; %s expected",
				infraction.getPlea().getName(), plea.getName());
	}
	
	@Test
	public void testImposedSanctionUpdate()
			throws DuplicateEntityFoundException {
		final Infraction infraction = this.createInfraction();
		final Offender offender = infraction.getDisciplinaryCodeViolation()
				.getViolationEvent().getOffender();
		
		ImposedSanction imposedSanction = this.resolutionService
				.createImposedSanction(infraction, offender, "Old Sanction");
		
		final String description = "New Sanction";
		
		imposedSanction = this.resolutionService.updateImposedSanction(
				imposedSanction, offender, description);
		
		assert infraction.equals(imposedSanction.getInfraction())
		: String.format("Wrong infraction for imposedSanction: "
				+ "%d found; %d expected",
				imposedSanction.getInfraction().getId(), infraction.getId());
		assert offender.equals(imposedSanction.getOffender())
		: String.format("Wrong offender for imposedSanction: "
				+ "%s found; %s expected",
				imposedSanction.getOffender().getName().getFirstName(),
				offender.getName().getFirstName());
		assert description.equals(imposedSanction.getDescription())
		: String.format("Wrong description for imposedSanction: "
				+ "%s found; %s expected",
				imposedSanction.getDescription(), description);
	}
	
	private Infraction createInfraction() throws DuplicateEntityFoundException {
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("The Batcave", "TBC", null);
		final DisciplinaryCode disciplinaryCode = this.disciplinaryCodeService
				.createDisciplinaryCode("Code Value", "Code Description",
						"Extended Description");
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final ViolationEvent violationEvent = this.violationEventService
				.createViolationEvent(offender, supervisoryOrganization,
						this.parseDateText("05/05/2017"), "Event Details",
						ViolationEventCategory.DISCIPLINARY);
		final DisciplinaryCodeViolation disciplinaryCodeViolation =
				this.violationEventService.createDisciplinaryCodeViolation(
						disciplinaryCode, violationEvent);
		final Person person = this.personDelegate.create("Buttehead", "Joel", 
				"Trevor", null);
		final String descision = "Resolution Decision";
		final String reason = "Resolution Reason";
		final ResolutionClassificationCategory resolutionCategory =
				ResolutionClassificationCategory.INFORMAL;
		final DispositionCategory disposition = DispositionCategory.NO_FINDING;
		
		final Hearing hearing = null;
		final ConditionViolation conditionViolation = null;
		final Resolution resolution = new Resolution();
		resolution.setCategory(resolutionCategory);
		resolution.setDate(this.parseDateText("05/01/2017"));
		resolution.setDescision(descision);
		resolution.setReason(reason);
		resolution.setDisposition(disposition);
		resolution.setAuthority(person);
		final InfractionPlea plea = this.infractionPleaDelegate
				.create("Not Guilty", true);
		return this.resolutionService.createInfraction(
				hearing, conditionViolation, disciplinaryCodeViolation,
				resolution, plea);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
}
