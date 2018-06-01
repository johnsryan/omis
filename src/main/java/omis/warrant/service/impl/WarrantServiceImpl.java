package omis.warrant.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementCategory;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionClause;
import omis.condition.service.delegate.AgreementDelegate;
import omis.condition.service.delegate.ConditionDelegate;
import omis.courtcasecondition.domain.CourtCaseAgreement;
import omis.courtcasecondition.service.delegate.CourtCaseAgreementDelegate;
import omis.docket.domain.Docket;
import omis.jail.domain.Jail;
import omis.jail.service.delegate.JailDelegate;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;
import omis.warrant.domain.WarrantCancellation;
import omis.warrant.domain.WarrantCauseViolation;
import omis.warrant.domain.WarrantNote;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.exception.WarrantArrestExistsException;
import omis.warrant.exception.WarrantCauseViolationExistsException;
import omis.warrant.exception.WarrantExistsException;
import omis.warrant.exception.WarrantNoteExistsException;
import omis.warrant.service.WarrantService;
import omis.warrant.service.delegate.WarrantArrestDelegate;
import omis.warrant.service.delegate.WarrantCancellationDelegate;
import omis.warrant.service.delegate.WarrantCauseViolationDelegate;
import omis.warrant.service.delegate.WarrantDelegate;
import omis.warrant.service.delegate.WarrantNoteDelegate;
import omis.warrant.service.delegate.WarrantReleaseDelegate;

/**
 * Warrant service implementation.
 * 
 *@author Annie Jacques 
 *@author Joel Norris
 *@version 0.1.1 (April 4, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantServiceImpl implements WarrantService {
	
	private final WarrantDelegate warrantDelegate;
	private final WarrantCauseViolationDelegate warrantCauseViolationDelegate;
	private final WarrantNoteDelegate warrantNoteDelegate;
	private final WarrantArrestDelegate warrantArrestDelegate;
	private final WarrantCancellationDelegate warrantCancellationDelegate;
	private final WarrantReleaseDelegate warrantReleaseDelegate;
	private final JailDelegate jailDelegate;
	private final ConditionDelegate conditionDelegate;
	private final CourtCaseAgreementDelegate courtCaseAgreementDelegate;
	
	
	/**
	 * Instantiates a warrant service with the specified delegates.
	 * 
	 * @param warrantDelegate warrant delegate
	 * @param warrantCauseViolationDelegate warrant cause violation delegate
	 * @param warrantViolationDelegate warrant violation delegate
	 * @param warrantNoteDelegate warrant note delegate
	 * @param warrantArrestDelegate warrant arrest delegate
	 * @param warrantReasonCategoryDelegate warrant reason category delegate
	 * @param warrantCancellationDelegate warrant cancellation delegate
	 * @param warrantReleaseDelegate warrant release delegate
	 * @param jailDelegate jail delegate
	 * @param conditionDelegate condition delegate
	 * @param agreementDelegate agreement delegate
	 * @param courtCaseAgreementDelegate court case agreement delegate
	 */
	public WarrantServiceImpl(final WarrantDelegate warrantDelegate,
			final WarrantCauseViolationDelegate warrantCauseViolationDelegate,
			final WarrantNoteDelegate warrantNoteDelegate,
			final WarrantArrestDelegate warrantArrestDelegate,
			final WarrantCancellationDelegate warrantCancellationDelegate,
			final WarrantReleaseDelegate warrantReleaseDelegate,
			final JailDelegate jailDelegate,
			final ConditionDelegate conditionDelegate,
			final AgreementDelegate agreementDelegate,
			final CourtCaseAgreementDelegate courtCaseAgreementDelegate) {
		this.warrantDelegate = warrantDelegate;
		this.warrantCauseViolationDelegate = warrantCauseViolationDelegate;
		this.warrantNoteDelegate = warrantNoteDelegate;
		this.warrantArrestDelegate = warrantArrestDelegate;
		this.warrantCancellationDelegate = warrantCancellationDelegate;
		this.warrantReleaseDelegate = warrantReleaseDelegate;
		this.jailDelegate = jailDelegate;
		this.conditionDelegate = conditionDelegate;
		this.courtCaseAgreementDelegate = courtCaseAgreementDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public Warrant create(final Offender offender, final Date date,
			final String addressee, final Person issuedBy,
			final Boolean bondable, final BigDecimal bondRecommendation,
			final WarrantReasonCategory warrantReason, final Jail holdingJail)
					throws WarrantExistsException {
		return this.warrantDelegate.create(offender, date, addressee, issuedBy,
				bondable, bondRecommendation, warrantReason, holdingJail);
	}

	/**{@inheritDoc} */
	@Override
	public Warrant update(final Warrant warrant, final Date date,
			final String addressee, final Person issuedBy,
			final Boolean bondable, final BigDecimal bondRecommendation,
			final WarrantReasonCategory warrantReason, final Jail holdingJail)
					throws WarrantExistsException {
		return this.warrantDelegate.update(warrant, date, addressee, issuedBy,
				bondable, bondRecommendation, warrantReason, holdingJail);
	}

	/**{@inheritDoc} */
	@Override
	public void remove(final Warrant warrant) {
		List<WarrantCauseViolation> violations = this.warrantCauseViolationDelegate.findByWarrant(warrant);
		for (WarrantCauseViolation violation : violations) {
			this.warrantCauseViolationDelegate.remove(violation);
		}
		WarrantArrest arrest = this.warrantArrestDelegate.findByWarrant(warrant);
		if (arrest != null) {
			this.warrantArrestDelegate.remove(arrest);
		}
		WarrantCancellation cancellation = this.warrantCancellationDelegate.findByWarrant(warrant);
		if (cancellation != null) {
			this.warrantCancellationDelegate.remove(cancellation);
		}
		WarrantRelease release = this.warrantReleaseDelegate.findByWarrant(warrant);
		if (release != null) {
			this.warrantReleaseDelegate.remove(release);
		}
		this.warrantDelegate.remove(warrant);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantCauseViolation createWarrantCauseViolation(
			final Warrant warrant,
			final ConditionClause conditionClause, final String description)
					throws WarrantCauseViolationExistsException {
		return this.warrantCauseViolationDelegate.create(
				warrant, conditionClause, description);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantCauseViolation updateWarrantCauseViolation(
			final WarrantCauseViolation warrantCauseViolation,
			final ConditionClause conditionClause,
			final String description)
					throws WarrantCauseViolationExistsException {
		return this.warrantCauseViolationDelegate.update(
				warrantCauseViolation, conditionClause, description);
	}

	/**{@inheritDoc} */
	@Override
	public void removeWarrantCauseViolation(
			final WarrantCauseViolation warrantCauseViolation) {
		this.warrantCauseViolationDelegate.remove(warrantCauseViolation);
	}

	

	/**{@inheritDoc} */
	@Override
	public WarrantNote createWarrantNote(
			final Warrant warrant, final String note, final Date date)
					throws WarrantNoteExistsException {
		return this.warrantNoteDelegate.create(warrant, note, date);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantNote updateWarrantNote(
			final WarrantNote warrantNote, final String note, final Date date)
					throws WarrantNoteExistsException {
		return this.warrantNoteDelegate.update(warrantNote, note, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeWarrantNote(final WarrantNote warrantNote) {
		this.warrantNoteDelegate.remove(warrantNote);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantArrest createArrest(final Warrant warrant, final Date date,
			final Jail jail, final Date contactByDate)
					throws WarrantArrestExistsException {
		return this.warrantArrestDelegate.create(
				warrant, date, jail, contactByDate);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantArrest updateArrest(final WarrantArrest warrantArrest,
			final Date date, final Jail jail, final Date contactByDate)
					throws WarrantArrestExistsException {
		return this.warrantArrestDelegate.update(
				warrantArrest, date, jail, contactByDate);
	}

	/**{@inheritDoc} */
	@Override
	public void removeArrest(final WarrantArrest warrantArrest) {
		this.warrantArrestDelegate.remove(warrantArrest);
	}
	/**{@inheritDoc} */
	@Override
	public List<WarrantCauseViolation> findWarrantCauseViolationsByWarrant(
			final Warrant warrant) {
		return this.warrantCauseViolationDelegate.findByWarrant(warrant);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantArrest findWarrantArrestByWarrant(final Warrant warrant) {
		return this.warrantArrestDelegate.findByWarrant(warrant);
	}

	/**{@inheritDoc} */
	@Override
	public List<WarrantNote> findWarrantNotesByWarrant(final Warrant warrant) {
		return this.warrantNoteDelegate.findByWarrant(warrant);
	}

	/**{@inheritDoc} */
	@Override
	public List<Jail> findAllJails() {
		return this.jailDelegate.findAll();
	}


	/**{@inheritDoc} */
	@Override
	public List<ConditionClause> findUniqueConditionClausesByOffender(final Offender offender, final Date date) {
		List<ConditionClause> clauses = new ArrayList<ConditionClause>();
		List<Condition> conditions = this.conditionDelegate.findByOffenderAndEffectiveDate(offender, date);
		for (Condition condition : conditions) {
			if (AgreementCategory.COURT_CASE.equals(condition.getAgreement().getCategory())
					&& !clauses.contains(condition.getConditionClause())) {
				clauses.add(condition.getConditionClause());
			}
		}
		return clauses;
	}

	/**{@inheritDoc} */
	@Override
	public List<Docket> findDocketsByConditionClauseAndOffender(final ConditionClause clause, final Offender offender,
			final Date effectiveDate) {
		
		//Find all conditions for the condition clause and offender
		List<Condition> conditions = this.conditionDelegate
				.findByConditionClauseAndOffenderOnDate(clause, offender, effectiveDate);
		
		//Find all agreements via conditions
		List<Agreement> agreements = new ArrayList<Agreement>();
		for(Condition condition : conditions) {
			if(!agreements.contains(condition.getAgreement())) {
				agreements.add(condition.getAgreement());
			}
		}
		
		//Find all court case agreements
		List<CourtCaseAgreement> courtCaseAgreements
			= this.courtCaseAgreementDelegate.findByOffender(offender);
		
		//Dockets
		List<Docket> dockets = new ArrayList<Docket>();
		for(CourtCaseAgreement courtCaseAgreement : courtCaseAgreements) {
			for (Agreement agreement : agreements) {
				if (courtCaseAgreement.getAgreement() == agreement
						&& !dockets.contains(courtCaseAgreement.getDocket())) {
					dockets.add(courtCaseAgreement.getDocket());
				}
			}
		}
		return dockets;
	}
}
