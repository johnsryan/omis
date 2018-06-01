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
package omis.bedplacement.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.bedplacement.dao.BedPlacementDao;
import omis.bedplacement.domain.BedPlacement;
import omis.bedplacement.domain.BedPlacementReason;
import omis.bedplacement.exception.BedOccupiedException;
import omis.bedplacement.exception.BedPlacementDateConflictException;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Bed;
import omis.facility.domain.Facility;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Bed placement delegate.
 *
 * @author Josh Divine
 * @version 0.1.2 (May 21, 2018)
 * @since OMIS 3.0
 */
public class BedPlacementDelegate {

	private final BedPlacementDao bedPlacementDao;
	
	private final InstanceFactory<BedPlacement> bedPlacementInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	public BedPlacementDelegate(final BedPlacementDao bedPlacementDao,
			final InstanceFactory<BedPlacement> bedPlacementInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.bedPlacementDao = bedPlacementDao;
		this.bedPlacementInstanceFactory = bedPlacementInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new bed placement for the specified offender.
	 * 
	 * @param offender offender
	 * @param bed bed
	 * @param confirmed confirmed
	 * @param dateRange date range
	 * @param description description
	 * @param reason bed placement reason
	 * @return created bed placement
	 * @throws DuplicateEntityFoundException thrown when a duplicate bed
	 * placement is found
	 * @throws BedPlacementDateConflictException thrown when a bed placement, 
	 * that is confirmed, has an overlapping date range
	 * @throws BedOccupiedException thrown when the specified bed is already
	 * occupied by another bed placement
	 */
	public BedPlacement create(final Offender offender, final Bed bed, 
			final Boolean confirmed, final DateRange dateRange, 
			final String description, final BedPlacementReason reason) 
		throws DuplicateEntityFoundException, BedPlacementDateConflictException, 
			BedOccupiedException {
		if (this.bedPlacementDao.find(offender, dateRange) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate bed placement found");
		}
		if (confirmed) {
			if (this.bedPlacementDao.findWithinDateRange(offender, dateRange)
					.size() > 0) {
				throw new BedPlacementDateConflictException(
						"Conflicting, confirmed,  bed placement found in"
						+ " date range");
			}
			if (this.isBedOccupied(dateRange.getStartDate(), bed, offender)) {
				throw new BedOccupiedException("Bed is occupied");
			}
		}
		BedPlacement bedPlacement = 
				this.bedPlacementInstanceFactory.createInstance();
		bedPlacement.setOffender(offender);
		bedPlacement.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		this.populateBedPlacement(bedPlacement, bed, confirmed, 
				dateRange, description, reason);
		return this.bedPlacementDao.makePersistent(bedPlacement);
	}
	
	/**
	 * Updates the specified bed placement.
	 * 
	 * @param bedPlacement bed placement
	 * @param bed bed
	 * @param confirmed confirmed
	 * @param dateRange date range
	 * @param description description
	 * @param reason bed placement reason
	 * @return updated bed placement
	 * @throws DuplicateEntityFoundException thrown when a duplicate bed
	 * placement is found
	 * @throws BedPlacementDateConflictException thrown when a bed placement, 
	 * that is confirmed, has an overlapping date range
	 * @throws BedOccupiedException thrown when the specified bed is already
	 * occupied by another bed placement
	 */
	public BedPlacement update(final BedPlacement bedPlacement, final Bed bed,
			final Boolean confirmed, final DateRange dateRange, 
			final String description,final BedPlacementReason reason)
		throws DuplicateEntityFoundException, BedPlacementDateConflictException, 
			BedOccupiedException {
		if (this.bedPlacementDao.findExcluding(bedPlacement.getOffender(), 
				dateRange, bedPlacement) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate bed placement found");
		}
		if (confirmed) {
			if (this.bedPlacementDao.findWithinDateRangeExcluding(
					bedPlacement.getOffender(), dateRange, 
					bedPlacement).size() > 0) {
				throw new BedPlacementDateConflictException(
						"Conflicting, confirmed, bed placement found in"
						+ " date range");
			}
			if (this.isBedOccupiedExcluding(dateRange.getStartDate(), bed, 
					bedPlacement)) {
				throw new BedOccupiedException("Bed is occupied");
			}
		}
		this.populateBedPlacement(bedPlacement, bed, confirmed, 
				dateRange, description, reason);
		return this.bedPlacementDao.makePersistent(bedPlacement);
	}
	
	/**
	 * Removes the specified bed placement.
	 * 
	 * @param bedPlacement bed placement
	 */
	public void remove(final BedPlacement bedPlacement) {
		this.bedPlacementDao.makeTransient(bedPlacement);
	}
	
	/**
	 * Confirms the specified bed placement.
	 * 
	 * @param bedPlacement bed placement
	 * @param date date
	 * @return confirmed bed placement
	 * @throws BedPlacementDateConflictException thrown when a bed placement,
	 * that is confirmed, already exists during the specified date for the
	 * specified bedplacement's offender 
	 * @throws BedOccupiedException thrown when the specified bedplacement's bed
	 * is already occupied on the specified date
	 */
	public BedPlacement confirm(final BedPlacement bedPlacement, 
			final Date date) 
		throws BedPlacementDateConflictException, BedOccupiedException {
		BedPlacement currentBedPlacement = this.bedPlacementDao
				.findConfirmedBedPlacementByOffenderOnDate(
						bedPlacement.getOffender(), date);
		if (currentBedPlacement != null && !currentBedPlacement
				.equals(bedPlacement)) {
			if (currentBedPlacement.getDateRange().getEndDate() == null 
					&& currentBedPlacement.getDateRange().getStartDate()
					.getTime() < date.getTime()) {
				currentBedPlacement.getDateRange().setEndDate(date);
				this.bedPlacementDao.makePersistent(currentBedPlacement);
			} else {
				throw new BedPlacementDateConflictException(
						"Conflicting bed placement date found");
			}
		}
		if (this.isBedOccupied(bedPlacement.getDateRange().getStartDate(), 
				bedPlacement.getBed(), bedPlacement.getOffender())) {
			throw new BedOccupiedException("Bed is occupied");
		}
		for (BedPlacement placement : this.bedPlacementDao
				.findWithinDateRangeExcluding(bedPlacement.getOffender(), 
						bedPlacement.getDateRange(), bedPlacement)) {
			if (placement.getConfirmed()) {
				throw new BedPlacementDateConflictException(
						"Conflicting bed placement date found");
			}
		}
		bedPlacement.setConfirmed(true);
		return this.bedPlacementDao.makePersistent(bedPlacement);
	}
	
	/**
	 * Ends the specified bed placement on the specified date.
	 * 
	 * @param bedPlacement bed placement
	 * @param endDate end date
	 * @return ended bed placement
	 */
	public BedPlacement endBedPlacement(final BedPlacement bedPlacement, 
			final Date endDate) {
		bedPlacement.getDateRange().setEndDate(endDate);
		return this.bedPlacementDao.makePersistent(bedPlacement);
	}
	
	/**
	 * Takes the specified dates and compares them with a bed placement.
	 * 
	 * @param bedPlacement bed placement
	 * @param currentDate current date
	 * @param endDate end date
	 * @param userAccount user account
	 * @param updateDate update date
	 */
	public void endBedPlacementAtDate(final BedPlacement bedPlacement,
			final Date currentDate, final Date endDate,
			final UserAccount userAccount, final Date updateDate) {
		Offender offender = bedPlacement.getOffender();
		Long bedPlacementStart = 
				bedPlacement.getDateRange().getStartDate().getTime();
		BedPlacement currentBedPlacement = bedPlacementDao
				.findBedPlacementByOffenderOnDate(offender, currentDate);
		if (currentBedPlacement != null && currentBedPlacement.getConfirmed()) {
			Long currentBedPlacementStart =
					currentBedPlacement.getDateRange().getStartDate().getTime();
			if (bedPlacementStart 
				> currentBedPlacementStart) {
				currentBedPlacement.getDateRange().setEndDate(endDate);
				currentBedPlacement.setUpdateSignature(new UpdateSignature(
					userAccount, updateDate));
				this.bedPlacementDao.makePersistent(currentBedPlacement);
			}
		}
	}
	
	/**
	 * Returns a list bed placements for the specified offender.
	 * 
	 * @param offender offender
	 * @return list of bed placements
	 */
	public List<BedPlacement> findBedPlacementByOffender(
			final Offender offender) {
		return this.bedPlacementDao.findBedPlacementByOffender(offender);
	}
	
	/**
	 * Returns a bed placement for the specified offender that is current
	 * for the specified date. If no current bed placement is found, 
	 * return is null.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return the bed placement / null
	 */
	public BedPlacement findBedPlacementByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.bedPlacementDao.findBedPlacementByOffenderOnDate(offender, 
				date);
	}
	
	/**
	 * Returns a list of bed placements that are valid for the specified
	 * facility on the specified date.
	 * 
	 * @param facility facility
	 * @param date date
	 * @return list of bed placements
	 */
	public List<BedPlacement> findSpecificBedPlacementsByFacility(
			final Facility facility, final Date date) {
		return this.bedPlacementDao.findSpecificBedPlacementsByFacility(
				facility, date);
	}

	/**
	 * Return a list of bed placements that are planned during the date
	 * specified for the specified facility.
	 * 
	 * @param facility facility
	 * @param date date
	 * @return list of bed placements
	 */
	public List<BedPlacement> findSpecificPlannedBedPlacements(
			final Facility facility, final Date date) {
		return this.bedPlacementDao.findSpecificPlannedBedPlacements(facility, 
				date);
	}

	/**
	 * Returns a list of bed placements that are completed for the date
	 * specified in the facility specified.
	 * 
	 * @param facility facility
	 * @param date date
	 * @return list of bed placements
	 */
	public List<BedPlacement> findSpecificCompletedBedPlacements(
			final Facility facility, final Date date) {
		return this.bedPlacementDao.findSpecificCompletedBedPlacements(facility, 
				date);
	}

	
	/* Helper methods. */
	
	/*
	 * Populates the specified bed placement.
	 * 
	 * @param bedPlacement bed placement
	 * @param bed bed
	 * @param confirmed confirmed
	 * @param dateRange date range
	 * @param description description
	 * @param reason reason
	 * @return populated bed placement
	 */
	private BedPlacement populateBedPlacement(final BedPlacement bedPlacement,
			final Bed bed, final Boolean confirmed, final DateRange dateRange,
			final String description, final BedPlacementReason reason) {
		bedPlacement.setBed(bed);
		bedPlacement.setDescription(description);
		bedPlacement.setDateRange(dateRange);
		bedPlacement.setConfirmed(confirmed);
		bedPlacement.setBedPlacementReason(reason);
		bedPlacement.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return bedPlacement;
	}
	
	/*
	 * Checks to see if the specified bed is occupied on the specified date.
	 *  
	 * @param date date
	 * @param bed bed
	 * @param offender offender
	 * @return whether bed is occupied
	 */
	private boolean isBedOccupied(final Date date, final Bed bed, 
			final Offender offender) {
		if (this.bedPlacementDao
				.findConfirmedBedPlacementByBedOnDate(date, bed, offender) 
					!= null) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Checks to see if the specified bed is occupied on the specified date
	 * excluding the specified bed placement.
	 * 
	 * @param date date
	 * @param bed bed
	 * @param bedPlacement bed placement to exclude
	 * @return whether bed is occupied
	 */
	private boolean isBedOccupiedExcluding(final Date date, final Bed bed, 
			final BedPlacement bedPlacement) {
		if (this.bedPlacementDao
				.findConfirmedBedPlacementByBedOnDateExcluding(date, bed, 
						bedPlacement) != null) {
			return true;
		} else {
			return false;
		}
	}
}
