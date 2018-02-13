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
package omis.mentalhealthreview.dao;

import java.util.Date;

import omis.dao.GenericDao;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.offender.domain.Offender;

/**
 * Data access object for mental health review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public interface MentalHealthReviewDao extends GenericDao<MentalHealthReview> {

	/**
	 * Returns the specified mental health review for the specified date, text, 
	 * and offender.
	 * 
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @return mental health review
	 */
	MentalHealthReview find(Date date, String text, Offender offender);
	
	/**
	 * Returns the specified mental health review for the specified date, text, 
	 * and offender excluding the specified mental health review.
	 * 
	 * @param date date
	 * @param text text
	 * @param offender offender
	 * @param excludedMentalHealthReview excluded mental health review
	 * @return mental health review
	 */
	MentalHealthReview findExcluding(Date date, String text, Offender offender, 
			MentalHealthReview excludedMentalHealthReview);
}