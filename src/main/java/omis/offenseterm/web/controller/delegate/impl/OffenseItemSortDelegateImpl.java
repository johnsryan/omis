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
package omis.offenseterm.web.controller.delegate.impl;

import java.util.ArrayList;
import java.util.List;

import omis.offenseterm.web.controller.delegate.OffenseItemSortDelegate;
import omis.offenseterm.web.form.OffenseItem;
import omis.offenseterm.web.form.OffenseItemConnectionClassification;
import omis.sentence.web.form.SentenceOperation;

/**
 * Implementation of delegate that encapsulates algorithm to sort offense items.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 1, 2018)
 * @since OMIS 3.0
 */
public class OffenseItemSortDelegateImpl
		implements OffenseItemSortDelegate {

	/** {@inheritDoc} */
	@Override
	public List<OffenseItem> sort(final List<OffenseItem> unorderedItems) {
		List<OffenseItem> orderedItems = new ArrayList<OffenseItem>();
		int consecutivelyConnectedCount = 0;
		int notConsecutivelyConnectedCount = 0;
		for (OffenseItem offenseItem : unorderedItems) {
			if ((SentenceOperation.CREATE
						.equals(offenseItem.getSentenceOperation())
					|| SentenceOperation.AMEND
						.equals(offenseItem.getSentenceOperation())
					|| SentenceOperation.UPDATE
						.equals(offenseItem.getSentenceOperation()))
					&& (OffenseItemConnectionClassification.CONSECUTIVE
							.equals(offenseItem.getConnection()
									.getClassification()))) {
				int totalCount = consecutivelyConnectedCount
						+ notConsecutivelyConnectedCount;
				if (offenseItem.getConnection().getIndex()
						< totalCount || totalCount == 0) {
					orderedItems.add(offenseItem);
				} else if (offenseItem.getConnection().getIndex()
						> totalCount) {
					int referencedOrderedIndex = orderedItems.indexOf(
							unorderedItems.get(offenseItem
									.getConnection().getIndex().intValue()));
					if (referencedOrderedIndex > -1) {
						orderedItems.add(referencedOrderedIndex, offenseItem);
					} else {
						boolean added = false;
						for (OffenseItem orderedItem : orderedItems) {
							if (OffenseItemConnectionClassification.CONSECUTIVE
									.equals(orderedItem.getConnection()
											.getClassification())) {
								if(orderedItem.getConnection().getIndex()
										.intValue() == totalCount) {
									orderedItems.add(orderedItems.indexOf(
											orderedItem), offenseItem);
									added = true;
									break;
								}
							}
						}
						if (!added) {
							orderedItems.add(offenseItem);
						}
					}
				} else {
					throw new IllegalArgumentException(
							"Offense term cannot be consecutive to itself");
				}
				consecutivelyConnectedCount++;
			} else {
				
				// Adds unconnected offense items first
				orderedItems.add(notConsecutivelyConnectedCount, offenseItem);
				notConsecutivelyConnectedCount++;
			}
		}
		return orderedItems;
	}
}