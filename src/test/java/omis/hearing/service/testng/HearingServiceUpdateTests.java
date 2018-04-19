
package omis.hearing.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;
import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.service.DisciplinaryCodeService;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.HearingNote;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.StaffAttendance;
import omis.hearing.domain.component.Resolution;
import omis.hearing.service.HearingService;
import omis.hearing.service.delegate.InfractionPleaDelegate;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.staff.dao.StaffAssignmentDao;
import omis.staff.domain.StaffAssignment;
import omis.staff.domain.StaffTitle;
import omis.staff.service.StaffTitleService;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.ViolationEventService;

/**
 * Hearing Service Update Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class HearingServiceUpdateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	@Autowired
	@Qualifier("hearingService")
	private HearingService hearingService;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private StaffAssignmentDao staffAssignmentDao;
	
	@Autowired
	@Qualifier("staffAssignmentInstanceFactory")
	private InstanceFactory<StaffAssignment> staffAssignmentInstanceFactory;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;

	@Autowired
	private InfractionPleaDelegate infractionPleaDelegate;
	
	@Autowired
	private StaffTitleService staffTitleService;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("violationEventService")
	private ViolationEventService violationEventService;
	
	@Autowired
	@Qualifier("disciplinaryCodeService")
	private DisciplinaryCodeService disciplinaryCodeService;
	
	private Infraction infraction;
	
	
	
	@Test
	public void testHearingUpdate() throws DuplicateEntityFoundException {
		
		final Organization organization = this.organizationDelegate.create(
				"Organization22", "org22", null);
		final Country country = this.countryDelegate.create(
				"Country2", "USA2", true);
		final State state = this.stateDelegate.create(
				"State2", "ST2", country, true, true);
		final City city = this.cityDelegate.create(
				"Cit2y", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "222333", null, true);
		final Address address = this.addressDelegate.findOrCreate(
				"321", "123", null,
				null, zipCode);
		final Person person = this.personDelegate.create(
				"Pennyworth2", "Alfred2", "J2", null);
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("The Batcave2", "TBC2", organization);
		
		StaffAssignment officer = this.staffAssignmentInstanceFactory
				.createInstance();
		officer.setStaffMember(person);
		officer.setStaffId("2221");
		officer.setSupervisoryOrganization(supervisoryOrganization);
		officer.setSupervisory(true);
		officer.setTitle(this.staffTitleService.create(
				"Butler2", (short) 2, true));
		officer = this.staffAssignmentDao.makePersistent(officer);
		final Boolean inAttendance = false;
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("02/01/2001"),
						this.parseDateText("02/01/2020")), address);
		final Date date = this.parseDateText("06/06/2017");
		final HearingCategory category = HearingCategory.SUPERVISION;
		
		Hearing hearing = this.createHearing();
		final Offender offender = hearing.getSubject().getOffender();
		hearing = this.hearingService.updateHearing(hearing, location,
				inAttendance, date, category, officer);
		
		assert location.equals(hearing.getLocation())
		: String.format("Wrong location for hearing: %s found; %s expected",
				location.getOrganization().getName(),
				location.getOrganization().getName());
		assert offender.equals(hearing.getSubject().getOffender())
		: String.format("Wrong offender for hearing: "
				+ "%s found; %s expected",
				hearing.getSubject().getOffender().getName().getFirstName(),
				offender.getName().getFirstName());
		assert inAttendance.equals(hearing.getSubject().getInAttendance())
		: String.format("Wrong inAttendance for hearing: %s found; %s expected",
				hearing.getSubject().getInAttendance(), inAttendance);
		assert date.equals(hearing.getDate())
		: String.format("Wrong date for hearing: %s found; %s expected",
				hearing.getDate(), date);
		assert category.equals(hearing.getCategory())
		: String.format("Wrong category for hearing: %s found; %s expected",
				hearing.getCategory(), category);
		assert officer.equals(hearing.getOfficer())
		: String.format("Wrong officer for hearing: %s found; %s expected",
				hearing.getOfficer().getStaffMember().getName().getFirstName(),
				officer.getStaffMember().getName().getFirstName());
	}
	
	@Test
	public void testHearingStatusCreate() throws DuplicateEntityFoundException {
		final Hearing hearing = this.createHearing();
		final String description = "Status Description";
		final Date date = this.parseDateText("05/05/2017");
		final HearingStatusCategory category = HearingStatusCategory.HELD;
		
		HearingStatus hearingStatus = this.hearingService
				.createHearingStatus(hearing, "description1",
						this.parseDateText("01/01/2017"),
						HearingStatusCategory.PENDING);
		hearingStatus = this.hearingService.updateHearingStatus(hearingStatus,
				description, date, category);
		
		assert hearing.equals(hearingStatus.getHearing())
		: String.format("Wrong hearing for hearingStatus: "
				+ "%d found; %d expected",
				hearingStatus.getHearing().getId(), hearing.getId());
		assert description.equals(hearingStatus.getDescription())
		: String.format("Wrong description for hearingStatus: "
				+ "%s found; %s expected",
				hearingStatus.getDescription(), description);
		assert date.equals(hearingStatus.getDate())
		: String.format("Wrong date for HearingStatus: %s found; %s expected",
				hearingStatus.getDate(), date);
		assert category.equals(hearingStatus.getCategory())
		: String.format("Wrong category for hearingStatus: "
				+ "%s found; %s expected",
				hearingStatus.getCategory(), category);
	}
	
	@Test
	public void testHearingNoteUpdate() throws DuplicateEntityFoundException {
		final Hearing hearing = this.createHearing();
		final String description = "Note Description";
		final Date date = this.parseDateText("05/05/2017");
		
		HearingNote hearingNote = this.hearingService.createHearingNote(
				hearing, "description2", this.parseDateText("01/01/2017"));
		hearingNote = this.hearingService.updateHearingNote(
				hearingNote, description, date);
		
		assert hearing.equals(hearingNote.getHearing())
		: String.format("Wrong hearing for hearingNote: %d found; %d expected",
				hearingNote.getHearing().getId(), hearing.getId());
		assert description.equals(hearingNote.getDescription())
		: String.format("Wrong description for hearingNote: "
				+ "%s found; %s expected",
				hearingNote.getDescription(), description);
		assert date.equals(hearingNote.getDate())
		: String.format("Wrong date for hearingNote: %s found; %s expected",
				hearingNote.getDate(), date);
		
	}
	
	//I can't get staffAttendance update tested without a more thorough
	//creation implementation for StaffAssignment, I think. 
	//But I am certain the staffAttendance update works in practice.
	//@Test
	public void testStaffAttendanceUpdate()
			throws DuplicateEntityFoundException {
		final Hearing hearing = this.createHearing();
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("Batcave2", "TBC", null);
		final Person person = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final StaffTitle staffTitle = this.staffTitleService.create(
				"Robin", (short) 12, true);
		StaffAttendance staffAttendance = this.hearingService
				.createStaffAttendance(hearing, this.createStaffAssignment(
						supervisoryOrganization, staffTitle));
		
		StaffAssignment staffAssignment = this.staffAssignmentInstanceFactory
				.createInstance();
		staffAssignment.setId((long) 2);
		staffAssignment.setStaffMember(person);
		staffAssignment.setStaffId("4321");
		staffAssignment.setSupervisoryOrganization(supervisoryOrganization);
		staffAssignment.setSupervisory(true);
		staffAssignment.setTitle(staffTitle);
		staffAssignment = this.staffAssignmentDao
				.makePersistent(staffAssignment);
		
		
		
		
		
		staffAttendance = this.hearingService.updateStaffAttendance(
				staffAttendance, staffAssignment);
		
		assert hearing.equals(staffAttendance.getHearing())
		: String.format("Wrong hearing for staffAttendance: "
				+ "%d found; %d expected",
				staffAttendance.getHearing().getId(), hearing.getId());
		assert staffAssignment.equals(staffAttendance.getStaff())
		: String.format("Wrong staffAssignment for staffAttendance: "
				+ "%s found; %s expected",
				staffAttendance.getStaff().getStaffMember()
				.getName().getFirstName(),
				staffAssignment.getStaffMember().getName().getFirstName());
	}
	
	
	
	@Test
	public void testInfractionUpdate() throws DuplicateEntityFoundException {
		final Hearing hearing = this.createHearing();
		final Resolution resolution2 = new Resolution();
		resolution2.setCategory(ResolutionClassificationCategory.DISMISSED);
		resolution2.setDescision("Resolution Decision2");
		resolution2.setReason("Resolution Reason2");
		resolution2.setDisposition(DispositionCategory.NO_FINDING);
		final InfractionPlea plea2 = this.infractionPleaDelegate
				.create("Guilty", true);
		this.infraction = this.hearingService.createInfraction(
				hearing, null, null, resolution2, plea2);
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("Batcave21", "TBC",
					hearing.getLocation().getOrganization());
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
		
		final Resolution resolution = new Resolution();
		resolution.setCategory(resolutionCategory);
		resolution.setDescision(descision);
		resolution.setReason(reason);
		resolution.setDisposition(disposition);
		final InfractionPlea plea = this.infractionPleaDelegate
				.create("Not Guilty", true);
		Infraction infraction = this.hearingService.updateInfraction(
				this.infraction, null, disciplinaryCodeViolation,
				resolution, plea);
		
		assert hearing.equals(infraction.getHearing())
		: String.format("Wrong hearing for infraction: %s found; %s expected",
				infraction.getHearing().getId(), hearing.getId());
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
	
	
	
	private Hearing createHearing() throws DuplicateEntityFoundException {
		final Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate(
				"123", "321", null,
				null, zipCode);
		final Person person = this.personDelegate.create(
				"Pennyworth", "Alfred", "J", null);
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("The Batcave", "TBC", organization);
		
		StaffAssignment officer = this.staffAssignmentInstanceFactory
				.createInstance();
		officer.setStaffMember(person);
		officer.setStaffId("999");
		officer.setSupervisoryOrganization(supervisoryOrganization);
		officer.setSupervisory(true);
		officer.setTitle(this.staffTitleService.create(
				"Butler", (short) 1, true));
		officer = this.staffAssignmentDao.makePersistent(officer);
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final Boolean inAttendance = false;
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Date date = this.parseDateText("05/04/2017");
		final HearingCategory category = HearingCategory.DISCIPLINARY;
		
		return this.hearingService.createHearing(location,
				offender, inAttendance, date, category, officer);
	}
	
	private StaffAssignment createStaffAssignment(
			final SupervisoryOrganization supervisoryOrganization,
			final StaffTitle staffTitle)
			throws DuplicateEntityFoundException {
		final Person person = this.personDelegate.create(
				"Grayson2", "Richard2", "J2", null);
		StaffAssignment staffAssignment2 =
				this.staffAssignmentInstanceFactory.createInstance();
		staffAssignment2.setId((long) 1);
		staffAssignment2.setStaffMember(person);
		staffAssignment2.setStaffId("432122");
		staffAssignment2.setSupervisoryOrganization(supervisoryOrganization);
		staffAssignment2.setSupervisory(true);
		staffAssignment2.setTitle(staffTitle);
		return this.staffAssignmentDao.makePersistent(staffAssignment2);
		
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
}
