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
package omis.medicalreview.dao;

import java.util.Date;
import omis.dao.GenericDao;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.offender.domain.Offender;

/**
 * Medical Review Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public interface MedicalReviewDao extends GenericDao<MedicalReview> {
	
	/**
	 * Returns a Medical Review with the specified properties.
	 * 
	 * @param offender - Offender
	 * @param date - Date
	 * @param healthClassification - Medical Health Classification
	 * @return Medical Review with the specified properties.
	 */
	MedicalReview find(Offender offender, Date date,
			MedicalHealthClassification healthClassification);
	
	/**
	 * Returns a Medical Review with the specified properties excluding
	 * the given Medical Review.
	 * 
	 * @param offender - Offender
	 * @param date - Date
	 * @param healthClassification - Medical Health Classification
	 * @param medicalReviewExcluding - Medical Review to exclude
	 * @return Medical Review with the specified properties excluding
	 * the given Medical Review.
	 */
	MedicalReview findExcluding(Offender offender, Date date,
			MedicalHealthClassification healthClassification,
			MedicalReview medicalReviewExcluding);
}
