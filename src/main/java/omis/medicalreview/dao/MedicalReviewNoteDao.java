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
import java.util.List;
import omis.dao.GenericDao;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewNote;

/**
 * Medical Review Note Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public interface MedicalReviewNoteDao extends GenericDao<MedicalReviewNote> {
	
	/**
	 * Returns a Medical Review Note with the specified properties.
	 * 
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @return Medical Review Note with the specified properties.
	 */
	MedicalReviewNote find(MedicalReview medicalReview,
			String description, Date date);
	
	/**
	 * Returns a Medical Review Note with the specified properties excluding
	 * the specified Medical Review Note.
	 * 
	 * @param medicalReview - Medical Review
	 * @param description - String description
	 * @param date - Date
	 * @param medicalReviewNoteExcluding - Medical Review Note to exclude
	 * @return Medical Review Note with the specified properties excluding
	 * the specified Medical Review Note.
	 */
	MedicalReviewNote findExcluding(MedicalReview medicalReview,
			String description, Date date,
			MedicalReviewNote medicalReviewNoteExcluding);
	
	/**
	 * Returns a list of Medical Review Notes for the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Notes for the specified Medical Review.
	 */
	List<MedicalReviewNote> findByMedicalReview(MedicalReview medicalReview);
}
