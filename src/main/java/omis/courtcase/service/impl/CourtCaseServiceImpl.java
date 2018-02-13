/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.courtcase.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.conviction.service.delegate.ConvictionDelegate;
import omis.court.domain.Court;
import omis.court.service.delegate.CourtDelegate;
import omis.courtcase.domain.Charge;
import omis.courtcase.domain.CourtCase;
import omis.courtcase.domain.CourtCaseNote;
import omis.courtcase.domain.JurisdictionAuthority;
import omis.courtcase.domain.OffenderDangerDesignator;
import omis.courtcase.domain.component.CourtCaseFlags;
import omis.courtcase.domain.component.CourtCasePersonnel;
import omis.courtcase.exception.ChargeExistsException;
import omis.courtcase.exception.CourtCaseExistsException;
import omis.courtcase.exception.CourtCaseNoteExistsException;
import omis.courtcase.service.CourtCaseService;
import omis.courtcase.service.delegate.ChargeDelegate;
import omis.courtcase.service.delegate.CourtCaseDelegate;
import omis.courtcase.service.delegate.CourtCaseNoteDelegate;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.docket.service.delegate.DocketDelegate;
import omis.judge.service.delegate.JudgeTermDelegate;
import omis.offender.domain.Offender;
import omis.offense.domain.Offense;
import omis.offense.service.delegate.OffenseDelegate;
import omis.person.domain.Person;
import omis.region.domain.State;
import omis.region.service.delegate.StateDelegate;

/**
 * Implementation of service for court cases.
 * 
 * @author Stephen Abson
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.6 (Feb 6, 2018)
 * @since OMIS 3.0
 */
public class CourtCaseServiceImpl
		implements CourtCaseService {

	/* Delegates. */
	private final CourtCaseDelegate courtCaseDelegate;
	private final CourtDelegate courtDelegate;
	private final ChargeDelegate chargeDelegate;
	private final CourtCaseNoteDelegate courtCaseNoteDelegate;
	private final ConvictionDelegate convictionDelegate;
	private final StateDelegate stateDelegate;
	private final DocketDelegate docketDelegate;
	private final OffenseDelegate offenseDelegate;
	private final JudgeTermDelegate judgeTermDelegate;
	
	/**
	 * IMplementation of service for court cases.
	 * 
	 * @param courtCaseDelegate - court case delegate.
	 * @param courtDelegate - court delegate.
	 * @param chargeDelegate delegate for charges
	 * @param courtJudgeAssignmentDelegate - court judge assignment delegate.
	 * @param courtCaseNoteDelegate delegate for court case notes
	 * @param convictionDelegate delegate for convictions
	 * @param stateDelegate delegate for states
	 * @param docketDelegate delegate for dockets
	 * @param offenseDelegate delegate for offenses
	 */
	public CourtCaseServiceImpl(
			final CourtCaseDelegate courtCaseDelegate,
			final CourtDelegate courtDelegate,
			final ChargeDelegate chargeDelegate,
			final CourtCaseNoteDelegate courtCaseNoteDelegate,
			final ConvictionDelegate convictionDelegate,
			final StateDelegate stateDelegate,
			final DocketDelegate docketDelegate,
			final OffenseDelegate offenseDelegate,
			final JudgeTermDelegate judgeTermDelegate) {
		this.courtCaseDelegate = courtCaseDelegate;
		this.courtDelegate = courtDelegate;
		this.chargeDelegate = chargeDelegate;
		this.courtCaseNoteDelegate = courtCaseNoteDelegate;
		this.convictionDelegate = convictionDelegate;
		this.stateDelegate = stateDelegate;
		this.docketDelegate = docketDelegate;
		this.offenseDelegate = offenseDelegate;
		this.judgeTermDelegate = judgeTermDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public CourtCase create(
			final Person offender, final Court court, final String docket,
			final String interStateNumber, final State interState,
			final Date pronouncementDate, 
			final JurisdictionAuthority jurisdictionAuthority, 
			final Date sentenceReviewDate,
			final OffenderDangerDesignator dangerDesignator,
			final CourtCasePersonnel courtCasePersonnel,
			final CourtCaseFlags courtCaseFlags, final String comments, 
			final Offense chargeOffense, final Date chargeDate, 
			final Date chargeFileDate, final Integer chargeCount)
					throws DocketExistsException {
		
		Docket newDocket = this.docketDelegate.create(offender, court, docket);
		final CourtCase courtCase;
		try {
		courtCase = this.courtCaseDelegate.create(newDocket, 
				interStateNumber, interState, pronouncementDate, 
				jurisdictionAuthority, sentenceReviewDate, dangerDesignator, 
				courtCasePersonnel, courtCaseFlags, comments);
		} catch (CourtCaseExistsException ccee) {
			throw new RuntimeException("Court case already exists");
		}
		try {
			this.chargeDelegate.create(courtCase, chargeOffense,	
					chargeDate, chargeFileDate, chargeCount);
		} catch (ChargeExistsException defe) {
			throw new RuntimeException(
					"Duplicate charge found for new court case");
		}
		return courtCase;
	}

	/** {@inheritDoc} */
	@Override
	public CourtCase createFromDocket(final Docket docket, 
			final String interStateNumber, final State interState, 
			final Date pronouncementDate, 
			final JurisdictionAuthority jurisdictionAuthority, 
			final Date sentenceReviewDate, 
			final OffenderDangerDesignator dangerDesignator, 
			final Offense chargeOffense, final Date chargeDate,
			final Date chargeFileDate, final Integer chargeCount, 
			final CourtCasePersonnel personnel, final CourtCaseFlags flags,
			final String comments) throws CourtCaseExistsException {
		final CourtCase courtCase = this.courtCaseDelegate.create(docket, 
				interStateNumber, interState, pronouncementDate, 
				jurisdictionAuthority, sentenceReviewDate, dangerDesignator, 
				personnel, flags, comments);
		try {
			this.chargeDelegate.create(courtCase, chargeOffense,	
					chargeDate, chargeFileDate, chargeCount);
		} catch (ChargeExistsException defe) {
			throw new RuntimeException(
					"Duplicate charge found for new court case");
		}
		return courtCase;
	}
	
	/** {@inheritDoc} */
	@Override
	public CourtCase update(
			final CourtCase courtCase, final String interStateNumber, 
			final State interState, final Date pronouncementDate, 
			final JurisdictionAuthority jurisdictionAuthority, 
			final Date sentenceReviewDate,
			final OffenderDangerDesignator dangerDesignator,
			final CourtCasePersonnel courtCasePersonnel,
			final CourtCaseFlags courtCaseFlags, final String comments) {
		return this.courtCaseDelegate.update(courtCase, interStateNumber, 
				interState, pronouncementDate, jurisdictionAuthority, 
				sentenceReviewDate, dangerDesignator, courtCasePersonnel, 
				courtCaseFlags, comments);
	}
	
	/** {@inheritDoc} */
	@Override
	public void remove(final CourtCase courtCase) {
		throw new UnsupportedOperationException(
				"Removal of court cases not supported.");
	}

	/** {@inheritDoc} */
	@Override
	public Charge createCharge(
			final CourtCase courtCase, final Offense offense, final Date date,
			final Date fileDate, final Integer count)
					throws ChargeExistsException {
		return this.chargeDelegate.create(courtCase, offense, date,
				fileDate, count);
	}
	
	
	/** {@inheritDoc} */
	@Override
	public Charge updateCharge(final Charge charge, final Offense offense, 
			final Date date, final Date fileDate, final Integer count) 
					throws ChargeExistsException {
		return this.chargeDelegate.update(charge, offense, date, fileDate, 
				count);
	}
	
	/** {@inhertiDoc} */
	@Override
	public void removeCharge(final Charge charge) {
		this.chargeDelegate.remove(charge);
	}
	

	/** {@inheritDoc} */
	@Override
	public List<Court> findCourts() {
		return this.courtDelegate.findAllCourts();
	}

	/** {@inheritDoc} */
	@Override
	public List<Court> findCourtsByState(final State state) {
		// TODO: Implement find courts by State - SA
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public List<Offense> findOffenses(final String query) {
		return this.offenseDelegate.findByQuery(query);
	}

	/** {@inheritDoc} */
	@Deprecated
	public List<CourtCase> findByDefendant(final Offender offender) {
		throw new UnsupportedOperationException("Offender is deprecated and "
				+ "will be removed.");
	}

	/** {@inheritDoc} */
	@Override
	public List<CourtCase> findByDefendant(final Person defendant) {
		return this.courtCaseDelegate.findByDefendant(defendant);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Charge> findCharges(final CourtCase courtCase) {
		return this.chargeDelegate.findByCourtCase(courtCase);
	}

	/** {@inheritDoc} */
	@Override
	public CourtCase dismiss(final CourtCase courtCase) {
		return this.courtCaseDelegate.dismiss(courtCase);
	}

	/** {@inheritDoc} */
	@Override
	public CourtCaseNote addNote(final CourtCase courtCase, final Date date, 
			final String value) throws CourtCaseNoteExistsException {
		return this.courtCaseNoteDelegate.addNote(courtCase, date, value);
	}

	/** {@inheritDoc} */
	@Override
	public CourtCaseNote updateNote(final CourtCaseNote courtCaseNote, 
			final Date date, final String value) 
					throws CourtCaseNoteExistsException {
		return this.courtCaseNoteDelegate.updateNote(courtCaseNote, date, 
				value);
	}

	/** {@inheritDoc} */
	@Override
	public void removeNote(final CourtCaseNote courtCaseNote) {
		this.courtCaseNoteDelegate.removeNote(courtCaseNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<CourtCaseNote> findNotes(final CourtCase courtCase) {
		return this.courtCaseNoteDelegate.findNotes(courtCase);
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean hasConvictions(final CourtCase courtCase) {
		return this.convictionDelegate.hasConvictions(courtCase);
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStates() {
		return this.stateDelegate.findByCountry(
				this.stateDelegate.findHomeState().getCountry());
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Person> findJudges(String query, Date date) {
		return this.judgeTermDelegate.findJudgesOnDate(query, date);
	}

	/** {@inheritDoc} */
	@Override
	public CourtCase findByDocket(Docket docket) {
		return this.courtCaseDelegate.findByDocket(docket);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Docket> findDocketsWithoutCourtCase(final Person person) {
		List<Docket> dockets = this.docketDelegate.findByPerson(person);
		List<Docket> docketsWithoutCourtCase = new ArrayList<Docket>();
		for (Docket docket : dockets) {
			if (this.courtCaseDelegate.findByDocket(docket) == null) {
				docketsWithoutCourtCase.add(docket);
			}
		}
		return docketsWithoutCourtCase;
	}
}