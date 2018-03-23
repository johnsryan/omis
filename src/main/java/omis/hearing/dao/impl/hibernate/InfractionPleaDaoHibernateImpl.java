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
package omis.hearing.dao.impl.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearing.dao.InfractionPleaDao;
import omis.hearing.domain.InfractionPlea;

/**
 * Infraction Plea DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
public class InfractionPleaDaoHibernateImpl extends
		GenericHibernateDaoImpl<InfractionPlea> implements InfractionPleaDao {
	
	private static final String FIND_ALL_QUERY_NAME = "findAllInfractionPleas";
	
	private static final String FIND_QUERY_NAME =
			"findInfractionPlea";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findInfractionPleaExcluding";
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String INFRACTION_PLEA_PARAM_NAME = "infractionPlea";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String Entity Name
	 */
	public InfractionPleaDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<InfractionPlea> findAll() {
		@SuppressWarnings("unchecked")
		List<InfractionPlea> infractionPleas = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_ALL_QUERY_NAME)
				.list();
		
		return infractionPleas;
	}
	
	/**{@inheritDoc} */
	@Override
	public InfractionPlea findExcluding(final String name,
			final InfractionPlea infractionPleaExcluded) {
		InfractionPlea infractionPlea =
				(InfractionPlea) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(INFRACTION_PLEA_PARAM_NAME,
						infractionPleaExcluded)
				.uniqueResult();
		
		return infractionPlea;
	}

	/**{@inheritDoc} */
	@Override
	public InfractionPlea find(final String name) {
		InfractionPlea infractionPlea =
				(InfractionPlea) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		
		return infractionPlea;
	}

}
