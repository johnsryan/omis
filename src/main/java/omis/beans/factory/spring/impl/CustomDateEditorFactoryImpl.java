package omis.beans.factory.spring.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import omis.beans.factory.spring.CustomDateEditorFactory;

import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Implementation of factory class for custom date property editors.
 * 
 * @author Stephen Abson
 * @version 0.1.1 (Nov 17, 2012)
 * @see CustomDateEditorFactory
 * @since OMIS 3.0
 */
public class CustomDateEditorFactoryImpl
		implements CustomDateEditorFactory {
	
	private final String dateOnlyPattern;
	
	private final String timeOnlyPattern;
	
	private final String dateTimePattern;
	
	private final boolean lenient;
	
	/**
	 * Instantiates a date editor factory with the specified patterns.
	 * 
	 * @param dateOnlyPattern date only pattern
	 * @param timeOnlyPattern time only pattern
	 * @param dateTimePattern date and time pattern
	 * @param lenient whether lenient
	 */
	public CustomDateEditorFactoryImpl(final String dateOnlyPattern,
			final String timeOnlyPattern, final String dateTimePattern,
			final boolean lenient) {
		this.dateOnlyPattern = dateOnlyPattern;
		this.timeOnlyPattern = timeOnlyPattern;
		this.dateTimePattern = dateTimePattern;
		this.lenient = lenient;
	}

	/** {@inheritDoc} */
	@Override
	public CustomDateEditor createCustomDateEditor(
			final String pattern, final boolean allowEmpty) {
		return  this.createImpl(pattern, allowEmpty);
	}

	/** {@inheritDoc} */
	@Override
	public CustomDateEditor createCustomDateOnlyEditor(
			final boolean allowEmpty) {
		return  this.createImpl(this.dateOnlyPattern, allowEmpty);
	}

	/** {@inheritDoc} */
	@Override
	public CustomDateEditor createCustomTimeOnlyEditor(
			final boolean allowEmpty) {
		return this.createImpl(this.timeOnlyPattern, allowEmpty);
	}

	/** {@inheritDoc} */
	@Override
	public CustomDateEditor createCustomDateTimeEditor(
			final boolean allowEmpty) {
		return this.createImpl(this.dateTimePattern, allowEmpty);
	}
	
	// Implementation
	private CustomDateEditor createImpl(
			final String pattern, final boolean allowEmpty) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(this.lenient);
		return new CustomDateEditor(dateFormat, allowEmpty);
	}
}