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

import java.util.List;
import omis.dao.GenericDao;
import omis.document.domain.Document;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;

/**
 * Medical Review Document Association Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public interface MedicalReviewDocumentAssociationDao
		extends GenericDao<MedicalReviewDocumentAssociation> {
	
	/**
	 * Returns a Medical Review Document Association with the specified
	 * properties.
	 * 
	 * @param medicalReview - Medical Review
	 * @param document - Document
	 * @return Medical Review Document Association with the specified
	 * properties.
	 */
	MedicalReviewDocumentAssociation find(
			MedicalReview medicalReview, Document document);
	
	/**
	 * Returns a Medical Review Document Association with the specified
	 * properties excluding the specified Medical Review Document Association.
	 * 
	 * @param medicalReview - Medical Review
	 * @param document - Document
	 * @param medicalReviewDocumentAssociationExcluding - Medical Review
	 * Document Association to exclude
	 * @return Returns a Medical Review Document Association with the specified
	 * properties excluding the specified Medical Review Document Association.
	 */
	MedicalReviewDocumentAssociation findExcluding(
			MedicalReview medicalReview, Document document,
			MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociationExcluding);
	
	/**
	 * Returns a list of Medical Review Document Associations by the specified
	 * Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return List of Medical Review Document Associations by the specified
	 * Medical Review.
	 */
	List<MedicalReviewDocumentAssociation> findByMedicalReview(
			MedicalReview medicalReview);
}
