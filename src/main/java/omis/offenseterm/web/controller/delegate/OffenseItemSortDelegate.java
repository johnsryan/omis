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
package omis.offenseterm.web.controller.delegate;

import java.util.List;

import omis.offenseterm.web.form.OffenseItem;

/**
 * Encapsulates algorithm to sort offense items.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 1, 2018)
 * @since OMIS 3.0
 */
public interface OffenseItemSortDelegate {

	/**
	 * Returns items ordered according to dependence.
	 * 
	 * <p>Dependence is based on one sentence's connection to another when
	 * consecutive.
	 * 
	 * <p>None consecutive and consecutive to other sentence offense items are
	 * ordered at the beginning in the order that they appear in the unordered
	 * items.
	 * 
	 * @param unorderedItems unordered items
	 * @return items ordered dependently
	 */
	List<OffenseItem> sort(List<OffenseItem> unorderedItems);
}