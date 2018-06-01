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
package omis.facility.dao.impl.hibernate;

import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.facility.dao.UnitDao;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.supervision.domain.SupervisoryOrganization;

import org.hibernate.SessionFactory;

/**
 * Unit data access object hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class UnitDaoHibernateImpl 
	extends GenericHibernateDaoImpl<Unit> 
	implements UnitDao {
	
	/* Query names. */
	
	private static final String FIND_UNITS_BY_FACILITY_QUERY_NAME = 
			"findUnitsByFacility";
	
	private static final String FIND_UNITS_BY_COMPLEX_QUERY_NAME = 
			"findUnitsByComplex";
	
	private static final String FIND_UNITS_QUERY_NAME = "findUnits";
	
	private static final String FIND_UNIT_QUERY_NAME = "findUnit";
	
	private static final String FIND_UNIT_EXCLUDING_QUERY_NAME = 
			"findUnitExcluding";
	
	private static final String 
			FIND_UNITS_BY_SUPERVISORY_ORGANIZATION_QUERY_NAME = 
					"findUnitsBySupervisoryOrganization";
	
	/* Parameter names. */
	
	private static final String FACILITY_PARAM_NAME = "facility";
	
	private static final String COMPLEX_PARAM_NAME = "complex";
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String UNIT_PARAM_NAME = "unit";
	
	private static final String SUPERVISORY_ORGANIZTION_PARAM_NAME = 
			"supervisoryOrganization";
	
	/* Property names. */
	
	private static final String FACILITY_PROPERTY_NAME = "facility";
	
	private static final String COMPLEX_PROPERTY_NAME = "complex";

	/**
	 * Instantiates a data access object for unit with the specified session
	 * factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public UnitDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Unit> findByFacility(final Facility facility) {
		@SuppressWarnings("unchecked")
		List<Unit> units = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNITS_BY_FACILITY_QUERY_NAME)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.list();
		
		return units;
	}

	/** {@inheritDoc} */
	@Override
	public List<Unit> findByComplex(final Complex complex) {
		@SuppressWarnings("unchecked")
		List<Unit> units = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNITS_BY_COMPLEX_QUERY_NAME)
				.setParameter(COMPLEX_PARAM_NAME, complex)
				.list();
		
		return units;
	}

	/** {@inheritDoc} */
	@Override
	public List<Unit> findUnits(final Facility facility, 
			final Complex complex) {
		@SuppressWarnings("unchecked")
		List<Unit> units = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNITS_QUERY_NAME)
				.setParameter(FACILITY_PARAM_NAME, facility, 
						this.getEntityPropertyType(FACILITY_PROPERTY_NAME))
				.setParameter(COMPLEX_PARAM_NAME,  complex,
						this.getEntityPropertyType(COMPLEX_PROPERTY_NAME))
				.list();
		return units;
	}

	/** {@inheritDoc} */
	@Override
	public Unit find(String name, Facility facility) {
		Unit unit = (Unit) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNIT_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.uniqueResult();
		return unit;
	}

	/** {@inheritDoc} */
	@Override
	public Unit findExcluding(String name, Facility facility, Unit unit) {
		Unit matchingUnit = (Unit) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_UNIT_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setParameter(UNIT_PARAM_NAME, unit)
				.uniqueResult();
		return matchingUnit;
	}

	/** {@inheritDoc} */
	@Override
	public List<Unit> findBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		@SuppressWarnings("unchecked")
		List<Unit> units = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						FIND_UNITS_BY_SUPERVISORY_ORGANIZATION_QUERY_NAME)
				.setParameter(SUPERVISORY_ORGANIZTION_PARAM_NAME, 
						supervisoryOrganization)
				.list();
		return units;
	}
}