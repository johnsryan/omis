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
package omis.parolereview.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.document.domain.Document;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;

/**
 * Data access object for parole review document association.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public interface ParoleReviewDocumentAssociationDao 
		extends GenericDao<ParoleReviewDocumentAssociation> {

	/**
	 * Returns the parole review document association for the specified 
	 * document and parole review.
	 * 
	 * @param document document
	 * @param paroleReview parole review
	 * @return parole review document association
	 */
	ParoleReviewDocumentAssociation find(Document document, 
			ParoleReview paroleReview);
	
	/**
	 * Returns a list of parole review document associations for the specified 
	 * parole review.
	 * 
	 * @param paroleReview parole review
	 * @return list of parole review document associations
	 */
	List<ParoleReviewDocumentAssociation> findByParoleReview(
			ParoleReview paroleReview);
}