package omis.paroleboarditinerary.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import omis.paroleboarditinerary.web.form.BoardMeetingSiteItem;
import omis.paroleboarditinerary.web.form.BoardMeetingSiteItemOperation;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryForm;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryNoteItem;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryNoteItemOperation;

/**
 * Validator for parole board itinerary.
 * 
 * @author Josh Divine
 * @author Annie Wahl 
 * @version 0.1.2 (Apr 11, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryFormValidator implements Validator {

	/**
	 * Instantiates a default parole board itinerary form validator.
	 */
	public ParoleBoardItineraryFormValidator() {
		// Default instantiation
	} 
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ParoleBoardItineraryForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ParoleBoardItineraryForm form = (ParoleBoardItineraryForm) target;
		
		if (form.getStartDate() == null) {
			errors.rejectValue("startDate", 
					"paroleBoardItinerary.dateRange.startDate.empty");
		}
		if (form.getEndDate() == null) {
			errors.rejectValue("endDate", 
					"paroleBoardItinerary.dateRange.endDate.empty");
		}
		if (form.getStartDate() != null && form.getEndDate() != null
				&& form.getStartDate().getTime()
					> form.getEndDate().getTime()) {
			errors.rejectValue("startDate",
					"paroleBoardItinerary.dateRange"
					+ ".startDateGreaterThanEndDate");
		}
		if (form.getBoardMember1() == null) {
			errors.rejectValue("boardMember1", 
					"paroleBoardItinerary.boardMember.empty");
		}
		if (form.getBoardMember2() == null) {
			errors.rejectValue("boardMember2", 
					"paroleBoardItinerary.boardMember.empty");
		} else {
			if (form.getBoardMember1() != null
					&& form.getBoardMember2().equals(form.getBoardMember1())) {
				errors.rejectValue("boardMember2", 
						"paroleBoardItinerary.boardMember.notUnique");
			}
		}
		if (form.getBoardMember3() == null) {
			errors.rejectValue("boardMember3", 
					"paroleBoardItinerary.boardMember.empty");
		} else {
			if (form.getBoardMember1() != null
					&& form.getBoardMember3().equals(form.getBoardMember1())) {
				errors.rejectValue("boardMember3", 
						"paroleBoardItinerary.boardMember.notUnique");
			} else if (form.getBoardMember2() != null
					&& form.getBoardMember3().equals(form.getBoardMember2())) {
				errors.rejectValue("boardMember3", 
						"paroleBoardItinerary.boardMember.notUnique");
			}
		}
		if (form.getBoardMemberAlternate() == null) {
			errors.rejectValue("boardMemberAlternate", 
					"paroleBoardItinerary.boardMember.empty");
		} else {
			if (form.getBoardMember1() != null
					&& form.getBoardMemberAlternate().equals(
							form.getBoardMember1())) {
				errors.rejectValue("boardMemberAlternate", 
						"paroleBoardItinerary.boardMember.notUnique");
			} else if (form.getBoardMember2() != null
					&& form.getBoardMemberAlternate().equals(
							form.getBoardMember2())) {
				errors.rejectValue("boardMemberAlternate",
						"paroleBoardItinerary.boardMember.notUnique");
			} else if (form.getBoardMember3() != null
					&& form.getBoardMemberAlternate().equals(
							form.getBoardMember3())) {
				errors.rejectValue("boardMemberAlternate", 
						"paroleBoardItinerary.boardMember.notUnique");
			}
		}
		if (form.getBoardMeetingSiteItems() != null
				&& form.getBoardMeetingSiteItems().size() > 0) {
			for (int index = 0; index < form.getBoardMeetingSiteItems().size(); 
					index++) {
				BoardMeetingSiteItem siteItem = 
						form.getBoardMeetingSiteItems().get(index);
				if (BoardMeetingSiteItemOperation.CREATE.equals(
						siteItem.getOperation())
						|| BoardMeetingSiteItemOperation.EDIT.equals(
								siteItem.getOperation())) {
					if (siteItem.getDate() == null) {
						errors.rejectValue("boardMeetingSiteItems[" + index
								+ "].date",  "paroleBoardItinerary"
										+ ".boardMeetingSite.date.empty");
					} else {
						if (form.getStartDate() != null && 
								form.getEndDate() != null) {
							if (siteItem.getDate().before(form.getStartDate()) || 
									siteItem.getDate().after(form.getEndDate())) {
								errors.rejectValue("boardMeetingSiteItems[" + 
									index + "].date",  "paroleBoardItinerary"
										+ ".boardMeetingSite.date.outOfRange");
							}
						}
					}
				}
			}
		}
		if (form.getBoardItineraryNoteItems() != null
				&& form.getBoardItineraryNoteItems().size() > 0) {
			for (int index = 0; index
					< form.getBoardItineraryNoteItems().size(); index++) {
				ParoleBoardItineraryNoteItem noteItem = 
						form.getBoardItineraryNoteItems().get(index);
				if (ParoleBoardItineraryNoteItemOperation.CREATE.equals(
						noteItem.getOperation())
						|| ParoleBoardItineraryNoteItemOperation.EDIT.equals(
								noteItem.getOperation())) {
					if (noteItem.getDate() == null) {
						errors.rejectValue("boardItineraryNoteItems[" + index
								+ "].date", "paroleBoardItinerary"
										+ ".boardItineraryNote.date.empty");
					}
					if (noteItem.getValue() == null
							|| noteItem.getValue().isEmpty()) {
						errors.rejectValue("boardItineraryNoteItems[" + index
								+ "].value", "paroleBoardItinerary"
										+ ".boardItineraryNote.value.empty");
					}
				}
			}
		}
	}

}
