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
package omis.country.dao;

import java.util.List;

import omis.country.domain.Country;
import omis.dao.GenericDao;

/**
 * Data access object for countries.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 6, 2013)
 * @since OMIS 3.0
 */
public interface CountryDao
		extends GenericDao<Country> {
	/**
	 * Returns all countries.
	 * 
	 * @return all countries
	 */
	List<Country> findAll();
	
	/**
	 * Returns country.
	 * 
	 * @param name name
	 * @return country
	 */
	Country find(String name);
}