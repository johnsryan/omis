package omis.detainernotification.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.detainernotification.dao.DetainerActivityCategoryDao;
import omis.detainernotification.dao.DetainerTypeDao;
import omis.detainernotification.domain.Detainer;
import omis.detainernotification.domain.DetainerActivityCategory;
import omis.detainernotification.domain.DetainerAgency;
import omis.detainernotification.domain.DetainerJurisdictionCategory;
import omis.detainernotification.domain.DetainerType;
import omis.detainernotification.domain.DetainerWarrantCancellationReason;
import omis.detainernotification.domain.DetainerWarrantProcessingStatus;
import omis.detainernotification.domain.Direction;
import omis.detainernotification.domain.InterstateAgreementDetainer;
import omis.detainernotification.domain.InterstateAgreementInitiatedByCategory;
import omis.detainernotification.domain.InterstateDetainerActivity;
import omis.detainernotification.service.DetainerService;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.facility.service.delegate.ComplexDelegate;
import omis.facility.service.delegate.FacilityDelegate;
import omis.facility.service.delegate.UnitDelegate;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * DetainerNotificationCreateTests.java
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Apr 10, 2017)
 *@since OMIS 3.0
 *
 */
public class DetainerNotificationCreateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("detainerService")
	private DetainerService detainerService;
	
	@Autowired
	private DetainerActivityCategoryDao detainerActivityCategoryDao;
	
	@Autowired
	@Qualifier("detainerActivityCategoryInstanceFactory")
	private InstanceFactory<DetainerActivityCategory>
		detainerActivityCategoryInstanceFactory;
	
	@Autowired
	private DetainerTypeDao detainerTypeDao;
	
	@Autowired
	@Qualifier("detainerTypeInstanceFactory")
	private InstanceFactory<DetainerType> detainerTypeInstanceFactory;
	
	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("facilityDelegate")
	private FacilityDelegate facilityDelegate;
	
	@Autowired
	@Qualifier("complexDelegate")
	private ComplexDelegate complexDelegate;
	
	@Autowired
	@Qualifier("unitDelegate")
	private UnitDelegate unitDelegate;
	
	@Autowired
	@Qualifier("facilityInstanceFactory")
	private InstanceFactory<Facility> facilityInstanceFactory;

	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Test
	public void testCreate() throws DuplicateEntityFoundException{
		
		/* Detainer Agency Properties */
		
		final String agencyName = "agencyName";
		final String telephoneNumber = "4065550123";
		final String value = "123 Didgeridoo";
		final String zipCodeValue = "59601";
		final String zipCodeExtension = "1234";
		
		final Boolean valid = true;
		final Boolean active = true;
	
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.detainerService.createZipCode(
				city, zipCodeValue, zipCodeExtension, active);
		final Address address = this.detainerService.createAddress(
				value, zipCode);
		
		/* Detainer Properties */
		
		final String alternateOffenderId = "alternateOffenderId";
		final String offenseDescription = "offenseDescription";
		final String courtCaseNumber = "courtCase#";
		final String warrantNumber = "warrant#";
		
		final Date warrantReceivedDate = this.parseDateText("04/25/2008");
		final Date warrantIssuedDate = this.parseDateText("04/16/2008");
		
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Man", "Old", "Guy", "Dude");
		
		final DetainerJurisdictionCategory jurisdiction =
				DetainerJurisdictionCategory.FEDERAL;
		
		DetainerType detainerType =
				this.detainerTypeInstanceFactory.createInstance();
		
		detainerType.setName("DetainerType");
		detainerType = this.detainerTypeDao.makePersistent(detainerType);
		
		DetainerAgency detainerAgency = this.detainerService
				.createDetainerAgency(
						agencyName, valid, telephoneNumber, address);
		
		/* Interstate Agreement Detainer Properties */
		
		final Date prosecutorReceivedDate = this.parseDateText("01/02/2003");
		final InterstateAgreementInitiatedByCategory initiatedBy =
				InterstateAgreementInitiatedByCategory.PROSECUTOR;
		
		/* Interstate Agreement Activity Properties */
		
		final Date activityDate = this.parseDateText("02/28/2008");
		final Direction direction = Direction.RECEIVED;
		
		DetainerActivityCategory category =
				this.detainerActivityCategoryInstanceFactory.createInstance();
		
		category.setDescription("ActivityCategory");
		category.setName("ActivityCategoryName");
		category.setReceivable(true);
		category.setSendable(false);
		
		category = this.detainerActivityCategoryDao
				.makePersistent(category);
		
		/* Document Properties */
		
		final String fileName = "file";
		final String fileExtension = ".doc";
		final String fileTitle = "DocumentTitle";
		final String tagName = "tag";
		
		final Date fileDate = this.parseDateText("03/28/2008");
		
		/* Detainer Warrant Processing Status Properties */
		
		final Date sentToFacilityDate = this.parseDateText("02/01/2004");
		final Date inmateServedDate = this.parseDateText("02/22/2004");
		final Date cancellationDate = this.parseDateText("03/16/2004");
		
		final Boolean refusedToSign = true;
		final Boolean waiverRequired = false;
		
		final String refusedToSignComment = "Refused to sign!";
		final String waiverRequiredComment = "Waiver required.";
		final Long telephoneNumber2 = new Long("1234567");
		final DetainerWarrantCancellationReason detainerWarrantCancellationReason =
				DetainerWarrantCancellationReason.DROPPED_BY_AGENCY;
		
		final Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
				
		Facility facility = this.facilityDelegate
				.create(location, "FACILITY", "FAC", true, telephoneNumber2);
		
		Complex complex = this.complexDelegate
				.create("COMPLEX", "COM", true, facility);
		
		Unit unit = this.unitDelegate.create("UNIT", "UN", true, facility,
				complex);
		
		String facilityName = "facilityName";
		
		/* Detainer Service Create Methods */
		
		final Detainer detainer = this.detainerService.create(
				offender, alternateOffenderId, offenseDescription,
				courtCaseNumber, detainerType, detainerAgency,
				jurisdiction, warrantReceivedDate,
				warrantIssuedDate, warrantNumber);
		
		final InterstateAgreementDetainer interstateAgreementDetainer = 
				this.detainerService.createInterstateAgreementDetainer(
						detainer, prosecutorReceivedDate, initiatedBy);
		
		final Document document = this.detainerService.createDocument(
				fileDate, fileName, fileExtension, fileTitle);
		final DocumentTag documentTag = this.detainerService.createDocumentTag(
				document, tagName);
		final InterstateDetainerActivity interstateDetainerActivity =
				this.detainerService.createDetainerActivity(
						interstateAgreementDetainer, activityDate, direction,
						document, category);
		
		final DetainerWarrantProcessingStatus detainerWarrantProcessingStatus =
				this.detainerService.createDetainerWarrantProcessingStatus(
						detainer, sentToFacilityDate, facility, unit, complex, inmateServedDate,
						refusedToSign, waiverRequired, cancellationDate,
						detainerWarrantCancellationReason,
						refusedToSignComment, waiverRequiredComment,
						facilityName);
		
		
		/* Assert Detainer Agency Properties */
		assert agencyName.equals(detainerAgency.getAgencyName())
		: String.format("Wrong agencyName for detainerAgency: %s found; %s expected",
				detainerAgency.getAgencyName(), agencyName);
		assert telephoneNumber.equals(detainerAgency.getTelephoneNumber())
		: String.format("Wrong telephoneNumber for detainerAgency: "
				+ "%s found; %s expected",
				detainerAgency.getTelephoneNumber(), telephoneNumber);
		assert address.equals(detainerAgency.getAddress())
		: String.format("Wrong address for detainerAgency: %d found; %d expected",
				detainerAgency.getAddress().getId(), address.getId());
		assert valid.equals(detainerAgency.isValid())
		: String.format("Wrong valid for detainerAgency: %s found; %s expected",
				detainerAgency.isValid(), valid);
		
		/* Assert Address Properties */
		assert value.equals(address.getValue())
		: String.format("Wrong value for address: %s found; %s expected",
				address.getValue(), value);
		assert zipCode.equals(address.getZipCode())
		: String.format("Wrong zipCode for address: %s found; %s expected",
				address.getZipCode().getId(), zipCode.getId());
		assert city.equals(zipCode.getCity())
		: String.format("Wrong city for zipCode: %s found; %s expected",
				zipCode.getCity().getName(), city.getName());
		assert zipCodeValue.equals(zipCode.getValue())
		: String.format("Wrong zipCodeValue for zipCode: %s found; %s expected",
				zipCode.getValue(), zipCodeValue);
		assert zipCodeExtension.equals(zipCode.getExtension())
		: String.format("Wrong zipCodeExtension for zipCode: %s found; %s expected",
				zipCode.getExtension(), zipCodeExtension);
		assert active.equals(zipCode.getValid())
		: String.format("Wrong active for zipCode: %s found; %s expected",
				zipCode.getValid(), active);
		
		/* Assert Detainer Properties */
		assert offender.equals(detainer.getOffender())
		: String.format("Wrong offender for Detainer: #%d found; #%d expected",
				detainer.getOffender().getId(), offender.getId());
		assert alternateOffenderId.equals(detainer.getAlternateOffenderId())
		: String.format("Wrong alternate offender id for Detainer: %s found;"
				+ " %s expected",
				detainer.getAlternateOffenderId(), alternateOffenderId);
		assert offenseDescription.equals(detainer.getOffenseDescription())
		: String.format("Wrong offense description for Detainer: %s found;"
				+ " %s expected",
				detainer.getOffenseDescription(), offenseDescription);
		assert courtCaseNumber.equals(detainer.getCourtCaseNumber())
		: String.format("Wrong court case number for Detainer: %s found;"
				+ " %s expected",
				detainer.getCourtCaseNumber(), courtCaseNumber);
		assert warrantNumber.equals(detainer.getWarrantNumber())
		: String.format("Wrong court case number for Detainer: %s found;"
				+ " %s expected",
				detainer.getWarrantNumber(), warrantNumber);
		assert warrantReceivedDate.equals(
				detainer.getReceiveDate())
		: String.format("Wrong warrantReceivedDate for detainer: "
				+ "%s found; %s expected",
				detainer.getReceiveDate(),
				warrantReceivedDate);
		assert warrantIssuedDate.equals(
				detainer.getIssueDate())
		: String.format("Wrong warrantIssuedDate for detainer: %s found; %s expected",
				detainer.getIssueDate(), warrantIssuedDate);
		assert jurisdiction.equals(detainer.getJurisdiction())
		: String.format("Wrong jurisdiction for detainer: %s found; %s expected",
				detainer.getJurisdiction(), jurisdiction);
		assert detainerType.equals(detainer.getDetainerType())
		: String.format("Wrong detainerType for detainer: %s found; %s expected",
				detainer.getDetainerType().getName(), detainerType.getName());
		assert detainerAgency.equals(detainer.getDetainerAgency())
		: String.format("Wrong detainerAgency for detainer: %s found; %s expected",
				detainer.getDetainerAgency().getAgencyName(),
				detainerAgency.getAgencyName());
		
		/* Assert Interstate Agreement Detainer Properties */
		assert detainer.equals(interstateAgreementDetainer.getDetainer())
		: String.format("Wrong detainer for interstateAgreementDetainer: "
				+ "%d found; %d expected",
				interstateAgreementDetainer.getDetainer().getId(),
				detainer.getId());
		assert prosecutorReceivedDate.equals(
				interstateAgreementDetainer.getProsecutorReceivedDate())
		: String.format("Wrong prosecutorReceivedDate for "
				+ "interstateAgreementDetainer: %s found; %s expected",
				interstateAgreementDetainer.getProsecutorReceivedDate(),
				prosecutorReceivedDate);
		assert initiatedBy.equals(interstateAgreementDetainer.getInitiatedBy())
		: String.format("Wrong initiatedBy for interstateAgreementDetainer: "
				+ "%s found; %s expected",
				interstateAgreementDetainer.getInitiatedBy(), initiatedBy);
		
		/* Assert Document Properties */
		assert fileName.equals(document.getFilename())
		: String.format("Wrong fileName for document: %s found; %s expected",
				document.getFilename(), fileName);
		assert fileExtension.equals(document.getFileExtension())
		: String.format("Wrong fileExtension for document: %s found; %s expected",
				document.getFileExtension(), fileExtension);
		assert fileTitle.equals(document.getTitle())
		: String.format("Wrong fileTitle for document: %s found; %s expected",
				document.getTitle(), fileTitle);
		assert fileDate.equals(document.getDate())
		: String.format("Wrong fileDate for document: %s found; %s expected",
				document.getDate(), fileDate);
		
		/* Assert Document Tag Properties */
		assert tagName.equals(documentTag.getName())
		: String.format("Wrong tagName for documentTag: %s found; %s expected",
				documentTag.getName(), tagName);
		assert document.equals(documentTag.getDocument())
		: String.format("Wrong document for documentTag: %d found; %d expected",
				documentTag.getDocument().getId(), document.getId());
		
		/* Assert Interstate Agreement Activity Properties */
		assert activityDate.equals(interstateDetainerActivity.getActivityDate())
		: String.format("Wrong activityDate for interstateDetainerActivity: "
				+ "%s found; %s expected",
				interstateDetainerActivity.getActivityDate(), activityDate);
		assert direction.equals(interstateDetainerActivity.getDirection())
		: String.format("Wrong direction for interstateDetainerActivity: "
				+ "%s found; %s expected",
				interstateDetainerActivity.getDirection(), direction);
		assert document.equals(interstateDetainerActivity.getDocument())
		: String.format("Wrong document for interstateDetainerActivity: "
				+ "%d found; %d expected",
				interstateDetainerActivity.getDocument().getId(), document.getId());
		assert interstateAgreementDetainer.equals(
				interstateDetainerActivity.getInterstateAgreementDetainer())
		: String.format("Wrong interstateAgreementDetainer for "
				+ "interstateDetainerActivity: %d found; %d expected",
				interstateDetainerActivity
				.getInterstateAgreementDetainer().getId(),
				interstateAgreementDetainer.getId());
		assert category.equals(interstateDetainerActivity.getCategory())
		: String.format("Wrong category for interstateDetainerActivity: "
				+ "%s found; %s expected",
				interstateDetainerActivity.getCategory().getName(),
				category.getName());
	
		/* Assert Detainer Warrant Processing Status Properties */
		assert detainer.equals(detainerWarrantProcessingStatus.getDetainer())
		: String.format("Wrong detainer for detainerWarrantProcessingStatus:"
				+ " %d found; %d expected",
				detainerWarrantProcessingStatus.getDetainer().getId(),
				detainer.getId());
		assert sentToFacilityDate.equals(
				detainerWarrantProcessingStatus.getSentToFacilityDate())
		: String.format("Wrong sentToFacilityDate for "
				+ "detainerWarrantProcessingStatus: %s found; %s expected",
				detainerWarrantProcessingStatus.getSentToFacilityDate(),
				sentToFacilityDate);
		assert inmateServedDate.equals(
				detainerWarrantProcessingStatus.getInmateServedDate())
		: String.format("Wrong inmateServedDate for "
				+ "detainerWarrantProcessingStatus: %s found; %s expected",
				detainerWarrantProcessingStatus.getInmateServedDate(),
				inmateServedDate);
		assert cancellationDate.equals
		(detainerWarrantProcessingStatus.getCancellation().getDate())
		: String.format("Wrong cancellationDate for "
				+ "detainerWarrantProcessingStatus: %s found; %s expected",
				detainerWarrantProcessingStatus.getCancellation().getDate(),
				cancellationDate);
		assert detainerWarrantCancellationReason.equals(
				detainerWarrantProcessingStatus.getCancellation().getReason())
		: String.format("Wrong detainerWarrantCancellationReason for "
				+ "detainerWarrantProcessingStatus: %s found; %s expected",
				detainerWarrantProcessingStatus.getCancellation().getReason(),
				detainerWarrantCancellationReason);
		assert refusedToSign.equals(
				detainerWarrantProcessingStatus.getRefusedToSign())
		: String.format("Wrong refusedToSign for detainerWarrantProcessingStatus:"
				+ " %s found; %s expected",
				detainerWarrantProcessingStatus.getRefusedToSign(), refusedToSign);
		assert waiverRequired.equals(
				detainerWarrantProcessingStatus.getWaiverRequired())
		: String.format("Wrong waiverRequired for detainerWarrantProcessingStatus: "
				+ "%s found; %s expected",
				detainerWarrantProcessingStatus.getWaiverRequired(),
				waiverRequired);
		assert refusedToSignComment.equals(
				detainerWarrantProcessingStatus.getRefusedToSignComment())
		: String.format("Mismatched refused to sign comment:"
				+ "%s found; %s expected",
				detainerWarrantProcessingStatus.getRefusedToSignComment(),
				refusedToSignComment);
		assert waiverRequiredComment.equals(
				detainerWarrantProcessingStatus.getWaiverRequiredComment())
		: String.format("Mismatched waiver required comment:"
				+ "%s found; %s expected",
				detainerWarrantProcessingStatus.getWaiverRequiredComment(),
				waiverRequiredComment);
		assert unit.equals(detainerWarrantProcessingStatus.getUnit())
		: String.format("Wrong unit for detainerWarrantProcessingStatus: "
				+ "%s found; %s expected",
				detainerWarrantProcessingStatus.getUnit(), unit);
		assert facility.equals(detainerWarrantProcessingStatus.getFacility())
		: String.format("Wrong facility for detainerWarrantProcessingStatus: "
				+ "%s found; %s expected",
				detainerWarrantProcessingStatus.getFacility().getName(),
				facility.getName());
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
}
