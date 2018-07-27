package omis.userpreference.domain;

import java.io.Serializable;

import omis.media.domain.Photo;
import omis.user.domain.UserAccount;

/**
 * User preference.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Nov 19, 2015)
 * @since OMIS 3.0
 */
public interface UserPreference extends Serializable {

	/**
	 * Returns the id.
	 * 
	 * @return id
	 */
	Long getId();
	
	/**
	 * Sets the id.
	 * 
	 * @param id id
	 */
	void setId(Long id);
	
	/**
	 * Returns the foreground color value.
	 * 
	 * @return foreground color value
	 */
	ColorValue getForegroundColorValue();
	
	/**
	 * Sets the foreground color value.
	 * 
	 * @param foreGroundColorValue foreground color value
	 */
	void setForegroundColorValue(ColorValue foregroundColorValue);
	
	/**
	 * Returns the background color value.
	 * 
	 * @return background color value
	 */
	ColorValue getBackgroundColorValue();
	
	/**
	 * Sets the background color value.
	 * 
	 * @param backgroundColorValue background color value
	 */
	void setBackgroundColorValue(ColorValue backgroundColorValue);
	
	/**
	 * Returns the accent color value.
	 * 
	 * @return accent color value
	 */
	ColorValue getAccentColorValue();
	
	/**
	 * Sets the accent color value.
	 * 
	 * @param accentColorValue accent color value
	 */
	void setAccentColorValue(ColorValue accentColorValue);
	
	/**
	 * Returns whether white background applies.
	 * 
	 * @return white background
	 */
	Boolean getWhiteBackground();
	
	/**
	 * Sets whether white background applies.
	 * 
	 * @param whiteBackground white background
	 */
	void setWhiteBackground(Boolean whiteBackground);
	
	/**
	 * Return whether shadows are applicable.
	 * 
	 * @return shadows
	 */
	Boolean getShadows();
	
	/**
	 * Sets whether shadows are applicable.
	 * 
	 * @param shadows shadows
	 */
	void setShadows(Boolean shadows);
	
	/**
	 * Returns border radius.
	 * 
	 * @return border radius
	 */
	Short getBorderRadius();
	
	/**
	 * Sets border radius.
	 * 
	 * @param borderRadius border radius
	 */
	void setBorderRadius(Short borderRadius);
	
	/**
	 * Returns background photo.
	 * 
	 * @return background photo
	 */
	Photo getBackgroundPhoto();
	
	/**
	 * Sets background photo.
	 * 
	 * @param backgroundPhoto background photo
	 */
	void setBackgroundPhoto(Photo backgroundPhoto);
	
	/**
	 * Returns display theme.
	 * 
	 * @return display theme
	 */
	DisplayTheme getDisplayTheme();
	
	/**
	 * Sets display theme.
	 * 
	 * @param displayTheme display theme
	 */
	void setDisplayTheme(DisplayTheme displayTheme);
	
	/**
	 * Returns the user account.
	 * 
	 * @return user account
	 */
	UserAccount getUserAccount();
	
	/**
	 * Sets the user account.
	 * 
	 * @param userAccount user account
	 */
	void setUserAccount(UserAccount userAccount);
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);
	
	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}