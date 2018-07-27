package omis.userpreference.service;

import java.math.BigDecimal;
import java.util.Date;

import omis.media.domain.Photo;
import omis.media.exception.PhotoExistsException;
import omis.user.domain.UserAccount;
import omis.userpreference.domain.DisplayTheme;
import omis.userpreference.domain.UserPreference;

/**
 * User preference service.
 * 
 * @author Joel Norris
 * @version 0.1.1 (May 15, 2018)
 * @since OMIS 3.0
 */
public interface UserPreferenceService {

	/**
	 * Create a new user preference for the specified user account.
	 * 
	 * @param foregroundHue foreground hue
	 * @param foregroundSaturation foreground saturation
	 * @param backgroundHue background hue
	 * @param backgroundSaturation background saturation
	 * @param accentHue accent hue
	 * @param accentSaturation accent saturation
	 * @param whiteBackground white background
	 * @param userAccount user account
	 * @return newly creates user preference
	 */
	@Deprecated
	UserPreference create(Short foregroundHue, Short foregroundSaturation,
			Short backgroundHue, Short backgroundSaturation, Short accentHue,
			Short accentSaturation, Boolean whiteBackground,
			UserAccount userAccount);
	
	/**
	 * Creates a new user preference for the specified user account.
	 * 
	 * @param foregroundHue foreground hue
	 * @param foregroundSaturation foreground saturation
	 * @param foregroundLightness foreground lightness
	 * @param foregroundOpacity foreground opacity
	 * @param backgroundHue background hue
	 * @param backgroundSaturation background saturation
	 * @param backgroundLightness background lightness
	 * @param backgroundOpacity background opacity
	 * @param accentHue accent hue
	 * @param accentSaturation accent saturation
	 * @param accentLightness accent lightness
	 * @param accentOpacity accent opacity
	 * @param whiteBackground white background
	 * @param shadows shadows
	 * @param borderRadius border radius
	 * @param backgroundPhoto background photo
	 * @param userAccount user account
	 * @return newly created user preference
	 */
	UserPreference create(Short foregroundHue, Short foregroundSaturation,
			Short foregroundLightness, BigDecimal foregroundOpacity,
			Short backgroundHue, Short backgroundSaturation,
			Short backgroundLightness, BigDecimal backgroundOpacity,
			Short accentHue, Short accentSaturation,
			Short accentLightness, BigDecimal accentOpacity,
			Boolean whiteBackground, Boolean shadows, Short borderRadius,
			DisplayTheme displayTheme, Photo backgroundPhoto,
			UserAccount userAccount);
	
	/**
	 * Edits the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param foregroundHue foreground hue
	 * @param foregroundSaturation foreground saturation
	 * @param backgroundHue background hue
	 * @param backgroundSaturation background saturation
	 * @param accentHue accent hue
	 * @param accentSaturation accent saturation
	 * @param whiteBackground white background
	 * @return updated user preference
	 */
	@Deprecated
	UserPreference update(UserPreference userPreference, Short foregroundHue,
			Short foregroundSaturation, Short backgroundHue,
			Short backgroundSaturation, Short accentHue,
			Short accentSaturation, Boolean whiteBackground);
	
	/**
	 * Updates the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param foregroundHue foreground hue
	 * @param foregroundSaturation foreground saturation
	 * @param foregroundLightness foreground lightness
	 * @param foregroundOpacity foreground opacity
	 * @param backgroundHue background hue
	 * @param backgroundSaturation background saturation
	 * @param backgroundLightness background lightness
	 * @param backgroundOpacity background opacity
	 * @param accentHue accent hue
	 * @param accentSaturation accent saturation
	 * @param accentLightness accent lightness
	 * @param accentOpacity accent opacity
	 * @param whiteBackground white background
	 * @param shadows shadows
	 * @param borderRadius border radius
	 * @return updated user preference
	 */
	UserPreference update(UserPreference userPreference,
			Short foregroundHue, Short foregroundSaturation,
			Short foregroundLightness, BigDecimal foregroundOpacity,
			Short backgroundHue, Short backgroundSaturation,
			Short backgroundLightness, BigDecimal backgroundOpacity,
			Short accentHue, Short accentSaturation,
			Short accentLightness, BigDecimal accentOpacity,
			Boolean whiteBackground, Boolean shadows, Short borderRadius,
			DisplayTheme displayTheme, Photo backgroundPhoto);

	/**
	 * Returns user preference by user account.
	 * 
	 * @return user preference
	 */
	UserPreference findByUserAccount();
	
	/**
	 * Creates a photo with the specified file name and date.
	 * 
	 * @param filename filename
	 * @param photoDate photo date
	 * @return photo
	 * @throws PhotoExistsException Thrown when a duplicate photo is found.
	 */
	Photo createPhoto(String filename, Date photoDate) throws PhotoExistsException;
	
	
	/**
	 * Removes the specified photo.
	 * 
	 * @param photo photo
	 */
	void removePhoto(Photo photo);
}