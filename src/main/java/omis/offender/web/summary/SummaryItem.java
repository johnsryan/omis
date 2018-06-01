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
package omis.offender.web.summary;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import omis.offender.domain.Offender;

/** Summarize-able offender information.
 * @author Ryan Johns
 * @author Stephen Abson
 * @version 0.1.0 (Jul 27, 2015)
 * @since OMIS 3 */
public interface SummaryItem extends Serializable, Comparable<SummaryItem> {
		
	/** Gets included page.
	 * @return included page. */
	String getIncludedPageName();
	
	/** Gets order for comparison.
	 * @return order - order. */
	int getOrder();
	
	/**
	 * Returns whether enabled.
	 * @return whether enabled
	 */
	boolean getEnabled();
	
	/** Build.
	 * @param modelMap - model map.
	 * @param offender - offender.
	 * @param date - date. */
	void build(Map<String, Object> modelMap, Offender offender, Date date); 
	
}
