package omis.travelpermit.web.validator;
import java.util.List;

import omis.address.web.validator.delegate.AddressFieldsValidatorDelegate;
import omis.travelpermit.web.form.TravelPermitForm;
import omis.travelpermit.web.validator.delegate.TravelPermitNoteItemValidatorDelegate;
import omis.web.validator.StringLengthChecks;
import omis.workassignment.web.form.WorkAssignmentForm;
import omis.workassignment.web.form.WorkAssignmentNoteItem;
import omis.workassignment.web.form.WorkAssignmentNoteItemOperation;
import omis.workassignment.web.validator.delegate.WorkAssignmentNoteItemValidatorDelegate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for travel permit form.
 * 
 * @author Yidong Li
 * @version 0.1.1 (May 22, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitFormValidator implements Validator {
	private static final String PERIODICITY_PROPERTY_NAME 
	= "periodicity";
	private static final String PERIODICITY_EMPTY_ERROR_KEY
	= "TravelPermit.periodicity.empty";
	private static final String START_DATE_PROPERTY_NAME 
	= "startDate";
	private static final String START_DATE_EMPTY_ERROR_KEY
	= "TravelPermit.startdate.empty";
	private static final String TRIP_PURPOSE_PROPERTY_NAME
	= "tripPurpose";
	private static final String TRIP_PURPOSE_EMPTY_ERROR_KEY
	= "TravelPermit.startdate.empty";
	private static final String DESTINATION_NAME_PROPERTY_NAME
	= "name";
	private static final String DESTINATION_NAME_EMPTY_ERROR_KEY
	= "TravelPermit.name.empty";
	private static final String ISSUER_PROPERTY_NAME
	= "issuer";
	private static final String ISSUER_EMPTY_ERROR_KEY
	= "TravelPermit.issuer.empty";
	
	
	
	private static final String WORK_ASSIGNMENT_DATE_EMPTY_ERROR_KEY
		= "WorkAssignment.assignedDate.empty";
	private static final String ASSIGNED_DATE_PROPERTY_NAME = "assignmentDate";
	private static final String FENCE_RESTRICTION_EMPTY_ERROR_KEY
		= "WorkAssignment.fenceRestriction.empty";
	private static final String FENCE_RESTRICTION_PROPERTY_NAME 
		= "fenceRestriction";
	private static final String WORK_ASSIGNMENT_CATEGORY_EMPTY_ERROR_KEY
		= "WorkAssignment.category.empty";
	private static final String CATEGORY_PROPERTY_NAME 
		= "workAssignmentCategory";
	private static final String WORK_ASSIGNMENT_CHANGE_REASON_EMPTY_ERROR_KEY
		= "WorkAssignment.changeReason.empty";
	private static final String CHANGE_REASON_PROPERTY_NAME 
		= "workAssignmentChangeReason";
	private static final String COMMENTS_PROPERTY_NAME 
		= "comments";
	private static final String ASSIGNMENT_DATE_PROPERTY_NAME 
		= "assignmentDate";
	private static final String ASSIGNMENT_DATE_GREATER_THAN_EMPTY_ERROR_KEY
		= "workAssignmentForm.assignmentDate.assignmentDateGreaterThanTerminationDate";
//	private final TravelPermitNoteItemValidatorDelegate 
//	travelPermitNoteItemValidatorDelegate;
	private final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate;
	
	/** Instantiates a validator for travel permit form. */
	public TravelPermitFormValidator(
//		final TravelPermitNoteItemValidatorDelegate 
//		travelPermitNoteItemValidatorDelegate,
		final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate) {
//		this.travelPermitNoteItemValidatorDelegate 
//			= travelPermitNoteItemValidatorDelegate;
		this.addressFieldsValidatorDelegate = addressFieldsValidatorDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return TravelPermitForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		TravelPermitForm travelPermitForm
			= (TravelPermitForm) target;
		if (travelPermitForm.getPeriodicity() == null) {
			errors.rejectValue(PERIODICITY_PROPERTY_NAME,
			PERIODICITY_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getStartDate() == null) {
			errors.rejectValue(START_DATE_PROPERTY_NAME,
			START_DATE_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getTripPurpose().isEmpty()
			|| travelPermitForm.getTripPurpose().length()==0
			|| travelPermitForm.getTripPurpose() == null) {
			errors.rejectValue(TRIP_PURPOSE_PROPERTY_NAME,
			TRIP_PURPOSE_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getName().isEmpty()
			|| travelPermitForm.getName().length()==0
			|| travelPermitForm.getName() == null) {
			errors.rejectValue(DESTINATION_NAME_PROPERTY_NAME,
			DESTINATION_NAME_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getIssuer() == null) {
			errors.rejectValue(ISSUER_PROPERTY_NAME,
			ISSUER_EMPTY_ERROR_KEY);
		}
		
		if(travelPermitForm.getDestinationOption()
			.equalsIgnoreCase("Use Full Address")){
			this.addressFieldsValidatorDelegate.validateAddressFields(
				travelPermitForm.getAddressFields(), "addressFields", errors);
		}
		
		
		
		
		
		
		
		
		
		/*if (workAssignmentForm.getAssignmentDate() == null) {
			errors.rejectValue(ASSIGNED_DATE_PROPERTY_NAME,
			WORK_ASSIGNMENT_DATE_EMPTY_ERROR_KEY);
		} 
		if (workAssignmentForm.getFenceRestriction() == null) {
			errors.rejectValue(FENCE_RESTRICTION_PROPERTY_NAME, 
			FENCE_RESTRICTION_EMPTY_ERROR_KEY);
		}
		if (workAssignmentForm.getWorkAssignmentCategory() == null) {
			errors.rejectValue(CATEGORY_PROPERTY_NAME,
			WORK_ASSIGNMENT_CATEGORY_EMPTY_ERROR_KEY);
		}
		if (workAssignmentForm.getWorkAssignmentChangeReason() == null) {
			errors.rejectValue(CHANGE_REASON_PROPERTY_NAME, 
			WORK_ASSIGNMENT_CHANGE_REASON_EMPTY_ERROR_KEY);
		}
		this.stringLengthChecks.getVeryHugeCheck().check(
			COMMENTS_PROPERTY_NAME, workAssignmentForm.getComments(), errors);
		
		if (workAssignmentForm.getAssignmentDate() != null
			&& workAssignmentForm.getTerminationDate() != null
			&& workAssignmentForm.getAssignmentDate().getTime()
			> workAssignmentForm.getTerminationDate().getTime()) {
			errors.rejectValue(ASSIGNMENT_DATE_PROPERTY_NAME,
			ASSIGNMENT_DATE_GREATER_THAN_EMPTY_ERROR_KEY);
		}
			
		int index = 0;
		if(workAssignmentForm.getWorkAssignmentNoteItems()!=null){
			List<WorkAssignmentNoteItem> workAssignmentNoteItems 
				= workAssignmentForm.getWorkAssignmentNoteItems();
			for(WorkAssignmentNoteItem noteItem : workAssignmentNoteItems){
				if(noteItem.getOperation()!=null){
					if(noteItem.getOperation().equals(
						WorkAssignmentNoteItemOperation.UPDATE)||
						noteItem.getOperation().equals(
							WorkAssignmentNoteItemOperation.CREATE)){
						workAssignmentNoteItemValidatorDelegate.validate(noteItem, 
							index, errors);
					}
					index = index + 1;
				}
			}
		}*/
	}
}