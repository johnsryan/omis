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
package omis.courtcase.service;

import java.util.Date;
import java.util.List;

import omis.court.domain.Court;
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
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.offense.domain.Offense;
import omis.person.domain.Person;
import omis.region.domain.State;

/**
 * Service for court cases.
 * 
 * @author Stephen Abson
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.6 (Feb 6, 2018)
 * @since OMIS 3.0
 */
public interface CourtCaseService {
	
	/**
	 * Creates a court case.
	 * 
	 * @param defendant defendant
	 * @param court court
	 * @param docket docket
	 * @param interStateNumber inter state number
	 * @param interState inter state
	 * @param pronouncementDate pronouncement date
	 * @param jurisdictionAuthority jurisdiction authority
	 * @param sentenceReviewDate sentence review date
	 * @param dangerDesignator danger designator
	 * @param courtCasePersonnel court case personnel
	 * @param courtCaseFlags court case flags
	 * @param comments comments
	 * @param chargeOffense charge offense
	 * @param chargeDate charge date
	 * @param chargeFileDate charge file date
	 * @param chargeCount charge count
	 * @return court case
	 * @throws DuplicateEntityFoundException if court case exists
	 */
	CourtCase create(Person defendant, Court court, String docket,
			String interStateNumber, State interState, Date pronouncementDate, 
			JurisdictionAuthority jurisdictionAuthority, 
			Date sentenceReviewDate, 
			OffenderDangerDesignator dangerDesignator,
			CourtCasePersonnel courtCasePersonnel, 
			CourtCaseFlags courtCaseFlags, String comments, 
			Offense chargeOffense, Date chargeDate, Date chargeFileDate, 
			Integer chargeCount) throws DocketExistsException;
	
	/**
	 * Creates a court case from an existing docket.
	 * 
	 * @param docket docket
	 * @param interStateNumber inter state number
	 * @param interState inter state
	 * @param pronouncementDate pronouncement date
	 * @param jurisdictionAuthority jurisdiction authority
	 * @param sentenceReviewDate sentence review date
	 * @param dangerDesignator danger designator
	 * @param chargeOffense charge offense
	 * @param chargeDate charge date
	 * @param chargeFileDate charge file date
	 * @param chargeCount charge count
	 * @param personnel court case personnel
	 * @param flags court case flags
	 * @param comments comments
	 * @return court case
	 * @throws DuplicateEntityFoundException if court case exists
	 */
	CourtCase createFromDocket(Docket docket, String interStateNumber, 
			State interState, Date pronouncementDate, 
			JurisdictionAuthority jurisdictionAuthority, 
			Date sentenceReviewDate, 
			OffenderDangerDesignator dangerDesignator, 
			Offense chargeOffense, Date chargeDate, Date chargeFileDate, 
			Integer chargeCount, CourtCasePersonnel personnel, 
			CourtCaseFlags flags, String comments) 
					throws CourtCaseExistsException;
	
	/**
	 * Updates court case.
	 * 
	 * @param courtCase court case
	 * @param interStateNumber inter state number
	 * @param interState inter state
	 * @param pronouncementDate pronouncement date
	 * @param jurisdictionAuthority jurisdiction authority
	 * @param sentenceReviewDate sentence review date
	 * @param dangerDesignator danger designator
	 * @param courtCasePersonnel court case personnel
	 * @param courtCaseFlags court case flags
	 * @param comments comments
	 * @return updated court case
	 * @throws DuplicateEntityFoundException when court case exists.
	 */
	CourtCase update(CourtCase courtCase, String interStateNumber, 
			State interState, Date pronouncementDate, 
			JurisdictionAuthority jurisdictionAuthority, 
			Date sentenceReviewDate, 
			OffenderDangerDesignator dangerDesignator,
			CourtCasePersonnel courtCasePersonnel, 
			CourtCaseFlags courtCaseFlags,	String comments);
	
	/**
	 * Removes the specified court case.
	 * 
	 * @param courtCase court case
	 */
	void remove(CourtCase courtCase);
	
	/**
	 * Creates new charge.
	 * 
	 * @param courtCase court case
	 * @param offense offense
	 * @param date date
	 * @param fileDate file date
	 * @param count count
	 * @return new charge
	 * @throws DuplicateEntityFoundException if charge exists
	 */
	Charge createCharge(CourtCase courtCase, Offense offense, Date date,
			Date fileDate, Integer count)
				throws ChargeExistsException;
	
	/** 
	 * Updates charge.
	 * 
	 * @param charge charge
	 * @param offense offense
	 * @param date date
	 * @param fileDate file date
	 * @param count count
	 * @throws DuplicateEntityfoundException if charge exists. 
	 */
	Charge updateCharge(Charge charge, Offense offense, Date date, 
			Date fileDate, Integer count) 
					throws ChargeExistsException;

	/** 
	 * Removes charge.
	 * 
	 * @param charge - charge. 
	 */
	void removeCharge(Charge charge);
	
	/**
	 * Returns courts.
	 * 
	 * @return courts
	 */
	List<Court> findCourts();
	
	/**
	 * Returns courts by State.
	 * 
	 * @param state State
	 * @return courts by State
	 */
	List<Court> findCourtsByState(State state);
	
	/**
	 * Returns offenses.
	 * 
	 * @param query 
	 * @return offenses
	 */
	List<Offense> findOffenses(String query);
	
	/**
	 * Returns court cases by defendant.
	 * 
	 * @param defendant defendant
	 * @return court cases by defendant
	 */
	List<CourtCase> findByDefendant(Person defendant);

	/**
	 * Returns charges for court case.
	 * 
	 * @param courtCase court case
	 * @return charges for court case
	 */
	List<Charge> findCharges(CourtCase courtCase);
	
	/**
	 * Dismisses the specified court case.
	 * 
	 * @param courtCase court case
	 * @return court case
	 */
	CourtCase dismiss(CourtCase courtCase);
	
	/**
	 * Adds a note to the specified court case with the specified date and 
	 * value.
	 * 
	 * @param courtCase court case
	 * @param date date
	 * @param value value
	 * @return court case note
	 * @throws DuplicateEntityFoundException occurs when duplicate note is found
	 */
	CourtCaseNote addNote(CourtCase courtCase, Date date, String value) 
			throws CourtCaseNoteExistsException;
	
	/**
	 * Updates the specified note with the date and value
	 * @param courtCaseNote court case note
	 * @param date date
	 * @param value value
	 * @return court case note
	 * @throws DuplicateEntityFoundException occurs when duplicate note is found
	 */
	CourtCaseNote updateNote(CourtCaseNote courtCaseNote, Date date, 
			String value) throws CourtCaseNoteExistsException;
	
	/**
	 * Remove the specified note.
	 * 
	 * @param courtCaseNote court case note
	 */
	void removeNote(CourtCaseNote courtCaseNote);
	
	/**
	 * Find all notes for the specified court case.
	 * 
	 * @param courtCase court case
	 * @return list of notes
	 */
	List<CourtCaseNote> findNotes(CourtCase courtCase);
	
	/**
	 * Returns whether the specified court case has convictions.
	 * 
	 * @param courtCase court case
	 * @return whether the specified court case has convictions
	 */
	Boolean hasConvictions(CourtCase courtCase);
	
	/**
	 * Returns a list of states.
	 * 
	 * @return list of states.
	 */
	List<State> findStates();

	/**
	 * Returns judges for the specified date.
	 * 
	 * @param query judges name
	 * @param effectiveDate effective date
	 * @return judges
	 */
	List<Person> findJudges(String query, Date effectiveDate);
	
	/**
	 * Returns the court case for the specified docket.
	 * 
	 * @param docket docket
	 * @return court case
	 */
	CourtCase findByDocket(Docket docket);

	/**
	 * Returns dockets without court case.
	 * 
	 * @return dockets without court case
	 */
	List<Docket> findDocketsWithoutCourtCase(Person person);
}