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

import java.util.List;

import omis.dao.GenericDao;
import omis.document.domain.Document;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;

/**
 * Data access object for mental health review document association.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public interface MentalHealthReviewDocumentAssociationDao 
		extends GenericDao<MentalHealthReviewDocumentAssociation> {

	/**
	 * Returns the mental health review document association for the specified 
	 * document and mental health review.
	 * 
	 * @param document document
	 * @param mentalHealthReview mental health review
	 * @return mental health review document association
	 */
	MentalHealthReviewDocumentAssociation find(Document document, 
			MentalHealthReview mentalHealthReview);
	
	/**
	 * Returns a list of mental health review document associations for the 
	 * specified mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health review document associations
	 */
	List<MentalHealthReviewDocumentAssociation> findByMentalHealthReview(
			MentalHealthReview mentalHealthReview);
}