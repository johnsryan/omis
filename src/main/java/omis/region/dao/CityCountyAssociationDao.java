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
package omis.region.dao;

import omis.dao.GenericDao;
import omis.region.domain.City;
import omis.region.domain.CityCountyAssociation;
import omis.region.domain.County;

/**
 * Data access object for association of city to county.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2015)
 * @since OMIS 3.0
 */
public interface CityCountyAssociationDao
		extends GenericDao<CityCountyAssociation> {

	/**
	 * Returns association of city to county.
	 * 
	 * <p>Returns {@code null} if the city and county are not associated.
	 * 
	 * @param city city
	 * @param county county
	 * @return association of city to county; {@code null} if city and county
	 * are not associated
	 */
	CityCountyAssociation find(City city, County county);
	
	/**
	 * Returns association of city to county with associations excluded.
	 * 
	 * <p>Returns {@code null} if the city and county are not associated
	 * other than by excluded associations.
	 * 
	 * @param city city
	 * @param county county
	 * @param excludedAssociations associations to exclude
	 * @return association of city to county; {@code null} if city and county
	 * are not associated other than by excluded associations
	 */
	CityCountyAssociation findExcluding(City city, County county,
			CityCountyAssociation... excludedAssociations);
}