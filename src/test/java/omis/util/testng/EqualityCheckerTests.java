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
package omis.util.testng;

import java.io.Serializable;
import java.util.Date;

import org.testng.annotations.Test;

import omis.util.EqualityChecker;

/**
 * Tests for equality checker.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Feb 2, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"util"})
public class EqualityCheckerTests {
	
	private static final long DAY_MS = 1000 * 60 * 60 * 12;
	
	// Test class - a name
	private static class Name implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private final String last;
		
		private final String first;
		
		// Instantiates name
		private Name(final String last, final String first) {
			this.last = last;
			this.first = first;
		}
		
		/**
		 * Returns last name.
		 * 
		 * @return last name
		 */
		protected String getLast() {
			return this.last;
		}
		
		/**
		 * Returns first name.
		 * 
		 * @return first name
		 */
		protected String getFirst() {
			return this.first;
		}
		
		/** {@inheritDoc} */
		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof Name)) {
				return false;
			}
			Name that = (Name) obj;
			return EqualityChecker.create(Serializable.class)
					.add(this.first, that.first)
					.add(this.last, that.last)
					.check();
		}
		
		/** {@inheritDoc} */
		@Override
		public int hashCode() {
			int hashCode = 14;
			hashCode = 29 * hashCode + this.first.hashCode();
			hashCode = 29 * hashCode + this.last.hashCode();
			return hashCode;
		}
	}

	// Test class - a name with a suffix
	private static class SuffixedName extends Name {
		
		private static final long serialVersionUID = 1L;
		
		private final String suffix;
		
		// Instantiates suffixed name
		private SuffixedName(
				final String last, final String first, final String suffix) {
			super(last, first);
			this.suffix = suffix;
		}
		
		/** {@inheritDoc} */
		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof SuffixedName)) {
				return false;
			}
			SuffixedName that = (SuffixedName) obj;
			return EqualityChecker.create(Serializable.class)
					.add(this.getFirst(), that.getFirst())
					.add(this.getLast(), that.getLast())
					.add(this.suffix, that.suffix)
					.check();
		}
		
		/** {@inheritDoc} */
		@Override
		public int hashCode() {
			int hashCode = 14;
			hashCode = 29 * hashCode + this.getFirst().hashCode();
			hashCode = 29 * hashCode + this.getLast().hashCode();
			hashCode = 29 * hashCode + this.suffix.hashCode();
			return hashCode;
		}
	}
	
	/** Tests a single, equal pair of objects. */
	public void testSingleEqualPair() {
		Date now = new Date();
		Date then = new Date(now.getTime());
		assert EqualityChecker.create(Serializable.class)
				.add(now, then)
				.check() : "Single equals pair check failure";
	}
	
	/** Tests a single, inequal pair. */
	public void testSingleInequalPair() {
		Date now = new Date();
		Date tomorrow = new Date(now.getTime() + DAY_MS);
		assert !EqualityChecker.create(Serializable.class)
				.add(now,  tomorrow)
				.check() : "Single inequal pair check failure";
	}
	
	/** Tests multiple, equal pairs. */
	public void testMultipleEqualPairs() {
		Date now = new Date();
		Date then = new Date(now.getTime());
		Name blofeld = new Name("Blofeld", "Ernst");
		Name alsoBlofeld = new Name("Blofeld", "Ernst");
		assert EqualityChecker.create(Serializable.class)
				.add(now, then)
				.add(blofeld, alsoBlofeld)
				.check() : "Multiple equal pair check failure";
	}
	
	/** Tests multiple, inequal pairs. */
	public void testMultipleInequalPairs() {
		Date now = new Date();
		Date then = new Date(now.getTime());
		Name blofeld = new Name("Blofeld", "Ernst");
		Name julius = new Name("No", "Julius");
		assert !EqualityChecker.create(Serializable.class)
				.add(now, then)
				.add(blofeld, julius)
				.check() : "Multiple inequal pair check failure";
	}
	
	/** Tests that nulls are equal. */
	public void testEqualNull() {
		assert EqualityChecker.create(String.class)
			.add(null, null)
			.check() : "Equal pair of nulls check failure";
	}
	
	/** Tests that one null and one not-null pair is not equal. */
	public void testInequalNull() {
		assert !EqualityChecker.create(Name.class)
			.add(null, new Name("Blofeld", "Ernst"))
			.check() : "Inequal pair check with null failure";
	}
	
	/** Tests that one not-null and not-null pair is not equal. */
	public void testInequalNullReverseOrder() {
		assert !EqualityChecker.create(Date.class)
			.add(new Date(), null)
			.check() : "Inequal pair check with null failure";
	}
	
	/**
	 * Tests that comparing types to subtypes is not allowed
	 * 
	 * <p>Test should throw {@code IllegalArgumentException}.
	 */
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testIllegalSubtypes() {
		EqualityChecker.create(Name.class)
			.add(new Name("Blofeld", "Ernst"),
					new SuffixedName("Blofeld", "Ernst", "Jr"));
	}
}