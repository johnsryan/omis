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

/** Abstract implementation of summary item.
 * @author Ryan Johns
 * @version 0.1.0 (Jul 27, 2015)
 * @since OMIS 3.0 */
public abstract class AbstractSummaryItem implements SummaryItem {
	private static final long serialVersionUID = 1L;
	private final String includedPageName;
	private final SummaryItemRegistry summaryItemRegistry;
	private final int order;
	private final boolean enabled;
	
	/** Constructor.
	 * @param includedPageName - page to be included.
	 * @param summaryItemRegistry - registry to register summary items. 
	 * @param order - order for comparison. */
	public AbstractSummaryItem(final String includedPageName, 
			final SummaryItemRegistry summaryItemRegistry, 
			final int order, final boolean enabled) {
		this.includedPageName = includedPageName;
		this.summaryItemRegistry = summaryItemRegistry;
		this.order = order;	
		this.enabled = enabled;
		this.summaryItemRegistry.register(this);
		
	}
	
	/** {@inheritDoc} */
	@Override
	public int getOrder() { 
		return this.order; 
	}
	
	/** {@inheritDoc} */
	@Override
	public String getIncludedPageName() { 
		return this.includedPageName; 
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean getEnabled() {
		return this.enabled;
	}
	
	/** {@inheritDoc} */
	@Override
	public int compareTo(final SummaryItem that) {
		return this.getOrder() - that.getOrder();
	}
}
