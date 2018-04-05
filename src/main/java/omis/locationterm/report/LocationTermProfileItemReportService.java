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
package omis.locationterm.report;

import java.util.Date;

import omis.offender.domain.Offender;

/**
 * Report service for location term profile item.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public interface LocationTermProfileItemReportService {

	/**
	 * Returns whether offender has active location term on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return whether offender has active location term on date 
	 */
	boolean findLocationExistenceByOffenderAndDate(
			Offender offender, Date effectiveDate);
}