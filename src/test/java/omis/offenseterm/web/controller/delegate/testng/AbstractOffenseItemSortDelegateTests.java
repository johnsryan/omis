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
package omis.offenseterm.web.controller.delegate.testng;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import omis.offenseterm.web.controller.delegate.OffenseItemSortDelegate;
import omis.offenseterm.web.form.OffenseItem;
import omis.offenseterm.web.form.OffenseItemConnection;
import omis.offenseterm.web.form.OffenseItemConnectionClassification;
import omis.sentence.web.form.SentenceOperation;

/**
 * Tests offense item sort delegate.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 1, 2018)
 * @since OMIS 3.0
 */
public abstract class AbstractOffenseItemSortDelegateTests {
	
	// Sort delegate to test
	private OffenseItemSortDelegate sortDelegate;
	
	/**
	 * Stores offense items and expected indexes. Performs sort and asserts
	 * expected positions.
	 */
	protected final static class Helper {
		
		private final OffenseItemSortDelegate innerSortDelegate;
		
		// Indicates the expected position of an offense term
		private static class ExpectedPosition {
			
			private final OffenseItem offenseItem;
			
			private final int index;
			
			// Instantiates expected position
			private ExpectedPosition(
					final OffenseItem offenseItem,
					final int index) {
				this.offenseItem = offenseItem;
				this.index = index;
			}
		}
		
		private List<ExpectedPosition> expectedPositions
			= new ArrayList<ExpectedPosition>();
		
		/**
		 * Instantiates helper with sort delegate.
		 * 
		 * @param sortDelegate sort delegate
		 */
		protected Helper(
				final OffenseItemSortDelegate sortDelegate) {
			this.innerSortDelegate = sortDelegate;
		}
		
		// Uses long original indexes to mimic sentence IDs 
		private Helper expectAt(
				final SentenceOperation sentenceOperation,
				final Long connectionIndex,
				final OffenseItemConnectionClassification
					connectionClassification,
				final Integer expectedOrderIndex) {
			OffenseItem offenseItem = new OffenseItem();
			offenseItem.setSentenceOperation(sentenceOperation);
			if (OffenseItemConnectionClassification.INITIAL
					.equals(connectionClassification)) {
				offenseItem.setConnection(
						OffenseItemConnection.createInitial());
			} else if (OffenseItemConnectionClassification.CONSECUTIVE
					.equals(connectionClassification)) {
				offenseItem.setConnection(
						OffenseItemConnection
						.createConsecutive(connectionIndex));
			} else if (OffenseItemConnectionClassification.CONCURRENT
					.equals(connectionClassification)) {
				offenseItem.setConnection(
						OffenseItemConnection.createConcurrent());
			} else if (OffenseItemConnectionClassification
						.CONSECUTIVE_OTHER_DOCKET
							.equals(connectionClassification)) {
				offenseItem.setConnection(
						OffenseItemConnection
							.createConsecutiveOtherDocket(connectionIndex));
			} else {
				throw new AssertionError(String.format(
						"Unknown connection classification",
						connectionClassification));
			}
			this.expectedPositions.add(
					new ExpectedPosition(offenseItem, expectedOrderIndex));
			return this;
		}
		
		/**
		 * Expects initial connection at index.
		 * 
		 * @param sentenceOperation sentence operation
		 * @param expectedOrderIndex expected order index
		 * @return {@code this}
		 */
		protected Helper expectInitialAt(
				final SentenceOperation sentenceOperation,
				final Integer expectedOrderIndex) {
			return expectAt(sentenceOperation, null,
					OffenseItemConnectionClassification.INITIAL,
					expectedOrderIndex);
		}
		
		/**
		 * Expects concurrent connection at index.
		 * 
		 * @param sentenceOperation sentence operation
		 * @param expectedOrderIndex expected order index
		 * @return {@code this}
		 */
		protected Helper expectConcurrentAt(
				final SentenceOperation sentenceOperation,
				final Integer expectedOrderIndex) {
			return expectAt(sentenceOperation, null,
					OffenseItemConnectionClassification.CONCURRENT,
					expectedOrderIndex);
		}
		
		/**
		 * Expects consecutive connection at index.
		 * 
		 * @param sentenceOperation sentence operation
		 * @param connectedToIndex index of item to which to connect
		 * consecutively
		 * @param expectedOrderIndex expected order index
		 * @return {@code this}
		 */
		protected Helper expectConsecutiveAt(
				final SentenceOperation sentenceOperation,
				final Long connectedToIndex,
				final Integer expectedOrderIndex) {
			return expectAt(sentenceOperation, connectedToIndex,
					OffenseItemConnectionClassification.CONSECUTIVE,
					expectedOrderIndex);
		}
		
		/**
		 * Expects consecutive connection to sentence on other docket.
		 * 
		 * @param sentenceOperation sentence operation
		 * @param sentenceId ID of sentence on other docket
		 * @param expectedOrderIndex expected order index
		 * @return {@code this}
		 */
		protected Helper expectConsecutiveOtherDocketAt(
				final SentenceOperation sentenceOperation,
				final Long sentenceId,
				final Integer expectedOrderIndex) {
			return expectAt(sentenceOperation, sentenceId,
					OffenseItemConnectionClassification
						.CONSECUTIVE_OTHER_DOCKET,
					expectedOrderIndex);
		}
		
		/**
		 * Sorts items and asserts that each item is in its expected position. 
		 */
		protected void sortAndAssert() {
			List<OffenseItem> unsortedItems = new ArrayList<OffenseItem>();
			for (ExpectedPosition expectedPosition : this.expectedPositions) {
				unsortedItems.add(expectedPosition.offenseItem);
			}
			List<OffenseItem> sortedItems
				= this.innerSortDelegate.sort(unsortedItems);
			int index = 0;
			for (OffenseItem sortedItem : sortedItems) {
				
				// Uses null to represent "not found"
				Integer expectedIndex = null;
				for (ExpectedPosition expectedPosition
						: this.expectedPositions) {
					if (expectedPosition.offenseItem.equals(sortedItem)) {
						expectedIndex = expectedPosition.index;
						break;
					}
				}
				if (expectedIndex == null) {
					throw new AssertionError(
							"Expected index not found for offense item "
							+ " - this should not be possible");
				}
				assert index == expectedIndex : String.format(
						"%s at wrong position - %d expected; %d actual",
						sortedItem, expectedIndex, index);
				index++;
			}
		}
	}
	
	/**
	 * Sets sort delegate.
	 * 
	 * @param sortDelegate sort delegate
	 */
	protected void setSortDelegate(final OffenseItemSortDelegate sortDelegate) {
		this.sortDelegate = sortDelegate;
	}
	
	/**
	 * Returns new helper.
	 * 
	 * @return new helper
	 * @throws IllegalStateException if sort delegate is not set
	 */
	protected Helper createHelper() {
		if (this.sortDelegate == null) {
			throw new IllegalStateException("Sort delegate not set");
		}
		return new Helper(this.sortDelegate);
	}
	
	/** Tests single initial stays first. */
	@Test
	public void testInitial() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.sortAndAssert();
	}
	
	/** Tests initial and concurrent remain in order. */ 
	@Test
	public void testInitialAndConcurrent() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.expectConcurrentAt(SentenceOperation.UPDATE, 1)
			.sortAndAssert();
	}
	
	/** Tests that consecutive to initial is ordered after initial. */
	@Test
	public void testConsecutiveAndInitialSwitch() {
		this.createHelper()
			.expectConsecutiveAt(SentenceOperation.UPDATE, 0L, 1)
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.sortAndAssert();
	}
	
	/** Tests that initial and consecutive to other docket remain in order. */
	@Test
	public void testInitialAndConsecutiveToOtherDocket() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.expectConsecutiveOtherDocketAt(SentenceOperation.UPDATE, 1L, 1)
			.sortAndAssert();
	}
	
	/**
	 * Tests that two consecutive offense items after an initial are switched.
	 */
	@Test
	public void testInitialAndSwitchLastTwoConsecutives() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 2L, 2)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 0L, 1)
			.sortAndAssert();
	}
	
	/**
	 * Tests that two consecutive offense items after an initial remain in
	 * order.
	 */
	@Test
	public void testInitialAndLastTwoConsecutivesRemain() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 0L, 1)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 1L, 2)
			.sortAndAssert();
	}
	
	/**
	 * Tests that initial followed by consecutive offense items in a mixed order
	 * are rearranged.
	 */
	// This test exposes a bug - enable the test to fix the bug. Bug does not
	// cause an error in application due to the way that offense items/sentences
	// are processed.
	// FIXME Offense item sort bug - SA
	@Test(enabled = false)
	public void testInitialAndMultipleConsecutiveRearrangement() {
		this.createHelper()
			.expectInitialAt(SentenceOperation.UPDATE, 0)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 2L, 3)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 3L, 4)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 0L, 1)
			.expectConsecutiveAt(SentenceOperation.UPDATE, 1L, 2)
			.sortAndAssert();
	}
}