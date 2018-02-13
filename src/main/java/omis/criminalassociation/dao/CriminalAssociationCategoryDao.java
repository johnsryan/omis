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
package omis.criminalassociation.dao;

import java.util.List;

import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.dao.GenericDao;

/**
 * Data access object for criminal association category.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Apr, 14, 2015)
 * @since OMIS 3.0
 *
 */
public interface CriminalAssociationCategoryDao 
	extends GenericDao<CriminalAssociationCategory> {
	
	/**
	 * Returns the list of criminal association categories 
	 * returns {@code null}.
	 * 
	 * @return list of criminal association category
	 */
	List<CriminalAssociationCategory> findCriminalAssociationCategories();
	
	CriminalAssociationCategory find(String name);
}