package omis.userpreference.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;

import omis.media.domain.Photo;
import omis.media.exception.PhotoExistsException;
import omis.media.service.delegate.PhotoDelegate;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.userpreference.dao.UserPreferenceDao;
import omis.userpreference.domain.ColorValue;
import omis.userpreference.domain.DisplayTheme;
import omis.userpreference.domain.UserPreference;
import omis.userpreference.service.UserPreferenceService;
import omis.userpreference.service.delegate.UserPreferenceDelegate;

/**
 * User preference service implementation.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Nov 20, 2015)
 * @since OMIS 3.0
 */
public class UserPreferenceServiceImpl implements UserPreferenceService {

	/* Data access objects. */
	
	private final UserPreferenceDao userPreferenceDao;
	
	/* Delegates. */
	
	private final UserAccountDelegate userAccountDelegate;
	private final UserPreferenceDelegate userPreferenceDelegate;
	private final PhotoDelegate photoDelegate;
	
	
	/**
	 * Instantiates an instance of user preference service with the specified
	 * data access object.
	 * 
	 * @param userPreferenceDao user preference data access object
	 * @param userAccountDelegate user account delegate
	 * @param userPreferenceDelegate user preference delegate
	 * @param userPreferenceInstanceFactory user preference instance factory
	 */
	public UserPreferenceServiceImpl(
			final UserPreferenceDao userPreferenceDao,
			final UserAccountDelegate userAccountDelegate,
			final UserPreferenceDelegate userPreferenceDelegate,
			final PhotoDelegate photoDelegate) {
		this.userPreferenceDao = userPreferenceDao;
		this.userAccountDelegate = userAccountDelegate;
		this.userPreferenceDelegate = userPreferenceDelegate;
		this.photoDelegate = photoDelegate;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	@Deprecated
	public UserPreference create(final Short foregroundHue,
			final Short foregroundSaturation, final Short backgroundHue,
			final Short backgroundSaturation,
			final Short accentHue, final Short accentSaturation,
			final Boolean whiteBackground, final UserAccount userAccount) {
		return this.userPreferenceDelegate.create(
				new ColorValue(foregroundHue, foregroundSaturation),
				new ColorValue(backgroundHue, backgroundSaturation),
				new ColorValue(accentHue, accentSaturation),
				whiteBackground, userAccount);
	}

	/** {@inheritDoc} */
	@Override
	public UserPreference create(final Short foregroundHue, final Short foregroundSaturation,
			final Short foregroundLightness, final BigDecimal foregroundOpacity,
			final Short backgroundHue, final Short backgroundSaturation,
			final Short backgroundLightness, final BigDecimal backgroundOpacity,
			final Short accentHue, final Short accentSaturation, final Short accentLightness,
			final BigDecimal accentOpacity, final Boolean whiteBackground, final Boolean shadows,
			final Short borderRadius, final DisplayTheme displayTheme, final Photo backgroundPhoto,
			final UserAccount userAccount) {
		return this.userPreferenceDelegate.create(
				new ColorValue(foregroundHue, foregroundSaturation, foregroundLightness, foregroundOpacity),
				new ColorValue(backgroundHue, backgroundSaturation, backgroundLightness, backgroundOpacity),
				new ColorValue(accentHue, accentSaturation, accentLightness, accentOpacity),
				whiteBackground, shadows, borderRadius, displayTheme, backgroundPhoto, userAccount);
	}

	/** {@inheritDoc} */
	@Override
	@Deprecated
	public UserPreference update(final UserPreference userPreference,
			final Short foregroundHue, final Short foregroundSaturation,
			final Short backgroundHue, final Short backgroundSaturation,
			final Short accentHue, final Short accentSaturation,
			final Boolean whiteBackground) {
		return this.userPreferenceDelegate.update(userPreference,
				new ColorValue(foregroundHue, foregroundSaturation),
				new ColorValue(backgroundHue, backgroundSaturation),
				new ColorValue(accentHue, accentSaturation),
				whiteBackground);
	}
	
	/** {@inheritDoc} */
	@Override
	public UserPreference findByUserAccount() {
		return this.userPreferenceDao.findByAccount(
				this.userAccountDelegate.findByUsername(
						SecurityContextHolder.getContext().getAuthentication()
					.getName()));
	}

	/** {@inheritDoc} */
	@Override
	public UserPreference update(final UserPreference userPreference,
			final Short foregroundHue, final Short foregroundSaturation,
			final Short foregroundLightness, final BigDecimal foregroundOpacity,
			final Short backgroundHue, final Short backgroundSaturation,
			final Short backgroundLightness, final BigDecimal backgroundOpacity,
			final Short accentHue, final Short accentSaturation,
			final Short accentLightness, final BigDecimal accentOpacity, final Boolean whiteBackground,
			final Boolean shadows, final Short borderRadius, final DisplayTheme displayTheme,
			final Photo backgroundPhoto) {
		return this.userPreferenceDelegate.update(userPreference,
				new ColorValue(foregroundHue, foregroundSaturation, foregroundLightness, foregroundOpacity),
				new ColorValue(backgroundHue, backgroundSaturation, backgroundLightness, backgroundOpacity),
				new ColorValue(accentHue, accentSaturation, accentLightness, accentOpacity),
				whiteBackground, shadows, borderRadius, displayTheme, backgroundPhoto);
	}

	/** {@inheritDoc} */
	@Override
	public Photo createPhoto(final String filename, final Date photoDate)
			throws PhotoExistsException {
		return this.photoDelegate.create(filename, photoDate);
		
	}

	/** {@inheritDoc} */
	@Override
	public void removePhoto(final Photo photo) {
		this.photoDelegate.remove(photo);
	}
}