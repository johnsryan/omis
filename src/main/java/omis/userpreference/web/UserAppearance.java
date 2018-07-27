package omis.userpreference.web;

import java.io.Serializable;
import java.util.Date;

import omis.media.domain.Photo;
import omis.userpreference.domain.ColorValue;

/**
 * User appearance.
 * 
 * @author Joel Norris
 * @version 0.1.1 (June 14, 2018)
 * @since OMIS 3.0
 */
public class UserAppearance implements Serializable {

	private static final long serialVersionUID = 1L;
	private ColorValue foregroundColorValue;
	private ColorValue backgroundColorValue;
	private ColorValue accentColorValue;
	private Boolean whiteBackground;
	private Boolean shadows;
	private Short borderRadius;
	private Date date;
	private Photo backgroundPhoto;
	
	/**
	 * Instantiates a default instance of user appearance.
	 */
	public UserAppearance() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an instance of user appearance with the specified color
	 * values.
	 * 
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param date date
	 */
	@Deprecated
	public UserAppearance(final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue, final Boolean whiteBackground,
			final Date date) {
		this.foregroundColorValue = foregroundColorValue;
		this.backgroundColorValue = backgroundColorValue;
		this.accentColorValue = accentColorValue;
		this.whiteBackground = whiteBackground;
		this.date = date;
	}
	
	/**
	 * Instantiates an instance of user appearance.
	 * 
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @param shadows shadows
	 * @param borderRadius border radius
	 * @param date date
	 */
	public UserAppearance(final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue, final Boolean whiteBackground,
			final Boolean shadows, final Short borderRadius, final Date date) {
		this.foregroundColorValue = foregroundColorValue;
		this.backgroundColorValue = backgroundColorValue;
		this.accentColorValue = accentColorValue;
		this.whiteBackground = whiteBackground;
		this.shadows = shadows;
		this.borderRadius = borderRadius;
		this.date = date;
	}

	/**
	 * Returns the foreground color value.
	 * 
	 * @return foreground color value
	 */
	public ColorValue getForegroundColorValue() {
		return this.foregroundColorValue;
	}

	/**
	 * Sets the foreground color value.
	 * 
	 * @param foregroundColorValue foreground color value
	 */
	public void setForegroundColorValue(final ColorValue foregroundColorValue) {
		this.foregroundColorValue = foregroundColorValue;
	}

	/**
	 * Returns the background color value.
	 * 
	 * @return background color value
	 */
	public ColorValue getBackgroundColorValue() {
		return this.backgroundColorValue;
	}

	/**
	 * Sets the background color value.
	 * 
	 * @param backgroundColorValue background color value
	 */
	public void setBackgroundColorValue(final ColorValue backgroundColorValue) {
		this.backgroundColorValue = backgroundColorValue;
	}

	/**
	 * Returns the accent color value.
	 * 
	 * @return accent color value
	 */
	public ColorValue getAccentColorValue() {
		return this.accentColorValue;
	}

	/**
	 * Sets the accent color value.
	 * 
	 * @param accentColorValue accent color value
	 */
	public void setAccentColorValue(final ColorValue accentColorValue) {
		this.accentColorValue = accentColorValue;
	}

	/**
	 * Returns whether white background applies.
	 * 
	 * @return white background
	 */
	public Boolean getWhiteBackground() {
		return this.whiteBackground;
	}

	/**
	 * Sets whether white background applies.
	 * 
	 * @param whiteBackground white background
	 */
	public void setWhiteBackground(final Boolean whiteBackground) {
		this.whiteBackground = whiteBackground;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns whether shadows applies.
	 * 
	 * @return shadows
	 */
	public Boolean getShadows() {
		return this.shadows;
	}

	/**
	 * Sets whether shadows applies.
	 * 
	 * @param shadows shadows
	 */
	public void setShadows(final Boolean shadows) {
		this.shadows = shadows;
	}

	/**
	 * Returns border radius.
	 * 
	 * @return border radius
	 */
	public Short getBorderRadius() {
		return this.borderRadius;
	}

	/**
	 * Sets border radius.
	 * 
	 * @param borderRadius border radius
	 */
	public void setBorderRadius(final Short borderRadius) {
		this.borderRadius = borderRadius;
	}

	/**
	 * Returns background photo.
	 * 
	 * @return background photo
	 */
	public Photo getBackgroundPhoto() {
		return this.backgroundPhoto;
	}

	/**
	 * Sets background photo.
	 * 
	 * @param backgroundPhoto background photo
	 */
	public void setBackgroundPhoto(final Photo backgroundPhoto) {
		this.backgroundPhoto = backgroundPhoto;
	}
}