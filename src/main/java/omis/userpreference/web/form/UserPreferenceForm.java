package omis.userpreference.web.form;

import java.io.Serializable;
import java.math.BigDecimal;

import omis.media.domain.Photo;
import omis.userpreference.domain.DisplayTheme;

/**
 * User preference form.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Nov 20, 2015)
 * @since OMIS 3.0
 */
public class UserPreferenceForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short backgroundHue;
	private Short backgroundSaturation;
	private Short backgroundLightness;
	private BigDecimal backgroundOpacity;
	private Short foregroundHue;
	private Short foregroundSaturation;
	private Short foregroundLightness;
	private BigDecimal foregroundOpacity;
	private Short accentHue;
	private Short accentSaturation;
	private Short accentLightness;
	private BigDecimal accentOpacity;
	private Boolean whiteBackground;
	private Boolean shadows;
	private Short borderRadius;
	private DisplayTheme displayTheme;
	private Boolean changeBackgroundPhoto;
	private Photo backgroundPhoto;
	private byte[] backgroundPhotoData;
	
	/**
	 * Instantiates a default instance of user preference form.
	 */
	public UserPreferenceForm() {
		//Default constructor.
	}

	/**
	 * Returns the background hue.
	 * 
	 * @return background hue
	 */
	public Short getBackgroundHue() {
		return this.backgroundHue;
	}

	/**
	 * Sets the background hue.
	 * 
	 * @param backgroundHue background hue
	 */
	public void setBackgroundHue(final Short backgroundHue) {
		this.backgroundHue = backgroundHue;
	}

	/**
	 * Returns the background saturation.
	 * 
	 * @return background saturation
	 */
	public Short getBackgroundSaturation() {
		return this.backgroundSaturation;
	}

	/**
	 * Sets the background saturation.
	 * 
	 * @param backgroundSaturation background saturation
	 */
	public void setBackgroundSaturation(final Short backgroundSaturation) {
		this.backgroundSaturation = backgroundSaturation;
	}

	/**
	 * Returns the foreground hue.
	 * 
	 * @return foreground hue
	 */
	public Short getForegroundHue() {
		return this.foregroundHue;
	}

	/**
	 * Sets the foreground hue.
	 * 
	 * @param foregroundHue foreground hue
	 */
	public void setForegroundHue(final Short foregroundHue) {
		this.foregroundHue = foregroundHue;
	}

	/**
	 * Returns the foreground saturation.
	 * 
	 * @return foreground saturation
	 */
	public Short getForegroundSaturation() {
		return this.foregroundSaturation;
	}

	/**
	 * Sets the foreground saturation.
	 * 
	 * @param foregroundSaturation foreground saturation
	 */
	public void setForegroundSaturation(final Short foregroundSaturation) {
		this.foregroundSaturation = foregroundSaturation;
	}

	/**
	 * Returns the accent hue.
	 * 
	 * @return accent hue
	 */
	public Short getAccentHue() {
		return this.accentHue;
	}

	/**
	 * Sets the accent hue.
	 * 
	 * @param accentHue accent hue
	 */
	public void setAccentHue(final Short accentHue) {
		this.accentHue = accentHue;
	}

	/**
	 * Returns the accent saturation.
	 * 
	 * @return accent saturation
	 */
	public Short getAccentSaturation() {
		return this.accentSaturation;
	}

	/**
	 * Sets the accent saturation.
	 * 
	 * @param accentSaturation accent saturation
	 */
	public void setAccentSaturation(final Short accentSaturation) {
		this.accentSaturation = accentSaturation;
	}

	/**
	 * Returns the white background.
	 * 
	 * @return white background
	 */
	public Boolean getWhiteBackground() {
		return this.whiteBackground;
	}

	/**
	 * Sets the white background.
	 * 
	 * @param whiteBackground white background
	 */
	public void setWhiteBackground(final Boolean whiteBackground) {
		this.whiteBackground = whiteBackground;
	}

	/**
	 * Returns background lightness.
	 * 
	 * @return background lightness
	 */
	public Short getBackgroundLightness() {
		return this.backgroundLightness;
	}

	/**
	 * Sets background lightness.
	 * 
	 * @param backgroundLightness background lightness
	 */
	public void setBackgroundLightness(final Short backgroundLightness) {
		this.backgroundLightness = backgroundLightness;
	}

	/**
	 * Returns background opacity.
	 * 
	 * @return background opacity
	 */
	public BigDecimal getBackgroundOpacity() {
		return this.backgroundOpacity;
	}

	/**
	 * Sets background opacity.
	 * 
	 * @param backgroundOpacity background opacity
	 */
	public void setBackgroundOpacity(final BigDecimal backgroundOpacity) {
		this.backgroundOpacity = backgroundOpacity;
	}

	/**
	 * Returns foreground lightness.
	 * 
	 * @return foreground lightness
	 */
	public Short getForegroundLightness() {
		return this.foregroundLightness;
	}

	/**
	 * Sets foreground lightness.
	 * 
	 * @param foregroundLightness foreground lightness
	 */
	public void setForegroundLightness(final Short foregroundLightness) {
		this.foregroundLightness = foregroundLightness;
	}

	/**
	 * Returns foreground opacity.
	 * 
	 * @return foreground opacity
	 */
	public BigDecimal getForegroundOpacity() {
		return this.foregroundOpacity;
	}

	/**
	 * Sets foreground opacity.
	 * 
	 * @param foregroundOpacity foreground opacity
	 */
	public void setForegroundOpacity(final BigDecimal foregroundOpacity) {
		this.foregroundOpacity = foregroundOpacity;
	}

	/**
	 * Returns accent lightness.
	 * 
	 * @return accent lightness
	 */
	public Short getAccentLightness() {
		return this.accentLightness;
	}

	/**
	 * Sets accent lightness.
	 * 
	 * @param accentLightness accent lightness
	 */
	public void setAccentLightness(final Short accentLightness) {
		this.accentLightness = accentLightness;
	}

	/**
	 * Returns accent opacity.
	 * 
	 * @return accent opacity
	 */
	public BigDecimal getAccentOpacity() {
		return this.accentOpacity;
	}

	/**
	 * Sets accent opacity.
	 * 
	 * @param accentOpacity accent opacity
	 */
	public void setAccentOpacity(final BigDecimal accentOpacity) {
		this.accentOpacity = accentOpacity;
	}

	/**
	 * Returns whether shadows apply.
	 * 
	 * @return shadows
	 */
	public Boolean getShadows() {
		return this.shadows;
	}

	/**
	 * Sets whether shadows apply.
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
		return borderRadius;
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
	 * Returns display theme.
	 * 
	 * @return display theme
	 */
	public DisplayTheme getDisplayTheme() {
		return this.displayTheme;
	}

	/**
	 * Sets display theme.
	 * 
	 * @param displayTheme display theme
	 */
	public void setDisplayTheme(final DisplayTheme displayTheme) {
		this.displayTheme = displayTheme;
	}

	/**
	 * Returns background photo.
	 * 
	 * @return background photo
	 */
	public Photo getBackgroundPhoto() {
		return backgroundPhoto;
	}

	/**
	 * Sets background photo.
	 * 
	 * @param backgroundPhoto background photo
	 */
	public void setBackgroundPhoto(final Photo backgroundPhoto) {
		this.backgroundPhoto = backgroundPhoto;
	}

	/**
	 * Returns background photo data.
	 * 
	 * @return background photo data
	 */
	public byte[] getBackgroundPhotoData() {
		return this.backgroundPhotoData;
	}

	/**
	 * Sets background photo data.
	 * 
	 * @param backgroundPhotoData background photo data
	 */
	public void setBackgroundPhotoData(final byte[] backgroundPhotoData) {
		this.backgroundPhotoData = backgroundPhotoData;
	}

	/**
	 * Returns whether change background photo applies.
	 * 
	 * @return change background photo
	 */
	public Boolean getChangeBackgroundPhoto() {
		return this.changeBackgroundPhoto;
	}

	/**
	 * Sets whether change background photo applies.
	 * 
	 * @param changeBackgroundPhoto change background photo
	 */
	public void setChangeBackgroundPhoto(final Boolean changeBackgroundPhoto) {
		this.changeBackgroundPhoto = changeBackgroundPhoto;
	}
}