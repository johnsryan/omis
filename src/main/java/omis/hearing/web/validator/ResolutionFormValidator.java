package omis.hearing.web.validator;

import java.util.Date;
import java.util.EnumSet;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.web.form.ItemOperation;
import omis.hearing.web.form.ResolutionForm;
import omis.hearing.web.form.UserAttendanceItem;

/**
 * Resolution Form Validator.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.4 (Jun 5, 2018)
 * @since OMIS 3.0
 */
public class ResolutionFormValidator implements Validator {
	
	private static final String CATEGORY_REQUIRED_MSG_KEY = "category.empty";
	
	private static final String STATUS_DESCRIPTION_REQUIRED_MSG_KEY =
			"description.required";
	
	private static final String ADJUDICATE_IN_FUTURE_MSG_KEY =
			"hearingStatus.adjudicate.inFuture";
	
	private static final String DISPOSITION_REQUIRED_MSG_KEY =
			"resolution.disposition.empty";
	
	private static final String SANCTION_REQUIRED_MESSAGE_KEY =
			"imposedSanction.sanction.empty";
	
	private static final String SANCTION_ONLY_ON_GUILTY_MSG_KEY =
			"imposedSanction.sanction.notOnGuilty";
	
	private static final String DECISION_REQUIRED_MSG_KEY =
			"resolution.decision.empty";
	
	private static final String REASON_REQUIRED_MSG_KEY =
			"resolution.reason.empty";
	
	private static final String DATE_REQUIRED_MSG_KEY = "date.empty";

	private static final String DECIDED_BY_REQUIRED_MSG_KEY =
			"resolution.authority.empty";
	
	private static final String OFFENDER_ATTENDANCE_FUTURE_MSG_KEY =
			"offenderAttendance.future";
	
	private static final String USER_ACCOUNT_REQUIRED_MSG_KEY = 
			"userAccount.required";
	
	private static final String USER_ATTENDANCE_EMPTY_IF_HELD_MSG_KEY =
			"userAttendance.emptyIfHeld";

	/*private static final String ADJUSTED_CONDITION_REQUIRED_MSG_KEY =
			"resolution.adjustedCondition.empty";

	private static final String ADJUSTED_DISCIPLINARY_CODE_REQUIRED_MSG_KEY =
			"resolution.adjustedDisciplinaryCode.empty";*/
	
	private ResolutionClassificationCategory resolutionCategory;

	/**{@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return ResolutionForm.class.isAssignableFrom(clazz);
	}
	
	/**
	 * @param target target
	 * @param errors errors
	 * @param resolutionCategory resolution category
	 */
	public void validate(final Object target, final Errors errors,
			final ResolutionClassificationCategory resolutionCategory) {
		this.resolutionCategory = resolutionCategory;
		this.validate(target, errors);
	}
	
	/**{@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		ResolutionForm form = (ResolutionForm) target;
		if (ResolutionClassificationCategory.FORMAL.equals(
				this.resolutionCategory)) {
			//validate hearing status properties
			ValidationUtils.rejectIfEmpty(errors, "date",
					DATE_REQUIRED_MSG_KEY);
			ValidationUtils.rejectIfEmpty(errors, "category",
					CATEGORY_REQUIRED_MSG_KEY);
			if (EnumSet.of(HearingStatusCategory.UPHELD,
					HearingStatusCategory.MODIFIED).contains(
							form.getCategory())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
					"statusDescription", STATUS_DESCRIPTION_REQUIRED_MSG_KEY);
			}
			if (form.getDate() != null) {
				if (form.getDate().getTime() > new Date().getTime()) {
					errors.rejectValue("date",
							ADJUDICATE_IN_FUTURE_MSG_KEY);
					if (form.getInAttendance()) {
						errors.rejectValue("inAttendance",
								OFFENDER_ATTENDANCE_FUTURE_MSG_KEY);
					}
					if (!form.getUserAttendanceItems().isEmpty()) {
						errors.rejectValue("userAttendanceItems",
								USER_ATTENDANCE_EMPTY_IF_HELD_MSG_KEY);
					}
				}
			}
			if (form.getUserAttendanceItems() != null) {
				int i = 0;
				for (UserAttendanceItem item
						: form.getUserAttendanceItems()) {
					if (ItemOperation.CREATE.equals(item.getItemOperation())
						|| ItemOperation.UPDATE.equals(
								item.getItemOperation())) {
						if (item.getUserAccount() == null) {
							errors.rejectValue(
								"userAttendanceItems[" + i + "].userAccount",
								USER_ACCOUNT_REQUIRED_MSG_KEY);
						}
					}
					i++;
				}
			}
		}
		for (int i = 0; i < form.getViolationItems().size(); i++) {
			switch (this.resolutionCategory) {
				case FORMAL:
					ValidationUtils.rejectIfEmpty(errors,
							"violationItems[" + i + "].disposition",
							DISPOSITION_REQUIRED_MSG_KEY);
					if (!(DispositionCategory.GUILTY.equals(
							form.getViolationItems().get(i).getDisposition())
							|| DispositionCategory.REDUCED.equals(
							form.getViolationItems().get(i).getDisposition()))
							&& (form.getViolationItems().get(i).getSanction()
									!= null
							&& !form.getViolationItems().get(i).getSanction()
							.equals(""))) {
						errors.rejectValue("violationItems[" + i + "].sanction",
								SANCTION_ONLY_ON_GUILTY_MSG_KEY);
					}
					/*if (form.getViolationItems().get(i).getAdjusted() != null
							&& form.getViolationItems().get(i).getAdjusted()) {
						if (form.getViolationItems().get(i)
								.getConditionViolation() != null) {
							ValidationUtils.rejectIfEmpty(errors,
									"violationItems[" + i + "]"
											+ ".adjustedCondition",
									ADJUSTED_CONDITION_REQUIRED_MSG_KEY);
						} else if (form.getViolationItems().get(i)
								.getDisciplinaryCodeViolation() != null) {
							ValidationUtils.rejectIfEmpty(errors,
									"violationItems[" + i + "]"
											+ ".adjustedDisciplinaryCode",
								ADJUSTED_DISCIPLINARY_CODE_REQUIRED_MSG_KEY);
						}
					}*/
				case INFORMAL:
					if ((ResolutionClassificationCategory.FORMAL.equals(
							this.resolutionCategory)
							&& (DispositionCategory.GUILTY.equals(
							form.getViolationItems().get(i).getDisposition())
							|| DispositionCategory.REDUCED.equals(
							form.getViolationItems().get(i).getDisposition())))
							|| ResolutionClassificationCategory.INFORMAL.equals(
							this.resolutionCategory)) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors,
							"violationItems[" + i + "].sanction",
							SANCTION_REQUIRED_MESSAGE_KEY);
					}
				case DISMISSED:
					if (!(ResolutionClassificationCategory.FORMAL.equals(
							this.resolutionCategory))) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors,
								"violationItems[" + i + "].decision",
								DECISION_REQUIRED_MSG_KEY);
						ValidationUtils.rejectIfEmpty(errors,
								"violationItems[" + i + "].date",
								DATE_REQUIRED_MSG_KEY);
						ValidationUtils.rejectIfEmpty(errors,
								"violationItems[" + i + "].authority",
								DECIDED_BY_REQUIRED_MSG_KEY);
					}
					ValidationUtils.rejectIfEmptyOrWhitespace(errors,
							"violationItems[" + i + "].reason",
							REASON_REQUIRED_MSG_KEY);
					break;
				default:
					throw new UnsupportedOperationException(
							"Resolution Category is not supported.");
			}
			if (form.getGroupEdit() != null && form.getGroupEdit()) {
				break;
			}
		}
	}
}
