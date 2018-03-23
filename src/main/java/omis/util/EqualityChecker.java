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
package omis.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks whether one or more pairs of objects are equals.
 * 
 * <p>Typical usage is:
 * 
 * <pre>
 *   if (EqualityChecker.create(Serializable.class)
 *       .add(this.getDate(), that.getDate())
 *       .add(this.getCategory(), that.getCategory())
 *       .check()) {
 *     // Perform operation
 *   }  
 * </pre>
 * 
 * <p>Types of pairs must be of type {@code T} or a deriavent to compile and
 * must not be different subtypes of {@code T} to be added. If the latter rule
 * is violated, an {@code IllegalArgumentException} is thrown by
 * {@code add(...)}.
 * 
 * <p>The check is {@code null} safe - two {@code null}s are considered equal;
 * a {@code null} and not {@code null} pair are not considered equal. 
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Feb 2, 2018)
 * @since OMIS 3.0
 * @param <T> base type of which all pairs must be derived
 */
public class EqualityChecker<T> {

	// Pair of object
	private static class Pair<T> {
		
		private final T object;
		
		private final T otherObject;
		
		// Instantiates pair
		private <U extends T> Pair(
				final U object, final U otherObject) {
			this.object = object;
			this.otherObject = otherObject;
		}
	}
	
	// Pairs
	private final List<Pair<T>> pairs = new ArrayList<Pair<T>>();
	
	// Hides constructor - forces factory method use
	private EqualityChecker() {
		// Default instantiation
	}
	
	/**
	 * Instantiates equality checker for type of {@code clazz}.
	 * 
	 * @param clazz type of equality checker
	 * @return equality checker
	 */
	public static <T> EqualityChecker<T> create(Class<T> clazz) {
		return new EqualityChecker<T>();
	}
	
	/**
	 * Adds a pair of objects.
	 * 
	 * <p>If objects are not of same type, an {@code IllegalArgumentException}
	 * is thrown.
	 * 
	 * @param object object
	 * @param otherObject other object to which to compare object
	 * @param <U> derivant or equal of <T> of which both pair must be a derivant
	 * or subtype; if the pairs are not of the same type,
	 * an {@code IllegalArgumentException} will be thrown  
	 * @return {@code this}
	 */
	public <U extends T> EqualityChecker<T> add(
			final U object, final U otherObject) {
		if (object != null && otherObject != null 
				&& !object.getClass().equals(otherObject.getClass())) {
			throw new IllegalArgumentException(
					String.format(
							"Type mismatch - object is %s; otherObject is %s",
							object.getClass().getName(),
							otherObject.getClass().getName()));
		}
		this.pairs.add(new Pair<T>(object, otherObject));
		return this;
	}
	
	/**
	 * Returns {@code true} if all added pairs are equal; {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if all added pairs are equal; {@code false}
	 * otherwise
	 */
	public boolean check() {
		for (Pair<T> pair : this.pairs) {
			if (pair.object != null) {
				if (!pair.object.equals(pair.otherObject)) {
					return false;
				}
			} else if (pair.otherObject != null)
				return false;
		}
		return true;
	}
}