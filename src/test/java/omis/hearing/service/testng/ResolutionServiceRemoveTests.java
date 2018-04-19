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
import omis.hearing.domain.ImposedSanction;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.component.Resolution;
import omis.hearing.service.HearingService;
import omis.hearing.service.ResolutionService;
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
import omis.staff.service.StaffTitleService;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.ViolationEventService;

/**
 * Resolution Service Remove Tests.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class ResolutionServiceRemoveTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("resolutionService")
	private ResolutionService resolutionService;
	
	@Autowired
	@Qualifier("hearingService")
	private HearingService hearingService;
	
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
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
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
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	private StaffTitleService staffTitleService;
	
	@Autowired
	private StaffAssignmentDao staffAssignmentDao;
	
	@Autowired
	@Qualifier("staffAssignmentInstanceFactory")
	private InstanceFactory<StaffAssignment> staffAssignmentInstanceFactory;
	
	@Test
	public void testInfractionRemove() throws DuplicateEntityFoundException {
		
		final Infraction infraction = this.createInfraction();
		
		this.resolutionService.removeInfraction(infraction);
		
		assert !this.resolutionService.findInfractionsByHearing(
				infraction.getHearing()).contains(infraction)
		: "Infraction was not removed!";
	}
	
	@Test
	public void testImposedSanctionRemove()
			throws DuplicateEntityFoundException {
		final Infraction infraction = this.createInfraction();
		final Offender offender = infraction.getDisciplinaryCodeViolation()
				.getViolationEvent().getOffender();
		
		final ImposedSanction imposedSanction = this.resolutionService
				.createImposedSanction(infraction, offender, "Sanction");
		
		this.resolutionService.removeImposedSanction(imposedSanction);
		
		assert this.resolutionService.findImposedSanctionByInfraction(
				infraction) == null : "Imposed Sanction was not removed!";
	}
	
	private Infraction createInfraction() throws DuplicateEntityFoundException {
		final SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate
			.create("Batcave", "TBC2", null);
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
				ResolutionClassificationCategory.INFORMAL;
		final DispositionCategory disposition = DispositionCategory.NO_FINDING;
		
		final Hearing hearing = this.createHearing();
		final ConditionViolation conditionViolation = null;
		final Resolution resolution = new Resolution();
		resolution.setCategory(resolutionCategory);
		resolution.setDescision(descision);
		resolution.setReason(reason);
		resolution.setDisposition(disposition);
		return this.resolutionService.createInfraction(
				hearing, conditionViolation, disciplinaryCodeViolation,
				resolution, null);
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
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
}
